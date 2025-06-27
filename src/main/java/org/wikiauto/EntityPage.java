package org.wikiauto;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EntityPage {
    protected WebDriver driver;

    private By redirectedFromSearch = By.cssSelector("#mw-content-subtitle > span");
    private By heading = By.cssSelector("#firstHeading > span");

    public EntityPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getPageTitle() {
        return driver.findElement(heading).getText();
    }

    public boolean isCurrentPageMatchingQuery(String query) {
        query = query.toLowerCase().trim();
        String pageTitle = getPageTitle().toLowerCase();

        // Проверяем точное совпадение с заголовком страницы
        if (pageTitle.equals(query)) {
            return true;
        }

        // Пробуем найти текст о перенаправлении (если он есть)
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(redirectedFromSearch));
            WebElement redirectNotice = driver.findElement(redirectedFromSearch);
            String redirectText = redirectNotice.getText().toLowerCase();
            if (redirectText.contains("перенаправлено с") && redirectText.contains(query)) {
                return true;
            }
        }
        catch (org.openqa.selenium.NoSuchElementException e) {}

        return false;
    }
}
