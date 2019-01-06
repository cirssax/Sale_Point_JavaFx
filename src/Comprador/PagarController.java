/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comprador;

import Alertas.AlertaDatoErroneoController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Algoritmos.Algoritmos;
import ConexionBBDD.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.StageStyle;
import proyecto.CompraConsulta;
import proyecto.FXMLDocumentController;
import proyecto.MenuController;
/**
 * FXML Controller class
 *
 * @author Cristobal
 */
public class PagarController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static String ID_CLIENTE="";
    private static String TotalPagar="";
    @FXML
    private JFXButton PagoEfectivo, PagoTarjeta, Pagar;
    @FXML
    public Label PagoTotal;
    @FXML
    private JFXTextField Dinero, Vuelto;  
    private static ObservableList<CompraConsulta> Data = FXCollections.observableArrayList();
    
    public static void SetIdCliente(String ID)
    {
        ID_CLIENTE=ID;
    }
    
    public static void SetCompras(ObservableList<CompraConsulta> Datos)
    {
        Data = Datos;
    }
    public static void SetMontoTotal(String Monto)
    {
        TotalPagar = Monto;
    }
    
    private void UpdateBBDD(String ID, String Cantidad, String Fecha)
    {
        String stock="";
        int aux=0;
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT stock " +
                            "FROM producto "+
                            "WHERE id_producto="+ID;
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {
                    stock = consulta.getString(1);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Error de acceso: "+e);
        }
        aux = Integer.parseInt(stock) - Integer.parseInt(Cantidad);
        try   
        {
            Conexion miconexion=new Conexion();
            String Consulta = "UPDATE producto "+
                              "SET "
                            + "fecha_ulti_mod_prod ='"+Fecha+"', "
                            + "stock = "+String.valueOf(aux)+" "
                            + "WHERE id_producto = "+ID;        
            miconexion.psPrepararSentencias= miconexion.miconexion.prepareStatement(Consulta); 
            if(miconexion.psPrepararSentencias.executeUpdate()>0)
                System.out.println("Actualizacion de datos correcta");
        }
        catch (Exception e)
        {
            System.out.println("Error en actualizacion de datos: "+e);
        }
    }
    
    private void InsertarDatosBBDDTarjeta()
    {
        java.util.Date FechaActual = new java.util.Date();
        SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String Fecha = FormatoFecha.format(FechaActual);
        String IdTrabajador = FXMLDocumentController.GetIdTrabajador();
        try
        {
            Conexion miconexion1=new Conexion();
            String sentencia1="INSERT INTO atiende (id_cliente, id_trabajador, fecha_atiende) VALUES (?,?,?) ";
            miconexion1.psPrepararSentencias=miconexion1.miconexion.prepareStatement(sentencia1);
            miconexion1.psPrepararSentencias.setString(1, ID_CLIENTE);//Id Cliente
            miconexion1.psPrepararSentencias.setString(2, IdTrabajador);//Id Trabajador
            miconexion1.psPrepararSentencias.setString(3, Fecha); //Fecha de atencion
            if(miconexion1.psPrepararSentencias.executeUpdate()>0)
            {
                System.out.println("Atencion almacenada Correctamente");
            }
        }
        catch (Exception e)
        {
            System.out.println("Error en Atencion: "+e);
        }
        for(int i=0; i<Data.size(); i++)
        {
            UpdateBBDD(Data.get(i).GetIdProducto(), Data.get(i).GetCantidadCompra(), Fecha);
            try
            {
                Conexion miconexion1=new Conexion();
                String sentencia1="INSERT INTO compra (id_cliente, id_producto, id_forma_pago, cantidad, fecha_compra) VALUES (?,?,?,?,?) ";
                miconexion1.psPrepararSentencias=miconexion1.miconexion.prepareStatement(sentencia1);
                miconexion1.psPrepararSentencias.setString(1, ID_CLIENTE);//Id Cliente
                miconexion1.psPrepararSentencias.setString(2, Data.get(i).GetIdProducto());//Id producto
                miconexion1.psPrepararSentencias.setString(3, "2"); //id de la forma de pago = tarjeta = 2
                miconexion1.psPrepararSentencias.setString(4, Data.get(i).GetCantidadCompra()); //cantidad de producto comprado
                miconexion1.psPrepararSentencias.setString(5, Fecha); //fecha de la compra
                if(miconexion1.psPrepararSentencias.executeUpdate()>0)
                {
                    System.out.println("Compra tarjeta almacenada Correctamente");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error de compra: "+e);
            }
        }
    }
    
    private void InsertarDatosBBDDEfectivo()
    {
        java.util.Date FechaActual = new java.util.Date();
        SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String Fecha = FormatoFecha.format(FechaActual);
        try
        {
            Conexion miconexion1=new Conexion();
            String sentencia1="INSERT INTO atiende (id_cliente, id_trabajador, fecha_atiende) VALUES (?,?,?) ";
            miconexion1.psPrepararSentencias=miconexion1.miconexion.prepareStatement(sentencia1);
            miconexion1.psPrepararSentencias.setString(1, ID_CLIENTE);//Id Cliente
            miconexion1.psPrepararSentencias.setString(2, FXMLDocumentController.GetIdTrabajador());//Id Trabajador
            miconexion1.psPrepararSentencias.setString(3, Fecha); //Fecha de atencion
            if(miconexion1.psPrepararSentencias.executeUpdate()>0)
            {
                System.out.println("Atencion almacenada Correctamente");
            }
        }
        catch (Exception e)
        {
            System.out.println("Error en Atencion: "+e);
        }
        for(int i=0; i<Data.size(); i++)
        {
            try
            {
                Conexion miconexion=new Conexion();
                String sentencia="INSERT INTO compra (id_cliente, id_producto, id_forma_pago, cantidad, fecha_compra) VALUES (?,?,?,?,?) ";
                miconexion.psPrepararSentencias=miconexion.miconexion.prepareStatement(sentencia);
                miconexion.psPrepararSentencias.setString(1, ID_CLIENTE);//Id Cliente
                miconexion.psPrepararSentencias.setString(2, Data.get(i).GetIdProducto());//Id producto
                miconexion.psPrepararSentencias.setString(3, "1"); //id de la forma de pago = efectivo = 1
                miconexion.psPrepararSentencias.setString(4, Data.get(i).GetCantidadCompra()); //cantidad de producto comprado
                miconexion.psPrepararSentencias.setString(5, Fecha); //fecha de la compra
                if(miconexion.psPrepararSentencias.executeUpdate()>0)
                {
                    System.out.println("Compra efectivo almacenada Correctamente");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error de compra: "+e);
            }
            UpdateBBDD(Data.get(i).GetIdProducto(), Data.get(i).GetCantidadCompra(), Fecha);
        }
    }
    
    @FXML
    private void Cancelar(MouseEvent event)
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
        ID_CLIENTE="";
        Data.clear();
    }
    @FXML
    private void PagarTarjeta(MouseEvent event)
    {
        InsertarDatosBBDDTarjeta();
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Comprador/Progreso.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage panel = new Stage();
            panel.initStyle(StageStyle.UNDECORATED);
            panel.setScene(new Scene(root1));
            panel.show();
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
        catch(Exception e)
        {
            System.out.print("Error: "+e);
        }
        MenuController.DataCompra.clear();
    }
    @FXML
    private void Pagar(MouseEvent event)
    {
        Dinero.setVisible(true);
        Vuelto.setVisible(true);
        Pagar.setVisible(true);
    }
    @FXML
    private void Vuelto(KeyEvent event)
    {
        
        Algoritmos valid = new Algoritmos();
        int i=0; 
        if(!valid.ValidacionNumerica(Dinero.getText()))
        {
            Dinero.setText("");
            Vuelto.setText("");
            i++;
        }
        if(i==0)
        {
            int dinero = Integer.parseInt(Dinero.getText()), total = Integer.parseInt(TotalPagar), aux;
            if(dinero>total)
            {
                aux = dinero - total;
                Vuelto.setText(String.valueOf(aux));
            }
        }
        else
        {
            try
            {
                AlertaDatoErroneoController.SetMensaje("Solo ingresar numeros");
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
    private void PagarEfectivo(MouseEvent event)
    {
        InsertarDatosBBDDEfectivo();
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
        MenuController.DataCompra.clear();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Dinero.setVisible(false);
        Vuelto.setVisible(false);
        Pagar.setVisible(false);
        Vuelto.setEditable(false);
        PagoTotal.setText(TotalPagar);
    }    
    
}
