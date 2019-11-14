import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SQLTest {

    public static WebDriver driver;

    @BeforeClass
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\theli\\Desktop\\chromedriver.exe");
    driver = new ChromeDriver();
    }


    //This test will determine if we successfully selected an item from
    //the dropdown menu and navigated to the next page
    @Test
    public void testDropDownSubmit() {

    }

}
