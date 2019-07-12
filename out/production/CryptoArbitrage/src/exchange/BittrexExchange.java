package exchange;

import core.Core;
import model.CryptoPair;
import impl.ExchangeHelper;
import model.ShrimpyHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class BittrexExchange extends Exchange{

    private static ArrayList<CryptoPair> allPairs = new ArrayList<>();
    public BittrexExchange(String type) {
        setExchangeType(type);
    }

    @Override
    public void run() {
        synchPrices();
    }



    @Override
    public void synchPrices() {

        allPairs = getAllCryptoPairsFromJsonArr();
        Core.updateFinishedExchange(this.allPairs, getExchangeType());
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
        ExchangeHelper helper = new ExchangeHelper();
        helper.uniformPairs(allPairs);
    }

}






















































