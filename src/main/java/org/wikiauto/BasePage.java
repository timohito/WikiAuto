package org.wikiauto;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BasePage {
    protected WebDriver driver;

    private By searchBoxBy = By.name("search");
    private By searchButtonBy = By.name("go");
    private By suggestionsResult = By.cssSelector(".suggestions-result");
    private By searchPagesContainingTip = By.cssSelector("body > div.suggestions > a > div > div.special-label");


    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://ru.wikipedia.org/");
    }

    public void enterQueryInSearchBox(String query) {
        driver.findElement(searchBoxBy).clear();
        driver.findElement(searchBoxBy).sendKeys(query);
    }

    public void enterQueryInSearchBoxSlowly(String query) throws InterruptedException {
        driver.findElement(searchBoxBy).clear();
        char[] q = query.toCharArray();
        for (char c : q) {
            driver.findElement(searchBoxBy).sendKeys(String.valueOf(c));
            Thread.sleep(200);
        }
    }

    public void clickSearchButton() {
        driver.findElement(searchButtonBy).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
    }

    public List<WebElement> getSuggestions() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(suggestionsResult));
        } catch (org.openqa.selenium.TimeoutException e) {

        }
        return Collections.emptyList();
    }

    public String getHighlightedText(WebElement element) {
        WebElement highlight;
        try {
            highlight = element.findElement(By.cssSelector(".highlight"));
        }
        catch (org.openqa.selenium.NoSuchElementException e) {
            return "";
        }
        return highlight.getText();
    }

    public boolean searchPagesContainingTipExistsAndFunctions() {
        SearchResultPage searchResPage = new SearchResultPage(driver);
        try {
            driver.findElement(searchPagesContainingTip).click();
        }
        catch (org.openqa.selenium.NoSuchElementException e) {}

        return searchResPage.isCurrentPageASearchResultPage();
    }
}
