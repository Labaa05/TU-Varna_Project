package cli;

import java.util.Collection;

/**
 * Команда help: извежда списък с поддържаните команди и кратко описание.
 */
public class HelpCommand extends BaseCommand {
    private final Collection<Command> all;

    public HelpCommand(Collection<Command> all) {
        this.all = all;
    }

    @Override public String name() { return "help"; }
    @Override public String help() { return "help - list commands"; }

    @Override
    public String execute(CommandContext ctx, String[] args) {
        StringBuilder sb = new StringBuilder();
        for (Command c : all) sb.append(c.help()).append(System.lineSeparator());
        return sb.toString().trim();
    }
}