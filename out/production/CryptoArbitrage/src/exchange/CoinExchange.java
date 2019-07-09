package exchange;

import core.Core;
import impl.CoinExchangeHandler;
import model.CryptoPair;
import model.ExchangeHelper;

import java.util.ArrayList;
import java.util.HashSet;

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

        unfiromPairStrings();
        Core.updateFinishedExchange(allPairs, getExchangeType());
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
        allPairs  = filterBtcOnly();
    }

    private ArrayList<CryptoPair> filterBtcOnly() {
        HashSet<CryptoPair> btcOnlyPairs = new HashSet<>();

        for(CryptoPair curr: allPairs) {
            ArrayList<CryptoPair> similarPairs = getAllSimilarPairs(curr);
            CryptoPair btcFromList = getBtcFromList(similarPairs);
            btcOnlyPairs.add(btcFromList);
        }
        ArrayList<CryptoPair> filteredList = new ArrayList<>(btcOnlyPairs);
        return filteredList;
    }

    private CryptoPair getBtcFromList(ArrayList<CryptoPair> allPairs) {
        CryptoPair highestVal = allPairs.get(0);

        for(CryptoPair currPair: allPairs) {
            if(currPair.getBestBid().getPrice() < highestVal.getBestBid().getPrice()) {
                highestVal = currPair;
            }
        }
        return highestVal;
    }

    private ArrayList<CryptoPair> getAllSimilarPairs(CryptoPair pair) {
        ArrayList<CryptoPair> similarPairs = new ArrayList<>();

        for(CryptoPair currPair: allPairs) {
            if(currPair.getCryptoPair().equals(pair.getCryptoPair())) {
                similarPairs.add(currPair);
            }
        }

        return similarPairs;
    }

}
























