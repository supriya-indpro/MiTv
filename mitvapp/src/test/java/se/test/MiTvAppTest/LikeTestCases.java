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

public class LikeTestCases extends Utilities{
	public static AppiumDriver driver;
	
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
	public static void test_2verifyLikeTodos() throws InterruptedException {
		Reporter.log("Verify Todos like option", true);
		implicitWait(driver,60, TimeUnit.SECONDS);
		profilePage(driver);
		List<WebElement> login = driver.findElements(By.id("com.mitv:id/myprofile_login_container_text"));
		if(login.size()>0) {
			testLogin(driver);
		}
		tvGuideTab(driver);
		driver.findElement(By.name("Todos")).click();
		WebElement firstChannel = driver.findElement(By.id("com.mitv:id/tvguide_program_line_live"));
		firstChannel.click();
		Thread.sleep(3000);
		WebElement firstProgram = driver.findElement(By.id("com.mitv:id/channelpage_broadcast_details_title_tv"));
		firstProgram.click();
		WebElement broadcast = driver.findElement(By.id("com.mitv:id/broadcast_scroll"));
		scrollDown(broadcast, "Repeticiones de este programa", driver);
		Thread.sleep(3000);
		WebElement like = driver.findElement(By.id("com.mitv:id/element_like_image_View"));
		like.click();	
		Thread.sleep(3000);
		verifyLikeExist(driver);
		verifyLikeIsEmpty(driver);
	} 

	/*@Test
	public static void test_3verifyLikePelis() throws InterruptedException {
		Reporter.log("Verify pelis like option", true);
		implicitWait(driver,60, TimeUnit.SECONDS);
		profilePage(driver);
		List<WebElement> login = driver.findElements(By.id("com.mitv:id/myprofile_login_container_text"));
		if(login.size()>0) {
			testLogin(driver);
		}
		tvGuideTab(driver);
		driver.findElement(By.name("Pelis")).click();
		WebElement firstListedMovie = driver.findElement(By.id("com.mitv:id/element_poster_broadcast_container"));
		firstListedMovie.click();
		Thread.sleep(5000);
		List<WebElement> like = driver.findElements(By.id("com.mitv:id/element_like_image_View"));
		if (like.size()>0) {
			like.get(0).click();
		} else {
		Thread.sleep(5000);
		WebElement broadcast = driver.findElement(By.id("com.mitv:id/broadcast_scroll"));
		scrollDown(broadcast, "Repeticiones de esta peli", driver);
		String broadcastText = driver.findElement(By.id("com.mitv:id/block_broadcastpage_broadcast_extra_tv")).getText();
		scrollDown(broadcast, broadcastText, driver);
		Thread.sleep(3000);
		WebElement clickOnLike = driver.findElement(By.id("com.mitv:id/element_like_image_View"));
		clickOnLike.click();
		}
		verifyLikeExist(driver);
		verifyLikeIsEmpty(driver);		
	}

	@Test
	public static void test_4verifyLikeSeries() throws InterruptedException {
		Reporter.log("Verify Series like option", true);
		implicitWait(driver,60, TimeUnit.SECONDS);
		profilePage(driver);
		List<WebElement> login = driver.findElements(By.id("com.mitv:id/myprofile_login_container_text"));
		if(login.size()>0) {
			testLogin(driver);
		}
		tvGuideTab(driver);
		driver.findElement(By.name("Series")).click();
		WebElement firstListedSeries = driver.findElement(By.id("com.mitv:id/element_poster_broadcast_container"));
		firstListedSeries.click();
		Thread.sleep(3000);
		List<WebElement> like = driver.findElements(By.id("com.mitv:id/element_like_image_View"));
		if (like.size()>0) {
			like.get(0).click();
		} else {
		Thread.sleep(5000);
		WebElement broadcast = driver.findElement(By.id("com.mitv:id/broadcast_scroll"));
		scrollDown(broadcast, "Comentarios", driver);
		String broadcastText = driver.findElement(By.id("com.mitv:id/block_broadcastpage_broadcast_extra_tv")).getText();
		scrollDown(broadcast, broadcastText, driver);
		Thread.sleep(3000);
		WebElement clickOnLike = driver.findElement(By.id("com.mitv:id/element_like_image_View"));
		clickOnLike.click();
		}
		Thread.sleep(3000);
		verifyLikeExist(driver);
		verifyLikeIsEmpty(driver);
	}

	@Test
	public static void test_5verifyLikeDeportes() throws InterruptedException {
		Reporter.log("Verify Deportes like option", true);
		implicitWait(driver,60, TimeUnit.SECONDS);
		profilePage(driver);
		List<WebElement> login = driver.findElements(By.id("com.mitv:id/myprofile_login_container_text"));
		if(login.size()>0) {
			testLogin(driver);
		}
		tvGuideTab(driver);
		driver.findElement(By.name("Deportes")).click();
		WebElement firstListedSport = driver.findElement(By.id("com.mitv:id/element_poster_broadcast_container"));
		firstListedSport.click();
		Thread.sleep(3000);
		List<WebElement> like = driver.findElements(By.id("com.mitv:id/element_like_image_View"));
		if (like.size()>0) {
			like.get(0).click();
		} else {
		Thread.sleep(5000);
		WebElement broadcast = driver.findElement(By.id("com.mitv:id/broadcast_scroll"));
		scrollDown(broadcast, "Comentarios", driver);
		String broadcastText = driver.findElement(By.id("com.mitv:id/block_broadcastpage_broadcast_extra_tv")).getText();
		scrollDown(broadcast, broadcastText, driver);
		Thread.sleep(3000);
		WebElement clickOnLike = driver.findElement(By.id("com.mitv:id/element_like_image_View"));
		clickOnLike.click();
		}
		Thread.sleep(3000);
		verifyLikeExist(driver);
		verifyLikeIsEmpty(driver);
	}

	@Test
	public static void test_6verifyLikeNiños() throws InterruptedException {
		Reporter.log("Verify Niños like option", true);
		implicitWait(driver,60, TimeUnit.SECONDS);
		profilePage(driver);
		List<WebElement> login = driver.findElements(By.id("com.mitv:id/myprofile_login_container_text"));
		if(login.size()>0) {
			testLogin(driver);
		}
		tvGuideTab(driver);
		WebElement Niños = driver.findElement(By.name("Niños"));
		Niños.click();
		List<WebElement> NiñosListView = driver.findElements(By.id("com.mitv:id/fragment_tvguide_type_tag_listview"));
		if (NiñosListView.get(0).getText().isEmpty()) {
			Reporter.log("Niños tab isEmpty");
			Assert.assertFalse(NiñosListView.isEmpty());
			testLogout(driver);
		} else {
		WebElement firstListedCartoon = driver.findElement(By.id("com.mitv:id/element_poster_broadcast_container"));
		firstListedCartoon.click();
		Thread.sleep(3000);
		List<WebElement> like = driver.findElements(By.id("com.mitv:id/element_like_image_View"));
		if (like.size()>0) {
			like.get(0).click();
		} else {
		Thread.sleep(5000);
		WebElement broadcast = driver.findElement(By.id("com.mitv:id/broadcast_scroll"));
		scrollDown(broadcast, "Comentarios", driver);
		String broadcastText = driver.findElement(By.id("com.mitv:id/block_broadcastpage_broadcast_extra_tv")).getText();
		scrollDown(broadcast, broadcastText, driver);
		Thread.sleep(3000);
		WebElement clickOnLike = driver.findElement(By.id("com.mitv:id/element_like_image_View"));
		clickOnLike.click();
		}
		Thread.sleep(3000);
		verifyLikeExist(driver);
		verifyLikeIsEmpty(driver);
		}
	}*/
}
