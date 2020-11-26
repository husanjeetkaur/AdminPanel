package com.example.firestoredataapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button button,button1;
    ImageView imageView;
    EditText name , desc;
    Spinner Category,SubCategory;
    ProgressDialog pd;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseFirestore db ;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private Uri imageUri ;
    String downloadUrl;

    String id = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Data");

        button = findViewById(R.id.Addbutton);
        button1 = findViewById(R.id.Showbutton);
        imageView = findViewById(R.id.image);
        name = findViewById(R.id.Name);
        desc = findViewById(R.id.Desc);
        Category = findViewById(R.id.category);
        SubCategory = findViewById(R.id.subcategory);

        pd = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage =FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        String[] items = new String[]{"Select Category", "Women", "Men", "Boys", "Girls"};
        Category.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));
        Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Ccategory = Category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String[] items1 = new String[]{"Select Sub-Category", "Clothing", "Accessories", "Shoes"};
        SubCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items1));
        SubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Subcategory = SubCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri == null){
                    Toast.makeText(MainActivity.this,"Please Select An Image",Toast.LENGTH_LONG).show();

                }else if(name.getText().toString().isEmpty()){
                    name.setError("Please Enter Price");
                    name.requestFocus();
                }else if(desc.getText().toString().isEmpty()){
                    desc.setError("Empty");
                    desc.requestFocus();
                }else if(Category.equals("Select Category")){
                    Category.requestFocus();
                    Toast.makeText(MainActivity.this,"Please Select Category",Toast.LENGTH_LONG).show();
                }else if(SubCategory.equals("Select Sub-Category")){
                    Toast.makeText(MainActivity.this,"Please Select Sub-Category",Toast.LENGTH_LONG).show();
                }else {

                    pd.setMessage("Uploading...");
                    pd.show();
                    UploadImage();
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ListActivity.class);
                intent.putExtra("Id",id);
                startActivity(intent);
            }
        });
    }


    private void UploadImage() {

        if (imageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference str = FirebaseStorage.getInstance().getReference();
            StorageReference filePath = str.child("Images").child(imageUri.getLastPathSegment());
            final UploadTask uploadTask = filePath.putFile(imageUri);
                   uploadTask .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {

                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                                {
                                    if (!task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        throw task.getException();
                                    }

                                    downloadUrl = filePath.getDownloadUrl().toString();
                                    return filePath.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        downloadUrl = task.getResult().toString();
                                        uploadData();

                                    }
                                }
                            });

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });
        }
    }
    private void uploadData() {

        pd.setTitle("Adding Data...");
        pd.show();
        String Image = downloadUrl;
        String Name = name.getText().toString();
        String Desc = desc.getText().toString();
        String category = Category.getSelectedItem().toString();
        String Subcategory = SubCategory.getSelectedItem().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("Image",Image);
        doc.put("Id", id);
        doc.put("Price", Name);
        doc.put("Desc", Desc);
        doc.put("Category", category);
        doc.put("Sub-Category", Subcategory);
        db.collection("Products").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Product Added", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void SelectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    }
