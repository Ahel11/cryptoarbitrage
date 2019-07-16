package exchangewallet;

import impl.HttpHandler;
import interfaceimpl.ExchangeWalletCheckerImpl;
import interfaceimpl.WalletHandlerImpl;
import org.json.JSONArray;
import org.json.JSONObject;

public class HuobiExchangeWalletChecker implements ExchangeWalletCheckerImpl{

    private String url = "https://www.hbg.com/-/x/pro/v2/beta/common/currencies";
    private JSONArray jsonArrResp;
    private HttpHandler httpHandler;

    public HuobiExchangeWalletChecker() {
        initialize();
    }

    public void initialize() {
        try {
            httpHandler = new HttpHandler();
            String respString = httpHandler.executeGetRequest(url);
            JSONObject currObj = new JSONObject(respString);
            jsonArrResp = currObj.getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isWalletStatusOffline(String pairType) {
        try {
            for(int i=0; i<jsonArrResp.length(); i++) {
                JSONObject currObj = jsonArrResp.getJSONObject(i);
                String currCurrency = currObj.getString("currency_code");
                if(currCurrency.equalsIgnoreCase(pairType)) {
                    Boolean depositEnabled = currObj.getBoolean("deposit_enabled");
                    Boolean withdrawEnabled = currObj.getBoolean("withdraw_enabled");
                    if(depositEnabled && withdrawEnabled) {
                        return false;
                    }
                }
            }
        } catch (Exception e ) {
            e.printStackTrace();
        }

        return true;
    }

    public static void main(String args[]) {
        HuobiExchangeWalletChecker checker = new HuobiExchangeWalletChecker();
        System.out.print(checker.isWalletStatusOffline("ven"));
    }



}
