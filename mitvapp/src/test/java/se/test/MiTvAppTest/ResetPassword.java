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

public class ResetPassword extends Utilities{

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
	public static void test_2ResetPassword() throws InterruptedException {
		Reporter.log("testResetPassword test case",true);
		implicitWait(driver,60, TimeUnit.SECONDS);
		profilePage(driver);
		List<WebElement> signedIn = driver.findElements(By.id("com.mitv:id/myprofile_person_container_signed_in"));
		if(signedIn.size()>0) {
			testLogout(driver);
		}

		//check login link is displayed and click on forgot password, enter email id and click on reset password
		List<WebElement> login = driver.findElements(By.id("com.mitv:id/myprofile_login_container_text"));
		Assert.assertTrue(login.size()>0);
		login.get(0).click();
		driver.findElement(By.id("com.mitv:id/mitvlogin_forgot_password_text")).click();
		List<WebElement> resetPassword = driver.findElements(By.id("com.mitv:id/resetpassword_email_edittext"));
		Assert.assertTrue(resetPassword.size()>0);
		resetPassword.get(0).sendKeys("supriya.v@indpro.se");
		driver.findElement(By.id("com.mitv:id/mitv_reset_password_button")).click();
		Thread.sleep(5000);

		//check reset password link 
		//		List<WebElement> resetPasswordLinkSent = driver.findElements(By.id("com.mitv:id/mitv_reset_password_button_tv"));
		//		Assert.assertTrue(resetPasswordLinkSent.size()>0);
		driver.findElement(By.name("Cambia tu contraseña, Navigate up")).click();
		driver.findElement(By.name("Inicia sesión, Navigate up")).click();
	}
}
