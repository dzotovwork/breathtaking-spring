import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class FirstTest {
    private WebDriver driver;
    private String baseUrl;
    @Before
    public void setUp(){
        driver = new ChromeDriver();
        baseUrl = "https://www.tinkoff.ru/career/vacancies/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    @Test
    public void testFirst(){
        driver.get(baseUrl);
        driver.findElement(By.xpath("//input[@name='name']")).click();
        driver.findElement(By.xpath("//input[@name='name']")).sendKeys("AlohaVida");
        driver.findElement(By.xpath("//input[@name='name']")).sendKeys(Keys.TAB);
        assertEquals("Допустимо использовать только буквы русского алфавита и дефис",driver.findElement(By.xpath("(//div[contains(@class,'ui-form-field-error-message')])[1]")).getText());
    }
    @After
    public void tearDown(){
        driver.quit();
    }

}
