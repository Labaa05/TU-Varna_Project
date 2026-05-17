package cli;

public interface Command {

    /**
     * Интерфейс за команда в CLI. Всяка команда има име, описание и изпълнение.
     */

    String name();
    String help();
    String execute(CommandContext ctx, String[] args) throws Exception;
}
