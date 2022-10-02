package com.example.safeme.Onboarding_Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.safeme.Dashboard.Dashboard;
import com.example.safeme.Onboarding_Pages.HelperClasses.UserRegisterHelperClass;
import com.example.safeme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegister extends AppCompatActivity {
    Spinner spinner;
    TextInputLayout NIC, Name, Mobile, Email, Password;
    Button Register;
    ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        //Hide Notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_register);

        //Hooks
        spinner = findViewById(R.id.spinCity);
        NIC = findViewById(R.id.NIC);
        Name = findViewById(R.id.Name);
        Mobile = findViewById(R.id.MobileNumber);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.Password);
        Register = findViewById(R.id.Registerbtn);
        progressBar = findViewById(R.id.progressBar);

        //firebase
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //Pass values to spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.
                simple_spinner_item, getResources().getStringArray(R.array.city_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //change colour in progressbar
        progressBar.getIndeterminateDrawable().setColorFilter(0xff000000,
                android.graphics.PorterDuff.Mode.MULTIPLY);
        //Validations
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nic = NIC.getEditText().getText().toString();
                String name = Name.getEditText().getText().toString();
                String mobile = Mobile.getEditText().getText().toString();
                String email = Email.getEditText().getText().toString();
                String pass = Password.getEditText().getText().toString();
                String spin = spinner.getSelectedItem().toString();

                if (isConnected()) {

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection, Please Connect to Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(nic)) {
                    Toast.makeText(getApplicationContext(), "Please enter your NIC", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(getApplicationContext(), "Please enter your mobile number", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_LONG).show();
                }
                if (pass.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
                }
                if (pass.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password must be more than 6 digit", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(spin)) {
                    Toast.makeText(getApplicationContext(), "Please enter your city", Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(UserRegister.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "ERROR! Cant create Account", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                        UserRegisterHelperClass userRegisterHelperClass = new UserRegisterHelperClass(nic, name, mobile, email, spin);
                                        databaseReference = firebaseDatabase.getReference("User Details").child("Users");
                                        FirebaseUser firebaseUser = auth.getCurrentUser();
                                        databaseReference.child(firebaseUser.getUid()).setValue(userRegisterHelperClass);
                                        Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    //Check Internet Connection Method
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public void gotoLogin(View view) {
        Intent intent = new Intent(UserRegister.this, UserLogin.class);
        startActivity(intent);
        finish();
    }
}