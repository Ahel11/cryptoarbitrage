package exchangewallet;

import exchange.KucoinExchange;
import impl.HttpHandler;
import interfaceimpl.ExchangeWalletCheckerImpl;
import org.json.JSONArray;
import org.json.JSONObject;

public class KucoinExchangeWalletChecker implements ExchangeWalletCheckerImpl {

    String apiUrl = "https://api.kucoin.com/api/v1/currencies";

    private JSONArray jsonArrResponse;

    public KucoinExchangeWalletChecker() {
        initialize();
    }

    @Override
    public void initialize() {
        try {
            HttpHandler httpHandler = new HttpHandler();
            String respString = httpHandler.executeGetRequest(apiUrl);
            JSONObject obj = new JSONObject(respString);

            jsonArrResponse = obj.getJSONArray("data");
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isWalletStatusOffline(String pairType) throws Exception {

        for(int i=0; i<jsonArrResponse.length(); i++) {
            JSONObject currJsonObj = jsonArrResponse.getJSONObject(i);
            if(currJsonObj.getString("name").equalsIgnoreCase(pairType)) {
                Boolean isWithdrawalEnabled = currJsonObj.getBoolean("isWithdrawEnabled");
                Boolean isDepositEnabled = currJsonObj.getBoolean("isDepositEnabled");

                if(!isDepositEnabled || !isWithdrawalEnabled) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String args[]) {
        try {
            KucoinExchangeWalletChecker checker = new KucoinExchangeWalletChecker();
            System.out.print(checker.isWalletStatusOffline("IOST"));
        }catch (Exception e ) {
            e.printStackTrace();
        }

    }

}




















































