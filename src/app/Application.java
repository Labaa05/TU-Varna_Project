package app;

import cli.*;

import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        CommandContext ctx = new CommandContext();
        CommandLoop loop = new CommandLoop(ctx);

        List<Command> all = new ArrayList<>();

        all.add(new OpenCommand());
        all.add(new CloseCommand());
        all.add(new SaveCommand());
        all.add(new ExitCommand());

        all.add(new HelpCommand(all));

        for (Command c : all) {
            loop.register(c);
        }

        loop.run();
    }
}
