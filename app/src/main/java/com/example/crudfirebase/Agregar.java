package com.example.crudfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Agregar extends AppCompatActivity {

    EditText nombre, apellido, email, imgURL;
    Button btn_Add, btn_Atras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        nombre=findViewById(R.id.nombreTxt);
        apellido=findViewById(R.id.apellidoTxt);
        email=findViewById(R.id.emailTxt);
        imgURL=findViewById(R.id.img1);

        btn_Add= findViewById(R.id.btn_add);
        btn_Atras=findViewById(R.id.btn_atras);

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarDatos();
            }
        });
        btn_Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void insertarDatos(){
        Map<String, Object>map= new HashMap<>();
        map.put("Nombre",nombre.getText().toString());
        map.put("Apellido",apellido.getText().toString());
        map.put("Email",email.getText().toString());
        map.put("imgURL",imgURL.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Programación Android").push()
                .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Agregar.this,"Insertado correctamente", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Agregar.this,"Error al insertar",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}