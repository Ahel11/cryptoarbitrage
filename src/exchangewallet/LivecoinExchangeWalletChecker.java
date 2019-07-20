package exchangewallet;

import impl.HttpHandler;
import interfaceimpl.ExchangeWalletCheckerImpl;
import org.json.JSONArray;
import org.json.JSONObject;

public class LivecoinExchangeWalletChecker implements ExchangeWalletCheckerImpl {

    JSONObject respJson = new JSONObject();

    public LivecoinExchangeWalletChecker() {
        initialize();
    }

    @Override
    public void initialize() {
        try {
            HttpHandler handler = new HttpHandler();
            respJson = new JSONObject(handler.executeGetRequest("https://api.livecoin.net/info/coinInfo"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isWalletStatusOffline(String pairType) throws Exception {
        JSONArray arr = respJson.getJSONArray("info");

        for(int i=0; i<arr.length(); i++) {
            JSONObject currOb = arr.getJSONObject(i);
            if(currOb.getString("symbol").equalsIgnoreCase(pairType)) {
                if(!currOb.getString("walletStatus").equalsIgnoreCase("normal")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void main(String args[]) {
        try {
            LivecoinExchangeWalletChecker checker = new LivecoinExchangeWalletChecker();
            System.out.print(checker.isWalletStatusOffline("BIT"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}














































