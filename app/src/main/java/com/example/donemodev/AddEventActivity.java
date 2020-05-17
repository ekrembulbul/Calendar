package com.example.donemodev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    int _startDay = -1, _startMonth = -1, _startYear = -1, _startHour = -1, _startMinute = -1;
    int _endDay = -1, _endMonth = -1, _endYear = -1, _endHour = -1, _endMinute = -1;
    int _alarmDay = -1, _alarmMonth = -1, _alarmYear = -1, _alarmHour = -1, _alarmMinute = -1;
    int _frequencyId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        init();
    }

    private void init() {
        final EditText name = findViewById(R.id.name_add);
        final EditText description = findViewById(R.id.description_add);
        final EditText place = findViewById(R.id.place);
        final TextView tvStartTime = findViewById(R.id.start_time);
        final TextView tvStartDay = findViewById(R.id.start_day);
        final TextView tvEndTime = findViewById(R.id.end_time);
        final TextView tvEndDay = findViewById(R.id.end_day);
        final TextView tvAlarmTime = findViewById(R.id.alarm_time);
        final TextView tvAlarmDay = findViewById(R.id.alarm_day);

        final Spinner frequencySpinner = findViewById(R.id.spinner_recall_frequency);
        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(this, R.array.string_recall_frequency, android.R.layout.simple_spinner_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(frequencyAdapter);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        final int index = intent.getIntExtra("index", -1);

        if (type.equals("edit")) {
            getSupportActionBar().setTitle("Edit Event");

            EventList eventList = EventList.getInstance();

            name.setText(eventList.data.get(index).name);
            description.setText(eventList.data.get(index).description);
            place.setText(eventList.data.get(index).place);

            _startHour = eventList.data.get(index).startHour;
            _startMinute = eventList.data.get(index).startMinute;
            if (_startHour != -1 && _startMinute != -1)
                tvStartTime.setText(_startHour + ":" + _startMinute);

            _startDay = eventList.data.get(index).startDay;
            _startMonth = eventList.data.get(index).startMonth;
            _startYear = eventList.data.get(index).startYear;
            if (_startDay != -1 && _startMonth != -1 && _startYear != -1)
                tvStartDay.setText(_startDay + "/" + _startMonth + "/" + _startYear);

            _endHour = eventList.data.get(index).endHour;
            _endMinute = eventList.data.get(index).endMinute;
            if (_endHour != -1 && _endMinute != -1)
                tvEndTime.setText(_endHour + ":" + _endMinute);

            _endDay = eventList.data.get(index).endDay;
            _endMonth = eventList.data.get(index).endMonth;
            _endYear = eventList.data.get(index).endYear;
            if (_endDay != -1 && _endMonth != -1 && _endYear != -1)
                tvEndDay.setText(_endDay + "/" + _endMonth + "/" + _endYear);

            _alarmHour = eventList.data.get(index).alarmHour;
            _alarmMinute = eventList.data.get(index).alarmMinute;
            if (_alarmHour != -1 && _alarmMinute != -1)
                tvAlarmTime.setText(_alarmHour + ":" + _alarmMinute);

            _alarmDay = eventList.data.get(index).alarmDay;
            _alarmMonth = eventList.data.get(index).alarmMonth;
            _alarmYear = eventList.data.get(index).alarmYear;
            if (_alarmDay != -1 && _alarmMonth != -1 && _alarmYear != -1)
                tvAlarmDay.setText(_alarmDay + "/" + _alarmMonth + "/" + _alarmYear);

            _frequencyId = eventList.data.get(index).frequencyId;
            frequencySpinner.setSelection(_frequencyId);
        }
        else if (type.equals("add")) {
            getSupportActionBar().setTitle("Add Event");
        }

        FloatingActionButton fabStartTime = findViewById(R.id.fab_start_time);
        fabStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        _startHour = hourOfDay;
                        _startMinute = minute;
                        tvStartTime.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);

                tpd.show();
            }
        });

        FloatingActionButton fabStartDay = findViewById(R.id.fab_start_day);
        fabStartDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        _startDay = dayOfMonth;
                        _startMonth = month;
                        _startYear = year;
                        month++;
                        tvStartDay.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, dayOfMonth);

                dpd.show();
            }
        });

        FloatingActionButton fabEndTime = findViewById(R.id.fab_end_time);
        fabEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        _endHour = hourOfDay;
                        _endMinute = minute;
                        tvEndTime.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);

                tpd.show();
            }
        });

        FloatingActionButton fabEndDay = findViewById(R.id.fab_end_day);
        fabEndDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        _endDay = dayOfMonth;
                        _endMonth = month;
                        _endYear = year;
                        month++;
                        tvEndDay.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, dayOfMonth);

                dpd.show();
            }
        });

        FloatingActionButton fabAlarmTime = findViewById(R.id.fab_alarm_time);
        fabAlarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        _alarmHour = hourOfDay;
                        _alarmMinute = minute;
                        tvAlarmTime.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);

                tpd.show();
            }
        });

        FloatingActionButton fabAlarmDay = findViewById(R.id.fab_alarm_day);
        fabAlarmDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        _alarmDay = dayOfMonth;
                        _alarmMonth = month;
                        _alarmYear = year;
                        month++;
                        tvAlarmDay.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, dayOfMonth);

                dpd.show();
            }
        });

        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _frequencyId = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        FloatingActionButton fabOk = findViewById(R.id.fab_add_event_ok);
        fabOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyEvent myEvent = new MyEvent(
                        name.getText().toString(),
                        description.getText().toString(),
                        _startDay, _startMonth, _startYear, _startHour, _startMinute,
                        _endDay, _endMonth, _endYear, _endHour, _endMinute,
                        _alarmDay, _alarmMonth, _alarmYear, _alarmHour, _alarmMinute,
                        _frequencyId,
                        place.getText().toString()
                );

                EventList eventList = EventList.getInstance();
                Intent returnIntent = new Intent();
                if (index == -1) {
                    eventList.data.add(myEvent);

                    if (_alarmDay != -1 && _alarmMonth != -1 && _alarmYear != -1 && _alarmHour != -1 && _alarmMinute != -1) setAlarm(eventList.data.size() - 1);

                    returnIntent.putExtra("index", eventList.data.size() - 1);
                    Toast.makeText(AddEventActivity.this, "Event added", Toast.LENGTH_SHORT).show();
                }
                else {
                    eventList.data.remove(index);
                    eventList.data.add(index, myEvent);

                    if (_alarmDay != -1 && _alarmMonth != -1 && _alarmYear != -1 && _alarmHour != -1 && _alarmMinute != -1) setAlarm(index);

                    returnIntent.putExtra("index", index);
                    Toast.makeText(AddEventActivity.this, "Event edited", Toast.LENGTH_SHORT).show();
                }

                setResult(Activity.RESULT_OK, returnIntent);
                EventList.writeData(AddEventActivity.this);
                finish();
            }
        });
    }

    private void setAlarm(int index) {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);

        intent.putExtra("index", index);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), index, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, _alarmHour);
        calendar.set(Calendar.MINUTE, _alarmMinute);
        calendar.set(Calendar.DAY_OF_MONTH, _alarmDay);
        calendar.set(Calendar.MONTH, _alarmMonth);
        calendar.set(Calendar.YEAR, _alarmYear);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
