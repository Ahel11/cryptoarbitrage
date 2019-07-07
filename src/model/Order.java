package model;

import java.text.DecimalFormat;

public class Order {

    public static final String ASK = "ASK";
    public static final String BID = "BID";

    private double volume;
    private double price;
    private String orderType;
    private DecimalFormat format = new DecimalFormat("#.#########");

    public Order() {
        this.orderType = "";
        this.volume = 0;
        this.price = 0;
    }


    public static String getASK() {
        return ASK;
    }

    public static String getBID() {
        return BID;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "Order{" +
                "volume=" + format.format(volume) +
                ", price=" + format.format(price)+
                ", orderType='" + orderType + '\'' +
                '}';
    }
}
