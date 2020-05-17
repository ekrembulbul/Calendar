package com.example.donemodev;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context _context;
    private EventList eventList;

    public EventAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        _context = context;
        eventList = EventList.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_view_main, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyEvent selectedEvent = eventList.data.get(position);
        holder.setData(selectedEvent);
        holder.setListener(position);
    }



    @Override
    public int getItemCount() {
        return eventList.data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final static int EDIT_EVENT_INTENT_REQUEST_CODE = 101;

        CardView cardView;
        TextView name;
        TextView startTimeDate;
        TextView endTimeDate;
        TextView alarmTimeDate;
        TextView place;
        TextView description;
        TextView frequency;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            name = itemView.findViewById(R.id.name_item);
            startTimeDate = itemView.findViewById(R.id.start_time_date_item);
            endTimeDate = itemView.findViewById(R.id.end_time_date_item);
            alarmTimeDate = itemView.findViewById(R.id.alarm_time_date_item);
            place = itemView.findViewById(R.id.place_item);
            description = itemView.findViewById(R.id.description_item);
            frequency = itemView.findViewById(R.id.frequency_item);
        }

        public void setData(MyEvent selectedEvent) {
            name.setText(selectedEvent.name);

            startTimeDate.setText("Start: " + selectedEvent.startDay + "/" + selectedEvent.startMonth + "/" + selectedEvent.startYear + " " + selectedEvent.startHour + ":" + selectedEvent.startMinute);
            if (selectedEvent.startDay == -1 || selectedEvent.startHour == -1) startTimeDate.setText("Start: ");

            endTimeDate.setText("End: " + selectedEvent.endDay + "/" + selectedEvent.endMonth + "/" + selectedEvent.endYear + " " + selectedEvent.endHour + ":" + selectedEvent.endMinute);
            if (selectedEvent.endDay == -1 || selectedEvent.endHour == -1) endTimeDate.setText("End: ");

            alarmTimeDate.setText("Alarm: " + selectedEvent.alarmDay + "/" + selectedEvent.alarmMonth + "/" + selectedEvent.alarmYear + " " + selectedEvent.alarmHour + ":" + selectedEvent.alarmMinute);
            if (selectedEvent.alarmDay == -1 || selectedEvent.alarmHour == -1) alarmTimeDate.setText("Alarm: ");

            place.setText("Place: " + selectedEvent.place);

            description.setText("Description: " + selectedEvent.description);

            String[] frequecyList = _context.getResources().getStringArray(R.array.string_recall_frequency);
            frequency.setText(frequecyList[selectedEvent.frequencyId]);
        }

        public void setListener(final int position) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    popUpMenu(v, position);
                    return false;
                }
            });
        }

        private void popUpMenu(View v, final int index) {
            PopupMenu popup = new PopupMenu(_context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.edit_menu:
                            editItem(index);
                            return true;
                        case R.id.delete_menu:
                            deleteItem(index);
                            return true;
                    }
                    return false;
                }
            });

            popup.show();
        }

        private void editItem(int index) {
            Intent intent = new Intent(_context, AddEventActivity.class);
            intent.putExtra("type", "edit");
            intent.putExtra("index", index);
            ((Activity)_context).startActivityForResult(intent, EDIT_EVENT_INTENT_REQUEST_CODE);
        }

        private void deleteItem(int index) {
            EventList.getInstance().data.remove(index);
            EventList.writeData(_context);
            notifyItemRemoved(index);

            AlarmManager alarmManager = (AlarmManager) _context.getApplicationContext().getSystemService(_context.getApplicationContext().ALARM_SERVICE);
            Intent intent = new Intent(_context.getApplicationContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(_context.getApplicationContext(), index, intent, 0);

            alarmManager.cancel(pendingIntent);
        }
    }
}
