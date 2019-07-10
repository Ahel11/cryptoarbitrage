package impl;

import model.CryptoPair;
import model.Order;

import java.util.ArrayList;

public class ExchangeHelper {

    public void uniformPairs(ArrayList<CryptoPair> allPairs) {
        for (CryptoPair pair : allPairs) {
            String uniformPair = pair.getCryptoPair().replace("/", "");

            if (uniformPair.contains("BTC")) {
                String other = uniformPair.replace("BTC", "");
                uniformPair = "BTC" + other;
            } else if (uniformPair.contains("ETH")) {
                String other = uniformPair.replace("ETH", "");
                uniformPair = "ETH" + other;
            } else if (uniformPair.contains("USDT")) {
                String other = uniformPair.replace("USDT", "");
                uniformPair = "USDT" + other;
            }
            uniformPair = uniformPair.replace("-", "");


            pair.setCryptoPair(uniformPair);
            pair.getBestAsk().setOrderType(Order.ASK);
            pair.getBestBid().setOrderType(Order.BID);
        }
    }

}
