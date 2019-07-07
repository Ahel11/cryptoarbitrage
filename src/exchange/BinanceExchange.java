package exchange;

import core.Core;
import model.CryptoPair;
import model.Order;
import net.sealake.binance.api.client.BinanceApiClientFactory;
import net.sealake.binance.api.client.BinanceApiRestClient;
import net.sealake.binance.api.client.domain.general.Asset;
import net.sealake.binance.api.client.domain.market.OrderBook;
import net.sealake.binance.api.client.domain.market.OrderBookEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BinanceExchange  extends Exchange {

    private BinanceApiRestClient client;
    private static ArrayList<CryptoPair> allPairs = new ArrayList<>();
    private static int totalNrOfAssets = 0;
    private static int nrOfLoadedAssets = 0;

    public BinanceExchange(String type) {
        this.setExchangeType(type);
        initialize();
        this.totalNrOfAssets = this.client.getAllAssets().size() * 2;
    }

    public static synchronized void updateTotalNrOfAssets(int nr) {
        totalNrOfAssets = totalNrOfAssets + nr;
    }

    public static synchronized void updateTotalNrOfLoadedAssets(int nr) {
        nrOfLoadedAssets = nrOfLoadedAssets + nr;
    }

    public static synchronized void addCryptoPairToList(CryptoPair pairToAdd) {
        allPairs.add(pairToAdd);
    }

    public static boolean isAllAssetsLoaded() {
        System.out.print("NrLoadedAssets:\t" + nrOfLoadedAssets + "\tTotalNrAssets:\t" + totalNrOfAssets + "\n");
        if(nrOfLoadedAssets >= (totalNrOfAssets-1)) {
            return true;
        }
        return false;
    }


    private void initialize() {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("8Jp0WnV4Taf80bXljZ1IS5zsHFOaV2ilEvOeqOvDM3EyfbXlUhxlup9Mk4nX4rdF ", "CngJfdsIZ2uSDsDAis5Qw74rbXFkQWujZBhHgaweIx5IKfGNznv7LYEKiPZ5S9hD");
        client = factory.newRestClient();
    }

    @Override
    public void run() {
        synchPrices();
    }

    @Override
    public void synchPrices() {
        List<Asset> allAssets = client.getAllAssets();
        for(Asset currAsset: allAssets) {
            handleAsset(currAsset);
        }

        while(true) {
            if(isAllAssetsLoaded()) {
                break;
            }
            exchangeSleep(500);
        }
        Core.updateFinishedExchange(allPairs, getExchangeType());
        unfiromPairStrings();
        setFinishedSync(true);
    }

    @Override
    public void printAllPairs() {
        for(CryptoPair currPair: allPairs) {
            System.out.print(currPair.toString() + "\n");
        }
    }

    private void handleAsset(Asset currAsset) {
        AssetHandler handler = new AssetHandler(currAsset);
        handler.start();
    }



    private Order generateOrderFromOrderBook(String pair, OrderBookEntry entry) {
        Order orderToReturn = new Order();

        orderToReturn.setOrderType(pair);
        orderToReturn.setVolume(Double.parseDouble(entry.getQty()));
        orderToReturn.setPrice(Double.parseDouble(entry.getPrice()));

        return orderToReturn;

    }

    private class AssetHandler extends Thread {
        private Asset asset;
        public AssetHandler(Asset asset) {
            this.asset = asset;
        }

        @Override
        public void run() {
            try {
                addCryptoPair(this.asset.getAssetCode() + "ETH");
                addCryptoPair(this.asset.getAssetCode() + "BTC");
            } catch (Exception e) {
                updateTotalNrOfAssets(-1);
                e.printStackTrace();
            }
        }

        private void addCryptoPair(String pair) {
            try {
                CryptoPair pairToAdd = getCryptoPair(pair);
                addCryptoPairToList(pairToAdd);
                updateTotalNrOfLoadedAssets(1);
            } catch (Exception e) {
                updateTotalNrOfAssets(-1);
            }
        }

        private CryptoPair getCryptoPair(String pair) {
            CryptoPair pairToReturn = new CryptoPair();

            OrderBook orderBook = client.getOrderBook(pair, 5);
            List<OrderBookEntry> allAsks = orderBook.getAsks();
            List<OrderBookEntry> allBds = orderBook.getBids();

            OrderBookEntry bestAsk = allAsks.get(0);
            OrderBookEntry bestBid = allBds.get(0);

            Order bestAskOrder = generateOrderFromOrderBook(pair, bestAsk);
            Order bestBidOrder = generateOrderFromOrderBook(pair, bestBid);

            pairToReturn.setBestAsk(bestAskOrder);
            pairToReturn.setBestBid(bestBidOrder);
            pairToReturn.setCryptoPair(pair);
            pairToReturn.setExchangeType(ExchangeType.BINANCE);

            return pairToReturn;
        }
    }

    private void unfiromPairStrings() {
        for(CryptoPair pair: allPairs) {
            String uniformPair = pair.getCryptoPair().replace("/", "");

            if(uniformPair.contains("BTC")) {
                String other = uniformPair.replace("BTC", "");
                uniformPair = "BTC" + other;
            } else if(uniformPair.contains("ETH")) {
                String other = uniformPair.replace("ETH", "");
                uniformPair = "ETH" + other;
            }

            pair.setCryptoPair(uniformPair);
            pair.getBestAsk().setOrderType(Order.ASK);
            pair.getBestBid().setOrderType(Order.BID);
        }
    }
}
























































