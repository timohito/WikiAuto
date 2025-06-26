package org.wikitests.searchtests;

import org.openqa.selenium.chrome.ChromeOptions;
import org.wikiauto.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.wikiauto.DriverFactory;

import java.util.List;

public class SearchTest {
    private BasePage basePage;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        WebDriver localDriver = new ChromeDriver(options);
        DriverFactory.setDriver(localDriver);
        basePage = new BasePage(localDriver);
    }


    @Test
    public void testBoldSuggestions() {
        String query = "Ива";
        basePage.open();
        basePage.enterQueryInSearchBox(query); // вводим запрос

        List<WebElement> Suggestions = basePage.getSuggestions(); // получаем саджесты

        for (WebElement element : Suggestions) { //для каждого саджеста проверяем содержит ли он подстроку из нашего запроса, выделена ли она жирным
            Assert.assertTrue(element.getText().contains(query));
            String highlightedText = basePage.getHighlightedText(element);
            Assert.assertTrue(highlightedText.toLowerCase().contains(query.toLowerCase()));
        }
    }

    @Test
    public void testNoBoldSuggestionsIfInvalidQuery() {
        String query = "Иваза";
        basePage.open();
        basePage.enterQueryInSearchBox(query);

        List<WebElement> Suggestions = basePage.getSuggestions();

        for (WebElement element : Suggestions) { //для каждого саджеста проверяем что он не содержит строку и никакая часть саджеста не выделена жирным
            Assert.assertFalse(element.getText().contains(query));
            String highlightedText = basePage.getHighlightedText(element);
            Assert.assertTrue(highlightedText.isEmpty());
        }
    }

    @Test
    public void testNoSuggestionsIfInvalidQuery() {
        String query = "Ивазаопщазл"; //ошибочный запрос
        basePage.open();
        basePage.enterQueryInSearchBox(query);

        List<WebElement> Suggestions = basePage.getSuggestions();

        Assert.assertTrue(Suggestions.isEmpty()); //проверяем что саджестов нет
    }

    @Test
    public void testClickSuggestionNavigatesToPage() {
        String query = "Татария";
        basePage.open();
        basePage.enterQueryInSearchBox(query);
        basePage.getSuggestions().get(0).click(); //кликаем по первому саджесту

        Assert.assertTrue(basePage.isCurrentPageMatchingQuery(query)); //убеждаемся что попали на верную страницу
    }

    @Test
    public void testSearchButtonFirstSuggestion() {
        String query = "Иван";
        basePage.open();
        basePage.enterQueryInSearchBox(query);
        basePage.clickSearchButton(); //кликаем по кнопке поиска

        Assert.assertTrue(basePage.isCurrentPageMatchingQuery(query)); //убеждаемся что попали на верную страницу
    }

    @Test
    public void testSearchButtonNoSuggestions() {
        String query = "Иваннннн"; //некорректный запрос
        basePage.open();
        basePage.enterQueryInSearchBox(query);
        basePage.clickSearchButton(); //клик на кнопку поиска

        Assert.assertTrue(basePage.isCurrentPageASearchResultPage()); //убеждаемся что попали на страницу поиска
    }

    @Test
    public void testSearchContainingPages() throws InterruptedException {
        String query = "абв";
        basePage.open();
        basePage.enterQueryInSearchBoxSlowly(query);
        Assert.assertTrue(basePage.searchPagesContainingTipExistsAndFunctions());// есть подсказка и функционирует
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}

