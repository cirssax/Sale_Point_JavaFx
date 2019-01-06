/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Alertas;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import proyecto.MenuController;

/**
 * FXML Controller class
 *
 * @author Cristobal
 */
public class AccesoCorrectoController implements Initializable {

    /**
     * Initializes the controller class.
    */
    private double xOffset = 0;
    private double yOffset = 0;
    
    private static String Txt="Acceso Correcto";
    @FXML
    private Label Mensaje;
    @FXML
    private JFXButton Acceso;
    
    public static void SetMensaje(String Msg)
    {
        Txt = Msg;
    }
    @FXML
    private void CerrarVentana(MouseEvent event)
    {
        try
        {
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/proyecto/Menu.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage panel = new Stage();
                panel.initStyle(StageStyle.UNDECORATED);
                //Opcion para hacerlo movible
                //Seteo del evento para el mouse
                root1.setOnMousePressed(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    }
                });
                //Accion del evento de presionar
                root1.setOnMouseDragged(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        panel.setX(event.getScreenX() - xOffset);
                        panel.setY(event.getScreenY() - yOffset);
                    }
                });
                panel.setScene(new Scene(root1));
                panel.show();
            }
            catch(Exception e)
            {
                System.out.print("Error en llamada a menu: "+e);
            }
        }
        catch(Exception e)
        {
            System.out.print("Error en cerrar la ventan de acceso correcto: "+e);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Mensaje.setText(Txt);
    }    
    
}
