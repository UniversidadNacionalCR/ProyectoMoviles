package unknown.Sistema_Inventario_Android.Edicion;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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

public class InventarioEdicion extends AppCompatActivity {
    ImageView back,edit;
    EditText n,p,d;
    FloatingActionButton yes;
    int iterator;
    String nombre,precio,disponible;
    int band_edit=0;
    int grade;
    Conector conn=new Conector(this, TablaInventario.TABLA_INVENTARIO,null,1);
    @Override
    public void onBackPressed(){
        backf();
    }
    private void backf(){
        Intent intent = new Intent (getApplicationContext(), Inventario.class);
        intent.putExtra("grade",grade);
        startActivityForResult(intent,2);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventario);
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
        iterator = Integer.parseInt(getIntent().getExtras().get("iterator").toString());
        ReadData();
        n.setText(nombre);
        n.setEnabled(false);
        p.setText(precio);
        p.setEnabled(false);
        d.setText(disponible);
        d.setEnabled(false);
        edit = (ImageView) findViewById(R.id.edit);
        yes = (FloatingActionButton) findViewById(R.id.b_yes);
        edit.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view){
                if(band_edit==0){
                    band_edit=1;
                    n.setEnabled(true);
                    p.setEnabled(true);
                    d.setEnabled(true);
                    yes.setVisibility(View.VISIBLE);
                    edit.setImageResource(R.mipmap.ic_x);
                }else{
                    promptDialogedit();
                }
            }
        });
        yes.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view){
                if (isStr(n.getText().toString().toUpperCase())){
                    if (isDob(p.getText().toString())){
                        if (isInt(d.getText().toString())){
                            promptDialogyes();
                            Toast.makeText(getApplicationContext(),"Registrado con exito",Toast.LENGTH_SHORT).show();
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
            double num = Integer.parseInt(numero);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isStr(String ed_text){
        return !(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null);
    }
    private  void ReadData() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String [] campos = {TablaInventario.CAMPO_NOMBRE, TablaInventario.CAMPO_PRECIO, TablaInventario.CAMPO_DISPONIBLE};
        Cursor c = db.rawQuery("SELECT * FROM "+ TablaInventario.TABLA_INVENTARIO+" ORDER BY " + TablaInventario.CAMPO_NOMBRE + " asc",null);
        c.moveToFirst();
        int i=0;
        if(i!=iterator) {
            while (i != iterator) {
                c.moveToNext();
                i++;
            }
        }
        nombre = c.getString(1).toString().toUpperCase();
        precio = c.getString(2).toString();
        disponible = c.getString(3).toString();
        db.close();
    }
    private void registrar() {
        SQLiteDatabase db=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        Cursor c = db.rawQuery("SELECT "+ TablaInventario.ID_INVENTARIO+" FROM "+ TablaInventario.TABLA_INVENTARIO+" ORDER BY " + TablaInventario.ID_INVENTARIO+ " asc",null);
        String Row="0";
        if (checkdb(c)) {
            c.moveToLast();
            Row=""+(Integer.parseInt(c.getString(0))+1);
        }
        c.close();
        values.put(TablaInventario.ID_INVENTARIO,Row);
        values.put(TablaInventario.CAMPO_NOMBRE,n.getText().toString().toUpperCase());
        values.put(TablaInventario.CAMPO_PRECIO,p.getText().toString());
        values.put(TablaInventario.CAMPO_DISPONIBLE,d.getText().toString());

        db.insert(TablaInventario.TABLA_INVENTARIO, TablaInventario.CAMPO_NOMBRE,values);
        c = db.rawQuery("SELECT * FROM "+ TablaInventario.TABLA_INVENTARIO,null);
        c.moveToFirst();
        c.close();
        db.close();
    }
    public Boolean checkdb(Cursor c){
        return c.moveToFirst();
    }
    private void promptDialogedit() {
        final EditText edtText = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Eliminar del inventario?");
        builder.setCancelable(false);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db=conn.getWritableDatabase();
                db.delete(TablaInventario.TABLA_INVENTARIO, TablaInventario.CAMPO_NOMBRE + "='" + nombre+"'", null);
                db.close();
                Toast.makeText(getApplicationContext(),n.getText().toString().toUpperCase() + " fue eliminado del inventario",Toast.LENGTH_SHORT).show();
                backf();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }
    private void promptDialogyes() {
        final EditText edtText = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificar");
        builder.setMessage("¿Modificar en el inventario?");
        builder.setCancelable(false);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                band_edit=0;
                SQLiteDatabase db=conn.getReadableDatabase();
                db.delete(TablaInventario.TABLA_INVENTARIO, TablaInventario.CAMPO_NOMBRE + "='" + nombre+"'", null);
                registrar();
                n.setEnabled(false);
                p.setEnabled(false);
                d.setEnabled(false);
                yes.setVisibility(View.INVISIBLE);
                edit.setImageResource(R.mipmap.ic_edit);
                Toast.makeText(getApplicationContext(),"Cambio registrado con exito",Toast.LENGTH_SHORT).show();
                backf();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }
}
