package cli;

import domain.LogEntry;
import domain.LogType;
import domain.Unit;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LossesCommand extends BaseCommand {

    /**
     * Команда losses: изчислява загубите за период по продукт и цена на база изхвърлените (expired) количества.
     */

    @Override public String name() { return "losses"; }

    @Override
    public String help() {
        return "losses <name> <manufacturer> <unit> <price> <from> <to> - expired losses";
    }

    @Override
    public String execute(CommandContext ctx, String[] args) {
        requireOpen(ctx);
        if (args.length != 6) return "Usage: " + help();

        try {
            String name = args[0];
            String manufacturer = args[1];
            Unit unit = Unit.valueOf(args[2].toUpperCase());
            double price = Double.parseDouble(args[3]);

            LocalDate fromD = LocalDate.parse(args[4]);
            LocalDate toD = LocalDate.parse(args[5]);

            LocalDateTime from = fromD.atStartOfDay();
            LocalDateTime to = toD.plusDays(1).atStartOfDay().minusNanos(1);

            double expiredQty = 0.0;

            for (LogEntry e : ctx.getWarehouse().log()) {
                if (e.getType() != LogType.CLEAN) continue;
                if (e.getNote() == null || !e.getNote().equalsIgnoreCase("expired")) continue;

                if (!e.getName().equalsIgnoreCase(name)) continue;
                if (!e.getManufacturer().equalsIgnoreCase(manufacturer)) continue;
                if (e.getUnit() != unit) continue;

                if (e.getTimestamp().isBefore(from) || e.getTimestamp().isAfter(to)) continue;

                expiredQty += e.getQuantity();
            }

            double total = expiredQty * price;
            return String.format("Losses: expiredQty=%.3f * price=%.2f = %.2f", expiredQty, price, total);
        } catch (Exception e) {
            return "Usage: " + help();
        }
    }
}
