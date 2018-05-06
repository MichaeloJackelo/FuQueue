package fq.fuqueue;

class Product {
    String name;
    double price;
    String description;
    int quantity;
    int barcode;
    public Product(String name, double price, String description,int quantity,int barcode){
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = 1;
        this.barcode = barcode;
    }
}
