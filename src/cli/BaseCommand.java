package cli;

public abstract class BaseCommand implements Command {

    /**
     * Базов клас за команди. Съдържа общи помощни проверки (например дали има отворен файл).
     */

    protected void requireOpen(CommandContext ctx) {
        if (ctx == null || !ctx.hasOpenFile()) {
            throw new IllegalStateException("No open file. Use 'open <file>'.");
        }
    }
}
