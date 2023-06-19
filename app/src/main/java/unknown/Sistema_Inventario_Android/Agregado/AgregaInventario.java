package unknown.Sistema_Inventario_Android.Agregado;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import unknown.Sistema_Inventario_Android.Tablas.Conector;
import unknown.Sistema_Inventario_Android.OpcionesPrincipales.Inventario;
import unknown.Sistema_Inventario_Android.R;
import unknown.Sistema_Inventario_Android.Tablas.TablaInventario;

public class AgregaInventario extends AppCompatActivity {
    ImageView back;
    EditText n,p,d;
    FloatingActionButton y;
    int grade;
    @Override
    public void onBackPressed(){
        backf();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventario);
        grade = Integer.parseInt(getIntent().getExtras().get("grade").toString());
        n=(EditText) findViewById(R.id.nombre);
        p=(EditText) findViewById(R.id.precio);
        d=(EditText) findViewById(R.id.disponibilidad);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                backf();
            }
        });
        y = (FloatingActionButton) findViewById(R.id.b_add);
        y.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (isStr(n.getText().toString().toUpperCase())){
                    if (isDob(p.getText().toString())){
                        if (isInt(d.getText().toString())){
                            if(!isExist(n.getText().toString().toUpperCase())){
                                registrar();
                                Toast.makeText(getApplicationContext(),"Registrado con exito",Toast.LENGTH_SHORT).show();
                                backf();
                            }else{
                                Toast.makeText(getApplicationContext(),"El producto ya existe en inventario",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext()," Disponible no es un numero entero \n o esta vacio",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Precio no es un numero \n o esta vacio",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Nombre esta vacio",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean isExist(String n){
        int bandExist=0;
        Conector conn=new Conector(this, TablaInventario.TABLA_INVENTARIO,null,1);
        SQLiteDatabase db=conn.getReadableDatabase();
        String [] campos = {TablaInventario.CAMPO_NOMBRE, TablaInventario.CAMPO_PRECIO, TablaInventario.CAMPO_DISPONIBLE};
        Cursor c = db.rawQuery("SELECT * FROM "+ TablaInventario.TABLA_INVENTARIO+" ORDER BY " + TablaInventario.CAMPO_NOMBRE + " asc",null);

        if (checkdb(c)) {
            c.moveToFirst();
            do{
                if(n==c.getString(0) || n.equals(c.getString(0)) ) {
                    db.close();
                    return true;
                }
            }while(c.moveToNext());
            db.close();
            return false;

        } else{
            db.close();
            return false;
        }
    }
    public Boolean checkdb(Cursor c){
        return c.moveToFirst();
    }
    private void backf(){
        Intent intent = new Intent (getApplicationContext(), Inventario.class);
        intent.putExtra("grade",grade);
        startActivityForResult(intent,1);
        finish();
    }
    private boolean isInt(String numero){
        try {
            int num = Integer.parseInt(numero);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isDob(String numero){
        try {
            double num = Double.parseDouble(numero);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isStr(String texto){
        return !(texto.isEmpty() || texto.length() == 0 || texto.equals("") || texto == null);
    }
    private void registrar() {
        Conector conector =new Conector(this, TablaInventario.TABLA_INVENTARIO,null,1);
        SQLiteDatabase db=conector.getWritableDatabase();
        ContentValues values=new ContentValues();
        Cursor cursor =
                db.rawQuery("SELECT "+ TablaInventario.ID_INVENTARIO+" FROM "+ TablaInventario.TABLA_INVENTARIO+" ORDER BY " + TablaInventario.ID_INVENTARIO+ " asc",null);
        String Row="0";
        if (checkdb(cursor)) {
            cursor.moveToLast();
            Row=""+(Integer.parseInt(cursor.getString(0))+1);
        }
        cursor.close();
        values.put(TablaInventario.ID_INVENTARIO,Row);
        values.put(TablaInventario.CAMPO_NOMBRE,n.getText().toString().toUpperCase());
        values.put(TablaInventario.CAMPO_PRECIO,p.getText().toString());
        values.put(TablaInventario.CAMPO_DISPONIBLE,d.getText().toString());

        db.insert(TablaInventario.TABLA_INVENTARIO, TablaInventario.CAMPO_NOMBRE,values);
        cursor = db.rawQuery("SELECT * FROM "+ TablaInventario.TABLA_INVENTARIO,null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

}
