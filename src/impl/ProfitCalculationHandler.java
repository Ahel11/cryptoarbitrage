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
    StringBuffer currLogger = new StringBuffer();

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

        currLogger.append("AskIndex:\t" + askIndex + "\n");
        currLogger.append("BidIndex:\t" + bidIndex + "\n");
        currLogger.append("BuyPrice:\t" + buyPrice + "\n");
        currLogger.append("SellPrice:\t" + sellPrice + "\n\n");

        currLogger.append("AskOrders:\n");
        for(Order o: askList) {
            currLogger.append(o.toString() + "\n");
        }

        currLogger.append("\nBidOrders:\n");
        for(Order o: bidList) {
            currLogger.append(o.toString() + "\n");
        }


        double profit = calculateProfit(askIndex, bidIndex);


        holder.setBuyPrice(buyPrice);
        holder.setSellPrice(sellPrice);
        holder.setProfit(profit);

        holder.setLogger(currLogger);
        currLogger = new StringBuffer();

        return holder;
    }

    private double calculateProfit(int askIndex, int bidIndex) {
        double profit = 0;
        double buyingPower = calculateBuyingPower(bidIndex);

        currLogger.append("\nBuyingPower:\t" + buyingPower + "\n");

        int i=0, j=0;

        //Nr of tokens that can be bought
        double boughtTokens = buyTokens(buyingPower, askIndex);

        //How much its sold for, wheter its BTC, ETH, USDT thats returned
        double soldTokensValue = sellTokens(boughtTokens, bidIndex);

        currLogger.append("BoughtTokens:\t" + boughtTokens + "\n");
        currLogger.append("SoldTokensVal:\t" + soldTokensValue + "\n");

        profit = soldTokensValue - buyingPower;

        currLogger.append("Profit:\t" + profit);

        //TODO
        //Max volume to buy should be the max volume of the entire bidList

        return profit;
    }

    private double sellTokens(double nrOfTokens, int bidIndex) {
        double profit = 0;
        double totalNrOfTokensLeft = nrOfTokens;
        currLogger.append("\n\n\nSelling tokens, bidIndex:\t" + bidIndex + "\n");

        for(int i=0; i<=bidIndex; i++) {
            currLogger.append("\nSelling tokens, i= " + i + "\n");
            Order currOrder = bidList.get(i);
            currLogger.append("CurrOrder:\t" + currOrder.toString() + "\n");
            if(totalNrOfTokensLeft > currOrder.getVolume()) {
                currLogger.append(totalNrOfTokensLeft + " > " + currOrder.getVolume() + "\n");
                profit = profit + currOrder.getPrice() * currOrder.getVolume();
                currLogger.append("Profit:\t" + profit + "\n");
                totalNrOfTokensLeft = totalNrOfTokensLeft - currOrder.getVolume();
                currLogger.append("totalNrOfTokensLeft:\t" + totalNrOfTokensLeft + "\n");
            } else {
                currLogger.append(totalNrOfTokensLeft + " < " + currOrder.getVolume() + "\n");
                currLogger.append("Profit = " + profit + " + " + (currOrder.getPrice() * totalNrOfTokensLeft) + "\n");
                profit = profit + (currOrder.getPrice() * totalNrOfTokensLeft);
                currLogger.append("Profit = \t" + profit + "\n");
                return profit;
            }
        }
        return profit;
    }

    private double buyTokens(double buyingPower, int askIndex) {
        double nrOfTokensBought = 0;
        double totalBuyingPower = buyingPower;
        currLogger.append("\n\n\nBuyig tokens, askIndex:\t" + askIndex + "\n");

        for(int i=0; i<=askIndex; i++) {
            currLogger.append("\nBuyig tokens, i= " + i + "\n");
            Order currOrder = askList.get(i);
            double totalCostForCurrentOrder = currOrder.getPrice() * currOrder.getVolume();
            currLogger.append("CurrOrder:\t" + currOrder.toString() + "\n");
            currLogger.append("totalCostForCurrentOrder = " + String.valueOf(currOrder.getPrice()) + " * " + currOrder.getVolume() + " = " + totalCostForCurrentOrder + "\n");
            if(totalCostForCurrentOrder < totalBuyingPower) {
                currLogger.append(totalCostForCurrentOrder + " < " + totalBuyingPower + "\n");
                nrOfTokensBought += currOrder.getVolume();
                totalBuyingPower = totalBuyingPower - totalCostForCurrentOrder;
                currLogger.append("currOrder.Volume():\t" + currOrder.getVolume() + "\n");
                currLogger.append("TotalBuyingPower:\t" + totalBuyingPower + "\n");
                currLogger.append("NrOfBoughtTokens:\t" + nrOfTokensBought + "\n");
            } else {
                nrOfTokensBought += (totalBuyingPower / currOrder.getPrice());
                currLogger.append("NrOfTokensBought:\t" + nrOfTokensBought + "\n");
                return nrOfTokensBought;
            }
        }

        return nrOfTokensBought;
    }

    private double calculateBuyingPower(int bidIndex) {
        double disposableCashBuyingPower = getBuyingPowerBasedOnCash();
        double buyingPowerBasedOnBids = getBuyingPowerBasedOnAllowedBids(bidIndex);

        if(buyingPowerBasedOnBids < disposableCashBuyingPower) {
            currLogger.append(String.valueOf(buyingPowerBasedOnBids) + " < " + String.valueOf(disposableCashBuyingPower) + "\n");
            return buyingPowerBasedOnBids;
        } else {
            currLogger.append(String.valueOf(buyingPowerBasedOnBids) + " > " + String.valueOf(disposableCashBuyingPower) + "\n");
            return disposableCashBuyingPower;
        }
    }

    private double getBuyingPowerBasedOnAllowedBids(int bidIndex) {
        double buyingPowerBasedOnBids = 0;

        for(int i=0; i<=bidIndex; i++) {
            buyingPowerBasedOnBids += (bidList.get(i).getPrice() * bidList.get(i).getVolume());
        }

        return  buyingPowerBasedOnBids;
    }

    private double getBuyingPowerBasedOnCash() {
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










































