package model;

import org.apache.commons.codec.digest.Crypt;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ShrimpyHandler {
    private HttpClient client;

    public ShrimpyHandler() {
        initialize();
    }

    private void initialize() {
        try {
            client = HttpClientBuilder.create().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CryptoPair> getAllPairsFromExchange(String exchage) {
        ArrayList<CryptoPair> allPairs = new ArrayList<>();
        try {
            String url = "https://dev-api.shrimpy.io/v1/orderbooks?exchange=" + exchage;
            String respString = executeRequest(url);

            //Parsing
            JSONArray respJsonArr = new JSONArray(respString);
            for(int i=1; i<respJsonArr.length(); i++) {
                JSONObject currJsonObj = (JSONObject)respJsonArr.get(i);
                String quoteSymbol = currJsonObj.getString("quoteSymbol");
                String baseSymbol = currJsonObj.getString("baseSymbol");

                if(quoteSymbol.equals("BTC") || quoteSymbol.equals("ETH")
                        || quoteSymbol.equals("USDT")
                ) {
                    String pair = quoteSymbol + baseSymbol;
                    JSONArray orderBooksArray = currJsonObj.getJSONArray("orderBooks");
                    JSONObject orderBookJsonResult = orderBooksArray.getJSONObject(0);
                    JSONObject orderBookJsonObj = orderBookJsonResult.getJSONObject("orderBook");

                    JSONArray askArr = orderBookJsonObj.getJSONArray("asks");
                    JSONArray bidsArr = orderBookJsonObj.getJSONArray("bids");

                    CryptoPair generatedCryptoPair = generateCryptoPairFromAskAndBid(askArr, bidsArr);
                    generatedCryptoPair.setExchangeType(exchage);
                    generatedCryptoPair.setCryptoPair(pair);
                    allPairs.add(generatedCryptoPair);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allPairs;
    }

    private CryptoPair generateCryptoPairFromAskAndBid(JSONArray asks, JSONArray bids) throws Exception{
        CryptoPair pairToReturn = new CryptoPair();
        JSONObject bestBidObj = bids.getJSONObject(0);
        JSONObject bestAskObj = asks.getJSONObject(0);

        Order bestAskOrder = new Order();
        bestAskOrder.setPrice(Double.parseDouble(bestAskObj.getString("price")));
        bestAskOrder.setVolume(Double.parseDouble(bestAskObj.getString("quantity")));
        bestAskOrder.setOrderType(Order.ASK);


        Order bestBidOrder = new Order();
        bestBidOrder.setPrice(Double.parseDouble(bestBidObj.getString("price")));
        bestBidOrder.setVolume(Double.parseDouble(bestBidObj.getString("quantity")));
        bestBidOrder.setOrderType(Order.BID);

        pairToReturn.setBestAsk(bestAskOrder);
        pairToReturn.setBestBid(bestBidOrder);

        return pairToReturn;
    }

    private String executeRequest(String url) {
        try {
            HttpGet get = new HttpGet(url);
            HttpResponse resp = client.execute(get);
            String respString = EntityUtils.toString(resp.getEntity());
            return respString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String args[]) {
        ShrimpyHandler handler = new ShrimpyHandler();
        ArrayList<CryptoPair> allPairs = handler.getAllPairsFromExchange("hitbtc");

        for(CryptoPair currPair: allPairs) {
            System.out.print(currPair.toString() + "\n");

        }
    }

}




























