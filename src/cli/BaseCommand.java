package cli;

/**
 * Базов клас за команди. Съдържа общи помощни проверки (например дали има отворен файл).
 */
public abstract class BaseCommand implements Command {
    protected void requireOpen(CommandContext ctx) {
        if (ctx == null || !ctx.hasOpenFile()) {
            throw new IllegalStateException("No open file. Use 'open <file>'.");
        }
    }
}
