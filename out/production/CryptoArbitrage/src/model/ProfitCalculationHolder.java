package model;

public class ProfitCalculationHolder implements Comparable{

    private double buyPrice = 0;
    private double sellPrice = 0;
    private double profit = 0;
    private double volume = 0;

    @Override
    public int compareTo(Object o) {
        ProfitCalculationHolder hold = (ProfitCalculationHolder)o;
        if(this.profit > hold.getProfit()) {
            return -1;
        } else if(this.profit < hold.getProfit()) {
            return 1;
        }
        return 0;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }


}
