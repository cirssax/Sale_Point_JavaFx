/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionBBDD;

import java.sql.*;
/**
 *
 * @author Cristobal
 */
public class Conexion 
{
    public Connection miconexion;
    public Statement stSentencias;
    public ResultSet rsDatos;
    public PreparedStatement psPrepararSentencias;
    
    public Conexion() throws SQLException, ClassNotFoundException
    {
        try
        {
            Class.forName("");
            String URL="";
            miconexion = DriverManager.getConnection(URL,"","");
            stSentencias = miconexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
        catch(ClassCastException e)
        {
            throw e;
        }
        catch(SQLException e1)
        {
            throw e1;
        }
    }
    
    public ResultSet consulta(String sql) throws SQLException
    {
        try
        {
            rsDatos = stSentencias.executeQuery(sql);
        }
        catch(SQLException e)
        {
            throw e;
        }
        return rsDatos;
    }
    public void ejecutar(String sql) throws SQLException
    {
        try
        {
            stSentencias.execute(sql);
        } 
        catch (SQLException ex)
        {
            throw ex;
        }
    }
}
