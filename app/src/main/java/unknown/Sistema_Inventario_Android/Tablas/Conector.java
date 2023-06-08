package unknown.Sistema_Inventario_Android.Tablas;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conector extends SQLiteOpenHelper {


    public Conector(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TablaUsuario.CREAR_TABLA_USUARIOS);
        db.execSQL(TablaCliente.CREAR_TABLA_CLIENTE);
        db.execSQL(TablaInventario.CREAR_TABLA_INVENTARIO);
        db.execSQL(TablaVenta.CREAR_TABLA_VENTA);
        db.execSQL(TablaVendedor.CREAR_TABLA_VENDEDORES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS "+ TablaInventario.TABLA_INVENTARIO);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS "+ TablaCliente.TABLA_CLIENTE);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS "+ TablaVenta.TABLA_VENTA);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS "+ TablaVendedor.TABLA_VENDEDORES);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS "+ TablaUsuario.TABLA_USUARIOS);
        onCreate(db);
    }
}