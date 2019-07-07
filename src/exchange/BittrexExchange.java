package exchange;

import core.Core;
import model.CryptoPair;
import model.Order;
import net.sealake.binance.api.client.BinanceApiRestClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class BittrexExchange extends Exchange{

    private static ArrayList<CryptoPair> allPairs = new ArrayList<>();
    private static int totalNrOfAssets = 0;
    private static int nrOfLoadedAssets = 0;

    public static synchronized void updateTotalNrOfAssets(int nr) {
        totalNrOfAssets = totalNrOfAssets + nr;
    }

    public static synchronized void updateTotalNrOfLoadedAssets(int nr) {
        nrOfLoadedAssets = nrOfLoadedAssets + nr;
    }

    public static synchronized void addCryptoPairToList(CryptoPair pairToAdd) {
        allPairs.add(pairToAdd);
    }

    public static boolean isAllAssetsLoaded() {
        System.out.print("NrLoadedAssets:\t" + nrOfLoadedAssets + "\tTotalNrAssets:\t" + totalNrOfAssets + "\n");
        if(nrOfLoadedAssets >= (totalNrOfAssets-2)) {
            return true;
        }
        return false;
    }


    public BittrexExchange(String type) {
        setExchangeType(type);
    }

    @Override
    public void run() {
        synchPrices();
    }



    @Override
    public void synchPrices() {
        ArrayList<String> allPairsToGet = getAllPairsToRetrieve();
        totalNrOfAssets = allPairsToGet.size();

        for(String pair: allPairsToGet) {
            BittrexHandler handler = new BittrexHandler(pair);
            handler.start();
            //exchangeSleep(100);
        }

        while(true) {
            if(isAllAssetsLoaded()) {
                break;
            }
            exchangeSleep(500);
        }
        Core.updateFinishedExchange(allPairs, getExchangeType());
        unfiromPairStrings();
        setFinishedSync(true);
    }

    @Override
    public void printAllPairs() {
        for(CryptoPair currPair: allPairs) {
            System.out.print(currPair.toString() + "\n");
        }
    }

    public ArrayList<String> getAllPairsToRetrieve() {
        ArrayList<String> allPairsString = new ArrayList<>();

        try {
            Document doc= Jsoup.connect("https://coinmarketcap.com/exchanges/bittrex/").get();
            Elements allElements = doc.getElementsByTag("td");

            for(Element e: allElements) {
                if(e.text().contains("/")) {
                    String bittrexPair = e.text().replace("/" , "-");
                    allPairsString.add(bittrexPair);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return allPairsString;
    }

    public static void setAllPairs(ArrayList<CryptoPair> allPairs) {
        allPairs = allPairs;
    }

    private void unfiromPairStrings() {
        for(CryptoPair pair: allPairs) {
            String uniformPair = pair.getCryptoPair().replace("/", "");

            if(uniformPair.contains("BTC")) {
                String other = uniformPair.replace("BTC", "");
                uniformPair = "BTC" + other;
            } else if(uniformPair.contains("ETH")) {
                String other = uniformPair.replace("ETH", "");
                uniformPair = "ETH" + other;
            }

            pair.setCryptoPair(uniformPair);
            pair.getBestAsk().setOrderType(Order.ASK);
            pair.getBestBid().setOrderType(Order.BID);
        }
    }

    public class BittrexHandler extends Thread {
        private String pair;
        public BittrexHandler(String pair) {
            this.pair = pair;
        }

        public void run() {
            CryptoPair cryptoPair = generateCryptopairFromPair(pair);
            if(cryptoPair == null) {
                return;
            }
            addCryptoPairToList(cryptoPair);
            updateTotalNrOfLoadedAssets(1);
        }

        private CryptoPair generateCryptopairFromPair(String pair) {
            try {
                pair = flipMarkets(pair);
                String url = generateGETUrl(pair);
                String respString = executeRequest(url);

                if(isSuccess(respString)) {
                    return getCryptoPairFromResult(pair, respString);
                } else {
                    //Check if invalid market and flip
                    if(isInvalidMarket(respString)) {
                        System.out.print("Flipping:\t" + pair + "\n");
                        String flipMarkets = flipMarkets(pair);
                        String newUrl = url.replace(pair, flipMarkets);
                        String newResponse = executeRequest(newUrl);
                        if(isSuccess(newResponse)) {
                            CryptoPair retrievedPair = getCryptoPairFromResult(flipMarkets, newResponse);
                            return retrievedPair;
                        } else {
                            return null;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private String executeRequest(String url) {
            String respString = null;
            try {
                HttpClient client = HttpClientBuilder.create().build();
                HttpGet get = new HttpGet(url);

                HttpResponse resp = client.execute(get);
                respString = EntityUtils.toString(resp.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return respString;
        }

        private String flipMarkets(String currMarket) {
            String split[] = currMarket.split("-");
            String flipped = split[1] + "-" + split[0];
            return flipped;
        }

        private boolean isSuccess(String resp) throws Exception{
            JSONObject obj = new JSONObject(resp);
            Boolean success = obj.getBoolean("success");
            if(!success) {
                return false;
            } else {
                return true;
            }
        }

        private boolean isInvalidMarket(String resp) throws Exception{
            JSONObject obj = new JSONObject(resp);
            String success = obj.getString("message");
            if(success.equals("INVALID_MARKET")) {
                return true;
            } else {
                return false;
            }
        }

        private CryptoPair getCryptoPairFromResult(String pair, String result) throws Exception{
            pair = pair.replace("-" , "/");
            JSONObject respObj = new JSONObject(result);
            JSONObject resultObj = (JSONObject)respObj.get("result");
            JSONArray buyArr = resultObj.getJSONArray("buy");
            JSONArray sellArr = resultObj.getJSONArray("sell");

            CryptoPair pairToReturn = new CryptoPair();

            Order bestBidOrder = new Order();
            double bestBid = buyArr.getJSONObject(0).getDouble("Rate");
            double volume = buyArr.getJSONObject(0).getDouble("Quantity");
            bestBidOrder.setPrice(bestBid);
            bestBidOrder.setVolume(volume);
            bestBidOrder.setOrderType(pair);

            Order bestAskOrder = new Order();
            double bestAsk = sellArr.getJSONObject(0).getDouble("Rate");
            volume = sellArr.getJSONObject(0).getDouble("Quantity");
            bestAskOrder.setPrice(bestAsk);
            bestAskOrder.setVolume(volume);
            bestAskOrder.setOrderType(pair);

            pairToReturn.setBestBid(bestBidOrder);
            pairToReturn.setBestAsk(bestAskOrder);
            pairToReturn.setExchangeType(ExchangeType.BITTREX);
            pairToReturn.setCryptoPair(pair);

            return pairToReturn;
        }

        private String generateGETUrl(String pair) {
            String url = "https://api.bittrex.com/api/v1.1/public/getorderbook?market=" + pair + "&type=both";
            return url;
        }

    }

    public static void main(String args[]) {
       /* BittrexExchange bit = new BittrexExchange(ExchangeType.BITTREX);

        ArrayList<String> allMarkets = bit.getAllPairsToRetrieve();

        String currMarket = allMarkets.get(32);
        currMarket = currMarket.replace("/", "-");
        String url = "https://api.bittrex.com/api/v1.1/public/getorderbook?market=" + currMarket + "&type=both";

        String res = bit.getRequestForPair(currMarket, url);
        System.out.print(res +  "\n");
        */

    }


}






















































