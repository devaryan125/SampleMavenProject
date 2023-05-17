package demoMaven.DemoMavenProject;

import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

public class TestCase1 {

	public WebDriver driver;
	
  @BeforeClass
  public void testSetup() {
  	WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.manage().window().maximize();
  }

  @Test
  public void mainTest() throws InterruptedException {
      // Navigate to the Just Eat careers website
      driver.get("https://careers.justeattakeaway.com/global/en/home");
      
      WebElement cookiesBtn = driver.findElement(By.xpath("//div[contains(@class,'button')]//button"));
      if (cookiesBtn.isDisplayed()) {
      	cookiesBtn.click();
      }
      
      // Find the search field and enter the job title "Test"             
      WebElement searchField = driver.findElement(By.id("keywordSearch"));
      WebDriverWait searchFieldWait = new WebDriverWait(driver, Duration.ofSeconds(10));
      searchFieldWait.until(ExpectedConditions.visibilityOf(searchField));
      
      searchField.sendKeys("Test");

      // Click the search button
      WebElement searchButton = driver.findElement(By.cssSelector("button[type='submit']"));
      searchButton.click();
  
      // Wait for the search results to load 
      WebDriverWait resultsWait = new WebDriverWait(driver, Duration.ofSeconds(10));
      resultsWait.until(ExpectedConditions.urlContains("/search"));
      
      // page scroll done to bring the search results into view
      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//input[@id='sub_search_textbox']")));
      Thread.sleep(1000);
      
      
      // Verify that the search results' locations vary
      List<WebElement> locations = driver.findElements(By.xpath("//span[@class='job-location']"));
      Iterator<WebElement> itr = locations.iterator();
	  Set<String> s = new HashSet<>();
      while(itr.hasNext()) {
      	String[] locationText = itr.next().getText().split("Location");
      	s.add(locationText[1]);
      }
      
      // Assertion to verify the locations 
      Assert.assertTrue(s.size()>1);
      System.out.println("search locations vary");

      // scroll to top of web page 
      ((JavascriptExecutor) driver).executeScript("window.scrollTo(document.body.scrollHeight, 0)");
      Thread.sleep(1000);
     
      WebElement countryButton = driver.findElement(By.xpath("//button[@aria-label='Country']"));
      countryButton.click();
      
      
      // Filter the search to Netherlands
      WebElement netherlandsCheckbox = driver.findElement(By.xpath("//input[@type='checkbox' and contains(@aria-label,'Netherlands')]//parent::label"));
      netherlandsCheckbox.click();

      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//input[@id='sub_search_textbox']")));
      Thread.sleep(1000);
      resultsWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='facet-tag' and text()='Netherlands']")));
      
      List<WebElement> filteredLocations = driver.findElements(By.xpath("//span[@class='job-location']"));
      Iterator<WebElement> i = filteredLocations.iterator();
      
      while(i.hasNext()) {
      	String[] loc = i.next().getText().split("Location");
      	Assert.assertTrue(loc[1].contains("Netherlands"));
      }
      
      System.out.println("Search results displayed for Netherlands only");
  }

  @AfterMethod
  public void teardownMethod() {
	  driver.quit();
  }

}
