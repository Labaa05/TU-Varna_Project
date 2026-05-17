package cli;

import domain.LogEntry;

import java.time.LocalDate;

public class LogCommand extends BaseCommand {

    /**
     * Команда log: извежда лога на промените (по желание в период).
     */

    @Override public String name() { return "log"; }
    @Override public String help() { return "log [from to] - show log (YYYY-MM-DD)"; }

    @Override
    public String execute(CommandContext ctx, String[] args) {
        requireOpen(ctx);

        LocalDate from = null;
        LocalDate to = null;

        if (args.length == 0) {

        } else if (args.length == 2) {
            from = LocalDate.parse(args[0]);
            to = LocalDate.parse(args[1]);
        } else {
            return "Usage: log  OR  log <from> <to> (YYYY-MM-DD)";
        }

        if (ctx.getWarehouse().log().isEmpty()) return "No log entries.";

        StringBuilder sb = new StringBuilder();
        for (LogEntry e : ctx.getWarehouse().log()) {
            LocalDate d = e.getTimestamp().toLocalDate();
            if (from != null) {
                if (d.isBefore(from) || d.isAfter(to)) continue;
            }
            sb.append(e.toString()).append(System.lineSeparator());
        }

        String out = sb.toString().trim();
        return out.isEmpty() ? "No log entries in range." : out;
    }
}