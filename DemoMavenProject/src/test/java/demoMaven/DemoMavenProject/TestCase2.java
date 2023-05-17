package demoMaven.DemoMavenProject;

import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.BeforeClass;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;

public class TestCase2 {
	
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
      
      // Accept/Deny cookies if banner appears on the web page
      WebElement cookiesBtn = driver.findElement(By.xpath("//div[contains(@class,'button')]//button"));
      if (cookiesBtn.isDisplayed()) {
      	cookiesBtn.click();
      }
      
      // Find the search field and enter the job title "Test"             
      WebElement searchField = driver.findElement(By.id("keywordSearch"));
      WebDriverWait searchFieldWait = new WebDriverWait(driver, Duration.ofSeconds(10));
      searchFieldWait.until(ExpectedConditions.visibilityOf(searchField));
      
      searchField.click();        
      ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)");
      Thread.sleep(1000);

      WebElement salesOption = driver.findElement(By.xpath("//span[@class='au-target phs-Sales']"));

      //  select sales
      salesOption.click();
      
      // Wait for the search results to load 
      WebDriverWait resultsWait = new WebDriverWait(driver, Duration.ofSeconds(10));
      resultsWait.until(ExpectedConditions.urlContains("/sales-jobs"));
      
      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//h2[contains(@title,'Refine your search results')]")));
      
      WebElement chckbox = driver.findElement(By.xpath("//input[@type='checkbox' and contains(@aria-label,'Sales')]"));
      Assert.assertTrue(chckbox.isSelected());
      System.out.println("sales is selected");
      
      String countJobCategory = chckbox.getAttribute("data-ph-at-count");
      String countJobResults = driver.findElement(By.xpath("//span[@class='result-count']")).getText();
      
      // Validate the result counts match for Sales Job Category
      Assert.assertEquals(countJobCategory,countJobResults);
      System.out.println("search results is matching for sales ");

      WebElement countryButton = driver.findElement(By.xpath("//button[@aria-label='Country']"));
      countryButton.click();
      
      
      // Filter the search to Netherlands
      WebElement GermanyCheckbox = driver.findElement(By.xpath("//input[@type='checkbox' and contains(@aria-label,'Germany')]//parent::label"));
      GermanyCheckbox.click();
      
      WebElement germanyCheckbox = driver.findElement(By.xpath("//input[@type='checkbox' and contains(@aria-label,'Germany')]"));
      Assert.assertTrue(germanyCheckbox.isSelected());
      System.out.println("Germany is selected");
      
      String countCountry = germanyCheckbox.getAttribute("data-ph-at-count");
      
      Thread.sleep(1000);
      String countJobResultsCountry = driver.findElement(By.xpath("//span[@class='result-count']")).getText();

      // Validate the result counts match for Germany
      Assert.assertEquals(countCountry,countJobResultsCountry);
      System.out.println("search results is matching for Germany Country ");
      
      List<WebElement> filteredCategory = driver.findElements(By.xpath("//span[contains(@class,'category')]"));
      Iterator<WebElement> i = filteredCategory.iterator();
      while(i.hasNext()) {
      	Assert.assertTrue(i.next().getText().contains("Sales"));
      }
      System.out.println("Search results displayed for Sales Category only");
  }
  
  @AfterClass
  public void afterClass() {
	// Close the browser
      driver.quit();
  }

}
