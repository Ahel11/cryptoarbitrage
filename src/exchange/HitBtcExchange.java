package exchange;

import core.Core;
import model.CryptoPair;
import impl.ExchangeHelper;
import model.ShrimpyHandler;

import java.util.ArrayList;

public class HitBtcExchange extends Exchange {
    private static ArrayList<CryptoPair> allPairs = new ArrayList<>();

    public HitBtcExchange(String type) {
        setExchangeType(type);
    }

    private void unfiromPairStrings() {
        ExchangeHelper helper = new ExchangeHelper();
        helper.uniformPairs(allPairs);
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

}
