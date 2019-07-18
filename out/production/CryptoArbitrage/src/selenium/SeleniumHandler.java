package selenium;

import exchangewallet.BitmartExchangeWalletChecker;
import exchangewallet.CrexExchangeWalletChecker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumHandler {

    private String profilePath = "C://Users//ahmad//AppData//Local//Google//Chrome//User Data//Default2";
    private String filePathHome = "D://GeneralFiles//librarys//Selenium//chromedriver2.exe";
    private String filePathToUse = filePathHome;
    private BitmartExchangeWalletChecker bitmartExchangeWalletChecker;

    private static WebDriver driver;

    public SeleniumHandler() {
        initialize();
    }

    private void initialize() {
        System.setProperty("webdriver.chrome.driver", filePathToUse);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + profilePath);
        this.driver = new ChromeDriver(options);
    }

    public void initializeWalletStatusForAllExchanges() {
        //Handle for all exchanges
    }

    public void closeBrowser() {
        this.driver.close();
    }

    public BitmartExchangeWalletChecker initializeBitmartWalletChecker() {
        //runEscapeCharThread();
        driver.navigate().to("https://www.bitmart.com/balance/en");

        bitmartExchangeWalletChecker = new BitmartExchangeWalletChecker();
        bitmartExchangeWalletChecker.parseSource(driver);
        return bitmartExchangeWalletChecker;
    }

    public CrexExchangeWalletChecker initializeCrexWalletChecker() {
        driver.navigate().to("https://crex24.com/sv/");
        CrexExchangeWalletChecker crexExchangeWalletChecker = new CrexExchangeWalletChecker();

        return crexExchangeWalletChecker;
    }

    public static void main(String args[]) {
        SeleniumHandler handler = new SeleniumHandler();
        handler.initializeCrexWalletChecker();
    }

    private void runEscapeCharThread() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    driver.findElement(By.tagName("body")).sendKeys("Keys.ESCAPE");
                    System.out.print("SENT\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


}











































