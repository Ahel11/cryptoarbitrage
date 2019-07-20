package core;

import exchange.*;
import exchangewallet.WalletHandler;
import impl.CoinApiHandler;
import impl.CoreHandler;
import model.ArbitrageOppurtunity;
import model.CryptoPair;
import model.PriceUtils;
import model.ShrimpyHandler;
import org.json.JSONArray;

import java.util.ArrayList;

public class Core {

    private CoreHandler coreHandler;
    private static ArrayList<Exchange> allExchanges;
    private JSONArray allSymbolsJsonArr;

    public Core() {
        //initializeExchangesCoinApi();
        startExchangesShrimpy();
    }

    public void startExchangesShrimpy() {
        System.out.print("Initializing components...\n");
        initiailize();

        System.out.print("Initializing walletHandler...\n");
        initializeWalletHandler();

        System.out.print("Initializing exchanges...\n");
        initializeExchangesShrimpy();

        System.out.print("Starting all exchanges...\n");
        startAllExchnages();

        System.out.print("Get arbitrage...\n");
        //printAll();
        getArbitrage();
    }

    public void initializeExchangesCoinApi() {
        System.out.print("Initializing wallet handler..");
        //initializeWalletHandler();

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
        printEmptyExchanges();
        //printAll();
    }

    public void printAll() {
        checkIfFinished();
        for(Exchange e: allExchanges) {
            e.printAllPairs();
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
        allExchanges = getAllExchangesFromName();
    }

    public void initializeExchangesShrimpy() {
        allExchanges = getAllExchangesShrimpy();
    }

    public void initializeWalletHandler() {
        WalletHandler walletHandler = new WalletHandler();
        walletHandler.initializeAllWalletStatuses();
        this.coreHandler.setWalletHandler(walletHandler);
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
        PriceUtils.DOGEValue = ShrimpyHandler.getDOGERate();
    }

    private ArrayList<Exchange> getAllExchangesFromName() {
        ArrayList<Exchange> exchangesList = new ArrayList<>();

        ArrayList<String> allExNames = getAllExchangeNames();
        for(String currExName: allExNames) {
            Exchange currEx = new Exchange(currExName);
            currEx.setAllSymbolsJsonArr(allSymbolsJsonArr);
            exchangesList.add(currEx);
        }

        return exchangesList;
    }

    private ArrayList<String> getAllExchangeNames() {
        ArrayList<String> allowedExchanges = new ArrayList<>();

        allowedExchanges.add("BINANCE");
        allowedExchanges.add("BITTREX");
        allowedExchanges.add("BITFINEX");
        allowedExchanges.add("KRAKEN");
        allowedExchanges.add("OKEX");
        allowedExchanges.add("HUOBIPRO");
        allowedExchanges.add("BIBOX");
        allowedExchanges.add("KUCOIN");
        allowedExchanges.add("BITMART");
        allowedExchanges.add("CREX24");
        allowedExchanges.add("LIVECOIN");
        allowedExchanges.add("COINEXCHANGE");
        //allowedExchanges .add("YOBIT");
        allowedExchanges.add("COINBENE");
        allowedExchanges.add("FATBTC");
       // allowedExchanges.add("COINEGG");


        allowedExchanges.add("IDAX");
        allowedExchanges.add("BITFOREX");
        allowedExchanges.add("DIGIFINEX");
        allowedExchanges.add("IDCM");
        allowedExchanges.add("COINTIGER");
        allowedExchanges.add("BITKER");
        allowedExchanges.add("RIGHTBTC");
        allowedExchanges.add("ZB");
        allowedExchanges.add("Bilaxy");
        allowedExchanges.add("DRAGONEX");
        allowedExchanges.add("SIMEX");
        allowedExchanges.add("TOKOK");
        allowedExchanges.add("kryptono");

        allowedExchanges.add("MERCATOX");
        allowedExchanges.add("STEX");
        allowedExchanges.add("QUOINE");
        allowedExchanges.add("COSS");
        allowedExchanges.add("TIDEX");
        allowedExchanges.add("IDEX");

        return allowedExchanges;
    }

    private ArrayList<Exchange> getAllExchangesShrimpy() {
        ArrayList<Exchange> allExchangesForShrimpy = new ArrayList<>();

        //AllExchanges for Shrimpy
        allExchangesForShrimpy.add(generateExchange(ExchangeType.KUCOIN));
        allExchangesForShrimpy.add(generateExchange(ExchangeType.BIBOX));
        allExchangesForShrimpy.add(generateExchange(ExchangeType.BINANCE));
        allExchangesForShrimpy.add(generateExchange(ExchangeType.BITTREX));
        allExchangesForShrimpy.add(generateExchange(ExchangeType.BITMART));
        allExchangesForShrimpy.add(generateExchange(ExchangeType.HUOBI));
        allExchangesForShrimpy.add(generateExchange(ExchangeType.OKEX));

        //All nonShrimpy exchanges
        CoinExchange coinExchange = new CoinExchange(ExchangeType.COINEXCHANGE);
        allExchangesForShrimpy.add(coinExchange);

        Crex24Exchange crex24Exchange = new Crex24Exchange(ExchangeType.CREX24);
        allExchangesForShrimpy.add(crex24Exchange);


        return allExchangesForShrimpy;
    }


    //Helper funcs

    private void initiailize() {
        initializeBasePrices();
        this.coreHandler = new CoreHandler();
    }

    private Exchange generateExchange(String exchangeName) {
        Exchange currExchange = new Exchange(exchangeName);
        return currExchange;
    }

    private void printEmptyExchanges() {
        /*System.out.print("\n\nALL EMPTY EXCHANGES:\n\n");
        for(Exchange e: allExchanges) {
            if(e.getAllCryptoPairsFromJsonArr().size() == 0) {
                System.out.print(e.getExchangeType() + "\n");
            }
        }*/
    }

    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {

        }
    }



}



























