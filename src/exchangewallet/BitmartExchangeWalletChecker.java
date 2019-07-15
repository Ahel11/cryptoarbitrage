package exchangewallet;

import exchange.BittrexExchange;
import model.WalletStatusHolder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class BitmartExchangeWalletChecker {

    private ArrayList<WalletStatusHolder> walletStatusList;

    public BitmartExchangeWalletChecker() {
        walletStatusList = new ArrayList<>();
    }

    public void parseSource(WebDriver driver) {

        WebElement tables = driver.findElement(By.tagName("table"));
        List<WebElement> tBodies = tables.findElements(By.tagName("tbody"));

        for (WebElement row : tBodies) {
            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (WebElement col : cols) {
                if(col.getText().contains("Deposit")) {
                    System.out.print("here");
                }
                System.out.print(col.getText() + "\n");
            }
            System.out.println();
        }

    }

}





























