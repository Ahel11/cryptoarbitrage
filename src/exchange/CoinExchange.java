package exchange;

import core.Core;
import impl.CoinExchangeHandler;
import model.CryptoPair;
import model.ExchangeHelper;

import java.util.ArrayList;

public class CoinExchange extends  Exchange{


    private static ArrayList<CryptoPair> allPairs = new ArrayList<>();

    public CoinExchange (String type) {
        setExchangeType(type);
    }

    @Override
    public void run() {
        synchPrices();
    }

    @Override
    public void synchPrices() {
        CoinExchangeHandler handler = new CoinExchangeHandler();
        allPairs = handler.getAllCrytoPairs();

        Core.updateFinishedExchange(allPairs, getExchangeType());
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

    private void unfiromPairStrings() {
        ExchangeHelper helper = new ExchangeHelper();
        helper.uniformPairs(allPairs);
    }

}
























