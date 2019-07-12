package impl;

import com.sun.org.apache.xpath.internal.operations.Or;
import model.Order;
import model.PriceUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class ProfitHandler {

    public static void main(String args[]) {
        Order bestAskOrder = new Order();
        ArrayList<Order> bidList = new ArrayList<>();

        bestAskOrder.setPrice(0.00000059);
        bestAskOrder.setVolume(1000000);


        Order bidOrderCurr = new Order();
        bidOrderCurr.setPrice(0.00000042);
        bidOrderCurr.setVolume(2700);
        bidList.add(bidOrderCurr);

        bidOrderCurr = new Order();
        bidOrderCurr.setPrice(0.00000051);
        bidOrderCurr.setVolume(4076);
        bidList.add(bidOrderCurr);

        bidOrderCurr = new Order();
        bidOrderCurr.setPrice(0.00000053);
        bidOrderCurr.setVolume(2000);
        bidList.add(bidOrderCurr);

        bidOrderCurr = new Order();
        bidOrderCurr.setPrice(0.00000055);
        bidOrderCurr.setVolume(16681);
        bidList.add(bidOrderCurr);

        bidOrderCurr = new Order();
        bidOrderCurr.setPrice(0.00000075);
        bidOrderCurr.setVolume(16681);
        bidList.add(bidOrderCurr);





        System.out.print("\nBestAsk:\n" + bestAskOrder.toString() + "\nBestBid\n\n");
        for(int i=0; i<bidList.size(); i++) {
            System.out.print(bidList.get(i).toString() + "\n");
        }

        ProfitHandler profHandler = new ProfitHandler();
        double profit = profHandler.calculateProfit("BTC", bestAskOrder, bidList);
        System.out.print(profit);

    }


    public ProfitHandler() {

    }

    public double calculateProfit(String pair, Order bestAskOrder, ArrayList<Order> bidList) {
        double profit = 0;
        double costOfBuying = 0;
        double costOfSelling = 0;
        double askPrice = bestAskOrder.getPrice();
        double nrOfTokensInVolumeToUse = 0;
        if(pair.contains("BTCMDT")) {
            System.out.print("HERE");
        }

        // -) Get the maximum sellVolume thats allowed
        // -) If the maximum sellVolume is < than the volume of the bestAskOrder, then thats the volume to use
        //    otherwise, the volume to use is the bestAskOrder.volume
        // -) With the volumeToUse, calculate the cost to buy the tokens from the bestAskorder
        // -) Calculate the cost to sell, by traversing the bidList as long as the price is higher than the askPrice


        //Calculates the mxaNr of tokens to sell
        double nrOfTokensToSell = calculateMaximumeNrOfTokensToSell(askPrice, bidList);
        if(nrOfTokensToSell == -1) {
            return-1;
        }

        if(nrOfTokensToSell < bestAskOrder.getVolume()) {
            nrOfTokensInVolumeToUse = nrOfTokensToSell;
        } else {
            nrOfTokensInVolumeToUse = bestAskOrder.getVolume();
        }
        //Cost of buying
        costOfBuying = bestAskOrder.getPrice() * nrOfTokensInVolumeToUse;
        costOfSelling = calculateCostOfSelling(askPrice, nrOfTokensInVolumeToUse, bidList);

        profit = costOfSelling - costOfBuying;

        return calculateUsdValue(pair, profit);
    }


    private double calculateUsdValue(String type, double profit) {
        if(type.contains("BTC")) {
            return profit * PriceUtils.BTCValue;
        } else if(type.contains("ETH")) {
            return profit * PriceUtils.ETHValue;
        } else if (type.contains("DOGE")) {
            return profit * PriceUtils.DOGEValue;
        }
        return profit;
    }

    public double calculateCostOfSelling(double askPrice, double nrOfTokens, ArrayList<Order> bidList) {
        double costOfSelling = 0;

        for(int i=0; i<bidList.size(); i++) {
            Order currOrder = bidList.get(i);
            if(nrOfTokens > currOrder.getVolume()) {
                costOfSelling = costOfSelling + (currOrder.getPrice() * currOrder.getVolume());
                nrOfTokens = nrOfTokens - currOrder.getVolume();
            } else  {
                costOfSelling = costOfSelling + currOrder.getPrice() * nrOfTokens;
                return costOfSelling;
            }
        }

        return costOfSelling;
    }

    private double calculateMaximumeNrOfTokensToSell(double askPrice, ArrayList<Order> bidList) {
        double maximumeNrOfTokensToSell = 0;
        int counter = 0;

        for(int i=0; i<bidList.size(); i++) {
            Order currBidOrder = bidList.get(i);
            if(currBidOrder.getPrice() > askPrice) {
                maximumeNrOfTokensToSell = maximumeNrOfTokensToSell +  currBidOrder.getVolume();
            } else {
                return maximumeNrOfTokensToSell;
            }
        }
        return -1;
    }

}




































