package scratch;

public class PriceHolder {

    double price = 0;
    double volume = 0;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "PriceHolder{" +
                "price=" + price +
                ", volume=" + volume +
                '}';
    }
}
