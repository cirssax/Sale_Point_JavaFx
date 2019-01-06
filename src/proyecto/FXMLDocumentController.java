/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import Alertas.AccesoCorrectoController;
import Alertas.AlertaDatoErroneoController;
import Alertas.UsuarioRootController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Algoritmos.Algoritmos;
import ConexionBBDD.Conexion;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import org.apache.commons.codec.digest.DigestUtils;
/**
 *
 * @author Cristobal
 */
class Trabajadores extends RecursiveTreeObject<Trabajadores>
{
    StringProperty IdUsuario;
    StringProperty Nombre;
    StringProperty Estado;
    StringProperty Activo;
    String IdUsuarioE;
    String Nombre2;
    
    public Trabajadores(String IdUsuarioIn, String Nombre, String Estado, String ActivoIn)
    {
        this.IdUsuario = new SimpleStringProperty(IdUsuarioIn);
        this.Nombre = new SimpleStringProperty(Nombre);
        this.IdUsuarioE = IdUsuarioIn;
        if(Estado.equals("2"))
            this.Estado =  new SimpleStringProperty("ADMINISTRADOR");
        else
            this.Estado = new SimpleStringProperty("VENDEDOR");
        if(ActivoIn.equals("1"))
            this.Activo = new SimpleStringProperty("ACTIVO");
        else
            this.Activo = new SimpleStringProperty("INACTIVO");
        this.Nombre2 = Nombre;
    }
    public String GetId()
    {
        return IdUsuarioE;
    }
    public String GetNombre()
    {
        return Nombre2;
    }
}

public class FXMLDocumentController implements Initializable {
    
    private static String ID_TRABAJADOR_INGRESADO="";
    private static String Fecha="";
    private static String HoraInicio="";
    private static void SetInicioTurno(String FechaInicio, String Hora)
    {
        Fecha = FechaInicio;
        HoraInicio = Hora;
    }
    public static String GetFecha()
    {
        return Fecha;
    }
    public static String GetHoraInicio()
    {
        return HoraInicio;
    }
    @FXML
    private static void SetIdTrabajador(String Id)
    {//Funcion globar para setear el id del trabajador que acaba de acceder a la DB
        ID_TRABAJADOR_INGRESADO=Id;
    }
    @FXML
    public static String GetIdTrabajador()
    {//Funcion global para acceder al id del trabajador que esta usando el software
        return ID_TRABAJADOR_INGRESADO;
    }
    
    private boolean Controller = false;
    
    @FXML
    private Label PestSup;
    @FXML
    private Pane PanelAcceso, PanelAgregar, PanelAgregarUsr;
    @FXML
    public JFXButton BotonAcceso, BotonAgregar, BotonSalir, BotonAgregarUsuario, BotonIngreso, BtnModificarUsuario, BtnSelecUsuario;
    @FXML
    public JFXComboBox<String> ComboBoxCargos, ComboEstado;
    @FXML
    public JFXTextField Usuario, UsuarioNuevo, CorreoNuevo, FonoNuevo, RutNuevoUsuario, DomNuevo, BusquedaNombre;
    @FXML
    public JFXPasswordField Pass, ContraseñaNueva, ContraseñaNueva2;
    @FXML
    private JFXTreeTableView<Trabajadores> TablaUsuarios;
    ObservableList<Trabajadores>DataUsuarios;
    
    //FUNCIONES DE CONSULTAS
    private boolean ValidUsuario()
    {//Funcion para determinar si el usuario que se esta intentando ingresar existe y esta activo o no
        String[] Datos;
        boolean valid=false;
        Datos = new String[2];
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT estado_trab, id_trabajador " +
                            "FROM trabajador "+
                            "WHERE nombre_trab='"+Usuario.getText().toUpperCase()+"' AND pass_trab='"+DigestUtils.sha1Hex(Pass.getText())+"'";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {//almacenamiento de los datos
                    Datos[0] = consulta.getString(1);
                    Datos[1] = consulta.getString(2);
                }
                if(Datos[0].equals("1"))
                {//seteo de la id del trabajador de turno y retorno de true ya que se encontro un trabajador al menos que 
                    SetIdTrabajador(Datos[1]);
                    valid = true;
                }   
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR de acceso a BBDD: "+e);
        }
        return valid;
    }
    private void InicioTurno()
    {
        java.util.Date FechaActual = new java.util.Date();
        SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyy-MM-dd");
        SimpleDateFormat FormatoHora = new SimpleDateFormat("HH:mm:ss");
        String Fecha = FormatoFecha.format(FechaActual);
        String Hora = FormatoHora.format(FechaActual);
        SetInicioTurno(Fecha, Hora);
        try
        {//Paso para hacer la insercion de datos
            Conexion miconexion1=new Conexion();
            String sentencia1="INSERT INTO turnos (id_trabajador, hora_inicio, fecha_inicio, hora_termino, id_cuadramiento_caja, cantidad_caja) VALUES (?,?,?,?,?,?) ";
            miconexion1.psPrepararSentencias=miconexion1.miconexion.prepareStatement(sentencia1);
            miconexion1.psPrepararSentencias.setString(1, GetIdTrabajador());//id_trabajador
            miconexion1.psPrepararSentencias.setString(2, Hora);//hora inicio del turno
            miconexion1.psPrepararSentencias.setString(3, Fecha);//fecha del turno
            miconexion1.psPrepararSentencias.setString(4, "00:00:00");//hora termino
            miconexion1.psPrepararSentencias.setString(5, "0");//id_cuadramiento_de_caja
            miconexion1.psPrepararSentencias.setString(6, "0");//cantidad_caja
            if(miconexion1.psPrepararSentencias.executeUpdate()>0)//opcion para hacer la actualizacion de la insercion en la DB
                System.out.println("Inicio de turno almacenado correctamente");
        }
        catch (Exception e)
        {
            System.out.println("Error inicio de turno: "+e);
        }
        
    }
    @FXML
    private void PanelModUsr(MouseEvent event)
    {//Funcion para traer al frente el panel que permite seleccionar un usuario para editarlo post
        PanelAgregarUsr.toFront();
        LlenarTablaUsuarios();
    }
    @FXML
    private void PanelAcceso(MouseEvent event)
    {//Funcion para traer al frente el panel de acceso del software
        PanelAcceso.toFront();
        LlenarTablaUsuarios();
        Usuario.setText("");
        Pass.setText("");
        Controller = false;
    }
    @FXML
    private void Salir(MouseEvent event)
    {//Funcion para cerrar el programa completo
        System.exit(0);
    }
    @FXML
    private void PanelAgregar(MouseEvent event)
    {//Funcion para traer al frente el panel para agregar y crear un usuario nuevo
        PanelAgregar.toFront();
        PestSup.setText("Nuevo Usuario");
        LlenarTablaUsuarios();
        Controller = false;
        UsuarioNuevo.setText("");
        CorreoNuevo.setText("");
        FonoNuevo.setText("");
        RutNuevoUsuario.setText("");
        ContraseñaNueva.setText("");
        ContraseñaNueva2.setText("");
        DomNuevo.setText("");
        ComboBoxCargos.setValue("Cargos");
        ComboEstado.setVisible(false);
        BotonAgregarUsuario.setText("Crear");
    }
    @FXML
    private void Acceso(MouseEvent event)
    {//FUNCION PARA ACCEDER
        int i=0;
        Algoritmos algoritmo = new Algoritmos();
        String User;
        User = Usuario.getText();
        if("".equals(Pass.getText()))//No se a ingresado usuario
            i++;
        if(!algoritmo.ValidacionLetras(User))//Caso en que el usuario no es un nombre
            i++;
        if(i>0)
        {//Caso en que hay errores
            try
            {
                AlertaDatoErroneoController.SetMensaje("Usuario mal ingresado");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Alertas/AlertaDatoErroneo.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root1));
                stage.show();
            }
            catch(Exception e)
            {
                System.out.print("Error de escritura: "+e);
            }
            Usuario.setText("");
            Pass.setText("");
        }
        else
        {//Caso en que los datos ingresados son coherentes
            if(ValidUsuario())
            {//caso en que los datos ingresados existen y son consistentes
                InicioTurno();
                try
                {
                    Usuario.setText("");
                    Pass.setText("");
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Alertas/AccesoCorrecto.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(new Scene(root1));
                    stage.show();
                }
                catch(Exception e)
                {
                    System.out.print("Error de conexion a acceso correcto: "+e);
                }
            }
            else
            {//caso en que el usuario o la contraseña estan mal escritos
                try
                {
                    AlertaDatoErroneoController.SetMensaje("Usuario o contraseña mal escritos");
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Alertas/AlertaDatoErroneo.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(new Scene(root1));
                    stage.show();
                }
                catch(Exception e)
                {
                    System.out.print("Error de conexion a acceso incorrecto: "+e);
                }
                Usuario.setText("");
                Pass.setText("");
            }
        }
    }
    
    private boolean UsuarioRepetido(String Nombre)
    {//Funcion para validar que al crear un nuevo usuario, comprobar si este ya existe o no. True--> !existe    False--> existe
        boolean valid = false;
        try
        {//consulta que devuelve -1 si el trabajador no existe y cualquier otro numero si existe
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT " +
                            "CASE " +
                            "	WHEN SUM(trabajador.id_trabajador + 0) IS NULL THEN -1 " +
                            "   ELSE SUM(trabajador.id_trabajador + 0) END " +
                            "FROM trabajador " +
                            "WHERE " +
                            "trabajador.nombre_trab = '"+Nombre+"' ";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {
                    if(consulta.getString(1).equals("-1"))
                        valid = true;//caso en que el usuario no existe
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
    private void CrearUsuario(MouseEvent event)
    {//FUNCION PARA CREAR USUARIO
        int i=0;
        Algoritmos valid = new Algoritmos();
        if(!valid.ValidacionLetras(UsuarioNuevo.getText()))
        {//Caso en que el usuario tiene un nombre incoherente
            i++;
            UsuarioNuevo.setText("");
        }
        if(!valid.ValidacionNumerica(FonoNuevo.getText()))
        {//Caso en que el teléfono es un valor no numerico
            i++;
            FonoNuevo.setText("");
        }
        if(!ContraseñaNueva.getText().equals(ContraseñaNueva2.getText()))
        {//Caso en que las contraseñas ingresadas son diferentes
            i++;
            ContraseñaNueva.setText("");
            ContraseñaNueva2.setText("");
        }
        if(!valid.ValidacionNumerica(RutNuevoUsuario.getText()))
        {
            i++;
            RutNuevoUsuario.setText("");
        }
        if("".equals(UsuarioNuevo.getText()) || "".equals(FonoNuevo.getText()) || "".equals(ContraseñaNueva.getText()) || "".equals(ContraseñaNueva2.getText()) || ComboBoxCargos.getValue()==null || "".equals(DomNuevo.getText()) || "".equals(RutNuevoUsuario.getText()))
            i++;//Caso en que alguno de los datos ingresados es nulo o vacío
        if(i==0)
        {//Caso en que no hay errores
            String Pass = DigestUtils.sha1Hex(ContraseñaNueva.getText());//Encriptacion de la contraseña definitiva
            if(!Controller)
            {//caso en que se va a realizar una insercion
                if(UsuarioRepetido(UsuarioNuevo.getText().toUpperCase()))
                {//caso en que el usuario no esta repetido
                    String[] Informacion = new String[8];
                    Informacion[0]=UsuarioNuevo.getText().toUpperCase();
                    Informacion[1]=RutNuevoUsuario.getText();
                    Informacion[2]=FonoNuevo.getText();
                    Informacion[3]=CorreoNuevo.getText().toUpperCase();
                    Informacion[4]=DomNuevo.getText().toUpperCase();
                    Informacion[5]=Pass;
                    if(ComboBoxCargos.getValue().toString().equals("VENDEDOR"))
                        Informacion[6]="1";
                    else
                        Informacion[6]="2";
                    if(!Controller)
                        Informacion[7]="1";
                    else
                    {
                        if(ComboEstado.getValue().toString().equals("ACTIVO"))
                            Informacion[7]="1";
                        else
                            Informacion[7]="0";
                    }
                    ComboEstado.setVisible(false);
                    UsuarioRootController.SetDatos(Informacion);//seteo externo de un arreglo con la informacion del usuario en el panel de confirmacion
                    try
                    {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Alertas/UsuarioRoot.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setScene(new Scene(root1));
                        stage.show();
                    }
                    catch(Exception e)
                    {
                        System.out.print("Error en agregar usuario: "+e);
                    }
                    UsuarioNuevo.setText("");
                    CorreoNuevo.setText("");
                    FonoNuevo.setText("");
                    RutNuevoUsuario.setText("");
                    ContraseñaNueva.setText("");
                    ContraseñaNueva2.setText("");
                    DomNuevo.setText("");
                    ComboBoxCargos.setValue("Cargos");
                    PanelAcceso.toFront();
                    LlenarTablaUsuarios();
                    Controller = false;//seteo del controlador a su valor por defecto
                }
                else
                {//caso en que el usuario ya existe
                    try
                    {
                        AlertaDatoErroneoController.SetMensaje("Ya existe un usuario con el mismo nombre");
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Alertas/AlertaDatoErroneo.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setScene(new Scene(root1));
                        stage.show();
                    }
                    catch(Exception e)
                    {
                        System.out.print("Error en mensaje de error de nuevo usuario: "+e);
                    }
                    UsuarioNuevo.setText("");
                }
            }
            else
            {//caso en que se desea realizar una insercion
                String[] Informacion = new String[8];
                Informacion[0]=UsuarioNuevo.getText().toUpperCase();
                Informacion[1]=RutNuevoUsuario.getText();
                Informacion[2]=FonoNuevo.getText();
                Informacion[3]=CorreoNuevo.getText().toUpperCase();
                Informacion[4]=DomNuevo.getText().toUpperCase();
                Informacion[5]=Pass;
                if(ComboBoxCargos.getValue().toString().equals("VENDEDOR"))
                    Informacion[6]="1";
                else
                    Informacion[6]="2";
                if(!Controller)
                    Informacion[7]="1";
                else
                {
                    if(ComboEstado.getValue().toString().equals("ACTIVO"))
                        Informacion[7]="1";
                    else
                        Informacion[7]="0";
                }
                ComboEstado.setVisible(false);
                UsuarioRootController.SetDatos(Informacion);//seteo externo de un arreglo con la informacion del usuario en el panel de confirmacion
                PanelAcceso.toFront();
                try
                {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Alertas/UsuarioRoot.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(new Scene(root1));
                    stage.show();
                }
                catch(Exception e)
                {
                    System.out.print("Error en agregar usuario: "+e);
                }
                UsuarioNuevo.setText("");
                CorreoNuevo.setText("");
                FonoNuevo.setText("");
                RutNuevoUsuario.setText("");
                ContraseñaNueva.setText("");
                ContraseñaNueva2.setText("");
                DomNuevo.setText("");
                ComboBoxCargos.setValue("Cargos");
                PanelAcceso.toFront();
                LlenarTablaUsuarios();
            }
        }
        else
        {//Caso en que hay errores
            try
            {
                AlertaDatoErroneoController.SetMensaje("Datos erroneos");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Alertas/AlertaDatoErroneo.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root1));
                stage.show();
            }
            catch(Exception e)
            {
                System.out.print("Error en mensaje de error de nuevo usuario: "+e);
            }
        }
        BotonAgregarUsuario.setText("Agregar");
    }
    
    //FUNCIONES PARA MODIFICAR UN USUARIO   
    @FXML
    private void SelecUsuario(MouseEvent event)
    {//Funcion asociada al boton de seleccion de usuario
        if(TablaUsuarios.getSelectionModel().getSelectedItem()==null)
        {//caso en que no se ha seleccionado un usuario en la tabla
            try
            {
                AlertaDatoErroneoController.SetMensaje("No ha seleccionado un usuario");
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
        {//caso en que hay uno seleccionado
            Controller = true;//seteo del semaforo de controlador para una edicion en vez de una insercion
            PestSup.setText("Modificar Usuario");
            BotonAgregarUsuario.setText("Modificar");
            try
            {
                TreeItem<Trabajadores> Trabajador = TablaUsuarios.getSelectionModel().getSelectedItem();//obtencion del nodo de la tabla
                SetearDatos(Trabajador.getValue().GetId());//seteo de datos en el controlador interno del id
                UsuarioRootController.SetControlador(true, Trabajador.getValue().GetId());//seteo de controlador externo de usuario root, para que realice un UPDATE en vez de un INSERT 
            }
            catch(Exception e)
            {
                System.out.println("Error: "+e);
            }
        }
    }
    private void SetearDatos(String ID)
    {//Funcion para setear los datos en el panel de agregar/modificar usuario
        String [] Datos = new String[8];
        try
        {
           Conexion miconexion =  new Conexion();
           String Consulta="SELECT nombre_trab, rut, fono_trab, correo_trab, direccion_trab, pass_trab, id_cargo, estado_trab "+
                           "FROM trabajador "+
                           "WHERE id_trabajador="+ID;
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                {
                    Datos[0]=consulta.getString(1);//nombre
                    Datos[1]=consulta.getString(2);//rut
                    Datos[2]=consulta.getString(3);//fono
                    Datos[3]=consulta.getString(4);//correo
                    Datos[4]=consulta.getString(5);//domicilio
                    Datos[5]=consulta.getString(6);//pass
                    if(consulta.getString(7).equals("2"))//cargo
                        Datos[6]="ADMINISTRADOR";
                    else
                        Datos[6]="VENDEDOR";
                    if(consulta.getString(8).equals("1"))
                        Datos[7]="ACTIVO";
                    else
                        Datos[7]="NO ACTIVO";
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
        ComboEstado.setVisible(true);
        PanelAgregar.toFront();
        UsuarioNuevo.setText(Datos[0]);
        CorreoNuevo.setText(Datos[3]);
        FonoNuevo.setText(Datos[2]);
        RutNuevoUsuario.setText(Datos[1]);
        ContraseñaNueva.setText("0000");
        ContraseñaNueva2.setText("0000");
        ComboBoxCargos.getSelectionModel().select(Datos[6]);
        ComboEstado.getSelectionModel().select(Datos[7]);
        DomNuevo.setText(Datos[4]);
    }
    private void BusquedaUsuariosBBDD(String Nombre)
    {//Funcion para buscar de manera instantanea un usuario por su nombre
        DataUsuarios.clear();
        try
        {
           Conexion miconexion =  new Conexion();
           String Consulta="SELECT id_trabajador, nombre_trab, id_cargo "+
                           "FROM trabajador "+
                           "WHERE nombre_trab LIKE '"+Nombre+"%' "+
                           "ORDER BY nombre_trab";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())//seteo de los datos la lista
                    DataUsuarios.add(new Trabajadores(consulta.getString(1),consulta.getString(2) , consulta.getString(3), ""));
                //seteo de los datos en la tabla
                final TreeItem<Trabajadores> root =  new RecursiveTreeItem<>(DataUsuarios, RecursiveTreeObject::getChildren);
                TablaUsuarios.setRoot(root);
                TablaUsuarios.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    private void LlenarTablaUsuarios()
    {//Funcion para llenar de manera predeterminada los datos en la tabla de usuarios
        DataUsuarios.clear();
        try
        {
            Conexion miconexion =  new Conexion();
            String Consulta="SELECT id_trabajador, nombre_trab, id_cargo "+
                            "FROM trabajador "+
                            "ORDER BY nombre_trab ";
            ResultSet consulta=miconexion.consulta(Consulta);
            if(consulta!=null)
            {
                while(consulta.next())
                    DataUsuarios.add(new Trabajadores(consulta.getString(1), consulta.getString(2), consulta.getString(3), ""));
                final TreeItem<Trabajadores> root =  new RecursiveTreeItem<>(DataUsuarios, RecursiveTreeObject::getChildren);
                TablaUsuarios.setRoot(root);
                TablaUsuarios.setShowRoot(false);
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR: "+e);
        }
    }
    @FXML
    private void BusquedaInstantaneaUsuarios(KeyEvent event)
    {//Funcion para validar la busqueda instantanea de usuarios por su nombre
        int i=0;
        String Nombre;
        Algoritmos valid = new Algoritmos();
        if(!"".equals(BusquedaNombre.getText()))
        {//caso en que hay al menos un caracter
            if(!valid.ValidacionLetras(BusquedaNombre.getText()))
            {//caso en que hay un numero
                i++;
                BusquedaNombre.setText("");
            }
        }
        if(i>0)
        {//muestra de error del ingreso de un numero
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
            DataUsuarios.clear();
            Nombre = BusquedaNombre.getText().toUpperCase();//conversion a mayusculas del texto
            BusquedaUsuariosBBDD(Nombre);//llamada a la funcion que realiza la busqueda en funcion del nombre
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        ID_TRABAJADOR_INGRESADO="";
        Fecha="";
        HoraInicio="";
         //ELEMENTOS DE LA OPCION DE AGREGAR USUARIO
        ComboEstado.getItems().add("ACTIVO");
        ComboEstado.getItems().add("NO ACTIVO");
        ComboEstado.setVisible(false);
        ComboBoxCargos.getItems().add("ADMINISTRADOR");
        ComboBoxCargos.getItems().add("VENDEDOR");   
        NumberValidator numValidator = new NumberValidator();
        RutNuevoUsuario.getValidators().add(numValidator);
        numValidator.setMessage("Sin puntos ni digito verificador");
        RutNuevoUsuario.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if(!newValue)
                {
                    RutNuevoUsuario.validate();
                }
            }
        });
        //ELEMENTOS DE LA OPCION PARA MODIFICAR LOS USUARIOS
        TreeTableColumn Id = new TreeTableColumn("ID");
        TreeTableColumn Nombre = new TreeTableColumn("Nombre");
        TreeTableColumn Estado = new TreeTableColumn("Estado");
        Id.setPrefWidth(25);
        Nombre.setPrefWidth(160);
        Estado.setPrefWidth(140);
        TablaUsuarios.getColumns().addAll(Id, Nombre, Estado);
        Id.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Trabajadores,String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Trabajadores,String> param)
            {
                return param.getValue().getValue().IdUsuario;
            }
        });
        Nombre.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Trabajadores, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Trabajadores,String> param)
            {
                return param.getValue().getValue().Nombre;
            }
        });
        Estado.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Trabajadores, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call (TreeTableColumn.CellDataFeatures<Trabajadores,String> param)
            {
                return param.getValue().getValue().Estado;
            }
        });
        DataUsuarios = FXCollections.observableArrayList();
        LlenarTablaUsuarios();
    }
}
