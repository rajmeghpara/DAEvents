package com.rarity.apps.events;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.Random;

public class CustomAdapter extends FirebaseRecyclerAdapter<Event, CustomAdapter.CustomViewHolder>{

    DatabaseReference dbRef;
    private Context context;
    static int[] androidColors;

    private SharedPreferences sharedPreferences;

    public CustomAdapter(Class<Event> modelClass, int modelLayout, Class<CustomViewHolder> viewHolderClass, Query ref,Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        dbRef = ref.getRef();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        this.context=context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_object, parent, false);
        Context context1 = parent.getContext();
        androidColors = context1.getResources().getIntArray(R.array.androidcolors);
        return new CustomViewHolder(v);
    }

    @Override
    protected void populateViewHolder(CustomViewHolder viewHolder, Event event, int position) {
        String key=this.getRef(position).getKey();
        String s=sharedPreferences.getString(key,"");
        System.out.println(222222222+" "+key+" "+s);
        if(s.equals("")) {
            viewHolder.name.setText(event.getName());
            viewHolder.startDate.setText(event.getStartTime());
            viewHolder.endDate.setText(event.getEndTime());
            viewHolder.letter.setText(event.getName().charAt(0) + "");
            viewHolder.setEvent(event);
            viewHolder.setPosition(position);
            viewHolder.likes.setText(event.getLikes() + "");
            viewHolder.setVisibility(true);
        }else
            viewHolder.setVisibility(false);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{

        TextView name, startDate, endDate, letter, likes;
        ImageButton likeBtn;
        int position;
        Event event;
        String eventKey;
        SwipeRevealLayout layout;
        ImageButton reminderButton;
        public CustomViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            startDate = (TextView) view.findViewById(R.id.startDate);
            endDate = (TextView) view.findViewById(R.id.endDate);
            letter = (TextView) view.findViewById(R.id.letter);
            likeBtn = (ImageButton) view.findViewById(R.id.likeButton);
            likes = (TextView) view.findViewById(R.id.likes);
            reminderButton = (ImageButton) view.findViewById(R.id.reminderButton);
            eventKey = CustomAdapter.this.getRef(position).getKey();

            layout= (SwipeRevealLayout)view.findViewById(R.id.layout1);

            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
            view.findViewById(R.id.background).setBackgroundColor(randomAndroidColor);

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EventDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("event", event);
                    intent.putExtra("bundle", bundle);
                    v.getContext().startActivity(intent);
                }
            });

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    event.addLike();
                    likes.setText(event.getLikes()+"");
                    dbRef.child(eventKey).child("likes").setValue(event.getLikes());
                }
            });
            reminderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context,EditAlarm.class);
                    i.putExtra("Name",name.getText().toString());
                    context.startActivity(i);
                }
            });
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public void setPosition(int position) {
            this.position = position;
        }
        public void setVisibility(boolean isVisible){
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)layout.getLayoutParams();
            if (isVisible){
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                layout.setVisibility(View.VISIBLE);
            }else{
                layout.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            layout.setLayoutParams(param);
        }
    }
    public void removeItem(int position){
        String key=this.getRef(position).getKey();
        System.out.println(111111111+" "+key);
        sharedPreferences.edit().putString(key,"H").commit();
        notifyDataSetChanged();
    }

}
