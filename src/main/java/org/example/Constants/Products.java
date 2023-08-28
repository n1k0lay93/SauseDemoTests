package org.example.Constants;

public enum Products {
    BACKPACK("Sauce Labs Backpack", "$29.99"),
    T_SHIRT("Sauce Labs Bolt T-Shirt", "$15.99"),
    LONGSLEEVE_SHIRT("Test.allTheThings() T-Shirt (Red)", "$15.99"),
    BIKE_LIGHT("Sauce Labs Bike Light", "$9.99"),
    FLEECE_JACKET("Sauce Labs Fleece Jacket", "$49.99"),
    ONESIE("Sauce Labs Onesie", "$7.99");

    public final String label;
    public final String price;

    private Products(String label, String price) {
        this.label = label;
        this.price = price;
    }

    public String getLabel() {
        return label;
    }

    public String getPrice() {
        return price;
    }
}