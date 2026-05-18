package app;

import cli.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Входна точка на приложението. Създава и регистрира командите и стартира CLI цикъла.
 */
public class Application {
    public static void main(String[] args) {
        CommandFactory.build().run();
    }
}
