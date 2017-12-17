package com.rarity.apps.events;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditAlarm extends Activity {
  private TextView mTitle;
  private CheckBox mAlarmEnabled;
  private Button mDateButton;
  private Button mTimeButton;

  private Alarm mAlarm;
  private DateTime mDateTime;

  private GregorianCalendar mCalendar;
  private int mYear;
  private int mMonth;
  private int mDay;
  private int mHour;
  private int mMinute;

  static final int DATE_DIALOG_ID = 0;
  static final int TIME_DIALOG_ID = 1;

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.edit);
    mAlarm = new Alarm();
    mTitle = (TextView)findViewById(R.id.title);
    mAlarmEnabled = (CheckBox)findViewById(R.id.alarm_checkbox);
    mDateButton = (Button)findViewById(R.id.date_button);
    mTimeButton = (Button)findViewById(R.id.time_button);

    mDateTime = new DateTime(this);

    mTitle.setText(mAlarm.getTitle());
    mTitle.addTextChangedListener(mTitleChangedListener);

    mAlarmEnabled.setChecked(mAlarm.getEnabled());
    mAlarmEnabled.setOnCheckedChangeListener(mAlarmEnabledChangeListener); 

    mCalendar = new GregorianCalendar();
    mCalendar.setTimeInMillis(mAlarm.getDate());
    mYear = mCalendar.get(Calendar.YEAR);
    mMonth = mCalendar.get(Calendar.MONTH);
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
    mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
    mMinute = mCalendar.get(Calendar.MINUTE);
    Intent i=getIntent();
    if(i!=null && i.hasExtra("Name"))
      mTitle.setText(i.getStringExtra("Name"));
    updateButtons();
  }

  @Override
  protected Dialog onCreateDialog(int id) {
    if (DATE_DIALOG_ID == id)
      return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
    else if (TIME_DIALOG_ID == id)
      return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, mDateTime.is24hClock());
    else
      return null;
  }

  @Override
  protected void onPrepareDialog(int id, Dialog dialog)
  {
    if (DATE_DIALOG_ID == id)
      ((DatePickerDialog)dialog).updateDate(mYear, mMonth, mDay);
    else if (TIME_DIALOG_ID == id)
      ((TimePickerDialog)dialog).updateTime(mHour, mMinute);
  }    

  public void onDateClick(View view) {
      showDialog(DATE_DIALOG_ID);
  }

  public void onTimeClick(View view)
  {
    showDialog(TIME_DIALOG_ID);
  }

  public void onDoneClick(View view) {
    Intent intent = new Intent(this,AlarmMe.class);
    mAlarm.toIntent(intent);
    startActivity(intent);
    finish();
  }

  public void onCancelClick(View view) {
    setResult(RESULT_CANCELED, null);  
    finish();
  }

  private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
  {
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
      mYear = year;
      mMonth = monthOfYear;
      mDay = dayOfMonth;

      mCalendar = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
      mAlarm.setDate(mCalendar.getTimeInMillis());

      updateButtons();
    }
  };

  private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener()
  {
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
      mHour = hourOfDay;
      mMinute = minute;

      mCalendar = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
      mAlarm.setDate(mCalendar.getTimeInMillis());

      updateButtons();
    }
  };

  private TextWatcher mTitleChangedListener = new TextWatcher()
  {
    public void afterTextChanged(Editable s)
    {
      mAlarm.setTitle(mTitle.getText().toString());
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after){}

    public void onTextChanged(CharSequence s, int start, int before, int count){}
  };

  private CompoundButton.OnCheckedChangeListener mAlarmEnabledChangeListener = new CompoundButton.OnCheckedChangeListener()
  {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
      mAlarm.setEnabled(isChecked);
    }
  };

  private void updateButtons() {
    mDateButton.setText(mDateTime.formatDate(mAlarm));
    mTimeButton.setText(mDateTime.formatTime(mAlarm));
  }
}

