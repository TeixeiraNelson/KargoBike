package ch.ribeironelson.kargobike.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;

public class DetailsActivity extends AppCompatActivity {

    private EditText DateData;
    private EditText ProductData;
    private EditText NumberData;
    private EditText ClientData;
    private EditText CyclistData;
    private ImageView DeliveryPicture;
    private ImageView SignaturPicture;
    private DeliveryEntity Delivery;

    public DetailsActivity(DeliveryEntity Delivery){
    this.Delivery = Delivery;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        DateData = findViewById(R.id.DateData);
        ProductData = findViewById(R.id.ProductData);
        NumberData= findViewById(R.id.NumberData);
        ClientData = findViewById(R.id.ClientData);
        CyclistData = findViewById(R.id.CyclistData);
        DeliveryPicture=findViewById(R.id.DeliveryPicture);
        SignaturPicture=findViewById(R.id.SignaturPicture);

        updateUI();
    }

    private void updateUI() {
        DateData.setText(Delivery.getDeliveryDateTime());
        ProductData.setText(Delivery.getIdProduct());
        NumberData.setText(Delivery.getNbPackages());
        ClientData.setText(Delivery.getFinalDestination());
        CyclistData.setText(Delivery.getIdUser());
        DeliveryPicture.setI(Delivery.getProofPictureName());
        SignaturPicture.setI(Delivery.getSignatureImageName());

    }
}
