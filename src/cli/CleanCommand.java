package cli;

import domain.LogEntry;
import domain.LogType;
import domain.Product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Команда clean: премахва изтекли и/или скоро изтичащи продукти и записва премахването в лога.
 */
public class CleanCommand extends BaseCommand {
    @Override public String name() { return "clean"; }
    @Override public String help() { return "clean [days] - remove expired and soon products"; }

    @Override
    public String execute(CommandContext ctx, String[] args) {
        requireOpen(ctx);

        int days = 0;
        try {
            if (args.length == 1) days = Integer.parseInt(args[0]);
            else if (args.length != 0) return "Usage: " + help();
            if (days < 0) return "days must be >= 0";
        } catch (Exception e) {
            return "Usage: " + help();
        }

        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plusDays(days);

        List<Product> toDelete = new ArrayList<>();
        for (Product p : ctx.getWarehouse().all()) {
            if (!p.getExpiryDate().isAfter(threshold)) { // expiry <= threshold
                toDelete.add(p);
            }
        }

        if (toDelete.isEmpty()) return "Nothing to clean.";

        StringBuilder sb = new StringBuilder();
        for (Product p : toDelete) {
            String reason = (!p.getExpiryDate().isAfter(today)) ? "expired" : "soon";

            ctx.getWarehouse().addLogInternal(new LogEntry(
                    LocalDateTime.now(),
                    LogType.CLEAN,
                    p.getName(),
                    p.getManufacturer(),
                    p.getUnit(),
                    p.getQuantity(),
                    p.getLocation(),
                    reason
            ));

            ctx.getWarehouse().remove(p);
        }

        ctx.markDirty();
        return "Cleaned products: " + toDelete.size();
    }
}