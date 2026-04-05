package dev.sdras.api.core.product;

public class Product {
    public int productId;
    public String name;
    public int weight;
    public String serviceAddress;

    public Product() {
        this.serviceAddress = "";
        productId = 0;
        name = null;
        weight = 0;
    }

    public Product(int productId, String name, int weight, String serviceAddress) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
        this.serviceAddress = serviceAddress;
    }

    public static Product createProduct() {
        return new Product();
    }
}
