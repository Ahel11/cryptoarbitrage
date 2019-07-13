package exchange;

import core.Core;
import model.CryptoPair;

import java.util.ArrayList;

public class CoinealExchange extends Exchange{


    private static ArrayList<CryptoPair> allPairs = new ArrayList<>();

    public CoinealExchange(String type) {
        this.setExchangeType(type);
        initialize();
    }

    private void initialize() {

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
