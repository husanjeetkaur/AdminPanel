package com.example.firestoredataapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListActivity extends AppCompatActivity  {

    List<Model> modelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public String uniqueId;
    DocumentReference databaseReference;
    CustomAdapter adapter;
    FirebaseFirestore db;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        db=FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        pd = new ProgressDialog(this);

        uniqueId = getIntent().getStringExtra("Id");

        ShowAllData();


    }

    private void ShowAllData() {

        pd.setTitle("Loading Data...");
        pd.show();


        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        pd.dismiss();
                        for(DocumentSnapshot doc: task.getResult()){
                            Model model = new Model(
                                    doc.getString("Category"),
                                    doc.getString("Sub-Category"),
                                    doc.getString("Price"),
                                    doc.getString("Desc"),
                                    doc.getString("Image"),
                                    doc.getString("Id")

                            );
                            modelList.add(model);
                        }
                        adapter = new CustomAdapter(ListActivity.this,modelList);
                        recyclerView.setAdapter(adapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ListActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

    }



    private void deleteProduct() {
    }

    public void delete(View view) {
        pd.setTitle("Deleting Data...");

        pd.show();
        FirebaseFirestore.getInstance().collection("Products")
                .document(uniqueId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                        builder.setTitle("Do you want to delete this product permanently:");
                        builder.setMessage("Deletion is permanent...");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),"Product Deleted Successfully!",Toast.LENGTH_LONG).show();
                                deleteProduct();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pd.dismiss();
                            }
                        });
                        AlertDialog ad = builder.create();
                        ad.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_LONG).show();

                    }
                });

    }
    }
