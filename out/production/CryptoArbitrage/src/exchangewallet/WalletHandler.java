package exchangewallet;

import exchange.Exchange;
import exchange.ExchangeType;
import interfaceimpl.WalletHandlerImpl;

public class WalletHandler implements WalletHandlerImpl {

    private BinanceExchangeWalletChecker binanceExchangeWalletChecker;
    private BittrexExchangeWalletChecker bittrexExchangeWalletChecker;
    private LivecoinExchangeWalletChecker livcoinExchangeWalletChecker;
    private CoinExchangeWalletChecker coinExchangeWalletChecker;

    public WalletHandler() {
        initializeAllWalletStatuses();
    }

    private void initializeAllWalletStatuses() {
        binanceExchangeWalletChecker = new BinanceExchangeWalletChecker();
        bittrexExchangeWalletChecker = new BittrexExchangeWalletChecker();
    }

    @Override
    public boolean checkWalletStatus(String exchangeType, String pairType) {

        try {
            switch (exchangeType) {
                case ExchangeType.BINANCE:
                    return binanceExchangeWalletChecker.isWalletStatusOffline(pairType);

                case ExchangeType.BITTREX:
                    return bittrexExchangeWalletChecker.isWalletStatusOffline(pairType);

                case ExchangeType.LIVECOIN:
                    return livcoinExchangeWalletChecker.isWalletStatusOffline(pairType);

                case ExchangeType.COINEXCHANGE:
                    return coinExchangeWalletChecker.isWalletStatusOffline(pairType);
            }
        } catch (Exception e) {

        }


        return false;
    }
}






























