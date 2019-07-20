package model;

import java.text.DecimalFormat;

public class ArbitrageOppurtunity implements Comparable{

    private String fromExchange;
    private String toExchange;
    private String pairType;
    private CryptoPair bidPair;
    private CryptoPair askPair;
    private ProfitResult res;
    private double buyPrice;
    private double sellPrice;
    private double profitPerc;
    private double profitDollar;
    private String space = "         ";
    DecimalFormat format =  new DecimalFormat("#.#########");
    DecimalFormat formatPerc =  new DecimalFormat("#.##");
    DecimalFormat formatProfit =  new DecimalFormat("#.##");

    public ArbitrageOppurtunity() {
        this.fromExchange ="";
        this.toExchange="";
        this.pairType = "";
        this.buyPrice = 0;
        this.sellPrice = 0;
        this.profitPerc = 0;
        this.profitDollar = 0;
    }

    public ProfitResult getRes() {
        return res;
    }

    public void setRes(ProfitResult res) {
        this.res = res;
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
                "\t\tP%:\t\t" + formatPerc.format(profitPerc) + "%" +
                "\t\tpP$:\t\t" + formatProfit.format(profitDollar) +
                //"\t\tminVolume:\t\t" + formatPerc.format(minVolume) +
                "\t\tPair:\t\t" + this.pairType;
                //res.toString();
        // + "\n\nAsks:\n" + askListBuf.toString() + "\n\nBids:\n" + bidListBuf.toString() + "\n\n";
    }

    public int comparePerc(Object o) {
        ArbitrageOppurtunity curr = (ArbitrageOppurtunity)o;
        if(this.getProfitPerc() > curr.getProfitPerc()) {
            return 1;
        } else if(this.getProfitPerc() < curr.getProfitPerc()) {
            return -1;
        } else {
            return 0;
        }
    }

    public int compareProfit(Object o) {
        ArbitrageOppurtunity curr = (ArbitrageOppurtunity)o;
        if(this.getProfitDollar() > curr.getProfitDollar()) {
            return 1;
        } else if(this.getProfitDollar() < curr.getProfitDollar()) {
            return -1;
        } else {
            return 0;
        }
    }

    public CryptoPair getBidPair() {
        return bidPair;
    }

    public void setBidPair(CryptoPair bidPair) {
        this.bidPair = bidPair;
    }

    public CryptoPair getAskPair() {
        return askPair;
    }

    public void setAskPair(CryptoPair askPair) {
        this.askPair = askPair;
    }

    @Override
    public int compareTo(Object o) {
        return comparePerc(o);
    }
}





































