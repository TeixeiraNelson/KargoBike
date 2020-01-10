package ch.ribeironelson.kargobike.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.ribeironelson.kargobike.BaseApp;
import ch.ribeironelson.kargobike.database.entity.CheckpointEntity;
import ch.ribeironelson.kargobike.database.entity.RoleEntity;
import ch.ribeironelson.kargobike.database.repository.CheckpointRepository;
import ch.ribeironelson.kargobike.database.repository.RoleRepository;

public class RoleListViewModel extends AndroidViewModel {
    private static final String TAG = "RoleListViewModel";

    private RoleRepository mRepository ;

    private final MediatorLiveData<List<RoleEntity>> mObservableRoles;

    public RoleListViewModel(@NonNull Application app, RoleRepository repo){
        super(app);

        mRepository = repo ;

        mObservableRoles = new MediatorLiveData<>() ;

        mObservableRoles.setValue(null);

        LiveData<List<RoleEntity>> roles = mRepository.getAllRoles();

        mObservableRoles.addSource(roles, mObservableRoles::setValue);

    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final RoleRepository mRepository;

        public Factory(@NonNull Application application) {
            mApplication = application;
            mRepository = ((BaseApp) application).getRoleRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RoleListViewModel(mApplication, mRepository);
        }
    }

    /**
     * Expose the LiveData ClientWithAccounts query so the UI can observe it.
     */
    public LiveData<List<RoleEntity>> getRoles() {
        return mObservableRoles;
    }
}
