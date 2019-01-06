/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cuadramiento;

import Alertas.AlertaDatoErroneoController;
import Algoritmos.Algoritmos;
import ConexionBBDD.Conexion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.commons.codec.digest.DigestUtils;
import proyecto.FXMLDocumentController;

/**
 * FXML Controller class
 *
 * @author cris_
 */
class CompraConsulta extends RecursiveTreeObject<CompraConsulta>
{
    String IdTrabajador;
    String FechaString;
    StringProperty NombreCliente;
    StringProperty FechaCompra;
    StringProperty ProductoCompra;
    StringProperty CantidadProd;
    StringProperty FormaPago;
    StringProperty Trabajador;
    StringProperty Total;
    StringProperty CostoProd;
    
    public CompraConsulta(String ID, String Cliente, String TrabajadorA, String Fecha, String Pago, String Total ,String Producto, String Cantidad, String CostoTotalProd)
    {
        this.NombreCliente =  new SimpleStringProperty(Cliente);
        this.Trabajador =new SimpleStringProperty(TrabajadorA);
        this.FechaCompra = new SimpleStringProperty(Fecha);
        this.FormaPago = new SimpleStringProperty(Pago);
        this.ProductoCompra = new SimpleStringProperty(Producto);
        this.CantidadProd = new SimpleStringProperty(Cantidad);
        this.CostoProd = new SimpleStringProperty(CostoTotalProd);
        this.Total = new SimpleStringProperty(Total);
        this.IdTrabajador = ID;
        this.FechaString = Fecha;
    }
    public String GetId()
    {
        return IdTrabajador;
    }
    public String GetFecha()
    {
        return FechaString;
    }
}

public class CuadramientoCajaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private boolean Control;
    @FXML
    private JFXTreeTableView<CompraConsulta> Ventas;
    @FXML
    private JFXTreeTableView<CompraConsulta> Desgloce;
    private ObservableList<CompraConsulta>DataVentas, DataDesgloce;
    @FXML
    private Label TotalLabel;
    @FXML
    private JFXTextField DiferenciaPost,DiferenciaNegt, Usuario;
    @FXML
    private JFXPasswordField Pass;
    @FXML
    private JFXButton Confirmar;
    
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
    
    private void EnviarSiCuadra()
    {
        java.util.Date FechaActual = new java.util.Date();
        SimpleDateFormat FormatoHora = new SimpleDateFormat("HH:mm:ss");
        String Hora = FormatoHora.format(FechaActual);
        try   
        {
            Conexion miconexion=new Conexion();
            String Consulta = "UPDATE turnos "+
                              "SET "+
                              "hora_termino = '"+Hora+"', "+
                              "id_cuadramiento_caja = 0, "+
                              "cantidad_caja = 0 "+
                              "WHERE id_trabajador = "+FXMLDocumentController.GetIdTrabajador()+" AND "+
                              "hora_inicio = '"+FXMLDocumentController.GetHoraInicio()+"' AND "+
                              "fecha_inicio = '"+FXMLDocumentController.GetFecha()+"' ";        
            miconexion.psPrepararSentencias= miconexion.miconexion.prepareStatement(Consulta); 
            if(miconexion.psPrepararSentencias.executeUpdate()>0)
                System.out.println("Cierre de turno correcto");
        }
        catch (Exception e)
        {
            System.out.println("Error en actualizacion de datos: "+e);
        }
    }
    private void EnviarNoCuadra(String Cuadramiento, int tipo)
    {
        if(tipo==1)
        {//cuadramiento positivo
            java.util.Date FechaActual = new java.util.Date();
            SimpleDateFormat FormatoHora = new SimpleDateFormat("HH:mm:ss");
            String Hora = FormatoHora.format(FechaActual);
            try   
            {
                Conexion miconexion=new Conexion();
                String Consulta = "UPDATE turnos "+
                                  "SET "+
                                  "hora_termino = '"+Hora+"', "+
                                  "id_cuadramiento_caja = 1, "+
                                  "cantidad_caja = "+Cuadramiento+" "+
                                  "WHERE id_trabajador = "+FXMLDocumentController.GetIdTrabajador()+" AND "+
                                  "hora_inicio = '"+FXMLDocumentController.GetHoraInicio()+"' AND "+
                                  "fecha_inicio = '"+FXMLDocumentController.GetFecha()+"' ";        
                miconexion.psPrepararSentencias= miconexion.miconexion.prepareStatement(Consulta); 
                if(miconexion.psPrepararSentencias.executeUpdate()>0)
                    System.out.println("Cierre de turno correcto");
            }
            catch (Exception e)
            {
                System.out.println("Error en actualizacion de datos: "+e);
            }
        }
        else
        {//cuadramiento negativo
            java.util.Date FechaActual = new java.util.Date();
            SimpleDateFormat FormatoHora = new SimpleDateFormat("HH:mm:ss");
            String Hora = FormatoHora.format(FechaActual);
            try   
            {
                Conexion miconexion=new Conexion();
                String Consulta = "UPDATE turnos "+
                                  "SET "+
                                  "hora_termino = '"+Hora+"', "+
                                  "id_cuadramiento_caja = 2, "+
                                  "cantidad_caja = "+Cuadramiento+" "+
                                  "WHERE id_trabajador = "+FXMLDocumentController.GetIdTrabajador()+" AND "+
                                  "hora_inicio = '"+FXMLDocumentController.GetHoraInicio()+"' AND "+
                                  "fecha_inicio = '"+FXMLDocumentController.GetFecha()+"' ";        
                miconexion.psPrepararSentencias= miconexion.miconexion.prepareStatement(Consulta); 
                if(miconexion.psPrepararSentencias.executeUpdate()>0)
                    System.out.println("Cierre de turno correcto");
            }
            catch (Exception e)
            {
                System.out.println("Error en actualizacion de datos: "+e);
            }
        }
    }
    
    @FXML
    private void SiCuadraCaja(MouseEvent envent)
    {//funcion para mostrar las opciones en caso que si cuadre la caja
        DiferenciaPost.setVisible(false);
        DiferenciaPost.setText("");
        DiferenciaNegt.setText("");
        DiferenciaNegt.setVisible(false);
        Usuario.setVisible(true);
        Pass.setVisible(true);
        Confirmar.setVisible(true);
        Control = true;
    }
    @FXML
    private void NoCuadraCaja(MouseEvent event)
    {//funcion para mostrar las opciones en caso que no cuadre la caja
        DiferenciaPost.setVisible(true);
        DiferenciaNegt.setVisible(true);
        Usuario.setVisible(true);
        Pass.setVisible(true);
        Confirmar.setVisible(true);
        Control = false;
    }
    @FXML
    private void Confirmar(MouseEvent event)
    {//funcion para confirmar el almacenamiento de la informacion
        if(!Control)
        {//caso en que se almacena un no cuadramiento de caja
            if((!"".equals(DiferenciaPost.getText()) && "".equals(DiferenciaNegt.getText())) || ("".equals(DiferenciaPost.getText()) && !"".equals(DiferenciaNegt.getText())))
            {//caso en que estan escritas las diferencias
                if(!"".equals(Usuario.getText()) && !"".equals(Pass.getText()))
                {//caso en que estan escritos los datos del usuario administrador
                    if(ValidUsuarioRoot())
                    {//caso en que se ingrese un usuario administrador
                        if(!"".equals(DiferenciaPost.getText()))
                        {//caso en que el cuadramiento es positivo
                            EnviarNoCuadra(DiferenciaPost.getText(), 1);
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
                        else if(!"".equals(DiferenciaNegt.getText()))
                        {//caso en que el cuadramiento es negativo
                            EnviarNoCuadra(DiferenciaNegt.getText(), 2);
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
                    }
                    else
                    {//caso en que no se ha ingresado un usuario administrador
                        try
                        {
                            AlertaDatoErroneoController.SetMensaje("Ingrese un usuario Administrador");
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
                else
                {//caso en que esta vacía la casilla del administrador
                    try
                    {
                        AlertaDatoErroneoController.SetMensaje("Faltan datos de Usuario");
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
            else
            {//caso en que no se han escrito los numeros del encuadramiento
                try
                {
                    AlertaDatoErroneoController.SetMensaje("Falta la diferencia del cuadramiento");
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
        else
        {//caso en que si cuadra la caja
            if(!"".equals(Usuario.getText()) && !"".equals(Pass.getText()))
            {//caso en que estan ingresados los datos del usuario administrador
                if(ValidUsuarioRoot())
                {//caso en que se ha ingresado un usuario administrador
                    EnviarSiCuadra();
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
                else
                {//caso en que no se ha usado un usuario administrador
                    try
                    {
                        AlertaDatoErroneoController.SetMensaje("Ingrese un usuario Administrador");
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
            else
            {//caso en que no estan ingresados los datos del usuario administrador
                try
                {
                    AlertaDatoErroneoController.SetMensaje("Faltan datos de Usuario");
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
    @FXML
    private void DifPost(KeyEvent event)
    {//funcion para controlar el ingreso de numeros cuando la diferencia es positiva
        DiferenciaNegt.setText("");//Exclusion mutua del ingreso de diferencias
        int i=0; 
        String Diferencia;
        Algoritmos valid = new Algoritmos();
        if(!"".equals(DiferenciaPost.getText()))
        {//caso en que hay al menos un caracter ingresado
            if(!valid.ValidacionNumerica(DiferenciaPost.getText()))
            {//caso en que hay una letra
                i++;
                DiferenciaPost.setText("");
            }
        }
        if(i>0)
        {//Caso en que hay un error
            try
            {
                AlertaDatoErroneoController.SetMensaje("Ingrese solo numeros");
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
    private void DifNeg(KeyEvent event)
    {//funcion para controlar el ingreso de numeros para el caso en que la diferencia es negativa
        DiferenciaPost.setText("");//Exclusion mutua del ingreso de diferencias
        int i=0; 
        String Diferencia;
        Algoritmos valid = new Algoritmos();
        if(!"".equals(DiferenciaNegt.getText()))
        {//caso en que hay al menos un caracter ingresado
            if(!valid.ValidacionNumerica(DiferenciaNegt.getText()))
            {//caso en que hay una letra
                i++;
                DiferenciaNegt.setText("");
            }
        }
        if(i>0)
        {//Caso en que hay un error
            try
            {
                AlertaDatoErroneoController.SetMensaje("Ingrese solo numeros");
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
    private void DetalleCompra(MouseEvent event)
    {//funcion para obtener el detalle de la compra
        DataDesgloce.clear();
        TreeItem<CompraConsulta> Compra = Ventas.getSelectionModel().getSelectedItem();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT DISTINCT " +
                            "producto.nombre_prod, " +
                            "compra.cantidad, " +
                            "(compra.cantidad*producto.precio) AS Costo " +
                            "FROM cliente, compra, producto, forma_pago, trabajador, atiende " +
                            "WHERE " +
                            "compra.fecha_compra = atiende.fecha_atiende AND " +
                            "trabajador.id_trabajador="+Compra.getValue().GetId()+" AND compra.fecha_compra = '"+Compra.getValue().GetFecha()+"' AND " +
                            "compra.id_cliente = cliente.id_cliente AND " +
                            "compra.id_producto = producto.id_producto AND " +
                            "compra.id_forma_pago = forma_pago.id_forma_pago AND " +
                            "trabajador.id_trabajador = atiende.id_trabajador AND " +
                            "atiende.id_cliente = cliente.id_cliente";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {
                    DataDesgloce.add(new CompraConsulta("", "", "", "", "", "", consulta.getString(1), consulta.getString(2), consulta.getString(3)));
                }
                final TreeItem<CompraConsulta> root =  new RecursiveTreeItem<>(DataDesgloce, RecursiveTreeObject::getChildren);
                Desgloce.setRoot(root);
                Desgloce.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR de conexion 1: "+e);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        //Elementos que aparecen de manera invisible al inicio
        DiferenciaPost.setVisible(false);
        DiferenciaNegt.setVisible(false);
        Usuario.setVisible(false);
        Pass.setVisible(false);
        Confirmar.setVisible(false);
        //Elementos de la tabla venta
        //Columnas
        TreeTableColumn Cliente = new TreeTableColumn("Cliente");
        TreeTableColumn Fecha = new TreeTableColumn("Fecha");
        TreeTableColumn Pago = new TreeTableColumn("Pago");
        TreeTableColumn Trabajador = new TreeTableColumn("Trabajador");
        TreeTableColumn Total = new TreeTableColumn("Total");
        Cliente.setPrefWidth(145);
        Fecha.setPrefWidth(130);
        Pago.setPrefWidth(100);
        Trabajador.setPrefWidth(164);
        Total.setPrefWidth(72);
        Ventas.getColumns().addAll(Cliente, Trabajador, Fecha, Pago, Total);
        Cliente.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CompraConsulta,String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CompraConsulta,String> param)
            {
                return param.getValue().getValue().NombreCliente;
            }
        });
        Fecha.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CompraConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CompraConsulta,String> param)
            {
                return param.getValue().getValue().FechaCompra;
            }
        });
        Pago.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CompraConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<CompraConsulta,String> param)
            {
                return param.getValue().getValue().FormaPago;
            }
        });
        Trabajador.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CompraConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<CompraConsulta,String> param)
            {
                return param.getValue().getValue().Trabajador;
            }
        });
        Total.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CompraConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<CompraConsulta,String> param)
            {
                return param.getValue().getValue().Total;
            }
        });
        //Tabla detalle de compras
        TreeTableColumn Producto = new TreeTableColumn("Producto");
        TreeTableColumn Cantidad = new TreeTableColumn("Cantidad");
        TreeTableColumn CostoIndv = new TreeTableColumn("Sub Total");
        Producto.setPrefWidth(130);
        Cantidad.setPrefWidth(76);
        CostoIndv.setPrefWidth(76);
        Desgloce.getColumns().addAll(Producto, Cantidad, CostoIndv);
        Producto.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CompraConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<CompraConsulta,String> param)
            {
                return param.getValue().getValue().ProductoCompra;
            }
        });
        Cantidad.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CompraConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<CompraConsulta,String> param)
            {
                return param.getValue().getValue().CantidadProd;
            }
        });
        CostoIndv.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CompraConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<CompraConsulta,String> param)
            {
                return param.getValue().getValue().CostoProd;
            }
        });
        //iniciacion del las listas observables
        DataVentas = FXCollections.observableArrayList();
        DataDesgloce = FXCollections.observableArrayList();
        DataVentas.clear();
        int total = 0;
        java.util.Date FechaActual = new java.util.Date();
        SimpleDateFormat FormatoHora = new SimpleDateFormat("HH:mm:ss");
        String Hora = FormatoHora.format(FechaActual);
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT DISTINCT "+
                            "trabajador.id_trabajador, " +
                            "cliente.nombre_clie, " +
                            "trabajador.nombre_trab, " +
                            "compra.fecha_compra, " +
                            "forma_pago.descrip_pago, " +
                            "SUM(compra.cantidad*producto.precio) AS CostoTotal " +
                            "FROM cliente, compra, producto, forma_pago, trabajador, atiende " +
                            "WHERE " +
                            "compra.fecha_compra = atiende.fecha_atiende AND " +
                            "trabajador.id_trabajador = "+FXMLDocumentController.GetIdTrabajador()+" AND " +
                            "compra.fecha_compra >= '"+FXMLDocumentController.GetFecha()+" "+FXMLDocumentController.GetHoraInicio()+"' AND "+
                            "compra.id_cliente = cliente.id_cliente AND " +
                            "compra.id_producto = producto.id_producto AND " +
                            "compra.id_forma_pago = forma_pago.id_forma_pago AND " +
                            "trabajador.id_trabajador = atiende.id_trabajador " +
                            "GROUP BY compra.fecha_compra " +
                            "ORDER BY compra.fecha_compra";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {
                    DataVentas.add(new CompraConsulta(consulta.getString(1), consulta.getString(2), consulta.getString(3), consulta.getString(4), consulta.getString(5), consulta.getString(6), "", "", ""));
                    total = total + Integer.parseInt(consulta.getString(6));
                }
                final TreeItem<CompraConsulta> root =  new RecursiveTreeItem<>(DataVentas, RecursiveTreeObject::getChildren);
                Ventas.setRoot(root);
                Ventas.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR de conexion 1: "+e);
        }
        TotalLabel.setText("$"+String.valueOf(total));
    }    
    
}
