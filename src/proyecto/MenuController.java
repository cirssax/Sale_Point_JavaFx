/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;


import Alertas.AccesoCorrectoSimpleController;
import Alertas.AlertaDatoErroneoController;
import Alertas.UsuarioRootController;
import Algoritmos.Algoritmos;
import Comprador.FormularioCompradorController;
import Comprador.PagarController;
import ConexionBBDD.Conexion;
import Distribuidor.FormularioDistribuidorController;
import Distribuidor.SolicitudController;
import Producto.NuevoProductoController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Border;
import javafx.util.*;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import proyecto.Trabajadores;
/**
 * FXML Controller class
 *
 * @author Cristobal
 */
class HorasConsulta extends RecursiveTreeObject<HorasConsulta>
{
    StringProperty NombreTrabajador;
    StringProperty MesTrabajador;
    StringProperty AnioTrabajador;
    StringProperty DiaTurno;
    StringProperty InicioTurno;
    StringProperty FinTurno;
    StringProperty TotalTrabajadoTurno;
    StringProperty CuadramientoCaja;
    StringProperty DiferenciaCaja;
    String IdTrabajador;
    String MesRetornable;
    int HraI;
    int HraT;
    int MinI;
    int MinT;
    
    public HorasConsulta(String Nombre, String Id, String Mes, String Anio, String Dia, String Inicio, String Fin, String HraInicio, String MinInicio, String HraTermino, String MinTermino, String Cuadramiento, String Diferencia)
    {
        this.NombreTrabajador = new SimpleStringProperty(Nombre);
        this.MesRetornable = Mes;
        if(Mes.equals("1"))
            this.MesTrabajador = new SimpleStringProperty("ENERO");
        else if(Mes.equals("2"))
            this.MesTrabajador = new SimpleStringProperty("FEBRERO");
        else if(Mes.equals("3"))
            this.MesTrabajador = new SimpleStringProperty("MARZO");
        else if(Mes.equals("4"))
            this.MesTrabajador = new SimpleStringProperty("ABRIL");
        else if(Mes.equals("5"))
            this.MesTrabajador = new SimpleStringProperty("MAYO");
        else if(Mes.equals("6"))
            this.MesTrabajador = new SimpleStringProperty("JUNIO");
        else if(Mes.equals("7"))
            this.MesTrabajador = new SimpleStringProperty("JULIO");
        else if(Mes.equals("8"))
            this.MesTrabajador = new SimpleStringProperty("AGOSTO");
        else if(Mes.equals("9"))
            this.MesTrabajador = new SimpleStringProperty("SEPTIEMBRE");
        else if(Mes.equals("10"))
            this.MesTrabajador = new SimpleStringProperty("OCTUBRE");
        else if(Mes.equals("11"))
            this.MesTrabajador = new SimpleStringProperty("NOVIEMBRE");
        else if(Mes.equals("12"))
            this.MesTrabajador = new SimpleStringProperty("DICIEMBRE");
        this.AnioTrabajador = new SimpleStringProperty(Anio);
        this.DiaTurno = new SimpleStringProperty(Dia);
        this.InicioTurno = new SimpleStringProperty(Inicio);
        this.FinTurno = new SimpleStringProperty(Fin);
        this.IdTrabajador = Id;
        if(!MinTermino.equals(""))
        {
            int hrs, mins;
            String Hora="";
            mins = Integer.parseInt(MinTermino) - Integer.parseInt(MinInicio);
            if(mins < 0)
            {
                mins = mins +60;
                hrs = Integer.parseInt(HraTermino) - Integer.parseInt(HraInicio) -1;
            }
            else
                hrs = Integer.parseInt(HraTermino) - Integer.parseInt(HraInicio);
            if(hrs < 0)
                hrs = hrs +24;
            if(mins<10)
                Hora=String.valueOf(hrs)+":0"+String.valueOf(mins);
            else
                Hora=String.valueOf(hrs)+":"+String.valueOf(mins);
            this.TotalTrabajadoTurno = new SimpleStringProperty(Hora);
            this.HraI = Integer.parseInt(HraInicio);
            this.HraT = Integer.parseInt(HraTermino);
            this.MinI = Integer.parseInt(MinInicio);
            this.MinT = Integer.parseInt(MinTermino);  
        }
        else
        {
            this.TotalTrabajadoTurno = new SimpleStringProperty("");
            this.HraI = 0;
            this.HraT = 0;
            this.MinI = 0;
            this.MinT = 0;  
        }
        if(Cuadramiento.equals("0"))
        {
            this.CuadramientoCaja = new SimpleStringProperty("Si Cuadro");
            this.DiferenciaCaja = new SimpleStringProperty("0");
        }
        else if(Cuadramiento.equals("1"))
        {
            this.CuadramientoCaja = new SimpleStringProperty("Negativo");
            this.DiferenciaCaja = new SimpleStringProperty(Diferencia);
        }
        else if(Cuadramiento.equals("2"))
        {
            this.CuadramientoCaja = new SimpleStringProperty("Positivo");
            this.DiferenciaCaja = new SimpleStringProperty(Diferencia);
        }
        
    }
    
    public String GetID()
    {
        return IdTrabajador;
    }
    public String GetMes()
    {
        return MesRetornable;
    }
    public int GetHraInicio()
    {
        return HraI;
    }
    public int GetHraTermino()
    {
        return HraT;
    }
    public int GetMinInicio()
    {
        return MinI;
    }
    public int GetMinTermino()
    {
        return MinT;
    }
}

class ClienteConsulta extends RecursiveTreeObject<ClienteConsulta>
{
    StringProperty IdCliente;
    StringProperty EstCliente;
    StringProperty NombreCliente;
    String ID;
    String NombreAux;
    public ClienteConsulta(String IdIngresada, String EstadoIngresado, String NombreIngresado)
    {
        this.IdCliente = new SimpleStringProperty(IdIngresada);
        if(EstadoIngresado=="0")
            this.EstCliente = new SimpleStringProperty("INACTIVO");
        else
            this.EstCliente = new SimpleStringProperty("ACTIVO");
        this.NombreCliente = new SimpleStringProperty(NombreIngresado);
        this.ID = IdIngresada;
        this.NombreAux = NombreIngresado;
    }
    public String GetID()
    {
        return ID;
    }
    public String GetNombre()
    {
        return NombreAux;
    }
}

class ProductosConsulta  extends RecursiveTreeObject<ProductosConsulta>
{
    StringProperty IdProducto;
    StringProperty StockProd;
    StringProperty PrecioProd;
    StringProperty NombreProd;
    String IDaux;
    String Nombreaux2;
    String Stock;
    
    public ProductosConsulta(String Id, String Precio, String Nombre, String Stock)
    {
        this.IdProducto= new SimpleStringProperty(Id);
        this.PrecioProd= new SimpleStringProperty(Precio);
        this.NombreProd= new SimpleStringProperty(Nombre);
        this.StockProd= new SimpleStringProperty(Stock);
        this.IDaux = Id;
        this.Nombreaux2 = Nombre;
        this.Stock = Stock;
    }
    public String GetID()
    {
        return IDaux;
    }
    public String GetNombre()
    {
        return Nombreaux2;
    }
    public int GetStock()
    {
        return Integer.parseInt(Stock);
    }
}
class ProveedorConsulta extends RecursiveTreeObject<ProveedorConsulta>
{
    StringProperty IdProveedor;
    StringProperty NombreProveedor;
    StringProperty PersonaContacto;
    StringProperty Fono;
    String ID;
    String Nombre;
    
    public ProveedorConsulta(String Id, String NombreProveedor, String Persona, String Fono)
    {
        this.IdProveedor= new SimpleStringProperty(Id);
        this.NombreProveedor= new SimpleStringProperty(NombreProveedor);
        this.PersonaContacto= new SimpleStringProperty(Persona);
        this.Fono= new SimpleStringProperty(Fono);
        this.ID= Id;
        this.Nombre= NombreProveedor;
    }
    
    public String GetID()
    {
        return ID;
    }
    public String GetNombre()
    {
        return Nombre;
    }
}

public class MenuController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static String ID_CLIENTE="";    
    
    /*ELEMENTOS DE GRAFICOS DE PANEL DE ESTADISTICAS*/
    private boolean Semaforo1 = false, Semaforo2 = false, Semaforo3 = false;
    @FXML
    private LineChart<?,?> GraficoProductos;
    @FXML
    private CategoryAxis EjeXProductos;
    @FXML
    private NumberAxis EjeYProd;
    @FXML
    private JFXComboBox<String> ComboProd1, ComboProd3, ComboProd2;
    @FXML
    private JFXTreeTableView<Trabajadores>TablaTrabajadoresEst;
    private ObservableList<Trabajadores> DataTrabajadores;
    @FXML
    private BarChart<?, ?> GraficoTrabajadoresEst;
    @FXML
    private CategoryAxis EjeXTbjdrs;
    @FXML
    private NumberAxis EjeYTbjdrs;
    @FXML
    private JFXTreeTableView<HorasConsulta> TablaHorariosPersona, TablaPersonasTurnos;
    private ObservableList<HorasConsulta> DataTurnosPersona, DataTurnosHora;
    
    
    @FXML
    private JFXTextField BusquedaNombreProd, BusquedaTipoProd, ProdSelec, DistribuidorSelec, BuscarDistPedido, BuscarProdPedido, BusquedaClienteCompra, BusquedaProdCompra, CantidadProd, InputTrabajadorEst, Trabajador1, Trabajador2, Trabajador3, NombreTrabajadorHorarios;
    @FXML
    private AnchorPane Pane1, Pane2;
    @FXML
    private Label Texto, DescripOpcBusqueda,TotalPagar, DescripConsulta, LargoTexto, HorasTrabajadas, MensajeError;
    @FXML
    public JFXButton Boton, BtnPrincipal, BtnVenta,BtnConsultas,BtnEnviarConsulta ,BtnSolicitud, BtnBusqueda, BtnEstadistica, BtnCrearUsuario, Pagar, BtnAgregarProductoVenta, SelecProd, BtnSelecClienteVenta, BtnEliminarProdCompra, BtnHistorialSolicitudes, BtnSeleccionTbjd, BtnHorasTrabajadas;
    @FXML
    private Pane PanelPrincipal, PanelVenta, PanelSolicitud, PanelBusqueda, PaneEstTrab, PaneEstProd, PanelEstadistica, PanelConsultas, PabelHoras;
    @FXML
    private JFXTextArea ConsultaProblema;
    @FXML
    private JFXTreeTableView TablaProductos, TablaUsuarios, TablaCompra;
    @FXML
    private JFXTreeTableView<ProductosConsulta> TablaRespuestaBusqueda, ProductosSolcitud, TablaProductosVenta;
    private ObservableList<ProductosConsulta> DataProd;
    @FXML
    private JFXTreeTableView<ProveedorConsulta> Distribuidores;
    private ObservableList<ProveedorConsulta> DataProv;
    @FXML 
    private JFXTreeTableView<ClienteConsulta> TablaUsuariosVenta;
    private ObservableList<ClienteConsulta> DataClient;
    @FXML 
    private JFXTreeTableView<CompraConsulta>TablaCompraVenta;
    public static ObservableList<CompraConsulta>DataCompra;
    @FXML
    private void DescripcionVolver(MouseEvent event)
    {//descripcion del boton de salir del menu principal al control de acceso
        Texto.setText("Volver al acceso principal");
    }
    @FXML
    private void Resetear(MouseEvent event)
    {//opcion para resetear el mensaje 
        Texto.setText("Deslice sobre los botones para obtener descripcion de lo que hace cada uno");
    }
    @FXML
    private void PanelPrincipal(MouseEvent event)
    {//boton para traer el panel principal hacia adelante
        PanelPrincipal.toFront();
        Texto.setText("Deslice sobre los botones para obtener descripcion de lo que hace cada uno");
    }
    @FXML
    private void PanelConsutlas(MouseEvent event)
    {//boton para traer el panel de consultas al frente
        PanelConsultas.toFront();
        LargoTexto.setText("");
        ConsultaProblema.setText("");
        ConsultaProblema.setEditable(true);
    }
    @FXML
    private void PanelVenta(MouseEvent event)
    {//boton para traer al frente el panel de ventas y llenar las tablas de compradores y productos
        PanelVenta.toFront();
        LlenarCompradoresVenta();
        LlenarProductosVenta();
        DataCompra.clear();
        CantidadProd.setText("");
        BtnAgregarProductoVenta.setVisible(false);
    }
    @FXML
    private void PanelSolicitud(MouseEvent event)
    {//boton para traer al frente el panel de solicitud y llenar las tablas de distribuidores y productos
        PanelSolicitud.toFront();
        LLenarTablaDistribuidor();
        LLenarTablaProductos();
        ProdSelec.setText("");
        DistribuidorSelec.setText("");
    }
    private boolean ValidacionAdmin(String ID)
    {//funcion para validar si el usuario que accedio es administrador o no (se usa para validar el acceso al panel de estadisticas)
        boolean valid = false;
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_cargo " +
                            "FROM trabajador "+
                            "WHERE id_trabajador = "+ID; 
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {
                    if(consulta.getString(1).equals("2"))
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
    private void PanelBusqueda(MouseEvent event)
    {//funcion para traer al frente el panel de busqueda de los productos
        PanelBusqueda.toFront();
    }
    @FXML
    private void PanelEstadistica(MouseEvent event)
    {//Funcion para traer al frente el panel de estadistias
        if(ValidacionAdmin(FXMLDocumentController.GetIdTrabajador()))
        {//caso en que el usuario en que accedio es administrador: se setean todos los controladores del panel de estadisticas
            try
            {
                Conexion miconexion =  new Conexion();
                String Consulta="SELECT nombre_prod "+
                                "FROM producto "+
                                "ORDER BY nombre_prod ";
                ResultSet consulta=miconexion.consulta(Consulta);
                if(consulta!=null)
                {
                    while(consulta.next())
                    {
                        ComboProd1.getItems().add(consulta.getString(1));
                        ComboProd3.getItems().add(consulta.getString(1));
                        ComboProd2.getItems().add(consulta.getString(1));
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("ERROR: "+e);
            }
            PanelEstadistica.toFront();
            PaneEstProd.toFront();
            GraficoProductos.getData().clear();
            Semaforo1 = false;
            Semaforo2 = false;
            Semaforo3 = false;
            ComboProd1.getSelectionModel().select("Producto");
            ComboProd2.getSelectionModel().select("Producto");
            ComboProd3.getSelectionModel().select("Producto");
            Trabajador1.setText("");
            Trabajador2.setText("");
            Trabajador3.setText("");
            InputTrabajadorEst.setText("");
            GraficoTrabajadoresEst.getData().clear();  
        }
        else
        {//caso en que el usuario no es un administrador: se muestra mensaje de error
            try
            {
                AlertaDatoErroneoController.SetMensaje("Opcion valida solo para usuarios administradores");
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
    private void DescripConsultas(MouseEvent event)
    {//accion de descripcion del panel de consultas/problemas, para informarlos almacenarlos en la DB
        Texto.setText("Entre a esta opcion para informar al soporte informatico a cerca de fallos en el programa o similares\n");
    }
    @FXML
    private void DescripcionVenta(MouseEvent event)
    {//accion para mostrar la descripcion de lo que permite el panel de venta
        Texto.setText("Esta opción permitirá almacenar la compra realizada por un cliente determinado. Se deberá"
                + " escoger al cliente del registro que se tiene, en el caso de ser un cliente nuevo se podrá volver"
                + "a crear con los datos que demanda:\n"
                + "-Nombre completo\n"
                + "-Apellidos\n"
                + "-Fono\n");     
    }
    @FXML
    private void DescripcionSolicitud(MouseEvent event)
    {//accion para mostrar la descripcion de lo que hace el panel de solicitudes con un distribuidor
        Texto.setText("Esta opción permite observar aquellos productos que poseen el menor stock, con el fin"
                + " de mantener informado al administrador para poder realizar una determinada solicitud de"
                + " avastecimiento. Al realizarla, se deberá seleccionar al distribuidor. En el caso que no este"
                + " almacenado en la lista, se solicitará ingresar los datos pertinentes del distribuidor, como:\n"
                + "-Nombre del distribuidor\n"
                + "-Producto que distribuye\n"
                + "-RUT\n"
                + "-Nombre de persona a cargo\n"
                + "-Fono\n"
                + "-Correo electrónico\n");
    }
    @FXML
    private void DescripcionBusqueda(MouseEvent event)
    {//accion para mostrar la descripcion de lo que realiza el panel de busqueda de productos
        Texto.setText("Esta opción permite realizar una búsqueda de un determinado producto por medio de su nombre."
                + " También permite añadir nuevos productos al Stock solicitando los datos:\n"
                + "-Nombre\n"
                + "-Marca\n"
                + "-Stock\n"
                + "-Fecha Vencimiento\n"
                + "-Precio\n");
    }
    @FXML
    private void DescripcionEstadisticas(MouseEvent event)
    {//accion para describir lo que permite realizar el panel de estadisticas
        Texto.setText("Esta opción permite mostrar gráficos estadísticos con la Oferta y demanda de determinados productos"
                + " con el fin de poder ir observando cómo varía la demanda a lo largo del tiempo para que se pueda prepara"
                + " para la tempordada y poder avastecerse como corresponda.");
    }
    @FXML
    private void CerrarVentana(MouseEvent event)
    {//controlador para acceder al cuadramiento de caja y cerrar el turno de trabajo
        try
        {
            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Cuadramiento/CuadramientoCaja.fxml"));
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
    
    
    //CONTROLADORES DEL PANEL DE CONSULTAS
    @FXML
    private void ControlLargo(KeyEvent event)
    {//accion para controlar el largo de la consulta o fallo
        int cont;
        if(!ConsultaProblema.getText().equals(""))
        {//caso en que el cuadro texto es distinto de vacio
            cont = ConsultaProblema.getText().length();//obtencion del largo del string
            if(cont<199)
            {//caso en que se esta en un rango aceptado para la cadena de caracteres
                LargoTexto.setText(String.valueOf(cont)+"/200");
                ConsultaProblema.setEditable(true);
            }
            else if(cont==199)
            {//caso en que se esta en el penultimo caracter de capacidad de lo que se permite
                LargoTexto.setText(String.valueOf(cont)+"/200");
                try
                {
                    AlertaDatoErroneoController.SetMensaje("Se va a bloquear el cuadro de texto");
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
            {//caso en que se alcanzo el tope y se bloquea el cuadro texto
                LargoTexto.setText(String.valueOf(cont)+"/200");
                ConsultaProblema.setEditable(false);
                MensajeError.setText("Pulse enviar si ha terminado el mensaje, de lo contrario vuelva a acceder a la opcion desde la barra lateral");
                MensajeError.setVisible(true);
                try
                {
                    AlertaDatoErroneoController.SetMensaje("Se puede maximo 200 caracteres");
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
    private void EnviarConsulta (MouseEvent event)
    {//Funcion para enviar la consulta/problema a la DB
        if(ConsultaProblema.getText().equals(""))
        {//caso en que no se a ingresado ninguna descripcion del fallo
            try
            {
                AlertaDatoErroneoController.SetMensaje("Debe ingersar la consulta o problema presentado");
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
            //Funciones para obtener la fecha del sistema
            java.util.Date FechaActual = new java.util.Date();
            SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            String Fecha = FormatoFecha.format(FechaActual);
            EnviarCorreo(Fecha);
            EnviarConsulta(Fecha);
            try
            {//muestra de mensaje que el correo ha sido enviado correctamente
                AccesoCorrectoSimpleController.SetMensaje("Mensaje enviado al soporte");
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
            PanelPrincipal.toFront();
        }
    }
   private void EnviarCorreo(String Fecha)
    {//Funcion para enviar con email al soporte
        String[] Datos = new String[5];
        Datos[4] = ConsultaProblema.getText();
        System.out.println(Datos[4]);
        try
        {//Obtencion de los datos del usuario que envia la consulta o problematica
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT nombre_trab, fono_trab, correo_trab, id_cargo "+
                            "FROM trabajador "+
                            "WHERE id_trabajador = "+FXMLDocumentController.GetIdTrabajador();
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {//Almacenamiento de los datos
                    Datos[0]=consulta.getString(1);//nombre
                    Datos[1]=consulta.getString(2);//fono
                    Datos[2]=consulta.getString(3);//correo
                    //almacenamiento del cargo
                    if(consulta.getString(4).equals("1"))
                        Datos[3]="Vendedor";
                    else
                        Datos[3]="Administrador";
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: "+e);
        }
        //Envio del correo electronico
        try
        {
            //propiedades para la conexion de la cuenta de origen
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.auth", "true");
            
            Session session = Session.getDefaultInstance(props);
            
            String CorreoRemitente = "almacen.gs.817@gmail.com";
            String PassRemitente = "rootcontroller1631";
            String CorreoReceptor = "cristobalisr@gmail.com";
            String Asunto = "Fallo en el Software de Gestion de Almacen";
            String Mensaje ="<h4><b>Hola estimado Cristobal Saldias</b></h4> " +
                            "<p> " +
                            "Se le envia este correo para informarle que con fecha y hora: <i>"+Fecha+"</i> " +
                            "el(la) usuario(a): <i>"+Datos[0]+"</i>, con fono: <i>"+Datos[1]+"</i>, correo: <i>"+Datos[2]+"</i> " +
                            "y cargo: <i>"+Datos[3]+"</i>, ha presentado el siguiente fallo descrito por el(ella):<br> " +
                            "<br><i>"+Datos[4]+"</i></br>" +
                            "<br>Esperemos que se pueda solucionar este problema a la brevedad. </br> " +
                            "</p> " +
                            "<small> " +
                            "Enviado por: <i>almacen.gs.817@gmail.com</i> <br> " +
                            "En caso de recibir este correo por error, informar debidamente al correo mencionado anteriormente.<br> " +
                            "Correo generado automaticamente por <i>Gestor de Almacenes</i>. " +
                            "</small>";
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(CorreoRemitente));
            
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(CorreoReceptor));
            message.setSubject(Asunto);
            message.setText(Mensaje, "ISO-8859-1", "html");
            
            Transport t = session.getTransport("smtp");
            t.connect(CorreoRemitente, PassRemitente);
            t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            t.close();
            System.out.println("Correo enviado");
        }
        catch (AddressException ex) 
        {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error de envio: "+ex);
        } catch (MessagingException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error de envio: "+ex);
        }
        
    }
    private void EnviarConsulta(String Fecha)
    {
        try
        {//Paso para hacer la insercion de datos
            Conexion miconexion1=new Conexion();
            String sentencia1="INSERT INTO consultas (id_trabajador, fecha_consulta, descrip_consulta) VALUES (?,?,?) ";
            miconexion1.psPrepararSentencias=miconexion1.miconexion.prepareStatement(sentencia1);
            miconexion1.psPrepararSentencias.setString(1, FXMLDocumentController.GetIdTrabajador());//id_trabajador
            miconexion1.psPrepararSentencias.setString(2, Fecha);//fecha de la consulta
            miconexion1.psPrepararSentencias.setString(3, ConsultaProblema.getText().toUpperCase());//descripcion de la consutla
            if(miconexion1.psPrepararSentencias.executeUpdate()>0)
            {//opcion para hacer la actualizacion de la insercion en la DB
                System.out.println("Consulta/Problema almacenado correctamente");
                ConsultaProblema.setText("");//Seteo del cuadro texto a vacio
            }
        }
        catch (Exception e)
        {
            System.out.println("Error de almacenamiento de consulta/problema: "+e);
        }
    }
    
    
    //CONTROLES MENU DE COMPRA Y CREACION DE COMPRADOR NUEVO
    @FXML
    private void CantidadProdCompra(KeyEvent event)
    {//Funcion para validar la cantidad de producto y mostrar el boton de confirmacion
        Algoritmos alg = new Algoritmos();
        if(alg.ValidacionNumerica(CantidadProd.getText()))
        {//Caso en que la cantidad de producto a llevar es un numero: se muestra el boton para confirmar
            BtnAgregarProductoVenta.setVisible(true);
        }
        else if (CantidadProd.getText().equals("") || !alg.ValidacionNumerica(CantidadProd.getText()))//Caso en que la cantidad es vacio
            BtnAgregarProductoVenta.setVisible(false);//Se oculta el boton de confirmacion
        else
        {//caso en que el dato ingresado no es un numero
            CantidadProd.setText("");
            BtnAgregarProductoVenta.setVisible(false);
            try
            {
                AlertaDatoErroneoController.SetMensaje("Debe ingresar solo numeros");
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
    private void SelecProductosCompra(MouseEvent event)
    {//Funcion para seleccionar el producto
        if(ID_CLIENTE=="")
        {//Caso en que no se ha seleccionado un cliente (para que se oblique a seleccionarlo)
            try
            {
                AlertaDatoErroneoController.SetMensaje("No ha seleccionado cliente");
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
        if(TablaProductosVenta.getSelectionModel().getSelectedItem()==null)
        {//Caso en que no se ha seleccionado un producto de la tabla
            try
            {
                AlertaDatoErroneoController.SetMensaje("No ha seleccionado producto");
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
        else if(CantidadProd.getText().equals("0"))
        {//Caso en que la cantidad de producto seleccionado es igual a cero
            try
            {
                AlertaDatoErroneoController.SetMensaje("Tiene que ser un monto mayor a cero");
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
            CantidadProd.setText("");
        }
        else
        {//Caso en que la cantidad es un numero
            String[] Datos = new String[2];
            String IdProd="";
            int Stock=0;
            int aux = 0;
            try
            {//Obtencion de los datos necesarios del producto seleccionado en le tabla de productos
                TreeItem<ProductosConsulta> prod = TablaProductosVenta.getSelectionModel().getSelectedItem();//obtencion del nodo
                IdProd = prod.getValue().GetID();//obtencion de la id del producto
                Stock = prod.getValue().GetStock();//obtencion de la cantidad disponible del producto
            }
            catch(Exception e)
            {
                System.out.println("Error: "+e);
            }
            if(Integer.parseInt(CantidadProd.getText())<= Stock)
            {//Caso en que el Stock disponible satisface la cantidad solicitada
                try
                {//Obtencion del precio individual y nombre del producto en funcion de su ID
                    Conexion miconexion =  new Conexion();
                    String Consulta="SELECT precio, nombre_prod " +
                                    "FROM producto "+
                                    "WHERE id_producto="+IdProd;
                    ResultSet consulta=miconexion.consulta(Consulta);
                    if(consulta!=null)
                    {
                        while(consulta.next())
                        {//Almacenamiento de los datos
                            Datos[0] = consulta.getString(1);//precio individual
                            Datos[1] = consulta.getString(2);//nombre del producto
                        }
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Error: "+e);
                }
                int aux2 = Integer.parseInt(Datos[0]) * Integer.parseInt(CantidadProd.getText());//calculo del sub total = precio individual * cantidad comprada
                CompraConsulta Compra = new CompraConsulta (IdProd, ID_CLIENTE, CantidadProd.getText(), String.valueOf(aux2) ,Datos[1]);//creacion del objeto 'CompraConsulta'
                DataCompra.add(Compra);//se agrega el objeto a la lista de la clase 'CompraConsulta'
                final TreeItem<CompraConsulta> root =  new RecursiveTreeItem<>(DataCompra, RecursiveTreeObject::getChildren);//se agrega el nuevo elemento de la lista a la tabla de compras
                TablaCompraVenta.setRoot(root);
                TablaCompraVenta.setShowRoot(false);
                //Se va calculando el monto acumulado total de la compra
                aux = aux + Integer.parseInt(TotalPagar.getText());
                aux = aux + Compra.GetMontoTotal();
                TotalPagar.setText(String.valueOf(aux));//Se setea el Label que muestra el monto total de la compra
                BtnAgregarProductoVenta.setVisible(false);
            }
            else
            {//Caso en que el Stock no satisface la compra
                CantidadProd.setText("");
                try
                {//Mensaje de error
                    AlertaDatoErroneoController.SetMensaje("No hay Stock suficiente del producto");
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
    private void EliminarCompra(MouseEvent event)
    {//Funcion del boton que permite elminiar un producto de la tabla de compras
        if(TablaCompraVenta.getSelectionModel().getSelectedItem()==null)
        {//Caso en que no se a seleccionado un producto de la tabla de compras
            try
            {
                AlertaDatoErroneoController.SetMensaje("No ha seleccionado producto");
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
        {//Caso en que se ha seleccionado un producto
            int columna = TablaCompraVenta.getSelectionModel().getSelectedIndex()/*obtencion de la columna del producto*/, Total, Final;
            TreeItem<CompraConsulta> compra = TablaCompraVenta.getSelectionModel().getSelectedItem();//Obtencion de toda la fila de la tabla
            DataCompra.remove(columna);//Eliminacion del producto de la tabla
            //Recalculo del nuevo total, restando el sub total del producto eliminado
            Total = Integer.parseInt(TotalPagar.getText());
            Final = Total - compra.getValue().GetMontoTotal();
            TotalPagar.setText(String.valueOf(Final));
        }
    }
    
    @FXML
    private void SelecClienteCompra(MouseEvent event)
    {//Seleccion del cliente que se esta atendiendo para la compra
        if(TablaUsuariosVenta.getSelectionModel().getSelectedItem()==null)
        {//caso en que no se ha seleccionado ningun cliente
            try
            {
                AlertaDatoErroneoController.SetMensaje("No ha seleccionado cliente");
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
        {//caso en que se ha seleccionado un cliente en la tabla
            try
            {
                TreeItem<ClienteConsulta> usuario = TablaUsuariosVenta.getSelectionModel().getSelectedItem();//obtencion de los datos del cliente seleccionado
                String id =  usuario.getValue().GetID();//obtencion del id del cliente
                ID_CLIENTE=id;//Asignacion a la variable global de la id del cliente
                PagarController.SetIdCliente(ID_CLIENTE);//Seteo del id del cliente de manera externa en el controllador de pago
                try
                {//muestra del mensaje de cliente seleccionado de manera correcta
                    AccesoCorrectoSimpleController.SetMensaje("Cliente seleccionado");
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
            }
            catch(Exception e)
            {
                System.out.println("Error: "+e);
            }
        }
    }
    
    @FXML
    private void BusquedaInstantaneaProdCompra(KeyEvent event)
    {//Funcion para validar la busqueda de manera instantanea un producto en el panel de compras por el nombre
        int i=0;
        String Nombre;
        Algoritmos valid = new Algoritmos();
        if(!"".equals(BusquedaProdCompra.getText()))
        {//caso en que el input del nombre es distinto de vacio
            if(!valid.ValidacionLetras(BusquedaProdCompra.getText()))
            {//caso en que se ingreso un numero
                i++;
                BusquedaProdCompra.setText("");
            }
        }
        if(i>0)
        {//caso en que se ingreso un numero
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
        {//caso en que hay solo letras
            DataProd.clear();//limpieza de la lista en que se iran almacenando los productos a buscar
            Nombre = BusquedaProdCompra.getText().toUpperCase();//conversion a mayusculas del texto ingresado
            BusquedaProdCompraBBDD(Nombre);//llamada a la funcion que realiza la busqueda instantanea en la DB
        }
        
    }
    private void BusquedaProdCompraBBDD(String Nombre)
    {//Funcion para buscar de manera instantanea los datos del producto en el panel de ventas en funcion del nombre
        DataProd.clear();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_producto, nombre_prod, stock, precio " +
                            "FROM producto "+
                            "WHERE nombre_prod LIKE '"+Nombre+"%' "+
                            "ORDER BY nombre_prod";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {//Almacenamiento en la lista de productos de lo que retorna la consulta en la DB
                    DataProd.add(new ProductosConsulta(consulta.getString(1),consulta.getString(3) , consulta.getString(2), consulta.getString(4)));
                }
                //agregado a la tabla de productos en el panel de ventas de los productos encontrados
                final TreeItem<ProductosConsulta> root =  new RecursiveTreeItem<>(DataProd, RecursiveTreeObject::getChildren);
                TablaProductosVenta.setRoot(root);
                TablaProductosVenta.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    @FXML
    private void BusquedaInstantaneaClienteCompra(KeyEvent event)
    {//validacion de la busqueda instantanea de un cliente por su nombre
        int i=0;
        String Nombre;
        Algoritmos valid = new Algoritmos();
        if(!"".equals(BusquedaClienteCompra.getText()))
        {//caso en que hay texto ingresado
            if(!valid.ValidacionLetras(BusquedaClienteCompra.getText()))
            {//caso en que hay numero
                i++;
                BusquedaClienteCompra.setText("");
            }
        }
        if(i>0)
        {//Caso en que hay numeros: mensaje de error
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
        {//Caso en que hay solo letras
            DataClient.clear();
            Nombre = BusquedaClienteCompra.getText().toUpperCase();//Conversion a mayusculas de los caracteres ingresados
            BusquedaClienteCompraBBDD(Nombre);//llamada a la funcion que realiza la busqueda en la DB
        }
    }
    private void BusquedaClienteCompraBBDD(String Nombre)
    {//Funcion para buscar de manera instantanea a un cliente por su nombre
        DataClient.clear();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_cliente, nombre_clie, estado_clie "+
                            "FROM cliente "+
                            "WHERE nombre_clie LIKE '"+Nombre+"%' "+
                            "ORDER BY nombre_clie";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {//Agregado de los clientes a la lista de datos
                    DataClient.add(new ClienteConsulta(consulta.getString(1),consulta.getString(3) , consulta.getString(2)));
                }
                //agregado de toda la lista con datos a la tabla
                final TreeItem<ClienteConsulta> root =  new RecursiveTreeItem<>(DataClient, RecursiveTreeObject::getChildren);
                TablaUsuariosVenta.setRoot(root);
                TablaUsuariosVenta.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    
    private void LlenarCompradoresVenta()
    {//Funcion para llenar la tabla de los clientes en el panel de ventas de manera predeterminada
        DataClient.clear();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_cliente, nombre_clie, estado_clie " +
                            "FROM cliente "+
                            "ORDER BY nombre_clie"; 
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {//copia de cada cliente a la lista asociada a la tabla
                    DataClient.add(new ClienteConsulta(consulta.getString(1),consulta.getString(3) , consulta.getString(2)));
                }
                //Agregado de cada cliente a al tabla
                final TreeItem<ClienteConsulta> root =  new RecursiveTreeItem<>(DataClient, RecursiveTreeObject::getChildren);
                TablaUsuariosVenta.setRoot(root);
                TablaUsuariosVenta.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    
    private void LlenarProductosVenta()
    {//Llenado de la tabla de los productos en el panel de venta de manera predeterminada
        DataProd.clear();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_producto, nombre_prod, stock, precio " +
                            "FROM producto "+
                            "ORDER BY nombre_prod";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {//agregado a la lista de productos que esta asociada a al tabla de productos en venta
                    DataProd.add(new ProductosConsulta(consulta.getString(1),consulta.getString(4) , consulta.getString(2), consulta.getString(3)));
                }
                //seteado de la lista completa en la tabla
                final TreeItem<ProductosConsulta> root =  new RecursiveTreeItem<>(DataProd, RecursiveTreeObject::getChildren);
                TablaProductosVenta.setRoot(root);
                TablaProductosVenta.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    @FXML
    private void CrearUsuario(MouseEvent event)
    {//Boton que permite la creacion o edicion de un cliente
        if(TablaUsuariosVenta.getSelectionModel().getSelectedItem()==null)
        {//Caso en que no se a seleccionado ningun cliente: se procede a crear un cliente nuevo
            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Comprador/FormularioComprador.fxml"));
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
        } 
        else
        {//Caso en que hay un cliente seleccionado: se procede a editarlo
            TreeItem<ClienteConsulta> usuario = TablaUsuariosVenta.getSelectionModel().getSelectedItem();//Obtencion de la id
            FormularioCompradorController.SetController(true, usuario.getValue().GetID());//Controlador externo, para informar que se procedera a editar el cliente con el ID enviado
            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Comprador/FormularioComprador.fxml"));
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
        }
    }
    @FXML
    private void Pagar(MouseEvent event)
    {//Boton para proceder a pagar los productos seleccionados
        if(DataCompra.size()>0)
        {//caso en que hay como minimo 1 producto en la compra
            PagarController.SetCompras(DataCompra);//envio de la lista de productos seleccionados al controlador externo del pago
            PagarController.SetMontoTotal(TotalPagar.getText());//envio al controlador externo de pagos el 'total a pagar'
            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Comprador/Pagar.fxml"));
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
            //retorno de todos los parametros y controladores a la normalidad
            ID_CLIENTE="";
            CantidadProd.setText("");
            TotalPagar.setText("0");
            PanelPrincipal.toFront();
            Texto.setText("Deslice sobre los botones para obtener descripcion de lo que hace cada uno");
        }
        else
        {//Caso en que se desee pagar por productos no seleccionados
            try
            {
                AlertaDatoErroneoController.SetMensaje("Debe seleccionar productos para compra");
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
    
    
    //CONTROLES MENU DE SOLICITUD Y CREACION DE NUEVO DISTRIBUIDOR
    @FXML
    private void BusquedaInstantaneaProdSolicitud(KeyEvent event)
    {//Funcion para validar el ingreso de letras en la busqueda de productos para una solicitud de distribuidor
        int i=0;
        String Nombre;
        Algoritmos valid = new Algoritmos();
        if(!"".equals(BuscarProdPedido.getText()))
        {//caso en que se ha ingresado al menos 1 caracter
            if(!valid.ValidacionLetras(BuscarProdPedido.getText()))
            {//caso en que se ha ingresado un numero
                i++;
                BuscarProdPedido.setText("");
            }
        }
        if(i>0)
        {//muestra de error de ingreso de numeros
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
        {//cuando hay solo letras
            DataProd.clear();
            Nombre = BuscarProdPedido.getText().toUpperCase();//obtencion de las letras en mayuscula
            BuscarNombreSolicitudBBDD(Nombre);//llamada a la funcion de busqueda
        }
    }
    private void BuscarNombreSolicitudBBDD(String Nombre)
    {//Funcion para buscar en la DB los datos del producto por nombre
        DataProd.clear();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_producto, nombre_prod, stock " +
                            "FROM producto "+
                            "WHERE nombre_prod LIKE '"+Nombre+"%' "+
                            "ORDER BY nombre_prod";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {//agregar los productos que coinciden con la condicion a la lista de productos
                    DataProd.add(new ProductosConsulta(consulta.getString(1),"" , consulta.getString(2), consulta.getString(3)));
                }
                //Agregar la lista a la tabla de productos
                final TreeItem<ProductosConsulta> root =  new RecursiveTreeItem<>(DataProd, RecursiveTreeObject::getChildren);
                ProductosSolcitud.setRoot(root);
                ProductosSolcitud.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    
    @FXML
    private void BusquedaInstantenaDistSolicitud(KeyEvent event)
    {//Funcion para validar el ingreso de letras en la busqueda de distribuidores
        int i=0;
        String Nombre;
        Algoritmos valid = new Algoritmos();
        if(!"".equals(BuscarDistPedido.getText()))
        {//caso en que se a ingresado al menos un caracter
            if(!valid.ValidacionLetras(BuscarDistPedido.getText()))
            {//caso en que el caracter es un numero
                i++;
                BuscarDistPedido.setText("");
            }
        }
        if(i>0)
        {//muestra del mensaje de error de ingreso de numeros
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
        {//caso en que solo hay letras
            DataProv.clear();
            Nombre = BuscarDistPedido.getText().toUpperCase();//conversion de las letras a mayusculas
            BuscarDistSolicitudBBDD(Nombre);//llamada a la funcion de busqueda en la DB
        }
    }
    private void BuscarDistSolicitudBBDD(String Nombre)
    {//Funcion para buscar en la DB la informacion del distribuidor en funcion del nombre
        DataProv.clear();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_proveedor, nombre_prob, persona_contacto, fono_prob "+
                        "FROM proveedor "+
                        "WHERE nombre_prob LIKE '"+Nombre+"%' "+
                        "ORDER BY nombre_prob ";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {//almacenamiento en una lista de datos, la informacion de los proveedores
                    DataProv.add(new ProveedorConsulta(consulta.getString(1),consulta.getString(2) , consulta.getString(3), consulta.getString(4)));
                }
                //seteo de la lista en la tabla de distribuidores
                final TreeItem<ProveedorConsulta> root =  new RecursiveTreeItem<>(DataProv, RecursiveTreeObject::getChildren);
                Distribuidores.setRoot(root);
                Distribuidores.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    
    private void LLenarTablaProductos()
    {//Funcion que llena la tabla de productos de manera predeterminada del panel de solicitud de productos con distribuidor
        DataProd.clear();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_producto, nombre_prod, stock " +
                            "FROM producto "+
                            "ORDER BY nombre_prod";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {//agregado de cada producto a una lista de informacion
                    DataProd.add(new ProductosConsulta(consulta.getString(1),"" , consulta.getString(2), consulta.getString(3)));
                }
                //seteo de la lista en la tabla de productos del panel de solicitud de productos
                final TreeItem<ProductosConsulta> root =  new RecursiveTreeItem<>(DataProd, RecursiveTreeObject::getChildren);
                ProductosSolcitud.setRoot(root);
                ProductosSolcitud.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    private void LLenarTablaDistribuidor()
    {//Funcion para llenar la tabla de productos 
        DataProv.clear();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_proveedor, nombre_prob, persona_contacto, fono_prob " +
                        "FROM proveedor " +
                        "ORDER BY nombre_prob ";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {//almacenamiento de los datos del proveedor en la lista de datos del proveedor
                    DataProv.add(new ProveedorConsulta(consulta.getString(1),consulta.getString(2) , consulta.getString(3), consulta.getString(4)));
                }
                //seteo de la lista en la tabla de proveedores
                final TreeItem<ProveedorConsulta> root =  new RecursiveTreeItem<>(DataProv, RecursiveTreeObject::getChildren);
                Distribuidores.setRoot(root);
                Distribuidores.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    @FXML
    private void SelecDist(MouseEvent event)
    {//Boton para seleccionar al distribuidor con el que se hara el pediro
        if(Distribuidores.getSelectionModel().getSelectedItem()==null)
        {//caso en que no se a seleccionado ningun distribuidor
            try
            {
                AlertaDatoErroneoController.SetMensaje("No ha seleccionado a un distribuidor");
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
        {//caso en que se ha seleccionado al distribuidor
            try
            {
                TreeItem<ProveedorConsulta> dist = Distribuidores.getSelectionModel().getSelectedItem();//obtencion del nodo de la tabla
                DistribuidorSelec.setText(dist.getValue().GetNombre());//seteo del nombre del distribuidor en el TxtField no editable
                SolicitudController.SetIdProveedor(dist.getValue().GetID());//seteo del id del distribuidor en un controlador externo: el que concreta la solicitud
            }
            catch(Exception e)
            {
                System.out.println("Error: "+e);
            }
        } 
    }
    @FXML
    private void SelecProd(MouseEvent event)
    {//Boton para seleccionar un producto de la tabla de productos para la solicitud
        if(ProductosSolcitud.getSelectionModel().getSelectedItem()==null)
        {//caso en que no se ha seleccionado ningun producto
            try
            {
                AlertaDatoErroneoController.SetMensaje("No ha seleccionado un producto");
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
        {//caso en que hay un producto seleccionado en la tabla
            try
            {
                TreeItem<ProductosConsulta> prod = ProductosSolcitud.getSelectionModel().getSelectedItem();//obtencion del nodo de la tabla
                ProdSelec.setText(prod.getValue().GetNombre());//seteo en el TxtField del nombre del producto
                SolicitudController.SetIdProducto(prod.getValue().GetID());//seteo en un controlador externo del id del producto: en el que finaliza la solicitud
            }
            catch(Exception e)
            {
                System.out.println("Error: "+e);
            }
        }  
    }
    @FXML
    private void CrearDistribuidor(MouseEvent event)
    {//Funcion asociada al boton para: crear o editar un distribuidor
        if(Distribuidores.getSelectionModel().getSelectedItem()==null)
        {//caso en que no se a seleccionado un distribuidor: crea un nuevo distribuidor
            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Distribuidor/FormularioDistribuidor.fxml"));
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
        }
        else
        {//caso en que hay un distribuidor seleccionado
            String ID="";//variable en que se almacenara el ID del proveedor seleccionado
            try
            {
                TreeItem<ProveedorConsulta> dist = Distribuidores.getSelectionModel().getSelectedItem();//obtencion del nodo de la tabla
                ID = dist.getValue().GetID();//obtencion del ID del distribuidor seleccionado
            }
            catch(Exception e)
            {
                System.out.println("Error: "+e);
            }
            FormularioDistribuidorController.SetValues(true, ID);//seteo externo del formulario del distribuidor, para que este preparado a modificar un distribuidor de acuerdo al ID enviado
            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Distribuidor/FormularioDistribuidor.fxml"));
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
        }
        PanelPrincipal.toFront();
    }
    @FXML
    private void HistorialSolicitudes(MouseEvent event)
    {//Funcion para el boton que llama a un panel externo con el historial de solicitudes de productos con sus respectivos distribuidores
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Distribuidor/HistorialSolicitudes.fxml"));
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
    @FXML
    private void RealizarSolicitud(MouseEvent event)
    {//Boton para llamar al panel que finaliza la solicitud de un producto
        if(ProdSelec.getText().equals(""))
        {//caso en que no se ha seleccionado un distribuidor
            try
            {
                AlertaDatoErroneoController.SetMensaje("No ha seleccionado un producto");
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
        else if(DistribuidorSelec.getText().equals(""))
        {//caso en que no se ha seleccionado un distribuidor
            try
            {
                AlertaDatoErroneoController.SetMensaje("No ha seleccionado un Distribuidor");
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
        else if(DistribuidorSelec.getText().equals("") && ProdSelec.getText().equals(""))
        {//caso en que no se ha seleecionado ni un distribuidor ni un producto
            try
            {
                AlertaDatoErroneoController.SetMensaje("No ha seleccionado un producto y distribuidor");
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
        {//caso en que si se seleccionaron ambos parametros
            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Distribuidor/Solicitud.fxml"));
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
            //seteo de todos los valores a la normalidad y retorno al panel principal
            ProdSelec.setText("");
            DistribuidorSelec.setText("");
            PanelPrincipal.toFront();
            Texto.setText("Deslice sobre los botones para obtener descripcion de lo que hace cada uno");
        }
    }
    
    
    //CONTROLADORES DEL MENU DE BUSQUEDA
    @FXML
    private void DescripcionBusqueda1(MouseEvent event)
    {//Muestra de la descripcion asociada a la busqueda por el nombre del producto
        DescripOpcBusqueda.setText("Busqueda en función del nombre del producto, "
                + "figurara el listado de productos en orden alfabetico.");
    }
    @FXML
    private void DescripcionBusqueda2(MouseEvent event)
    {//Muestra de la descripcion asociada a la busqueda por el tipo de producto
        DescripOpcBusqueda.setText("Busqueda en función del tipo de producto que posee el "
                + "almacén. Serán ordenados en relación al tipo de producto.");
    }
    @FXML
    private void DescripModificacionProducto(MouseEvent event)
    {//Muestra de la descripcion del boton de modificacion de producto
        DescripOpcBusqueda.setText("Permite seleccionar un producto de la tabla para poder editar los datos asociados a este.");
    }
    @FXML
    private void DescripBusquedaCompras(MouseEvent event)
    {//Muestra de la descripcion del boton del historial de compras
        DescripOpcBusqueda.setText("Permitirá visualizar el historial de compras realizado en función de la fecha de compra.");
    }
    @FXML
    private void DescripAgregarProd(MouseEvent event)
    {//Muestra de la descripcion del boton de agregar un producto
        DescripOpcBusqueda.setText("Permitirá añadir un nuevo producto con su respectivo proveedor.");
    }
    @FXML
    private void ResetearDescripBusqueda(MouseEvent event)
    {//Seteo de la descripcion 
        DescripOpcBusqueda.setText("Deslice por alguna opcion para obtener una breve descripcion");
    }
    private String Tipo(String Nombre)
    {//funcion que retorna la ID del tipo de un producto
        String ID="";
        try
        {
            Conexion miconexion = new Conexion();
            String Consulta = "SELECT id_tipo "
                            + "FROM tipo "
                            + "WHERE descrip_tipo='"+Nombre+"'";
            ResultSet consulta = miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {
                    ID=consulta.getString(1);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: "+e);
        }
        return ID;
    }
    private void BuscarNombreBBDD(String Nombre)
    {//Funcion para buscar de manera instantanea los datos de un producto en funcion de su nombre
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_producto, precio, nombre_prod, stock " +
                            "FROM producto "+
                            "WHERE nombre_prod LIKE '"+Nombre+"%' "+
                            "ORDER BY precio";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {//almacenamiento de los datos del producto en una lista 
                    DataProd.add(new ProductosConsulta(consulta.getString(1), consulta.getString(2), consulta.getString(3), consulta.getString(4)));
                }
                //seteo de toda la lista en la tabla de busqueda de productos
                final TreeItem<ProductosConsulta> root =  new RecursiveTreeItem<>(DataProd, RecursiveTreeObject::getChildren);
                TablaRespuestaBusqueda.setRoot(root);
                TablaRespuestaBusqueda.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    private void BuscarTipoBBDD(String Tipo)
    {//Funcion para buscar de manera instantanea productos por su categoria
       try
       {
           Conexion miconexion =  new Conexion();
           String Consulta="SELECT id_producto, precio, nombre_prod, stock " +
                           "FROM producto "+
                           "WHERE  id_tipo = "+Tipo+" "+
                           "ORDER BY precio";
           ResultSet consulta=miconexion.consulta(Consulta);
           if(consulta!=null)
           {
               while(consulta.next())
               {//seteo de la lista de productos
                   DataProd.add(new ProductosConsulta(consulta.getString(1), consulta.getString(2), consulta.getString(3), consulta.getString(4)));
               }
               //seteo de la lista en la tabla con los productos que cumple la categoria
               final TreeItem<ProductosConsulta> root =  new RecursiveTreeItem<>(DataProd, RecursiveTreeObject::getChildren);
               TablaRespuestaBusqueda.setRoot(root);
               TablaRespuestaBusqueda.setShowRoot(false);
           }
       }
       catch(Exception e)
       {
           System.out.println("ERROR: "+e);
       }
    }
    //Opciones de descripciones
    @FXML
    private void EditarProd(MouseEvent event)
    {//Funcion que permite editar un producto de la tabla
        if(TablaRespuestaBusqueda.getSelectionModel().getSelectedItem()==null)
        {//caso en que no se ha seleccionado un producto
            try
            {//alerta de error
                AlertaDatoErroneoController.SetMensaje("No ha seleccionado un producto");
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
        {//caso en que se ha seleccionado el producto
            try
            {
                TreeItem<ProductosConsulta> prod = TablaRespuestaBusqueda.getSelectionModel().getSelectedItem();//obtencion del nodo de la tabla
                NuevoProductoController.SetID(prod.getValue().GetID());//seteo del ID del producto en un controlador externo
                try
                {

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Producto/NuevoProducto.fxml"));
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
            catch(Exception e)
            {
                System.out.println("Error: "+e);
            }
        }  
    }
    @FXML
    private void BusquedaInstantaneaNombre(KeyEvent event)
    {//Validacion de letras de la busqueda instantanea por nombre de los producto
        BusquedaTipoProd.setText("");//busqueda excluyente
        int i=0;
        String Nombre;
        Algoritmos valid = new Algoritmos();
        if(!"".equals(BusquedaNombreProd.getText()))
        {//caso en que hay al menos 1 caracter ingresado
            if(!valid.ValidacionLetras(BusquedaNombreProd.getText()))
            {//caso en que hay un numero
                i++;
                BusquedaNombreProd.setText("");
            }
        }
        if(i>0)
        {//caso en que hay numeros ingresados
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
        {//caso en que hay solo letras
            DataProd.clear();
            Nombre = BusquedaNombreProd.getText().toUpperCase();//conversion a letras mayusculas del nombre ingresado
            BuscarNombreBBDD(Nombre);//invocacion a la funcion de busqueda en la DB
        }
    }
    @FXML
    private void BusquedaInstantaneaTipo(KeyEvent event)
    {//Funcion para validar el ingreso de letras en el tipo de dato
        String Cat;
        BusquedaNombreProd.setText("");//busqueda excluyente
        int i=0;
        Algoritmos valid = new Algoritmos();
        if(!"".equals(BusquedaTipoProd.getText()))
        {//caso en que hay al menos 1 caracter
            if(!valid.ValidacionLetras(BusquedaTipoProd.getText()))
            {//caso en que hay 1 numero
                i++;
                BusquedaTipoProd.setText("");
            }
        }
        if(i>0)
        {//caso en que hay numeros presentes
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
        {//caso en que hay solo letras
            DataProd.clear();
            Cat = BusquedaTipoProd.getText().toUpperCase();//obtencion del nombre de la categoria en mayusculas
            String ID = Tipo(Cat);//invocacion a la funcion que retorna el ID del tipo asociado
            BuscarTipoBBDD(ID);//invocacion a la funcion que obtiene la informacion de los productos en funcion de su categoria
                
        }
    }
    @FXML
    private void AgregarProd(MouseEvent event)
    {//Funcion para agregar un producto nuevo
        try
        {
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Producto/NuevoProducto.fxml"));
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
    @FXML
    private void BuscarCompra(MouseEvent event)
    {//Funcion para buscar una determinada compra
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Producto/ComprasRealizadas.fxml"));
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
    
    
    
    /*FUNCIONES DEL MENU DE ESTADISTICAS*/
    @FXML
    private void PanelHoras(MouseEvent event)
    {
        PabelHoras.toFront();
        DataTurnosPersona.clear();
        DataTurnosHora.clear();
    }
    @FXML
    private void PanelEstProductos(MouseEvent event)
    {//Funcion para setear todos los parametros del sub panel de estadistica de productos
        PaneEstProd.toFront();
        GraficoProductos.getData().clear();
        Semaforo1 = false;
        Semaforo2 = false;
        Semaforo3 = false;
        ComboProd1.getSelectionModel().select("Producto");
        ComboProd2.getSelectionModel().select("Producto");
        ComboProd3.getSelectionModel().select("Producto");
    }
    @FXML
    private void PanelEstTrabajadores(MouseEvent event)
    {//Funcion para setear todo el sub panel de estadistica de trabajadores
        PaneEstTrab.toFront();
        Trabajador1.setText("");
        Trabajador2.setText("");
        Trabajador3.setText("");
        InputTrabajadorEst.setText("");
        GraficoTrabajadoresEst.getData().clear();
    }
    @FXML
    private void Producto1(MouseEvent event)
    {//Funcion para obtener los datos del primer producto seleccionado
        if(ComboProd1.getValue() == null || ComboProd1.getValue().toString().equals("Producto"))
        {//Caso en que no se a seleccionado ningun producto
            try
            {
                AlertaDatoErroneoController.SetMensaje("Seleccione un producto de la primera lista");
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
        else if(!Semaforo1)
        {//Caso en que el primer txtfield esta vacio
            Semaforo1 = true;
            String aux = ComboProd1.getValue().toString().toUpperCase(), Id_prod="";
            int mes = 1, conta;
            try
            {//Consulta para obtener el ID del producto seleccionado
               Conexion miconexion =  new Conexion();
               String Consulta="SELECT id_producto "+
                               "FROM producto "+
                               "WHERE nombre_prod = '"+aux+"' ";
                ResultSet consulta=miconexion.consulta(Consulta);
                if(consulta!=null)
                {
                    while(consulta.next())
                    {
                        Id_prod = consulta.getString(1);
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("ERROR: "+e);
            }
            XYChart.Series pares = new XYChart.Series();//creacion del grafico de puntos
            pares.setName(aux);//asignacion de la etiqueda: nombre del producto
            try
            {//consulta para obtener la suma de ventas del producto por mes
               Conexion miconexion =  new Conexion();
               String Consulta="SELECT DISTINCT " +
                                "MONTH(compra.fecha_compra) AS MES, " +
                                "SUM(compra.cantidad) AS TOTAL_POR_MES " +
                                "FROM " +
                                "compra, producto, atiende " +
                                "WHERE "+
                                "producto.id_producto = "+Id_prod+" AND " +
                                "compra.id_producto = producto.id_producto AND " +
                                "compra.fecha_compra = atiende.fecha_atiende " +
                                "GROUP BY MES";
                ResultSet consulta=miconexion.consulta(Consulta);
                if(consulta!=null)
                {
                    while(consulta.next())
                    {
                        conta = Integer.parseInt(consulta.getString(1));//Almacenamiento de la cantidad de venta mensual
                        if(conta > mes)
                        {//caso en que el mes de primeras ventas es después de enero
                            while(conta > mes)
                            {//se deja en cero las ventas de los meses que la DB no arroja respuestas
                                if(mes == 1)
                                    pares.getData().add(new XYChart.Data("Enero", 0));
                                else if(mes == 2)
                                    pares.getData().add(new XYChart.Data("Febrero", 0));
                                else if(mes == 3)
                                    pares.getData().add(new XYChart.Data("Marzo", 0));
                                else if(mes == 4)
                                    pares.getData().add(new XYChart.Data("Abril", 0));
                                else if(mes == 5)
                                    pares.getData().add(new XYChart.Data("Mayo", 0));
                                else if(mes == 6)
                                    pares.getData().add(new XYChart.Data("Junio", 0));
                                else if(mes == 7)
                                    pares.getData().add(new XYChart.Data("Julio", 0));
                                else if(mes == 8)
                                    pares.getData().add(new XYChart.Data("Agosto", 0));
                                else if(mes == 9)
                                    pares.getData().add(new XYChart.Data("Septiembre", 0));
                                else if(mes == 10)
                                    pares.getData().add(new XYChart.Data("Octubre", 0));
                                else if(mes == 11)
                                    pares.getData().add(new XYChart.Data("Noviembre", 0));
                                else 
                                    pares.getData().add(new XYChart.Data("Diciembre", 0));
                                mes++;
                            }
                            //cada uno de los casos de los meses de venta
                            if(mes == 1)
                                pares.getData().add(new XYChart.Data("Enero", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 2)
                                pares.getData().add(new XYChart.Data("Febrero", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 3)
                                pares.getData().add(new XYChart.Data("Marzo", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 4)
                                pares.getData().add(new XYChart.Data("Abril", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 5)
                                pares.getData().add(new XYChart.Data("Mayo", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 6)
                                pares.getData().add(new XYChart.Data("Junio", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 7)
                                pares.getData().add(new XYChart.Data("Julio", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 8)
                                pares.getData().add(new XYChart.Data("Agosto", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 9)
                                pares.getData().add(new XYChart.Data("Septiembre", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 10)
                                pares.getData().add(new XYChart.Data("Octubre", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 11)
                                pares.getData().add(new XYChart.Data("Noviembre", Integer.parseInt(consulta.getString(2))));
                            else 
                                pares.getData().add(new XYChart.Data("Diciembre", Integer.parseInt(consulta.getString(2))));
                            mes++;
                        }
                        else
                        {//caso en que los meses de venta estan consecutivos
                            if(conta == 1)
                                pares.getData().add(new XYChart.Data("Enero", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 2)
                                pares.getData().add(new XYChart.Data("Febrero", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 3)
                                pares.getData().add(new XYChart.Data("Marzo", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 4)
                                pares.getData().add(new XYChart.Data("Abril", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 5)
                                pares.getData().add(new XYChart.Data("Mayo", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 6)
                                pares.getData().add(new XYChart.Data("Junio", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 7)
                                pares.getData().add(new XYChart.Data("Julio", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 8)
                                pares.getData().add(new XYChart.Data("Agosto", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 9)
                                pares.getData().add(new XYChart.Data("Septiembre", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 10)
                                pares.getData().add(new XYChart.Data("Octubre", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 11)
                                pares.getData().add(new XYChart.Data("Noviembre", Integer.parseInt(consulta.getString(2))));
                            else 
                                pares.getData().add(new XYChart.Data("Diciembre", Integer.parseInt(consulta.getString(2))));
                            mes++;
                        }
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("ERROR: "+e);
            }
            if(mes <= 12)
            {//caso en que la venta no alcanzo a ser hasta el mes de diciembre
                while(mes <= 12)
                {//se setean en cero cada uno de los meses que no se alcanzaron
                    if(mes == 1)
                        pares.getData().add(new XYChart.Data("Enero", 0));
                    else if(mes == 2)
                        pares.getData().add(new XYChart.Data("Febrero", 0));
                    else if(mes == 3)
                        pares.getData().add(new XYChart.Data("Marzo", 0));
                    else if(mes == 4)
                        pares.getData().add(new XYChart.Data("Abril", 0));
                    else if(mes == 5)
                        pares.getData().add(new XYChart.Data("Mayo", 0));
                    else if(mes == 6)
                        pares.getData().add(new XYChart.Data("Junio", 0));
                    else if(mes == 7)
                        pares.getData().add(new XYChart.Data("Julio", 0));
                    else if(mes == 8)
                        pares.getData().add(new XYChart.Data("Agosto", 0));
                    else if(mes == 9)
                        pares.getData().add(new XYChart.Data("Septiembre", 0));
                    else if(mes == 10)
                        pares.getData().add(new XYChart.Data("Octubre", 0));
                    else if(mes == 11)
                        pares.getData().add(new XYChart.Data("Noviembre", 0));
                    else 
                        pares.getData().add(new XYChart.Data("Diciembre", 0));
                    mes++;
                }
            }
            //graficado de los puntos obtenidos
            GraficoProductos.getData().addAll(pares);
        }
        else
        {//caso en que se desea graficar un elemento sin antes borrar todo el grafico
            try
            {
                AlertaDatoErroneoController.SetMensaje("Debe limpiar el grafico");
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
    private void Producto2(MouseEvent event)
    {//Funcion para setear en el segundo txt field 
        if(ComboProd2.getValue() == null || ComboProd2.getValue().toString().equals("Producto"))
        {//caso en que no se ha seleccionado ningun producto del 2° combo box
            try
            {
                AlertaDatoErroneoController.SetMensaje("Seleccione un producto de la segunda lista");
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
        else if(!Semaforo2)
        {//caso en que el segundo txt field esta vacio
            Semaforo2 = true;
            String aux = ComboProd2.getValue().toString().toUpperCase(), Id_prod="";
            int mes = 1, conta;
            try
            {//consulta para obtener el id del producto seleccionado
               Conexion miconexion =  new Conexion();
               String Consulta="SELECT id_producto "+
                               "FROM producto "+
                               "WHERE nombre_prod = '"+aux+"' ";
                ResultSet consulta=miconexion.consulta(Consulta);
                if(consulta!=null)
                {
                    while(consulta.next())
                    {
                        Id_prod = consulta.getString(1);
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("ERROR: "+e);
            }
            XYChart.Series coordenadas = new XYChart.Series();//creacion del grafico asociado al producto
            coordenadas.setName(aux);//asignacion de una etiqueta al grafico
            try
            {//consulta para obtener las ventas mensuales
               Conexion miconexion =  new Conexion();
               String Consulta="SELECT DISTINCT " +
                                "MONTH(compra.fecha_compra) AS MES, " +
                                "SUM(compra.cantidad) AS TOTAL_POR_MES " +
                                "FROM " +
                                "compra, producto, atiende " +
                                "WHERE "+
                                "producto.id_producto = "+Id_prod+" AND " +
                                "compra.id_producto = producto.id_producto AND " +
                                "compra.fecha_compra = atiende.fecha_atiende " +
                                "GROUP BY MES";
                ResultSet consulta=miconexion.consulta(Consulta);
                if(consulta!=null)
                {
                    while(consulta.next())
                    {
                        conta = Integer.parseInt(consulta.getString(1));
                        if(conta > mes)
                        {//caso en que la cantidad no esta a contar del primer mes
                            while(conta > mes)
                            {//seteo en cero de cada uno de los meses anteriores a la primera venta
                                if(mes == 1)
                                    coordenadas.getData().add(new XYChart.Data("Enero", 0));
                                else if(mes == 2)
                                    coordenadas.getData().add(new XYChart.Data("Febrero", 0));
                                else if(mes == 3)
                                    coordenadas.getData().add(new XYChart.Data("Marzo", 0));
                                else if(mes == 4)
                                    coordenadas.getData().add(new XYChart.Data("Abril", 0));
                                else if(mes == 5)
                                    coordenadas.getData().add(new XYChart.Data("Mayo", 0));
                                else if(mes == 6)
                                    coordenadas.getData().add(new XYChart.Data("Junio", 0));
                                else if(mes == 7)
                                    coordenadas.getData().add(new XYChart.Data("Julio", 0));
                                else if(mes == 8)
                                    coordenadas.getData().add(new XYChart.Data("Agosto", 0));
                                else if(mes == 9)
                                    coordenadas.getData().add(new XYChart.Data("Septiembre", 0));
                                else if(mes == 10)
                                    coordenadas.getData().add(new XYChart.Data("Octubre", 0));
                                else if(mes == 11)
                                    coordenadas.getData().add(new XYChart.Data("Noviembre", 0));
                                else 
                                    coordenadas.getData().add(new XYChart.Data("Diciembre", 0));
                                mes++;
                            }
                            //caso en que se alcanzo la venta y se asigna al mes que corresponde
                            if(mes == 1)
                                coordenadas.getData().add(new XYChart.Data("Enero", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 2)
                                coordenadas.getData().add(new XYChart.Data("Febrero", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 3)
                                coordenadas.getData().add(new XYChart.Data("Marzo", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 4)
                                coordenadas.getData().add(new XYChart.Data("Abril", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 5)
                                coordenadas.getData().add(new XYChart.Data("Mayo", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 6)
                                coordenadas.getData().add(new XYChart.Data("Junio", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 7)
                                coordenadas.getData().add(new XYChart.Data("Julio", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 8)
                                coordenadas.getData().add(new XYChart.Data("Agosto", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 9)
                                coordenadas.getData().add(new XYChart.Data("Septiembre", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 10)
                                coordenadas.getData().add(new XYChart.Data("Octubre", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 11)
                                coordenadas.getData().add(new XYChart.Data("Noviembre", Integer.parseInt(consulta.getString(2))));
                            else 
                                coordenadas.getData().add(new XYChart.Data("Diciembre", Integer.parseInt(consulta.getString(2))));
                            mes++;
                        }
                        else
                        {//caso en que el mes corresponde al primero del año
                            if(conta == 1)
                                coordenadas.getData().add(new XYChart.Data("Enero", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 2)
                                coordenadas.getData().add(new XYChart.Data("Febrero", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 3)
                                coordenadas.getData().add(new XYChart.Data("Marzo", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 4)
                                coordenadas.getData().add(new XYChart.Data("Abril", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 5)
                                coordenadas.getData().add(new XYChart.Data("Mayo", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 6)
                                coordenadas.getData().add(new XYChart.Data("Junio", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 7)
                                coordenadas.getData().add(new XYChart.Data("Julio", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 8)
                                coordenadas.getData().add(new XYChart.Data("Agosto", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 9)
                                coordenadas.getData().add(new XYChart.Data("Septiembre", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 10)
                                coordenadas.getData().add(new XYChart.Data("Octubre", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 11)
                                coordenadas.getData().add(new XYChart.Data("Noviembre", Integer.parseInt(consulta.getString(2))));
                            else 
                                coordenadas.getData().add(new XYChart.Data("Diciembre", Integer.parseInt(consulta.getString(2))));
                            mes++;
                        }
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("ERROR: "+e);
            }
            if(mes <= 12)
            {//caso en que faltaron meses para terminar el año
                while(mes <= 12)
                {//seteo en cero de cada uno de esos meses
                    if(mes == 1)
                        coordenadas.getData().add(new XYChart.Data("Enero", 0));
                    else if(mes == 2)
                        coordenadas.getData().add(new XYChart.Data("Febrero", 0));
                    else if(mes == 3)
                        coordenadas.getData().add(new XYChart.Data("Marzo", 0));
                    else if(mes == 4)
                        coordenadas.getData().add(new XYChart.Data("Abril", 0));
                    else if(mes == 5)
                        coordenadas.getData().add(new XYChart.Data("Mayo", 0));
                    else if(mes == 6)
                        coordenadas.getData().add(new XYChart.Data("Junio", 0));
                    else if(mes == 7)
                        coordenadas.getData().add(new XYChart.Data("Julio", 0));
                    else if(mes == 8)
                        coordenadas.getData().add(new XYChart.Data("Agosto", 0));
                    else if(mes == 9)
                        coordenadas.getData().add(new XYChart.Data("Septiembre", 0));
                    else if(mes == 10)
                        coordenadas.getData().add(new XYChart.Data("Octubre", 0));
                    else if(mes == 11)
                        coordenadas.getData().add(new XYChart.Data("Noviembre", 0));
                    else 
                        coordenadas.getData().add(new XYChart.Data("Diciembre", 0));
                    mes++;
                }
            }
            //seteo de la lista de datos del grafico
            GraficoProductos.getData().addAll(coordenadas);
        }
        else
        {//caso en que se desee graficar algo antes de limpiar el grafico
            try
            {
                AlertaDatoErroneoController.SetMensaje("Debe limpiar el grafico");
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
    private void Producto3(MouseEvent event)
    {//Funcion para graficar el producto que se encuentra en la tercera lista de productos
        if(ComboProd3.getValue() == null || ComboProd3.getValue().toString().equals("Producto"))
        {//caso en que no se a seleccionado ningun producto del 3° combo de listas
            try
            {
                AlertaDatoErroneoController.SetMensaje("Seleccione un producto de la tercera lista");
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
        else if(!Semaforo3)
        {//caso en que se ha seleccionado
            Semaforo3 = true;
            String aux = ComboProd3.getValue().toString().toUpperCase(), Id_prod="";
            int mes = 1, conta;
            try
            {//busqueda de la ID del producto por su nombre
               Conexion miconexion =  new Conexion();
               String Consulta="SELECT id_producto "+
                               "FROM producto "+
                               "WHERE nombre_prod = '"+aux+"' ";
                ResultSet consulta=miconexion.consulta(Consulta);
                if(consulta!=null)
                {
                    while(consulta.next())
                    {
                        Id_prod = consulta.getString(1);
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("ERROR: "+e);
            }
            //creacion del grafico a realizar
            XYChart.Series serie = new XYChart.Series();
            serie.setName(aux);//asignacion de una etiqueta a los datos entrantes
            try
            {//consulta para obtener la cantidad de dinero de venta mensual del producto
               Conexion miconexion =  new Conexion();
               String Consulta="SELECT DISTINCT " +
                                "MONTH(compra.fecha_compra) AS MES, " +
                                "SUM(compra.cantidad) AS TOTAL_POR_MES " +
                                "FROM " +
                                "compra, producto, atiende " +
                                "WHERE "+
                                "producto.id_producto = "+Id_prod+" AND " +
                                "compra.id_producto = producto.id_producto AND " +
                                "compra.fecha_compra = atiende.fecha_atiende " +
                                "GROUP BY MES";
                ResultSet consulta=miconexion.consulta(Consulta);
                if(consulta!=null)
                {
                    while(consulta.next())
                    {
                        conta = Integer.parseInt(consulta.getString(1));
                        if(conta > mes)
                        {//caso en que el primer mes de venta no corresponde al primero del año
                            while(conta > mes)
                            {//cobertura y asignacion en cero de cada uno de esos meses
                                if(mes == 1)
                                    serie.getData().add(new XYChart.Data("Enero", 0));
                                else if(mes == 2)
                                    serie.getData().add(new XYChart.Data("Febrero", 0));
                                else if(mes == 3)
                                    serie.getData().add(new XYChart.Data("Marzo", 0));
                                else if(mes == 4)
                                    serie.getData().add(new XYChart.Data("Abril", 0));
                                else if(mes == 5)
                                    serie.getData().add(new XYChart.Data("Mayo", 0));
                                else if(mes == 6)
                                    serie.getData().add(new XYChart.Data("Junio", 0));
                                else if(mes == 7)
                                    serie.getData().add(new XYChart.Data("Julio", 0));
                                else if(mes == 8)
                                    serie.getData().add(new XYChart.Data("Agosto", 0));
                                else if(mes == 9)
                                    serie.getData().add(new XYChart.Data("Septiembre", 0));
                                else if(mes == 10)
                                    serie.getData().add(new XYChart.Data("Octubre", 0));
                                else if(mes == 11)
                                    serie.getData().add(new XYChart.Data("Noviembre", 0));
                                else 
                                    serie.getData().add(new XYChart.Data("Diciembre", 0));
                                mes++;
                            }
                            //caso en que se alcanzo el mes en que se realizo la primera venta
                            if(mes == 1)
                                serie.getData().add(new XYChart.Data("Enero", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 2)
                                serie.getData().add(new XYChart.Data("Febrero", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 3)
                                serie.getData().add(new XYChart.Data("Marzo", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 4)
                                serie.getData().add(new XYChart.Data("Abril", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 5)
                                serie.getData().add(new XYChart.Data("Mayo", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 6)
                                serie.getData().add(new XYChart.Data("Junio", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 7)
                                serie.getData().add(new XYChart.Data("Julio", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 8)
                                serie.getData().add(new XYChart.Data("Agosto", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 9)
                                serie.getData().add(new XYChart.Data("Septiembre", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 10)
                                serie.getData().add(new XYChart.Data("Octubre", Integer.parseInt(consulta.getString(2))));
                            else if(mes == 11)
                                serie.getData().add(new XYChart.Data("Noviembre", Integer.parseInt(consulta.getString(2))));
                            else 
                                serie.getData().add(new XYChart.Data("Diciembre", Integer.parseInt(consulta.getString(2))));
                            mes++;
                        }
                        else
                        {//caso en que en el primer mes del año se encuentra la primera venta
                            if(conta == 1)
                                serie.getData().add(new XYChart.Data("Enero", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 2)
                                serie.getData().add(new XYChart.Data("Febrero", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 3)
                                serie.getData().add(new XYChart.Data("Marzo", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 4)
                                serie.getData().add(new XYChart.Data("Abril", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 5)
                                serie.getData().add(new XYChart.Data("Mayo", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 6)
                                serie.getData().add(new XYChart.Data("Junio", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 7)
                                serie.getData().add(new XYChart.Data("Julio", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 8)
                                serie.getData().add(new XYChart.Data("Agosto", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 9)
                                serie.getData().add(new XYChart.Data("Septiembre", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 10)
                                serie.getData().add(new XYChart.Data("Octubre", Integer.parseInt(consulta.getString(2))));
                            else if(conta == 11)
                                serie.getData().add(new XYChart.Data("Noviembre", Integer.parseInt(consulta.getString(2))));
                            else 
                                serie.getData().add(new XYChart.Data("Diciembre", Integer.parseInt(consulta.getString(2))));
                            mes++;
                        }
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("ERROR: "+e);
            }
            if(mes <= 12)
            {//caso en que faltaron meses por cubrir
                while(mes <= 12)
                {//asignacion de cero en cada uno de esos meses
                    if(mes == 1)
                        serie.getData().add(new XYChart.Data("Enero", 0));
                    else if(mes == 2)
                        serie.getData().add(new XYChart.Data("Febrero", 0));
                    else if(mes == 3)
                        serie.getData().add(new XYChart.Data("Marzo", 0));
                    else if(mes == 4)
                        serie.getData().add(new XYChart.Data("Abril", 0));
                    else if(mes == 5)
                        serie.getData().add(new XYChart.Data("Mayo", 0));
                    else if(mes == 6)
                        serie.getData().add(new XYChart.Data("Junio", 0));
                    else if(mes == 7)
                        serie.getData().add(new XYChart.Data("Julio", 0));
                    else if(mes == 8)
                        serie.getData().add(new XYChart.Data("Agosto", 0));
                    else if(mes == 9)
                        serie.getData().add(new XYChart.Data("Septiembre", 0));
                    else if(mes == 10)
                        serie.getData().add(new XYChart.Data("Octubre", 0));
                    else if(mes == 11)
                        serie.getData().add(new XYChart.Data("Noviembre", 0));
                    else 
                        serie.getData().add(new XYChart.Data("Diciembre", 0));
                    mes++;
                }
            }
            //seteo de los datos en el grafico
            GraficoProductos.getData().addAll(serie);
        }
        else
        {//caso en que se desee graficar sin antes haber limpiado el grafico
            try
            {
                AlertaDatoErroneoController.SetMensaje("Debe limpiar el grafico");
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
    private void Limpiar(MouseEvent event)
    {//Funcion para limiar el grafico y setear todos los controladores del grafico de productos
        GraficoProductos.getData().clear();
        Semaforo1 = false;
        Semaforo2 = false;
        Semaforo3 = false;
        ComboProd1.getSelectionModel().select("Producto");
        ComboProd2.getSelectionModel().select("Producto");
        ComboProd3.getSelectionModel().select("Producto");
    }
    @FXML
    private void Clear(MouseEvent event)
    {//Funcion para limpiar y grafico y setear todos los parametros del grafico de trabajadores
        Trabajador1.setText("");
        Trabajador2.setText("");
        Trabajador3.setText("");
        InputTrabajadorEst.setText("");
        GraficoTrabajadoresEst.getData().clear();
    }
    private void GraficarTrabajador(String ID, String Nombre)
    {//Funcion para graficar de barras el historial de ventas de un trabajador
        String Venta="0", aux="";
        XYChart.Series valores = new XYChart.Series<>();
        try
        {//consulta para obtener el monto total ventas por trabajador
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT DISTINCT " +
                            "trabajador.nombre_trab, "+
                            "SUM(compra.cantidad * producto.precio) AS total, " +
                            "CASE WHEN SUM(compra.cantidad * producto.precio) IS NULL THEN 0 " +
                            "ELSE SUM(compra.cantidad * producto.precio) END  "+
                            "FROM " +
                            "cliente, compra, producto, atiende, trabajador " +
                            "WHERE " +
                            "compra.id_producto = producto.id_producto AND " +
                            "compra.id_cliente = cliente.id_cliente AND " +
                            "atiende.id_cliente = cliente.id_cliente AND " +
                            "atiende.id_trabajador = trabajador.id_trabajador AND " +
                            "atiende.fecha_atiende = compra.fecha_compra AND " +
                            "trabajador.id_trabajador = "+ID;
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {
                    aux = consulta.getString(1);//almacenamiento del nombre del trabajador
                    Venta = consulta.getString(3);//almacenamiento del monto total vendido por el trabajador
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
        valores.getData().add(new XYChart.Data(Nombre, Integer.parseInt(Venta)));//Seteo de los datos en la lista de datos
        valores.setName(aux);//Etiqueta del grafico
        GraficoTrabajadoresEst.getData().addAll(valores);//Seteo de los datos en el grafico
    }
    @FXML
    private void SelecTrabajadorEst(MouseEvent event)
    {//Funcion para seleccionar a un trabajadro
        if(TablaTrabajadoresEst.getSelectionModel().getSelectedItem() == null)
        {//Caso en que no se ha seleccionado ningun trabajador
            try
            {
                AlertaDatoErroneoController.SetMensaje("No ha seleccionado a un trabajador");
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
        {//Caso en que se ha seleccionado un trabajador
            String ID1="", ID2="", ID3="", aux="", N1="", N2="", N3="";
            if(Trabajador1.getText().equals(""))
            {//Caso en que el primer txtfield esta vacio
                try
                {//obtencion del nodo de la tabla de los trabajadores
                    TreeItem<Trabajadores> W1 = TablaTrabajadoresEst.getSelectionModel().getSelectedItem();
                    Trabajador1.setText(W1.getValue().GetNombre());//seteo del nombre en el txtfield
                    ID1 = W1.getValue().GetId();//obtencion de la id
                    N1 = W1.getValue().GetNombre();//obtencion del nombre
                }
                catch(Exception e)
                {
                    System.out.println("Error: "+e);
                }
                GraficarTrabajador(ID1, N1);//llamada al procedimiento de grafica del trabajador seleccionado
            }
            else if(!Trabajador1.getText().equals("") && Trabajador2.getText().equals(""))
            {//caso en que el primer txtfield esta ocupado y el segundo esta vacio
                try
                {
                    TreeItem<Trabajadores> W2 = TablaTrabajadoresEst.getSelectionModel().getSelectedItem();//seleccion del nodo de la tabla de trabajadores
                    aux = W2.getValue().GetNombre();//obtencion del nombre
                    if(aux.equals(Trabajador1.getText()))
                    {//caso en que el trabajador numero 2 es igual al primero
                        try
                        {
                            AlertaDatoErroneoController.SetMensaje("Seleccione un trabajador diferente");
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
                    {//caso en que son diferentes
                        Trabajador2.setText(W2.getValue().GetNombre());//seteo del nombre en el txtfield 2
                        ID2 = W2.getValue().GetId();//obtencion de la ID
                        N2 = W2.getValue().GetNombre();//obtencion del nombre
                        GraficarTrabajador(ID2, N2);//llamada a la funcion de graficacion con los parametros del segundo trabajador
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Error: "+e);
                }
            }
            else if(!Trabajador1.getText().equals("") && !Trabajador2.getText().equals("") && Trabajador3.getText().equals(""))
            {//caso en que el primer y segundo txtfield estan ocupados y el tercero esta vacio
                try
                {
                    TreeItem<Trabajadores> W3 = TablaTrabajadoresEst.getSelectionModel().getSelectedItem();//obtencion del nodo de la tabla de trabajadores
                    aux = W3.getValue().GetNombre();//obtencion del nombre del trabajador seleccionad
                    if(aux.equals(Trabajador1.getText()) || aux.equals(Trabajador2.getText()))
                    {//caso en que sea igual al primer o segundo trabajador seleccionado
                        try
                        {
                            AlertaDatoErroneoController.SetMensaje("Seleccione un trabajador diferente");
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
                    {//caso en que son distintos
                        Trabajador3.setText(W3.getValue().GetNombre());//seteo en el tercer txtfield del nombre
                        ID3 = W3.getValue().GetId();//obtencion de la id del trabajador seleccionado
                        N3 = W3.getValue().GetNombre();//obtencion del nombre
                        GraficarTrabajador(ID3, N3);//llamada a la funcion de graficacion con los parametros del tercer trabajador seleccionado
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Error: "+e);
                }
            }
            else
            {//caso en que estan los tres espacios ocupados y se debe limpiar el grafico completo
                try
                {
                    AlertaDatoErroneoController.SetMensaje("Debe limpiar los datos seleccionados");
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
    private void BusquedaInstantaneaTrabajadores(KeyEvent event)
    {//Funcion para validar la busqueda de trabajadores por su nombre en el panel de estadisticas de ventas del trabajador
        int i=0;
        String Nombre;
        Algoritmos valid = new Algoritmos();
        if(!"".equals(InputTrabajadorEst.getText()))
        {//obtencion de los caracteres ingresados
            if(!valid.ValidacionLetras(InputTrabajadorEst.getText()))
            {//caso en que son numeros
                i++;
                InputTrabajadorEst.setText("");
            }
        }
        if(i>0)
        {//mensaje de error en el caso que son numeros
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
        {//caso en que son solo letras
            DataTrabajadores.clear();
            Nombre = InputTrabajadorEst.getText().toUpperCase();//conversion a mayusculas de lo ingresado
            BusquedaBBDDTrabajadores(Nombre);//llamada a la funcion de busqueda instantanea por el nombre
        }
    }
    private void BusquedaBBDDTrabajadores(String Nombre)
    {//Funcion para buscar de manera instantanea en la DB los trabajadores en funcion del nombre
        DataTrabajadores.clear();
        try
        {
           Conexion miconexion =  new Conexion();
           String Consulta="SELECT id_trabajador, nombre_trab, estado_trab "+
                           "FROM trabajador "+
                           "WHERE nombre_trab LIKE '"+Nombre+"%' "+
                           "ORDER BY nombre_trab";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())//almacenamiento de los datos en una lista
                    DataTrabajadores.add(new Trabajadores(consulta.getString(1),consulta.getString(2) ,"" ,consulta.getString(3)));
                //seteo de los datos en la tabla de trabajadores
                final TreeItem<Trabajadores> root =  new RecursiveTreeItem<>(DataTrabajadores, RecursiveTreeObject::getChildren);
                TablaTrabajadoresEst.setRoot(root);
                TablaTrabajadoresEst.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    private void LlenarTrabajadores()
    {//Funcion para llenar de manera predeterminada la tabla de trabajadores del panel de estadisticas
        DataTrabajadores.clear();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_trabajador, nombre_trab, estado_trab "+
                            "FROM trabajador "+
                            "ORDER BY nombre_trab ";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())//almacenamiento de los datos en la lista
                    DataTrabajadores.add(new Trabajadores(consulta.getString(1), consulta.getString(2),"" ,consulta.getString(3)));
                //seteo de los datos en la tabla de trabajadores
                final TreeItem<Trabajadores> root =  new RecursiveTreeItem<>(DataTrabajadores, RecursiveTreeObject::getChildren);
                TablaTrabajadoresEst.setRoot(root);
                TablaTrabajadoresEst.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    /*Panel de horarios de trabajadores*/
    @FXML
    private void BusquedaInstantaneaTrabajador2(KeyEvent event)
    {//Validacion de letras de la busqueda instantanea por nombre de los producto
        int i=0;
        String Nombre;
        Algoritmos valid = new Algoritmos();
        if(!"".equals(NombreTrabajadorHorarios.getText()))
        {//caso en que hay al menos 1 caracter ingresado
            if(!valid.ValidacionLetras(NombreTrabajadorHorarios.getText()))
            {//caso en que hay un numero
                i++;
                NombreTrabajadorHorarios.setText("");
            }
        }
        if(i>0)
        {//caso en que hay numeros ingresados
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
                System.out.print("Error de busqueda: "+e);
            }
        }
        else
        {//caso en que hay solo letras
            if(!"".equals(NombreTrabajadorHorarios.getText()))
            {
                DataTurnosPersona.clear();
                Nombre = NombreTrabajadorHorarios.getText().toUpperCase();//conversion a letras mayusculas del nombre ingresado
                LlenarTablaTrabajadoresMes(Nombre);//invocacion a la funcion de busqueda en la DB
            }
        }
    }
    private void LlenarTablaTrabajadoresMes(String Nombre)
    {//Funcion para llenar la tabla de usuarios ordenada alfabeticamente, por año y por mes
        DataTurnosPersona.clear();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT " +
                            "trabajador.nombre_trab, " +
                            "trabajador.id_trabajador, " +
                            "MONTH(turnos.fecha_inicio), " +
                            "YEAR(turnos.fecha_inicio) " +
                            "FROM trabajador, turnos " +
                            "WHERE " +
                            "turnos.id_trabajador = trabajador.id_trabajador AND "+
                            "trabajador.nombre_trab LIKE '"+Nombre+"%' "+
                            "GROUP BY MONTH(turnos.fecha_inicio) "+
                            "ORDER BY trabajador.nombre_trab, YEAR(turnos.fecha_inicio), MONTH(turnos.fecha_inicio)";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())//almacenamiento de los datos en la lista
                    DataTurnosPersona.add(new HorasConsulta(consulta.getString(1), consulta.getString(2), consulta.getString(3), consulta.getString(4), "", "", "", "", "", "", "", "", ""));
                //seteo de los datos en la tabla de trabajadores
                final TreeItem<HorasConsulta> root =  new RecursiveTreeItem<>(DataTurnosPersona, RecursiveTreeObject::getChildren);
                TablaPersonasTurnos.setRoot(root);
                TablaPersonasTurnos.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    @FXML
    private void LlenarTablaDeTurnos(MouseEvent event)
    {
        String ID="", Mes="";
        TreeItem<HorasConsulta> trabajador = TablaPersonasTurnos.getSelectionModel().getSelectedItem();
        ID = trabajador.getValue().GetID();
        Mes = trabajador.getValue().GetMes();
        DataTurnosHora.clear();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT " +
                            "DAY(fecha_inicio), " +
                            "hora_inicio, " +
                            "HOUR(hora_inicio), " +
                            "MINUTE(hora_inicio), " +
                            "hora_termino, " +
                            "HOUR(hora_termino), " +
                            "MINUTE(hora_termino), " +
                            "id_cuadramiento_caja, "+
                            "cantidad_caja "+
                            "FROM turnos " +
                            "WHERE " +
                            "id_trabajador = "+ID+" AND MONTH(turnos.fecha_inicio) ="+Mes+" "+
                            "ORDER BY DAY(fecha_inicio), hora_inicio";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())//almacenamiento de los datos en la lista
                    DataTurnosHora.add(new HorasConsulta("", "", "", "", consulta.getString(1), consulta.getString(2), consulta.getString(5), consulta.getString(3), consulta.getString(4), consulta.getString(6), consulta.getString(7), consulta.getString(8), consulta.getString(9)));
                //seteo de los datos en la tabla de trabajadores
                final TreeItem<HorasConsulta> root =  new RecursiveTreeItem<>(DataTurnosHora, RecursiveTreeObject::getChildren);
                TablaHorariosPersona.setRoot(root);
                TablaHorariosPersona.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
        //calculo del total de hrs trabajadas en el mes
        int hrs = 0, mins = 0, hrsaux, minsaux;
        String HrsTrabajadasMes="";
        for(int i=0; i<DataTurnosHora.size(); i++)
        {//recorrido de todos los turnos del mes
            minsaux = DataTurnosHora.get(i).GetMinTermino() - DataTurnosHora.get(i).GetMinInicio();
            if(minsaux < 0)
            {
                minsaux = minsaux + 60;
                hrsaux = DataTurnosHora.get(i).GetHraTermino() - DataTurnosHora.get(i).GetHraInicio() - 1;
            }
            else
                hrsaux = DataTurnosHora.get(i).GetHraTermino() - DataTurnosHora.get(i).GetHraInicio();
            if(hrsaux < 0)
                hrsaux = hrsaux + 24;
            hrs = hrs + hrsaux;//almacenamiento de las hrs trabajadas
            mins = mins + minsaux;//almcenamiento de los mins trabajados
        }
        if(mins>=60)
        {//caso en que hay minutos que se convierten en hrs
            while(mins>=60)
            {
                mins = mins-60;
                hrs = hrs+1;
            }
        }
        if(mins<10)
            HrsTrabajadasMes = String.valueOf(hrs)+":0"+String.valueOf(mins);
        else
            HrsTrabajadasMes = String.valueOf(hrs)+":"+String.valueOf(mins);
        HorasTrabajadas.setText(HrsTrabajadasMes);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        //INICIALIZACION DEL BOTON DE SELECCION PARA COMPRA
        BtnAgregarProductoVenta.setVisible(false);
        //INICIALIZACION DE LA TABLA DE BUSQUEDA
        //creacion de cada una de las columnas con sus tamaños
        JFXTreeTableColumn<ProductosConsulta,String> ProdIDTablaB = new JFXTreeTableColumn("ID");
        JFXTreeTableColumn<ProductosConsulta,String> NombreProdTablaB = new JFXTreeTableColumn("Nombre");
        JFXTreeTableColumn<ProductosConsulta,String> PrecioProdTablaB = new JFXTreeTableColumn("Precio");
        JFXTreeTableColumn<ProductosConsulta,String> StockProdTablaB = new JFXTreeTableColumn("Stock");
        ProdIDTablaB.setPrefWidth(40);
        NombreProdTablaB.setPrefWidth(200);
        PrecioProdTablaB.setPrefWidth(100);
        StockProdTablaB.setPrefWidth(100);
        //seteo de las columnas en la tabla 
        TablaRespuestaBusqueda.getColumns().addAll(ProdIDTablaB, NombreProdTablaB, PrecioProdTablaB, StockProdTablaB);
        //indicacion de la ubicacion de la informacion en la clase asociada a la tabla y lista en que se encuentra la informacion
        ProdIDTablaB.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductosConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductosConsulta,String> param)
            {
                return param.getValue().getValue().IdProducto;
            }
        });
        NombreProdTablaB.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductosConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductosConsulta,String> param)
            {
                return param.getValue().getValue().NombreProd;
            }
        });
        PrecioProdTablaB.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductosConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductosConsulta,String> param)
            {
                return param.getValue().getValue().PrecioProd;
            }
        });
        StockProdTablaB.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductosConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductosConsulta,String> param)
            {
                return param.getValue().getValue().StockProd;
            }
        });
        //inciacion de la lista de datos 
        DataProd = FXCollections.observableArrayList();
        
        //INICIALIZACION DE LAS TABLAS DE MUESTRA DE DISTRIBUIDORES Y DE PRODUCTOS
        //Tabla Distribuidores
        TreeTableColumn DistID = new TreeTableColumn("ID");
        TreeTableColumn Nombre = new TreeTableColumn("Nombre");
        TreeTableColumn Persona = new TreeTableColumn("Contacto");
        TreeTableColumn Fono = new TreeTableColumn("Fono");
        DistID.setPrefWidth(20);
        Nombre.setPrefWidth(80);
        Fono.setPrefWidth(80);
        Persona.setPrefWidth(226);
        Distribuidores.getColumns().addAll(DistID, Nombre, Persona, Fono);
        DistID.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProveedorConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProveedorConsulta,String> param)
            {
                return param.getValue().getValue().IdProveedor;
            }
        });
        Nombre.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProveedorConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProveedorConsulta,String> param)
            {
                return param.getValue().getValue().NombreProveedor;
            }
        });
        Persona.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProveedorConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProveedorConsulta,String> param)
            {
                return param.getValue().getValue().PersonaContacto;
            }
        });
        Fono.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProveedorConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProveedorConsulta,String> param)
            {
                return param.getValue().getValue().Fono;
            }
        });
        DataProv = FXCollections.observableArrayList();
        //Tabla de Productos
        TreeTableColumn ProdID = new TreeTableColumn("ID");
        TreeTableColumn NombreProd = new TreeTableColumn("Nombre");
        TreeTableColumn StockProd = new TreeTableColumn("Stock");
        ProdID.setPrefWidth(20);
        NombreProd.setPrefWidth(200);
        StockProd.setPrefWidth(50);
        ProductosSolcitud.getColumns().addAll(ProdID, NombreProd, StockProd);
        ProdID.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductosConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductosConsulta,String> param)
            {
                return param.getValue().getValue().IdProducto;
            }
        });
        NombreProd.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductosConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductosConsulta,String> param)
            {
                return param.getValue().getValue().NombreProd;
            }
        });
        StockProd.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductosConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductosConsulta,String> param)
            {
                return param.getValue().getValue().StockProd;
            }
        });
        //LLenarTablaDistribuidor();
        //LLenarTablaProductos();
        
        //TABLAS DE COMPRAS
        //Tabla de los clientes
        TreeTableColumn IdCliente = new TreeTableColumn("ID");
        TreeTableColumn NombreCliente = new TreeTableColumn("Nombre");
        TreeTableColumn EstadoCliente = new TreeTableColumn("Estado");
        IdCliente.setPrefWidth(20);
        NombreCliente.setPrefWidth(160);
        EstadoCliente.setPrefWidth(60);
        TablaUsuariosVenta.getColumns().addAll(IdCliente, NombreCliente, EstadoCliente);
        IdCliente.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ClienteConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ClienteConsulta,String> param)
            {
                return param.getValue().getValue().IdCliente;
            }
        });
        NombreCliente.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ClienteConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ClienteConsulta,String> param)
            {
                return param.getValue().getValue().NombreCliente;
            }
        });
        EstadoCliente.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ClienteConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ClienteConsulta,String> param)
            {
                return param.getValue().getValue().EstCliente;
            }
        });
        DataClient = FXCollections.observableArrayList();
        //Tabla de Productos
        TreeTableColumn IdProdVenta = new TreeTableColumn("ID");
        TreeTableColumn NombreProdVenta = new TreeTableColumn("Nombre");
        TreeTableColumn StockProdVenta = new TreeTableColumn("Stock");
        TreeTableColumn PrecioProdVenta = new TreeTableColumn("Precio");
        IdProdVenta.setPrefWidth(24);
        NombreProdVenta.setPrefWidth(160);
        StockProdVenta.setPrefWidth(50);
        PrecioProdVenta.setPrefWidth(50);
        TablaProductosVenta.getColumns().addAll(IdProdVenta, NombreProdVenta, StockProdVenta, PrecioProdVenta);
        IdProdVenta.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductosConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductosConsulta,String> param)
            {
                return param.getValue().getValue().IdProducto;
            }
        });
        NombreProdVenta.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductosConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductosConsulta,String> param)
            {
                return param.getValue().getValue().NombreProd;
            }
        });
        StockProdVenta.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductosConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductosConsulta,String> param)
            {
                return param.getValue().getValue().StockProd;
            }
        });
        PrecioProdVenta.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductosConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductosConsulta,String> param)
            {
                return param.getValue().getValue().PrecioProd;
            }
        });
        //LlenarProductosVenta();
        //LlenarCompradoresVenta();
        //Tabla de compra
        TreeTableColumn NombreProductoTablaVenta = new TreeTableColumn("Producto");
        TreeTableColumn Cantidad = new TreeTableColumn("Cantidad");
        TreeTableColumn PrecioAPagar = new TreeTableColumn("Total");
        NombreProductoTablaVenta.setPrefWidth(160);
        Cantidad.setPrefWidth(75);
        PrecioAPagar.setPrefWidth(50);
        TablaCompraVenta.getColumns().addAll(NombreProductoTablaVenta, Cantidad, PrecioAPagar);
        Cantidad.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CompraConsulta,String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CompraConsulta,String> param)
            {
                return param.getValue().getValue().CantidadCompra;
            }
        });
        PrecioAPagar.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CompraConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CompraConsulta,String> param)
            {
                return param.getValue().getValue().MontoTotalCompra;
            }
        });
        NombreProductoTablaVenta.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CompraConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<CompraConsulta,String> param)
            {
                return param.getValue().getValue().NombreProdCompra;
            }
        });
        DataCompra = FXCollections.observableArrayList();
        
        /*PANEL DE ESTADISTICAS*/
        TreeTableColumn NombreTrabajador = new TreeTableColumn("Nombre");
        TreeTableColumn EstadoTrabajador = new TreeTableColumn("Estado");
        NombreTrabajador.setPrefWidth(162);
        EstadoTrabajador.setPrefWidth(159);
        TablaTrabajadoresEst.getColumns().addAll(NombreTrabajador, EstadoTrabajador);
        NombreTrabajador.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Trabajadores, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Trabajadores,String> param)
            {
                return param.getValue().getValue().Nombre;
            }
        });
        EstadoTrabajador.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Trabajadores, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<Trabajadores,String> param)
            {
                return param.getValue().getValue().Activo;
            }
        });
        DataTrabajadores = FXCollections.observableArrayList();
        LlenarTrabajadores();
        //Panel de horarios de turnos
        //tabla de personas
        TreeTableColumn NombresTrabajadorHorarios = new TreeTableColumn("Nombre");
        TreeTableColumn MesTrabajo = new TreeTableColumn("Mes");
        TreeTableColumn AnioTrabajo = new TreeTableColumn("Año");
        NombresTrabajadorHorarios.setPrefWidth(160);
        MesTrabajo.setPrefWidth(82);
        AnioTrabajo.setPrefWidth(81);
        TablaPersonasTurnos.getColumns().addAll(NombresTrabajadorHorarios, MesTrabajo, AnioTrabajo);
        NombresTrabajadorHorarios.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HorasConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<HorasConsulta,String>param)
            {
                return param.getValue().getValue().NombreTrabajador;
            }
        });
        MesTrabajo.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HorasConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<HorasConsulta,String>param)
            {
                return param.getValue().getValue().MesTrabajador;
            }
        });
        AnioTrabajo.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HorasConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<HorasConsulta,String>param)
            {
                return param.getValue().getValue().AnioTrabajador;
            }
        });
        DataTurnosPersona = FXCollections.observableArrayList();
        //tabla de la descripcion del turno por dia
        TreeTableColumn DiaTrabajo = new TreeTableColumn("Dia");
        TreeTableColumn HoraInicio = new TreeTableColumn("Inicio");
        TreeTableColumn FinTurno = new TreeTableColumn("Termino");
        TreeTableColumn DuracionTurno = new TreeTableColumn("Duracion");
        TreeTableColumn CuadramientoCaja = new TreeTableColumn("Cuadramiento");
        TreeTableColumn DiferenciaCaja = new TreeTableColumn("Diferencia"); 
        DiaTrabajo.setPrefWidth(30);
        HoraInicio.setPrefWidth(60);
        FinTurno.setPrefWidth(70);
        DuracionTurno.setPrefWidth(80);
        CuadramientoCaja.setPrefWidth(92);
        DiferenciaCaja.setPrefWidth(92);
        TablaHorariosPersona.getColumns().addAll(DiaTrabajo, HoraInicio, FinTurno, DuracionTurno, CuadramientoCaja, DiferenciaCaja);
        DiaTrabajo.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HorasConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<HorasConsulta,String>param)
            {
                return param.getValue().getValue().DiaTurno;
            }
        });
        HoraInicio.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HorasConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<HorasConsulta,String>param)
            {
                return param.getValue().getValue().InicioTurno;
            }
        });
        FinTurno.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HorasConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<HorasConsulta,String>param)
            {
                return param.getValue().getValue().FinTurno;
            }
        });
        DuracionTurno.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HorasConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<HorasConsulta,String>param)
            {
                return param.getValue().getValue().TotalTrabajadoTurno;
            }
        });
        CuadramientoCaja.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HorasConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<HorasConsulta,String>param)
            {
                return param.getValue().getValue().CuadramientoCaja;
            }
        });
        DiferenciaCaja.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HorasConsulta, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<HorasConsulta,String>param)
            {
                return param.getValue().getValue().DiferenciaCaja;
            }
        });
        DataTurnosHora = FXCollections.observableArrayList();
    }      
}
