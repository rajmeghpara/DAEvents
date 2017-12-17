package com.rarity.apps.events;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private EditText id, pass;
    private FloatingActionButton signup;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText) findViewById(R.id.id);
        pass = (EditText) findViewById(R.id.pass);
        signup = (FloatingActionButton) findViewById(R.id.signup);
        progress = (ProgressBar) findViewById(R.id.progress);

        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        Explode explode = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            explode = new Explode();
            explode.setDuration(500);

            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);
        }
    }


    public void login(View view) {

        progress.setVisibility(View.VISIBLE);
        String email = id.getText().toString() + "@daiict.ac.in";

        InputMethodManager imm = (InputMethodManager)this.getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(progress.getWindowToken(), 0);

        auth.signInWithEmailAndPassword(email, pass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    if(auth.getCurrentUser()!=null && !auth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "Please verify your email address:)", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(LoginActivity.this, "Logged in  successfully :)", Toast.LENGTH_LONG).show();

                    Query query = dbRef.child("Users").orderByChild("id").equalTo(id.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userKey = dataSnapshot.getChildren().iterator().next().getKey();
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            sharedPreferences.edit().putString("userKey", userKey).commit();

                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                            Intent i2 = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(i2, oc2.toBundle());
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                    Toast.makeText(LoginActivity.this, "Something is wrong :(", Toast.LENGTH_LONG).show();

                progress.setVisibility(View.GONE);
            }
        });

    }


    public void signup(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(null);
            getWindow().setExitTransition(null);

            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(this, signup, signup.getTransitionName());
            startActivity(new Intent(this, SignupActivity.class), options.toBundle());
        } else {
            startActivity(new Intent(this, SignupActivity.class));
        }
    }
}
