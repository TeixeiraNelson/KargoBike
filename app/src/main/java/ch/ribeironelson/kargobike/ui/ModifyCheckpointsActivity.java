package ch.ribeironelson.kargobike.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.CheckpointEntity;
import ch.ribeironelson.kargobike.database.repository.CheckpointRepository;
import ch.ribeironelson.kargobike.ui.Delivery.AddDeliveryActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.viewmodel.CheckpointViewModel;
import ch.ribeironelson.kargobike.viewmodel.UsersListViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyCheckpointsActivity extends BaseActivity {
    private static final String TAG = "ModifyCheckPoints Activity";
    private String mode;
    private String checkPointId;

    private TextView description;
    private EditText checkPointName;
    private Button btnCheckPoint;
    private Button btnDeleteCheckPoint;

    private String checkpointname;
    private CheckpointEntity checkPoint;

    private CheckpointViewModel viewModelCheckpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_modify_checkpoints, frameLayout);
        navigationView.setCheckedItem(R.id.nav_delivery);

        mode = getIntent().getExtras().getString("mode","0");
        checkPointId = getIntent().getExtras().getString("checkpointid", "0");


        description = findViewById(R.id.textCheckPointDescription);
        checkPointName = findViewById(R.id.CheckPointData);
        btnCheckPoint = findViewById(R.id.btnCheckPoint);
        btnDeleteCheckPoint = findViewById(R.id.btnDeleteCheckPoint);

        btnCheckPoint.setOnClickListener(v -> verifyUserInputs());
        btnDeleteCheckPoint.setOnClickListener(v -> deleteCheckPoint());

        initComponents();

    }
    //init the Components and create the viewModel
    public void initComponents(){
        CheckpointViewModel.Factory factory = new CheckpointViewModel.Factory(getApplication(),checkPointId);
        viewModelCheckpoint = ViewModelProviders.of(this, factory).get(CheckpointViewModel.class);
        if(mode.equals("add")){
            description.setText("Add a new Check-Point");
            btnDeleteCheckPoint.setVisibility(View.INVISIBLE);
        }
        if(mode.equals("modify")) {
            description.setText("Modify the Check-Point");
            btnDeleteCheckPoint.setVisibility(View.VISIBLE);
            viewModelCheckpoint.getDelivery().observe(this, checkpointEntity -> {
                if (checkpointEntity != null) {
                    checkPoint = checkpointEntity;
                    checkPointName.setText(checkPoint.getName());
                }
            });
        }
    }
    //Method to delete a checkpoint
    private void deleteCheckPoint(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Delete Check-Point");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are your sure to Delete this Check-Point");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
            viewModelCheckpoint.deleteCheckPoint(checkPoint, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Intent h=new Intent (ModifyCheckpointsActivity.this, MainActivity.class);
                    startActivity(h);
                }

                @Override
                public void onFailure(Exception e) {}
            });

        });
    }

    //Verify the UserInputs and create or Update a Checkpoint
    private void verifyUserInputs(){
        checkpointname = checkPointName.getText().toString();
        if(!isAnyEditEmpty()){
            CheckpointEntity checkPoint = new CheckpointEntity(checkpointname);
            if(mode.equals("add")){
                viewModelCheckpoint.createCheckPoint(checkPoint, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Toast toast = Toast.makeText(ModifyCheckpointsActivity.this, "Created a new Check-Point", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
            if(mode.equals("modify")){
                viewModelCheckpoint.updateCheckPoint(checkPoint, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Toast toast = Toast.makeText(ModifyCheckpointsActivity.this, "Update Check-Point", Toast.LENGTH_LONG);
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
