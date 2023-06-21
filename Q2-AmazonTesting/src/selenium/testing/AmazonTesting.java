package selenium.testing;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import org.openqa.selenium.support.ui.Select;



public class AmazonTesting {

	public static WebDriver driver = null;
	
	public static void main(String[] args) throws InterruptedException {
		
		boolean try_again = true;
		String filePath = ".\\output\\productsList.txt";
		System.setProperty("webdriver.chrome.driver", ".\\driver\\chromedriver.exe");
		
	
		while(try_again) {
			
			try {
				//Driver Creation
				driver = new ChromeDriver();
				
				//Opening amazon.in
				driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
				driver.navigate().to("https://amazon.in");
				driver.manage().window().maximize();
				
				//checking the title once 
				String title = driver.getTitle();
				System.out.println("\nTitle1 : " + title);
				WebElement allCategoriesDropdown = driver.findElement(By.id("nav-hamburger-menu"));
		        allCategoriesDropdown.click();
		        
		        List<WebElement> categorytitleLinks = driver.findElements(By.cssSelector(".hmenu-item.hmenu-title "));
		        List<String> categorytitles = new ArrayList<>();
		        for (WebElement categorytitleLink : categorytitleLinks) {
		        	String catg = categorytitleLink.getText();

		        	if(catg.matches(".*[a-zA-Z0-9].*")) {
		        		categorytitles.add(catg);
		        	}
		        }
		        
		        List<WebElement> categoryLinks = driver.findElements(By.cssSelector(".hmenu-item"));
		        List<String> category = new ArrayList<>();
		        for (WebElement categoryLink : categoryLinks) {
		        	String catg = categoryLink.getText();
//	        		System.out.println(catg);

		        	if(catg.equals("Help & Settings")) break;
		        	if(catg.matches(".*[a-zA-Z0-9].*") && !categorytitles.contains(catg)) {
		        		category.add(catg);
		        	}
		        }
				Random random = new Random();

		        int randomTimes = random.nextInt(3);
				while(randomTimes-- > 0) {

					//creating a random product choosing from list of dynamically generated catelogs
					String randomItem = randomItem(category);
					
					//getting the webelement instance for the search box
					WebElement searchbox = driver.findElement(By.cssSelector("#twotabsearchtextbox"));
					//searching for the randomItem
					searchbox.sendKeys(randomItem);
					searchbox.submit();
					
					//webscraping all the product names that are coming on the site this helps in randomizing
					List<WebElement> headingElements = driver.findElements(By.cssSelector(".a-color-base.a-text-normal"));
					
					//storing all the product names in an arraylist
					List<String> products = new ArrayList<>();
					for(WebElement element : headingElements) {
		        		String product_names = element.getText();
		        		products.add(product_names);
					}
					
					Thread.sleep(4000);
					
					//selecting a random product from the Products arraylist
					searchbox = driver.findElement(By.cssSelector("#twotabsearchtextbox"));
					searchbox.clear();
					searchbox.sendKeys(products.get(random.nextInt(products.size())));
					searchbox.submit();
					
					//selecting the first item in the list that appears on search
					WebElement searchElement1 = driver.findElement(By.cssSelector("div[data-component-type='s-search-result'] h2 a"));
					searchElement1.click();
					
					//since in amazon the item on clicking opens in a new window 
					//so switching between the windows
					String firstWindowHandle = driver.getWindowHandle();
					for (String windowHandle : driver.getWindowHandles()) {
					    if (!windowHandle.equals(firstWindowHandle)) {
					        driver.switchTo().window(windowHandle);
					        break;
					    }
					}	
					//rechecking the title of the window
					String title2 = driver.getCurrentUrl();
					System.out.println("\nTitle2 : " + title2);
					
					Thread.sleep(4000);
				
					//getting the minimum and maximum quantity allowed on the item and randomly selecting one quantity
					WebElement quantityDropdown = driver.findElement(By.xpath("//select[@name='quantity']"));
					String randomQty = randomquantity(quantityDropdown.getText());
					Select quantitySelect = new Select(quantityDropdown);
					quantitySelect.selectByValue(randomQty);
					
					//finding add to cart button and clicking it
					WebElement addTo_cart = driver.findElement(By.id("add-to-cart-button"));
					addTo_cart.click();
					Thread.sleep(4000);
					
					//switch to first window of chrome and refreshing the page
					driver.close();
					
					driver.switchTo().window(firstWindowHandle);
					driver.navigate().refresh();
				}
				
				//finding the cart button and clicking it to display the cart
				WebElement cart = driver.findElement(By.cssSelector("#nav-cart-count"));
				cart.click();
		
				Thread.sleep(3000);
				driver.quit();
				try_again = false;
			}
			catch(Exception e) {
			    // in case of no search results
			    // or any other error
	        	System.err.println("ERROR \nRestarting...");
				driver.quit();
				Thread.sleep(1000);
				try_again = true;
				e.printStackTrace();
			}
		}
	}


	//method to first search a random product query
	static String randomItem(List<String> categories) {
		Random random = new Random();
		int randomIndex = 0;
        try {
        	randomIndex = random.nextInt(categories.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
        String randomElement = categories.get(randomIndex);
        System.out.println("\nSearch category selected " + randomElement );
		return randomElement;	
	}

	//choosing a random quantity for the product out of the allowed quantities
	static String randomquantity(String quantity) {
		ArrayList<String> qty = new ArrayList<>();
		for(int i=0;i<quantity.length();i++) {
			if(Character.isDigit(quantity.charAt(i))) {
				if(quantity.charAt(i) == '0') {}
				else {
					qty.add(String.valueOf(quantity.charAt(i)));
				}
			}
		}
		Random random = new Random();
        int randomIndex = random.nextInt(qty.size());
        System.out.println("\nThe quantity selected is : " + qty.get(randomIndex));
        return qty.get(randomIndex);	
	}
}