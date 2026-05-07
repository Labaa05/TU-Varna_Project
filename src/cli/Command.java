package cli;

public interface Command {
    String name();
    String help();
    String execute(CommandContext ctx, String[] args) throws Exception;
}
