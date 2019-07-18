package com.example.todoapp;
import android.app.ProgressDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class updateAnd extends AppCompatActivity {
    Button update, delete,imageupload;
    String id;
    DatabaseReference mDatabase;
    EditText ntitle, ndiscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_and);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        ntitle = findViewById(R.id.title);
        imageupload=findViewById(R.id.imageupload);
        ndiscription = findViewById(R.id.discription);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        ntitle.setText(intent.getExtras().getString("title"));
        ndiscription.setText(intent.getExtras().getString("desc"));
imageupload.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i=new Intent(getApplicationContext(),Image.class);
        startActivity(i);

    }
});

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("Notes").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(updateAnd.this, "Task Deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(updateAnd.this,homeActivity.class));
                        }
                    }
                });
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask();
            }
        });
    }
    public void updateTask(){
        String myTitle =ntitle.getText().toString();
        String myDescription=ndiscription.getText().toString();
        Notes listdata = new Notes(id,myTitle,myDescription);
        mDatabase.child("Notes").child(id).setValue(listdata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(updateAnd.this, "Notes Updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),homeActivity.class));
            }
        });
    }
}
