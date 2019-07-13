package exchangewallet;

import impl.HttpHandler;
import interfaceimpl.ExchangeWalletCheckerImpl;
import org.json.JSONArray;
import org.json.JSONObject;

public class BinanceExchangeWalletChecker implements ExchangeWalletCheckerImpl {

    private JSONArray walletStatusResp = new JSONArray();

    public BinanceExchangeWalletChecker() {
        initialize();
    }

    @Override
    public void initialize() {
        try {
            HttpHandler handler = new HttpHandler();
            String resp = handler.executeGetRequest("https://www.binance.com/assetWithdraw/getAllAsset.html");
            walletStatusResp = new JSONArray(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isWalletStatusOffline(String pairTypeName) throws Exception{
        for(int i=0; i<walletStatusResp.length(); i++) {
            JSONObject currObj = (JSONObject) walletStatusResp.get(i);
            String assetName = currObj.getString("assetCode");
            if(assetName.equalsIgnoreCase(pairTypeName)) {
                Boolean enableCharge = currObj.getBoolean("enableCharge");
                Boolean enableWithdraw = currObj.getBoolean("enableWithdraw");
                if(!enableCharge || !enableWithdraw) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String args[]) {
        try {
            BinanceExchangeWalletChecker checker = new BinanceExchangeWalletChecker();
            boolean isWalletOffLine = checker.isWalletStatusOffline("ZIL");
            System.out.print(isWalletOffLine + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

























