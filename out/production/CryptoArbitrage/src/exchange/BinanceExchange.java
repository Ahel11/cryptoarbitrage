package exchange;

import core.Core;
import model.CryptoPair;
import impl.ExchangeHelper;
import model.ShrimpyHandler;

import java.util.ArrayList;

public class BinanceExchange  extends Exchange {

    private static ArrayList<CryptoPair> allPairs = new ArrayList<>();

    public BinanceExchange(String type) {
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
        ShrimpyHandler shrimpyHandler = new ShrimpyHandler();

        while(true) {
            allPairs = shrimpyHandler.getAllPairsFromExchange(ExchangeType.BINANCE);
            if(allPairs.size() > 300) {
                break;
            }
            exchangeSleep(800);
        }

        Core.updateFinishedExchange(this.allPairs, getExchangeType());
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
























































