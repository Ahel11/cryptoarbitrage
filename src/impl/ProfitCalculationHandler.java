package impl;

import model.Order;
import model.PriceUtils;
import model.ProfitCalculationHolder;

import java.util.ArrayList;
import java.util.Collections;

public class ProfitCalculationHandler {

    private ArrayList<Order> askList;
    private ArrayList<Order> bidList;
    private String type = "";
    private double maxUSDVolumeDisposable = 0;

    private ArrayList<ProfitCalculationHolder> profitCalculationList;

    public ProfitCalculationHandler() {
        profitCalculationList = new ArrayList<>();
        askList = new ArrayList<>();
        bidList = new ArrayList<>();
    }

    public ArrayList<Order> getAskList() {
        return askList;
    }

    public void setAskList(ArrayList<Order> askList) {
        this.askList = askList;
    }

    public ArrayList<Order> getBidList() {
        return bidList;
    }

    public void setBidList(ArrayList<Order> bidList) {
        this.bidList = bidList;
    }



    public ProfitCalculationHolder calculateBestProfit() {

        int i=0;
        int j=0;

        for(i=0; i<askList.size(); i++) {
            for(j=0; j<bidList.size(); j++) {
                //Generate profitCalculationHolder for all scenarios in the iteration
                ProfitCalculationHolder holder = generateProfitHolder(i, j);
                profitCalculationList.add(holder);
            }
        }

        Collections.sort(profitCalculationList);
        return profitCalculationList.get(0);
    }

    private ProfitCalculationHolder generateProfitHolder(int askIndex, int bidIndex) {
        ProfitCalculationHolder holder = new ProfitCalculationHolder();

        Order bestAskOrder = askList.get(askIndex);
        Order bestBidOrder = bidList.get(bidIndex);

        double buyPrice = bestAskOrder.getPrice();
        double sellPrice = bestBidOrder.getPrice();
        double profit = calculateProfit(askIndex, bidIndex);

        holder.setBuyPrice(buyPrice);
        holder.setSellPrice(sellPrice);
        holder.setProfit(profit);

        return holder;
    }

    private double calculateProfit(int askIndex, int bidIndex) {
        double profit = 0;
        double buyingPower = calculateBuyingPower();

        int i=0, j=0;

        //Nr of tokens that can be bought
        double boughtTokens = buyTokens(buyingPower, askIndex);

        //How much its sold for, wheter its BTC, ETH, USDT thats returned
        double soldTokensValue = sellTokens(boughtTokens, bidIndex);

        profit = soldTokensValue - buyingPower;

        //TODO
        //Max volume to buy should be the max volume of the entire bidList

        return profit;
    }

    private double sellTokens(double nrOfTokens, int bidIndex) {
        double profit = 0;
        double totalNrOfTokensLeft = nrOfTokens;

        for(int i=0; i<=bidIndex; i++) {
            Order currOrder = bidList.get(i);
            if(totalNrOfTokensLeft > currOrder.getVolume()) {
                profit += currOrder.getPrice() * currOrder.getVolume();
                nrOfTokens = nrOfTokens - currOrder.getVolume();
            } else {
                profit = profit + (currOrder.getPrice() * nrOfTokens);
                return profit;
            }
        }
        return profit;
    }

    private double buyTokens(double buyingPower, int askIndex) {
        double nrOfTokensBought = 0;
        double totalBuyingPower = buyingPower;

        for(int i=0; i<=askIndex; i++) {
            Order currOrder = askList.get(i);
            double totalCostForCurrentOrder = currOrder.getPrice() * currOrder.getVolume();
            if(totalCostForCurrentOrder < totalBuyingPower) {
                nrOfTokensBought += currOrder.getVolume();
                totalBuyingPower = totalBuyingPower - totalCostForCurrentOrder;
            } else {
                nrOfTokensBought += (totalBuyingPower / currOrder.getPrice());
                return nrOfTokensBought;
            }
        }

        return nrOfTokensBought;
    }

    private double calculateBuyingPower() {
        if(this.type.contains("BTC")) {
            return this.maxUSDVolumeDisposable * (1 / PriceUtils.BTCValue);
        } else if(this.type.contains("ETH")) {
            return this.maxUSDVolumeDisposable * (1 / PriceUtils.ETHValue);
        } else {
            return this.maxUSDVolumeDisposable;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private double getVolumeToUse(int askIndex, int bidIndex) {
        double aggregatedBidVolume = 0;
        double aggregatedAskVolume = 0;

        aggregatedAskVolume = aggregateVolume(askIndex, askList);
        aggregatedBidVolume = aggregateVolume(bidIndex, bidList);

        if(aggregatedBidVolume > aggregatedAskVolume) {
            return aggregatedAskVolume;
        }
        return aggregatedBidVolume;
    }

    private double aggregateVolume(int index, ArrayList<Order> orders) {
        double aggregatedVolume = 0;

        for(int i=0; i<=index; i++) {
            aggregatedVolume += orders.get(i).getVolume();
        }

        return aggregatedVolume;
    }

    public double getMaxUSDVolumeDisposable() {
        return maxUSDVolumeDisposable;
    }

    public void setMaxUSDVolumeDisposable(double maxUSDVolumeDisposable) {
        this.maxUSDVolumeDisposable = maxUSDVolumeDisposable;
    }

    public ArrayList<ProfitCalculationHolder> getProfitCalculationList() {
        return profitCalculationList;
    }

    public void setProfitCalculationList(ArrayList<ProfitCalculationHolder> profitCalculationList) {
        this.profitCalculationList = profitCalculationList;
    }
}










































