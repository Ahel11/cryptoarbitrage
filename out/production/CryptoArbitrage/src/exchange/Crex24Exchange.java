package exchange;

import core.Core;
import impl.Crex24Handler;
import impl.Crex24HandlerThread;
import model.CryptoPair;
import model.ExchangeHelper;

import java.util.ArrayList;

public class Crex24Exchange extends Exchange{

    private static ArrayList<CryptoPair> allCryptoPairs = new ArrayList<>();
    private static int totalNrOfPairsToFetch = 0;
    private static int totanlNrOfPairsLoaded = 0;

    public Crex24Exchange(String type) {
        allCryptoPairs = new ArrayList<>();

        setExchangeType(type);
    }

    public static synchronized void addGeneratedCryptoPair(CryptoPair pair) {
        allCryptoPairs.add(pair);
    }

    public static synchronized void updateTotalNrOfPairsToFetch(int nr) {
        totalNrOfPairsToFetch = totalNrOfPairsToFetch + nr;
    }

    public static synchronized void updateTotalNrOfPairsLoaded(int nr) {
        totanlNrOfPairsLoaded = totanlNrOfPairsLoaded + nr;
    }

    public static synchronized boolean isFinishedLoadingAllPairs() {
        System.out.print("Loaded:\t" + totanlNrOfPairsLoaded + "\tTotal:\t" + totalNrOfPairsToFetch + "\n");
        return totanlNrOfPairsLoaded >= (totalNrOfPairsToFetch-2);
    }


    @Override
    public void run() {
        synchPrices();
    }

    @Override
    public void synchPrices() {
        try {
            Crex24Handler handler = new Crex24Handler();
            ArrayList<String> allPairs = handler.generatePairsToFetch();
            totalNrOfPairsToFetch = allPairs.size();
            int currThreads = 0;

            for(String pair: allPairs) {
                Crex24HandlerThread currThread = new Crex24HandlerThread(pair);
                currThread.start();
                currThreads += 1;
                if(currThreads == 50) {
                    exchangeSleep(200);
                    currThreads = 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        while(!isFinishedLoadingAllPairs()) {
            exchangeSleep(500);
        }

        Core.updateFinishedExchange(allCryptoPairs, ExchangeType.CREX24);
        unfiromPairStrings();
        setFinishedSync(true);

    }

    @Override
    public void printAllPairs() {
        for(CryptoPair currPair: allCryptoPairs) {
            System.out.print(currPair.toString() + "\n");
        }
        System.out.print("\nSIZE OF " + this.getExchangeType() + "\t" + allCryptoPairs.size() + "\n\n");
    }

    private void unfiromPairStrings() {
        ExchangeHelper helper = new ExchangeHelper();
        helper.uniformPairs(allCryptoPairs);
    }

}












































