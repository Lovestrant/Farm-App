package com.svs.farm_app.main.calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.OfficerTraining;
import com.svs.farm_app.main.calendar.decorator.EventDecorator;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.Preferences;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalenderActivity extends BaseClass implements OnDateSelectedListener {

    private static final String TAG = CalenderActivity.class.getSimpleName();

    private DatabaseHandler db;
    @BindView(R.id.cvCalendar)
    public MaterialCalendarView widget;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CalenderActivity mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = CalenderActivity.this;

        initData();

        initListeners();
    }

    private void initData() {
        db = new DatabaseHandler(mContext);

        ArrayList<CalendarDay> datesList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        List<OfficerTraining> trainingList = db.getOfficerTrainingByUserId(Preferences.USER_ID);

        for (OfficerTraining training : trainingList) {

            String[] dateParts = training.getTrainDate().split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) -1;
            int day = Integer.parseInt(dateParts[2]);

            calendar.set(year, month, day);
            CalendarDay calendarDay = CalendarDay.from(calendar);
            datesList.add(calendarDay);
        }

        widget.addDecorator(new EventDecorator(Color.RED, datesList));
    }

    private void initListeners() {
        widget.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                NumberFormat numberFormat = new DecimalFormat("00");

                String stringDate = date.getYear()+"-"+numberFormat.format(date.getMonth()+1)+"-"+numberFormat.format(date.getDay());

                Intent intent = new Intent(mContext, MyTrainingsActivity.class);
                intent.putExtra("date", stringDate);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
    }
}
