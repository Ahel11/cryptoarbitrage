package model;

public class PriceUtils {

    public static double BTCValue = 0;
    public static double ETHValue = 0;

    public synchronized static double calculateCryptoBTCToUsdValue(double amount, double rate) {
        double usdValue = 0;
        double totBtc = amount * rate;
        usdValue = totBtc * BTCValue;

        return usdValue;
    }

    public synchronized static double calculateCryptoETHToUsdValue(double amount, double rate) {
        double usdValue = 0;
        double totBtc = amount * rate;
        usdValue = totBtc * ETHValue;

        return usdValue;
    }

    public synchronized static double generateUSDVolume(String symbol, double amount, double rate) {
        if(symbol.equals("BTC")) {
            return calculateCryptoBTCToUsdValue(amount, rate);
        } else if(symbol.equals("ETH")) {
            return calculateCryptoETHToUsdValue(amount, rate);
        }
        return amount * rate;
    }

}
