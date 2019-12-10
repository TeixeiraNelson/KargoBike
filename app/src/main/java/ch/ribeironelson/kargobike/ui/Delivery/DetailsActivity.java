package ch.ribeironelson.kargobike.ui.Delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;

public class DetailsActivity extends AppCompatActivity {

    private EditText DateData;
    private EditText HoursData;
    //ProductData = description de la delivery
    private EditText ProductData;
    private EditText DepartData;
    private EditText ArrivalData;
    private EditText NumberData;
    private EditText ClientData;
    private EditText CyclistData;
    private ImageView DeliveryPicture;
    private ImageView SignaturPicture;
    private ImageButton ButtonSignature;
    private ImageButton ButtonProof;
    private Button ButtonModify;
    private DeliveryEntity Delivery;

    public DetailsActivity(DeliveryEntity Delivery) {
        this.Delivery = Delivery;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        DateData = findViewById(R.id.DateData);
        HoursData = findViewById(R.id.HoursData);
        ProductData = findViewById(R.id.DescriptionData);
        DepartData = findViewById(R.id.DepartData);
        ArrivalData = findViewById(R.id.ArrivalData);
        NumberData = findViewById(R.id.NumberData);
        ClientData = findViewById(R.id.ClientData);
        CyclistData = findViewById(R.id.CyclistData);
        DeliveryPicture = findViewById(R.id.DeliveryPicture);
        SignaturPicture = findViewById(R.id.SignaturPicture);
        // Capture button clicks
        ButtonModify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(DetailsActivity.this,
                        ModifyDetailsActivity.class);
                startActivity(myIntent);
            }
        });
    }
}

