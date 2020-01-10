package ch.ribeironelson.kargobike.database.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.entity.ProductEntity;

public class ProductListLiveData extends LiveData<List<ProductEntity>> {
    private static final String TAG = "ProductListLiveData";

    private final DatabaseReference reference;
    private final ProductListLiveData.MyValueEventListener listener = new ProductListLiveData.MyValueEventListener();

    public ProductListLiveData(DatabaseReference ref) {
        reference = ref;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toProduct(dataSnapshot));
            setValue(toProduct(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<ProductEntity> toProduct(DataSnapshot snapshot) {
        List<ProductEntity> products = new ArrayList<>();
        Log.d(TAG,"Transforming into deliveries");
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            ProductEntity entity = childSnapshot.getValue(ProductEntity.class);
            entity.setIdProduct(childSnapshot.getKey());
            products.add(entity);
        }
        return products;
    }
}
