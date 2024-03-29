package impl;

import exchange.Exchange;
import exchange.ExchangeType;
import exchangewallet.WalletHandler;
import model.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class CoreHandler {

    private WalletHandler walletHandler;
    private double maxArbitrageCash = 3000;
    private double minPerc = 1.3;
    private double maxPerc = 60;
    private ProfitHandler profitHandler = new ProfitHandler();

    private double minProfitDollar = 1;
    private double maxProfitDollar = 200;

    public CoreHandler() {

    }


    //


    public ArrayList<ArbitrageOppurtunity> calculateArbitrageOppurtunities(ArrayList<Exchange> allExchanges) {
        ArrayList<ArbitrageOppurtunity> oppurtunities = new ArrayList<>();

        for(Exchange currExchange: allExchanges) {
            ArrayList<CryptoPair> allPairsForExchange = currExchange.getExchangePairs();
            for(CryptoPair currCryptoPairForCurrExchange: allPairsForExchange) {
                ArrayList<CryptoPair> pairsFromOtherExchanges = getPairFromOtherExchanges(currCryptoPairForCurrExchange, allExchanges);
                if(pairsFromOtherExchanges.size() == 0) {
                    continue;
                }
                ArrayList<ArbitrageOppurtunity> generatedOppurtunities = generateArbitrageOppurtunities(currCryptoPairForCurrExchange, pairsFromOtherExchanges);
                oppurtunities.addAll(generatedOppurtunities);
            }
        }

        //oppurtunities = partitionedOppurtunities(oppurtunities);
        oppurtunities = applyFilter(oppurtunities);
        Collections.sort(oppurtunities);
        return oppurtunities;
    }

    private ArrayList <ArbitrageOppurtunity> applyFilter(ArrayList<ArbitrageOppurtunity> oppList) {
        ArrayList<ArbitrageOppurtunity> filtered = new ArrayList<>();

        for(ArbitrageOppurtunity currOpp: oppList) {
            if(currOpp.getPairType().contains("xlr") || currOpp.getPairType().contains("XLR")){
                System.out.print("here\n");
            }
            if(passesFilter(currOpp)) {
                filtered.add(currOpp);
            }
        }

        return filtered;
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
                opp.setAskPair(currPair);
                opp.setBidPair(currPairFromOtherExchange);
                arbitrageOppurtunities.add(opp);
            }
        }

        return arbitrageOppurtunities;
    }

    private ArbitrageOppurtunity generateArbitrageObj(CryptoPair askPair, CryptoPair bidPair) {
        ArbitrageOppurtunity oppurtunity = new ArbitrageOppurtunity();

        double perc = 100 * ((bidPair.getBestBid().getPrice() / askPair.getBestAsk().getPrice()) - 1);

        //LEFT -> BUYING
        //

        //ProfitResult profitResult = profitHandler.calculateProfit(askPair.getCryptoPair(), bidPair.getBestBid(), (ArrayList)askPair.getAskOrders());

        //oppurtunity.setMinVolume(minVolume);
        //oppurtunity.setRes(profitResult);
        oppurtunity.setProfitDollar(0);
        oppurtunity.setProfitPerc(perc);
        oppurtunity.setBuyPrice(askPair.getBestAsk().getPrice());
        oppurtunity.setSellPrice(bidPair.getBestBid().getPrice());
        oppurtunity.setFromExchange(askPair.getExchangeType());
        oppurtunity.setToExchange(bidPair.getExchangeType());
        oppurtunity.setPairType(askPair.getCryptoPair());

        return oppurtunity;
    }

    private double calculateProfitDollar(CryptoPair askPair, CryptoPair bidPair) {
        double profit = 0;
        double totalSellVolume = bidPair.getBestBid().getVolume();
        double totalBuyPrice = 0;
        double totalSellPrice = 0;
        double adjustedVolume = 0;
        double volumeToUse = 0;
        String actionFirst = "";
        String actionSecond = "";
        DecimalFormat format = new DecimalFormat("#.#########");

        if(totalSellVolume > askPair.getBestAsk().getVolume()) {
            volumeToUse = askPair.getBestAsk().getVolume();
            adjustedVolume = adjustVolumeFromBalance(askPair.getBestAsk().getVolume(), askPair.getBestAsk().getPrice(), askPair.getCryptoPair());
            if(askPair.getBestAsk().getVolume() > adjustedVolume) {
                volumeToUse = adjustedVolume;
            }

            totalBuyPrice =  volumeToUse * askPair.getBestAsk().getPrice();
            totalSellPrice = volumeToUse * bidPair.getBestBid().getPrice();


            profit = totalSellPrice - totalBuyPrice;
            actionFirst = "TotalBuyPrice:\t" + volumeToUse + " * " + askPair.getBestAsk().getPrice() + " = " + totalBuyPrice + "\n";
            actionFirst = actionFirst + "TotalSellPrice:\t" + volumeToUse + " * " + bidPair.getBestBid().getPrice() + " = " + totalSellPrice + "\n";
        } else {
            volumeToUse = bidPair.getBestBid().getVolume();
            adjustedVolume = adjustVolumeFromBalance(bidPair.getBestBid().getVolume(), bidPair.getBestBid().getPrice(), askPair.getCryptoPair());
            if(bidPair.getBestBid().getVolume() > adjustedVolume) {
                volumeToUse = adjustedVolume;
            }

            totalBuyPrice = volumeToUse * askPair.getBestAsk().getPrice();
            totalSellPrice = volumeToUse * bidPair.getBestBid().getPrice();


            actionSecond = "TotalBuyPrice:\t" + volumeToUse + " * " + askPair.getBestAsk().getPrice() + " = " + totalBuyPrice + "\n";
            actionSecond = actionSecond + "TotalSellPrice:\t" + volumeToUse + " * " + bidPair.getBestBid().getPrice() + " = " + totalSellPrice + "\n";
            profit = totalSellPrice - totalBuyPrice;
        }

        if(askPair.getCryptoPair().contains("BTC")) {
            profit = profit * PriceUtils.BTCValue;
        } else if(askPair.getCryptoPair().contains("ETH")) {
            profit = profit * PriceUtils.ETHValue;
        }

        return profit;
    }

    private double adjustVolumeFromBalance(double volume, double price, String currency) {
        double adjustedVolume = 0;

        if(currency.contains("BTC")) {
            adjustedVolume = (this.maxArbitrageCash / (price * PriceUtils.BTCValue));
        } else if(currency.contains("ETH")) {
            adjustedVolume = (this.maxArbitrageCash / (price * PriceUtils.ETHValue));
        } else if(currency.contains("USDT")) {
            adjustedVolume = (this.maxArbitrageCash / (price));
        }

        return adjustedVolume;
    }

    private boolean passesFilter(ArbitrageOppurtunity currOpp) {
        ArrayList<String> excludedCoins = excludedCoins();
        for(String s: excludedCoins) {
            if(currOpp.getPairType().contains(s)) {
                return false;
            }
        }

        if(currOpp.getPairType().contains("USD") && !currOpp.getPairType().contains("USDT")) {
            return false;
        }

        if(pairContainsUsdType(currOpp.getPairType()) && (currOpp.getPairType().contains("BTC") || currOpp.getPairType().contains("ETH") || currOpp.getPairType().contains("LTC"))) {
            return false;
        }

        if(currOpp.getPairType().contains("RUB")) {
            return false;
        }

        if(currOpp.getProfitPerc() <= minPerc || currOpp.getProfitPerc() >= maxPerc) {
            return false;
        }
        if(currOpp.getPairType().contains("xlr") || currOpp.getPairType().contains("XLR")) {
            System.out.print("here\n");
        }
        boolean isWalletOfflineFromEx = this.walletHandler.isWalletStatusOffline(currOpp.getFromExchange(), getOtherCurr(currOpp.getPairType()));
        boolean isWalletOfflineToEx = this.walletHandler.isWalletStatusOffline(currOpp.getToExchange(), getOtherCurr(currOpp.getPairType()));

        if(isWalletOfflineFromEx || isWalletOfflineToEx) {
            return false;
        }

        return true;
    }

    private String getOtherCurr(String pairType) {
        if(pairType.contains("XLR") || pairType.contains("xlr")) {
            System.out.print("here\n");
        }
        String toReturn = "";

        toReturn = pairType.replace("BTC", "");
        toReturn = toReturn.replace("ETH", "");
        toReturn = toReturn.replace("LTC", "");

        return toReturn;
    }

    private boolean pairContainsUsdType(String pair) {
        return pair.contains("USDT") || pair.contains("USD") || pair.contains("PAX") ||pair.contains("DAI");
    }

    private ArrayList<String> excludedCoins() {
        ArrayList<String> excludedCoins = new ArrayList<>();

        //Add coins to be excluded
        excludedCoins.add("RUS");
        excludedCoins.add("EUR");
        excludedCoins.add("JPY");

        return excludedCoins;
    }

    public WalletHandler getWalletHandler() {
        return walletHandler;
    }

    public void setWalletHandler(WalletHandler walletHandler) {
        this.walletHandler = walletHandler;
    }
}
























































