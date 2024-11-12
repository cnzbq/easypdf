package cn.zbq.pdf.pool;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;

/**
 * @author zbq
 * @link <a href="https://developer.chrome.com/docs/chromium/headless?hl=zh-cn">Chrome headless</a>
 * @link <a href="https://developer.chrome.com/docs/chromedriver?hl=zh-cn">ChromeDriver</a>
 * @link <a href="https://commons.apache.org/proper/commons-pool/examples.html">pool</a>
 * <p/>
 * @since 1.0.0
 */
public class WebDriverPool {
    private static final ObjectPool<WebDriver> POOL;

    static {
        GenericObjectPoolConfig<WebDriver> config = new GenericObjectPoolConfig<>();
        // 设置连接池的最大连接数
        config.setMaxTotal(20);
        // 设置连接池的最大空闲连接数
        config.setMaxIdle(5);
        // 设置连接池的最小空闲连接数
        config.setMinIdle(2);
        POOL = new GenericObjectPool<>(new WebDriverFactory(), config);
    }

    public static WebDriver getWebDriver() throws Exception {
        return POOL.borrowObject();
    }

    public static void releaseWebDriver(WebDriver driver) throws Exception {
        if (driver != null) {
            POOL.returnObject(driver);
        }
    }

    public static void close() {
        if (POOL != null) {
            POOL.close();
        }
    }
}
