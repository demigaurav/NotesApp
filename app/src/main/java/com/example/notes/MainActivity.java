package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView list;
    ArrayAdapter<String> notes;
    SharedPreferences sharedPreferences;
    ArrayList<String> note;
    int r;
    Intent intent;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int n=note.size();
        note.add(n,"");
        intent=new Intent(getApplicationContext(),noteActivity.class);
        intent.putExtra("i",n);
        try {
            sharedPreferences.edit().putString("Str",ObjectSerializer.serialize(note)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

        startActivity(intent);

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences=this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        Integer c= sharedPreferences.getInt("c",0);
        list = findViewById(R.id.list);

        if(c==0) {

            note = new ArrayList<>();


                note.add("Example note");


            try {
                sharedPreferences.edit().putString("Str", ObjectSerializer.serialize(note)).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
            sharedPreferences.edit().putInt("c",1).apply();
        }


            String Str = sharedPreferences.getString("Str", "");
            try {
                note = (ArrayList<String>) ObjectSerializer.deserialize(Str);
            } catch (Exception e) {
                e.printStackTrace();
            }


            notes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, note);

            list.setAdapter(notes);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               intent=new Intent(getApplicationContext(),noteActivity.class);
                intent.putExtra("i",i);

                startActivity(intent);
            }
        });



        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Are You Sure You Want to Delete this Note?")

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {

                                Toast.makeText(MainActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                note.remove(i);
                                list.setAdapter(notes);
                                try {
                                    sharedPreferences.edit().putString("Str",ObjectSerializer.serialize(note)).apply();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


                return true;
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();


        String Str= sharedPreferences.getString("Str","");
        try {
            note= (ArrayList<String>) ObjectSerializer.deserialize(Str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notes=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,note);
        list.setAdapter(notes);

        try {
            sharedPreferences.edit().putString("Str",ObjectSerializer.serialize(note)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void update(View view){
        Toast.makeText(this, note.get(0), Toast.LENGTH_SHORT).show();
    }

}
