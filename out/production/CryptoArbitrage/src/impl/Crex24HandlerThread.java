package impl;

import exchange.Crex24Exchange;
import model.CryptoPair;

public class Crex24HandlerThread extends Thread{
    private static int totalNrOfAttempts = 5;
    private String pair = "";
    private Crex24Handler handler;

    public Crex24HandlerThread(String pair) {
        this.pair = pair;
        handler = new Crex24Handler();
    }

    public void run() {
        int nrOfRetries = 0;
        CryptoPair pair = null;
        try {
            while(true) {
                pair = attemptToGenerateCryptoPair(this.pair);
                if(pair != null) {
                    break;
                }
                nrOfRetries++;
                Thread.sleep(1900);
                if(nrOfRetries == totalNrOfAttempts) {
                    break;
                }
            }

            if(pair != null) {
                Crex24Exchange.addGeneratedCryptoPair(pair);
                Crex24Exchange.updateTotalNrOfPairsLoaded(1);
            } else {
                Crex24Exchange.updateTotalNrOfPairsToFetch(-1);
            }

        } catch (Exception e) {
            Crex24Exchange.updateTotalNrOfPairsToFetch(-1);
            e.printStackTrace();
        }

    }

    private CryptoPair attemptToGenerateCryptoPair(String pair) {
        try {
            CryptoPair toReturn = handler.generateCryptoPairFromPair(this.pair);
            return toReturn;
        }catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

}


























