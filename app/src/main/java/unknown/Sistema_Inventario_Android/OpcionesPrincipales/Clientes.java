package unknown.Sistema_Inventario_Android.OpcionesPrincipales;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import unknown.Sistema_Inventario_Android.Agregado.RegistraCliente;
import unknown.Sistema_Inventario_Android.Tablas.Conector;
import unknown.Sistema_Inventario_Android.Edicion.ClientesEdicion;
import unknown.Sistema_Inventario_Android.R;
import unknown.Sistema_Inventario_Android.Tablas.TablaCliente;
import unknown.Sistema_Inventario_Android.Menu;

public class Clientes extends AppCompatActivity {
    ImageView back;
    FloatingActionButton add;
    ListView lista;
    Conector conn=new Conector(this, TablaCliente.TABLA_CLIENTE,null,1);
    int grade;
    ArrayAdapter<String> adapter;
    @Override
    public void onBackPressed(){
        backf();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        grade = Integer.parseInt(getIntent().getExtras().get("grade").toString());
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                backf();
            }
        });
        lista= (ListView) findViewById(R.id.lista_contenido);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(grade<=2){
                Intent intent = new Intent (getApplicationContext(), ClientesEdicion.class);
                intent.putExtra("iterator",i);
                intent.putExtra("grade",grade);
                startActivityForResult(intent,2);
                finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Nivel de usuario insuficiente",Toast.LENGTH_SHORT).show();
                }
            }
        });
        add = (FloatingActionButton) findViewById(R.id.b_add);
        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                if(grade<=3){
                Intent intent = new Intent (getApplicationContext(), RegistraCliente.class);
                intent.putExtra("grade",grade);
                startActivityForResult(intent,2);
                finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Nivel de usuario insuficiente",Toast.LENGTH_SHORT).show();
                }
            }
        });
        fillData();
    }//oncreate
    private  void fillData() {
        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TablaCliente.TABLA_CLIENTE+" ORDER BY " + TablaCliente.CAMPO_NOMBRE + " asc",null);
        if (checkdb(c)) {
            c.moveToFirst();
            do{
                adapter.add(c.getString(1) + "\n Cedula: "+ c.getString(2));
            }while(c.moveToNext());
            lista.setAdapter(adapter);
            c.close();
            db.close();
        }
    }
    public Boolean checkdb(Cursor c){
        Boolean rowExists;

        if (c.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            rowExists = true;

        } else
        {
            // I AM EMPTY
            rowExists = false;
        }
        return rowExists;
    }
    private void backf(){
        Intent intent = new Intent (getApplicationContext(), Menu.class);
        intent.putExtra("grade",grade);
        startActivityForResult(intent,0);
        finish();
    }
}
