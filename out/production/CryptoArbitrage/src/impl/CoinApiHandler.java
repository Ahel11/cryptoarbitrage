package impl;

import model.CryptoPair;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;

public class CoinApiHandler {

    private static String ordersUrl = "https://rest.coinapi.io/v1/orderbooks/current";

    private ArrayList<String> keys = new ArrayList<>();
    private int keyIndex = 0;
    private HttpHandler httpHtttpHandler;


    public CoinApiHandler() {
        initializeKeys();
        httpHtttpHandler = new HttpHandler();
    }

    private void initializeKeys() {
        keys.add("415A9330-D667-4BFA-872B-A686DE890897"); //ael7
        keys.add("BF277330-BCED-43FF-A339-C014A900F9CB"); // swtransbui
    }


    public JSONArray getAllSymbolsWithAllInformation() {
        JSONArray arrResp = null;
        try {
            String respJsonArr = httpHtttpHandler.executeGetRequest(ordersUrl, getCurrentHeader());
            arrResp = new JSONArray(respJsonArr);
        }catch (Exception e ) {
            e.printStackTrace();
        }
        return arrResp;
    }

    private BasicHeader getCurrentHeader() {
        String currHeaderKey = this.keys.get(keyIndex);
        BasicHeader currentHeader = new BasicHeader("X-CoinAPI-Key", currHeaderKey);
        return currentHeader;
    }

    public static void main(String args[]) {
        try {
            CoinApiParser parser = new CoinApiParser();
            CoinApiHandler handler = new CoinApiHandler();
            long msStart = System.currentTimeMillis();
            JSONArray arr = handler.getAllSymbolsWithAllInformation();
            long msEnd = System.currentTimeMillis();

            for(int i=12400; i<13450; i++) {
                JSONObject currJsonOb = (JSONObject)arr.getJSONObject(i);
                CryptoPair currCryptoPair = parser.parseCoinApiObjToPair(currJsonOb);

                if(currCryptoPair != null) {
                    System.out.print(currCryptoPair.toString() + "\n");
                }
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }


        System.out.print("SUCCESS");
    }

}






































