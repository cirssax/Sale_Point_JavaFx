/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribuidor;

import Alertas.AlertaDatoErroneoController;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Algoritmos.Algoritmos;
import ConexionBBDD.Conexion;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import proyecto.MenuController;
/**
 * FXML Controller class
 *
 * @author Cristobal
 */
public class SolicitudController implements Initializable {

    /**
     * Initializes the controller class.
     */    
    private static String ID_PROVEEDOR="";
    private static String ID_PRODUCTO="";
    public static void SetIdProveedor(String id)
    {
        ID_PROVEEDOR=id;
    }
    public static void SetIdProducto(String id)
    {
        ID_PRODUCTO=id;
    }
    @FXML
    public JFXTextField TxtCantidad;
    
    private void UpdateBBDD()
    {
        java.util.Date FechaActual = new java.util.Date();
        SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String Fecha = FormatoFecha.format(FechaActual), StockActual="";
        int aux;
        try
        {
           Conexion miconexion =  new Conexion();
           String Consulta="SELECT stock "+
                           "FROM producto "+
                           "WHERE id_producto = "+ID_PRODUCTO;
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                   StockActual = consulta.getString(1);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
        aux = Integer.parseInt(TxtCantidad.getText()) + Integer.parseInt(StockActual);
        try   
        {
            Conexion miconexion=new Conexion();
            String Consulta = "UPDATE producto "+
                              "SET "+
                              "stock = "+String.valueOf(aux)+", "+
                              "fecha_ulti_mod_prod = '"+Fecha+"' "+
                              "WHERE id_producto = "+ID_PRODUCTO;        
            miconexion.psPrepararSentencias= miconexion.miconexion.prepareStatement(Consulta); 
            if(miconexion.psPrepararSentencias.executeUpdate()>0)
                System.out.println("Actualizacion de productos correcta");
        }
        catch (Exception e)
        {
            System.out.println("Error en actualizacion de productos: "+e);
        }
    }
    
    private void InsertBBDD()
    {
        java.util.Date FechaActual = new java.util.Date();
        SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String Fecha = FormatoFecha.format(FechaActual);
        try
        {
            Conexion miconexion1=new Conexion();
            String sentencia1="INSERT INTO provee (id_producto, id_proveedor, fecha_creac_prov, cantidad_sol) VALUES (?,?,?,?) ";
            miconexion1.psPrepararSentencias=miconexion1.miconexion.prepareStatement(sentencia1);
            miconexion1.psPrepararSentencias.setString(1, ID_PRODUCTO);//id producto
            miconexion1.psPrepararSentencias.setString(2, ID_PROVEEDOR);//id proveedor
            miconexion1.psPrepararSentencias.setString(3, Fecha); //fecha solicitud
            miconexion1.psPrepararSentencias.setString(4, TxtCantidad.getText()); //cantidad solicitada
            if(miconexion1.psPrepararSentencias.executeUpdate()>0)
            {
                System.out.println("Solicitud de producto realizada correctamente");
            }
        }
        catch (Exception e)
        {
            System.out.println("Error en solicitud de producto: "+e);
        }

    }
    @FXML
    private void Cerrar(MouseEvent event)
    {
        ID_PROVEEDOR="";
        ID_PRODUCTO="";
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
    private void Enviar(MouseEvent event)
    {
        Algoritmos valid = new Algoritmos();
        int i=0;
        if(!valid.ValidacionNumerica(TxtCantidad.getText()))
        {
            i++;
            TxtCantidad.setText("");
        }
        else if(TxtCantidad.getText().equals("0"))
        {
            i++;
            TxtCantidad.setText("");
        }
        if(i==0)
        {
            InsertBBDD();
            UpdateBBDD();
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
            ID_PROVEEDOR="";
            ID_PRODUCTO="";
        }
        else
        {
            try
            {
                AlertaDatoErroneoController.SetMensaje("Solo se pueden ingresar numeros mayores a cero");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Alertas/AlertaDatoErroneo.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root1));
                stage.show();
            }
            catch(Exception e)
            {
                System.out.print("Error: "+e);
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
