package core;

import exchange.*;
import model.CryptoPair;

import java.util.ArrayList;

public class Core {

    private static ArrayList<Exchange> allExchanges;

    public Core() {
        initializeExchanges();
        startAllExchnages();
        getArbitrage();
    }

    //Core functionalities
    public void getArbitrage() {
        while(true) {
            if(!isAllExchangeSyncFinished()) {
                sleep(600);
                continue;
            }
            break;
        }

        for(Exchange currExchange: allExchanges) {
            System.out.print("Printing all " + currExchange.getExchangeType() + "\t" + "\n\n\n\n");
            currExchange.printAllPairs();
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

    private ArrayList<Exchange> getAllExchanges() {
        ArrayList<Exchange> allExchanges = new ArrayList<Exchange>();
        Exchange currExchange;

        //currExchange = new BinanceExchange(ExchangeType.BINANCE);
       // allExchanges.add(currExchange);

        currExchange = new BittrexExchange(ExchangeType.BITTREX);
        allExchanges.add(currExchange);

        //currExchange = new OKExchange(ExchangeType.OKEX);
        //allExchanges.add(currExchange);

        return allExchanges;
    }

    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {

        }
    }
}



























