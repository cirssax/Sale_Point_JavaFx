/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import ConexionBBDD.Conexion;
import java.sql.ResultSet;
/**
 *
 * @author Cristobal
 */
public class Productos 
{
    String IdProducto;
    String IdTipo;
    String PrecioProd;
    String NombreProd;
    String StockProd;
    String FechaUltMod;
    
    public Productos(String Id)
    {
        this.IdProducto= Id;
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_tipo, precio, nombre_prod, fecha_ulti_mod_prod, stock " +
                            "FROM producto "+
                            "WHERE id_producto="+Id;
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {
                    this.IdTipo= consulta.getString(1);
                    this.PrecioProd= consulta.getString(2);
                    this.NombreProd= consulta.getString(3);
                    this.FechaUltMod= consulta.getString(4);
                    this.StockProd= consulta.getString(5);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    
    public String GetIDProd()
    {
        return IdProducto;
    }
    public String GetTipoProd()
    {
        return IdTipo;
    }
    public String GetPrecioProd()
    {
        return PrecioProd;
    }
    public String GetNombreProd()
    {
        return NombreProd;
    }
    public String GetFechaMod()
    {
        return FechaUltMod;
    }
    public String GetStockProd()
    {
        return StockProd;
    }
}
