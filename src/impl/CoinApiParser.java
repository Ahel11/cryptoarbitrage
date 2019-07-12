package impl;

import model.CryptoPair;
import model.Order;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CoinApiParser {

    public CoinApiParser() {

    }

    public CryptoPair parseCoinApiObjToPair(JSONObject jsonApiObj) {
        CryptoPair pairToReturn = new CryptoPair();
        try {
            String symbolId = jsonApiObj.getString("symbol_id");
            String splittedSymbol[] = symbolId.split("_");

            JSONArray asks = jsonApiObj.getJSONArray("asks");
            JSONArray bids = jsonApiObj.getJSONArray("bids");

            ArrayList<Order> askList = generateOrderArray(Order.ASK, asks);
            ArrayList<Order> bidList = generateOrderArray(Order.BID, bids);
            if(askList.size() == 0 || bidList.size() == 0) {
                return null;
            }
            Order bestAsk = askList.get(0);
            Order bestBid = bidList.get(0);

            pairToReturn.setExchangeType(splittedSymbol[0]);
            pairToReturn.setCryptoPair(generatePairType(splittedSymbol, symbolId));
            pairToReturn.setBestBid(bestBid);
            pairToReturn.setBestAsk(bestAsk);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return pairToReturn;
    }

    private ArrayList<Order> generateOrderArray(String type, JSONArray orderArrayJson) {
        ArrayList<Order> generatedOrderList = new ArrayList<>();
        try {
            for(int i=0; i<orderArrayJson.length(); i++) {
                JSONObject currObj = (JSONObject)orderArrayJson.get(i);
                Order currOrder = new Order();

                currOrder.setVolume(currObj.getDouble("size"));
                currOrder.setPrice(currObj.getDouble("price"));
                generatedOrderList.add(currOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedOrderList;
    }

    private String generatePairType(String[] splitted, String symboldId) {

        String quoteSymbol = splitted[splitted.length-1];
        String other = splitted[splitted.length-2];
        return quoteSymbol + other;
    }

}






















