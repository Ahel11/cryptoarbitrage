package impl;

import exchange.Exchange;
import exchange.ExchangeType;
import model.ArbitrageOppurtunity;
import model.CryptoPair;

import java.util.ArrayList;
import java.util.Collections;

public class CoreHandler {

    public CoreHandler() {

    }

    public ArrayList<ArbitrageOppurtunity> calculateArbitrageOppurtunities(ArrayList<Exchange> allExchanges) {
        ArrayList<ArbitrageOppurtunity> oppurtunities = new ArrayList<>();

        // FOR EACH PAIR IN EXCHANGE
        //      FIND SAME PAIR IN OTHER EXCHANGES
        //      CHECK IF ASK ON FIRST EXCHANGE IS LOWER THAN BID ON OTHER EXCHANGE
        //          IF LOWER, GENERATE ARBITRAGE OBJECT AND ADD TO LIST

        for(Exchange currExchange: allExchanges) {
            ArrayList<CryptoPair> allPairsForExchange = currExchange.getExchangePairs();
            for(CryptoPair currCryptoPairForCurrExchange: allPairsForExchange) {
                ArrayList<CryptoPair> pairsFromOtherExchanges = getPairFromOtherExchanges(currCryptoPairForCurrExchange, allExchanges);
                ArrayList<ArbitrageOppurtunity> generatedOppurtunities = generateArbitrageOppurtunities(currCryptoPairForCurrExchange, pairsFromOtherExchanges);
                oppurtunities.addAll(generatedOppurtunities);
            }
        }

        oppurtunities = partitionedOppurtunities(oppurtunities);
        return oppurtunities;
    }

    private ArrayList<ArbitrageOppurtunity> partitionedOppurtunities(ArrayList<ArbitrageOppurtunity> allOppurtunities) {
        ArrayList<ArbitrageOppurtunity> partitionedOppurtunitiesList = new ArrayList<>();

        ArrayList<String> allExchangesList= new ArrayList<>();
        allExchangesList.add(ExchangeType.BINANCE);
        allExchangesList.add(ExchangeType.BITTREX);
        allExchangesList.add(ExchangeType.BITMART);
        allExchangesList.add(ExchangeType.OKEX);
        allExchangesList.add(ExchangeType.COINEXCHANGE);
        allExchangesList.add(ExchangeType.BIBOX);
        allExchangesList.add(ExchangeType.CREX24);
        allExchangesList.add(ExchangeType.HITBTC);
        allExchangesList.add(ExchangeType.HUOBI);
        allExchangesList.add(ExchangeType.BIBOX);
        allExchangesList.add(ExchangeType.KUCOIN);
        allExchangesList.add(ExchangeType.POLONIEX);


        for(String currExchange: allExchangesList) {
            ArrayList<ArbitrageOppurtunity> currOppurtunityList = new ArrayList<>();
            for(ArbitrageOppurtunity currArb: allOppurtunities) {
                if(currArb.getFromExchange().equals(currExchange)) {
                    currOppurtunityList.add(currArb);
                }
            }

            Collections.sort(currOppurtunityList);
            partitionedOppurtunitiesList.addAll(currOppurtunityList);
            currOppurtunityList = new ArrayList<>();
        }

        return partitionedOppurtunitiesList;
    }

    private ArrayList<CryptoPair> getPairFromOtherExchanges(CryptoPair currPair, ArrayList<Exchange> allExchanges) {
        ArrayList<CryptoPair> cryptoPairsFromOtherExchanges = new ArrayList<>();

        for(Exchange currExchange: allExchanges) {
            if(!currExchange.getExchangeType().equals(currPair.getExchangeType())) {

                for(CryptoPair currExchangePair: currExchange.getExchangePairs()) {
                    if(currExchangePair.getCryptoPair().equals(currPair.getCryptoPair())) {
                        cryptoPairsFromOtherExchanges.add(currExchangePair);
                    }
                }
            }
        }

        return cryptoPairsFromOtherExchanges;
    }

    private ArrayList<ArbitrageOppurtunity> generateArbitrageOppurtunities(CryptoPair currPair, ArrayList<CryptoPair> pairsFromOtherExchanges) {
        ArrayList<ArbitrageOppurtunity> arbitrageOppurtunities = new ArrayList<>();

        for(CryptoPair currPairFromOtherExchange: pairsFromOtherExchanges) {
            if(currPair.getBestAsk().getPrice() < currPairFromOtherExchange.getBestBid().getPrice()) {
                ArbitrageOppurtunity opp = generateArbitrageObj(currPair, currPairFromOtherExchange);
                arbitrageOppurtunities.add(opp);
            }
        }

        return arbitrageOppurtunities;
    }

    private ArbitrageOppurtunity generateArbitrageObj(CryptoPair askPair, CryptoPair bidPair) {
        ArbitrageOppurtunity oppurtunity = new ArbitrageOppurtunity();

        double perc = 100 * ((bidPair.getBestBid().getPrice() / askPair.getBestAsk().getPrice()) - 1);

        double minVolume = askPair.getBestAsk().getVolume();
        if(bidPair.getBestBid().getVolume() < minVolume) {
            minVolume = bidPair.getBestBid().getVolume();
        }

        if(minVolume == 0) {
            System.out.print("HERE\n");
        }

        String buyFrom = askPair.getExchangeType();
        String sellTo = bidPair.getExchangeType();

        oppurtunity.setMinVolume(minVolume);
        oppurtunity.setProfitPerc(perc);
        oppurtunity.setBuyPrice(askPair.getBestAsk().getPrice());
        oppurtunity.setSellPrice(bidPair.getBestBid().getPrice());
        oppurtunity.setFromExchange(askPair.getExchangeType());
        oppurtunity.setToExchange(bidPair.getExchangeType());
        oppurtunity.setPairType(askPair.getCryptoPair());

        return oppurtunity;
    }

}
























































