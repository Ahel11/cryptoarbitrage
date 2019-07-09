package impl;

import exchange.Exchange;
import model.ArbitrageOppurtunity;
import model.CryptoPair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
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

        Collections.sort(oppurtunities);
        return oppurtunities;
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

        double maxVolume = askPair.getBestAsk().getVolume();
        if(bidPair.getBestBid().getVolume() > maxVolume) {
            maxVolume = bidPair.getBestBid().getVolume();
        }

        String buyFrom = askPair.getExchangeType();
        String sellTo = bidPair.getExchangeType();

        oppurtunity.setMaxVolume(maxVolume);
        oppurtunity.setProfitPerc(perc);
        oppurtunity.setBuyPrice(askPair.getBestAsk().getPrice());
        oppurtunity.setSellPrice(bidPair.getBestBid().getPrice());
        oppurtunity.setFromExchange(askPair.getExchangeType());
        oppurtunity.setToExchange(bidPair.getExchangeType());
        oppurtunity.setPairType(askPair.getCryptoPair());

        return oppurtunity;
    }

}
























































