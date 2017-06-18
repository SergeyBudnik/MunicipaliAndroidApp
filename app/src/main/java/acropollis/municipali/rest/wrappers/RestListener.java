package acropollis.municipali.rest.wrappers;

public abstract class RestListener<T> {
    public void onStart() {}
    public void onSuccess(T o) {}
    public void onFailure() {}
}
