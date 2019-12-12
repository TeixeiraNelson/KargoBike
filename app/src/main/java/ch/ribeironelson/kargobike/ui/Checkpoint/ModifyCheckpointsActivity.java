package ch.ribeironelson.kargobike.ui.Checkpoint;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.adapter.ListAdapter;
import ch.ribeironelson.kargobike.database.entity.CheckpointEntity;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.database.entity.WorkingZoneEntity;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.ui.Delivery.AddDeliveryActivity;
import ch.ribeironelson.kargobike.ui.MainActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.viewmodel.CheckpointListViewModel;
import ch.ribeironelson.kargobike.viewmodel.CheckpointViewModel;
import ch.ribeironelson.kargobike.viewmodel.WorkingZoneListViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModifyCheckpointsActivity extends BaseActivity {
    private static final String TAG = "ModifyCheckPointsActivity";
    private String mode = "add";

    private TextView description;
    private EditText checkPointName;
    private Button btnCheckPoint;
    private Button btnDeleteCheckPoint;
    private Button btnModify;
    private Spinner spinnerCheckpoints;
    private Spinner spinnerLocations;
    private TextView textView;
    FloatingActionButton fab;

    private String checkpointname;
    private CheckpointEntity checkPoint;
    private WorkingZoneEntity workingZoneEntity;

    private ListAdapter<String> adapterCheckpointList;
    private ListAdapter<String> adapterWorkingZoneList;

    private CheckpointViewModel viewModelCheckpoint;
    private CheckpointListViewModel viewModelCheckpointList;
    private WorkingZoneListViewModel workingZoneListViewModel;

    private List<CheckpointEntity> checkpointEntities;
    private List<WorkingZoneEntity> workingZoneEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_modify_checkpoints, frameLayout);
        navigationView.setCheckedItem(R.id.nav_checkpoints);

        mode = getIntent().getExtras().getString("mode","add");
        fab = findViewById(R.id.floatingActionButton);
        description = findViewById(R.id.textCheckPointDescription);
        checkPointName = findViewById(R.id.CheckPointData);
        btnCheckPoint = findViewById(R.id.btnCheckPoint);
        btnDeleteCheckPoint = findViewById(R.id.btnDeleteCheckPoint);
        btnModify = findViewById(R.id.btnModifyCheckpoint);
        textView = findViewById(R.id.textView6);
        spinnerCheckpoints = findViewById(R.id.spinner_checkpoints);
        spinnerLocations = findViewById(R.id.spinner_locations);
        btnModify.setOnClickListener(v-> enableButtons());
        btnCheckPoint.setOnClickListener(v -> verifyUserInputs());
        btnDeleteCheckPoint.setOnClickListener(v -> deleteCheckPoint());

        adapterCheckpointList = new ListAdapter<>(ModifyCheckpointsActivity.this, R.layout.row_list, new ArrayList<>());
        spinnerCheckpoints.setAdapter(adapterCheckpointList);

        adapterWorkingZoneList = new ListAdapter<>(ModifyCheckpointsActivity.this, R.layout.row_list, new ArrayList<>());
        spinnerLocations.setAdapter(adapterWorkingZoneList);

        initComponents();

    }

    private void enableButtons() {
        btnDeleteCheckPoint.setEnabled(true);
        btnCheckPoint.setEnabled(true);
        textView.setVisibility(View.VISIBLE);
        spinnerLocations.setVisibility(View.VISIBLE);
    }

    private void startEditingMode(){
        Intent intent = new Intent(ModifyCheckpointsActivity.this, ModifyCheckpointsActivity.class);
        intent.putExtra("mode", "modify");
        startActivity(intent);
        this.finish();
    }

    private void startAddMode(){
        Intent intent = new Intent(ModifyCheckpointsActivity.this, ModifyCheckpointsActivity.class);
        intent.putExtra("mode", "add");
        startActivity(intent);
        this.finish();
    }

    //init the Components and create the viewModel
    public void initComponents(){

        if(mode.equals("add")){
            description.setText("Add a new Check-Point");
            btnDeleteCheckPoint.setVisibility(View.INVISIBLE);
            btnModify.setVisibility(View.INVISIBLE);
            spinnerCheckpoints.setVisibility(View.INVISIBLE);
            btnCheckPoint.setEnabled(true);
            checkPointName.setEnabled(true);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   startEditingMode();
                }
            });

        }
        if(mode.equals("modify")) {
            description.setText("Modify a Check-Point");
            btnDeleteCheckPoint.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            spinnerLocations.setVisibility(View.INVISIBLE);
            FloatingActionButton fab = findViewById(R.id.floatingActionButton);
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
        spinnerLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                workingZoneEntity = workingZoneEntities.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spinnerCheckpoints.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkPoint = checkpointEntities.get(position);
                CheckpointViewModel.Factory factory = new CheckpointViewModel.Factory(getApplication(),checkPoint.getIdCheckpoint());
        viewModelCheckpoint = ViewModelProviders.of(ModifyCheckpointsActivity.this, factory).get(CheckpointViewModel.class);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fillSpinners() {

        CheckpointListViewModel.Factory factory2 = new CheckpointListViewModel.Factory(getApplication());
        viewModelCheckpointList = ViewModelProviders.of(this, factory2).get(CheckpointListViewModel.class);

        WorkingZoneListViewModel.Factory factory3 = new WorkingZoneListViewModel.Factory(getApplication());
        workingZoneListViewModel = ViewModelProviders.of(this,factory3).get(WorkingZoneListViewModel.class);


        viewModelCheckpointList.getCheckpoints().observe(this, checkpointEntitiesList -> {
            if (checkpointEntitiesList != null) {
                //Array
                ArrayList<String> checkpointNames = new ArrayList<String>();
                checkpointEntities = new ArrayList<>();
                for (CheckpointEntity u : checkpointEntitiesList) {
                    Log.d("ModifyCheckpoint", u.getName());
                    if(!u.getIdCheckpoint().equals("0")){
                        checkpointNames.add(u.getName() + " - " + u.getLocation().getLocation());
                        checkpointEntities.add(u);
                    }
                }
                adapterCheckpointList.updateData(new ArrayList<>(checkpointNames));
            }
        });

        workingZoneListViewModel.getAllWorkingZones().observe(this, workingZoneEntityList -> {
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
    private void deleteCheckPoint(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Delete Check-Point");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are your sure you want to delete this Check-Point");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
            viewModelCheckpoint.deleteCheckPoint(checkPoint, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d("MODIFYCHECKPOINTS","Checkpoint successfully deleted.");
                    Toast toast = Toast.makeText(ModifyCheckpointsActivity.this, "Checkpoint deleted!", Toast.LENGTH_LONG);
                    toast.show();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("MODIFYCHECKPOINTS","Checkpoint delete error." + e.getStackTrace());
                }
            });

        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "NO", (dialog, which) -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    //Verify the UserInputs and create or Update a Checkpoint
    private void verifyUserInputs(){
        checkpointname = checkPointName.getText().toString();
        if(!isAnyEditEmpty()){

            if(mode.equals("add")){
                CheckpointEntity checkPoint2 = new CheckpointEntity(checkpointname);
                checkPoint2.setLocation(workingZoneEntity);
                viewModelCheckpoint.createCheckPoint(checkPoint2, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Toast toast = Toast.makeText(ModifyCheckpointsActivity.this, "Created a new Check-Point", Toast.LENGTH_LONG);
                        toast.show();
                        checkPointName.setText(null);
                    }
                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
            if(mode.equals("modify")){
                if(checkpointname.length()>1)
                    checkPoint.setName(checkpointname);

                if(spinnerLocations.getVisibility()==View.VISIBLE)
                    checkPoint.setLocation(workingZoneEntity);

                viewModelCheckpoint.updateCheckPoint(checkPoint, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Toast toast = Toast.makeText(ModifyCheckpointsActivity.this, "Updated Check-Point", Toast.LENGTH_LONG);
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
        if (checkPointName.length() < 1) {
            checkPointName.setError("You must enter a checkpoint name !");
            return true;
        }
        return false;
    }
}
