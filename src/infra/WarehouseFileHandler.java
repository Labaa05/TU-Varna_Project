package infra;

import domain.Location;
import domain.Product;
import domain.Unit;
import domain.Warehouse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WarehouseFileHandler {

    public static void save(String path, Warehouse wh) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("WAREHOUSE_V1");

        for (Product p : wh.all()) {
            lines.add(
                    p.getName() + ";" +
                            p.getManufacturer() + ";" +
                            p.getUnit() + ";" +
                            p.getQuantity() + ";" +
                            p.getExpiryDate() + ";" +
                            p.getArrivalDate() + ";" +
                            p.getLocation().getSection() + ";" +
                            p.getLocation().getShelf() + ";" +
                            p.getLocation().getNumber() + ";" +
                            (p.getComment() == null ? "" : p.getComment())
            );
        }

        Files.write(Paths.get(path), lines, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static Warehouse load(String path) throws IOException {
        Path p = Paths.get(path);

        if (!Files.exists(p)) {
            Path parent = p.getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.createFile(p);
            return new Warehouse();
        }

        List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
        Warehouse wh = new Warehouse();

        for (String line : lines) {
            if (line.trim().isEmpty() || line.equals("WAREHOUSE_V1")) continue;

            String[] a = line.split(";", -1);
            if (a.length < 10) continue;

            Product pr = new Product(
                    a[0],
                    a[1],
                    Unit.valueOf(a[2]),
                    Double.parseDouble(a[3]),
                    LocalDate.parse(a[4]),
                    LocalDate.parse(a[5]),
                    new Location(a[6], Integer.parseInt(a[7]), Integer.parseInt(a[8])),
                    a[9].isEmpty() ? null : a[9]
            );
            wh.add(pr);
        }

        return wh;
    }
}