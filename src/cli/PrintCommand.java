package cli;

import domain.Product;

public class PrintCommand extends BaseCommand {

    /**
     * Команда print: извежда всички налични продукти в склада.
     */

    @Override public String name() { return "print"; }
    @Override public String help() { return "print - list all products"; }

    @Override
    public String execute(CommandContext ctx, String[] args) {
        requireOpen(ctx);

        if (ctx.getWarehouse().all().isEmpty()) {
            return "Warehouse is empty.";
        }

        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Product p : ctx.getWarehouse().all()) {
            sb.append(i++).append(") ").append(p.toString()).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}