package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.List;

public class YandexPage extends Page{
    public YandexPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@aria-label,'Запрос')]")
    public WebElement searchField;

    @FindBy(xpath = "//li[contains(@class,'serp-item')]/div/h2//div[contains(@class,'organic__url')]")
    public List<WebElement> searchedList;

    public void open(){
        driver.navigate().to("https://www.yandex.ru/");
        isLoadedByTitleContains("Яндекс");
    }
    public void openSearchResultsPageByRequest(String request, String requestItem) {
        searchField.sendKeys(request);
        searchField.sendKeys(Keys.ENTER);
        //ожидание, игнорирующее StaleElementReferenceException
        wait
                .ignoring(StaleElementReferenceException.class)
                .withMessage("Что-то пошло не так...")
                .pollingEvery(Duration.ofMillis(500))
                .until(d -> {
                    //список поисковой выдачи
                    List<WebElement> elements = searchedList;  //driver.findElements(listItems);
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


    public boolean isLoadedByTitleContains(String substring) {
        wait.until(d -> d.getTitle().contains(substring));
        return true;
    }
}
