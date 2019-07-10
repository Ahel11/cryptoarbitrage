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
    private static int arrayLookBack = 5;

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

    public static double getBTCRate() {
        double btcRate = 0;
        try {

            HttpClient tempClient = HttpClientBuilder.create().build();
            String url = "https://dev-api.shrimpy.io/v1/orderbooks?exchange=binance&baseSymbol=BTC&quoteSymbol=USDT&limit=1";
            HttpGet get = new HttpGet(url);
            HttpResponse resp = tempClient.execute(get);
            String respString = EntityUtils.toString(resp.getEntity());

            JSONArray respJsonArr = new JSONArray(respString);
            JSONObject obj = respJsonArr.getJSONObject(0);
            JSONArray books = obj.getJSONArray("orderBooks");

            JSONObject bookObj = books.getJSONObject(0);
            JSONObject bookObj2 = bookObj.getJSONObject("orderBook");

            JSONArray asks = bookObj2.getJSONArray("asks");
            JSONObject finalObj = (JSONObject)asks.get(0);

            btcRate = Double.parseDouble(finalObj.getString("price"));

        } catch (Exception e) {
            e.printStackTrace();
        }


        return btcRate;
    }

    public static double getETHRate() {
        double btcRate = 0;
        try {

            HttpClient tempClient = HttpClientBuilder.create().build();
            String url = "https://dev-api.shrimpy.io/v1/orderbooks?exchange=binance&baseSymbol=ETH&quoteSymbol=USDT&limit=1";
            HttpGet get = new HttpGet(url);
            HttpResponse resp = tempClient.execute(get);
            String respString = EntityUtils.toString(resp.getEntity());

            JSONArray respJsonArr = new JSONArray(respString);
            JSONObject obj = respJsonArr.getJSONObject(0);
            JSONArray books = obj.getJSONArray("orderBooks");

            JSONObject bookObj = books.getJSONObject(0);
            JSONObject bookObj2 = bookObj.getJSONObject("orderBook");

            JSONArray asks = bookObj2.getJSONArray("asks");
            JSONObject finalObj = (JSONObject)asks.get(0);

            btcRate = Double.parseDouble(finalObj.getString("price"));

        } catch (Exception e) {
            e.printStackTrace();
        }


        return btcRate;
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

                    if(askArr.length() == 0 || bidsArr.length() ==0) {
                        continue;
                    }

                    CryptoPair generatedCryptoPair = generateCryptoPairFromAskAndBid(quoteSymbol, askArr, bidsArr);
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

    private CryptoPair generateCryptoPairFromAskAndBid(String quoteSymbol, JSONArray asks, JSONArray bids) throws Exception{
        CryptoPair pairToReturn = new CryptoPair();
        ArrayList<Order> askList = new ArrayList<>();
        ArrayList<Order> bidList = new ArrayList<>();

        JSONObject bestBidObj = bids.getJSONObject(0);
        JSONObject bestAskObj = asks.getJSONObject(0);
        double currP = 0, currQ = 0, usdVolume = 0;

        Order bestAskOrder = new Order();
        currP = Double.parseDouble(bestAskObj.getString("price"));
        currQ = Double.parseDouble(bestAskObj.getString("quantity"));
        usdVolume = PriceUtils.generateUSDVolume(quoteSymbol, currQ, currP);

        bestAskOrder.setPrice(currP);
        bestAskOrder.setVolume(usdVolume);
        bestAskOrder.setOrderType(Order.ASK);


        Order bestBidOrder = new Order();
        currP = Double.parseDouble(bestBidObj.getString("price"));
        currQ = Double.parseDouble(bestBidObj.getString("quantity"));
        usdVolume = PriceUtils.generateUSDVolume(quoteSymbol, currQ, currP);

        bestBidOrder.setPrice(currP);
        bestBidOrder.setVolume(usdVolume);
        bestBidOrder.setOrderType(Order.BID);

        pairToReturn.setBestAsk(bestAskOrder);
        pairToReturn.setBestBid(bestBidOrder);

        //Set ask and bid list
        for(int i=0; i<asks.length() && i<arrayLookBack; i++) {
            askList.add(getOrderFromJSONRepresentation(asks.getJSONObject(i)));
        }

        for(int i=0; i<bids.length() && i<arrayLookBack; i++) {
            bidList.add(getOrderFromJSONRepresentation(bids.getJSONObject(i)));
        }

        pairToReturn.setAskOrders(askList);
        pairToReturn.setBidOrders(bidList);

        return pairToReturn;
    }

    private Order getOrderFromJSONRepresentation(JSONObject currJson) {
        Order orderToReturn = new Order();
        try {
            orderToReturn.setPrice(Double.parseDouble(currJson.getString("price")));
            orderToReturn.setVolume(Double.parseDouble(currJson.getString("quantity")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderToReturn;
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




























