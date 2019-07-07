package model;

import java.util.ArrayList;

public class CryptoPair {

    private Order bestAsk;
    private Order bestBid;
    private String cryptoPair;
    private String exchangeType;

    public Order getBestAsk() {
        return bestAsk;
    }

    public void setBestAsk(Order bestAsk) {
        this.bestAsk = bestAsk;
    }

    public Order getBestBid() {
        return bestBid;
    }

    public void setBestBid(Order bestBid) {
        this.bestBid = bestBid;
    }

    public String getCryptoPair() {
        return cryptoPair;
    }

    public void setCryptoPair(String cryptoPair) {
        this.cryptoPair = cryptoPair;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    @Override
    public String toString() {
        return "CryptoPair{" +
                "bestAsk=" + bestAsk +
                ", bestBid=" + bestBid +
                ", cryptoPair='" + cryptoPair + '\'' +
                ", exchangeType='" + exchangeType + '\'' +
                '}';
    }
}











































