package cli;

import domain.LogEntry;
import domain.LogType;
import domain.Product;
import domain.Unit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RemoveCommand extends BaseCommand {

    /**
     * Команда remove: премахва количество от продукта по FEFO (най-ранен срок първо) и логва промяната.
     */

    private double round3(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }

    @Override
    public String name() {
        return "remove";
    }

    @Override
    public String help() {
        return "remove <name> <manufacturer> <unit> <qty> [force] - FEFO removal";
    }

    @Override
    public String execute(CommandContext ctx, String[] args) {
        requireOpen(ctx);

        if (args.length < 4) return "Usage: " + help();

        try {
            String name = args[0];
            String manufacturer = args[1];
            Unit unit = Unit.valueOf(args[2].toUpperCase());

            double requested = round3(Double.parseDouble(args[3]));
            boolean force = (args.length >= 5 && args[4].equalsIgnoreCase("force"));

            if (requested <= 0) return "qty must be > 0";

            List<Product> batches = new ArrayList<>();
            for (Product p : ctx.getWarehouse().all()) {
                if (p.getUnit() == unit
                        && p.getName().equalsIgnoreCase(name)
                        && p.getManufacturer().equalsIgnoreCase(manufacturer)) {
                    batches.add(p);
                }
            }

            if (batches.isEmpty()) return "No such product in warehouse.";

            Collections.sort(batches, new Comparator<Product>() {
                @Override
                public int compare(Product a, Product b) {
                    return a.getExpiryDate().compareTo(b.getExpiryDate());
                }
            });

            double available = 0.0;
            for (Product p : batches) {
                available = round3(available + p.getQuantity());
            }

            if (available < requested && !force) {
                StringBuilder sb = new StringBuilder();
                sb.append("Not enough quantity. Requested=").append(requested)
                        .append(", available=").append(available).append(System.lineSeparator());

                sb.append("Batches (FEFO order):").append(System.lineSeparator());
                for (Product p : batches) {
                    sb.append("- exp=").append(p.getExpiryDate())
                            .append(" qty=").append(round3(p.getQuantity()))
                            .append(" loc=").append(p.getLocation())
                            .append(System.lineSeparator());
                }

                sb.append("If you want to remove the remaining, run: ")
                        .append("remove ").append(name).append(" ").append(manufacturer).append(" ")
                        .append(unit).append(" ").append(available).append(System.lineSeparator());

                sb.append("Or remove available automatically: ")
                        .append("remove ").append(name).append(" ").append(manufacturer).append(" ")
                        .append(unit).append(" ").append(requested).append(" force");

                return sb.toString().trim();
            }

            double toRemove = round3(Math.min(requested, available));
            double removed = 0.0;

            for (Product p : batches) {
                if (toRemove <= 0.0) break;

                double take = round3(Math.min(toRemove, p.getQuantity()));
                if (take <= 0.0) continue;

                p.setQuantity(round3(p.getQuantity() - take));
                removed = round3(removed + take);
                toRemove = round3(toRemove - take);

                ctx.getWarehouse().addLogInternal(new LogEntry(
                        LocalDateTime.now(),
                        LogType.REMOVE,
                        p.getName(),
                        p.getManufacturer(),
                        p.getUnit(),
                        take,
                        p.getLocation(),
                        "remove"
                ));

                if (p.getQuantity() == 0.0) {
                    ctx.getWarehouse().remove(p);
                }
            }

            ctx.markDirty();
            return "Removed: " + removed + " (requested: " + requested + ")";
        } catch (Exception e) {
            return "Invalid input. Usage: " + help();
        }
    }
}