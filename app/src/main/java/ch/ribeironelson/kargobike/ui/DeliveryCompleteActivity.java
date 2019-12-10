package ch.ribeironelson.kargobike.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.repository.DeliveryRepository;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.util.TimeStamp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class DeliveryCompleteActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_RUNTIME_PERMISSION = 3;
    static final int REQUEST_SIGNATURE = 2;
    private static final String TAG = "DeliveryCompleteAc";

    private DeliveryEntity delivery;

    private Button deliveryDetailsBtn;
    private Button takeAPicBtn;
    private Button SignatureBtn;
    private Button validateBtn;

    private Boolean isSignature = false;
    private Bitmap imageBitmap;
    private ImageView imageView;

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
        imageView = findViewById(R.id.delivery_image);

        deliveryDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: HADLE DELIVERY DETAILS HERE.
            }
        });

        SignatureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryCompleteActivity.this, DrawActivity.class);
                startActivityForResult(intent, REQUEST_SIGNATURE);
            }
        });

        takeAPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDelivery();
            }
        });

    }

    private void checkPermission() {
        //select which permission you want
        final String permission = Manifest.permission.CAMERA;
        //final String permission = Manifest.permission.Storage;
        // if in fragment use getActivity()
        if (ContextCompat.checkSelfPermission(DeliveryCompleteActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DeliveryCompleteActivity.this, permission)) {

            } else {
                ActivityCompat.requestPermissions(DeliveryCompleteActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_RUNTIME_PERMISSION);
            }
        } else {
            // you have permission go ahead
            dispatchTakePictureIntent();
        }
    }


    private void saveDelivery() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String fileName =delivery.getIdDelivery()+".jpg";
        delivery.setAchievedTimeStamp(TimeStamp.getTimeStamp());

        // Create a reference with an initial file path and name
        StorageReference pathReference = storageRef.child(fileName);

        Bitmap bitmap = imageBitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = pathReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG,"Upload image error.");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if(!isSignature)
                    delivery.setProofPictureName(fileName);
                else
                    delivery.setSignatureImageName(fileName);

                DeliveryRepository.getInstance().update(delivery, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"Upload image successful.");
                        DeliveryCompleteActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }

        if (requestCode == REQUEST_SIGNATURE && resultCode == RESULT_OK) {
            byte[] byteArray = data.getByteArrayExtra("data");
            imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(imageBitmap);
        }

        if(imageBitmap!= null){
            imageView.setVisibility(View.VISIBLE);
            validateBtn.setVisibility(View.VISIBLE);
        }
    }


}
