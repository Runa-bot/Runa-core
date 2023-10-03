package com.kindit.bot.data;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class StreamParser {
    private TwitchCategory category;
    private String streamUrl;

    public String getStreamUrl() {
        return streamUrl;
    }

    public StreamParser(TwitchCategory category) {
        this.category = category;

        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        switch (category) {
            case MINECRAFT:
                driver.get("https://www.twitch.tv/directory/category/minecraft");
                break;
        }

        WebElement element = driver.findElement(By.xpath("//div[@data-target=\"directory-first-item\"]/div/div/article/div[2]/div[5]/a"));
        this.streamUrl = element.getAttribute("href");
        driver.close();
    }


}

