package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.junit.Assert.assertEquals;


public class TinkoffJobPage extends Page {
    public TinkoffJobPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[contains(@class,'ui-input__column')]/input[@name='name']")
    WebElement name;

    public void typeNameField(String value) {
        //Заполняем форму максиально быстро, пытаясь игнорировать анимацию страницы
        wait.ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class)
                .until(d -> {
                    name.sendKeys(value);
                    return true;
                });
    }

    public void checkNameField(String value){
        name.sendKeys(Keys.ENTER);
        assertEquals(value,name.getAttribute("value"));
    }
}
