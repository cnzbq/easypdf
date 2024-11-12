package cn.zbq.pdf.pool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Objects;

/**
 * @author zbq
 * @since 1.0.0
 */
@Slf4j
public class WebDriverFactory extends BasePooledObjectFactory<WebDriver> {

    @Override
    public WebDriver create() throws Exception {
        ChromeOptions options = new ChromeOptions();
        //  default new headless mode on Chrome 112+
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }

    @Override
    public PooledObject<WebDriver> wrap(WebDriver obj) {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public void destroyObject(PooledObject<WebDriver> p) throws Exception {
        p.getObject().quit();
    }

    @Override
    public void passivateObject(PooledObject<WebDriver> p) throws Exception {
        p.getObject().manage().deleteAllCookies();
    }

    @Override
    public boolean validateObject(PooledObject<WebDriver> p) {
        return Objects.nonNull(((ChromeDriver) p.getObject()).getSessionId());
    }
}
