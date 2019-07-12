package exchange;

import impl.CoinApiParser;
import impl.ExchangeImpl;
import model.CryptoPair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Exchange extends Thread implements ExchangeImpl {

    private JSONArray allSymbolsJsonArr;
    private String exchangeType = "";
    private ArrayList<CryptoPair> exchangePairs;
    private boolean finishedSync = false;

    public Exchange() {
        this.exchangePairs = new ArrayList<CryptoPair>();
    }


    public void synchPrices() {

    }

    public void printAllPairs() {

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

    public ArrayList<CryptoPair> getAllCryptoPairsFromJsonArr() {
        ArrayList<CryptoPair> allPairs = new ArrayList<>();
        CoinApiParser parser = new CoinApiParser();
        try {
            for(int i=0; i<this.allSymbolsJsonArr.length(); i++) {
                JSONObject currObj = (JSONObject)this.allSymbolsJsonArr.get(i);
                if(parser.getExchangeFromJsonObj(currObj).equals(exchangeType)) {
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

    public JSONArray getAllSymbolsJsonArr() {
        return allSymbolsJsonArr;
    }

    public void setAllSymbolsJsonArr(JSONArray allSymbolsJsonArr) {
        this.allSymbolsJsonArr = allSymbolsJsonArr;
    }
}
