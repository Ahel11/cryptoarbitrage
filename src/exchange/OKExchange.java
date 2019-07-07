package exchange;

import core.Core;
import model.CryptoPair;
import model.Order;
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

public class OKExchange extends Exchange{

    private static ArrayList<CryptoPair> allPairs = new ArrayList<>();
    private static int totalNrOfAssets = 0;
    private static int nrOfLoadedAssets = 0;

    public OKExchange(String type) {
        setExchangeType(type);
    }

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


    public ArrayList<String> getAllPairsToRetrieve() {
        ArrayList<String> allPairsString = new ArrayList<>();

        try {
            Document doc= Jsoup.connect("https://coinmarketcap.com/exchanges/okex/").get();
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

    @Override
    public void run() {
        synchPrices();
    }

    @Override
    public void synchPrices() {
        ArrayList<String> allPairs = getAllPairsToRetrieve();

        for(int i=0; i<allPairs.size(); i++) {
            String currPair = allPairs.get(0);
            OKexHandler okexHandler = new OKexHandler(currPair);
            okexHandler.start();
            exchangeSleep(100);
        }

        while(true) {
            if(isAllAssetsLoaded()) {
                break;
            }
            exchangeSleep(500);
        }
        Core.updateFinishedExchange(this.allPairs, getExchangeType());
        unfiromPairStrings();
        setFinishedSync(true);

    }

    @Override
    public void printAllPairs() {
        for(CryptoPair currPair: allPairs) {
            System.out.print(currPair.toString() +  "\n");
        }
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












    public class OKexHandler extends Thread {
        private String pair;
        public OKexHandler(String pair) {
            this.pair = pair;
        }

        public void run() {

        }

    }

}








































