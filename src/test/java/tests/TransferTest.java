package tests;

import base.BaseTest;
import pages.TransferPage;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class TransferTest extends BaseTest {

@Test
void e2e_transfer_test() {
    TransferPage page = new TransferPage(driver);
    
    try {
        page.openApp();
        page.tomarEvidencia("1_inicio_app"); 
        
        page.createTransfer("98765", "100");
        page.tomarEvidencia("2_despues_de_click"); 
        
        String statusFinal = page.getStatusMessage();
        page.tomarEvidencia("3_estado_final");
        
        Assertions.assertTrue(statusFinal.contains("APROBADO"), "El estado final es: " + statusFinal);
        
    } catch (Exception e) {
        // Esta captura de emergencia nos dirá qué pasó justo antes de morir
        page.tomarEvidencia("ERROR_INESPERADO");
        e.printStackTrace(); // Esto imprimirá el error real en los logs de GitHub Actions
        throw e; // Relanzamos el error para que el test marque fallido
    }
}
    }




    