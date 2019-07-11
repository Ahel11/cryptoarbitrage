package scratch;

import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String args[]) {
        checkBestProfit();
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































