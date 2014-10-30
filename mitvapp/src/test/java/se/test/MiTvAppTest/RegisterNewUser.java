 package se.test.MiTvAppTest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.JsonParser;

public class RegisterNewUser extends Utilities{

	public static AppiumDriver driver=null;
	JsonParser parser=new JsonParser();

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
	
	@Test(dataProvider="loginCredentials")
	public static void test_2RegisterNewUser(String firstName, String lastName, String email, String password) throws InterruptedException {
		Reporter.log("Register new user test case",true);
		implicitWait(driver,1, TimeUnit.MINUTES);
		profilePage(driver);

		//check whether the user is logged in or not if logged in then display a message or else try to register new user.
		List<WebElement> loggedIn = driver.findElements(By.id("com.mitv:id/myprofile_person_container_signed_in"));
		if (loggedIn.size()>0) {
			Reporter.log("User is loggedIn", true);
		} else {
			WebElement signUp = driver.findElement(By.id("com.mitv:id/myprofile_signup_text"));
			Assert.assertTrue(signUp.isDisplayed());
			signUp.click();
			WebElement register = driver.findElement(By.id("com.mitv:id/signin_signup_email_title_tv"));
			Assert.assertTrue(register.isDisplayed());
			register.click();
			WebElement pageTitle = driver.findElement(By.id("android:id/action_bar_title"));
			Assert.assertEquals(pageTitle.getText(), "Regístrate");
			driver.findElement(By.id("com.mitv:id/signup_firstname_edittext")).sendKeys(firstName);
			driver.findElement(By.id("com.mitv:id/signup_lastname_edittext")).sendKeys(lastName);
			driver.findElement(By.id("com.mitv:id/signup_email_edittext")).sendKeys(email);
			driver.findElement(By.id("com.mitv:id/signup_password_edittext")).sendKeys(password);
			driver.findElement(By.id("com.mitv:id/mitv_sign_up_button_tv")).click();
			Thread.sleep(3000);

			//if the user is already registered then display a message and get back to login page or else verify the profile name and logout.
			List<WebElement> existingEmail = driver.findElements(By.id("com.mitv:id/signup_error_email_textview"));
			if(existingEmail.size()>0)
			{
				Reporter.log("Email id already registered", true);
				driver.findElement(By.name("Regístrate, Navigate up")).click();
				driver.findElement(By.name("Regístrate, Navigate up")).click();
			} else {
				WebElement profileName = driver.findElement(By.id("com.mitv:id/myprofile_name_tv"));
				Assert.assertEquals(profileName.getText(), "supriya"+ " " +"vivek");
				Reporter.log("New user registered successfully", true);
				testLogout(driver);
			}
		}
	}

	@DataProvider
	public Object[][] loginCredentials() {
		return new Object[][] {{"supriya", "vivek", "supriya.v@indpro.se", "password"}};
	}

	@Test 
	public static void test_3VerifyDates() throws InterruptedException {
		Reporter.log("verifyDates test case",true);
		implicitWait(driver,60, TimeUnit.SECONDS);
		tvGuideTab(driver);
		driver.findElement(By.name("Todos")).click();
		//verify the no. of days displayed in the dropdown
		WebElement daysAndDates = driver.findElement(By.id("android:id/action_bar_spinner"));
		daysAndDates.click();
		List<WebElement> dates = driver.findElements(By.id("com.mitv:id/layout_actionbar_dropdown_list_date_item_number"));
		Assert.assertTrue(dates.size()==7);
		Thread.sleep(2000);
		driver.findElement(By.id("com.mitv:id/layout_actionbar_dropdown_list_date_item_number")).click();
	}

	@Test 
	public static void test_4MovieIcon() throws InterruptedException {
		Reporter.log("testMovieIcon test case",true);
		implicitWait(driver,60, TimeUnit.SECONDS);
		tvGuideTab(driver);
		//verify if the movie icon is displayed with the name in the first screen
		driver.findElement(By.name("Pelis")).click();
		List<WebElement> titleText = driver.findElements(By.id("com.mitv:id/element_poster_broadcast_title_tv"));
		for(int i=0; i<titleText.size(); i++) {
			String movieIcon = titleText.get(i).getText().substring(0, 2);
			Assert.assertEquals("΢ ", movieIcon);
		}
	}

	/*public String randomEmailId()
	{
		String alphabets="0123456789abcdefghijklmnopqrstuvwxyz";
		int n=alphabets.length();
		StringBuilder email=new StringBuilder();
		Random r=new Random();

		for(int i=0; i<10; i++) {
			email.append(alphabets.charAt(r.nextInt(n)));
		}

		email.append("Test@delete.com");
		return email.toString();
	}*/
}
