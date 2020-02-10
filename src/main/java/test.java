import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import java.util.Map;
import java.util.Set;


public class test {

    public static void main(String[] arguments) {

        String LOGIN = "anielskieoczko@wp.pl";
        String PASSWORD = "dotrix";

        WebDriver driver = login.createDriver();
        login.login(driver, LOGIN, PASSWORD);

        SessionId sessionid = ((RemoteWebDriver) driver).getSessionId();

        Set<Cookie> allCookies = driver.manage().getCookies();
        System.out.println(allCookies);

        /* check if max resources max capacity reached */

        //Boolean status = economy.isBuildingQueueBussy(driver);
        //Map capacity = economy.getResourcesWarhouseCappacity(driver);
        //System.out.println(capacity.get(resources.RESOURCES_METAL.toString()));

        //economy.build(driver, buildings.CRYSTAL.toInt());

        System.out.println(economy.checkIfPossibleToBuild(driver, buildings.CRYSTALSTORE.toInt()));
        //String metal = economy.getStore(driver, resources.resources_energy);
        //System.out.println(metal);
    }

}
