package cli;

/**
 * Команда close: затваря текущия файл и изчиства заредените данни от паметта (без запис).
 */
public class CloseCommand extends BaseCommand {
    @Override public String name() { return "close"; }
    @Override public String help() { return "close - close current file"; }

    @Override
    public String execute(CommandContext ctx, String[] args) {
        requireOpen(ctx);
        ctx.close();
        return "Successfully closed file.";
    }
}
