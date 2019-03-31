import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestExample extends BaseRunner {
    Logger logger = LoggerFactory.getLogger(TestExample.class);
    @Test
    public void test() {
        app.getPage("https://yandex.ru/");
        app.openSearchResultsPageByRequest("Тинькофф работа в москве","Вакансии | Тинькофф Банк");
        app.switchToWindow("Вакансии");
        app.typeNameField("Иванов Иван Иванович");
        app.checkPopularNameRequest("Иванова");
        app.closeCurrentTab();
        app.switchToMainTab();
    }
}
