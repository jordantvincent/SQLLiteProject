package com.company;

import com.sun.jdi.NativeMethodException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Main {

    private static WebDriver driver;
    private static String connectionUrl = "jdbc:sqlite:BannerTeachingData.db";
    private static int uniqueId = 1;



    public static void main(String[] args) {


        openDatabase();              //Opens Database Connection
        createProfessorTable();      //Create Professor Table
        populateProfessorTable();    //Populate Professor Table
        createCourseTable();         //Create Course Table



        //Insert a record int the Database
        //insertCourseRecord(subject, title, courseNumber, semester, creditHours, numStudents, professor);

        //set up Chrome Driver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\theli\\Desktop\\chromedriver.exe");
        driver = new ChromeDriver();

        for (int i = 2; i <= 38; ++i) {
            driver.get("https://ggc.gabest.usg.edu/pls/B400/bwckschd.p_disp_dyn_sched\n");
            Select semesterDropDown = new Select(driver.findElement(By.xpath("/html/body/div[3]/form/table/tbody/tr/td/select")));
            if (i == 3) continue;
            semesterDropDown.selectByIndex(i);
            String tempCourseSemester = semesterDropDown.getFirstSelectedOption().getText();
            WebElement submitButton = driver.findElement(By.xpath("/html/body/div[3]/form/input[2]"));
            submitButton.click();

            Select classType = new Select(driver.findElement(By.xpath("//*[@id=\"subj_id\"]")));
            classType.selectByValue("ITEC");

            submitButton = driver.findElement(By.xpath("/html/body/div[3]/form/input[12]"));
            submitButton.click();

            WebElement table = driver.findElement(By.xpath("/html/body/div[3]/table[1]"));
            List<WebElement> rows = table.findElements(By.tagName("tr"));
            LinkedList<String> instructorList = new LinkedList<>();
            for (int k = 1; k < rows.size() / 2; k += 2) {
                String myXpath = "/html/body/div[3]/table[1]/tbody/tr[" + (k + 1) + "]/td/table/tbody/tr[2]/td[7]";
                String tempHref = "/html/body/div[3]/table[1]/tbody/tr[" + k + "]/th/a ";

                WebElement instructor = driver.findElement(By.xpath(myXpath));
                String tempProf = instructor.getText();
                String tempCourseProfessor = "";
                if (tempProf.contains("Tacksoo") || tempProf.contains("Gunay") || tempProf.contains("Richard Price")) {

                    if (tempProf.contains("Tacksoo")) {
                        tempCourseProfessor = "Tacksoo Im";
                    }

                    if (tempProf.contains("Gunay")) {
                        tempCourseProfessor = "Cenguiz Gunay";
                    }
                    if (tempProf.contains("Richard Price")) {
                        tempCourseProfessor = "Richard Price";
                    }
                    //System.out.println("Professor " +  tempCourseProfessor);

                    WebElement href = driver.findElement(By.xpath(tempHref));
                    String courseInfoString = href.getText();
                    String[] courseInfo = courseInfoString.split(" - ");
                    String tempCourseTitle = courseInfo[0];
                    String tempCourseCRN = courseInfo[1];
                    String tempCourseSubject = courseInfo[2];

                    /*
                    System.out.println("Course Title: " + tempCourseTitle);
                    System.out.println("CRN: " + tempCourseCRN);
                    System.out.println("Subject: " + tempCourseSubject);
                     */

                    href.click();

                    WebElement tempCredits = driver.findElement(By.xpath("/html/body/div[3]/table[1]/tbody/tr[2]/td"));
                    //holds the long String containing all the information
                    String unparsedCourseInformation = tempCredits.getText();

                    String[] words = unparsedCourseInformation.split(" "); //Array to hold all words from Long String
                    String tempCreditValue = "";
                    //String tempCourseSemester = words[2];
                    //String tempCourseYear = words[3].substring(0,4);
                    //String tempCourseOffered = tempCourseSemester + " " + tempCourseYear;
                    //System.out.println(tempCourseOffered);
                    //loops through each word
                    for (int j = 0; j < words.length; j++) {
                        if (words[j].contains("000")) { // searches for word containing "000"
                            String tempString = words[j];
                            for (int p = 0; p < words[j].length(); p++) {
                                if (tempCreditValue.length() < 5) {
                                    Character tempChar = words[j].charAt(p);
                                    if (Character.isDigit(tempChar)) {
                                        tempCreditValue += tempChar;
                                    }
                                    if (tempChar.equals('.')) {
                                        tempCreditValue += tempChar;
                                    }
                                }
                            }
                            //tempCreditValue = tempString.substring(tempString.length() - 5, tempString.length());
                        }
                    }

                    String tempCourseCreditHours = tempCreditValue;
                    System.out.println(tempCourseCreditHours);
                    //System.out.println("Credits: " + tempCourseCreditHours);

                    //GET NUM_STUDENTS
                    WebElement actualEnrolled = driver.findElement(By.xpath("/html/body/div[3]/table[1]/tbody/tr[2]/td/table/tbody/tr[2]/td[2]"));
                    String tempCourseNumStudents = actualEnrolled.getText();

                    //System.out.println("Number of Students: " + tempCourseNumStudents);
                    driver.navigate().back();

                    insertCourseRecord(tempCourseSubject, tempCourseTitle, tempCourseCRN, tempCourseSemester, tempCourseCreditHours, tempCourseNumStudents, tempCourseProfessor);
                    System.out.println("Successfully inserted record for " + tempCourseProfessor + "\'s " + tempCourseSemester + " " + tempCourseTitle + " class!");
                }

                //System.out.println(instructor.getText());
                //System.out.println(href.getText());
            }


        }}

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

    public static void insertCourseRecord(String subject, String title, String courseNum, String semesterOffered, String creditHours, String numStudents, String professor) {
        Statement stmt = null;
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(connectionUrl);
            c.setAutoCommit(false);
            //System.out.println("Opened Database Successfully");

            stmt = c.createStatement();
            String sql = "INSERT INTO COURSE (ID,SUBJECT,TITLE, COURSE_NUM,SEMESTER_OFFERED,CREDIT_HOURS,NUM_STUDENTS,PROFESSOR) " +
                    "VALUES (" + uniqueId + ", '" + subject + "', '" + title + "' , '" + courseNum + "', '" + semesterOffered +"', '"
                    + creditHours + "', '" + numStudents + "', '" + professor + "');";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();
            //System.out.println("Record Successfully inserted into DB");
            //System.out.println("UniqueID pre Insert = " + uniqueId);
            uniqueId += 1;
            //System.out.println("UniqueID post Insert = " + uniqueId);

        } catch (Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        //System.out.println("Table successfully Populated!");

    }

}
