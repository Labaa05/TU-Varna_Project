package cli;

import domain.Warehouse;

public class CommandContext {
    private String currentFile;
    private Warehouse warehouse;

    public boolean hasOpenFile() {
        return currentFile != null && warehouse != null;
    }

    public String getCurrentFile() { return currentFile; }
    public Warehouse getWarehouse() { return warehouse; }

    public void open(String file, Warehouse wh) {
        this.currentFile = file;
        this.warehouse = wh;
    }

    public void close() {
        this.currentFile = null;
        this.warehouse = null;
    }
}
