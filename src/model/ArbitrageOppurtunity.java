package model;

import java.text.DecimalFormat;

public class ArbitrageOppurtunity implements Comparable{

    private String fromExchange;
    private String toExchange;
    private String pairType;
    private double buyPrice;
    private double sellPrice;
    private double profitPerc;
    private double profitDollar;
    private double minVolume;
    private String space = "         ";
    DecimalFormat format =  new DecimalFormat("#.#########");
    DecimalFormat formatPerc =  new DecimalFormat("#.##");

    public ArbitrageOppurtunity() {
        this.fromExchange ="";
        this.toExchange="";
        this.pairType = "";
        this.buyPrice = 0;
        this.sellPrice = 0;
        this.profitPerc = 0;
        this.minVolume = 0;
        this.profitDollar = 0;
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

    public double getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(double minVolume) {
        this.minVolume = minVolume;
    }

    public String getPairType() {
        return pairType;
    }

    public void setPairType(String pairType) {
        this.pairType = pairType;
    }

    public double getProfitDollar() {
        return profitDollar;
    }

    public void setProfitDollar(double profitDollar) {
        this.profitDollar = profitDollar;
    }

    @Override
    public String toString() {
        return "FROM:\t\t" + fromExchange +
                "\t\tTO:\t\t" + toExchange  +
                "\t\t\tbuyPrice:\t\t" + format.format(buyPrice)+
                "\t\t\tsellPrice:\t\t" + format.format(sellPrice )+
                "\t\tprofitPerc:\t\t" + formatPerc.format(profitPerc) + "%" +
                "\t\tpP$:\t\t" + formatPerc.format(profitDollar) +
                //"\t\tminVolume:\t\t" + formatPerc.format(minVolume) +
                "\t\tPair:\t\t" + this.pairType;
    }



    @Override
    public int compareTo(Object o) {
        ArbitrageOppurtunity curr = (ArbitrageOppurtunity)o;
        if(this.getProfitDollar() > curr.getProfitDollar()) {
            return 1;
        } else if(this.getProfitDollar() < curr.getProfitDollar()) {
            return -1;
        } else {
            return 0;
        }
    }
}





































