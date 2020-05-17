package com.example.donemodev;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EventList {
    private static EventList instance = null;
    private static String SHARED_PREFS_FILE = "event";

    public ArrayList<MyEvent> data;

    private EventList()
    {
        data = new ArrayList<>();
    }

    public static EventList getInstance()
    {
        if (instance == null) instance = new EventList();
        return instance;
    }

    public static void setData(ArrayList<MyEvent> data)
    {
        instance.data = data;
    }

    public static void writeData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        try {
            editor.putString("eventList", ObjectSerializer.serialize(instance.data));
        } catch (IOException e) {
            e.printStackTrace();
        }

        editor.commit();
    }

    public static void readData(Context context) {
        if (instance == null) instance = new EventList();

        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        try {
            instance.data = (ArrayList<MyEvent>) ObjectSerializer.deserialize(prefs.getString("eventList", ObjectSerializer.serialize(new ArrayList<MyEvent>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
