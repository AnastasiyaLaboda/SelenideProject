package com.it_academy.test.web.onliner;

import com.it_academy.onliner.constants.OnlinerUrls;
import com.it_academy.onliner.pageobject.onliner.CatalogPage;
import com.it_academy.onliner.pageobject.onliner.HomePage;
import com.it_academy.test.web.BaseTest;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Selenide.open;
import static com.it_academy.onliner.constants.WaitTimeouts.EXPLICIT_WAIT;
import static java.time.Duration.ofSeconds;

public class ProductGroupTest extends BaseTest {

    private static final String MAIN_SECTION_TITLE = "Каталог";
    private static final String CATALOG_CATEGORY = "Компьютеры";
    private static final String CATALOG_CATEGORY_SECTION = "Комплектующие";
    private static final String VALID_PRODUCTS_DESCRIPTION = "\\s*\\S*([\\d,]+)\\s*товар[а-я]*\\nот\\s([\\d,]+)\\sр\\s*\\S*";
    private static int PRODUCT_GROUP_COUNT;

    @BeforeClass
    @Description("Navigate to Onliner HomePage and then get product groups count {PRODUCT_GROUP_COUNT} on CatalogPage")
    public void init() {
        HomePage homePage = new HomePage();
        open(OnlinerUrls.HOME.getUrl());
        PRODUCT_GROUP_COUNT = homePage
                .clickOnMainNavigationLink(MAIN_SECTION_TITLE)
                .clickOnCatalogCategorySectionLink(CATALOG_CATEGORY)
                .clickOnCatalogCategorySectionClassifierLink(CATALOG_CATEGORY_SECTION)
                .getProductGroups()
                .size();
    }

    @Test
    @Description("Verify each product group contains valid number of products and min price")
    public void verifyEachProductGroupContains_Valid_NumberOfProductsAndMinPrice() {
        new CatalogPage().getProductGroupsValues(CatalogPage.ProductGroupValues.NUMBER_OF_PRODUCTS_AND_MIN_PRICE)
                .shouldHave(size(PRODUCT_GROUP_COUNT))
                .should(allMatch("Verify that the price and count are specified correctly",
                        element -> element.getText().matches(VALID_PRODUCTS_DESCRIPTION)), ofSeconds(EXPLICIT_WAIT));
    }

    @Test
    @Description("Verify each product group contains title")
    public void verifyEachProductGroupContains_Title() {
        new CatalogPage().getProductGroupsValues(CatalogPage.ProductGroupValues.TITLE)
                .shouldHave(size(PRODUCT_GROUP_COUNT));
    }
}