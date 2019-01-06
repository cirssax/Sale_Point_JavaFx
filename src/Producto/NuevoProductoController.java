/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import Alertas.AlertaDatoErroneoController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import Algoritmos.Algoritmos;
import ConexionBBDD.Conexion;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Distribuidor.FormularioDistribuidorController;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import proyecto.Productos;
/**
 * FXML Controller class
 *
 * @author Cristobal
 */
public class NuevoProductoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static String ID="0";
    private static boolean validacion=false;
    
    public static void SetID(String id)
    {
        ID = id;
        validacion=true;
    }
    @FXML
    public JFXTextField NombreNuevoProd, PrecioNuevoProd, StockNuevoProd, InputNuevaCategoria;
    @FXML
    private JFXButton BtnSiguiente;
    @FXML
    private JFXComboBox<String> TipoNuevoProd;
    
    private void InsertBBDD()
    {
        java.util.Date FechaActual = new java.util.Date();
        SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String Fecha = FormatoFecha.format(FechaActual);
        String IdTipoNuevo="";
        //CASO EN QUE SE DESEA CREAR UN NUEVO PRODUCTO CON UNA NUEVA CATEGORIA
        if(TipoNuevoProd.getValue().toString().equals("OTRA CATEGORIA"))
        {
            //CONSULTA DE ACCION PARA CREAR EL NUEVO TIPO
            try
            {
                Conexion miconexion1=new Conexion();
                String sentencia1="INSERT INTO tipo (descrip_tipo) VALUES (?) ";
                miconexion1.psPrepararSentencias=miconexion1.miconexion.prepareStatement(sentencia1);
                miconexion1.psPrepararSentencias.setString(1, InputNuevaCategoria.getText().toUpperCase());//Nuevo Tipo de Producto
                if(miconexion1.psPrepararSentencias.executeUpdate()>0)
                {
                    System.out.println("Nuevo Tipo Almacenado Correctamente");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error de creacion de tipo1: "+e);
            }
            //CONSULTA PARA OBTENER EL ID DEL NUEVO TIPO
            try
            {
                Conexion miconexion2 =  new Conexion();
                String Consulta2="SELECT id_tipo "+
                                "FROM tipo "+
                                "WHERE descrip_tipo='"+InputNuevaCategoria.getText().toUpperCase()+"' ";
                ResultSet consulta2=miconexion2.consulta(Consulta2);
                if(consulta2!=null)
                {
                    while(consulta2.next())
                        IdTipoNuevo = consulta2.getString(1);
                }
            }
            catch(Exception e)
            {
                System.out.println("erro de obtencion de id1: "+e);
            }  
            //CONSULTA DE ACCION PARA CREAR EL PRODUCTO CON SU NUEVA CATEGORIA
            try
            {
                Conexion miconexion=new Conexion();
                String sentencia="INSERT INTO producto (id_tipo, precio, nombre_prod, fecha_prod_creac, fecha_ulti_mod_prod, stock) VALUES (?,?,?,?,?,?) ";
                miconexion.psPrepararSentencias=miconexion.miconexion.prepareStatement(sentencia);
                miconexion.psPrepararSentencias.setString(1, IdTipoNuevo);//Id del tipo del producto
                miconexion.psPrepararSentencias.setString(2, PrecioNuevoProd.getText());//precio
                miconexion.psPrepararSentencias.setString(3, NombreNuevoProd.getText().toUpperCase());//nombre producto
                miconexion.psPrepararSentencias.setString(4, Fecha);//fecha de creacion
                miconexion.psPrepararSentencias.setString(5, Fecha);//fecha de ultima modificacion
                miconexion.psPrepararSentencias.setString(6, StockNuevoProd.getText());//stock disponible
                if(miconexion.psPrepararSentencias.executeUpdate()>0)
                {
                    System.out.println("Datos de producto creados correctamente");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error de creacion de producto1: "+e);
            }
        }
        else //CASO EN QUE SE USE UNA CATEGORÍA EXISTENTE
        {
            //CONSULTA PARA OBTENER EL ID DEL PRODUCTO
            try
            {
                Conexion miconexion =  new Conexion();
                String Consulta="SELECT id_tipo "+
                                "FROM tipo "+
                                "WHERE descrip_tipo='"+TipoNuevoProd.getValue().toString()+"' ";
                ResultSet consulta=miconexion.consulta(Consulta);
                if(consulta!=null)
                {
                    while(consulta.next())
                        IdTipoNuevo = consulta.getString(1);
                }
            }
            catch (Exception e)
            {
                System.out.println("Error de creacion de tipo2: "+e);
            }
            System.out.println("Id capturado "+IdTipoNuevo);
            try
            {
                Conexion miconexion=new Conexion();
                String sentencia="INSERT INTO producto (id_tipo, precio, nombre_prod, fecha_prod_creac, fecha_ulti_mod_prod, stock) VALUES (?,?,?,?,?,?) ";
                miconexion.psPrepararSentencias=miconexion.miconexion.prepareStatement(sentencia);
                miconexion.psPrepararSentencias.setString(1, IdTipoNuevo);//Id del tipo del producto
                miconexion.psPrepararSentencias.setString(2, PrecioNuevoProd.getText());//precio
                miconexion.psPrepararSentencias.setString(3, NombreNuevoProd.getText().toUpperCase());//nombre producto
                miconexion.psPrepararSentencias.setString(4, Fecha);//fecha de creacion
                miconexion.psPrepararSentencias.setString(5, Fecha);//fecha de ultima modificacion
                miconexion.psPrepararSentencias.setString(6, StockNuevoProd.getText());//stock disponible
                if(miconexion.psPrepararSentencias.executeUpdate()>0)
                {
                    System.out.println("Datos de producto Almacenados correctamente");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error de creacion de producto 2: "+e);
            }
        }
    }
    private void UpdateBBDD()
    {
        java.util.Date FechaActual = new java.util.Date();
        SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String Fecha = FormatoFecha.format(FechaActual);
        String aux="";
        if(TipoNuevoProd.getValue().toString().equals("OTRA CATEGORIA"))
        {
            //CONSULTA DE ACCION PARA CREAR EL NUEVO TIPO
            try
            {
                Conexion miconexion1=new Conexion();
                String sentencia1="INSERT INTO tipo (descrip_tipo) VALUES (?) ";
                miconexion1.psPrepararSentencias=miconexion1.miconexion.prepareStatement(sentencia1);
                miconexion1.psPrepararSentencias.setString(1, InputNuevaCategoria.getText().toUpperCase());//Nuevo Tipo de Producto
                if(miconexion1.psPrepararSentencias.executeUpdate()>0)
                {
                    System.out.println("Nuevo Tipo Almacenado Correctamente");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error de actualizacion de tipo 3: "+e);
            }
            //CONSULTA PARA OBTENER EL ID DEL NUEVO TIPO
            try
            {
                Conexion miconexion2 =  new Conexion();
                String Consulta2="SELECT id_tipo "+
                                "FROM tipo "+
                                "WHERE descrip_tipo='"+InputNuevaCategoria.getText().toUpperCase()+"' ";
                ResultSet consulta2=miconexion2.consulta(Consulta2);
                if(consulta2!=null)
                {
                    while(consulta2.next())
                        aux = consulta2.getString(1);
                }
            }
            catch(Exception e)
            {
                System.out.println("error de obtencion de id 3: "+e);
            }
            //CONSULTA PARA ACTUALIZAR EL DATO
            try   
            {
                Conexion miconexion=new Conexion();
                String Consulta = "UPDATE producto "+
                                  "SET "+
                                  "id_tipo= "+aux+", "+
                                  "precio = "+PrecioNuevoProd.getText()+", "+
                                  "nombre_prod ='"+NombreNuevoProd.getText().toUpperCase()+"', "+
                                  "fecha_ulti_mod_prod ='"+Fecha+"',"+
                                  "stock ="+StockNuevoProd.getText()+" "+
                                  "WHERE id_producto = "+ID;        
                miconexion.psPrepararSentencias= miconexion.miconexion.prepareStatement(Consulta); 
                if(miconexion.psPrepararSentencias.executeUpdate()>0)
                    System.out.println("Actualizacion de datos del producto correcta");
            }
            catch (Exception e)
            {
                System.out.println("Error en actualizacion de datos 3: "+e);
            }
        }
        else//CASO EN QUE SE USA UN TIPO DE PRODUCTO YA EXISTENTE
        {
            try
            {
                Conexion miconexion =  new Conexion();
                String Consulta="SELECT id_tipo "+
                                "FROM tipo "+
                                "WHERE descrip_tipo='"+TipoNuevoProd.getValue().toString()+"' ";
                ResultSet consulta=miconexion.consulta(Consulta);
                if(consulta!=null)
                {
                    while(consulta.next())
                        aux = consulta.getString(1);
                }
            }
            catch (Exception e)
            {
                System.out.println("Error de obtencion de tipo 4: "+e);
            }
            try   
            {
                Conexion miconexion=new Conexion();
                String Consulta = "UPDATE producto "+
                                  "SET "+
                                  "id_tipo= "+aux+", "+
                                  "precio = "+PrecioNuevoProd.getText()+", "+
                                  "nombre_prod ='"+NombreNuevoProd.getText().toUpperCase()+"', "+
                                  "fecha_ulti_mod_prod ='"+Fecha+"',"+
                                  "stock ="+StockNuevoProd.getText()+" "+
                                  "WHERE id_producto = "+ID;        
                miconexion.psPrepararSentencias= miconexion.miconexion.prepareStatement(Consulta); 
                if(miconexion.psPrepararSentencias.executeUpdate()>0)
                    System.out.println("Actualizacion de datos del producto correcta");
            }
            catch (Exception e)
            {
                System.out.println("Error en actualizacion de datos 4: "+e);
            }
        }
    }
    private boolean RepeticionNombre(String Nombre)
    {
        boolean valid = false;
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT " +
                            "CASE " +
                            "	WHEN SUM(producto.id_producto + 0) IS NULL THEN -1 " +
                            "   ELSE SUM(producto.id_producto + 0) END " +
                            "FROM producto " +
                            "WHERE " +
                            "producto.nombre_prod = '"+Nombre+"' ";
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
    private void SelecTipo(ActionEvent event)
    {
        if(TipoNuevoProd.getValue().toString().equals("OTRA CATEGORIA"))
            InputNuevaCategoria.setVisible(true);
        else if(TipoNuevoProd.getValue()==null)
            InputNuevaCategoria.setVisible(false);
        else
            InputNuevaCategoria.setVisible(false);
    }
    @FXML
    private void Siguiente(MouseEvent event)
    {
        Algoritmos valid = new Algoritmos();
        int i=0;
        if(!valid.ValidacionLetras(NombreNuevoProd.getText()))
        {
            i++;
            NombreNuevoProd.setText("");
        }
        if(TipoNuevoProd.getValue()==null)
            i++;
        if(TipoNuevoProd.getValue().toString().equals("OTRA CATEGORIA"))
        {
            if(!valid.ValidacionLetras(InputNuevaCategoria.getText()))
            {
                i++;
                InputNuevaCategoria.setText("");
            }
        }
        if(!valid.ValidacionNumerica(PrecioNuevoProd.getText()))
        {
            i++;
            PrecioNuevoProd.setText("");
        }
        if(!valid.ValidacionNumerica(StockNuevoProd.getText()))
        {
            i++;
            StockNuevoProd.setText("");
        }
        if(i==0)
        {
            if(validacion)
            {//Realizar un update
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
                ID="0";
                validacion=false;
            }
            else
            {//Realizar un insert
                if(RepeticionNombre(NombreNuevoProd.getText().toUpperCase()))
                {
                    InsertBBDD();
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
                    ID="0";
                    validacion=false;
                }
                else
                {
                    try
                    {
                        AlertaDatoErroneoController.SetMensaje("Ya existe un producto con ese nombre");
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
                AlertaDatoErroneoController.SetMensaje("Datos mal Ingresados");
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
    private void Cerrar(MouseEvent event)
    {
        validacion=false;
        ID="0";
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
    public void initialize(URL url, ResourceBundle rb) 
    {
        InputNuevaCategoria.setVisible(false);
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_tipo, descrip_tipo "+
                            "FROM tipo";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {
                    TipoNuevoProd.getItems().add(consulta.getString(2));
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }  
        TipoNuevoProd.getItems().add("OTRA CATEGORIA");
        if(validacion)
        {
            Productos NuevoProducto = new Productos(ID);
            NombreNuevoProd.setText(NuevoProducto.GetNombreProd()); 
            PrecioNuevoProd.setText(NuevoProducto.GetPrecioProd()); 
            StockNuevoProd.setText(NuevoProducto.GetStockProd());
            try
            {
                Conexion miconexion =  new Conexion();
                String Consulta="SELECT id_tipo, descrip_tipo "+
                                "FROM tipo "+
                                "WHERE id_tipo="+NuevoProducto.GetTipoProd();
                ResultSet consulta=miconexion.consulta(Consulta);
                if(consulta!=null)
                {
                    while(consulta.next())
                    {
                        TipoNuevoProd.setValue(consulta.getString(2));
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("ERROR: "+e);
            }                            
        }
    }    
    
}
