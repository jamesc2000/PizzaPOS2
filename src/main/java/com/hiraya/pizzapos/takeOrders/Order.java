package com.hiraya.pizzapos.takeOrders;

public class Order {
    public String name;
    public String size;
    public int quantity = 1;
    public Double price;
    @Override
    public String toString() {
        return "Order [name=" + name + ", price=" + price + ", quantity=" + quantity + ", size=" + size + "]";
    }
}
