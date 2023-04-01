package com.example.studentdata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    EditText newn,oldn;
    Button b1,delete;
    FirebaseFirestore fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        newn = findViewById(R.id.ed2u);
        oldn = findViewById(R.id.ed1u);
        b1 =findViewById(R.id.button);
        delete = findViewById(R.id.deleteb);

        fb = FirebaseFirestore.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldname= oldn.getText().toString().trim();
                String newname = newn.getText().toString().trim();
                oldn.setText("");
                newn.setText("");
                UpdateData(oldname,newname);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = oldn.getText().toString();
                DeleteData(name);
            }
        });

    }

    private void DeleteData(String name) {

        fb.collection("students").whereEqualTo("fname", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();




                    fb.collection("students").document(documentID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(UpdateActivity.this, "Data deleted", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    startActivity(new Intent(UpdateActivity.this,LogActivity.class));
                }
            }

        });
    }

    private void UpdateData(String oldname, String newname) {
        Map<String, Object> userdetails = new HashMap<>();
        userdetails.put("fname", newname);

        fb.collection("students").whereEqualTo("fname", oldname).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();


                    fb.collection("students").document(documentID).update(userdetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(UpdateActivity.this, "Data updated", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateActivity.this, "Failure in update", Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(UpdateActivity.this,LogActivity.class));
                }
            }

        });
    }
}