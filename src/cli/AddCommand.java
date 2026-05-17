package cli;

import domain.Location;
import domain.LogEntry;
import domain.LogType;
import domain.Product;
import domain.Unit;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AddCommand extends BaseCommand {

    /**
     * Команда add: добавя продукт/партида в склада и записва промяната в лога.
     */

    @Override public String name() { return "add"; }

    @Override
    public String help() {
        return "add <name> <manufacturer> <unit> <qty> <expiry> <arrival> <section> <shelf> <number> [comment]";
    }

    private double round3(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }

    @Override
    public String execute(CommandContext ctx, String[] args) {
        requireOpen(ctx);

        if (args.length < 9) return "Usage: " + help();

        try {
            String name = args[0];
            String manufacturer = args[1];

            Unit unit = Unit.valueOf(args[2].toUpperCase());

            double qty = round3(Double.parseDouble(args[3]));

            LocalDate expiry = LocalDate.parse(args[4]);
            LocalDate arrival = LocalDate.parse(args[5]);

            String section = args[6];
            int shelf = Integer.parseInt(args[7]);
            int number = Integer.parseInt(args[8]);

            String comment = null;
            if (args.length >= 10) {
                StringBuilder sb = new StringBuilder();
                for (int i = 9; i < args.length; i++) {
                    if (i > 9) sb.append(" ");
                    sb.append(args[i]);
                }
                comment = sb.toString();
            }

            Product p = new Product(
                    name, manufacturer, unit, qty,
                    expiry, arrival,
                    new Location(section, shelf, number),
                    comment
            );

            ctx.getWarehouse().add(p);

            ctx.getWarehouse().addLogInternal(new LogEntry(
                    LocalDateTime.now(),
                    LogType.ADD,
                    p.getName(),
                    p.getManufacturer(),
                    p.getUnit(),
                    p.getQuantity(),
                    p.getLocation(),
                    "add"
            ));

            ctx.markDirty();
            return "Added.";
        } catch (Exception e) {
            return "Invalid input: " + e.getClass().getSimpleName() + " - " + e.getMessage()
                    + "\nUsage: " + help();
        }
    }
}