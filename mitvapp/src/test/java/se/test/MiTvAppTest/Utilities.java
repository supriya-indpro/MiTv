package se.test.MiTvAppTest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class Utilities {
	
	public static AppiumDriver driver;
		
	public static void testLogin(AppiumDriver driver) throws InterruptedException { 
		Reporter.log("login test case",true);
		implicitWait(driver,60, TimeUnit.SECONDS);
		profilePage(driver);
		List<WebElement> signedIn = driver.findElements(By.id("com.mitv:id/myprofile_person_container_signed_in"));
		if(signedIn.size()>0) {
			Reporter.log("User is already logged in", true);
		} else {

			//click on login and check if facebook button is displayed
			List<WebElement> login = driver.findElements(By.id("com.mitv:id/myprofile_login_container_text"));
			Assert.assertTrue(login.get(0).isDisplayed());
			login.get(0).click();
			WebElement facebookLink = driver.findElement(By.id("com.mitv:id/mitvlogin_facebook_container"));
			Assert.assertTrue(facebookLink.isDisplayed());

			//enter username, password and login
			driver.findElement(By.id("com.mitv:id/mitvlogin_login_email_edittext")).sendKeys("supriya.v@indpro.se");
			WebElement pass = driver.findElement(By.id("com.mitv:id/mitvlogin_login_password_edittext"));
			pass.sendKeys("password");
			Thread.sleep(3000);
			WebElement scrollableView = driver.findElement(By.xpath("//android.widget.ScrollView[1]"));
			String canIScroll = scrollableView.getAttribute("scrollable");
			if (canIScroll.equals("true")) {
				driver.hideKeyboard("Done");
			}
			WebElement loginButton = driver.findElement(By.id("com.mitv:id/mitvlogin_login_button"));
			loginButton.click();
			Thread.sleep(5000);
		}
	}

	public static void testLogout(AppiumDriver driver) {
		Reporter.log("logout test case",true);
		implicitWait(driver,60, TimeUnit.SECONDS);

		//verify login and click on logout
		profilePage(driver);
		WebElement signedIn = driver.findElement(By.id("com.mitv:id/myprofile_person_container_signed_in"));
		Assert.assertTrue(signedIn.isDisplayed());
		driver.findElement(By.id("com.mitv:id/element_tab_text_me")).click();
		driver.findElement(By.id("com.mitv:id/myprofile_logout_container_text")).click();
		List<WebElement> loginLink = driver.findElements(By.id("com.mitv:id/myprofile_login_container_text"));
		Assert.assertTrue(loginLink.size()>0);
	}
	
	public static void tvGuideTab(AppiumDriver driver) {
		WebElement tvGuideTab = driver.findElement(By.id("com.mitv:id/tab_tv_guide"));
		tvGuideTab.click();
	}

	public static void profilePage(AppiumDriver driver) {
		WebElement myProfile = driver.findElement(By.id("com.mitv:id/tab_me"));
		Assert.assertTrue(myProfile.isDisplayed());
		myProfile.click();
	}

	public static void verifyShareOptions(AppiumDriver driver) {
		implicitWait(driver,60, TimeUnit.SECONDS);

		//check if all the 3 options are displayed
		List<WebElement> like = driver.findElements(By.id("com.mitv:id/element_social_buttons_like_view"));
		Assert.assertTrue(like.size()>0);
		List<WebElement> share = driver.findElements(By.id("com.mitv:id/element_social_buttons_share_button_container"));
		Assert.assertTrue(share.size()>0);
		List<WebElement> reminder = driver.findElements(By.id("com.mitv:id/element_social_buttons_reminder"));
		Assert.assertTrue(reminder.size()>0);
	}

	public static void verifyLikeIsEmpty(AppiumDriver driver) throws InterruptedException {
		implicitWait(driver,60, TimeUnit.SECONDS);
		Reporter.log("verify like empty", true);
		profilePage(driver);
		List<WebElement> login = driver.findElements(By.id("com.mitv:id/myprofile_login_container_text"));
		if(login.size()>0) {
			testLogin(driver);
		}

		//verify empty like screen
		WebElement likeCount = driver.findElement(By.id("com.mitv:id/myprofile_likes_count_tv"));
		Assert.assertEquals("(0)", likeCount.getText());
		testLogout(driver);
	}

	public static void clickBack(AppiumDriver driver) {
		WebElement clickBack = driver.findElement(By.name("Me gustan, Navigate up"));
		clickBack.click();
	}

	public static void verifyLikeExist(AppiumDriver driver) throws InterruptedException {
		Reporter.log("verify like exists", true);
		implicitWait(driver,60, TimeUnit.SECONDS);
		driver.findElement(By.id("com.mitv:id/tab_me")).click();
		driver.findElement(By.id("com.mitv:id/myprofile_likes_title_tv")).click();
		List<WebElement> likedProg = driver.findElements(By.id("com.mitv:id/row_likes_button_tv"));
		Assert.assertTrue(likedProg.size()>0);
		likedProg.get(0).click();
		List<WebElement> no = driver.findElements(By.id("com.mitv:id/dialog_remove_notification_button_no"));
		Assert.assertTrue(no.size()>0);
		driver.findElement(By.id("com.mitv:id/dialog_remove_notification_button_yes")).click();
		clickBack(driver);
		testLogout(driver);
	}

	/*public static void programCount(AppiumDriver driver) throws InterruptedException {
		int count;
		driver.findElement(By.id("com.mitv:id/element_tab_text_guide")).click();
		driver.findElement(By.name("Todos")).click();
		Thread.sleep(3000);
		List<WebElement> progList = driver.findElements(By.id("com.mitv:id/tvguide_program_line_live"));
		count=progList.size();	
		String lastlink="Agrega canales a tu programación";		
		WebElement scrollTodos = driver.findElement(By.id("com.mitv:id/tvguide_table_listview"));
		scrollDown(scrollTodos, lastlink, driver);
		count=count+progList.size();
		Reporter.log("Program count=" + count,true);
	}*/
	
	public static void scrollDown(WebElement scroll, String tillLastLink, AppiumDriver driver) throws InterruptedException {
		implicitWait(driver, 1, TimeUnit.MINUTES);
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			HashMap<String, String> scrollObject = new HashMap<String, String>();
			scrollObject.put("direction", "down"); 
			scrollObject.put("text", tillLastLink);
			scrollObject.put("element",( (RemoteWebElement) scroll).getId());
			js.executeScript("mobile: scrollTo", scrollObject);
		} catch (Exception e) {
			System.out.println(e);
			Reporter.log("Scroll bar error", true);
		}
	}
	
	public static void implicitWait(AppiumDriver driver,long i, TimeUnit minutes) {
		driver.manage().timeouts().implicitlyWait(i, minutes);	
	}

	public static void explicitlyWait(AppiumDriver driver, long i, WebElement element) {
		WebDriverWait wait=new WebDriverWait(driver,i);
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	/*public static void multiGestureSingleActionTest(AppiumDriver driver) throws InterruptedException {
		MultiTouchAction multiTouch = new MultiTouchAction(driver);
		TouchAction action0 = new TouchAction(driver).tap(100,300);
		multiTouch.add(action0).perform();
	}*/
}
