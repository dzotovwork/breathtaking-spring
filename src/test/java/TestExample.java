import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestExample extends BaseRunner {
    Logger logger = LoggerFactory.getLogger(TestExample.class);

    @Test
    public void test1() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("https://yandex.ru/");
        driver.findElement(By.xpath("//input[contains(@aria-label,'Запрос')]"))
                .sendKeys("Тинькофф работа в москве");
        driver.findElement(By.xpath("//input[contains(@aria-label,'Запрос')]"))
                .sendKeys(Keys.ENTER);
        By listItems = By.xpath("//li[contains(@class,'serp-item')]/div/h2//div[contains(@class,'organic__url')]");
        List<WebElement> items = driver.findElements(listItems);
        wait
                .ignoring(StaleElementReferenceException.class)
                .withMessage("что то пошло не так")
                .pollingEvery(Duration.ofMillis(500))
                .until(driver ->{
                    for (WebElement element : items){
                        if (element.getText().contains("Вакансии | Тинькофф Банк")){
                            element.click();
                            break;
                        }
                    }
                    Set<String> ids = driver.getWindowHandles();
                    ids.forEach(id -> {
                        if (!id.equals(driver.getWindowHandle())){
                            driver.switchTo().window(id);
                            logger.info("Переключились к вкладке " + driver.getTitle());
                        }
                    });
                    return driver.getTitle().equals("Вакансии");
                });
        wait.until(d -> d.getTitle().equals("Вакансии"));

        String surname = "Иванова";
        driver.findElement(By.name("name")).sendKeys(surname);
        wait.until(d -> checkPopularNameRequest(surname));

        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.xpath("//span[contains(text(),'Отправить')]")))
                .click()
                .perform();
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);",driver.findElement(By.xpath("//span[contains(text(),'Отправить')]")));
    }

    private boolean checkPopularNameRequest(String surname) {
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
}
