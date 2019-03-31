import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Иван on 16.04.2018.
 */
public class Application {

    Logger logger = LoggerFactory.getLogger(Application.class);
    private WebDriverWait wait;
    private WebDriver driver;
    public final String browserName = "chrome";


    public Application() {
        driver = new EventFiringWebDriver(getDriver());
        ((EventFiringWebDriver) driver).register(new BrowsersFactory.MyListener());
        wait = new WebDriverWait(driver, 10);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public void quit() {
        driver.quit();
        driver = null;
    }

    public void getPage(String url) {
        driver.navigate().to(url);
    }

    public boolean isLoadedByTitleContains(String substring) {
        wait.until(d -> d.getTitle().contains(substring));
        return true;
    }

    public void openSearchResultsPageByRequest(String request, String requestItem) {
        driver.findElement(By.xpath("//input[contains(@aria-label,'Запрос')]"))
                .sendKeys(request);
        driver.findElement(By.xpath("//input[contains(@aria-label,'Запрос')]"))
                .sendKeys(Keys.ENTER);

        //ожидание, игнорирующее StaleElementReferenceException
        wait
                .ignoring(StaleElementReferenceException.class)
                .withMessage("Что-то пошло не так...")
                .pollingEvery(Duration.ofMillis(500))
                .until(d -> {
                    //список поисковой выдачи
                    By listItems = By.xpath("//li[contains(@class,'serp-item')]/div/h2//div[contains(@class,'organic__url')]");
                    List<WebElement> elements = driver.findElements(listItems);
                    for (WebElement element : elements){
                        if (element.getText().contains(requestItem)){
                            element.click();
                            break;
                        }
                    }
                    //Ожидание появления заголовка
                    return isLoadedByTitleContains(request);
                });
    }

    public void switchToWindow(String windowName){
        wait.until(d -> {
            boolean check = false;
            for (String title : driver.getWindowHandles()) {
                driver.switchTo().window(title);
                System.out.println(d.getTitle());
                check = d.getTitle().equals(windowName);
            }
            return check;
        });
    }

    public void typeNameField(String value){
        //Заполняем форму максиально быстро, пытаясь игнорировать анимацию страницы
        wait.ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class)
                .until(d -> {
                    driver.findElement(By.name("name")).sendKeys(value);
                    return true;
                });
    }

    public boolean checkPopularNameRequest(String surname) {
        boolean match = false;
        String messageResponse;
        String response;
        List<String> responseList = new ArrayList<>();
        for (LogEntry entry : driver.manage().logs().get(LogType.PERFORMANCE)) {
            messageResponse = entry.getMessage();
            if (messageResponse.contains("get_popular_names") && messageResponse.contains("requestWillBeSent")) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(messageResponse);
                    response = jsonObject.getJSONObject("message").getJSONObject("params").getJSONObject("request").get("postData").toString();
                    response = URLDecoder.decode(response, "UTF-8");
                    logger.info(response);
                    responseList.add(response);
                } catch (JSONException e) {
                    logger.error("JSONException: Ошибка преобразования JSON объекта, для получения параметров операции", e);
                } catch (UnsupportedEncodingException e) {
                    logger.error("UnsupportedEncodingException: Ошибка преобразования строки в utf-8", e);
                } catch (NullPointerException e) {
                    logger.error("NullPointerException: Метод get_popular_names не выполнился", e);
                }
            }
        }
        for (String res : responseList) {
            if (res.contains(surname)) {
                match = true;
                break;
            }
        }
        return match;
    }

    public void switchToMainTab(){
        driver.switchTo().window(driver.getWindowHandles().iterator().next());
    }

    //универсальный xpath локатор, вернет все элементы, содержащие текст
    public List<WebElement> xpathSearcherByText(String searchText) {
        String xpath = String.format("//*[contains(text(),'%s')]", searchText);
        return driver.findElements(By.xpath(xpath));
    }

    public void closeCurrentTab(){
        driver.close();
        logger.info("Закрыта активная вкладка");
    }

    private WebDriver getDriver() {
        return BrowsersFactory.buildDriver(browserName);
    }

}
