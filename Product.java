package myproject;

public class Product {
    private String name;
    private int stock;
    private int sold;

    public Product(String name, int stock) {
        this.name = name;
        this.stock = stock;
        this.sold = 0;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public int getSold() {
        return sold;
    }

    public int getRemaining() {
        return stock - sold;
    }

    public void sell(int quantity) {
        if (quantity <= getRemaining()) {
            sold += quantity;
        } else {
            System.out.println("Not enough stock to sell " + quantity + " " + name);
        }
    }

    public void restock(int quantity) {
        stock += quantity;
    }
}
