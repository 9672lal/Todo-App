package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.todoapp.Addnotes;
import com.example.todoapp.R;
import com.example.todoapp.Textholder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class homeActivity extends AppCompatActivity {
    RecyclerView todoList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List <Notes> list = new ArrayList<Notes>();
    Context context;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Notes");


        todoList = findViewById(R.id.recycle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Addnotes.class));
            }
        });

        final RecyclerView.Adapter adapter = new Textholder(list,this);
        todoList.setHasFixedSize(true);
        todoList.setLayoutManager(new LinearLayoutManager(this));
        todoList.setAdapter(adapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               list.removeAll(list);
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Notes listdata = dataSnapshot1.getValue(Notes.class);
                    list.add(listdata);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "No Data added", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
