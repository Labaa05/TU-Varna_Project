package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Склад (агрегат) – съдържа списък с продукти и лог на промените.
 */
public class Warehouse {
    private final List<Product> products = new ArrayList<>();
    private final List<LogEntry> log = new ArrayList<>();

    public void add(Product p) {
        if (p == null) throw new IllegalArgumentException("product is null");
        products.add(p);
    }

    public boolean remove(Product p) {
        return products.remove(p);
    }

    public List<Product> all() {
        return Collections.unmodifiableList(products);
    }

    public List<LogEntry> log() {
        return Collections.unmodifiableList(log);
    }

    public void addLogInternal(LogEntry e) {
        if (e == null) throw new IllegalArgumentException("log entry is null");
        log.add(e);
    }
}