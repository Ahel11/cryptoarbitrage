package scratch;

import exchange.Exchange;
import impl.HttpHandler;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.Collections;

public class ScratchMain {
    private static String symbolsUrl = "https://rest.coinapi.io/v1/symbols";
    private static String orderBooksUrl = "https://rest.coinapi.io/v1/orderbooks/current?filter_symbol_id=";

    public static void main(String args[]) {
        //checkBestProfit();
        //orderBookTest();


    }

    public static void test() {


    }


    public static void orderBookTest() {
        try {
            String orderBook = "https://rest.coinapi.io/v1/orderbooks/current?limit=5";
            HttpHandler handler = new HttpHandler();
            Header header = new BasicHeader("X-CoinAPI-Key", "BF277330-BCED-43FF-A339-C014A900F9CB");
            String respString = handler.executeGetRequest(orderBook, header);

            System.out.print("RESPSTRINGLENGTH:\t" + respString.length() + "\n");
            JSONArray arr = new JSONArray(respString);


            for(int i=3000; i<3100; i++) {
                JSONObject currObj = (JSONObject)arr.getJSONObject(i);
            }

            System.out.print(respString);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void checkCoinApi() {
        String symbolIdArr = "{";
        try {
            HttpHandler handler = new HttpHandler();
            Header header = new BasicHeader("X-CoinAPI-Key", "BF277330-BCED-43FF-A339-C014A900F9CB");
            String respString = handler.executeGetRequest(symbolsUrl, header);


            JSONArray respJsonArr = new JSONArray(respString);

            for(int i=1000; i<1005; i++) {
                JSONObject currObj = (JSONObject)respJsonArr.getJSONObject(i);
                if(symbolIdArr.equals("{")){
                    symbolIdArr = symbolIdArr + currObj.getString("symbol_id");
                } else {
                    symbolIdArr = symbolIdArr + "," + currObj.getString("symbol_id");
                }
            }
            symbolIdArr = symbolIdArr + "}";

            System.out.print("\n\n" + symbolIdArr);

            String urlForOrderBook = orderBooksUrl + symbolIdArr;
            String orderBookResp = handler.executeGetRequest(urlForOrderBook, header);

            System.out.print("\n\n\n\nOrderBookLength:\t" + orderBookResp.length() + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public static void checkBestProfit() {

        ArrayList<PriceHolder> priceListCrex = getPricelistCrex();
        ArrayList<Result> results = new ArrayList<>();

        double BTCPrice = 11700;

        double priceBittrex = 0.00000059;

        for(int i=0; i<priceListCrex.size(); i++) {
            double tokensNr = calculateTokenNrToBuy(i, priceListCrex);
            double costToBuy = calculateCostToBuy(i, priceListCrex);
            double costToSell = tokensNr * priceBittrex;

            double profit = costToSell - costToBuy;
            profit = profit * BTCPrice;
            Result currRes = new Result();

            currRes.setCostToBuy(costToBuy);
            currRes.setTokenNr(tokensNr);
            currRes.setProfit(profit);
            results.add(currRes);
        }

        Collections.sort(results);

        for(Result r: results) {
            System.out.print(r.toString() + "\n");
        }

    }



    public static double calculateTokenNrToBuy(int index, ArrayList<PriceHolder> priceList) {
        double tokensNr = 0;

        for(int i=0; i<=index; i++) {
            tokensNr = tokensNr + priceList.get(i).getVolume();
        }
        return tokensNr;
    }

    public static double calculateCostToBuy(int index, ArrayList<PriceHolder> priceList){
        double costToBuy = 0;

        for(int i=0; i<=index; i++) {
            costToBuy = costToBuy + priceList.get(i).getPrice() * priceList.get(i).getVolume();
        }
        return costToBuy;
    }

    public static ArrayList<PriceHolder> getPricelistCrex() {
        ArrayList<PriceHolder> priceListCrex = new ArrayList<>();

        PriceHolder currHolder = new PriceHolder();

        currHolder.setPrice(0.00000042);
        currHolder.setVolume(2700);
        priceListCrex.add(currHolder);

        currHolder = new PriceHolder();
        currHolder.setPrice(0.00000051);
        currHolder.setVolume(4076);
        priceListCrex.add(currHolder);

        currHolder = new PriceHolder();
        currHolder.setPrice(0.00000053);
        currHolder.setVolume(2000);
        priceListCrex.add(currHolder);

        currHolder = new PriceHolder();
        currHolder.setPrice(0.00000055);
        currHolder.setVolume(16681);
        priceListCrex.add(currHolder);


        currHolder = new PriceHolder();
        currHolder.setPrice(0.00000056);
        currHolder.setVolume(1251);
        priceListCrex.add(currHolder);

        currHolder = new PriceHolder();
        currHolder.setPrice(0.00000057);
        currHolder.setVolume(4233);
        priceListCrex.add(currHolder);

        currHolder = new PriceHolder();
        currHolder.setPrice(0.00000058);
        currHolder.setVolume(4221);
        priceListCrex.add(currHolder);

        return priceListCrex;
    }

}































