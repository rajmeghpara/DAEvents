package com.rarity.apps.events;

import android.content.Intent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Alarm implements Comparable<Alarm> {
  private long mId;
  private String mTitle;
  private long mDate;
  private boolean mEnabled;
  private long mNextOccurence=1;

  public Alarm() {
    mId = 0;
    mTitle = "";
    mDate = System.currentTimeMillis();
    mEnabled = true;
    update();
  }

  public long getId()
  {
    return mId;
  }

  public void setId(long id)
  {
    mId = id;
  }

  public String getTitle()
  {
    return mTitle;
  }

  public void setTitle(String title)
  {
    mTitle = title;
  }

  public long getDate()
  {
    return mDate;
  }

  public void setDate(long date) {
    mDate = date;
    update();
  }

  public boolean getEnabled()
  {
    return mEnabled;
  }

  public void setEnabled(boolean enabled)
  {
    mEnabled = enabled;
  }

  public long getNextOccurence() {
    return mNextOccurence;
  }

  public boolean getOutdated()
  {
    return mNextOccurence < System.currentTimeMillis();
  }

  public int compareTo(Alarm another) {
    final long thisNext = getNextOccurence();
    final long anotherNext = another.getNextOccurence();

    if (this == another)
      return 0;

    return (int)(anotherNext-thisNext);
  }

  public void update() {
    mNextOccurence = mDate;
  }

  public void toIntent(Intent intent)
  {
    intent.putExtra("id", mId);
    intent.putExtra("title", mTitle);
    intent.putExtra("date", mDate);
    intent.putExtra("alarm", mEnabled);
  }

  public void fromIntent(Intent intent)
  {
    mId = intent.getLongExtra("id", 0);
    mTitle = intent.getStringExtra("title");
    mDate = intent.getLongExtra("date", 0);
    mEnabled = intent.getBooleanExtra("alarm", true);
    update();
  }
  public void add(DataOutputStream dos) throws IOException {
    dos.writeLong(mId);
    dos.writeUTF(mTitle);
    dos.writeLong(mDate);
    dos.writeBoolean(mEnabled);
  }

  public void retrieve(DataInputStream dis) throws IOException {
    mId = dis.readLong();
    mTitle = dis.readUTF();
    mDate = dis.readLong();
    mEnabled = dis.readBoolean();
    update();
  }
}

