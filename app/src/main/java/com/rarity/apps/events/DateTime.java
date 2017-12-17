package com.rarity.apps.events;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTime {
  private Context mContext;
  private String[] mFullDayNames;
  private String[] mShortDayNames;
  private boolean mWeekStartsOnMonday;
  private boolean m24hClock;
  private SimpleDateFormat mTimeFormat;
  private SimpleDateFormat mDateFormat;

  public DateTime(Context context) {
    mContext = context;
    update();
  }

  public void update() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    mWeekStartsOnMonday = prefs.getBoolean("week_starts_pref", false);
    m24hClock = prefs.getBoolean("use_24h_pref", false);

    mDateFormat = new SimpleDateFormat("E MMM d, yyyy");

    if (m24hClock)
      mTimeFormat = new SimpleDateFormat("H:mm");
    else
      mTimeFormat = new SimpleDateFormat("h:mm a");

    mFullDayNames = new String[7];
    mShortDayNames = new String[7];

    SimpleDateFormat fullFormat = new SimpleDateFormat("EEEE");
    SimpleDateFormat shortFormat = new SimpleDateFormat("E");
    Calendar calendar;

    if (mWeekStartsOnMonday)
      calendar = new GregorianCalendar(2012, Calendar.AUGUST, 6);
    else
      calendar = new GregorianCalendar(2012, Calendar.AUGUST, 5);

    for (int i = 0; i < 7; i++)
    {
      mFullDayNames[i] = fullFormat.format(calendar.getTime());
      mShortDayNames[i] = shortFormat.format(calendar.getTime());
      calendar.add(Calendar.DAY_OF_WEEK, 1);
    }
  }

  public boolean is24hClock()
  {
    return m24hClock;
  }

  public String formatTime(Alarm alarm) {
    return mTimeFormat.format(new Date(alarm.getDate()));
  }

  public String formatDate(Alarm alarm) {
    return mDateFormat.format(new Date(alarm.getDate()));
  }

  public String formatDetails(Alarm alarm) {
    String res = formatDate(alarm);
    res += ", " + formatTime(alarm);

    return res;
  }


  public String[] getFullDayNames()
  {
    return mFullDayNames;
  }
}

