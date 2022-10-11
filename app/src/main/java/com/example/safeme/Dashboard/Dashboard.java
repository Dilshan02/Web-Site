package com.example.safeme.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.safeme.Complaints.Complaints;
import com.example.safeme.Onboarding_Pages.HelperClasses.UserRegisterHelperClass;
import com.example.safeme.Onboarding_Pages.UserLogin;
import com.example.safeme.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menu_icon;
    LinearLayout contentView;
    TextView DashboardName;
    ProgressBar progressBar;
    static final float END_SCALE = 0.7f;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide Notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        //Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        menu_icon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);
        DashboardName = findViewById(R.id.dash_name);
        progressBar = findViewById(R.id.progressBar);

        //FirebaseHooks
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        navigationDrawer();

        DatabaseReference databaseReference = firebaseDatabase.getReference("User Details")
                .child("Users").child(firebaseAuth.getUid());
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserRegisterHelperClass userRegisterHelperClass = snapshot.getValue(UserRegisterHelperClass.class);

                DashboardName.setText(userRegisterHelperClass.getName());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Please enter your NIC", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isConnected();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Please enter your NIC", Toast.LENGTH_LONG).show();
                finish();
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        Toast.makeText(getApplicationContext(), "Please enter your NIC", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.logout:
                        Toast.makeText(getApplicationContext(), "Please enter your NIC", Toast.LENGTH_LONG).show();
                        signout();
                }
                return false;
            }
        });
    }

    private void signout() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(),
                UserLogin.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        Toast.makeText(getApplicationContext(), "Please enter your NIC", Toast.LENGTH_LONG).show();
    }

    //Navigation Drawer functions
    private void navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        animateNavigationDrawer();
    }


    private void animateNavigationDrawer() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.transparent_white_hex_1));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                        // Scale the View based on current slide offset
                        final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                        final float offsetScale = 1 - diffScaledOffset;
                        contentView.setScaleX(offsetScale);
                        contentView.setScaleY(offsetScale);

                        // Translate the View, accounting for the scaled width
                        final float xOffset = drawerView.getWidth() * slideOffset;
                        final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                        final float xTranslation = xOffset - xOffsetDiff;
                        contentView.setTranslationX(xTranslation);
                    }
                });
            }
        });
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public void setNavigationView(NavigationView navigationView) {
        this.navigationView = navigationView;
    }

    public ImageView getMenu_icon() {
        return menu_icon;
    }

    public void setMenu_icon(ImageView menu_icon) {
        this.menu_icon = menu_icon;
    }

    public LinearLayout getContentView() {
        return contentView;
    }

    public void setContentView(LinearLayout contentView) {
        this.contentView = contentView;
    }

    public TextView getDashboardName() {
        return DashboardName;
    }

    public void setDashboardName(TextView dashboardName) {
        DashboardName = dashboardName;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public void setFirebaseDatabase(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    protected void onResume() {
        isConnected();
        super.onResume();
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

    public void gotoComplaints(View view) {
        Intent intent = new Intent(getApplicationContext(), Complaints.class);
        startActivity(intent);
    }
}