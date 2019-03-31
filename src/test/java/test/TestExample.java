package test;

import org.junit.Test;
import pages.TinkoffJobPage;
import pages.YandexPage;

public class TestExample extends BaseRunner {

    @Test
    public void test() {
        YandexPage yandex = app.yandex;
        yandex.open();
        yandex.openSearchResultsPageByRequest("Тинькофф работа в москве", "Вакансии | Тинькофф Банк");
        yandex.switchToWindow("Вакансии");
        TinkoffJobPage tinkoff = app.tinkoffjob;
        tinkoff.typeNameField("Иванов Иван Иванович");
        tinkoff.checkNameField("Иванов Иван Иванович");
        tinkoff.closeCurrentTab();
        tinkoff.switchToMainTab();
    }
}
