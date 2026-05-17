package cli;

import infra.WarehouseFileHandler;

public class SaveCommand extends BaseCommand {

    /**
     ** Команда save: ... Поддържа и {@code save as <file>} за запис в друг файл. *
     */

    @Override public String name() { return "save"; }
    @Override public String help() { return "save | save as <file>  (file path must not contain spaces)"; }

    @Override
    public String execute(CommandContext ctx, String[] args) throws Exception {
        requireOpen(ctx);

        if (args.length == 0) {
            WarehouseFileHandler.save(ctx.getCurrentFile(), ctx.getWarehouse());
            ctx.markClean();
            return "Saved.";
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("as")) {
            WarehouseFileHandler.save(args[1], ctx.getWarehouse());
            ctx.open(args[1], ctx.getWarehouse());
            ctx.markClean();
            return "Saved as.";
        }

        return "Usage: save  OR  save as <file>";
    }
}