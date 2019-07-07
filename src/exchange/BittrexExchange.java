package exchange;

import core.Core;
import model.CryptoPair;
import model.Order;
import model.ShrimpyHandler;
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


    public BittrexExchange(String type) {
        setExchangeType(type);
    }

    @Override
    public void run() {
        synchPrices();
    }



    @Override
    public void synchPrices() {
        ShrimpyHandler shrimpyHandler = new ShrimpyHandler();

        while(true) {
            allPairs = shrimpyHandler.getAllPairsFromExchange(ExchangeType.BITTREX);
            if(allPairs.size() > 60) {
                break;
            }
            exchangeSleep(800);
        }

        Core.updateFinishedExchange(this.allPairs, getExchangeType());
        unfiromPairStrings();
        setFinishedSync(true);
    }

    @Override
    public void printAllPairs() {
        for(CryptoPair currPair: allPairs) {
            System.out.print(currPair.toString() + "\n");
        }
        System.out.print("\nSIZE OF " + this.getExchangeType() + "\t" + allPairs.size() + "\n\n");
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
            } else if(uniformPair.contains("USDT")) {
                String other = uniformPair.replace("USDT", "");
                uniformPair = "USDT" + other;
            }


            pair.setCryptoPair(uniformPair);
            pair.getBestAsk().setOrderType(Order.ASK);
            pair.getBestBid().setOrderType(Order.BID);
        }
    }

}






















































