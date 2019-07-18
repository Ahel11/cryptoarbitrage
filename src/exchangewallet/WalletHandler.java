package exchangewallet;

import com.thoughtworks.selenium.Selenium;
import exchange.Exchange;
import exchange.ExchangeType;
import interfaceimpl.WalletHandlerImpl;
import selenium.SeleniumHandler;

public class WalletHandler implements WalletHandlerImpl {

    private BinanceExchangeWalletChecker binanceExchangeWalletChecker;
    private BittrexExchangeWalletChecker bittrexExchangeWalletChecker;
    private LivecoinExchangeWalletChecker livcoinExchangeWalletChecker;
    private CoinExchangeWalletChecker coinExchangeWalletChecker;
    private BitmartExchangeWalletChecker bitmartExchangeWalletChecker;
    private HuobiExchangeWalletChecker huobiExchangeWalletChecker;
    private KucoinExchangeWalletChecker kucoinExchangeWalletChecker;
    private SeleniumHandler seleniumHandler;

    public WalletHandler() {

    }

    public void initializeAllWalletStatuses() {
        SeleniumHandler seleniumHandler = new SeleniumHandler();
        bitmartExchangeWalletChecker = seleniumHandler.initializeBitmartWalletChecker();
        binanceExchangeWalletChecker = new BinanceExchangeWalletChecker();
        bittrexExchangeWalletChecker = new BittrexExchangeWalletChecker();
        livcoinExchangeWalletChecker = new LivecoinExchangeWalletChecker();
        coinExchangeWalletChecker = new CoinExchangeWalletChecker();
        huobiExchangeWalletChecker = new HuobiExchangeWalletChecker();
        kucoinExchangeWalletChecker = new KucoinExchangeWalletChecker();
        seleniumHandler.closeBrowser();
    }



    @Override
    public boolean isWalletStatusOffline(String exchangeType, String pairType) {
        try {
            switch (exchangeType) {
                case ExchangeType.BITMART:
                    return bitmartExchangeWalletChecker.isWalletStatusOffline(pairType);

                case ExchangeType.BINANCE:
                    return binanceExchangeWalletChecker.isWalletStatusOffline(pairType);

                case ExchangeType.BITTREX:
                    return bittrexExchangeWalletChecker.isWalletStatusOffline(pairType);

                case ExchangeType.LIVECOIN:
                    return livcoinExchangeWalletChecker.isWalletStatusOffline(pairType);

                case ExchangeType.COINEXCHANGE:
                    return coinExchangeWalletChecker.isWalletStatusOffline(pairType);

                case ExchangeType.HUOBI:
                    return huobiExchangeWalletChecker.isWalletStatusOffline(pairType);

                case ExchangeType.KUCOIN:
                    return kucoinExchangeWalletChecker.isWalletStatusOffline(pairType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void main(String args[]) {
        WalletHandler handler = new WalletHandler();
        handler.initializeAllWalletStatuses();
        System.out.print("NER:\t" + handler.isWalletStatusOffline(ExchangeType.BITMART, "NER") + "\n");
        System.out.print("LQD:\t" + handler.isWalletStatusOffline(ExchangeType.BITMART, "LQD") + "\n");
        System.out.print("MBIT:\t" + handler.isWalletStatusOffline(ExchangeType.BITMART, "MBIT") + "\n");
        System.out.print("JCT:\t" + handler.isWalletStatusOffline(ExchangeType.BITMART, "JCT") + "\n");

    }

}






























