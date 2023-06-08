package unknown.Sistema_Inventario_Android.Tablas;

public class TablaUsuario {
    //Constantes campos tabla usuarios
    public static final String TABLA_USUARIOS="USUARIOS";
    public static final String ID_USER="ID";
    public static final String CAMPO_USER="USER";
    public static final String CAMPO_PASS="PASSWORD";
    public static final String CAMPO_RANK="RANK";
    //CREAR TABLA USUARIOS
    public static final String CREAR_TABLA_USUARIOS="CREATE TABLE " +
            ""+TABLA_USUARIOS+" ("+ID_USER+" " +
            "TEXT, "+CAMPO_USER+" TEXT,"+CAMPO_PASS+" TEXT,"+CAMPO_RANK+" TEXT)";
}
