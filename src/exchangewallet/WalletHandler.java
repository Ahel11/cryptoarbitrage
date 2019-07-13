package exchangewallet;

import exchange.Exchange;
import exchange.ExchangeType;
import interfaceimpl.WalletHandlerImpl;

public class WalletHandler implements WalletHandlerImpl {

    private BinanceExchangeWalletChecker binanceExchangeWalletChecker;
    private BittrexExchangeWalletChecker bittrexExchangeWalletChecker;

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
                    break;

                case ExchangeType.COINEXCHANGE:
                    break;
            }
        } catch (Exception e) {

        }


        return false;
    }
}






























