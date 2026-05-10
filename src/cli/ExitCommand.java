package cli;

public class ExitCommand extends BaseCommand {
    @Override public String name() { return "exit"; }
    @Override public String help() { return "exit - quit program"; }

    @Override
    public String execute(CommandContext ctx, String[] args) {
        ctx.requestExit();
        return "Bye.";
    }
}