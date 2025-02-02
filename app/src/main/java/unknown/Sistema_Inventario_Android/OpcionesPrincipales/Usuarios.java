package unknown.Sistema_Inventario_Android.OpcionesPrincipales;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import unknown.Sistema_Inventario_Android.Tablas.Conector;
import unknown.Sistema_Inventario_Android.R;
import unknown.Sistema_Inventario_Android.Tablas.TablaUsuario;
import unknown.Sistema_Inventario_Android.Menu;
import unknown.Sistema_Inventario_Android.Agregado.AgregaUsuarios;

public class Usuarios extends AppCompatActivity {
    ImageView back;
    FloatingActionButton add;
    ListView lista;
    Conector conn=new Conector(this, TablaUsuario.TABLA_USUARIOS,null,1);
    ArrayAdapter<String> adapter;
    ArrayList<String> ids = new ArrayList<>();
    int grade;
    int max=0;
    @Override
    public void onBackPressed(){
        backf();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        grade = Integer.parseInt(getIntent().getExtras().get("grade").toString());
        lista= (ListView) findViewById(R.id.lista_contenido);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>max) {
                    promptDialogedit(i);
                }
            }
        });
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onBackPressed();
            }
        });
        add = (FloatingActionButton) findViewById(R.id.b_add);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                    Intent intent = new Intent(getApplicationContext(), AgregaUsuarios.class);
                    intent.putExtra("grade", grade);
                    startActivityForResult(intent, 1);
                    finish();

            }
        });
        fillData();
    }
    private  void fillData() {
        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TablaUsuario.TABLA_USUARIOS+" ORDER BY " + TablaUsuario.ID_USER + " asc",null);
        if (checkdb(c)) {
            c.moveToFirst();
            do{
                if(Integer.parseInt(c.getString(3).toString())>grade || grade==1) {
                    adapter.add("Usuario: " + c.getString(1) + "\nContraseña: " + c.getString(2) + "\nRank: " + c.getString(3));
                    ids.add(c.getString(0));
                    if (c.getString(3).equals(grade)) {
                        max++;
                    }
                }
            }while(c.moveToNext());
            lista.setAdapter(adapter);
            db.close();
        }
    }
    public Boolean checkdb(Cursor c){
        return c.moveToFirst();
    }
    private void backf(){
        Intent intent = new Intent (getApplicationContext(), Menu.class);
        intent.putExtra("grade",grade);
        startActivityForResult(intent,0);
        finish();
    }
    private void promptDialogedit(final int position) {
        final EditText edtText = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Eliminar de los usuarios?");
        builder.setCancelable(false);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db2=conn.getReadableDatabase();
                Cursor c = db2.rawQuery("SELECT * FROM "+ TablaUsuario.TABLA_USUARIOS+" ORDER BY " + TablaUsuario.ID_USER + " asc",null);
                c.moveToPosition(Integer.parseInt(ids.get(position)));
                String nombre=c.getString(1);
                SQLiteDatabase db=conn.getWritableDatabase();
                db.delete(TablaUsuario.TABLA_USUARIOS, TablaUsuario.ID_USER + "='" + Integer.parseInt(ids.get(position))+"'", null);
                db.close();
                Toast.makeText(getApplicationContext(),nombre + " fue eliminado de los usuarios",Toast.LENGTH_SHORT).show();
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
