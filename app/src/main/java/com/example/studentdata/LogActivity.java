package com.example.studentdata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {
    FirebaseFirestore Fstore;
    ListView lv;
    List<String> values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        lv = findViewById(R.id.list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,values);
        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(LogActivity.this,UpdateActivity.class));
            }
        });

        Fstore = FirebaseFirestore.getInstance();
        Fstore.collection("students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LogActivity.this, "fetched data", Toast.LENGTH_SHORT).show();
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                    {
                        values.add(documentSnapshot.getString("fname")+"\n Age:"+documentSnapshot.getString("old"));
                        Log.d("Read data",documentSnapshot.getId()+"===="+documentSnapshot.getData());
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}