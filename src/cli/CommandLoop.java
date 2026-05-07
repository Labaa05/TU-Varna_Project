package cli;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandLoop {
    private final Map<String, Command> commands = new HashMap<>();
    private final CommandContext ctx;

    public CommandLoop(CommandContext ctx) {
        this.ctx = ctx;
    }

    public void register(Command c) {
        commands.put(c.name().toLowerCase(), c);
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Warehouse CLI. Type 'help'.");

        while (true) {
            System.out.print("> ");
            if (!sc.hasNextLine()) break;

            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String cmdName = parts[0].toLowerCase();

            String[] args = new String[Math.max(0, parts.length - 1)];
            for (int i = 0; i < args.length; i++) args[i] = parts[i + 1];

            Command cmd = commands.get(cmdName);
            if (cmd == null) {
                System.out.println("Unknown command. Type 'help'.");
                continue;
            }

            try {
                String msg = cmd.execute(ctx, args);
                if (msg != null && !msg.isEmpty()) System.out.println(msg);

                if ("exit".equals(cmdName)) break;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Bye.");
    }
}
