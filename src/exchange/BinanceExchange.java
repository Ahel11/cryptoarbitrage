package exchange;

import core.Core;
import model.CryptoPair;
import model.ExchangeHelper;
import model.Order;
import model.ShrimpyHandler;
import net.sealake.binance.api.client.BinanceApiClientFactory;
import net.sealake.binance.api.client.BinanceApiRestClient;
import net.sealake.binance.api.client.domain.general.Asset;
import net.sealake.binance.api.client.domain.market.OrderBook;
import net.sealake.binance.api.client.domain.market.OrderBookEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BinanceExchange  extends Exchange {

    private static ArrayList<CryptoPair> allPairs = new ArrayList<>();
    private static int totalNrOfAssets = 0;
    private static int nrOfLoadedAssets = 0;
    private static boolean allAssetsLoaded = false;

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
























































