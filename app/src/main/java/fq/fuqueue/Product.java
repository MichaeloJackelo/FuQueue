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
    public Product(Product new_product){
        this.name = new_product.name;
        this.price = new_product.price;
        this.description = new_product.description;
        this.quantity = 1;
        this.barcode = new_product.barcode;
    }
}
