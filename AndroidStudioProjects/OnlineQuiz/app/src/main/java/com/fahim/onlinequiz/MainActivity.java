package com.fahim.onlinequiz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.lang.UScript;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.fahim.onlinequiz.BroadcastReciever.AlarmReciever;
import com.fahim.onlinequiz.Common.Common;
import com.fahim.onlinequiz.Model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity{

    MaterialEditText editTextUsername, editTextPassword, editTextEmail;
    MaterialEditText loginUsername, loginpassword;
    Button buttonSignUp, buttonSignIn;
/*
    SignInButton signInButton;
    GoogleSignInOptions signInOptions;
    GoogleApiClient googleApiClient;
*/
    FirebaseDatabase database;
    DatabaseReference users;
    String googleEmailId = "";

    private static final int REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);
       /* googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        */
       registerAlarm();

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        loginUsername = (MaterialEditText) findViewById(R.id.edituser);
        loginpassword = (MaterialEditText) findViewById(R.id.edituserpass);

        buttonSignIn = (Button) findViewById(R.id.btn_sign_in);
        buttonSignUp = (Button) findViewById(R.id.btn_sign_up);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignupDialog();
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(loginUsername.getText().toString(), loginpassword.getText().toString());
            }
        });
    }

    private void registerAlarm() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getApplicationContext(), AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


    }

    private void signIn(final String username, final String password) {

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(username).exists()) {
                    if (!username.isEmpty()) {
                        User login = dataSnapshot.child(username).getValue(User.class);
                        if (login.getPassword().equals(password)) {
                            Common.currentUser = login;
                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Please Enter your username", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "User is not Valid ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void showSignupDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign up");
        alertDialog.setMessage("please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View signup_layout = inflater.inflate(R.layout.signup_layout, null);

        editTextEmail = (MaterialEditText) signup_layout.findViewById(R.id.editEmail);
        editTextUsername = (MaterialEditText) signup_layout.findViewById(R.id.editUsername);
        editTextPassword = (MaterialEditText) signup_layout.findViewById(R.id.editPassword);
       /* signInButton = (SignInButton) signup_layout.findViewById(R.id.google_login);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });*/

        alertDialog.setView(signup_layout);
        //  alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final User user = new User();
                if (!isValidEmail(editTextEmail.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Email Id is Not Valid", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editTextPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                user.setEmail(editTextEmail.getText().toString());
                user.setPassword(editTextPassword.getText().toString());
                user.setUsername(editTextUsername.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(user.getUsername()).exists()) {
                            Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();

                        } else {
                            boolean isOkay = false;
                            ArrayList<String> listOfEmail = new ArrayList<>();
                            listOfEmail = collectAllEmail((Map<String, Object>) dataSnapshot.getValue());
                            for (int i = 0; i < listOfEmail.size(); i++) {
                                if (listOfEmail.get(i).equals(user.getEmail())) {
                                    isOkay = true;
                                }
                            }
                            if (isOkay) {
                                Toast.makeText(MainActivity.this, "Email Exists", Toast.LENGTH_SHORT).show();

                            } else {
                                try {
                                    users.child(user.getUsername()).setValue(user);
                                    Toast.makeText(MainActivity.this, "User registered Success", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {

                                }
                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private ArrayList<String> collectAllEmail(Map<String, Object> value) {
        ArrayList<String> emailIds = new ArrayList<>();
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            Map singleUser = (Map) entry.getValue();
            emailIds.add((String) singleUser.get("email"));
        }
        return emailIds;
    }
/*
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);

    }

    private void handleResult(GoogleSignInResult googleSignInResult) {

        if (googleSignInResult.isSuccess()) {
            GoogleSignInAccount account = googleSignInResult.getSignInAccount();
            String email = account.getEmail();
            googleEmailId = email;
            editTextEmail.setText("");
            editTextEmail.setText(email);
            Toast.makeText(this, "Email is Set", Toast.LENGTH_SHORT).show();


        }

    }

    private void signout(final Context context) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                startActivity(new Intent(context, MainActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }*/
}
