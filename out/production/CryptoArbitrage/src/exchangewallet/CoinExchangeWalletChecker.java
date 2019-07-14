package exchangewallet;

import impl.HttpHandler;
import interfaceimpl.ExchangeWalletCheckerImpl;
import org.json.JSONArray;
import org.json.JSONObject;

public class CoinExchangeWalletChecker implements ExchangeWalletCheckerImpl {

    private String allCurrencyIdUrlFetch = "https://www.coinexchange.io/api/v1/getmarkets";
    private String walletStatusUrlFetch = "https://www.coinexchange.io/api/v1/getcurrencies";
    private JSONObject allCurrencyListRespJosn;
    private JSONObject allWalletStatusJson;


    public CoinExchangeWalletChecker() {
        initialize();
    }

    @Override
    public void initialize() {
        try {
            HttpHandler handler = new HttpHandler();
            allCurrencyListRespJosn = new JSONObject(handler.executeGetRequest(allCurrencyIdUrlFetch));
            allWalletStatusJson = new JSONObject(handler.executeGetRequest(walletStatusUrlFetch));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isWalletStatusOffline(String pairType) throws Exception {

        boolean isMarketIdWalletOff = isWalletForPairOff(pairType);
        if(isMarketIdWalletOff) {
            return true;
        }
        return false;
    }

    private String getMarketIdForPair(String pair) {
        try {
            JSONArray currArr = allCurrencyListRespJosn.getJSONArray("result");
            for(int i=0; i<currArr.length(); i++) {
                JSONObject currJsonObj = currArr.getJSONObject(i);
                if(currJsonObj.getString("MarketAssetCode").equalsIgnoreCase(pair)) {
                    return currJsonObj.getString("MarketID");
                }
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private boolean isWalletForPairOff(String pair) {
        try {
            JSONArray resultJsonArr = allWalletStatusJson.getJSONArray("result");
            for(int i=0; i<resultJsonArr.length(); i++) {
                JSONObject currJsonObj = resultJsonArr.getJSONObject(i);
                if(currJsonObj.getString("TickerCode").equalsIgnoreCase(pair)) {
                    String walletStatus = currJsonObj.getString("WalletStatus");
                    if(!walletStatus.equalsIgnoreCase("online")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String args[]) {
        try {
            CoinExchangeWalletChecker walletChecker = new CoinExchangeWalletChecker();
            System.out.print(walletChecker.isWalletStatusOffline("DNT"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
















































