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

import unknown.Sistema_Inventario_Android.Tablas.Conector;
import unknown.Sistema_Inventario_Android.R;
import unknown.Sistema_Inventario_Android.Tablas.TablaVendedor;
import unknown.Sistema_Inventario_Android.OpcionesPrincipales.Vendedores;

public class AgregaVendedores extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ImageView back;
    Spinner rifident;
    EditText n,r,t,em,d;
    FloatingActionButton y;
    int grade;
    @Override
    public void onBackPressed(){
        backf();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendedores);
        grade = Integer.parseInt(getIntent().getExtras().get("grade").toString());
        rifident=(Spinner) findViewById(R.id.spinner_rif);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.rif_ident,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rifident.setAdapter(adapter);
        rifident.setOnItemSelectedListener(this);
        n= (EditText) findViewById(R.id.nombre);
        r= (EditText) findViewById(R.id.rif2);
        t= (EditText) findViewById(R.id.telefono);
        em= (EditText) findViewById(R.id.email);
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
                                Toast.makeText(getApplicationContext(),"El vendedor ya esta registrado",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext()," Direccion esta vacio",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Cedula no valida",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Nombre esta vacio",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isExist(String n){
        Conector conn=new Conector(this, TablaVendedor.TABLA_VENDEDORES,null,1);
        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TablaVendedor.TABLA_VENDEDORES+" ORDER BY " + TablaVendedor.CAMPO_NOMBRE + " asc",null);
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

        return c.moveToFirst();
    }
    private void backf(){
        Intent intent = new Intent (getApplicationContext(), Vendedores.class);
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
        return !(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null);
    }
    private void registrar() {
        Conector conn=new Conector(this, TablaVendedor.TABLA_VENDEDORES,null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        Cursor c = db.rawQuery("SELECT "+ TablaVendedor.ID_VENDEDORES+" FROM "+ TablaVendedor.TABLA_VENDEDORES+" ORDER BY " + TablaVendedor.ID_VENDEDORES+ " asc",null);
        String Row="0";
        if (checkdb(c)) {
            c.moveToLast();
            Row=""+(Integer.parseInt(c.getString(0))+1);
        }
        c.close();
        values.put(TablaVendedor.ID_VENDEDORES,Row);
        values.put(TablaVendedor.CAMPO_NOMBRE,n.getText().toString().toUpperCase());
        values.put(TablaVendedor.CAMPO_RIF,rifident.getSelectedItem().toString()+"-"+r.getText().toString());
        values.put(TablaVendedor.CAMPO_DIRECCION,d.getText().toString());
        values.put(TablaVendedor.CAMPO_TELEFONO,t.getText().toString());
        values.put(TablaVendedor.CAMPO_EMAIL,em.getText().toString());
        db.insert(TablaVendedor.TABLA_VENDEDORES, TablaVendedor.ID_VENDEDORES,values);
        c = db.rawQuery("SELECT * FROM "+ TablaVendedor.TABLA_VENDEDORES,null);
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
