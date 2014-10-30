package se.test.MiTvAppTest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

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

public class DragDropChannelTestCase extends Utilities{

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
	public static void test_2DragAndDrop() throws InterruptedException {
		Reporter.log("Drag drop channels test case",true);
		implicitWait(driver,60, TimeUnit.SECONDS);
		profilePage(driver);
		testLogin(driver);

		//search for AXN channel and add to the list of channels, check if the channel is already added or not
		driver.findElement(By.id("com.mitv:id/myprofile_channels_title_tv")).click();
		driver.findElement(By.id("com.mitv:id/tvchannels_user_edit_search_text")).sendKeys("AXN");
		List<WebElement> channelExists = driver.findElements(By.name("Remover"));
		if(channelExists.size()>0) {
			driver.findElement(By.xpath("//android.widget.HorizontalScrollView[1]/android.widget.TextView[2]")).click();
		} else  {
			List<WebElement> addChannel = driver.findElements(By.name("+  Agregar"));
			if (addChannel.size()>0) 
				driver.findElement(By.id("com.mitv:id/row_mychannels_channel_button_tv")).click();
			driver.findElement(By.xpath("//android.widget.HorizontalScrollView[1]/android.widget.TextView[2]")).click();
		}

		//Drag and drop AXN channel from last position to last 4th position
		WebElement list = driver.findElement(By.id("com.mitv:id/tvchannels_user_reorder_list"));
		scrollDown(list, "AXN", driver);
		WebElement drag = driver.findElement(By.xpath("//android.support.v4.view.ViewPager[1]/android.widget.ListView[1]/android.view.View[6]/android.widget.TextView[2]"));
		WebElement drop = driver.findElement(By.xpath("//android.support.v4.view.ViewPager[1]/android.widget.ListView[1]/android.view.View[3]/android.widget.TextView[2]"));
		TouchAction dragNDrop=new TouchAction(driver).longPress(drag).moveTo(drop).release();
		dragNDrop.perform();
		Thread.sleep(3000);

		//Remove AXN channel from the group and logout
		driver.findElement(By.xpath("//android.widget.HorizontalScrollView[1]/android.widget.TextView[1]")).click();
		driver.findElement(By.id("com.mitv:id/tvchannels_user_edit_search_text")).sendKeys("AXN");
		driver.findElement(By.id("com.mitv:id/row_mychannels_channel_button")).click();
		driver.findElement(By.name("Organiza tus canales, Navigate up")).click();
		testLogout(driver);
	}
}
