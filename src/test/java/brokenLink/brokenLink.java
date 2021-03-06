package brokenLink;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class brokenLink {

    public void invokeBrokenLink(String address) {
        try {
            System.setProperty("webdriver.chrome.driver", Paths.get("src/test/resources/chromedriver_win32/chromedriver.exe").toString());
            WebDriver driver;
            driver = new ChromeDriver();
            driver.manage().deleteAllCookies();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

            driver.get(address);

            List<WebElement> links = driver.findElements(By.tagName("a"));

            for (int i = 0; i < links.size(); i++) {
                WebElement element = links.get(i);
                String url = element.getAttribute("href");
                checkLink(url);
            }
            driver.close();
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkLink(String url) {
        try {
            URL link = new URL(url);

            // Create a connection using URL object by link
            HttpURLConnection httpConn =(HttpURLConnection)link.openConnection();
            //Set the timeout for 2 seconds
            httpConn.setConnectTimeout(2000);
            //connect using connect method
            httpConn.connect();
            //use getResponseCode() to get the response code.
            if(httpConn.getResponseCode()== 200) {
                System.out.println(url + " | " + httpConn.getResponseMessage());
            }
            if(httpConn.getResponseCode()== 404) {
                System.out.println(url + " | " + httpConn.getResponseMessage());
            }
        }
        //getResponseCode method returns = IOException - if an error occurred connecting to the server.
        catch (Exception e) {
            //e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        brokenLink link = new brokenLink();
        String[] urlList = {"http://vnu.edu.vn/", "http://fit.uet.vnu.edu.vn", "http://fet.uet.vnu.edu.vn", "http://hust.edu.vn", "http://usth.edu.vn"};
        for (int i = 0; i < urlList.length; i++) {
            System.out.println(i + " " + urlList[i]);
            link.invokeBrokenLink(urlList[i]);
        }

    }

}
