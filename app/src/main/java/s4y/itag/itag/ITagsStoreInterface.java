package s4y.itag.itag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import s4y.rasat.Observable;

public interface ITagsStoreInterface {
    int count();
    @NonNull
    Observable<StoreOp> observable();
    @Nullable
    ITagInterface byId(@NonNull String id);
    ITagInterface byPos(int pos);
    ITagInterface everById(@NonNull String id);
    @NonNull
    String[] forgottenIDs();
    void forget(@NonNull String id);
    void remember(@NonNull ITagInterface tag);
    boolean remembered(@NonNull String id);
    void setAlert(@NonNull String id,boolean alert);
    void setColor(@NonNull String id,@NonNull TagColor color);
    void setName(@NonNull String id,String name);
    void connectAll();
    void stopAlertAll();
}