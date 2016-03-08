package com.sqa.jf.auto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class JobSearchTest
{
	public String BASE_URL = "http://careers.renttesters.com/advancedSearch";

	private WebDriver driver;

	private WebDriverWait wait;

	@AfterClass
	public void afterClass()
	{
		this.driver.quit();
	};

	@DataProvider
	public Object[][] age()
	{
		return new Object[][] { new Object[] { "anytime" }, { "for last 15 days" }, { "for last 7 days" },
				{ "for last 3 days" }, { "from yesterday" } };
	}

	@Test(dataProvider = "age")
	public void agePostsVerification(String ageOptions) throws ParseException
	{
		this.driver.get(this.BASE_URL);
		this.driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		WebElement ages = this.driver.findElement(By.id("fromage"));

		// to work with age drop-dwon list
		Select selector = new Select(ages);
		selector.selectByVisibleText(ageOptions);

		// display 50 results per page
		this.driver.findElement(By.cssSelector("#items_per_page>option:nth-child(4)")).click();

		// submit search
		this.driver.findElement(By.cssSelector("input[type=submit]")).click();

		this.wait.until(ExpectedConditions.titleIs("Careers at SQA Solution"));

		// iterate through page and collect time stamps of job postings
		List<WebElement> dates = this.driver.findElements(By.className("date"));
		for (WebElement date : dates)
		{
			Assert.assertTrue(verifyDate(date.getText(), ageOptions));
		}
	}

	@BeforeClass
	public void beforeClass()
	{
		this.driver = new FirefoxDriver();
		this.wait = new WebDriverWait(this.driver, 10);
	}

	// method to verify the date
	public boolean verifyDate(String jobDate, String ageOptions) throws ParseException
	{
		// Assign date format matching with job search format
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();

		// get current date
		df.format(date);

		// get search date as a Date object
		Date searchDate = df.parse(jobDate);

		// get the difference between two dates in miliseconds
		long diff = date.getTime() - searchDate.getTime();

		// convert it to days
		int diffDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

		// check if the date values (ie. year, month and day) are within
		// a valid range
		df.setLenient(false);

		// verify if the job Date is in format yyyy-mm-dd
		if (jobDate.matches("\\d{4}-\\d{2}-\\d{2}"))
		{
			// return true if the date within valid range
			df.parse(jobDate);

			if (ageOptions.equals("anytime"))
			{
				return true;
			}
			else if (ageOptions.equals("for last 15 days") && diffDays <= 15)
			{
				return true;
			}
			else if (ageOptions.equals("for last 7 days") && diffDays <= 7)
			{
				return true;
			}
			else if (ageOptions.equals("for last 3 days") && diffDays <= 3)
			{
				return true;
			}
			else if (ageOptions.equals("from yesterday") && diffDays <= 1)
			{
				return true;
			}
		}
		return false;
	}
}
