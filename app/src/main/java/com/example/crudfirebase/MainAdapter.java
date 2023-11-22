package com.example.crudfirebase;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {


    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.nombre.setText(model.getNombre());
        holder.apellido.setText(model.getApellido());
        holder.email.setText(model.getEmail());

        Glide.with(holder.img.getContext())
                .load(model.getImgURL())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus= DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.ventana_emergente))
                        .setExpanded(true,1200)
                        .create();
                View view=dialogPlus.getHolderView();
                EditText nombre= view.findViewById(R.id.nombreTxt);
                EditText apellido= view.findViewById(R.id.apellidoTxt);
                EditText email= view.findViewById(R.id.emailTxt);
                EditText imgURL = view.findViewById(R.id.img1);

                Button actualizar = view.findViewById(R.id.btn_actualizar);

                nombre.setText(model.getNombre());
                apellido.setText(model.getApellido());
                email.setText(model.getEmail());
                imgURL.setText(model.imgURL);

                dialogPlus.show();

                actualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object>map = new HashMap<>();
                        map.put("Nombre", nombre.getText().toString());
                        map.put("Apellido", apellido.getText().toString());
                        map.put("Email", email.getText().toString());
                        map.put("imgURL",imgURL.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Programación android")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.nombre.getContext(),"Actualización Correcta", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.nombre.getContext(),"Error en la Actualización",Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
                holder.eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder= new AlertDialog.Builder(holder.nombre.getContext());
                        builder.setTitle("Seguro de eliminar?");
                        builder.setMessage("Eliminadio");

                        builder.setPositiveButton("Eliminado", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("Programación Android")
                                        .child(getRef(position).getKey())
                                        .removeValue();
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(holder.nombre.getContext(),"Cancelar",Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }
                });
            }
        });
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent, false);
        return null;
    }
    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView nombre, apellido, email;

        Button editar, eliminar;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img1);
            nombre = itemView.findViewById(R.id.nombreTxt);
            apellido = itemView.findViewById(R.id.apellidoTxt);
            email = itemView.findViewById(R.id.emailTxt);

            editar= itemView.findViewById(R.id.btn_edit);
            eliminar=itemView.findViewById(R.id.btn_eliminar);
        }
    }
}
