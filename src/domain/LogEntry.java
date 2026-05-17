package domain;

import java.time.LocalDateTime;

public class LogEntry {

    /**
     * Запис в лога за промяна в склада (ADD/REMOVE/CLEAN) с дата/час, продукт и количество.
     */

    private LocalDateTime timestamp;
    private LogType type;

    private String name;
    private String manufacturer;
    private Unit unit;

    private double quantity;
    private Location location;
    private String note;

    public LogEntry() { }

    public LogEntry(LocalDateTime timestamp, LogType type,
                    String name, String manufacturer, Unit unit,
                    double quantity, Location location, String note) {
        this.timestamp = timestamp;
        this.type = type;
        this.name = name;
        this.manufacturer = manufacturer;
        this.unit = unit;
        this.quantity = quantity;
        this.location = location;
        this.note = note;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public LogType getType() { return type; }
    public String getName() { return name; }
    public String getManufacturer() { return manufacturer; }
    public Unit getUnit() { return unit; }
    public double getQuantity() { return quantity; }
    public Location getLocation() { return location; }
    public String getNote() { return note; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setType(LogType type) { this.type = type; }
    public void setName(String name) { this.name = name; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public void setUnit(Unit unit) { this.unit = unit; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public void setLocation(Location location) { this.location = location; }
    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(fmt) + " | " + type + " | " +
                name + " | " + manufacturer + " | " + unit +
                " | qty=" + quantity +
                " | loc=" + location +
                (note == null || note.trim().isEmpty() ? "" : " | " + note);
    }
}
