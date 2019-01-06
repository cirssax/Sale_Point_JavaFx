/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Alertas;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Cristobal
 */
public class AlertaDatoErroneoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static String mensaje="";
    public static void SetMensaje(String txt)
    {
        mensaje=txt;
    }
    
    @FXML
    private Label Mensaje;
    @FXML
    private JFXButton BotonAceptar;
    @FXML
    private void CerrarVentana(MouseEvent event)
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Mensaje.setText(mensaje);
    }    
    
}
