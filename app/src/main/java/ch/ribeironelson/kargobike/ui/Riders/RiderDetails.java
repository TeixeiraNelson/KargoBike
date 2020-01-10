package ch.ribeironelson.kargobike.ui.Riders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.CheckpointEntity;
import ch.ribeironelson.kargobike.database.entity.RoleEntity;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.database.entity.WorkingZoneEntity;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.viewmodel.RoleListViewModel;
import ch.ribeironelson.kargobike.viewmodel.UserViewModel;
import ch.ribeironelson.kargobike.viewmodel.WorkingZoneListViewModel;

public class RiderDetails extends BaseActivity {

    private static final String TAG = "RiderDetails";

    private UserViewModel userViewModel ;
    private UserEntity user ;
    private String userid ;

    private List<WorkingZoneEntity> workingZonesL ;
    private List<RoleEntity> rolesL ;
    private RoleListViewModel roleListViewModel ;
    private WorkingZoneListViewModel workingZoneListViewModel ;

    private EditText firstnameData;
    private EditText lastnameData;
    private EditText emailData;
    private EditText phoneNumberData;
    private TextView workingZoneTxt;
    private Spinner workingZoneSpinner;
    private Spinner roleSpinner;
    private Button modifyRiderButton;
    private Button cancelRiderButton;

    private boolean isEditable ;

    private Application mApp;
    private Context mContext;
    private DatabaseReference mRoleRef ;
    private DatabaseReference mWorkingZoneRef;

    private Toast toast ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_rider_details, frameLayout);
        navigationView.setCheckedItem(R.id.nav_riders);

        mRoleRef = FirebaseDatabase.getInstance().getReference().child("roles");
        mWorkingZoneRef = FirebaseDatabase.getInstance().getReference().child("workingZone");
        mApp = getApplication();
        mContext = RiderDetails.this;

        userid = getIntent().getStringExtra("userid");

        workingZonesL = new ArrayList<>();
        rolesL = new ArrayList<>();

        loadData();
        initiateView();
        initiateSpinnerRole();
        initiateSpinnerWorkingZone();


    }

    private void initiateSpinnerWorkingZone() {
        mWorkingZoneRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> workingZones = new ArrayList<String>();

                for(DataSnapshot workingZonesSnapshot: dataSnapshot.getChildren()){
                    String workingZoneName = workingZonesSnapshot.child("location").getValue(String.class);
                    workingZones.add(workingZoneName);
                }

                ArrayAdapter<String> workingZonesAdapter = new ArrayAdapter<String>(RiderDetails.this, android.R.layout.simple_spinner_item, workingZones);
                workingZonesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                workingZoneSpinner.setAdapter(workingZonesAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Can't listen to query " + mWorkingZoneRef, databaseError.toException());
            }
        });
    }

    private void initiateSpinnerRole() {
        mRoleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> roles = new ArrayList<String>();

                for(DataSnapshot roleSnapshot: dataSnapshot.getChildren()){
                    String roleName = roleSnapshot.child("role").getValue(String.class);
                    roles.add(roleName);
                }

                ArrayAdapter<String> rolesAdapter = new ArrayAdapter<String>(RiderDetails.this, android.R.layout.simple_spinner_item, roles);
                rolesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                roleSpinner.setAdapter(rolesAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Can't listen to query " + mRoleRef, databaseError.toException());
            }
        });
    }

    private void initiateView() {
        isEditable = false ;
        firstnameData = findViewById(R.id.firstnameData);
        lastnameData = findViewById(R.id.lastnameData);
        emailData = findViewById(R.id.emailData);
        phoneNumberData = findViewById(R.id.phoneNumberData);
        workingZoneSpinner = findViewById(R.id.workingZoneSpinner);
        roleSpinner = findViewById(R.id.roleSpinner);
        workingZoneTxt = findViewById(R.id.workingZoneText);
        modifyRiderButton = findViewById(R.id.modifyRiderButton);
        cancelRiderButton = findViewById(R.id.cancelSaveButton);

        workingZoneTxt.setVisibility(View.INVISIBLE);

        firstnameData.setFocusable(false);
        firstnameData.setClickable(false);
        firstnameData.setEnabled(false);

        lastnameData.setFocusable(false);
        lastnameData.setClickable(false);
        lastnameData.setEnabled(false);

        emailData.setFocusable(false);
        emailData.setClickable(false);
        emailData.setEnabled(false);

        phoneNumberData.setFocusable(false);
        phoneNumberData.setClickable(false);
        phoneNumberData.setEnabled(false);

        workingZoneSpinner.setEnabled(false);
        workingZoneSpinner.setClickable(false);
        workingZoneSpinner.setVisibility(View.INVISIBLE);

        roleSpinner.setEnabled(false);
        roleSpinner.setClickable(false);

        modifyRiderButton.setText("Modify");
        modifyRiderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    switchToEdit();
            }
        });

        cancelRiderButton.setVisibility(View.INVISIBLE);

    }

    private void switchToEdit(){
        if(!isEditable){
            firstnameData.setFocusableInTouchMode(true);
            firstnameData.setFocusable(true);
            firstnameData.setClickable(true);
            firstnameData.setEnabled(true);

            lastnameData.setFocusableInTouchMode(true);
            lastnameData.setFocusable(true);
            lastnameData.setClickable(true);
            lastnameData.setEnabled(true);

            emailData.setFocusableInTouchMode(true);
            emailData.setFocusable(true);
            emailData.setClickable(true);
            emailData.setEnabled(true);

            phoneNumberData.setFocusableInTouchMode(true);
            phoneNumberData.setFocusable(true);
            phoneNumberData.setClickable(true);
            phoneNumberData.setEnabled(true);

            workingZoneSpinner.setEnabled(true);
            workingZoneSpinner.setClickable(true);

            roleSpinner.setEnabled(true);
            roleSpinner.setClickable(true);

            modifyRiderButton.setText("Save Changes");

            cancelRiderButton.setVisibility(View.VISIBLE);
            cancelRiderButton.setClickable(true);
            cancelRiderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initiateView();
                }
            });
        }else{
            saveChanges(firstnameData.getText().toString(),
                    lastnameData.getText().toString(),
                    emailData.getText().toString(),
                    phoneNumberData.getText().toString(),
                    workingZoneSpinner.getSelectedItem().toString(),
                    roleSpinner.getSelectedItem().toString());

            firstnameData.setFocusable(false);
            firstnameData.setClickable(false);
            firstnameData.setEnabled(false);

            lastnameData.setFocusable(false);
            lastnameData.setClickable(false);
            lastnameData.setEnabled(false);

            emailData.setFocusable(false);
            emailData.setClickable(false);
            emailData.setEnabled(false);

            phoneNumberData.setFocusable(false);
            phoneNumberData.setClickable(false);
            phoneNumberData.setEnabled(false);

            workingZoneSpinner.setEnabled(false);
            workingZoneSpinner.setClickable(false);

            roleSpinner.setEnabled(false);
            roleSpinner.setClickable(false);

            modifyRiderButton.setText("Modify");

            cancelRiderButton.setVisibility(View.INVISIBLE);
            cancelRiderButton.setClickable(false);
        }
        isEditable = !isEditable;
    }

    private void saveChanges(String firstname, String lastname, String email, String phonenumber, String workingzone, String role) {
        // TO DO : working zone get id && Role get id + set
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPhoneNumber(phonenumber);
        for(RoleEntity r : rolesL){
            if(role.equals(r.getRoleId())){
                role = r.getRole();
            }
        }
        user.setIdRole(role);

        userViewModel.updateUser(user, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "UpdateUser: Success");
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "UpdateUser: Failure");
                setResponse(false);
            }
        });
    }

    private void setResponse(Boolean response){
        if(response){
            toast = Toast.makeText(this,"User edited", Toast.LENGTH_LONG);
            toast.show();
        }else{
            toast = Toast.makeText(this, "User not edited", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void loadData() {

        UserViewModel.Factory factory = new UserViewModel.Factory(getApplication(), userid);
        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        userViewModel.getUser().observe(this, userEntity -> {
            if(userEntity != null) {
                user = userEntity;

                RoleListViewModel.Factory factoryR = new RoleListViewModel.Factory(getApplication());
                roleListViewModel = ViewModelProviders.of(this, factoryR).get(RoleListViewModel.class);
                roleListViewModel.getRoles().observe(this, roleEntities -> {
                    if(roleEntities != null){
                        rolesL = roleEntities ;
                        System.out.println("PAS NUL");

                        WorkingZoneListViewModel.Factory factory3 = new WorkingZoneListViewModel.Factory(getApplication());
                        workingZoneListViewModel = ViewModelProviders.of(this,factory3).get(WorkingZoneListViewModel.class);
                        workingZoneListViewModel.getAllWorkingZones().observe(this, workingZoneEntityList -> {
                            if (workingZoneEntityList != null) {
                                workingZonesL = workingZoneEntityList;
                                updateContent();
                            }
                        });
                    }
                });
            }
        });
    }


    private void updateContent(){
        if(user != null){
            firstnameData.setText(user.getFirstname());
            lastnameData.setText(user.getLastname());
            emailData.setText(user.getEmail());
            phoneNumberData.setText(user.getPhoneNumber());

            String roleName = null;
            String roleId = user.getIdRole();
            for(RoleEntity role: rolesL){
                if(role.getRoleId().equals(roleId))
                    roleName = role.getRole();
            }
            ArrayAdapter myAdapR = (ArrayAdapter) roleSpinner.getAdapter();
            int spinnerPosR = myAdapR.getPosition(roleName);
            roleSpinner.setSelection(spinnerPosR);

            String workingZoneName = null;
            String workingZoneId = null ;
            for(WorkingZoneEntity w: workingZonesL){
                if(w.getAssignedDispatcherId().equals(user.getIdUser())){
                    workingZoneId = w.getAssignedDispatcherId();
                }
            }
            for(WorkingZoneEntity w: workingZonesL){
                if(w.getWorkingZoneId().equals(workingZoneId))
                    workingZoneName=w.getLocation();
            }

            ArrayAdapter myAdapW = (ArrayAdapter) workingZoneSpinner.getAdapter();
            int spinnerPosW = myAdapW.getPosition(workingZoneName);
            workingZoneSpinner.setSelection(spinnerPosW);

        }
    }
}
