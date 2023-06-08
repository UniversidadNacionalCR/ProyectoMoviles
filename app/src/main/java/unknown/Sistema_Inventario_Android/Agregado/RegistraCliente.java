
package unknown.Sistema_Inventario_Android.Agregado;

import android.content.ContentValues;
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
import android.widget.Spinner;
import android.widget.Toast;

import unknown.Sistema_Inventario_Android.OpcionesPrincipales.Clientes;
import unknown.Sistema_Inventario_Android.Tablas.Conector;
import unknown.Sistema_Inventario_Android.R;
import unknown.Sistema_Inventario_Android.Tablas.TablaCliente;

public class RegistraCliente extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ImageView back;
    Spinner rifident;
    EditText n,r,d;
    FloatingActionButton y;
    int grade;
    @Override
    public void onBackPressed(){
        backf();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clientes);
        grade = Integer.parseInt(getIntent().getExtras().get("grade").toString());
        rifident=(Spinner) findViewById(R.id.spinner_rif);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.rif_ident,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rifident.setAdapter(adapter);
        rifident.setOnItemSelectedListener(this);
        n= (EditText) findViewById(R.id.nombre);
        r= (EditText) findViewById(R.id.rif2);
        d= (EditText) findViewById(R.id.direccion);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onBackPressed();
            }
        });
        y = (FloatingActionButton) findViewById(R.id.b_add);
        y.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (isStr(n.getText().toString().toUpperCase())){
                    if (isInt(r.getText().toString()) && r.getText().toString().length()==9){
                        if (isStr(d.getText().toString())){
                            if(!isExist(rifident.getSelectedItem().toString()+"-"+r.getText().toString())){
                                registrar();
                                Toast.makeText(getApplicationContext(),"Registrado con exito",Toast.LENGTH_SHORT).show();
                                backf();
                            }else{
                                Toast.makeText(getApplicationContext(),"El cliente ya esta registrado",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext()," Direccion esta vacio",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Error en RIF",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Nombre esta vacio",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isExist(String n){
        Conector conn=new Conector(this, TablaCliente.TABLA_CLIENTE,null,1);
        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TablaCliente.TABLA_CLIENTE+" ORDER BY " + TablaCliente.CAMPO_NOMBRE + " asc",null);

        if (checkdb(c)) {
            c.moveToFirst();
            do{
                if(n==c.getString(2) || n.equals(c.getString(2)) ) {
                    c.close();
                    db.close();
                    return true;
                }
            }while(c.moveToNext());
            c.close();
            db.close();
            return false;

        } else{
            c.close();
            db.close();
            return false;
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
        Intent intent = new Intent (getApplicationContext(), Clientes.class);
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
    private boolean isStr(String ed_text){
        if(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    private void registrar() {
        Conector conn=new Conector(this, TablaCliente.TABLA_CLIENTE,null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        Cursor c = db.rawQuery("SELECT * FROM "+ TablaCliente.TABLA_CLIENTE+" ORDER BY " + TablaCliente.ID_CLIENTES+ " asc",null);
        String Row="0";
        if (checkdb(c)) {
            c.moveToLast();
            Row=""+(Integer.parseInt(c.getString(0))+1);
        }
        values.put(TablaCliente.ID_CLIENTES,Row);
        values.put(TablaCliente.CAMPO_NOMBRE,n.getText().toString().toUpperCase());
        values.put(TablaCliente.CAMPO_RIF,rifident.getSelectedItem().toString()+"-"+r.getText().toString());
        values.put(TablaCliente.CAMPO_DIRECCION,d.getText().toString());
        db.insert(TablaCliente.TABLA_CLIENTE, TablaCliente.CAMPO_NOMBRE,values);
        c = db.rawQuery("SELECT * FROM "+ TablaCliente.TABLA_CLIENTE,null);
        c.moveToFirst();
        c.close();
        db.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
