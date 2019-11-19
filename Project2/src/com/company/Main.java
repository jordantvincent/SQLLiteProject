package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class Main {

    private static WebDriver driver;


    public static void createDatabase(String filename) {
        String url = "jdbc:sqlite:C:\\Users\\theli\\IdeaProjects\\Project2\\Database" + filename;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Driver name: " + meta.getDriverName());
                System.out.println("Database Created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        //createDatabase("sqlite-jdbc-3.27.2.1.jar");
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:BannerTeachingData.db");
            System.out.println("Successfully connected to BannerTeachingData.db!");

        } catch (Exception e) {
            //System.out.println(e.getMessage());
            //System.exit(0);
            System.out.println("There was a problem with connection to the database ... idiot ");

        }

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


