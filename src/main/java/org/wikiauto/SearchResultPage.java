package org.wikiauto;

import org.openqa.selenium.WebDriver;

public class SearchResultPage {
    protected WebDriver driver;


    public SearchResultPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isCurrentPageASearchResultPage() {
        return driver.getCurrentUrl().contains("search=");
    }
}
