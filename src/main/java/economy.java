import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class economy {

    public static void waitObject(WebDriver driver, String elementToWaitFor, int time) {
        /**
         * Method create wait object
         * args:
         *      driver -> WebDriver object,
         *      elementToWaitFor -> xpath string to wait for,
         *      time -> set time to wait,
         **/
        WebDriverWait wait = new WebDriverWait(driver, time);// 1 minute
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementToWaitFor)));
    }

    public static void navigateMenu(WebDriver driver, int menuID) {
        /**
         * Method is used to navigate through main menu (left menu)
         * args:
         *      driver -> WebDriver object,
         *      menuID -> ID of the menu button
         **/
        economy.waitObject(driver, "//*[@id=\"menuTable\"]/li[" + menuID + "]/a", 10);
        driver.findElement(By.xpath("//*[@id=\"menuTable\"]/li[" + menuID + "]/a")).click();
    }

    public static Map checkBuildingCost(WebDriver driver, int BuildingID) {
        /**
         * Method is used to get building cost (only economy buildings for now),
         * args:
         *      driver -> WebDriver object,
         *      BuildingID -> ID of the building button
         * returns Map with building cost (metal, crystal, deuter, energy)
         **/

        Map<String, Integer> map = new HashMap<String, Integer>();

        economy.navigateMenu(driver,2);
        /* navigate to building */
        economy.waitObject(driver, "//*[@id=\"producers\"]/li[" + BuildingID + "]/span", 10);
        driver.findElement(By.xpath("//*[@id=\"producers\"]/li[" + BuildingID + "]/span")).click();

        int metalCost = 0;
        int crystalCost = 0;
        int deuterCost = 0;
        int energyCost = 0;

        metalCost = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"technologydetails\"]/div[2]/div/div[1]/ul/li[1]")).getAttribute("data-value"));
        crystalCost = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"technologydetails\"]/div[2]/div/div[1]/ul/li[1]")).getAttribute("data-value"));

        try {
            energyCost = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"technologydetails\"]/div[2]/div/ul/li[2]/span")).getAttribute("data-value"));
        }
        catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }

        try {
            deuterCost = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"technologydetails\"]/div[2]/div/div[1]/ul/li[3]")).getAttribute("data-value"));
        }
        catch(NoSuchElementException e) {
            System.out.println(e.getMessage());
        }

        /* get metal, crystal, deuter and energy cost of next building lvl */
        map.put(String.valueOf(resources.RESOURCES_METAL), metalCost);
        map.put(String.valueOf(resources.RESOURCES_CRYSTAL), crystalCost);
        map.put(String.valueOf(resources.RESOURCES_DEUTERIUM), deuterCost);

        map.put(String.valueOf(resources.RESOURCES_ENERGY),energyCost);

        return map;
    }

    public static Map getResourceLvl(WebDriver driver) {
        /**
         * Method is used to get current amount of resources gathered,
         * args:
         *      driver -> WebDriver object,
         * returns Map with resources amount (metal, crystal, deuter, energy)
         **/
        Map <String, Integer> map = new HashMap<String, Integer>();
        EnumSet.allOf((resources.class)).forEach(resourceType ->
                map.put(String.valueOf(resourceType), Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"" + resourceType + "\"]")).getText().replaceAll("\\.", ""))));
        return map;
    }

    public static boolean checkIfPossibleToBuild(WebDriver driver, int buildingId) {
        boolean ableToBuild = false;

        Boolean checkMetal = null;
        Boolean checkCrystal = null;
        Boolean checkDeuterium = null;
        Boolean checkEnergy = null;

        Map resourcesMined = getResourceLvl(driver);
        Map cost = checkBuildingCost(driver, buildingId);
        int metalIn = (int) resourcesMined.get(resources.RESOURCES_METAL.toString());
        int crystalIn = (int) resourcesMined.get(resources.RESOURCES_CRYSTAL.toString());
        int deuterIn = (int) resourcesMined.get(resources.RESOURCES_DEUTERIUM.toString());
        int energyIn = (int) resourcesMined.get(resources.RESOURCES_ENERGY.toString());

        int metalCost = (int) cost.get(resources.RESOURCES_METAL.toString());
        int crystalCost = (int) cost.get(resources.RESOURCES_CRYSTAL.toString());
        int deuterCost = (int) cost.get(resources.RESOURCES_DEUTERIUM.toString());
        int energyCost = (int) cost.get(resources.RESOURCES_ENERGY.toString());

        checkMetal = metalIn >= metalCost;
        checkCrystal = crystalIn >= crystalCost;
        checkDeuterium = crystalIn >= crystalCost;
        checkEnergy = energyIn >= energyCost;

        Boolean[] checkArray = {checkMetal, checkCrystal, checkDeuterium, checkEnergy};

        int counter = 0;

        for (int i = 0; i > checkArray.length; i++) {
            if (checkArray[i] == true) counter++;
        }

        if (counter == 4) ableToBuild = true;

        return ableToBuild;
    }

    public static void build(WebDriver driver, int buildingID) {
        /**
         * Method used to build buildings (only economy buildings)
         * args:
         *      driver -> WebDriver object,
         *      buildingID -> Id of the building,
         **/
        waitObject(driver,"//*[@id=\"menuTable\"]/li[2]/a",5);

        /* navigate to economy buildings menu */
        navigateMenu(driver,2);

        /* get building time */
        /* click on building tab */
        driver.findElement(By.xpath("//*[@id=\"producers\"]/li[" + buildingID + "]/span")).click();
        String buildingTime = driver.findElement(By.xpath("//*[@id=\"technologydetails\"]/div[2]/div/ul/li[1]/time")).getAttribute("datetime");

        int secondsToCompleteBuilding = parseBuildingTime(buildingTime);
        /* convert building time from String to int (seconds) */
        waitObject(driver,"//*[@id=\"producers\"]/li[" + buildingID +"]/span/button",5);

        if (isBuildingQueueBussy(driver)) {
            /* start building */
            driver.findElement(By.xpath("//*[@id=\"producers\"]/li[" + buildingID +"]/span/button")).click();
        }


        System.out.println("Building: " + buildingID);
        System.out.println("Build time: " + buildingTime);
        driver.manage().timeouts().implicitlyWait(secondsToCompleteBuilding, TimeUnit.SECONDS);
    }

    public static Boolean isBuildingQueueBussy(WebDriver driver) {
        /**
         * Method used to check if building queue is bussy,
         * args:
         *      driver -> WebDriver object,
         * returns TRUE if queue bussy.
         **/
        Boolean status;
        String production_queue = driver.findElement(By.xpath("//*[@id=\"productionboxbuildingcomponent\"]/div/div[2]/table/tbody/tr/td")).getAttribute("class");

        if (production_queue.toLowerCase() != "") {
            status = false;
        } else {
            status = true;
        }
        return status;
    }

    public static Map getResourcesWarhouseCappacity(WebDriver driver) {
        /**
         * Method returns map with max resources warhouse capacity
         * args:
         *      driver -> WebDriver object,
         *      buildingID -> Id of the building,
         * returns TRUE if queue bussy.
         **/
        driver.findElement(By.xpath("//*[@id=\"menuTable\"]/li[2]/span/a")).click();

        Map<String, String> map = new HashMap<String, String>();

        map.put(String.valueOf(resources.RESOURCES_METAL), driver.findElement(By.xpath("//*[@id=\"inhalt\"]/div[2]/div[2]/form/table/tbody/tr[17]/td[2]/span")).getText());
        map.put(String.valueOf(resources.RESOURCES_CRYSTAL), driver.findElement(By.xpath("//*[@id=\"inhalt\"]/div[2]/div[2]/form/table/tbody/tr[17]/td[3]/span")).getText());
        map.put(String.valueOf(resources.RESOURCES_DEUTERIUM), driver.findElement(By.xpath("//*[@id=\"inhalt\"]/div[2]/div[2]/form/table/tbody/tr[17]/td[4]/span")).getText());

        return map;
    }

    public static int parseBuildingTime(String pattern) {
        /**
         * Method returns building time in secouds, this will be used to estimate waiting time of the bot to take next action.
         * args:
         *      pattern -> format of building time,
         **/
        String days = "";
        int seconds = 0;
        /* start iteration from third character as first two are not needed ex: 'PT1D8M36S' */
        for (int i = 2; i < pattern.length(); i++) {
            if (Character.isDigit(pattern.charAt(i)))  {
                days = days + pattern.charAt(i);
            } else {
                switch (pattern.charAt(i)) {
                    case 'D':

                        seconds = seconds + (Integer.parseInt(days) * 24 * 60 * 60);
                        days = "";
                        break;
                    case 'M':

                        seconds = seconds + Integer.parseInt(days) * 60;
                        days = "";
                        break;
                    case 'S':

                        seconds = seconds + Integer.parseInt(days);
                        days = "";
                    default:
                }
            }
        }
        return seconds;
    }


    public static void main (String[] arguments) {
        EnumSet.allOf((resources.class)).forEach(resourceType -> System.out.println(resourceType));
        int crystalMineId = buildings.CRYSTAL.toInt();
        System.out.println(crystalMineId);

    }



}
