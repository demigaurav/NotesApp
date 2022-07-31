package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class noteActivity extends AppCompatActivity  {

    EditText notes;
    ArrayList<String> note;
    int i;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        sharedPreferences=this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        note=new ArrayList<>();
        String Str= sharedPreferences.getString("Str","");
        try {
            note= (ArrayList<String>) ObjectSerializer.deserialize(Str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        i=getIntent().getIntExtra("i",0);
//        String note=getIntent().getStringExtra("note");
        notes=(EditText) findViewById(R.id.notetext);
        notes.setText(note.get(i));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        note.set(i,notes.getText().toString());

        try {
            sharedPreferences.edit().putString("Str",ObjectSerializer.serialize(note)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        getIntent().putExtra("note",note.get(i));
//        getIntent().putExtra("i",i);
        super.onBackPressed();

    }
}