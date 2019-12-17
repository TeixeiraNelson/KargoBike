package ch.ribeironelson.kargobike.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.ribeironelson.kargobike.BaseApp;
import ch.ribeironelson.kargobike.database.entity.ProductEntity;
import ch.ribeironelson.kargobike.database.repository.ProductRepository;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class ProductViewModel extends AndroidViewModel {
    private Application application;
    private ProductRepository repository;
    private final MediatorLiveData<ProductEntity> observableProduct;

    public ProductViewModel(@NonNull Application application,
                             final String productId, ProductRepository repository) {
        super(application);

        this.application = application;
        this.repository = repository;

        observableProduct = new MediatorLiveData<>();
        observableProduct.setValue(null);

        LiveData<ProductEntity> product = repository.getProduct(productId);

        observableProduct.addSource(product, observableProduct::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String productId;
        private final ProductRepository repository;

        public Factory(@NonNull Application application, String productId) {
            this.application = application;
            this.productId = productId;
            repository = ((BaseApp) application).getProductRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProductViewModel(application, productId, repository);
        }
    }

    public LiveData<ProductEntity> getProduct() {
        return observableProduct;
    }

    public void createProduct(ProductEntity product, OnAsyncEventListener callback) {
        repository.insertProduct(product, callback);
    }

    public void updateProduct(ProductEntity product, OnAsyncEventListener callback) {
        repository.updateProduct(product, callback);
    }

    public void deleteProduct(ProductEntity product, OnAsyncEventListener callback){
        repository.deleteProduct(product,callback);
    }
}
