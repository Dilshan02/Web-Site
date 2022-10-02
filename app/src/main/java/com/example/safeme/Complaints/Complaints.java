package com.example.safeme.Complaints;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safeme.Complaints.ComplaintHelperClasses.ComplaintHelperClass;
import com.example.safeme.Complaints.ComplaintsAdapters.ComplaintsAdapter;
import com.example.safeme.Onboarding_Pages.HelperClasses.UserRegisterHelperClass;
import com.example.safeme.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Complaints extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private FirebaseAuth firebaseAuth;

    ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private Uri ImageUri;
    private int upload_count = 1;

    List<ComplaintHelperClass> complaintHelperClass;
    RecyclerView recyclerView;
    ComplaintsAdapter complaintsAdapter;
    EditText SearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide Notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_complaints);

        //Hooks
        SearchBar = findViewById(R.id.ComplaintSearchView);
        recyclerView = findViewById(R.id.ComplaintRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        complaintHelperClass = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //Set complanits to recyclerview
        databaseReference = firebaseDatabase.getReference("Complaints").child(firebaseAuth.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ComplaintHelperClass data = dataSnapshot.getValue(ComplaintHelperClass.class);
                    complaintHelperClass.add(data);
                }
                complaintsAdapter = new ComplaintsAdapter(complaintHelperClass);
                recyclerView.setAdapter(complaintsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Search Function
        SearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String toString) {
        ArrayList<ComplaintHelperClass> filterList = new ArrayList<>();
        for (ComplaintHelperClass item : complaintHelperClass) {
            if (item.getCID().toLowerCase().contains(toString.toLowerCase())) {
                filterList.add(item);
            }
        }
        complaintsAdapter.filteredList(filterList);
    }

    public void placeComplaint(View view) {
        placeAComplaint();
    }


    private void placeAComplaint() {
        //get current date
        String date_n = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(new Date());

        //FirebaseHooks
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();

        View view = LayoutInflater.from(this).inflate(R.layout.place_a_complaint, null);
        TextInputLayout NIC = view.findViewById(R.id.NIC);
        TextInputLayout Name = view.findViewById(R.id.Name);
        TextInputLayout Mobile = view.findViewById(R.id.MobileNumber);
        TextInputLayout City = view.findViewById(R.id.city);
        TextInputLayout CID = view.findViewById(R.id.CID);
        TextInputLayout Date = view.findViewById(R.id.Date);
        TextInputLayout Description = view.findViewById(R.id.description);
        Spinner CType = view.findViewById(R.id.complaintType);
        TextInputLayout Status = view.findViewById(R.id.status);


        Button place = view.findViewById(R.id.place_complaint);
        Button chooseImage = view.findViewById(R.id.choose_image);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final android.app.AlertDialog dialog = builder.create();
        dialog.show();

        //set date
        Date.getEditText().setText(date_n);

        //create CID
        final Random random = new Random();
        CID.getEditText().setText(String.valueOf(random.nextInt(10000)));

        //set values to spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.
                simple_spinner_item, getResources().getStringArray(R.array.complaint_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CType.setAdapter(adapter);

        //choose images
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
        //Fetch data
        DatabaseReference databaseReference = firebaseDatabase.getReference("User Details")
                .child("Users").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserRegisterHelperClass userRegisterHelperClass = snapshot.getValue(UserRegisterHelperClass.class);

                NIC.getEditText().setText(userRegisterHelperClass.getNIC());
                Name.getEditText().setText(userRegisterHelperClass.getName());
                Mobile.getEditText().setText(userRegisterHelperClass.getMobile());
                City.getEditText().setText(userRegisterHelperClass.getCity());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                finish();
            }
        });

        //Place a new complaint
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nic = NIC.getEditText().getText().toString();
                String name = Name.getEditText().getText().toString();
                String mobile = Mobile.getEditText().getText().toString();
                String city = City.getEditText().getText().toString();
                String cid = CID.getEditText().getText().toString();
                String date = Date.getEditText().getText().toString();
                String description = CType.getSelectedItem().toString();
                String cType = Description.getEditText().getText().toString();
                String status = Status.getEditText().getText().toString();

                if (isConnected()) {

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection, Please Connect to Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(description)) {
                    Toast.makeText(getApplicationContext(), "Please describe your complaint", Toast.LENGTH_LONG).show();
                    return;
                }
                if (ImageList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select a image", Toast.LENGTH_LONG).show();
                } else {


                    //Upload Images
                    StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("Complaints");

                    Uri IndividualImage = ImageList.get(upload_count);
                    StorageReference ImageName = ImageFolder.child("Image" + IndividualImage.getLastPathSegment());

                    ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                    ComplaintHelperClass complaintHelperClass = new ComplaintHelperClass(nic, name, mobile, city, cid, date, cType, description, status);
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    databaseReference2 = firebaseDatabase.getReference("Complaints").child(firebaseUser.getUid());
                                    String url = String.valueOf(uri);
                                    complaintHelperClass.setImageLink(url);
                                    databaseReference2.push().setValue(complaintHelperClass);
                                }
                            });
                        }
                    });
                }
                Toast.makeText(getApplicationContext(), "Complaint placed", Toast.LENGTH_LONG).show();
                finish();
            }

        });
    }

    //Image validations
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data.getClipData() != null) {
                    int countClipData = data.getClipData().getItemCount();
                    int currentImageSelect = 0;
                    while (currentImageSelect < countClipData) {
                        ImageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                        ImageList.add(ImageUri);
                        currentImageSelect = currentImageSelect + 1;
                    }
                    Toast.makeText(getApplicationContext(), "You have selected  " + ImageList.size() + "  Images", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Please select at least two image", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
}