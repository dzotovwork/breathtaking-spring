package app;

import org.openqa.selenium.*;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.TinkoffJobPage;
import pages.YandexPage;
import test.BrowsersFactory;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Иван on 16.04.2018.
 */
public class Application {

    private WebDriverWait wait;
    private WebDriver driver;
    public YandexPage yandex;
    public TinkoffJobPage tinkoffjob;
    public final String browserName = "chrome";

    public Application() throws MalformedURLException{
        driver = new EventFiringWebDriver(getDriver());
        ((EventFiringWebDriver) driver).register(new BrowsersFactory.MyListener());
        wait = new WebDriverWait(driver, 10);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        //pages
        yandex = new YandexPage(driver);
        tinkoffjob = new TinkoffJobPage(driver);
    }

    public void quit() {
        driver.quit();
        driver = null;
    }

    private WebDriver getDriver() throws MalformedURLException {
        return BrowsersFactory.buildDriver(browserName);
    }

}
