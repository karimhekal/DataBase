package com.example.karimhekal.train;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;

import static android.widget.AdapterView.*;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    EditText name, id, age;
    TextToSpeech t;
    //static Set<String> set;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t=new TextToSpeech(this,this);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        db = this.openOrCreateDatabase("database", MODE_PRIVATE, null);
        id = findViewById(R.id.id);
        ListView listView = (ListView) findViewById(R.id.list);
        // SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.robpercival.notes", Context.MODE_PRIVATE);
        try {
            //     db.execSQL("delete from students");
            //   db.execSQL("insert into students (name,age)values('huss',87)");

            refresh(); //puts data to listview from database
        } catch (Exception e) {
            e.printStackTrace();
        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);
    }


    void clearTexts() {
        id.setText("");
        name.setText("");
        age.setText("");

    }

    void refresh() {
        try {
            clearTexts();
            notes.clear();
            Cursor c = db.rawQuery("select * from students ", null);
            int NameIndex = c.getColumnIndex("name");
            int AgeIndex = c.getColumnIndex("age");
            c.moveToFirst();
            while (c != null) {
                notes.add(Integer.toString(c.getInt(c.getColumnIndex("id"))) + " : " + c.getString(NameIndex) + " | Age : " + Integer.toString(c.getInt(AgeIndex)));
                c.moveToNext();
            }
        } catch (Exception e) {
            Log.i("hey", e.getMessage().toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void update(View view) {
        try {
            db = this.openOrCreateDatabase("database", MODE_PRIVATE, null);
            String ID = id.getText().toString();
            String NAME = name.getText().toString();
            String AGE = age.getText().toString();


            if (name.getText().toString().equals("") || name.getText().toString().equals(" ")) {
                t.speak("Empty", TextToSpeech.QUEUE_FLUSH, null);
            } else {

                db.execSQL("UPDATE students SET name='" + NAME + "', age=" + AGE + " WHERE id=" + ID + "");
                t.speak("Updated " + name.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }


            refresh();
        } catch (Exception e) {
            Log.i("hey", e.getStackTrace().toString());
        }

    }

    public void delete(View view) {
        try {
            db = this.openOrCreateDatabase("database", MODE_PRIVATE, null);
            String NAME = name.getText().toString();
            String AGE = age.getText().toString();

            if (name.getText().toString().equals("") || name.getText().toString().equals(" ")) {
                t.speak("Empty", TextToSpeech.QUEUE_FLUSH, null);
            } else {

                db.execSQL("delete from students where name='" + NAME + "'");
                t.speak("Deleted " + name.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }


            refresh();
        } catch (Exception e) {
            Log.i("hey", e.getStackTrace().toString());
        }

    }

    public void insert(View view) {
        try {
            db = this.openOrCreateDatabase("database", MODE_PRIVATE, null);
            String NAME = name.getText().toString();
            String AGE = age.getText().toString();

            if (name.getText().toString().equals("") || name.getText().toString().equals(" ")) {
                t.speak("Empty", TextToSpeech.QUEUE_FLUSH, null);
            } else {

                db.execSQL("insert into students (name,age) values('" + NAME + "'," + AGE + ")");
                t.speak("Added " + name.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
            refresh();
        } catch (Exception e) {
            Log.i("hey", e.getStackTrace().toString());
        }
    }

    public void deleteAll(View view) {
        try {
            db = this.openOrCreateDatabase("database", MODE_PRIVATE, null);
            String NAME = name.getText().toString();
            String AGE = age.getText().toString();
            db.execSQL("drop table students");
            db.execSQL("create table if not exists students (id integer primary key autoincrement ,name text,age integer)");
            refresh();
        } catch (Exception e) {
            Log.i("hey", e.getStackTrace().toString());
        }
    }

    @Override
    public void onInit(int i) {

    }
}
