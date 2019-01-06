/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comprador;

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
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.StageStyle;
import ConexionBBDD.*;
import com.jfoenix.controls.JFXComboBox;
import java.sql.*;
import java.text.SimpleDateFormat;
/**
 * FXML Controller class
 *
 * @author Cristobal
 */
public class FormularioCompradorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static boolean Controlador=false;
    private static String ID="";
    public static void SetController(boolean ctrl, String Id)
    {
        Controlador = ctrl;
        ID=Id;
    }
    @FXML
    private JFXButton BtnAceptar;
    @FXML
    public JFXTextField Nombre, Fono, Correo;
    @FXML
    private JFXComboBox<String> ComboEstadoCliente;
    
    private void InsertBBDD()
    {
        java.util.Date FechaActual = new java.util.Date();
        SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String Fecha = FormatoFecha.format(FechaActual);
        try
        {
            Conexion miconexion=new Conexion();
            String sentencia="INSERT INTO cliente (estado_clie, nombre_clie, correo_clie, fono_clie, fecha_clie_creac, fecha_ult_mod_clie) VALUES (?,?,?,?,?,?) ";
            miconexion.psPrepararSentencias=miconexion.miconexion.prepareStatement(sentencia);
            miconexion.psPrepararSentencias.setString(1, "1");//Estado del cliente
            miconexion.psPrepararSentencias.setString(2, Nombre.getText().toUpperCase());//Nombre
            miconexion.psPrepararSentencias.setString(3, Correo.getText().toUpperCase());//Correo
            miconexion.psPrepararSentencias.setString(4, Fono.getText());//Fono
            miconexion.psPrepararSentencias.setString(5, Fecha);//Fecha de creacion
            miconexion.psPrepararSentencias.setString(6, Fecha);//Fecha de ultima modificacion
            if(miconexion.psPrepararSentencias.executeUpdate()>0)
            {
                System.out.println("Datos Almacenados correctamente");
            }
        }
        catch (Exception e)
        {
            System.out.println("Error de creacion de usuario: "+e);
        }
    }
    private boolean UsuarioRepetido(String Nombre)
    {
        boolean valid = false;
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT " +
                            "CASE " +
                            "	WHEN SUM(cliente.id_cliente + 0) IS NULL THEN -1 " +
                            "   ELSE SUM(cliente.id_cliente + 0) END " +
                            "FROM cliente " +
                            "WHERE " +
                            "cliente.nombre_clie = '"+Nombre+"' ";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {
                    if(consulta.getString(1).equals("-1"))
                        valid = true;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
        return valid;
    }
    private void UpdateBBDD()
    {
        java.util.Date FechaActual = new java.util.Date();
        SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String Fecha = FormatoFecha.format(FechaActual);
        String aux="";
        System.out.println(ComboEstadoCliente.getValue().toString());
        if(ComboEstadoCliente.getValue().toString().equals("ACTIVO"))
            aux="1";
        else
            aux="0";
        try   
        {
            Conexion miconexion=new Conexion();
            String Consulta = "UPDATE cliente "+
                              "SET "+
                              "nombre_clie= '"+Nombre.getText().toUpperCase()+"', "+
                              "fono_clie= '"+Fono.getText()+"', "+
                              "correo_clie= '"+Correo.getText().toUpperCase()+"', "+
                              "estado_clie= "+aux+", "+
                              "fecha_ult_mod_clie= '"+Fecha+"' "+
                              "WHERE id_cliente = "+ID;        
            miconexion.psPrepararSentencias= miconexion.miconexion.prepareStatement(Consulta); 
            if(miconexion.psPrepararSentencias.executeUpdate()>0)
                System.out.println("Actualizacion de datos correcta");
        }
        catch (Exception e)
        {
            System.out.println("Error en actualizacion de datos: "+e);
        }
    }
    
    @FXML
    private void Aceptar(MouseEvent event)
    {
        Algoritmos valid = new Algoritmos();
        int i=0;
        if(Correo.getText().equals("") && Fono.getText().equals(""))
            i++;
        if(!valid.ValidacionLetras(Nombre.getText()))
        {
            i++;
            Nombre.setText("");
        }
        if(!valid.ValidacionNumerica(Fono.getText()))
        {
            i++;
            Fono.setText("");
        }
        if(i==0)
        {
            if(Controlador)
            {//Realizar un Update
                UpdateBBDD();
                try
                {
                    final Node source = (Node) event.getSource();
                    final Stage stage = (Stage) source.getScene().getWindow();
                    try
                    {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Alertas/AccesoCorrectoSimple.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        Stage panel = new Stage();
                        panel.initStyle(StageStyle.UNDECORATED);
                        panel.setScene(new Scene(root1));
                        panel.show();
                    }
                    catch(Exception e)
                    {
                        System.out.print("Error: "+e);
                    }
                    stage.close();
                }
                catch(Exception e)
                {
                    System.out.print("Error: "+e);
                }
                if(Controlador)
                {
                    Controlador = false;
                    ID="";
                }
            }
            else
            {//Realizar un Insert
                if(UsuarioRepetido(Nombre.getText().toUpperCase()))
                {
                    InsertBBDD();
                    try
                    {
                        final Node source = (Node) event.getSource();
                        final Stage stage = (Stage) source.getScene().getWindow();
                        try
                        {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Alertas/AccesoCorrectoSimple.fxml"));
                            Parent root1 = (Parent) fxmlLoader.load();
                            Stage panel = new Stage();
                            panel.initStyle(StageStyle.UNDECORATED);
                            panel.setScene(new Scene(root1));
                            panel.show();
                        }
                        catch(Exception e)
                        {
                            System.out.print("Error: "+e);
                        }
                        stage.close();
                    }
                    catch(Exception e)
                    {
                        System.out.print("Error: "+e);
                    }
                    if(Controlador)
                    {
                        Controlador = false;
                        ID="";
                    }
                }
                else
                {
                    Nombre.setText("");
                    AlertaDatoErroneoController.SetMensaje("Ya hay un usuario con ese nombre");
                    try
                    {
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
        }
        else
        {
            AlertaDatoErroneoController.SetMensaje("Datos incorrectos");
            try
            {
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
    @FXML
    private void CerrarVentana(MouseEvent event)
    {
        if(Controlador)
        {
            Controlador = false;
            ID="";
        }
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
        System.out.println(Controlador +" "+ID);
        ComboEstadoCliente.getItems().add("ACTIVO");
        ComboEstadoCliente.getItems().add("NO ACTIVO");
        if(!Controlador)
        {
            ComboEstadoCliente.setVisible(false);
            BtnAceptar.setText("Crear");
        }
        else
        {
            BtnAceptar.setText("Modificar");
            ComboEstadoCliente.setVisible(true);
            try
            {
                Conexion miconexion =  new Conexion();
                String Consulta="SELECT nombre_clie, fono_clie, correo_clie, estado_clie "+
                                "FROM cliente "+
                                "WHERE id_cliente="+ID;
                ResultSet consulta=miconexion.consulta(Consulta);
                if(consulta!=null)
                {
                    while(consulta.next())
                    {
                        Nombre.setText(consulta.getString(1));
                        Fono.setText(consulta.getString(2));
                        Correo.setText(consulta.getString(3));
                        if(consulta.getString(4).equals("1"))
                            ComboEstadoCliente.getSelectionModel().select("ACTIVO");
                        else
                            ComboEstadoCliente.getSelectionModel().select("NO ACTIVO");
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("Error conexion usuario: "+e);
            }
        }
    }    
    
}
