package com.example.personalizedlearningexperience;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    HashMap<String, String> mapTopics = new HashMap<>();
    TextView tv_greet, tv_noti;
    DBHelper db;
    RecyclerView rv;
    Bitmap bmp;
    ImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        db = new DBHelper(this);
        setContentView(R.layout.activity_dashboard);
        GetTaskData();
        String id = getIntent().getStringExtra("id");
        rv = findViewById(R.id.recyclerViewItems);
        tv_noti = findViewById(R.id.textViewNotification);
        imgProfile = findViewById(R.id.imageViewProfile);
        String[] data = getData(id);
        imgProfile.setImageBitmap(bmp);
        tv_greet = findViewById(R.id.textViewGreeting);
        tv_greet.setText("Hello, " + data[0]);
        List<String> interests = convertToList(data[1]);
        tv_noti.setText("You have " + interests.size()  + " tasks due");
        AdapterMyTask urlAdapter = new AdapterMyTask(interests);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(urlAdapter);
    }

    private class AdapterMyTask extends RecyclerView.Adapter<AdapterMyTask.TaskViewHolder> {

        private List<String> taskList;

        public AdapterMyTask(List<String> urlList) {
            this.taskList = urlList;
        }

        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            String title = taskList.get(position);
            holder.bind(title);
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }

        public class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView tv_task_title, tv_task_description;

            public TaskViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_task_title = itemView.findViewById(R.id.tv_task_title);
                tv_task_description = itemView.findViewById(R.id.tv_task_description);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Dashboard.this, ViewTask.class);
                        i.putExtra("title", taskList.get(getAdapterPosition()));
                        i.putExtra("desc", mapTopics.get(taskList.get(getAdapterPosition())));
                        startActivity(i);
                    }
                });
            }

            public void bind(String title) {
                tv_task_title.setText(title);
                tv_task_description.setText(mapTopics.get(title));
            }
        }
    }

    public static List<String> convertToList(String input) {
        List<String> list = new ArrayList<>();
        if (input != null && !input.isEmpty()) {
            String[] parts = input.split(", ");
            list.addAll(Arrays.asList(parts));
        }
        return list;
    }

    public String[] getData(String userId) {
        SQLiteDatabase sqlDb = db.getReadableDatabase();
        String selection = "id = ?";
        String[] selectionArgs = {userId};
        Cursor cursor = sqlDb.query(
                "my_table",
                new String[]{"username", "interest", "image"},
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String[] userData = null;
        if (cursor.moveToFirst()) {
            userData = new String[2];
            userData[0] = cursor.getString(cursor.getColumnIndex("username"));
            userData[1] = cursor.getString(cursor.getColumnIndex("interest"));
        }
        byte[] imageData = cursor.getBlob(cursor.getColumnIndex("image"));
        bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        cursor.close();
        sqlDb.close();

        return userData;
    }

    public void GetTaskData() {
        mapTopics.put("Java Programming", "Develop applications using the Java programming language.");
        mapTopics.put("Python Programming", "Write code quickly and integrate systems more effectively with Python.");
        mapTopics.put("C++ Programming", "Build high-performance software using C++.");
        mapTopics.put("C# Programming", "Develop applications on Microsoft's .NET framework with C#.");
        mapTopics.put("Ruby Programming", "Create web applications easily with Ruby.");
        mapTopics.put("Swift Programming", "Develop applications for iOS, macOS, watchOS, and tvOS using Swift.");
        mapTopics.put("Kotlin Programming", "Build modern, expressive, and powerful applications with Kotlin.");
        mapTopics.put("R Programming", "Analyze and visualize data effectively using R.");
        mapTopics.put("MATLAB Programming", "Solve complex engineering and scientific problems with MATLAB.");
        mapTopics.put("Go Programming", "Write simple, efficient, and reliable software with Go.");
        mapTopics.put("PHP Programming", "Develop dynamic web pages and applications using PHP.");
        mapTopics.put("Scala Programming", "Combine object-oriented and functional programming in Scala.");
        mapTopics.put("TypeScript Programming", "Extend JavaScript by adding static types.");
        mapTopics.put("Assembly Language", "Write low-level programs for hardware interactions.");
        mapTopics.put("Perl Programming", "Write powerful and efficient scripts with Perl.");
        mapTopics.put("Lua Programming", "Embed Lua in applications for scripting and automation.");
        mapTopics.put("Haskell Programming", "Explore functional programming with Haskell.");
        mapTopics.put("Dart Programming", "Develop apps for mobile, desktop, and the web with Dart.");
        mapTopics.put("Groovy Programming", "Write concise, readable, and powerful scripts in Groovy.");
        mapTopics.put("Shell Scripting", "Automate repetitive tasks and system administration with shell scripts.");
    }
}
