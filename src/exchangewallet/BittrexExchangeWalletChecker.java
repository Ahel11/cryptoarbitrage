package exchangewallet;

import impl.HttpHandler;
import interfaceimpl.ExchangeWalletCheckerImpl;
import org.json.JSONArray;
import org.json.JSONObject;

public class BittrexExchangeWalletChecker implements ExchangeWalletCheckerImpl {

    private JSONObject walletStatusResp;

    public BittrexExchangeWalletChecker() {
        initialize();
    }

    @Override
    public void initialize() {
        try {
            HttpHandler handler = new HttpHandler();
            walletStatusResp = new JSONObject(handler.executeGetRequest("https://international.bittrex.com/api/v2.0/pub/currencies/GetWalletHealth"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isWalletStatusOffline(String pairType) throws Exception {
        JSONArray currJsonArr = (JSONArray) walletStatusResp.get("result");

        for(int i=0; i<currJsonArr.length(); i++) {
            JSONObject currJsonObj = (JSONObject)currJsonArr.get(i);
            JSONObject health = (JSONObject)currJsonObj.get("Health");
            if(health.getString("Currency").equalsIgnoreCase(pairType)) {
                Boolean isActive = health.getBoolean("IsActive");
                return isActive;
            }
        }
        return false;
    }

    public static void main(String args[]) {
        try {
            BittrexExchangeWalletChecker walletChecker = new BittrexExchangeWalletChecker();
            System.out.print(walletChecker.isWalletStatusOffline("ETC"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}



































