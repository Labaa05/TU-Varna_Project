package service;

import domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Слой с бизнес логика за операциите върху склада (add/remove/clean/log).
 * Изпълнява правилата от условието, без да се занимава с конзолен вход/изход.
 */

public class WarehouseService {
    private double round3(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }

    private boolean sameText(String a, String b) {
        if (a == null || b == null) return false;
        return a.equalsIgnoreCase(b);
    }

    private boolean matches(Product p, String name, String manufacturer, Unit unit) {
        return sameText(p.getName(), name)
                && sameText(p.getManufacturer(), manufacturer)
                && p.getUnit() == unit;
    }

    /**
     * Връща списък с продуктите, сортиран за по-лесно извеждане (име, после срок).
     *
     * @param wh складът в паметта
     * @return сортиран списък с продукти
     */
    public List<Product> listAllSorted(Warehouse wh) {
        if (wh == null) throw new IllegalArgumentException("warehouse is null");

        List<Product> copy = new ArrayList<>(wh.all());
        Collections.sort(copy, new Comparator<Product>() {
            @Override
            public int compare(Product a, Product b) {
                int byName = a.getName().compareToIgnoreCase(b.getName());
                if (byName != 0) return byName;
                return a.getExpiryDate().compareTo(b.getExpiryDate());
            }
        });
        return copy;
    }

    /**
     * Добавя продукт в склада и записва операцията в лога.
     *
     * @param wh складът в паметта
     * @param p продукт/партида за добавяне
     */
    public void add(Warehouse wh, Product p) {
        if (wh == null) throw new IllegalArgumentException("warehouse is null");
        if (p == null) throw new IllegalArgumentException("product is null");

        validateProduct(p);

        p.setQuantity(round3(p.getQuantity()));
        wh.add(p);

        wh.addLogInternal(new LogEntry(LocalDateTime.now(), LogType.ADD,
                p.getName(), p.getManufacturer(), p.getUnit(),
                p.getQuantity(), p.getLocation(), "add"));
    }

    /**
     * Премахва количество от продукт по FEFO правило (най-ранен срок първо) и логва операцията.
     *
     * @param wh складът в паметта
     * @param name име на продукта
     * @param manufacturer производител
     * @param unit мерна единица
     * @param qty количество за премахване
     * @return реално премахнатото количество
     */
    public double remove(Warehouse wh, String name, String manufacturer, Unit unit, double qty) {
        if (wh == null) throw new IllegalArgumentException("warehouse is null");
        if (unit == null) throw new IllegalArgumentException("unit is null");
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");

        double toRemove = round3(qty);

        List<Product> batches = new ArrayList<>();
        for (Product p : wh.all()) {
            if (matches(p, name, manufacturer, unit)) {
                batches.add(p);
            }
        }

        Collections.sort(batches, new Comparator<Product>() {
            @Override
            public int compare(Product a, Product b) {
                return a.getExpiryDate().compareTo(b.getExpiryDate());
            }
        });

        double available = 0.0;
        for (Product p : batches) {
            available = round3(available + p.getQuantity());
        }

        if (available < toRemove) {
            throw new IllegalArgumentException("Not enough quantity. Requested=" + toRemove + ", available=" + available);
        }

        double removed = 0.0;

        for (Product p : batches) {
            if (toRemove <= 0.0) break;

            double take = p.getQuantity() < toRemove ? p.getQuantity() : toRemove;
            take = round3(take);

            p.setQuantity(round3(p.getQuantity() - take));
            removed = round3(removed + take);
            toRemove = round3(toRemove - take);

            wh.addLogInternal(new LogEntry(LocalDateTime.now(), LogType.REMOVE,
                    p.getName(), p.getManufacturer(), p.getUnit(),
                    take, p.getLocation(), "remove"));

            if (p.getQuantity() == 0.0) {
                wh.remove(p);
            }
        }

        return removed;
    }

    /**
     * Премахва продукти с expiryDate <= today + soonDays и логва премахването.
     *
     * @param wh складът в паметта
     * @param soonDays 0 = само изтекли, >0 = изтекли + скоро изтичащи
     * @return брой премахнати записи
     */
    public int clean(Warehouse wh, int soonDays) {
        if (wh == null) throw new IllegalArgumentException("warehouse is null");
        if (soonDays < 0) throw new IllegalArgumentException("soonDays must be >= 0");

        LocalDate threshold = LocalDate.now().plusDays(soonDays);

        List<Product> toDelete = new ArrayList<>();
        for (Product p : wh.all()) {
            if (!p.getExpiryDate().isAfter(threshold)) { // expiry <= threshold
                toDelete.add(p);
            }
        }

        for (Product p : toDelete) {
            wh.addLogInternal(new LogEntry(LocalDateTime.now(), LogType.CLEAN,
                    p.getName(), p.getManufacturer(), p.getUnit(),
                    p.getQuantity(), p.getLocation(), "clean"));
            wh.remove(p);
        }

        return toDelete.size();
    }

    /**
     * Връща лог зSаписи в период [from, to].
     *
     * @param wh складът в паметта
     * @param from начална дата/час (включително)
     * @param to крайна дата/час (включително)
     * @return списък с лог записи в периода
     */
    public List<LogEntry> logBetween(Warehouse wh, LocalDateTime from, LocalDateTime to) {
        if (wh == null) throw new IllegalArgumentException("warehouse is null");
        if (from == null || to == null) throw new IllegalArgumentException("from/to is required");
        if (to.isBefore(from)) throw new IllegalArgumentException("to must be >= from");

        List<LogEntry> out = new ArrayList<>();
        for (LogEntry e : wh.log()) {
            if (!e.getTimestamp().isBefore(from) && !e.getTimestamp().isAfter(to)) {
                out.add(e);
            }
        }
        return out;
    }

    private void validateProduct(Product p) {
        if (p.getName() == null || p.getName().trim().isEmpty()) throw new IllegalArgumentException("name is required");
        if (p.getManufacturer() == null || p.getManufacturer().trim().isEmpty()) throw new IllegalArgumentException("manufacturer is required");
        if (p.getUnit() == null) throw new IllegalArgumentException("unit is required");
        if (p.getQuantity() <= 0) throw new IllegalArgumentException("quantity must be > 0");
        if (p.getExpiryDate() == null) throw new IllegalArgumentException("expiryDate is required");
        if (p.getArrivalDate() == null) throw new IllegalArgumentException("arrivalDate is required");
        if (p.getLocation() == null) throw new IllegalArgumentException("location is required");
    }
}
