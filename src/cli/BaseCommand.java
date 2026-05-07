package cli;

public class BaseCommand {
    protected void requireOpen(CommandContext ctx) {
        if (!ctx.hasOpenFile()) {
            throw new IllegalStateException("No open file. Use 'open <file>'.");
        }
    }
}
