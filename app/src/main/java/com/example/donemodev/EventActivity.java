package com.example.donemodev;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.EventLog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class EventActivity extends AppCompatActivity {

    private final static int ADD_EVENT_INTENT_REQUEST_CODE = 100;
    private final static int EDIT_EVENT_INTENT_REQUEST_CODE = 101;

    private RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new EventAdapter(EventActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void init() {
        createNotificationChannel();

        EventList.readData(this);

        recyclerView = findViewById(R.id.recycler_view_main);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EventActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = findViewById(R.id.fab_add_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, AddEventActivity.class);
                intent.putExtra("type", "add");
                startActivityForResult(intent, ADD_EVENT_INTENT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_EVENT_INTENT_REQUEST_CODE) {
                int index = data.getIntExtra("index", -1);
                if (index != -1) adapter.notifyItemInserted(index);
            }
            else if (requestCode == EDIT_EVENT_INTENT_REQUEST_CODE) {
                int index = data.getIntExtra("index", -1);
                if (index != -1) adapter.notifyItemChanged(index);
            }
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("123456", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
