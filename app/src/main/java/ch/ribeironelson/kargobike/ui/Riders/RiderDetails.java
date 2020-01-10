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

    private static final int EDIT_RIDER = 0 ;

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
    private Spinner workingZoneSpinner;
    private Spinner roleSpinner;
    private Button modifyRiderButton;

    private boolean isEditable ;

    private Application mApp;
    private Context mContext;
    private DatabaseReference mRoleRef ;
    private DatabaseReference mWorkingZoneRef;

    private Toast toast ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_details);
        navigationView.setCheckedItem(R.id.nav_riders);

        mRoleRef = FirebaseDatabase.getInstance().getReference().child("roles");
        mWorkingZoneRef = FirebaseDatabase.getInstance().getReference().child("workingZone");
        mApp = getApplication();
        mContext = RiderDetails.this;

        userid = getIntent().getStringExtra("userid");

        loadData();
        initiateView();
        initiateSpinnerRole();
        initiateSpinnerWorkingZone();

        UserViewModel.Factory factory = new UserViewModel.Factory(getApplication(), userid);
        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        userViewModel.getUser().observe(this, userEntity -> {
            user = userEntity ;
            updateContent();
        });

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
        modifyRiderButton = findViewById(R.id.modifyRiderButton);

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

    }

    private void switchToEdit(){
        if(!isEditable){
            firstnameData.setFocusable(true);
            firstnameData.setClickable(true);
            firstnameData.setEnabled(true);

            lastnameData.setFocusable(true);
            lastnameData.setClickable(true);
            lastnameData.setEnabled(true);

            emailData.setFocusable(true);
            emailData.setClickable(true);
            emailData.setEnabled(true);

            phoneNumberData.setFocusable(true);
            phoneNumberData.setClickable(true);
            phoneNumberData.setEnabled(true);

            workingZoneSpinner.setEnabled(true);
            workingZoneSpinner.setClickable(true);

            roleSpinner.setEnabled(true);
            roleSpinner.setClickable(true);
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
        }
        isEditable = !isEditable;
    }

    private void saveChanges(String firstname, String lastname, String email, String phonenumber, String workingzone, String role) {
        // TO DO : working zone get id && Role get id + set
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPhoneNumber(phonenumber);

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

        RoleListViewModel.Factory factoryR = new RoleListViewModel.Factory(mApp);
        roleListViewModel = ViewModelProviders.of(this, factoryR).get(RoleListViewModel.class);
        roleListViewModel.getRoles().observe(this, roleEntities -> {
            if(roleEntities != null){
                rolesL = roleEntities ;
                System.out.println("PAS NUL");
            }

        });

        WorkingZoneListViewModel.Factory factoryW = new WorkingZoneListViewModel.Factory(mApp);
        workingZoneListViewModel = ViewModelProviders.of(this, factoryW).get(WorkingZoneListViewModel.class);
        workingZoneListViewModel.getAllWorkingZones().observe(this, workingZoneEntities -> {
            if(workingZoneEntities != null){
                workingZonesL = workingZoneEntities ;
                System.out.println("PAS NUL");
            }

        });
    }


    private void updateContent(){
        if(user != null){
            firstnameData.setText(user.getFirstname());
            lastnameData.setText(user.getLastname());
            emailData.setText(user.getEmail());
            phoneNumberData.setText(user.getPhoneNumber());

          /*  String roleName = null;
            String roleId = user.getIdRole();
            for(RoleEntity role: rolesL){
                if(role.getRoleId().equals(roleId))
                    roleName = role.getRole();
            }
            ArrayAdapter myAdapR = (ArrayAdapter) roleSpinner.getAdapter();
            int spinnerPos = myAdapR.getPosition(roleName);
            roleSpinner.setSelection(spinnerPos);

            String workingZoneName = null;
            String workingZoneId = user.getIdZone();
            for(WorkingZoneEntity w: workingZonesL){
                if(w.getWorkingZoneId().equals(workingZoneId))
                    workingZoneName=w.getLocation();
            }

            ArrayAdapter myAdapW = (ArrayAdapter) workingZoneSpinner.getAdapter();
            int spinnerPos = myAdapW.getPosition(workingZoneName);
            workingZoneSpinner.setSelection(spinnerPos);  */

        }
    }
}
