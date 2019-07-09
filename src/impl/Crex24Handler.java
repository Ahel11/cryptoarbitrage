package impl;

import exchange.ExchangeType;
import model.CryptoPair;
import model.HttpHandler;
import model.Order;
import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Crex24Handler {

    private HttpHandler httpHandler;

    public Crex24Handler() {
        httpHandler = new HttpHandler();
    }

    public ArrayList<String> generatePairsToFetch() {
        ArrayList<String> allPairs = new ArrayList<>();
        try {
            String url = "https://api.crex24.com/v2/public/instruments";

            String resp = httpHandler.executeGetRequest(url);
            JSONArray respJson = new JSONArray(resp);

            for(int i=0; i<respJson.length(); i++) {
                JSONObject currJson = respJson.getJSONObject(i);
                String currentCurrency = currJson.getString("symbol");
                allPairs.add(currentCurrency);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        return allPairs;
    }

    public CryptoPair generateCryptoPairFromPair(String pair) throws Exception{
        CryptoPair pairToReturn = new CryptoPair();

        String url = "https://api.crex24.com/v2/public/orderBook?instrument=" + pair;
        String respString = httpHandler.executeGetRequest(url);
        JSONObject respJson = new JSONObject(respString);

        JSONArray buyArr = respJson.getJSONArray("buyLevels");
        JSONArray sellArr= respJson.getJSONArray("sellLevels");

        Order bestBidOrder = new Order();
        Order bestAskOrder = new Order();

        JSONObject bestBidObj = (JSONObject)buyArr.get(0);
        JSONObject bestAskObj = (JSONObject)sellArr.get(0);

        bestBidOrder.setPrice(bestBidObj.getDouble("price"));
        bestBidOrder.setVolume(bestBidObj.getDouble("volume"));

        bestAskOrder.setPrice(bestAskObj.getDouble("price"));
        bestAskOrder.setVolume(bestAskObj.getDouble("volume"));

        pairToReturn.setBestAsk(bestAskOrder);
        pairToReturn.setBestBid(bestBidOrder);
        pairToReturn.setCryptoPair(pair);
        pairToReturn.setExchangeType(ExchangeType.CREX24);


        return pairToReturn;

    }


    public static void main(String args[]) {
        try {

            Crex24Handler handler = new Crex24Handler();
            ArrayList<String> allPairs = handler.generatePairsToFetch();

            Crex24HandlerThread t = new Crex24HandlerThread(allPairs.get(6));
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}









































