package unknown.Sistema_Inventario_Android.Agregado;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import unknown.Sistema_Inventario_Android.OpcionesPrincipales.Usuarios;
import unknown.Sistema_Inventario_Android.R;
import unknown.Sistema_Inventario_Android.Tablas.Conector;
import unknown.Sistema_Inventario_Android.Tablas.TablaUsuario;

public class AgregaUsuarios extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Conector conn=new Conector(this, TablaUsuario.TABLA_USUARIOS,null,1);
    ImageView back;
    Spinner user_levels;
    EditText user,pass;
    TextView id;
    FloatingActionButton y;
    String Row;
    int grade;
    @Override
    public void onBackPressed(){
        backf();
    }
    private void backf(){
        Intent intent = new Intent (getApplicationContext(), Usuarios.class);
        intent.putExtra("grade",grade);
        startActivityForResult(intent,1);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_usuarios);
        grade = Integer.parseInt(getIntent().getExtras().get("grade").toString());
        SQLiteDatabase db=conn.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TablaUsuario.TABLA_USUARIOS+" ORDER BY " + TablaUsuario.ID_USER+ " asc",null);
        c.moveToLast();
        Row=""+(Integer.parseInt(c.getString(0))+1);
        id =(TextView)  findViewById(R.id.id);
        id.setText(Row);
        user_levels=(Spinner) findViewById(R.id.level);
        String [] userlevel = new String[]{""};
        switch (grade){
            case 1:
                userlevel= new String[]{
                        "2","3","4"
                };
                break;
            case 2:
                userlevel= new String[]{
                        "3","4"
                };
                break;
            case 3:
                userlevel= new String[]{
                        "4"
                };
                break;
        }
        List<String> stringlist;
        stringlist= new ArrayList<>(Arrays.asList(userlevel));
        ArrayAdapter<String> adapter;
        adapter= new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,stringlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_levels.setAdapter(adapter);
        user_levels.setOnItemSelectedListener(this);
        user= (EditText) findViewById(R.id.user);
        pass= (EditText) findViewById(R.id.pass);
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
                if (isStr(user.getText().toString().toUpperCase())){
                    if(isStr(pass.getText().toString().toUpperCase())) {
                        if (!isExist(user.getText().toString().toUpperCase())) {
                            registrar();
                            backf();
                            Toast.makeText(getApplicationContext(), "Registrado con exito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "El nombre de usuario ya esta registrado", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Contraseña esta vacio",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Usuario esta vacio",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void registrar() {
        ContentValues values=new ContentValues();
        SQLiteDatabase db=conn.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TablaUsuario.TABLA_USUARIOS+" ORDER BY " + TablaUsuario.ID_USER+ " asc",null);
        c.moveToLast();
        values.put(TablaUsuario.ID_USER,Row);
        values.put(TablaUsuario.CAMPO_USER,user.getText().toString().toUpperCase());
        values.put(TablaUsuario.CAMPO_PASS,pass.getText().toString().toUpperCase());
        values.put(TablaUsuario.CAMPO_RANK,user_levels.getSelectedItem().toString());
        db.insert(TablaUsuario.TABLA_USUARIOS, TablaUsuario.ID_USER,values);
        c = db.rawQuery("SELECT * FROM "+ TablaUsuario.TABLA_USUARIOS,null);
        c.moveToFirst();
        c.close();
        db.close();
    }
    private boolean isExist(String n){
        Conector conn=new Conector(this, TablaUsuario.TABLA_USUARIOS,null,1);
        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TablaUsuario.TABLA_USUARIOS+" ORDER BY " + TablaUsuario.CAMPO_USER + " asc",null);
        if (checkdb(c)) {
            c.moveToFirst();
            do{
                if(n==c.getString(1) || n.equals(c.getString(1)) ) {
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
    private boolean isStr(String ed_text){
        return !(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        }
}
