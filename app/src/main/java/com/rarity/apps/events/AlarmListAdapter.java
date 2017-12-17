package com.rarity.apps.events;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class AlarmListAdapter extends BaseAdapter {

  private Context mContext;
  private DataSource mDataSource;
  private LayoutInflater mInflater;
  private DateTime mDateTime;
  private int mColorOutdated;
  private int mColorActive;
  private AlarmManager mAlarmManager; 

  public AlarmListAdapter(Context context) {
    mContext = context;
    mDataSource = DataSource.getInstance(context);

    mInflater = LayoutInflater.from(context);
    mDateTime = new DateTime(context);

    mColorOutdated = Color.GRAY;
    mColorActive = Color.GREEN;

    mAlarmManager = (AlarmManager)context.getSystemService(mContext.ALARM_SERVICE);

    dataSetChanged();
  }

  public void update(Alarm alarm)
  {
    mDataSource.update(alarm);
    dataSetChanged();
  }

  public void updateAlarms() {
    for (int i = 0; i < mDataSource.size(); i++)
      mDataSource.update(mDataSource.get(i));
    dataSetChanged();
  }

  public void add(Alarm alarm) {
    mDataSource.add(alarm);
    dataSetChanged();
  }

  public void delete(int index)
  {
    cancelAlarm(mDataSource.get(index));
    mDataSource.remove(index);
    dataSetChanged();
  }

  public void onSettingsUpdated() {
    mDateTime.update();
    dataSetChanged();
  }

  public int getCount() {
    return mDataSource.size();
  }

  public Alarm getItem(int position) {
    return mDataSource.get(position);
  }

  public long getItemId(int position) {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    Alarm alarm = mDataSource.get(position);

    if (convertView == null) {
      convertView = mInflater.inflate(R.layout.list_item, null);

      holder = new ViewHolder();
      holder.title = (TextView)convertView.findViewById(R.id.item_title);
      holder.details = (TextView)convertView.findViewById(R.id.item_details);
      convertView.setTag(holder);
    }
    else
      holder = (ViewHolder)convertView.getTag();
  
    holder.title.setText(alarm.getTitle());
    holder.details.setText(mDateTime.formatDetails(alarm) + (alarm.getEnabled() ? "" : " [disabled]"));

    if (alarm.getOutdated())
      holder.title.setTextColor(mColorOutdated);
    else
      holder.title.setTextColor(mColorActive);

    return convertView;
  }

  private void dataSetChanged() {
    for (int i = 0; i < mDataSource.size(); i++)
      setAlarm(mDataSource.get(i));
    notifyDataSetChanged();
  }

  private void setAlarm(Alarm alarm) {
    PendingIntent sender;
    Intent intent;

    if (alarm.getEnabled() && !alarm.getOutdated()) {
      intent = new Intent(mContext, AlarmReceiver.class);
      alarm.toIntent(intent);
      sender = PendingIntent.getBroadcast(mContext, (int)alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
      mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getDate(), sender);
    }
  }

  private void cancelAlarm(Alarm alarm) {
    PendingIntent sender;
    Intent intent;

    intent = new Intent(mContext, AlarmReceiver.class);
    sender = PendingIntent.getBroadcast(mContext, (int)alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    mAlarmManager.cancel(sender);
  }

  static class ViewHolder{
    TextView title;
    TextView details;
  }
}

