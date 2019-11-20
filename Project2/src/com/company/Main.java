package com.company;

import com.sun.jdi.NativeMethodException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.sql.*;

public class Main {

    private static WebDriver driver;
    private static String connectionUrl = "jdbc:sqlite:BannerTeachingData.db";


    //method that connects us to the database
    public static void openDatabase() {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(connectionUrl);
            System.out.println("Successfully connected to BannerTeachingData.db!");

        } catch (Exception e) {
            //System.out.println(e.getMessage());
            //System.exit(0);
            System.out.println("There was a problem with connection to the database ... idiot ");

        }
    }

    public static void createProfessorTable() {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(connectionUrl);
            System.out.println("Opened Database Successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE PROFESSOR " +
                    "(ID INT PRIMARY KEY    NOT NULL," +
                    "FIRSTNAME           TEXT    NOT NULL," +
                    "LASTNAME            TEXT    NOT NULL," +
                    "MAJOR               TEXT    NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Professor Table successfully created!");

    }

    public static void populateProfessorTable() {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(connectionUrl);
            c.setAutoCommit(false);
            System.out.println("Opened Database Successfully");

            stmt = c.createStatement();
            String sql = "INSERT INTO PROFESSOR (ID,FIRSTNAME,LASTNAME, MAJOR) " +
                    "VALUES (1, 'Tacksoo', 'Im' , 'ITEC');";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO PROFESSOR (ID,FIRSTNAME,LASTNAME, MAJOR) " +
                    "VALUES (2, 'Cenguiz', 'Gunay' , 'ITEC');";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO PROFESSOR (ID,FIRSTNAME,LASTNAME, MAJOR) " +
                    "VALUES (3, 'Richard', 'Price' , 'ITEC');";
            stmt.executeUpdate(sql);


            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table successfully Populated!");

    }

    public static void createCourseTable() {
        Statement stmt = null;
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(connectionUrl);
            System.out.println("Opened Database Successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE COURSE " +
                    "(ID INT PRIMARY KEY    NOT NULL, " +
                    "SUBJECT          TEXT    NOT NULL, " +
                    "TITLE            TEXT    NOT NULL, " +
                    "COURSE_NUM       TEXT    NOT NULL, " +
                    "SEMESTER_OFFERED TEXT    NOT NULL, " +
                    "CREDIT_HOURS     TEXT    NOT NULL, " +
                    "NUM_STUDENTS          TEXT    NOT NULL, " +
                    "PROFESSOR            TEXT    NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Course Table successfully created!");

}

    public static void main(String[] args) {

        //Opens Database Connection
        openDatabase();
        //Create Professor Table
        createProfessorTable();
        //Populate Professor Table
        populateProfessorTable();
        //Create Course Table
        createCourseTable();

        //set up Chrome Driver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\theli\\Desktop\\chromedriver.exe");
        driver = new ChromeDriver();

        //load the initial page on Banner
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



