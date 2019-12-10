package ch.ribeironelson.kargobike.ui.Checkpoint;

import androidx.appcompat.app.AppCompatActivity;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.ui.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class CheckPointsActivity extends BaseActivity {
    private Button edit;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_check_points, frameLayout);
        navigationView.setCheckedItem(R.id.nav_checkpoints);

        edit = findViewById(R.id.btnTest);
        edit.setOnClickListener(v -> testF());

        add = findViewById(R.id.btnAdd);
        add.setOnClickListener(v -> addF());
    }

    private void testF(){
        Intent intent = new Intent(CheckPointsActivity.this, ModifyCheckpointsActivity.class);
        intent.putExtra("mode", "modify");
        intent.putExtra("checkpointid", "LvFojti-0YEH-rOpCJt");
        startActivity(intent);

    }

    private void addF(){
        Intent intent = new Intent(CheckPointsActivity.this, ModifyCheckpointsActivity.class);
        intent.putExtra("mode", "add");
        startActivity(intent);
    }
}
