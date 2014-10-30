package se.test.MiTvAppTest;

import io.appium.java_client.AppiumDriver;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HomeActivitiesTestCase extends Utilities{

public static AppiumDriver driver=null;
	
	@BeforeClass
	public void SetUp () throws Exception {
		String appdirectory = System.getProperty("user.dir")+"/src/test/resources";
		File appDir = new File(appdirectory);
		File app=new File(appDir, "miTv-debug.apk");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("deviceName", "Google Nexus");
		capabilities.setCapability("appPackage", "com.mitv");
		capabilities.setCapability("app", app.getAbsolutePath());
		driver= new AppiumDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
		Reporter.log("App launched",true);
		Thread.sleep(3000);
	}
	@AfterClass
	public void TearDown()
	{
		driver.quit();
	}
	
	@Test
	public static void test_1SplashScreen() throws InterruptedException {
		Reporter.log("SplashScreen",true);
		implicitWait(driver,60, TimeUnit.SECONDS);
		WebElement firstScreen = driver.findElement(By.id("com.mitv:id/splash_screen_activity_info_text_tutorial"));
		Assert.assertTrue(firstScreen.isDisplayed());
		driver.findElement(By.id("com.mitv:id/button_splash_tutorial")).click();

		WebElement secondScreenHeader = driver.findElement(By.id("com.mitv:id/tutorial_header"));
		Assert.assertTrue(secondScreenHeader.isDisplayed());
		driver.findElement(By.id("com.mitv:id/button_tutorial_next")).click();

		//Swipe to move to the next tutorial screen
		((JavascriptExecutor)driver).executeScript("mobile: swipe", new HashMap() {{ put("touchCount", 1); put("startX", 538); put("startY", 1035); put("endX", 158); put("endY", 1024); put("duration", 0.5); }});
		((JavascriptExecutor)driver).executeScript("mobile: swipe", new HashMap() {{ put("touchCount", 1); put("startX", 538); put("startY", 1035); put("endX", 158); put("endY", 1024); put("duration", 0.5); }});
		((JavascriptExecutor)driver).executeScript("mobile: swipe", new HashMap() {{ put("touchCount", 1); put("startX", 538); put("startY", 1035); put("endX", 158); put("endY", 1024); put("duration", 0.5); }});
		driver.findElement(By.id("com.mitv:id/button_tutorial_next")).click();

		//click on start button, end of tutorial
		WebElement startButton = driver.findElement(By.id("com.mitv:id/start_primary_button_container"));
		Assert.assertTrue(startButton.isDisplayed());
		startButton.click();

		//verify the landing page after tutorial is finished
		WebElement dateIsVisible = driver.findElement(By.id("com.mitv:id/layout_actionbar_dropdown_list_date_header_name"));
		Assert.assertTrue(dateIsVisible.isDisplayed());
	}
	
	@Test
	public static void test_2homeActivity() throws InterruptedException {
		tvGuideTab(driver);
		implicitWait(driver,60, TimeUnit.SECONDS);

		//swipe to check whether all the tabs are displayed or not
		driver.findElement(By.name("Todos")).click();
		((JavascriptExecutor)driver).executeScript("mobile: swipe", new HashMap() {{ put("touchCount", 1); put("startX", 538); put("startY", 1035); put("endX", 158); put("endY", 1024); put("duration", 0.5); }});
		Thread.sleep(2000);
		((JavascriptExecutor)driver).executeScript("mobile: swipe", new HashMap() {{ put("touchCount", 1); put("startX", 538); put("startY", 1035); put("endX", 158); put("endY", 1024); put("duration", 0.5); }});
		Thread.sleep(2000);
		((JavascriptExecutor)driver).executeScript("mobile: swipe", new HashMap() {{ put("touchCount", 1); put("startX", 538); put("startY", 1035); put("endX", 158); put("endY", 1024); put("duration", 0.5); }});
		Thread.sleep(2000);
		((JavascriptExecutor)driver).executeScript("mobile: swipe", new HashMap() {{ put("touchCount", 1); put("startX", 538); put("startY", 1035); put("endX", 158); put("endY", 1024); put("duration", 0.5); }});
		Thread.sleep(2000);
		((JavascriptExecutor)driver).executeScript("mobile: swipe", new HashMap() {{ put("touchCount", 1); put("startX", 538); put("startY", 1035); put("endX", 158); put("endY", 1024); put("duration", 0.5); }});
		Thread.sleep(2000);

		//click to check whether all the tabs are displayed or not
		driver.findElement(By.name("Todos")).click();
		List<WebElement> channelsList = driver.findElements(By.id("com.mitv:id/item_container"));
		Assert.assertTrue(channelsList.size()>0);
		driver.findElement(By.name("Pelis")).click();
		List<WebElement> movieList = driver.findElements(By.id("com.mitv:id/home_pager"));
		Assert.assertTrue(movieList.size()>0);
		driver.findElement(By.name("Series")).click();
		List<WebElement> seriesList = driver.findElements(By.id("com.mitv:id/home_pager"));
		Assert.assertTrue(seriesList.size()>0);
		driver.findElement(By.name("Deportes")).click();
		List<WebElement> sportsList = driver.findElements(By.id("com.mitv:id/home_pager"));
		Assert.assertTrue(sportsList.size()>0);
		driver.findElement(By.name("Niños")).click();
		List<WebElement> cartoonList = driver.findElements(By.id("com.mitv:id/fragment_tvguide_type_tag_listview"));
		Assert.assertTrue(cartoonList.size()>0);
		driver.findElement(By.name("Todos")).click();
	}
}
