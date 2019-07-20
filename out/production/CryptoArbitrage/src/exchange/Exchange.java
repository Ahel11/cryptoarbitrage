package exchange;

import core.Core;
import impl.CoinApiParser;
import interfaceimpl.ExchangeImpl;
import model.CryptoPair;
import model.ShrimpyHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Exchange extends Thread implements ExchangeImpl {
    private ArrayList<CryptoPair> allPairs = new ArrayList<>();

    private JSONArray allSymbolsJsonArr;
    private String exchangeType = "";
    private ArrayList<CryptoPair> exchangePairs;
    private boolean finishedSync = false;

    public Exchange(String type) {
        setExchangeType(type);
        this.exchangePairs = new ArrayList<CryptoPair>();
    }

    public Exchange() {

    }

    public void run() {
        synchPrices();
    }

    public void synchPrices() {
        allPairs = getAllCryptoPairsFromShrimpy();
        Core.updateFinishedExchange(this.allPairs, getExchangeType());
        setFinishedSync(true);
    }

    public void printAllPairs() {
        for(CryptoPair currPair: allPairs) {
            System.out.print(currPair.toString() + "\n");
        }
        System.out.print("\nSIZE OF " + this.getExchangeType() + "\t" + allPairs.size() + "\n\n");
    }


    public ArrayList<CryptoPair> getExchangePairs() {
        return exchangePairs;
    }

    public void setExchangePairs(ArrayList<CryptoPair> exchangePairs) {
        this.exchangePairs = exchangePairs;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public boolean isFinishedSync() {
        return finishedSync;
    }

    public void setFinishedSync(boolean finishedSync) {
        this.finishedSync = finishedSync;
    }

    public void exchangeSleep(long ms) {
        try {
            Thread.sleep(ms);
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<CryptoPair> getAllCryptoPairsFromJsonArrCoinApi() {
        ArrayList<CryptoPair> allPairs = new ArrayList<>();
        CoinApiParser parser = new CoinApiParser();
        try {
            for(int i=0; i<this.allSymbolsJsonArr.length(); i++) {
                JSONObject currObj = (JSONObject)this.allSymbolsJsonArr.get(i);
                if(parser.getExchangeFromJsonObj(currObj).equalsIgnoreCase(exchangeType)) {
                    CryptoPair currPair = parser.parseCoinApiObjToPair(currObj);
                    if(currPair != null) {
                        allPairs.add(currPair);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allPairs;
    }

    public ArrayList<CryptoPair> getAllCryptoPairsFromShrimpy() {
        ShrimpyHandler handler = new ShrimpyHandler();
        ArrayList<CryptoPair> allPairs = handler.getAllPairsFromExchange(this.getExchangeType());
        return allPairs;
    }

    public JSONArray getAllSymbolsJsonArr() {
        return allSymbolsJsonArr;
    }

    public void setAllSymbolsJsonArr(JSONArray allSymbolsJsonArr) {
        this.allSymbolsJsonArr = allSymbolsJsonArr;
    }
}
