/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comprador;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import java.net.URL;
import java.security.Provider.Service;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Cristobal
 */
public class ProgresoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    class PorHilos implements Runnable
    {
        @Override
        public void run()
        {
            for (int i=0; i<100; i++)
            {
                try
                {
                    Progreso.setProgress(i/100.0);
                    Thread.sleep(100);
                }
                catch(InterruptedException e)
                {
                    System.out.println(e);
                }
            }
            Cerrar.setVisible(true);
        }
    }
    
    @FXML
    private Label Titulo;
    @FXML
    private JFXButton OperarTarjeta, Cerrar;
    @FXML
    private JFXProgressBar Progreso;
    @FXML
    private void Cerrar(MouseEvent event)
    {
        try
        {
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
        catch(Exception e)
        {
            System.out.print("Error: "+e);
        }
    }
    
    @FXML
    private void Operar(MouseEvent event)
    {
        Thread th = new Thread(new PorHilos());
        th.start();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Progreso.setProgress(0.0);
        Cerrar.setVisible(false);
    }    
    
}
