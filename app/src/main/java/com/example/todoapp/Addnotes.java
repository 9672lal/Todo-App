package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Addnotes extends AppCompatActivity {
    EditText title, description;
    Button add;
    String id;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnotes);
        title = findViewById(R.id.title);
        description = findViewById(R.id.discription);
       add=findViewById(R.id.b1);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();

        try {
            id = intent.getExtras().getString("id");
            title.setText(intent.getExtras().getString("title"));
            description.setText(intent.getExtras().getString("desc"));
        }catch (NullPointerException e)
        {

        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        title.addTextChangedListener(loginTextWatcher);

          add.setEnabled(title.length()!=0 && description.length()!=0);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                add.setEnabled(false);
                addNotes();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void addNotes()
    {
        String t = title.getText().toString();
        String d = description.getText().toString();

        if(id == null) id = mDatabase.push().getKey();

        Notes listdata = new Notes(id,t,d);

        mDatabase.child("Notes").child(id).setValue(listdata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Note Added/Updated", Toast.LENGTH_SHORT).show();
                finish();
                progressDialog.dismiss();
                add.setEnabled(true);
            }
        });


    }
 private TextWatcher loginTextWatcher =new TextWatcher() {
     @Override
     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

     }

     @Override
     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String ussernameinput= title.getText().toString().trim();
         String ussernamess= description.getText().toString().trim();
         add.setEnabled(!ussernameinput.isEmpty() );
     }

     @Override
     public void afterTextChanged(Editable editable) {

     }
 };
}
