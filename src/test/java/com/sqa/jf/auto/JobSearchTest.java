package com.sqa.jf.auto;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class JobSearchTest
{
	public String BASE_URL = "http://careers.renttesters.com/advancedSearch";

	private WebDriver driver;

	@AfterClass
	public void afterClass()
	{
		this.driver.quit();
	}

	// @DataProvider
	// public Object[][] AdvanceAllJobSearchAllFieldTestData()
	// {
	// return new Object[][] { new Object[] { 1, "a" }, new Object[] { 2, "b" },
	// };
	// }

	@Test
	// (dataProvider = "AdvanceAllJobSearchAllFieldData")
	public void agePostsVerification()
	{
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		WebElement ageOptions = this.driver.findElement(By.id("fromage"));

		// to work with age drop-dwon list
		Select selector = new Select(ageOptions);
		selector.selectByVisibleText("for last 15 days");

		// display 50 results per page
		this.driver.findElement(By.cssSelector("#items_per_page>option:nth-child(4)")).click();

		// submit search
		this.driver.findElement(By.cssSelector("input[type=submit]")).click();
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		List<WebElement> dates = this.driver.findElements(By.className("date"));
		for (WebElement date : dates)
		{
			System.out.println(date.getText());
		}
	}

	@BeforeClass
	public void beforeClass()
	{
		this.driver = new FirefoxDriver();
		this.driver.get(this.BASE_URL + "/");
	}
}
