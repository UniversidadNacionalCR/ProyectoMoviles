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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import unknown.Sistema_Inventario_Android.OpcionesPrincipales.Vendedores;
import unknown.Sistema_Inventario_Android.R;
import unknown.Sistema_Inventario_Android.Tablas.Conector;
import unknown.Sistema_Inventario_Android.Tablas.TablaVendedor;

public class VendedoresEdicion extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ImageView back,edit;
    Spinner rifident;
    EditText n,r,t,em,d;
    FloatingActionButton y;
    int iterator;
    String nombre,rif2,rifi,direccion,telefono,correo;
    int band_edit=0;
    int grade;
    Conector conn=new Conector(this, TablaVendedor.TABLA_VENDEDORES,null,1);
    @Override
    public void onBackPressed(){
        backf();
    }
    private void backf(){
        Intent intent = new Intent (getApplicationContext(), Vendedores.class);
        intent.putExtra("grade",grade);
        startActivityForResult(intent,1);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vendedores);
        grade = Integer.parseInt(getIntent().getExtras().get("grade").toString());
        iterator = Integer.parseInt(getIntent().getExtras().get("iterator").toString());
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
        ReadData();
        rifident.setEnabled(false);
        n.setEnabled(false);
        r.setEnabled(false);
        t.setEnabled(false);
        em.setEnabled(false);
        d.setEnabled(false);
        n.setText(nombre);
        r.setText(rif2);
        t.setText(telefono);
        em.setText(correo);
        d.setText(direccion);
        d.setText(direccion);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onBackPressed();
            }
        });
        y = (FloatingActionButton) findViewById(R.id.b_yes);
        edit = (ImageView) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view){
                if(band_edit==0){
                    band_edit=1;
                    rifident.setEnabled(true);
                    n.setEnabled(true);
                    r.setEnabled(true);
                    t.setEnabled(true);
                    em.setEnabled(true);
                    d.setEnabled(true);
                    y.setVisibility(View.VISIBLE);
                    edit.setImageResource(R.mipmap.ic_x);
                }else{
                    promptDialogedit();
                }
            }
        });
        y.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view){
                //editarrrrrrrrrrrrrrrrrrrrrr
                if (isStr(n.getText().toString().toUpperCase())){
                    if (isInt(r.getText().toString()) && r.getText().toString().length()==9){
                        if (isStr(d.getText().toString())){
                            promptDialogyes();
                            Toast.makeText(getApplicationContext(),"Registrado con exito",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Direccion esta vacio",Toast.LENGTH_SHORT).show();
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
    private  void ReadData() {
        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TablaVendedor.TABLA_VENDEDORES+" ORDER BY " + TablaVendedor.CAMPO_NOMBRE + " asc",null);
        c.moveToPosition(iterator);
        String ident=c.getString(1).substring(0,1);
        switch(ident){
            case "E":
                rifident.setSelection(0);
                break;
            case "G":
                rifident.setSelection(1);
                break;
            case "J":
                rifident.setSelection(2);
                break;
            case "V":
                rifident.setSelection(3);
                break;
        }
        nombre = c.getString(1).toUpperCase();
        rif2 = c.getString(2).substring(2);
        direccion = c.getString(3);
        telefono=c.getString(4);
        correo=c.getString(5);
        db.close();
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
    public Boolean checkdb(Cursor c){
        return c.moveToFirst();
    }
    private void promptDialogedit() {
        final EditText edtText = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Eliminar de la lista de clientes?");
        builder.setCancelable(false);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db=conn.getWritableDatabase();
                db.delete(TablaVendedor.TABLA_VENDEDORES, TablaVendedor.CAMPO_NOMBRE + "='" + nombre+"'", null);
                db.close();
                Toast.makeText(getApplicationContext(),n.getText().toString().toUpperCase() + " fue eliminado de los vendedores",Toast.LENGTH_SHORT).show();
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
        builder.setMessage("¿Modificar el vendedor?");
        builder.setCancelable(false);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                band_edit=0;
                SQLiteDatabase db=conn.getReadableDatabase();
                db.delete(TablaVendedor.TABLA_VENDEDORES, TablaVendedor.CAMPO_NOMBRE + "='" + nombre+"'", null);
                registrar();
                n.setEnabled(false);
                r.setEnabled(false);
                d.setEnabled(false);
                y.setVisibility(View.INVISIBLE);
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
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
