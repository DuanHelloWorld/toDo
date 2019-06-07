package com.example.todo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.*;

public class EditActivity extends AppCompatActivity {

    private EditText         editTitle;
    private EditText         editContent;
    private Button           lessImportant;
    private Button           important;
    private Button           veryImportant;
    private Switch           alarmSwitch;
    private TimePickerView   pvCustomTime;
    private TextView         dateText;
    private TextView         timeText;
    private Intent           intent;
    private ConstraintLayout timeLayout;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        Button                 checkButton  = findViewById(R.id.check_Button);
        Button                 backButton   = findViewById(R.id.back_Button);
        ConstraintLayout       date         = findViewById(R.id.date_Layout);
        final ConstraintLayout time         = findViewById(R.id.time_ConstraintLayout);


        editTitle     = findViewById(R.id.editTitle_EditText);
        editContent   = findViewById(R.id.editContent_EditText);
        lessImportant = findViewById(R.id.lessImportant_Button);
        important     = findViewById(R.id.important_Button);
        veryImportant = findViewById(R.id.veryImportant_Button);
        alarmSwitch   = findViewById(R.id.alarm_switch);
        dateText      = findViewById(R.id.date_Text);
        timeText      = findViewById(R.id.time_Text);
        timeLayout    = findViewById(R.id.time_ConstraintLayout);
        intent        = getIntent();



        if (intent.getIntExtra("type", 1) == 2) {
            editTitle.setText(intent.getStringExtra("title"));
            editContent.setText(intent.getStringExtra("content"));
            switch (intent.getIntExtra("importance", 0)) {
                case 0:
                    lessImportant.setActivated(true);
                    break;
                case 1:
                    important.setActivated(true);
                case 2:
                    veryImportant.setActivated(true);
                    default:
                        break;
            }
            if (intent.getIntExtra("year", 0) != 0) {
                alarmSwitch.setChecked(true);
                timeLayout.setVisibility(ConstraintLayout.VISIBLE);

                String dateFormat = String.format(Locale.CHINA,"%d-%d-%d",
                        intent.getIntExtra("year", 0),
                        intent.getIntExtra("month", 0),
                        intent.getIntExtra("day",0));

                dateText.setText(dateFormat);

                String timeFormat = String.format(Locale.CHINA, "%d:%d:%d",
                        intent.getIntExtra("hour", 0),
                        intent.getIntExtra("minute", 0),
                        intent.getIntExtra("second", 0));

                timeText.setText(timeFormat);
            }
        }

        lessImportant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessImportant.setActivated(true);
                important.setActivated(false);
                veryImportant.setActivated(false);

                lessImportant.setBackgroundColor(getResources().getColor(R.color.less_important_button_activity));
                important.setBackgroundColor(getResources().getColor(R.color.important_button_not_activity));
                veryImportant.setBackgroundColor(getResources().getColor(R.color.very_important_button_not_activity));
            }
        });

        important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessImportant.setActivated(false);
                important.setActivated(true);
                veryImportant.setActivated(false);

                lessImportant.setBackgroundColor(getResources().getColor(R.color.less_important_button_not_activity));
                important.setBackgroundColor(getResources().getColor(R.color.important_button_activity));
                veryImportant.setBackgroundColor(getResources().getColor(R.color.very_important_button_not_activity));
            }
        });

        veryImportant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessImportant.setActivated(false);
                important.setActivated(false);
                veryImportant.setActivated(true);

                lessImportant.setBackgroundColor(getResources().getColor(R.color.less_important_button_not_activity));
                important.setBackgroundColor(getResources().getColor(R.color.important_button_not_activity));
                veryImportant.setBackgroundColor(getResources().getColor(R.color.very_important_button_activity));
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView pvTime = new TimePickerBuilder(EditActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        dateText.setText(getTime(date, 1));
                    }
                }).build();

                pvTime.setDate(Calendar.getInstance());
                pvTime.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar selectedDate = Calendar.getInstance();//系统当前时间
                Calendar startDate = Calendar.getInstance();

                Calendar endDate = Calendar.getInstance();
                endDate.set(2027, 2, 28);
                //时间选择器 ，自定义布局
                pvCustomTime = new TimePickerBuilder(EditActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        timeText.setText(getTime(date, 2));
                    }
                })
                        .setDate(selectedDate)
                        .setRangDate(startDate, endDate)
                        .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                            @Override
                            public void customLayout(View v) {
                                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                                ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                                tvSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pvCustomTime.returnData();
                                        pvCustomTime.dismiss();
                                    }
                                });
                                ivCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pvCustomTime.dismiss();
                                    }
                                });
                            }
                        })
                        .setContentTextSize(18)
                        .setType(new boolean[]{false, false, false, true, true, true})
                        .setLabel("年", "月", "日", "时", "分", "秒")
                        .setLineSpacingMultiplier(1.2f)
                        .setTextXOffset(0, 0, 0, 40, 0, -40)
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .setDividerColor(0xFF24AD9D)
                        .build();

                pvCustomTime.show();

            }

        });

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    timeLayout.setVisibility(ConstraintLayout.VISIBLE);
                } else {
                    timeLayout.setVisibility(ConstraintLayout.INVISIBLE);
                }
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);

                builder.setTitle("保存事项");
                builder.setMessage("是否保存事项？");
                builder.setPositiveButton("确定保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title   = editTitle.getText().toString();
                        String content = editContent.getText().toString();
                        int    year    = 0;
                        int    month   = 0;
                        int    day     = 0;
                        int    hour    = 0;
                        int    minute  = 0;
                        int    second  = 0;
                        int    importance;

                        if (lessImportant.isActivated()) {
                            importance = 0;
                        } else if (important.isActivated()) {
                            importance = 1;
                        } else {
                            importance = 2;
                        }

                        if (alarmSwitch.isChecked()) {
                            StringTokenizer stringTokenizer = new StringTokenizer(dateText.getText().toString(), "-");
                            year  = Integer.parseInt(stringTokenizer.nextToken());
                            month = Integer.parseInt(stringTokenizer.nextToken());
                            day   = Integer.parseInt(stringTokenizer.nextToken());

                            stringTokenizer = new StringTokenizer(timeText.getText().toString(), ":");
                            hour   = Integer.parseInt(stringTokenizer.nextToken());
                            minute = Integer.parseInt(stringTokenizer.nextToken());
                            second = Integer.parseInt(stringTokenizer.nextToken());

                            Calendar date = Calendar.getInstance();
                            date.set(year, month, day, hour, minute, second);

                            Calendar now = Calendar.getInstance();
                            if (date.before(System.currentTimeMillis())) {
                                Toast.makeText(EditActivity.this,"hh", Toast.LENGTH_LONG).show();
                                AlertDialog.Builder alert = new AlertDialog.Builder(EditActivity.this)
                                        .setTitle("时间错误")
                                        .setMessage("设置的时间必须大于当前时间")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {}
                                        });
                            }
                        }

                        int type = intent.getIntExtra("type", 0);
                        int id   = intent.getIntExtra("id", 0);


                        editMatter(title, content, year, month, day, hour, minute, second, importance, type, id);
                        finish();
                    }
                });
                builder.setNegativeButton("继续编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                builder.show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this)
                        .setTitle("退出页面")
                        .setMessage("事项还没有保存，是否退出当前页面？")
                        .setPositiveButton("退出页面", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("继续编辑", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
            }
        });
    }


    private void editMatter(String title,
                            String content,
                            int    year,
                            int    month,
                            int    day,
                            int    hour,
                            int    minute,
                            int    second,
                            int    importance,
                            int    type,
                            int    id) {


        Matter matter = new Matter();
        if (type == 1) {

            matter.setTitle(title);
            matter.setContent(content);
            matter.setYear(year);
            matter.setMonth(month);
            matter.setDay(day);
            matter.setHour(hour);
            matter.setMinute(minute);
            matter.setSecond(second);
            matter.setImportance(importance);
            matter.save();
        } else if (type == 2){

            matter = LitePal.find(Matter.class, id);

            matter.setTitle(title);
            matter.setContent(content);
            matter.setYear(year);
            matter.setMonth(month);
            matter.setDay(day);
            matter.setHour(hour);
            matter.setMinute(minute);
            matter.setSecond(second);
            matter.setImportance(importance);
            matter.save();
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Calendar date = Calendar.getInstance();
        date.set(matter.getYear(), matter.getMonth(), matter.getDay(),
                 matter.getHour(), matter.getMinute(), matter.getSecond());

        Notification noti = new Notification.Builder(this)
                .setContentTitle(matter.getTitle())
                .setContentText(matter.getContent())
                .setSmallIcon(R.mipmap.checklist)
                .setWhen(date.getTimeInMillis())
                .build();

        notificationManager.notify(matter.getId(), noti);
    }

    private String getTime(Date date, int type) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format;
        if (type == 1) {
            format = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            format = new SimpleDateFormat("HH:mm:ss");
        }
        return format.format(date);
    }
}