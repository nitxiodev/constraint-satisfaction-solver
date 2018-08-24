package sudoku;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Fetcher implements AutoCloseable{
    private WebDriver driver;
    private Map<String, Integer> levels;


    public Fetcher() {
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
        this.driver = new FirefoxDriver(options);
        this.levels = Stream.of(new SimpleEntry<>("EASY", 1), new SimpleEntry<>("MEDIUM", 2),
                new SimpleEntry<>("HARD", 3), new SimpleEntry<>("EVIL", 4)).
                collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    }

    public String fetch_sudoku(String level) {
        StringBuilder sudoku = new StringBuilder();
        String url = String.format("https://nine.websudoku.com/?level=%s", this.levels.getOrDefault(level, 2));
        this.driver.get(url);
        WebElement table = this.driver.findElement(By.id("puzzle_grid"));
        List<WebElement> table_rows = table.findElements(By.tagName("tr"));

        table_rows.forEach(row -> {
            row.findElements(By.tagName("td")).forEach(cell -> {
                String value = cell.findElement(By.tagName("input")).getAttribute("value");
                sudoku.append(!value.isEmpty() ? value : "0");
            });
        });
        return sudoku.toString();
    }

    @Override
    public void close() {
        this.driver.quit();
    }
}
