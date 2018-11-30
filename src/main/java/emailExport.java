import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;


public  class emailExport  {

      public static  Properties readConifg(String confFilePath) throws Exception {
        if(confFilePath==null){
            confFilePath="config.properties";
        }
        Properties prop = new Properties();

        InputStream in = new BufferedInputStream(new FileInputStream(confFilePath));

        prop.load(in);

        in.close();

        return prop;
    }

    public static void main(String[] args) throws Exception {
        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.

        final String confFilePath = args[0];

        final String outFilePath =  args[1];

        Properties properties =  readConifg(confFilePath);

        String username = properties.getProperty("username");

        String password = properties.getProperty("password");

        final int time =Integer.parseInt( properties.getProperty("times"));

        final WebDriver driver = new ChromeDriver();

        // And now use this to visit Google
        driver.get("https://qiye.163.com/login/");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        // Find the text input element by its name
        driver.findElement(By.id("accname")).sendKeys(username);
        driver.findElement(By.id("accpwd")).sendKeys(password);

        // Enter something to search for
        ((ChromeDriver) driver).findElementByXPath("html/body/section/div/div/div[2]/div[2]/form[1]/div[4]/button").click();

        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                ((ChromeDriver) driver).findElement(By.id("_mail_tabitem_1_31text")).click();
                driver.switchTo().frame("frmoutlink.OutlinkModule_0");
                ((ChromeDriver) driver).findElementByXPath("html/body/div[3]/div/div/div[2]/div/span").click();

                try{
                    ((ChromeDriver) driver).executeScript("document.documentElement.scrollTop=10000");
                    //根据公司邮箱多少来延时
                    Thread.sleep(time);
                    List<WebElement> webElements = driver.findElements(By.className("Corp-corpCnta-mainList-colEmail-content"));

                    File file =new File(outFilePath);

                    if(!file.exists()){
                        file.createNewFile();
                    }else {
                        //考虑到定时任务的时候如果存在则直接删除该文件
                        file.delete();
                    }
                    for(int i=0;i<webElements.size();i++){
                        String usernames = webElements.get(i).getText();//获取每一个Example对象
                        //true = append file
                        FileWriter fileWritter = new FileWriter(file.getName(),true);
                        fileWritter.write(usernames+'\n');
                        fileWritter.close();
                    }
                }catch(Exception e){
                    System.out.println(e);
                }finally {
                    return  true;
                }
            }
        });

        driver.quit();


        // Check the title of the page

    }
}