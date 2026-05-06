package domain;

import java.time.LocalDate;

public class Product {
    private String name;
    private String manufacturer;
    private Unit unit;
    private double quantity;

    private LocalDate expiryDate;
    private LocalDate arrivalDate;

    private Location location;
    private String comment;

    public Product() { }

    public Product(String name,
                   String manufacturer,
                   Unit unit,
                   double quantity,
                   LocalDate expiryDate,
                   LocalDate arrivalDate,
                   Location location,
                   String comment) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.unit = unit;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.arrivalDate = arrivalDate;
        this.location = location;
        this.comment = comment;
    }

    public String getName() { return name; }
    public String getManufacturer() { return manufacturer; }
    public Unit getUnit() { return unit; }
    public double getQuantity() { return quantity; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public LocalDate getArrivalDate() { return arrivalDate; }
    public Location getLocation() { return location; }
    public String getComment() { return comment; }

    public void setName(String name) { this.name = name; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public void setUnit(Unit unit) { this.unit = unit; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public void setArrivalDate(LocalDate arrivalDate) { this.arrivalDate = arrivalDate; }
    public void setLocation(Location location) { this.location = location; }
    public void setComment(String comment) { this.comment = comment; }

    @Override
    public String toString() {
        return name + " | " + manufacturer + " | " + unit +
                " | qty=" + quantity +
                " | exp=" + expiryDate +
                " | in=" + arrivalDate +
                " | loc=" + location +
                (comment == null || comment.trim().isEmpty() ? "" : " | " + comment);
    }
}