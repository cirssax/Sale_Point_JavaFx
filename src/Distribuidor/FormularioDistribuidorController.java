/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribuidor;

import Alertas.AlertaDatoErroneoController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import Algoritmos.Algoritmos;
import ConexionBBDD.Conexion;
import com.jfoenix.validation.NumberValidator;
import java.sql.*;
import java.text.SimpleDateFormat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * FXML Controller class
 *
 * @author Cristobal
 */
public class FormularioDistribuidorController implements Initializable {

    private static boolean validacion=false;
    private static String ID="";
    public static void SetValues (boolean valid, String Id)
    {
        validacion = valid;
        ID = Id;
    }
    
    @FXML
    public JFXTextField RutDistribuidor, FonoDistribuidor, CorreoDistribuidor, PersonaDistribuidor, Nombre;
    @FXML
    private JFXButton BtnAgregarDistribuidor;
    @FXML
    private Label Titulo;
    
    private void InsertBBDD()
    {
        java.util.Date FechaActual = new java.util.Date();
        SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String Fecha = FormatoFecha.format(FechaActual);
        try
        {
            Conexion miconexion1=new Conexion();
            String sentencia1="INSERT INTO proveedor (nombre_prob, correo_prob, fono_prob, rut_prob, persona_contacto, fecha_prov_creac, fecha_prov_ult_sol) VALUES (?,?,?,?,?,?,?) ";
            miconexion1.psPrepararSentencias=miconexion1.miconexion.prepareStatement(sentencia1);
            miconexion1.psPrepararSentencias.setString(1, Nombre.getText().toUpperCase());//nombre proveedor
            miconexion1.psPrepararSentencias.setString(2, CorreoDistribuidor.getText().toUpperCase());//correo proveedor
            miconexion1.psPrepararSentencias.setString(3, FonoDistribuidor.getText()); //fono del proveedor
            miconexion1.psPrepararSentencias.setString(4, RutDistribuidor.getText()); //rut
            miconexion1.psPrepararSentencias.setString(5, PersonaDistribuidor.getText().toUpperCase()); //persona de contacto
            miconexion1.psPrepararSentencias.setString(6, Fecha); //fecha de creacion
            miconexion1.psPrepararSentencias.setString(7, Fecha); //fecha de ultima modificacion
            if(miconexion1.psPrepararSentencias.executeUpdate()>0)
            {
                System.out.println("Distribuidor creado correctamente");
            }
        }
        catch (Exception e)
        {
            System.out.println("Error en creacion de distribuidor: "+e);
        }
    }
    private void UpdateBBDD()
    {
        java.util.Date FechaActual = new java.util.Date();
        SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String Fecha = FormatoFecha.format(FechaActual);
        try   
        {
            Conexion miconexion=new Conexion();
            String Consulta = "UPDATE proveedor "+
                              "SET "+
                              "nombre_prob = '"+Nombre.getText().toUpperCase()+"', "+
                              "correo_prob = '"+CorreoDistribuidor.getText().toUpperCase()+"', "+
                              "fono_prob = "+FonoDistribuidor.getText()+", "+
                              "rut_prob = "+RutDistribuidor.getText()+", "+
                              "persona_contacto = '"+PersonaDistribuidor.getText().toUpperCase()+"', "+
                              "fecha_prov_ult_sol = '"+Fecha+"' "+
                              "WHERE id_proveedor = "+ID;        
            miconexion.psPrepararSentencias= miconexion.miconexion.prepareStatement(Consulta); 
            if(miconexion.psPrepararSentencias.executeUpdate()>0)
                System.out.println("Actualizacion de proveedor correcta");
        }
        catch (Exception e)
        {
            System.out.println("Error en actualizacion de proveedor: "+e);
        }
        
    }
    private boolean RepeticionNombre(String Nombre)
    {
        boolean valid = false;
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT "+
                            "CASE "+
                            "WHEN SUM(proveedor.id_proveedor + 0) IS NULL THEN -1 "+
                            "ELSE SUM(proveedor.id_proveedor + 0) END "+
                            "FROM proveedor "+
                            "WHERE "+
                            "proveedor.nombre_prob = '"+Nombre+"' ";
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
    @FXML
    private void Cerrar(MouseEvent event)
    {
        validacion=false;
        ID="";
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
    private void CrearDistribuidor(MouseEvent event)
    {
        Algoritmos valid = new Algoritmos();
        int i=0;
        if(!valid.ValidacionLetras(Nombre.getText()))
        {
            Nombre.setText("");
            i++;
        }
        if(!valid.ValidacionNumerica(FonoDistribuidor.getText()))
        {
            FonoDistribuidor.setText("");
            i++;
        }
        if(!valid.ValidacionNumerica(RutDistribuidor.getText()))
        {
            i++;
            RutDistribuidor.setText("");
        }
        if(i==0)
        {
            if(validacion)//Caso en que se desean modificar datos
            {
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
                validacion=false;
                ID="";
            }
            else//Caso en que se desea crear un nuevo proveedor
            {
                if(RepeticionNombre(Nombre.getText().toUpperCase()))
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
                    validacion=false;
                    ID="";
                }
                else
                {
                    try
                    {
                        AlertaDatoErroneoController.SetMensaje("Ya existe un distribuidor con ese nombre");
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
            try
            {
                AlertaDatoErroneoController.SetMensaje("Datos incorrectos");
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
    public void initialize(URL url, ResourceBundle rb) 
    {
        NumberValidator numValidator = new NumberValidator();
        RutDistribuidor.getValidators().add(numValidator);
        numValidator.setMessage("Sin puntos ni digito verificador");
        RutDistribuidor.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if(!newValue)
                {
                    RutDistribuidor.validate();
                }
            }
        });
        if(validacion)
        {
            BtnAgregarDistribuidor.setText("Modificar");
            Titulo.setText("Modificacion de distribuidor");
            try
            {
                Conexion miconexion =  new Conexion();
                String Consulta="SELECT nombre_prob, correo_prob, fono_prob, rut_prob, persona_contacto "+
                                "FROM proveedor "+
                                "WHERE id_proveedor="+ID;
                ResultSet consulta=miconexion.consulta(Consulta);
                if(consulta!=null)
                {
                    while(consulta.next())
                    {
                        RutDistribuidor.setText(consulta.getString(4));
                        FonoDistribuidor.setText(consulta.getString(3));
                        CorreoDistribuidor.setText(consulta.getString(2));
                        PersonaDistribuidor.setText(consulta.getString(5));
                        Nombre.setText(consulta.getString(1));
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("ERROR: "+e);
            }      
        }
        else
            BtnAgregarDistribuidor.setText("Añadir");
    }    
    
}
