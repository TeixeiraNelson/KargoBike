package ch.ribeironelson.kargobike.database.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.ribeironelson.kargobike.database.entity.ProductEntity;
import ch.ribeironelson.kargobike.database.firebase.ProductLiveData;
import ch.ribeironelson.kargobike.database.firebase.ProductListLiveData;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class ProductRepository {
    private static ProductRepository instance ;

    public static ProductRepository getInstance(){
        if(instance == null) {
            synchronized (RoleRepository.class) {
                if (instance == null) {
                    instance = new ProductRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<List<ProductEntity>> getAllProducts() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("products");

        return new ProductListLiveData(reference);
    }

    public LiveData<ProductEntity> getProduct(final String productId){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("products")
                .child(productId);
        return new ProductLiveData(reference);
    }

    public void insertProduct(final ProductEntity product, OnAsyncEventListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("products");
        String key = reference.push().getKey();
        product.setIdProduct(key);
        FirebaseDatabase.getInstance()
                .getReference("products")
                .child(key)
                .setValue(product, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void updateProduct(final ProductEntity product, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("products")
                .child(product.getIdProduct())
                .updateChildren(product.toMap(),(databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void deleteProduct(final ProductEntity product, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("products")
                .child(product.getIdProduct())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
