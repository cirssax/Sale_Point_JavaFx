/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Cristobal
 */
public class CompraConsulta extends RecursiveTreeObject<CompraConsulta>
{
    StringProperty NombreProdCompra;
    StringProperty CantidadCompra;
    StringProperty MontoTotalCompra;
    String IdProductoCompra;
    String IdClienteCompra;
    int MontoTotalAux;
    String MontoTotalAux2;
    String CantidadCompra2;
    
    public CompraConsulta(String IdProducto, String IdCliente, String CantidadProd, String MontoTotal, String NombreProd)
    {
        this.CantidadCompra = new SimpleStringProperty(CantidadProd);
        this.NombreProdCompra = new SimpleStringProperty(NombreProd);
        this.MontoTotalCompra =  new SimpleStringProperty(MontoTotal);
        this.IdProductoCompra = IdProducto;
        this.IdClienteCompra = IdCliente;
        this.MontoTotalAux = Integer.parseInt(MontoTotal);
        this.MontoTotalAux2 = MontoTotal;
        this.CantidadCompra2 = CantidadProd;
    }
    public String GetMontoTotalString()
    {
        return MontoTotalAux2;
    }
    public String GetIdCliente()
    {
        return IdClienteCompra;
    }
    public String GetIdProducto()
    {
        return IdProductoCompra;
    }
    public int GetMontoTotal()
    {
        return MontoTotalAux;
    }    
    public String GetCantidadCompra()
    {
        return CantidadCompra2;
    }
}