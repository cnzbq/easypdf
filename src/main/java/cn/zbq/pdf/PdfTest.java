package cn.zbq.pdf;

import cn.zbq.pdf.pool.WebDriverPool;
import org.openqa.selenium.By;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.PrintsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.print.PageMargin;
import org.openqa.selenium.print.PageSize;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileOutputStream;
import java.time.Duration;
import java.util.Base64;

/**
 * @link <a href="https://developer.chrome.com/docs/chromium/headless?hl=zh-cn">Chrome 无头模式</a>
 * @link <a href="https://www.selenium.dev/zh-cn/documentation/webdriver/interactions/print_page/">打印页面</a>
 * @link <a href="https://developer.chrome.com/docs/chromedriver?hl=zh-cn">ChromeDriver 概览</a>
 * <p/>
 * @since 2024/11/11
 */
public class PdfTest {

    public static void main(String[] args) {
        // 指定驱动，否则需要从https://googlechromelabs.github.io/chrome-for-testing/known-good-versions-with-downloads.json下载
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.home") + "\\.cache\\selenium\\chromedriver-win64\\chromedriver.exe");

        try {
            WebDriver driver = WebDriverPool.getWebDriver();

            PrintOptions printOptions = new PrintOptions();
            // 打印方向：PORTRAIT 纵向  /   LANDSCAPE 横向
            printOptions.setOrientation(PrintOptions.Orientation.PORTRAIT);
            // 纸张大小 默认A4
            printOptions.setPageSize(new PageSize());
            // 打印边距
            printOptions.setPageMargin(new PageMargin(0, 0, 0, 0));

            driver.get("http://127.0.0.1:5503/build/7041afc2-4c42-4b75-890d-14173203ec66.html");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            // 等待分页完成  pagedjs初始化完成的标志
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".pagedjs_pages")));
            // 等待图表加载完成 页面加载完成需要在div中增加一个名为chart-class的class
            /**
             * example: 自定义页面初始化好的话，可以在js中设置一个class，然后通过css选择器选择这个class，即可完成
             * <script>
             *      var div = document.getElementById('myDiv');
             *      div.setAttribute('class', 'chart-class');
             * </script>
             *
             * <body>
             *   <div id="myDiv"> </div>
             *   ...
             * </body>
             */
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".chart-class")));

            PrintsPage printer = (PrintsPage) driver;
            Pdf printedPage = printer.print(printOptions);
            byte[] pdfBytes = Base64.getDecoder().decode(printedPage.getContent());

            try (FileOutputStream fos = new FileOutputStream("output.pdf")) {
                fos.write(pdfBytes);
            }

            WebDriverPool.releaseWebDriver(driver);
            System.out.println("执行完成");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        WebDriverPool.close();
    }

}
