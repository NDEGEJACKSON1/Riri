package com.example.riri;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;


public class home_page extends AppCompatActivity {

    TabLayout tabLayout;
    SharedPreferences user_datas_file,user_settings;
    EditText title, descrption, time, date;
    MaterialToolbar toolbar;
    MaterialTextView toolbar_text;
    RecyclerView recyclerView;
    List<user_dataInfos> user_dataInfos;
    SwipeRefreshLayout swipeRefreshLayout;

    public void first_Fragment() {

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.user_data_infos);

        if (recyclerView == null || swipeRefreshLayout == null) {
            tabLayout.selectTab(tabLayout.getTabAt(1));
        } else {
            swipeRefreshLayout.setOnRefreshListener(() -> {
                first_Fragment();
                new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 1050);
            });
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setHapticFeedbackEnabled(true);

            // Reading  Data in shared Preference.....
            user_datas_file = getSharedPreferences("RiriUserDatas", Context.MODE_PRIVATE);

            // Creating Variables to access The User Edit Info
            // The idea is to keep the count the number of user values in the shared preference...
            int count = user_datas_file.getAll().size() / 4;

            // Appending these Results to a new Class.....................
            user_dataInfos = new ArrayList<>();

            for (int index = 0; index < count; index++) {

                user_dataInfos.add(
                        new user_dataInfos(
                                user_datas_file.getString(MessageFormat.format("user_titl_{0}", index), "invalid"),
                                user_datas_file.getString(MessageFormat.format("user_desc_{0}", index), "invalid"),
                                user_datas_file.getString(MessageFormat.format("user_time_{0}", index), "invalid"),
                                user_datas_file.getString(MessageFormat.format("user_date_{0}", index), "invalid")
                        )
                );
            }

            // Removing classes with inf....
            user_dataInfos.removeIf(com.example.riri.user_dataInfos::isOutOfTime);

            //Creating adapter that holds alarm information and stays withing recycler view
            user_dataAdapter user_dataAdapter = new user_dataAdapter(user_dataInfos);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(user_dataAdapter);
            update_indicator(user_dataInfos.size());

            new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 1050);

        }

    }


    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    public void remove_edit_item(String mode, int position, String title, String description, String time, String date) {

        user_dataInfos.remove(position);
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        update_indicator(user_dataInfos.size());

        SharedPreferences.Editor editor = user_datas_file.edit();
        editor.remove(MessageFormat.format("user_titl_{0}", position));
        editor.remove(MessageFormat.format("user_desc_{0}", position));
        editor.remove(MessageFormat.format("user_time_{0}", position));
        editor.remove(MessageFormat.format("user_date_{0}", position));
        editor.apply();

        if (mode.equals("edit")) {
            tabLayout.selectTab(tabLayout.getTabAt(1));
            toolbar_text.setText("Edit Reminder");

            this.title.setText(title);
            this.descrption.setText(description);
            this.time.setText(time);
            this.date.setText(date);

            Toast.makeText(this, "Reminder Edited", Toast.LENGTH_SHORT).show();

        } else Toast.makeText(this, "Reminder removed", Toast.LENGTH_SHORT).show();

    }
    //set date and time
    @SuppressLint("ResourceType")
    public void set_datetime(View view) {
        final Calendar calendar = Calendar.getInstance();
        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int current_minutes = calendar.get(Calendar.MINUTE);
        int current_year = calendar.get(Calendar.YEAR);
        int current_month = calendar.get(Calendar.MONTH);
        int current_day = calendar.get(Calendar.DAY_OF_MONTH);

        switch (view.getTag().toString()) {
            case "date_picker":
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, date) -> {
                    TextInputEditText date_or_time = findViewById(R.id.date_picker);
                    date_or_time.setText(MessageFormat.format("{0}/{1}/{2}", date, month + 1, String.valueOf(year)));
                }, current_year, current_month, current_day);

                datePickerDialog.show();
                break;

            case "time_picker":
                MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(user_settings.getString("time_format", "12").equals("12") ? TimeFormat.CLOCK_12H : TimeFormat.CLOCK_24H)
                        .setHour(current_hour)
                        .setMinute(current_minutes)
                        .setTitleText("Select Your Reminder Time")
                        .setInputMode(user_settings.getString("time_Style", "invalid").equals("time") ? 1 : 0)
                        .build();

                materialTimePicker.addOnPositiveButtonClickListener(view1 -> {
                    TextInputEditText time_or_date = findViewById(R.id.time_picker);
                    time_or_date.setText(MessageFormat.format("{0}:{1}", materialTimePicker.getHour(), materialTimePicker.getMinute()));
                });
                materialTimePicker.show(getSupportFragmentManager(), "Riri");
                break;
        }


    }
    //get field entries
    @SuppressLint({"MutatingSharedPrefs", "SimpleDateFormat", "ShortAlarm"})
    public void get_userData(View view) throws ParseException {

        title = findViewById(R.id.title);
        descrption = findViewById(R.id.description);
        time = findViewById(R.id.time_picker);
        date = findViewById(R.id.date_picker);

        if (title.getText().toString().isEmpty() || descrption.getText().toString().isEmpty() || date.getText().toString().isEmpty()) {
            new MaterialAlertDialogBuilder(home_page.this).setTitle("Reminder Alert")
                    .setMessage("Please fill All The Fields")
                    .setPositiveButton("Got It", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        } else {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            final long due_date_time = (Objects.requireNonNull(simpleDateFormat.parse(date.getText().toString() + " " + time.getText().toString()))).getTime();
            final long current_date_time = Calendar.getInstance().getTimeInMillis();

            final long remained_time = due_date_time - current_date_time - (10792707 + 7000);

            System.out.println(simpleDateFormat.getCalendar());

            if (remained_time <= 0) {
                new MaterialAlertDialogBuilder(home_page.this).setTitle("Back Time Travelling")
                        .setMessage("The Reminder can not be Set from past dates")
                        .setPositiveButton("Got It", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            } else {
                // Checking if the Date is set In the Past......
                SharedPreferences.Editor user_editor = user_datas_file.edit();

                // Creating Variables to access The User Edit Info
                // The idea is to keep the count the number of user values in the shared preference...
                int count = user_datas_file.getAll().size() / 4;

                // Appending New Values to the Editor...............................
                user_editor.putString(MessageFormat.format("user_titl_{0}", count), title.getText().toString());
                user_editor.putString(MessageFormat.format("user_desc_{0}", count), descrption.getText().toString());
                user_editor.putString(MessageFormat.format("user_time_{0}", count), time.getText().toString());
                user_editor.putString(MessageFormat.format("user_date_{0}", count), date.getText().toString());
                user_editor.apply();

                // This Code is To initialize Alarm Mananger....
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(this, NotificationReceiver.class);

                intent.putExtra("title",user_datas_file.getString(MessageFormat.format("user_titl_{0}", count),"invalid"));
                intent.putExtra("desc",user_datas_file.getString(MessageFormat.format("user_desc_{0}", count),"invalid"));
                intent.putExtra("time",user_datas_file.getString(MessageFormat.format("user_time_{0}", count),"invalid"));

                @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(this,new Random().nextInt(10000), intent, 0);

                alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + remained_time - 1500 ,pendingIntent);

                Toast.makeText(this, "Alarm Manager Has been Set", Toast.LENGTH_LONG).show();

                first_Fragment();
                tabLayout.selectTab(tabLayout.getTabAt(0));
            }

        }
    }
    //update indicator tab numbers
    private void update_indicator(int counter) {
        // Creating the First Fragments.................
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        Objects.requireNonNull(tab).getOrCreateBadge().setNumber(counter);
        tab.getOrCreateBadge().setBackgroundColor(Color.parseColor("white"));
        tab.getOrCreateBadge().setBadgeTextColor(Color.parseColor("black"));
    }
    //initialize view pager
    private void initiate_fragments() {

        // TabLayout For Grouping tabs
        tabLayout = findViewById(R.id.tab_layout);
        toolbar_text = findViewById(R.id.toolbar_text);

        // For Sliding between Fragments
        ViewPager page_slider = findViewById(R.id.slider_page);

        // this is the button for clearing userdata
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu);

        //clear all functionality
        toolbar.setOnMenuItemClickListener(item -> {
            user_datas_file.edit().clear().apply();
            first_Fragment();
            return false;
        });

        // This Fragment container slider holds all fragments
        fragment_container_slider fragment_container_slider = new fragment_container_slider(getSupportFragmentManager(), tabLayout.getTabCount());

        // then they are added to view pager (pageslider)
        page_slider.setAdapter(fragment_container_slider);

        // these were functions for sliding purpose
        page_slider.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                page_slider.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    toolbar_text.setText("Reminders");
                    first_Fragment();
                    toolbar.getMenu().getItem(0).setVisible(true);
                } else if (tab.getPosition() == 1) {
                    toolbar_text.setText("New Reminder");
                    toolbar.getMenu().getItem(0).setVisible(false);
                } else {
                    toolbar_text.setText("Settings");
                    toolbar.getMenu().getItem(0).setVisible(false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // initializing the first Tab by Calling...........
        tabLayout.selectTab(tabLayout.getTabAt(1));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_layout);

        // For Saving Data and reading in shared Preference.....
        user_datas_file = getSharedPreferences("RiriUserDatas", Context.MODE_PRIVATE);
        user_settings = PreferenceManager.getDefaultSharedPreferences(this);

        System.out.println(user_settings.getAll());
        initiate_fragments();

    }


}