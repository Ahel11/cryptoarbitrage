package model;

import java.text.DecimalFormat;

public class ArbitrageOppurtunity implements Comparable{

    private String fromExchange;
    private String toExchange;
    private String pairType;
    private double buyPrice;
    private double sellPrice;
    private double profitPerc;
    private double maxVolume;
    private String space = "         ";
    DecimalFormat format =  new DecimalFormat("#.##########");

    public ArbitrageOppurtunity() {
        this.fromExchange ="";
        this.toExchange="";
        this.pairType = "";
        this.buyPrice = 0;
        this.sellPrice = 0;
        this.profitPerc = 0;
        this.maxVolume = 0;
    }

    public String getFromExchange() {
        return fromExchange;
    }

    public void setFromExchange(String fromExchange) {
        this.fromExchange = fromExchange;
    }

    public String getToExchange() {
        return toExchange;
    }

    public void setToExchange(String toExchange) {
        this.toExchange = toExchange;
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

    public double getProfitPerc() {
        return profitPerc;
    }

    public void setProfitPerc(double profitPerc) {
        this.profitPerc = profitPerc;
    }

    public double getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(double maxVolume) {
        this.maxVolume = maxVolume;
    }

    public String getPairType() {
        return pairType;
    }

    public void setPairType(String pairType) {
        this.pairType = pairType;
    }

    @Override
    public String toString() {
        return "fromExchange:\t\t" + fromExchange +
                "\t\ttoExchange:\t\t" + toExchange  +
                "\t\t\tbuyPrice:\t\t" + format.format(buyPrice)+
                "\t\t\tsellPrice:\t\t" + format.format(sellPrice )+
                "\t\tprofitPerc:\t\t" + format.format(profitPerc)+
                "\t\tmaxVolume:\t\t" + maxVolume +
                "\t\tPair:\t\t" + this.pairType;
    }



    @Override
    public int compareTo(Object o) {
        ArbitrageOppurtunity curr = (ArbitrageOppurtunity)o;
        if(this.getProfitPerc() > curr.getProfitPerc()) {
            return 1;
        } else if(this.getProfitPerc() < curr.getProfitPerc()) {
            return -1;
        } else {
            return 0;
        }
    }
}





































