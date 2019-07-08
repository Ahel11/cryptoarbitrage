package impl;

import exchange.ExchangeType;
import model.CryptoPair;
import model.HttpHandler;
import model.Order;
import org.apache.commons.codec.digest.Crypt;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CoinExchangeHandler {

    public CoinExchangeHandler() {

    }

    public ArrayList<CryptoPair> getAllCrytoPairs() {
        ArrayList<CryptoPair> allPairs = new ArrayList<>();
        try {
            String url = "https://www.coinexchange.io/api/v1/getmarketsummaries";
            HttpHandler httpHandler = new HttpHandler();
            String respString = httpHandler.executeGetRequest(url);

            JSONObject respJson = new JSONObject(respString);
            allPairs = parseAllPairsFromJson(respJson);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return allPairs;
    }

    private ArrayList<CryptoPair> parseAllPairsFromJson(JSONObject respJson) throws Exception{
        ArrayList<CryptoPair> allPairs = new ArrayList<>();

        JSONArray jsonArr = respJson.getJSONArray("result");
        for(int i=0; i<jsonArr.length(); i++) {
            JSONObject currJsonObj = (JSONObject)jsonArr.get(i);
            String pair = "BTC" + currJsonObj.getString("MarketAssetCode");

            CryptoPair currPair = new CryptoPair();
            currPair.setCryptoPair(pair);

            Order bestBidOrder = new Order();
            Order bestAskOrder = new Order();

            bestAskOrder.setPrice(Double.parseDouble(currJsonObj.getString("AskPrice")));
            bestAskOrder.setVolume(Double.parseDouble(currJsonObj.getString("Volume")));
            bestAskOrder.setOrderType(Order.ASK);

            bestBidOrder.setPrice(Double.parseDouble(currJsonObj.getString("BidPrice")));
            bestBidOrder.setVolume(Double.parseDouble(currJsonObj.getString("Volume")));
            bestBidOrder.setOrderType(Order.BID);

            currPair.setBestBid(bestBidOrder);
            currPair.setBestAsk(bestAskOrder);
            currPair.setExchangeType(ExchangeType.COINEXCHANGE);
            allPairs.add(currPair);
        }
        return allPairs;
    }

}




























