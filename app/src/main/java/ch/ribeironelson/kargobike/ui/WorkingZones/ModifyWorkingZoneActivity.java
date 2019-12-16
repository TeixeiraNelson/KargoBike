package ch.ribeironelson.kargobike.ui.WorkingZones;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.adapter.ListAdapter;
import ch.ribeironelson.kargobike.database.entity.CheckpointEntity;
import ch.ribeironelson.kargobike.database.entity.WorkingZoneEntity;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.ui.Checkpoint.ModifyCheckpointsActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.viewmodel.CheckpointListViewModel;
import ch.ribeironelson.kargobike.viewmodel.CheckpointViewModel;
import ch.ribeironelson.kargobike.viewmodel.WorkingZoneListViewModel;
import ch.ribeironelson.kargobike.viewmodel.WorkingZoneViewModel;

public class ModifyWorkingZoneActivity extends BaseActivity {

    private static final String TAG = "ModifyWorkingZonesActivity";
    private String mode = "add";

    private TextView description;
    private EditText workingZoneName;
    private Button btnWorkingZone;
    private Button btnDeleteWorkingZone;
    private Spinner spinnerWorkingZones;
    FloatingActionButton fab;

    private String  workingzonename;
    private WorkingZoneEntity workingZoneEntity;

    private ListAdapter<String> adapterWorkingZoneList;

    private WorkingZoneViewModel viewModelWorkingZone;
    private WorkingZoneListViewModel listViewModelWorkingZone;

    private List<WorkingZoneEntity> workingZoneEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_modify_working_zone, frameLayout);
        navigationView.setCheckedItem(R.id.nav_working_zones);

        mode = getIntent().getExtras().getString("mode","add");
        fab = findViewById(R.id.floatingActionButtonWZ);
        description = findViewById(R.id.textWorkingZoneDescription);
        workingZoneName = findViewById(R.id.workingZoneData);
        btnWorkingZone = findViewById(R.id.btnWorkingZone);
        btnDeleteWorkingZone = findViewById(R.id.btnDeleteWorkingZone);
        spinnerWorkingZones = findViewById(R.id.spinner_workingZones);
        btnWorkingZone.setOnClickListener(v -> verifyUserInputs());
        btnDeleteWorkingZone.setOnClickListener(v -> deleteWorkingZone());

        adapterWorkingZoneList = new ListAdapter<>(ModifyWorkingZoneActivity.this, R.layout.row_list, new ArrayList<>());
        spinnerWorkingZones.setAdapter(adapterWorkingZoneList);

        initComponents();
    }

    private void enableButtons() {

    }

    private void startEditingMode(){
        Intent intent = new Intent(ModifyWorkingZoneActivity.this, ModifyWorkingZoneActivity.class);
        intent.putExtra("mode", "modify");
        startActivity(intent);
        this.finish();
    }

    private void startAddMode(){
        Intent intent = new Intent(ModifyWorkingZoneActivity.this, ModifyWorkingZoneActivity.class);
        intent.putExtra("mode", "add");
        startActivity(intent);
        this.finish();
    }

    //init the Components and create the viewModel
    public void initComponents(){

        if(mode.equals("add")){
            description.setText("Add a new Working Zone");
            btnDeleteWorkingZone.setVisibility(View.INVISIBLE);
            spinnerWorkingZones.setVisibility(View.INVISIBLE);
            btnWorkingZone.setEnabled(true);
            workingZoneName.setEnabled(true);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startEditingMode();
                }
            });

        }
        if(mode.equals("modify")) {
            description.setText("Modify a Working Zone");
            btnDeleteWorkingZone.setVisibility(View.VISIBLE);
            spinnerWorkingZones.setVisibility(View.VISIBLE);
            btnDeleteWorkingZone.setEnabled(true);
            btnWorkingZone.setEnabled(true);
            spinnerWorkingZones.setVisibility(View.VISIBLE);
            FloatingActionButton fab = findViewById(R.id.floatingActionButtonWZ);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_circle_black_24dp));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startAddMode();
                }
            });
        }

        fillSpinners();
        assignSpinnerActions();
    }

    private void assignSpinnerActions() {
        spinnerWorkingZones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                workingZoneEntity = workingZoneEntities.get(position);
                WorkingZoneViewModel.Factory factory = new WorkingZoneViewModel.Factory(getApplication(),workingZoneEntity.getWorkingZoneId());
                viewModelWorkingZone = ViewModelProviders.of(ModifyWorkingZoneActivity.this, factory).get(WorkingZoneViewModel.class);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void fillSpinners() {

        WorkingZoneListViewModel.Factory factory3 = new WorkingZoneListViewModel.Factory(getApplication());
        listViewModelWorkingZone = ViewModelProviders.of(this,factory3).get(WorkingZoneListViewModel.class);

        listViewModelWorkingZone.getAllWorkingZones().observe(this, workingZoneEntityList -> {
            if (workingZoneEntityList != null) {
                //Array
                ArrayList<String> workingZoneNames = new ArrayList<String>();
                workingZoneEntities = new ArrayList<>();
                for (WorkingZoneEntity u : workingZoneEntityList) {
                    Log.d("WORKING ZONE", u.toString());
                    if(!u.getWorkingZoneId().equals("0")) {
                        workingZoneNames.add(u.getLocation());
                        workingZoneEntities.add(u);
                    }
                }
                adapterWorkingZoneList.updateData(new ArrayList<>(workingZoneNames));
            }
        });
    }

    //Method to delete a checkpoint
    private void deleteWorkingZone(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Delete Working Zone");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are your sure you want to delete this Working Zone");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
            viewModelWorkingZone.deleteWorkingZone(workingZoneEntity, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d("MODIFYWORKINGZONES","Working Zone successfully deleted.");
                    Toast toast = Toast.makeText(ModifyWorkingZoneActivity.this, "Working Zone deleted!", Toast.LENGTH_LONG);
                    toast.show();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("MODIFYWORKINGZONES","Working Zone delete error." + e.getStackTrace());
                }
            });

        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", (dialog, which) -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    //Verify the UserInputs and create or Update a Checkpoint
    private void verifyUserInputs(){
        workingzonename = workingZoneName.getText().toString();
        if(!isAnyEditEmpty()){

            if(mode.equals("add")){
                WorkingZoneEntity workingZoneEntity1 = new WorkingZoneEntity(workingzonename);
                viewModelWorkingZone.createWorkingZone(workingZoneEntity1, new OnAsyncEventListener()
                    {
                    @Override
                    public void onSuccess() {
                        Toast toast = Toast.makeText(ModifyWorkingZoneActivity.this, "Created a new Working Zone", Toast.LENGTH_LONG);
                        toast.show();
                        workingZoneName.setText(null);
                    }
                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
            if(mode.equals("modify")){
                if(workingzonename.length()>1)
                    workingZoneEntity.setLocation(workingzonename);

                viewModelWorkingZone.updateWorkingZone(workingZoneEntity, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Toast toast = Toast.makeText(ModifyWorkingZoneActivity.this, "Updated Working Zone", Toast.LENGTH_LONG);
                        toast.show();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
        }
    }

    private boolean isAnyEditEmpty() {
        if (workingZoneName.length() < 1) {
            workingZoneName.setError("You must enter a checkpoint name !");
            return true;
        }
        return false;
    }


}
