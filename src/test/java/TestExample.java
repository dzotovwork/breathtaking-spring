import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class TestExample extends BaseRunner {

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
                        }
                    });
                    return driver.getTitle().equals("Вакансии");
                });
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.xpath("//span[contains(text(),'Отправить')]")))
                .click()
                .perform();
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);",driver.findElement(By.xpath("//span[contains(text(),'Отправить')]")));
    }
}
