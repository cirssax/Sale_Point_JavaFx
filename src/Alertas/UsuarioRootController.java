/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Alertas;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Algoritmos.Algoritmos;
import ConexionBBDD.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * FXML Controller class
 *
 * @author Cristobal
 */
public class UsuarioRootController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static String Datos[];
    private static boolean Control=false;
    private static String ID="";
    public static void SetControlador(boolean ctrl, String Id)
    {
        Control=ctrl;
        ID = Id;
    }
    public static void SetDatos(String Informacion[])
    {
        Datos = new String [10];
        for(int i=0; i<Informacion.length; i++)
            Datos[i]=Informacion[i];
    }
    
    private boolean ValidUsuarioRoot()
    {
        boolean valid =false;
        String PassRoot = DigestUtils.sha1Hex("RootController");
        if(Usuario.getText().toUpperCase().equals("ROOT") && DigestUtils.sha1Hex(Pass.getText()).equals(PassRoot))
            valid=true;
        else
        {
            String info="";
            try
            {
                Conexion miconexion =  new Conexion();
                String Consulta="SELECT id_cargo " +
                                "FROM trabajador "+
                                "WHERE nombre_trab='"+Usuario.getText().toUpperCase()+"' AND pass_trab='"+DigestUtils.sha1Hex(Pass.getText())+"'";
                ResultSet consulta=miconexion.consulta(Consulta);
                if(consulta!=null)
                {
                    while(consulta.next())
                    {
                        info = consulta.getString(1);
                    }
                    if(info.equals("2"))
                        valid = true;
                }
            }
            catch(Exception e)
            {
                System.out.println("ERROR de acceso a BBDD: "+e);
            }
        }
        return valid;
    }
    
    @FXML
    private JFXButton Confirmar;
    @FXML
    public JFXTextField Usuario, SueldoNuevoUsuario;
    @FXML
    public JFXPasswordField Pass;
    
    private void AgregarUsuario()
    {
        System.out.println(Datos[8]);
        if(!Control)
        {
            try
            {
                Conexion miconexion=new Conexion();
                String sentencia="INSERT INTO trabajador(nombre_trab, rut ,fono_trab, correo_trab, direccion_trab, pass_trab, id_cargo, sueldo, fecha_trab_creac, fecha_ult_mod_trab, estado_trab) VALUES (?,?,?,?,?,?,?,?,?,?,?) ";
                miconexion.psPrepararSentencias=miconexion.miconexion.prepareStatement(sentencia);
                miconexion.psPrepararSentencias.setString(1, Datos[0]);//nombre
                miconexion.psPrepararSentencias.setString(2, Datos[1]);//rut
                miconexion.psPrepararSentencias.setString(3, Datos[2]);//fono
                miconexion.psPrepararSentencias.setString(4, Datos[3]);//correo
                miconexion.psPrepararSentencias.setString(5, Datos[4]);//domicilio
                miconexion.psPrepararSentencias.setString(6, Datos[5]);//pass
                miconexion.psPrepararSentencias.setString(7, Datos[6]);//cargo
                miconexion.psPrepararSentencias.setString(8, Datos[8]);//sueldo
                miconexion.psPrepararSentencias.setString(9, Datos[9]);//fechas de creacion
                miconexion.psPrepararSentencias.setString(10, Datos[9]);//fecha de ultima modificacion
                miconexion.psPrepararSentencias.setString(11, "1");//estado
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
        else
        {
            try   
            {
                Conexion miconexion=new Conexion();
                String Consulta = "UPDATE trabajador "+
                                  "SET "
                                + "nombre_trab ='"+Datos[0]+"', "
                                + "rut ="+Datos[1]+", "
                                + "fono_trab ="+Datos[2]+", "
                                + "correo_trab ='"+Datos[3]+"', "
                                + "direccion_trab ='"+Datos[4]+"', "
                                + "pass_trab ='"+Datos[5]+"', "
                                + "id_cargo ="+Datos[6]+", "
                                + "sueldo ="+Datos[8]+", "
                                + "fecha_ult_mod_trab ='"+Datos[9]+"', "
                                + "estado_trab = "+Datos[7]+" "
                                + "WHERE id_trabajador = "+ID;        
                miconexion.psPrepararSentencias= miconexion.miconexion.prepareStatement(Consulta); 
                if(miconexion.psPrepararSentencias.executeUpdate()>0)
                    System.out.println("Actualizacion de datos correcta");
            }
            catch (Exception e)
            {
                System.out.println("Error en actualizacion de datos: "+e);
            }
            
        }
        Control=false;
        String ID="";
        
    }
    @FXML
    private void CerrarVentana(MouseEvent event)
    {
        int i=0;
        Algoritmos valid = new Algoritmos();
        if(!valid.ValidacionNumerica(SueldoNuevoUsuario.getText()))
        {
             i++;
             SueldoNuevoUsuario.setText("");
        }
        if(!valid.ValidacionLetras(Usuario.getText()))
        {
            i++;
            Usuario.setText("");
            Pass.setText("");
        }
        if("".equals(Pass.getText()))
        {
            i++;
            Usuario.setText("");
            Pass.setText("");
        }
        if(i==0)
        {
            if(ValidUsuarioRoot())
            {
                Date FechaActual = new Date();
                SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                String Fecha = FormatoFecha.format(FechaActual);
                Datos[8]=SueldoNuevoUsuario.getText();
                Datos[9]=Fecha;
                try
                {
                    AgregarUsuario();
                }
                catch(Exception e)
                {
                    System.out.println("Error: "+e);
                }
                try
                {
                    try
                    {
                        Usuario.setText("");
                        Pass.setText("");
                        SueldoNuevoUsuario.setText("");
                        AccesoCorrectoController.SetMensaje("Datos Almacenados");
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Alertas/AccesoCorrectoSimple.fxml"));
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
                    final Node source = (Node) event.getSource();
                    final Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                 }
                catch(Exception e)
                {
                    System.out.print("Error: "+e);
                }
            }
            else
            {
                try
                {
                    AlertaDatoErroneoController.SetMensaje("Usuario o Contraseña mal ingresados");
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
                Usuario.setText("");
                Pass.setText("");
            }
            Control=false;
            String ID="";
        }
        else
        {
            try
            {
                AlertaDatoErroneoController.SetMensaje("Datos mal ingresados");
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
            Usuario.setText("");
            Pass.setText("");
        }
    }
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
        Control=false;
        String ID="";
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
    }    
    
}
