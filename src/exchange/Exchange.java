package exchange;

import impl.ExchangeImpl;
import model.CryptoPair;
import net.sealake.binance.api.client.domain.market.OrderBook;
import net.sealake.binance.api.client.domain.market.OrderBookEntry;

import java.util.ArrayList;
import java.util.List;

public class Exchange extends Thread implements ExchangeImpl {

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
}
