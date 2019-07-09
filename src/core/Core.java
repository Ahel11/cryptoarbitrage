package core;

import exchange.*;
import impl.CoreHandler;
import model.ArbitrageOppurtunity;
import model.CryptoPair;
import model.PriceUtils;
import model.ShrimpyHandler;

import java.util.ArrayList;

public class Core {

    private static ArrayList<Exchange> allExchanges;

    public Core() {
        initializeBasePrices();
        initializeExchanges();
        startAllExchnages();
        getArbitrage();
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
                return false;
            }
        }
        return true;
    }

    private void initializeBasePrices() {
        PriceUtils.ETHValue = ShrimpyHandler.getETHRate();
        PriceUtils.BTCValue = ShrimpyHandler.getBTCRate();
    }

    private ArrayList<Exchange> getAllExchanges() {
        ArrayList<Exchange> allExchanges = new ArrayList<Exchange>();
        Exchange currExchange;

        currExchange = new BinanceExchange(ExchangeType.BINANCE);
        allExchanges.add(currExchange);

        //currExchange = new BittrexExchange(ExchangeType.BITTREX);
        //allExchanges.add(currExchange);

        currExchange = new OKExchange(ExchangeType.OKEX);
        allExchanges.add(currExchange);

        currExchange = new HitBtcExchange(ExchangeType.HITBTC);
        allExchanges.add(currExchange);

        currExchange = new HuobiExchange(ExchangeType.HUOBI);
        allExchanges.add(currExchange);

        currExchange = new BiboxExchange(ExchangeType.BIBOX);
        allExchanges.add(currExchange);

        currExchange = new PoloniexExchange(ExchangeType.POLONIEX);
        allExchanges.add(currExchange);

        currExchange = new KucoinExchange(ExchangeType.KUCOIN);
        allExchanges.add(currExchange);

        currExchange = new BitmartExchange(ExchangeType.BITMART);
        allExchanges.add(currExchange);

        //currExchange = new Crex24Exchange(ExchangeType.CREX24);
        //allExchanges.add(currExchange);

        currExchange = new CoinExchange(ExchangeType.COINEXCHANGE);
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



























