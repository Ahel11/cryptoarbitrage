package exchange;

import core.Core;
import impl.ExchangeHelper;
import model.CryptoPair;

import java.util.ArrayList;

public class LatokenExchange extends Exchange{


    private static ArrayList<CryptoPair> allPairs = new ArrayList<>();

    public LatokenExchange(String type) {
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
