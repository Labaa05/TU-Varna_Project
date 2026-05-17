package infra;

import domain.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WarehouseFileHandler {

    /**
     * Четене/запис на склада във файл в текстов формат (редове, разделени със ';').
     * Поддържа зареждане (load) при open и запис (save) при save/save as.
     */


    public static void save(String path, Warehouse wh) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("WAREHOUSE_V1");

        for (Product p : wh.all()) {
            lines.add(
                    "P;" +
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

        for (LogEntry e : wh.log()) {
            lines.add(
                    "L;" +
                            e.getTimestamp() + ";" +
                            e.getType() + ";" +
                            e.getName() + ";" +
                            e.getManufacturer() + ";" +
                            e.getUnit() + ";" +
                            e.getQuantity() + ";" +
                            e.getLocation().getSection() + ";" +
                            e.getLocation().getShelf() + ";" +
                            e.getLocation().getNumber() + ";" +
                            (e.getNote() == null ? "" : e.getNote())
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

            if (line.startsWith("L;")) {
                String[] a = line.split(";", -1);
                if (a.length < 11) continue;

                LogEntry le = new LogEntry(
                        LocalDateTime.parse(a[1]),
                        LogType.valueOf(a[2]),
                        a[3],
                        a[4],
                        Unit.valueOf(a[5]),
                        Double.parseDouble(a[6]),
                        new Location(a[7], Integer.parseInt(a[8]), Integer.parseInt(a[9])),
                        a[10].isEmpty() ? null : a[10]
                );
                wh.addLogInternal(le);
                continue;
            }

            if (line.startsWith("P;")) {
                String[] a = line.split(";", -1);
                if (a.length < 11) continue;

                Product pr = new Product(
                        a[1],
                        a[2],
                        Unit.valueOf(a[3]),
                        Double.parseDouble(a[4]),
                        LocalDate.parse(a[5]),
                        LocalDate.parse(a[6]),
                        new Location(a[7], Integer.parseInt(a[8]), Integer.parseInt(a[9])),
                        a[10].isEmpty() ? null : a[10]
                );
                wh.add(pr);
            }
        }
        return wh;
    }
}