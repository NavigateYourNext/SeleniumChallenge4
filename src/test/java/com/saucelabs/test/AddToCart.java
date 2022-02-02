package com.saucelabs.test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AddToCart {

	public static void main(String[] args) {

		WebDriver driver;
		double foundmaximumPrice=0.0;

		try
		{
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			driver.get("https://www.saucedemo.com/");
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			driver.manage().deleteAllCookies();

			driver.findElement(By.cssSelector("input[id='user-name']")).sendKeys("standard_user");
			driver.findElement(By.cssSelector("input[id='password']")).sendKeys("secret_sauce");
			driver.findElement(By.cssSelector("input[id='login-button']")).click();

			List<WebElement> inventoryPriceList = driver.findElements(By.xpath("//div[@class='inventory_item_price']"));
			foundmaximumPrice = getMaximumPrice(inventoryPriceList);
			
			for(int i=0; i<inventoryPriceList.size(); i++) {
				double itemCost = Double.parseDouble(inventoryPriceList.get(i).getText().substring(1));
				if(itemCost == foundmaximumPrice) {
					driver.findElement(By.xpath("(//div[@class='pricebar'])["+(i+1)+"]/button")).click();
					break;
				}
			}
			
			driver.findElement(By.xpath("//a[@class='shopping_cart_link']")).click();
			double addedItemValue = Double.parseDouble(driver.findElement(By.xpath("//div[@class='inventory_item_price']")).getText().substring(1));
			if(addedItemValue == foundmaximumPrice)
				System.out.println("Correct item added in cart, item value is: $"+addedItemValue);
			else
				System.out.println("Incorrect item added in cart");
			
			Thread.sleep(2000);
			driver.quit();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static double getMaximumPrice(List<WebElement> inventoryPriceList) {
		double maxItemValue=0.0;
		
		for(WebElement item:inventoryPriceList) {
			double itemCost = Double.parseDouble(item.getText().substring(1));
			if(itemCost >= maxItemValue)
				maxItemValue = itemCost;
		}
		
		return maxItemValue;
	}

}
