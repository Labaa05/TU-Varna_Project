package app;

import cli.*;

import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        CommandFactory.build().run();
    }
}
