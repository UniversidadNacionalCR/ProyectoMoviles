package unknown.Sistema_Inventario_Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import unknown.Sistema_Inventario_Android.OpcionesPrincipales.Clientes;
import unknown.Sistema_Inventario_Android.OpcionesPrincipales.Inventario;
import unknown.Sistema_Inventario_Android.OpcionesPrincipales.Ventas;
import unknown.Sistema_Inventario_Android.OpcionesPrincipales.Usuarios;
import unknown.Sistema_Inventario_Android.OpcionesPrincipales.Vendedores;

public class Menu extends AppCompatActivity {
    LinearLayout binventario;
    LinearLayout bvendedores;
    LinearLayout bventas;
    LinearLayout bclientes;
    LinearLayout buser;
    int grade ;
    @Override
    public void onBackPressed(){
        backf();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
        grade = Integer.parseInt(getIntent().getExtras().get("grade").toString());
        binventario =  (LinearLayout) findViewById(R.id.b_inventario);
        binventario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                    Intent intent = new Intent (getApplicationContext(), Inventario.class);
                    intent.putExtra("grade",grade);
                    startActivityForResult(intent,1);
                    finish();
            }
        });
        bventas =  (LinearLayout) findViewById(R.id.b_ventas);
        bventas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent (getApplicationContext(), Ventas.class);
                intent.putExtra("grade",grade);
                startActivity(intent);
            }
        });
        bclientes =  (LinearLayout) findViewById(R.id.b_clientes);
        bclientes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent (getApplicationContext(), Clientes.class);
                intent.putExtra("grade",grade);
                startActivity(intent);
            }
        });
        bvendedores =  (LinearLayout) findViewById(R.id.b_vendedores);
        bvendedores.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (grade < 4) {
                    Intent intent = new Intent(getApplicationContext(), Vendedores.class);
                    intent.putExtra("grade", grade);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Nivel de usuario insuficiente",Toast.LENGTH_SHORT).show();
                }
            }
        });
        buser =  (LinearLayout) findViewById(R.id.b_user);
        buser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(grade<3) {
                    Intent intent = new Intent(getApplicationContext(), Usuarios.class);
                    intent.putExtra("grade", grade);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Nivel de usuario insuficiente",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void backf(){
        Intent intent = new Intent (getApplicationContext(), Main.class);
        startActivityForResult(intent,0);
        finish();
    }
}
