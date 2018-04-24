package Models;

public class Product {
    private long barcode;
    private String description;
    private String file_name;
    private long id;
    private String name;
    private double prize;

    public long getBarcode() {
        return barcode;
    }

    public String getDescription() {
        return description;
    }

    public String getFile_name() {
        return file_name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrize() {
        return prize;
    }

    public Product(long barcode, String description, String file_name, long id, String name, long prize) {
        this.barcode = barcode;
        this.description = description;
        this.file_name = file_name;
        this.id = id;
        this.name = name;
        this.prize = prize;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }
}