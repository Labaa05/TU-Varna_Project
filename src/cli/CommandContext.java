package cli;

import domain.Warehouse;

/**
 * Контекст на приложението (сесия). Пази текущо отворения файл, склада в паметта и флаг за незаписани промени.
 */
public class CommandContext {
    private String currentFile;
    private Warehouse warehouse;
    private boolean dirty;
    private boolean exitRequested;

    public boolean hasOpenFile() {
        return currentFile != null && warehouse != null;
    }

    public String getCurrentFile() { return currentFile; }
    public Warehouse getWarehouse() { return warehouse; }
    public boolean isDirty() { return dirty; }

    public boolean isExitRequested() { return exitRequested; }
    public void requestExit() { this.exitRequested = true; }

    public void open(String file, Warehouse wh) {
        this.currentFile = file;
        this.warehouse = wh;
        this.dirty = false;
    }

    public void close() {
        this.currentFile = null;
        this.warehouse = null;
        this.dirty = false;
    }

    public void markDirty() { this.dirty = true; }
    public void markClean() { this.dirty = false; }
}