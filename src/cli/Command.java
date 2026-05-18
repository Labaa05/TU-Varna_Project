package cli;

/**
 * Интерфейс за команда в CLI. Всяка команда има име, описание и изпълнение.
 */
public interface Command {
    String name();
    String help();
    String execute(CommandContext ctx, String[] args) throws Exception;
}
