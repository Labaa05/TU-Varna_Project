package app;

import cli.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Сглобява и регистрира всички команди в приложението (bootstrapping).
 * Връща готов CommandLoop, който може да бъде стартиран от Application.
 */
public class CommandFactory {
    public static CommandLoop build() {

        CommandContext ctx = new CommandContext();
        CommandLoop loop = new CommandLoop(ctx);

        List<Command> all = new ArrayList<>();

        all.add(new OpenCommand());
        all.add(new CloseCommand());
        all.add(new SaveCommand());
        all.add(new ExitCommand());

        all.add(new PrintCommand());
        all.add(new AddCommand());
        all.add(new LogCommand());
        all.add(new RemoveCommand());
        all.add(new CleanCommand());
        all.add(new LossesCommand());

        all.add(new HelpCommand(all));

        for (Command c : all) loop.register(c);

        return loop;
    }
}
