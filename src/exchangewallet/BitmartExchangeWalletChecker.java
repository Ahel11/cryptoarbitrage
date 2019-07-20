package exchangewallet;

import interfaceimpl.ExchangeWalletCheckerImpl;
import model.WalletStatusHolder;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Scanner;

public class BitmartExchangeWalletChecker implements ExchangeWalletCheckerImpl {

    private ArrayList<WalletStatusHolder> walletStatusList;

    public BitmartExchangeWalletChecker() {
        walletStatusList = new ArrayList<>();
    }

    public void parseSource(WebDriver driver) {

        WebElement tables = driver.findElement(By.tagName("table"));
        String hmtl = tables.getAttribute("innerHTML");
        generateWalletStatusForEachCurrency(hmtl);


    }

    private boolean generateWalletStatusForEachCurrency(String html) {
        Scanner scanner = new Scanner(html);
        ArrayList<String> htmlComponents = new ArrayList<>();
        while(scanner.hasNext()) {
            String next = scanner.next();
            if(isLegitimateToken(next)) {
                htmlComponents.add(next);
            }
        }

        for(int i=0; i<htmlComponents.size(); i++) {

            String currString = htmlComponents.get(i);
            if(currString.contains("coin-name-t1")) {
                boolean walletStatusBool = false;
                String coinName = "";
                coinName = Jsoup.parse(currString).text();
                coinName = coinName.split(">")[1];

                if(!isDepositOrWithdrawalDisabled(htmlComponents, i)) {
                    walletStatusBool = true;
                }
                WalletStatusHolder currHolder = new WalletStatusHolder();
                currHolder.setPair(coinName);
                currHolder.setWalletDepositEnabled(walletStatusBool);
                currHolder.setWalletWithdrawalEnabled(walletStatusBool);
                walletStatusList.add(currHolder);
            }
        }

        return false;
    }

    private boolean isLegitimateToken(String token) {
        if(token.contains("coin-name-t1") || token.contains("deposit-span")) {
            return true;
        }
        return false;
    }

    private boolean isDepositOrWithdrawalDisabled(ArrayList<String> list, int index) {
        String deposit = list.get(index+1);
        String withdraw = list.get(index+2);
        if(deposit.contains("deposit-span-no") || withdraw.contains("deposit-span-no")) {
            return true;
        }
        return false;
    }

    private String getCoinNameFromCurrWalletStatus(ArrayList<String> stringList, int index) {
        for(int i=index; i>0; i--) {
            String currString = stringList.get(i);
            if(currString.contains("coin-name-t1")) {
                currString = Jsoup.parse(currString).text().split(">")[1];
                return currString;
            }
        }
        return null;
    }


    @Override
    public void initialize() {

    }

    @Override
    public boolean isWalletStatusOffline(String pairType) throws Exception {
        for(WalletStatusHolder hold: walletStatusList) {
            if(hold.getPair().equalsIgnoreCase(pairType)) {
                if(!hold.isWalletDepositEnabled() || !hold.isWalletWithdrawalEnabled()) {
                    return true;
                }
            }
        }
        return false;
    }
}





























