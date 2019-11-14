package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class Main {

    private static WebDriver driver;

    public static void main(String[] args) {

        //set up Chrome Driver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\theli\\Desktop\\chromedriver.exe");
        driver = new ChromeDriver();

        //load the initial page
        driver.get("https://ggc.gabest.usg.edu/pls/B400/bwckschd.p_disp_dyn_sched\n");


        Select semesterDropDown = new Select(driver.findElement(By.xpath("/html/body/div[3]/form/table/tbody/tr/td/select")));
        semesterDropDown.selectByIndex(2);

        WebElement submitButton = driver.findElement(By.xpath("/html/body/div[3]/form/input[2]"));
        submitButton.click();



        Select classType = new Select(driver.findElement(By.xpath("//*[@id=\"subj_id\"]")));
        classType.selectByValue("ITEC");

        WebElement submitButton2 = driver.findElement(By.xpath("/html/body/div[3]/form/input[12]"));
        submitButton2.click();
    }
}

