package pages;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions; // Importante
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.io.File; // Para la clase File
import org.openqa.selenium.OutputType; // Para OutputType
import org.openqa.selenium.TakesScreenshot; // Para TakesScreenshot
import org.apache.commons.io.FileUtils; // Para FileUtils

public class TransferPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public TransferPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    // Localizadores (Sin cambios)
    private By targetInput = By.xpath("//*[@id=\"root\"]/div/div/input[1]");
    private By amountInput = By.xpath("//*[@id=\"root\"]/div/div/input[2]");
    private By sendButton = By.xpath("//*[@id=\"root\"]/div/div/button");
    private By processingMsg = By.id("status-box");

    // Acciones con Polling
    public void openApp() {
        driver.get("http://localhost:5173");
    }

    public void fillForm(String target, String amount) {
        // Espera a que el input sea visible antes de escribir
        wait.until(ExpectedConditions.visibilityOfElementLocated(targetInput)).sendKeys(target);
        driver.findElement(amountInput).sendKeys(amount);
    }

    public void clickSend() {
        // Espera a que el botón sea clickeable
        wait.until(ExpectedConditions.elementToBeClickable(sendButton)).click();
    }

    

    public void createTransfer(String target, String amount) {
        fillForm(target, amount);
        clickSend();
 }
    
    public String getStatusMessage() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    try {
        // Esperamos a que el estado sea APROBADO
        wait.until(ExpectedConditions.textToBe(processingMsg, "Estado: APROBADO"));
    } catch (org.openqa.selenium.TimeoutException e) {
        // Si falla, capturamos el texto que está ahí en ese momento
        WebElement element = driver.findElement(processingMsg);
        String textoActual = element.getText();
        
        // Lanzamos un error con el mensaje real que obtuvimos
        throw new RuntimeException("El test falló esperando APROBADO. El estado actual es: " + textoActual);
    }

    return driver.findElement(processingMsg).getText();
}

public void tomarEvidencia(String nombre) {
    try {
        // 1. Asegurar que el directorio existe
        File directory = new File("target/screenshots");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // 2. Definir la ruta del archivo
        File destFile = new File(directory, System.currentTimeMillis() + "_" + nombre + ".png");
        
        // 3. Imprimir la ruta completa en los logs (ESTO ES LO QUE TE AYUDARÁ A DEPURAR)
        System.out.println("DEBUG: Intentando guardar en -> " + destFile.getAbsolutePath());
        
        // 4. Tomar y copiar la captura
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, destFile);
        
        System.out.println("Evidencia guardada exitosamente.");
    } catch (Exception e) {
        System.err.println("Error crítico al tomar captura: " + e.getMessage());
        e.printStackTrace(); 
    }
}


/*public String getStatusMessage() {

    long startTime = System.currentTimeMillis();

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));

    wait.until(
        ExpectedConditions.textToBe(processingMsg, "Estado: APROBADO")
    );

    WebElement element = driver.findElement(processingMsg);

    long endTime = System.currentTimeMillis();

    long totalTime = endTime - startTime;

    System.out.println("EL TEXTO REAL EN EL STATUS-BOX ES: " + element.getText());
    System.out.println("TIEMPO HASTA 'Estado: APROBADO': " + totalTime + " ms");
    System.out.println("TIEMPO EN SEGUNDOS: " + (totalTime / 1000.0));

    return element.getText();
}*/
}