package exchange;

import core.Core;
import model.CryptoPair;
import impl.ExchangeHelper;
import model.ShrimpyHandler;

import java.util.ArrayList;

public class BitmartExchange extends Exchange {

    private static ArrayList<CryptoPair> allPairs = new ArrayList<>();

    public BitmartExchange(String type) {
        setExchangeType(type);
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

    @Override
    public void run() {
        synchPrices();
    }

    private void unfiromPairStrings() {
        ExchangeHelper helper = new ExchangeHelper();
        helper.uniformPairs(allPairs);
    }

}
