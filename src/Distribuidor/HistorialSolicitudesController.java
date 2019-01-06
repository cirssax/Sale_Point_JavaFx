/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribuidor;

import Alertas.AlertaDatoErroneoController;
import Algoritmos.Algoritmos;
import ConexionBBDD.*;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.sql.*;
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
class HistorialConsulta extends RecursiveTreeObject<HistorialConsulta>
{
    StringProperty ProductoSolicitud;
    StringProperty ProveedorSolicitud;
    StringProperty CantidadSolicitud;
    StringProperty FechaSolicitud;
    
    public HistorialConsulta(String NombreProducto, String NombreProveedor, String Cantidad, String Fecha)
    {
        this.ProductoSolicitud = new SimpleStringProperty(NombreProducto);
        this.ProveedorSolicitud = new SimpleStringProperty(NombreProveedor);
        this.CantidadSolicitud = new SimpleStringProperty(Cantidad);
        this.FechaSolicitud = new SimpleStringProperty(Fecha);
    }
}

public class HistorialSolicitudesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXTextField InputProdHistorialSolicitudes;
    @FXML
    private JFXTreeTableView<HistorialConsulta> TablaSolicitudes;
    private ObservableList<HistorialConsulta> DataSolcitudes;
    
    private void BusquedaBBDD(String Nombre)
    {
        DataSolcitudes.clear();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT producto.nombre_prod, proveedor.nombre_prob, provee.cantidad_sol, provee.fecha_creac_prov "+
                            "FROM producto, proveedor, provee " +
                            "WHERE " +
                            "provee.id_producto = producto.id_producto AND " +
                            "provee.id_proveedor = proveedor.id_proveedor AND "+
                            "producto.nombre_prod LIKE '"+Nombre+"%' "+
                            "ORDER BY provee.fecha_creac_prov";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                    DataSolcitudes.add(new HistorialConsulta(consulta.getString(1), consulta.getString(2), consulta.getString(3), consulta.getString(4)));
                final TreeItem<HistorialConsulta> root =  new RecursiveTreeItem<>(DataSolcitudes, RecursiveTreeObject::getChildren);
                TablaSolicitudes.setRoot(root);
                TablaSolicitudes.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    @FXML
    private void BusquedaInstantaneaHistorial(KeyEvent event)
    {
        int i=0;
        String Nombre;
        Algoritmos valid = new Algoritmos();
        if(!"".equals(InputProdHistorialSolicitudes.getText()))
        {
            if(!valid.ValidacionLetras(InputProdHistorialSolicitudes.getText()))
            {
                i++;
                InputProdHistorialSolicitudes.setText("");
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
            DataSolcitudes.clear();
            Nombre = InputProdHistorialSolicitudes.getText().toUpperCase();
            BusquedaBBDD(Nombre);
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
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
        TreeTableColumn Producto = new TreeTableColumn("Producto");
        TreeTableColumn Proveedor = new TreeTableColumn("Proveedor");
        TreeTableColumn Cantidad = new TreeTableColumn("Cantidad");
        TreeTableColumn Fecha = new TreeTableColumn("Fecha");
        Producto.setPrefWidth(160);
        Proveedor.setPrefWidth(160);
        Cantidad.setPrefWidth(155);
        Fecha.setPrefWidth(155);
        TablaSolicitudes.getColumns().addAll(Producto, Proveedor, Cantidad, Fecha);
        Producto.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HistorialConsulta,String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<HistorialConsulta,String> param)
            {
                return param.getValue().getValue().ProductoSolicitud;
            }
        }
        );
        Proveedor.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HistorialConsulta,String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<HistorialConsulta,String> param)
            {
                return param.getValue().getValue().ProveedorSolicitud;
            }
        }
        );
        Cantidad.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HistorialConsulta,String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<HistorialConsulta,String> param)
            {
                return param.getValue().getValue().CantidadSolicitud;
            }
        }
        );
        Fecha.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HistorialConsulta,String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<HistorialConsulta,String> param)
            {
                return param.getValue().getValue().FechaSolicitud;
            }
        }
        );
        DataSolcitudes = FXCollections.observableArrayList();
    }    
    
}
