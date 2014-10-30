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

public class ReminderTestCases extends Utilities{

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
	public static void test_2SportsReminder() throws InterruptedException {
		Reporter.log("test sports test case",true);
		implicitWait(driver,60, TimeUnit.SECONDS);
		boolean isBroadCast = false;
		profilePage(driver);

		//click on reminder without login and verify if the reminder is empty or not
		WebElement reminder = driver.findElement(By.id("com.mitv:id/myprofile_reminders_container"));
		reminder.click();
		WebElement reminderText = driver.findElement(By.id("com.mitv:id/request_empty_details_tv"));
		Assert.assertEquals("No tienes recordatorios todavía", reminderText.getText());
		WebElement navigateBack = driver.findElement(By.name("Recordatorios, Navigate up"));
		navigateBack.click();

		//login, click on deportes and add reminder
		testLogin(driver);
		tvGuideTab(driver);
		WebElement deportes = driver.findElement(By.name("Deportes"));
		deportes.click();
		
		//select ligaPostobon sport
		List<WebElement> ligaPostobon = driver.findElements(By.id("com.mitv:id/banner_image_competition_frame_layout"));
		Assert.assertTrue(ligaPostobon.size()>0);
		ligaPostobon.get(0).click();
		Thread.sleep(2000);
		//select the first match and click on reminder, if reminder does not exist go back and select the next match or else assert reminder does not exist
		WebElement firstMatch = driver.findElement(By.xpath("//android.widget.ScrollView[1]/android.support.v4.view.ViewPager[1]/android.widget.RelativeLayout[1]"));
		firstMatch.click();
		List<WebElement> setReminder = driver.findElements(By.id("com.mitv:id/competition_event_row_reminder_view"));
		if(setReminder.size()>0) {
		setReminder.get(0).click();
		isBroadCast=true;
		Thread.sleep(3000);
		}

		// scroll and test how many share options are displayed 
		List<WebElement> shareIsDisplayed = driver.findElements(By.id("com.mitv:id/competition_element_social_buttons_share_button_iv"));
		Assert.assertTrue(shareIsDisplayed.size()>0);
		if (shareIsDisplayed.size()>0) {
			shareIsDisplayed.get(0).click();
		} else {
			WebElement scrollView = driver.findElement(By.id("com.mitv:id/event_page_scrollview"));
			scrollDown(scrollView, "Todos Contra Todos", driver);
			Thread.sleep(3000);
			shareIsDisplayed.get(0).click();
		}
		WebElement alertTitle = driver.findElement(By.id("android:id/alertTitle"));
		Assert.assertEquals("Compartir con…", alertTitle.getText());
		List<WebElement> shareOptions = driver.findElements(By.id("android:id/text1"));
		Assert.assertTrue(shareOptions.size()==3);
		driver.navigate().back();

		//check if reminder is added or not, remove reminder and logout
		if(isBroadCast==true) {
		profilePage(driver);
		reminder.click();
		List<WebElement> getReminder = driver.findElements(By.id("com.mitv:id/row_reminders_notification_iv"));
		Assert.assertTrue(getReminder.size()>0);
		getReminder.get(0).click();
		List<WebElement> noOption = driver.findElements(By.id("com.mitv:id/dialog_remove_notification_button_no"));
		Assert.assertTrue(noOption.size()>0);
		WebElement clickYes = driver.findElement(By.id("com.mitv:id/dialog_remove_notification_button_yes"));
		clickYes.click();
		navigateBack.click();
		WebElement reminderCount = driver.findElement(By.id("com.mitv:id/myprofile_reminders_count_tv"));
		Assert.assertEquals("(0)", reminderCount.getText());
		}
		testLogout(driver);
	}
	
}
