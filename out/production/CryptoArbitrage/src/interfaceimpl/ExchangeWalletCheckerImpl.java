package interfaceimpl;

public interface ExchangeWalletCheckerImpl {

    void initialize();
    boolean isWalletStatusOffline(String pairType) throws Exception;

}
