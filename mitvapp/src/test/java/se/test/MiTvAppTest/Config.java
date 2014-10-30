package se.test.MiTvAppTest;

import io.appium.java_client.AppiumDriver;
import java.io.File;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;

public class Config {

	public static AppiumDriver driver;
	
	public void startClass(AppiumDriver driver) throws Exception {
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
}
