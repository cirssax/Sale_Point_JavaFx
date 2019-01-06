/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import Alertas.AlertaDatoErroneoController;
import Algoritmos.Algoritmos;
import ConexionBBDD.*;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.sql.ResultSet;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Cristobal
 */
class CompraConsulta extends RecursiveTreeObject<CompraConsulta>
{
    String IdCliente;
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
        this.IdCliente = ID;
        this.FechaString = Fecha;
    }
    public String GetId()
    {
        return IdCliente;
    }
    public String GetFecha()
    {
        return FechaString;
    }
}

public class ComprasRealizadasController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXTreeTableView<CompraConsulta> TablaBusquedaCompras, TablaDetalleCompra;
    private ObservableList<CompraConsulta> DataCompra1, DataCompra2;
    @FXML
    private JFXTextField BuscarClienteCompras;
    
    private void BuscarBBDD(String Nombre)
    {
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT DISTINCT "+
                            "cliente.id_cliente, " +
                            "cliente.nombre_clie, " +
                            "trabajador.nombre_trab, " +
                            "compra.fecha_compra, " +
                            "forma_pago.descrip_pago, " +
                            "SUM(compra.cantidad*producto.precio) AS CostoTotal " +
                            "FROM cliente, compra, producto, forma_pago, trabajador, atiende " +
                            "WHERE " +
                            "compra.fecha_compra = atiende.fecha_atiende AND " +
                            "cliente.nombre_clie LIKE '"+Nombre+"%' AND " +
                            "compra.id_cliente = cliente.id_cliente AND " +
                            "compra.id_producto = producto.id_producto AND " +
                            "compra.id_forma_pago = forma_pago.id_forma_pago AND " +
                            "trabajador.id_trabajador = atiende.id_trabajador AND " +
                            "atiende.id_cliente = cliente.id_cliente " +
                            "GROUP BY compra.fecha_compra " +
                            "ORDER BY compra.fecha_compra";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {
                    DataCompra1.add(new CompraConsulta(consulta.getString(1), consulta.getString(2), consulta.getString(3), consulta.getString(4), consulta.getString(5), consulta.getString(6), "", "", ""));
                }
                final TreeItem<CompraConsulta> root =  new RecursiveTreeItem<>(DataCompra1, RecursiveTreeObject::getChildren);
                TablaBusquedaCompras.setRoot(root);
                TablaBusquedaCompras.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR de conexion 1: "+e);
        }
    }
    
    @FXML
    private void BuscarDetalleCompra(MouseEvent event)
    {
        DataCompra2.clear();
        TreeItem<CompraConsulta> Compra = TablaBusquedaCompras.getSelectionModel().getSelectedItem();
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
                            "cliente.id_cliente="+Compra.getValue().GetId()+" AND compra.fecha_compra = '"+Compra.getValue().GetFecha()+"' AND " +
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
                    DataCompra2.add(new CompraConsulta("", "", "", "", "", "", consulta.getString(1), consulta.getString(2), consulta.getString(3)));
                }
                final TreeItem<CompraConsulta> root =  new RecursiveTreeItem<>(DataCompra2, RecursiveTreeObject::getChildren);
                TablaDetalleCompra.setRoot(root);
                TablaDetalleCompra.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR de conexion 1: "+e);
        }
    }
    
    @FXML
    private void BusqueadInstantenaCLientes(KeyEvent event)
    {
        int i=0;
        DataCompra2.clear();
        String Nombre;
        Algoritmos valid = new Algoritmos();
        if(!"".equals(BuscarClienteCompras.getText()))
        {
            if(!valid.ValidacionLetras(BuscarClienteCompras.getText()))
            {
                i++;
                BuscarClienteCompras.setText("");
            }
        }
        if(i>0)
        {
            try
            {
                AlertaDatoErroneoController.SetMensaje("Ingrese solo letras");
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
        else
        {
            if(!BuscarClienteCompras.getText().equals(""))
            {
                DataCompra1.clear();
                Nombre = BuscarClienteCompras.getText().toUpperCase();
                BuscarBBDD(Nombre);
            }
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
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TABLA DE COMPRAS
        //Tabla de usuario compra
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
        TablaBusquedaCompras.getColumns().addAll(Cliente, Trabajador, Fecha, Pago, Total);
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
        TablaDetalleCompra.getColumns().addAll(Producto, Cantidad, CostoIndv);
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
        DataCompra1 = FXCollections.observableArrayList();
        DataCompra2 = FXCollections.observableArrayList();
    }    
    
}
