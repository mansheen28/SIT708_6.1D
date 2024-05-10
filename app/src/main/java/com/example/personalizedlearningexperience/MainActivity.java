package com.example.personalizedlearningexperience;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    DBHelper database;
    EditText ed_un, et_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        database = new DBHelper(this);
        ed_un = findViewById(R.id.editTextUsername);
        et_pass = findViewById(R.id.editTextPassword);
        findViewById(R.id.textViewNeedAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Registration.class);
                startActivity(i);
            }
        });
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = login(ed_un.getText().toString(), et_pass.getText().toString());
                if (id == null || id.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Cannot find user with this combination", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, Dashboard.class);
                    i.putExtra("id", id);
                    startActivity(i);
                }
            }
        });
    }

    public String login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) return null;

        SQLiteDatabase db = database.getReadableDatabase();
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(
                "my_table",
                new String[]{"id"},
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String userId = null;
        if (cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndex("id"));
        }
        cursor.close();
        db.close();

        return userId;
    }
}