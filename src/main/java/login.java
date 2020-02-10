import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class login {





    public static WebDriver createDriver() {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\RJ\\Downloads\\chromedriver_win32 (79)\\chromedriver.exe");
        return new ChromeDriver();
    }

    public static void login(WebDriver driver, String LOGIN, String PASSWORD) {
        /* open web page */
        driver.navigate().to("https://lobby.ogame.gameforge.com/en_GB/");
        WebDriverWait wait = new WebDriverWait(driver, 60);// 1 minute

        /* change to from default (register) view to login */
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"loginRegisterTabs\"]/ul/li[1]/span")));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"loginRegisterTabs\"]/ul/li[1]/span"));
        loginButton.click();

        /* input login and password */
        WebElement loginInput = driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div[1]/div/input"));
        loginInput.sendKeys(LOGIN);
        WebElement passwordInput = driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div[2]/div/input"));
        passwordInput.sendKeys(PASSWORD);

        WebElement loginConfirm = driver.findElement(By.xpath("//*[@id=\"loginForm\"]/p/button[1]/span"));
        loginConfirm.click();

        /* join to latest game/universum */
        /* wait until element loaded and visible */
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"joinGame\"]/button")));
        WebElement joinGame = driver.findElement(By.xpath("//*[@id=\"joinGame\"]/button"));
        joinGame.click();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        /* switch to tab 1 */
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        System.out.println("Login completed as: " + LOGIN);
    }

}
