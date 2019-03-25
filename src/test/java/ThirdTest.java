import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static org.junit.Assert.assertEquals;

public class ThirdTest extends BaseRunner {
    @Test
    public void test1() {
        driver.get(baseUrl);
        driver.findElement(By.xpath("//input[@name='name']")).click();
        driver.findElement(By.xpath("//input[@name='name']")).sendKeys("AlohaVida");
        driver.findElement(By.xpath("//input[@name='name']")).sendKeys(Keys.TAB);
        assertEquals("Допустимо использовать только буквы русского алфавита и дефис",
                driver.findElement(By.xpath("(//div[contains(@class,'ui-form-field-error-message')])[1]")).getText());
        assertEquals("Поле обязательное",
                driver.findElement(By.xpath("(//div[contains(@class,'ui-form-field-error-message')])[2]")).getText());
    }

    @Test
    public void test2() {
        driver.get(baseUrl);
        driver.findElement(By.xpath("//input[@name='name']")).click();
        driver.findElement(By.xpath("//input[@name='name']")).sendKeys("AlohaVida");
        driver.findElement(By.xpath("//input[@name='name']")).sendKeys(Keys.TAB);
        assertEquals("Допустимо использовать только буквы русского алфавита и дефис",
                driver.findElement(By.xpath("(//div[contains(@class,'ui-form-field-error-message')])[1]")).getText());
        assertEquals("Поле обязательное",
                driver.findElement(By.xpath("(//div[contains(@class,'ui-form-field-error-message')])[2]")).getText());
    }

    @Test
    public void test3() {
        driver.get(baseUrl);
        driver.findElement(By.xpath("//input[@name='name']")).click();
        driver.findElement(By.xpath("//input[@name='name']")).sendKeys("AlohaVida");
        driver.findElement(By.xpath("//input[@name='name']")).sendKeys(Keys.TAB);
        assertEquals("Допустимо использовать только буквы русского алфавита и дефис",
                driver.findElement(By.xpath("(//div[contains(@class,'ui-form-field-error-message')])[1]")).getText());
        assertEquals("Поле обязательное",
                driver.findElement(By.xpath("(//div[contains(@class,'ui-form-field-error-message')])[2]")).getText());
    }
}
