package com.example.studentnotifyapp.Student;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.studentnotifyapp.BaseFragment;
import com.example.studentnotifyapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class ReminderFragment extends BaseFragment {

    private FloatingActionButton add;
    private Dialog dialog;
    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private AdapterReminders adapter;
    private List<Reminders> temp;
    private TextView empty;
    private ArrayList<Integer> parallelArrayOf_Ids;

    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

//        Toolbar toolbar1 = findViewById(R.id.ReminderToolbar1);
//        setSupportActionBar(toolbar1);

        appDatabase = AppDatabase.geAppdatabase(getContext());

        add = view.findViewById(R.id.floatingButton);
        empty = view.findViewById(R.id.empty);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReminder();
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        setItemsInRecyclerView();

        return view;
    }
    public void addReminder(){

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.floating_popup);

        final TextView textView = dialog.findViewById(R.id.date);
        Button select,add,cancel;
        select = dialog.findViewById(R.id.selectDate);
        add = dialog.findViewById(R.id.addButton);
        cancel = dialog.findViewById(R.id.cancelButton);
        final EditText message = dialog.findViewById(R.id.reminderMessage);


        final Calendar newCalender = Calendar.getInstance();
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                        final Calendar newDate = Calendar.getInstance();
                        Calendar newTime = Calendar.getInstance();
                        TimePickerDialog time = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                newDate.set(year,month,dayOfMonth,hourOfDay,minute,0);
                                Calendar tem = Calendar.getInstance();

                                if((newDate.getTimeInMillis()-System.currentTimeMillis())>0)
                                {
                                    textView.setText(newDate.getTime().toString());
                                }

                                else
                                    Toast.makeText(getContext(),"Invalid time",Toast.LENGTH_SHORT).show();

                            }
                        },newTime.get(Calendar.HOUR_OF_DAY),newTime.get(Calendar.MINUTE),false);
                        time.show();

                    }
                },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH),newCalender.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RoomDAO roomDAO = appDatabase.getRoomDAO();
                Reminders reminders = new Reminders();
                reminders.setMessage(message.getText().toString().trim());

                Date remind;
                try {
                    SimpleDateFormat format =
                            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                    remind = format.parse(textView.getText().toString().trim());
                }
                catch(ParseException pe) {
                    Toast.makeText(getContext(), "mainPage error line 133", Toast.LENGTH_SHORT).show();
                    throw new IllegalArgumentException(pe);
                }



                reminders.setRemindDate(remind);
                roomDAO.Insert(reminders);
                List<Reminders> l = roomDAO.getAll();
                reminders = l.get(l.size()-1);
                Log.e("ID required",reminders.getId()+"");

                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:45"));
                calendar.setTime(remind);
                calendar.set(Calendar.SECOND,0);
                Intent intent = new Intent(getContext(),NotifierAlarm.class);
                intent.putExtra("Message",reminders.getMessage());
                intent.putExtra("RemindDate",reminders.getRemindDate().toString());
                intent.putExtra("id",reminders.getId());
                PendingIntent intent1 = PendingIntent.getBroadcast(getContext(),reminders.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),intent1);
                }

                Toast.makeText(getContext(),"Inserted Successfully",Toast.LENGTH_SHORT).show();
                setItemsInRecyclerView();
                AppDatabase.destroyInstance();
                dialog.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void setItemsInRecyclerView(){

        RoomDAO dao = appDatabase.getRoomDAO();
        temp = dao.orderThetable();
        if(temp.size()>0) {
            empty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        parallelArrayOf_Ids = new ArrayList<>();
        for(int i = 0; i < temp.size();i++)
            parallelArrayOf_Ids.add(temp.get(i).getId());

        adapter = new AdapterReminders(temp);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterReminders.onItemClickListener() {
            @Override
            public void onDeleteClick(int position) {

                //removing alarm
                alarmRemover(parallelArrayOf_Ids.get(position));
                //removing from data base
                AppDatabase appDatabase2 = AppDatabase.geAppdatabase(getContext());
                final RoomDAO dao1 = appDatabase2.getRoomDAO();
                Reminders reminder = dao1.getObjectUsingID(parallelArrayOf_Ids.get(position));
                dao1.Delete(reminder);
                AppDatabase.destroyInstance();
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                setItemsInRecyclerView();
            }
        });


    }


    public void alarmRemover(int id){
        Intent intent = new Intent(getContext(),NotifierAlarm.class);
        PendingIntent intent1 = PendingIntent.getBroadcast(getContext(),id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        alarmManager.cancel(intent1);
    }
}