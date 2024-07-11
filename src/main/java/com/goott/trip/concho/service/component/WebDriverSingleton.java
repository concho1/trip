package com.goott.trip.concho.service.component;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class WebDriverSingleton {

    private static WebDriver driver;

    private WebDriverSingleton() {}

    public static WebDriver getDriver() {
        if (driver == null) {
            synchronized (WebDriverSingleton.class) {
                if (driver == null) {
                    WebDriverManager.chromedriver().driverVersion("126.0.6478.127").setup();
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--headless");
                    driver = new ChromeDriver(options);
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        if (driver != null) {
                            driver.quit();
                        }
                    }));
                }
            }
        }
        return driver;
    }
}
