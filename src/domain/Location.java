package domain;

public class Location {
    private String section;
    private int shelf;
    private int number;

    public Location() { }
    public Location(String section, int shelf, int number) {
        this.section = section;
        this.shelf = shelf;
        this.number = number;
    }
    public String getSection() { return section; }
    public int getShelf() { return shelf; }
    public int getNumber() { return number; }

    public void setSection(String section) { this.section = section; }
    public void setShelf(int shelf) { this.shelf = shelf; }
    public void setNumber(int number) { this.number = number; }

    @Override
    public String toString() {
        return section + "/" + shelf + "/" + number;
    }
}
