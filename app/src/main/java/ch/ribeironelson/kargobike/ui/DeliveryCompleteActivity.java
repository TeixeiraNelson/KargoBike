package ch.ribeironelson.kargobike.ui;

import androidx.appcompat.app.AppCompatActivity;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class DeliveryCompleteActivity extends AppCompatActivity {
    private DeliveryEntity delivery;

    private Button deliveryDetailsBtn;
    private Button takeAPicBtn;
    private Button SignatureBtn;
    private Button validateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_complete);

        Intent i = getIntent();
        delivery = (DeliveryEntity)i.getSerializableExtra("DeliveryEntity");

        deliveryDetailsBtn = findViewById(R.id.delivery_details_btn);
        takeAPicBtn = findViewById(R.id.take_a_pic_btn);
        SignatureBtn = findViewById(R.id.signature_btn);
        validateBtn = findViewById(R.id.validate_btn);
        
    }
}
