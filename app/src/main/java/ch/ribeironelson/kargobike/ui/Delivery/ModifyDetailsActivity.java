package ch.ribeironelson.kargobike.ui.Delivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;

public class ModifyDetailsActivity extends AppCompatActivity {
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
    private Button ButtonSave;
    private Button ButtonBack;
    private DeliveryEntity Delivery;

    public ModifyDetailsActivity(DeliveryEntity Delivery) {
        this.Delivery = Delivery;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        DateData = findViewById(R.id.DateData);
        HoursData = findViewById(R.id.HoursData);
        ProductData = findViewById(R.id.ProductData);
        DepartData = findViewById(R.id.DepartData);
        ArrivalData = findViewById(R.id.ArrivalData);
        NumberData = findViewById(R.id.NumberData);
        ClientData = findViewById(R.id.ClientData);
        CyclistData = findViewById(R.id.CyclistData);
        DeliveryPicture = findViewById(R.id.DeliveryPicture);
        SignaturPicture = findViewById(R.id.SignaturPicture);
        // Capture button clicks
        ButtonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(ModifyDetailsActivity.this,
                        DetailsActivity.class);
                startActivity(myIntent);
            }

        });
        ButtonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(ModifyDetailsActivity.this,
                        DetailsActivity.class);
                startActivity(myIntent);
            }

        });
    }
}
