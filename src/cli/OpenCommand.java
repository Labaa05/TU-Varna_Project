package cli;

import domain.Warehouse;
import infra.WarehouseFileHandler;

import java.io.File;

public class OpenCommand extends BaseCommand {
    @Override public String name() { return "open"; }
    @Override public String help() { return "open <file> - open or create file"; }

    @Override
    public String execute(CommandContext ctx, String[] args) throws Exception {
        if (args.length != 1) return "Usage: open <file>";

        File f = new File(args[0]);
        Warehouse wh = WarehouseFileHandler.load(f.getPath());
        ctx.open(f.getPath(), wh);

        return "Successfully opened " + f.getName();
    }
}
