package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
//ממיר מטח כסף לפי נתונים שמשתמש מכניס ואתר ייעודי שמכניס את הנתונים
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CurrencyConverterApp extends JFrame {
    private JLabel resultLabel;
    private JTextField amountTextField;
    private JComboBox<String> currencyComboBox;
    private JButton convertButton;

    public CurrencyConverterApp() {
        setTitle("Currency Converter");//יתירת חלון
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        resultLabel = new JLabel("Converted amount: ");
        add(resultLabel);

        amountTextField = new JTextField(10);
        add(amountTextField);

        currencyComboBox = new JComboBox<>(new String[]{"EUR/USD", "GBP/USD", "USD/JPY", "USD/CAD", "AUD/USD"});
        add(currencyComboBox);

        convertButton = new JButton("Convert");
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCurrency = (String) currencyComboBox.getSelectedItem();// לוקח את הבחירה של התיבת בחירה
                try {
                    int amount = Integer.parseInt(amountTextField.getText());// ממיר את הטקסט כסף
                    double convertedAmount = convertCurrency(selectedCurrency, amount);
                    resultLabel.setText("" + convertedAmount);
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Invalid amount");
                }
            }
        });
        add(convertButton);

        pack();
        setVisible(true);
    }

    private double convertCurrency(String currencyPair, int amount) {
        double finalResult;
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.hamara.co.il/");
        try {
            Robot robot = new Robot();// יצירת רובוט לחיצה
            Robot robot2 = new Robot();
            Thread.sleep(2000);
            WebElement webElement1 = driver.findElement(By.xpath("//*[@id=\"select2-SelectSourceCurrency-container\"]"));
            webElement1.click();
            WebElement webElementSelect = driver.findElement(By.xpath("/html/body/span/span/span[1]/input"));
            webElementSelect.sendKeys(currencyPair.substring(0, 3));
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(2000);
            WebElement webElement2 = driver.findElement(By.xpath("//*[@id=\"tab-one\"]/div/div[1]/div[4]/div/span/span[1]/span"));
            webElement2.click();
            WebElement webElementSelect2 = driver.findElement(By.xpath("/html/body/span/span/span[1]/input"));
            webElementSelect2.sendKeys(currencyPair.substring(4, 7));
            Thread.sleep(2000);
            robot2.keyPress(KeyEvent.VK_ENTER);
            robot2.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(3000);
            WebElement webElement = driver.findElement(By.xpath("//*[@id=\"SelectCurrencyAmount\"]"));
            webElement.sendKeys("" + amount);
            Thread.sleep(3000);
            WebElement enter = driver.findElement(By.xpath("//*[@id=\"calcsubmit\"]"));
            enter.click();
            Thread.sleep(6000);
            WebElement result = driver.findElement(By.xpath("//*[@id=\"ShowResultAmount\"]/span"));
            //System.out.println(result.getText());
            finalResult = Double.parseDouble(result.getText());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        return finalResult;
    }


}