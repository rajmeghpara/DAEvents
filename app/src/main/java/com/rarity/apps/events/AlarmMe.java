package com.rarity.apps.events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class AlarmMe extends Activity {
  private ListView mAlarmList;
  private AlarmListAdapter mAlarmListAdapter;
  private Alarm mCurrentAlarm;

  private final int NEW_ALARM_ACTIVITY = 0;
  private final int EDIT_ALARM_ACTIVITY = 1;

  private final int CONTEXT_MENU_EDIT = 0;
  private final int CONTEXT_MENU_DELETE = 1;
  private final int CONTEXT_MENU_DUPLICATE = 2;

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.alarm);

    mAlarmList = (ListView)findViewById(R.id.alarm_list);

    mAlarmListAdapter = new AlarmListAdapter(this);
    mAlarmList.setAdapter(mAlarmListAdapter);
    mAlarmList.setOnItemClickListener(mListOnItemClickListener);
    registerForContextMenu(mAlarmList);
    mCurrentAlarm = new Alarm();

    Intent i=getIntent();
    if(i!=null && i.hasExtra("id")){
      onActivityResult(i);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    mAlarmListAdapter.updateAlarms();
  }

  protected void onActivityResult(Intent data) {
      //  System.out.println(333333+" "+data.getStringExtra("title"));
        mCurrentAlarm.fromIntent(data);
        mAlarmListAdapter.add(mCurrentAlarm);

  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    if (v.getId() == R.id.alarm_list) {
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

      menu.setHeaderTitle(mAlarmListAdapter.getItem(info.position).getTitle());
      menu.add(Menu.NONE, CONTEXT_MENU_EDIT, Menu.NONE, "Edit");
      menu.add(Menu.NONE, CONTEXT_MENU_DELETE, Menu.NONE, "Delete");
      menu.add(Menu.NONE, CONTEXT_MENU_DUPLICATE, Menu.NONE, "Duplicate");
    }
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
    int index = item.getItemId();

    if (index == CONTEXT_MENU_EDIT) {
      Intent intent = new Intent(getBaseContext(), EditAlarm.class);

      mCurrentAlarm = mAlarmListAdapter.getItem(info.position);
      mCurrentAlarm.toIntent(intent);
      startActivityForResult(intent, EDIT_ALARM_ACTIVITY);
    }
    else if (index == CONTEXT_MENU_DELETE) {
      mAlarmListAdapter.delete(info.position);
    }
    else if (index == CONTEXT_MENU_DUPLICATE) {
      Alarm alarm = mAlarmListAdapter.getItem(info.position);
      Alarm newAlarm = new Alarm();
      Intent intent = new Intent();

      alarm.toIntent(intent);
      newAlarm.fromIntent(intent);
      newAlarm.setTitle(alarm.getTitle() + " (copy)");
      mAlarmListAdapter.add(newAlarm);
    }
    return true;
  }

  private AdapterView.OnItemClickListener mListOnItemClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      Intent intent = new Intent(getBaseContext(), EditAlarm.class);

      mCurrentAlarm = mAlarmListAdapter.getItem(position);
      mCurrentAlarm.toIntent(intent);
      AlarmMe.this.startActivityForResult(intent, EDIT_ALARM_ACTIVITY);
    }
  };

}

