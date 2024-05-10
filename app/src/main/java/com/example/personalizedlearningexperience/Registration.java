package com.example.personalizedlearningexperience;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class Registration extends AppCompatActivity {

    private static final int REQ = 1;
    FrameLayout frame;
    ImageView view_profile;
    private byte[] img_d;
    EditText et_un, et_email, et_ce, et_pass, et_cpass, et_phone;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        frame = findViewById(R.id.fr_image);
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ);
            }
        });
        dbHelper = new DBHelper(this);
        et_un = findViewById(R.id.editTextUsername);
        et_email = findViewById(R.id.editTextEmail);
        view_profile = findViewById(R.id.imageViewProfile);
        et_ce = findViewById(R.id.editTextConfirmEmail);
        et_pass = findViewById(R.id.editTextPassword);
        et_cpass = findViewById(R.id.editTextConfirmPassword);
        et_phone = findViewById(R.id.editTextPhoneNumber);
        findViewById(R.id.buttonCreateAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_un.getText().toString();
                String email = et_email.getText().toString();
                String confirmEmail = et_ce.getText().toString();
                String password = et_pass.getText().toString();
                String confirmPassword = et_cpass.getText().toString();
                String phoneNumber = et_phone.getText().toString();

                if (username.isEmpty() || email.isEmpty() || confirmEmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(Registration.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!email.equals(confirmEmail)) {
                    Toast.makeText(Registration.this, "Email and Confirm Email do not match", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(Registration.this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
                } else {
                    long id = register(username, email, password, phoneNumber);
                    Toast.makeText(Registration.this, "Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Registration.this, YourInterests.class);
                    i.putExtra("data", new String[]{ String.valueOf(id), username, ""});
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                view_profile.setImageBitmap(bitmap);

                // Convert bitmap to byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                img_d = stream.toByteArray();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public long register(String username, String email, String password, String phone) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);
        values.put("phone", phone);
        values.put("interest", "");
        if(img_d == null) {
            values.put("image", encodeImage(BitmapFactory.decodeResource(getResources(), R.drawable.profile)));
        }else {
            values.put("image", img_d);
        }
        long id = db.insert("my_table", null, values);
        db.close();
        return id;
    }

    private byte[] encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        return stream.toByteArray();
    }
}