package core;

import exchange.*;
import impl.CoinApiHandler;
import impl.CoreHandler;
import model.ArbitrageOppurtunity;
import model.CryptoPair;
import model.PriceUtils;
import model.ShrimpyHandler;
import org.json.JSONArray;

import java.util.ArrayList;

public class Core {

    private static ArrayList<Exchange> allExchanges;
    private JSONArray allSymbolsJsonArr;

    public Core() {

        System.out.print("Initializing arr...\n");
        initializeAllSymbolsJsonArr();

        System.out.print("Initializing basePrices...\n");
        initializeBasePrices();

        System.out.print("Initializing exchanges...\n");
        initializeExchanges();

        System.out.print("Starting exchanges...\n");
        startAllExchnages();

        System.out.print("Beginning arbitrages...\n");
        getArbitrage();
        //printAll();
    }

    public void printAll() {
        checkIfFinished();
        for(Exchange e: allExchanges) {
            e.printAllPairs();;
        }
    }

    public void checkIfFinished() {
        while(true) {
            if(!isAllExchangeSyncFinished()) {
                System.out.print("Checking....\n");
                sleep(600);
                continue;
            }
            break;
        }
    }

    //Core functionalities
    public void getArbitrage() {
        while(true) {
            if(!isAllExchangeSyncFinished()) {
                System.out.print("Checking....\n");
                sleep(600);
                continue;
            }
            break;
        }

        CoreHandler coreHandler = new CoreHandler();
        ArrayList<ArbitrageOppurtunity> arbitrageOppurtunities = coreHandler.calculateArbitrageOppurtunities(allExchanges);

        //Printing oppurtunities
        for(ArbitrageOppurtunity opp: arbitrageOppurtunities) {
            System.out.print(opp.toString() + "\n");
        }

    }

    public void startAllExchnages() {
        for(Exchange e: allExchanges) {
            e.start();
        }
    }

    public void initializeExchanges() {
        allExchanges = getAllExchanges();
    }


    //Callback funcs
    public static synchronized void updateFinishedExchange(ArrayList<CryptoPair> pairs, String type) {
        for(Exchange currExchange: allExchanges) {
            if(currExchange.getExchangeType().equals(type)) {
                currExchange.setExchangePairs(pairs);
            }
        }
    }

    public boolean isAllExchangeSyncFinished() {
        for(Exchange currExchange: allExchanges) {
            if(!currExchange.isFinishedSync()) {
                System.out.print(currExchange.getExchangeType() + "\n");
                return false;
            }
        }
        return true;
    }

    private void initializeAllSymbolsJsonArr() {
        CoinApiHandler handler = new CoinApiHandler();
        this.allSymbolsJsonArr = handler.getAllSymbolsWithAllInformation();
    }

    private void initializeBasePrices() {
        PriceUtils.ETHValue = ShrimpyHandler.getETHRate();
        PriceUtils.BTCValue = ShrimpyHandler.getBTCRate();
    }

    private ArrayList<Exchange> getAllExchanges() {
        ArrayList<Exchange> allExchanges = new ArrayList<Exchange>();
        Exchange currExchange;

        currExchange = new BinanceExchange(ExchangeType.BINANCE);
        currExchange.setAllSymbolsJsonArr(allSymbolsJsonArr);
        allExchanges.add(currExchange);

        currExchange = new BittrexExchange(ExchangeType.BITTREX);
        currExchange.setAllSymbolsJsonArr(allSymbolsJsonArr);
        allExchanges.add(currExchange);

         currExchange = new OKExchange(ExchangeType.OKEX);
        currExchange.setAllSymbolsJsonArr(allSymbolsJsonArr);
        allExchanges.add(currExchange);

        //currExchange = new HitBtcExchange(ExchangeType.HITBTC);
        //currExchange.setAllSymbolsJsonArr(allSymbolsJsonArr);
        //allExchanges.add(currExchange);

        currExchange = new HuobiExchange(ExchangeType.HUOBI);
        currExchange.setAllSymbolsJsonArr(allSymbolsJsonArr);
        allExchanges.add(currExchange);

        currExchange = new BiboxExchange(ExchangeType.BIBOX);
        currExchange.setAllSymbolsJsonArr(allSymbolsJsonArr);
        allExchanges.add(currExchange);

        currExchange = new PoloniexExchange(ExchangeType.POLONIEX);
        currExchange.setAllSymbolsJsonArr(allSymbolsJsonArr);
        allExchanges.add(currExchange);

        currExchange = new KucoinExchange(ExchangeType.KUCOIN);
        currExchange.setAllSymbolsJsonArr(allSymbolsJsonArr);
        allExchanges.add(currExchange);

        currExchange = new BitmartExchange(ExchangeType.BITMART);
        currExchange.setAllSymbolsJsonArr(allSymbolsJsonArr);
        allExchanges.add(currExchange);

        currExchange = new LivecoinExchange(ExchangeType.LIVECOIN);
        currExchange.setAllSymbolsJsonArr(allSymbolsJsonArr);
        allExchanges.add(currExchange);
        //

        currExchange = new CoinExchange(ExchangeType.COINEXCHANGE);
        currExchange.setAllSymbolsJsonArr(allSymbolsJsonArr);
        allExchanges.add(currExchange);

        currExchange = new Crex24Exchange(ExchangeType.CREX24);
        currExchange.setAllSymbolsJsonArr(allSymbolsJsonArr);
        allExchanges.add(currExchange);

        currExchange = new Crex24Exchange(ExchangeType.YOBIT);
        currExchange.setAllSymbolsJsonArr(allSymbolsJsonArr);
        allExchanges.add(currExchange);

        return allExchanges;
    }

    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {

        }
    }
}



























