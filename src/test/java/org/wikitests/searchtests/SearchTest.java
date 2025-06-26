package org.wikitests.searchtests;

import org.wikiauto.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class SearchTest {
    private BasePage basePage;
    private WebDriver driver;

    @BeforeTest
    public void setUp() {
        driver = new ChromeDriver();
        basePage = new BasePage(driver);
    }

    @Test
    public void testBoldSuggestions() {
        String query = "Ива";
        basePage.open();
        basePage.enterQueryInSearchBox(query);

        List<WebElement> Suggestions = basePage.getSuggestions();

        for (WebElement element : Suggestions) {
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

        for (WebElement element : Suggestions) {
            Assert.assertFalse(element.getText().contains(query));
            String highlightedText = basePage.getHighlightedText(element);
            Assert.assertTrue(highlightedText.isEmpty());
        }
    }

    @Test
    public void testNoSuggestionsIfInvalidQuery() {
        String query = "Ивазаопщазл";
        basePage.open();
        basePage.enterQueryInSearchBox(query);

        List<WebElement> Suggestions = basePage.getSuggestions();

        Assert.assertTrue(Suggestions.isEmpty());
    }

    @Test
    public void testClickSuggestionNavigatesToPage() {
        String query = "Татария";
        basePage.open();
        basePage.enterQueryInSearchBox(query);
        basePage.getSuggestions().get(0).click();

        Assert.assertTrue(basePage.isCurrentPageMatchingQuery(query));
    }

    @Test
    public void testSearchButtonFirstSuggestion() {
        String query = "Иван";
        basePage.open();
        basePage.enterQueryInSearchBox(query);
        basePage.clickSearchButton();

        Assert.assertTrue(basePage.isCurrentPageMatchingQuery(query));
    }

    @Test
    public void testSearchButtonNoSuggestions() {
        String query = "Иваннннн";
        basePage.open();
        basePage.enterQueryInSearchBox(query);
        basePage.clickSearchButton();

        Assert.assertTrue(basePage.isCurrentPageASearchResultPage());
    }

    @Test
    public void testSearchContainingPages() throws InterruptedException {
        String query = "абв";
        basePage.open();
        basePage.enterQueryInSearchBoxSlowly(query);
        Assert.assertTrue(basePage.searchPagesContainingTipExistsAndFunctions());// есть подсказка и функционирует
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}

