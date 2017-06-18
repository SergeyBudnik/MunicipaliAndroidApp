package acropollis.municipali.bootstrap.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.util.concurrent.atomic.AtomicBoolean;

import acropollis.municipali.R;

@EViewGroup(R.layout.bootstrap_view_loading)
public class MunicipaliLoadingView extends RelativeLayout {
    public enum LoadingFailureAction {
        REDIRECT, STAY
    }

    public interface Loader {
        void onStart();
        LoadingFailureAction onFailure();
    }

    @ViewById(R.id.spinner)
    public View spinnerView;
    @ViewById(R.id.loading_failed)
    public View loadingFailedView;

    @AnimationRes(R.anim.spinner)
    Animation spinnerAnimation;

    private Loader loader;

    private AtomicBoolean loadingInProgress = new AtomicBoolean(false);

    public MunicipaliLoadingView(Context context) {
        super(context);
    }

    public MunicipaliLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(Loader loader) {
        this.loader = loader;
    }

    @Click(R.id.loading_failed)
    void onLoadingRetry() {
        startLoading();
    }

    public void startLoading() {
        if (!loadingInProgress.get()) {
            synchronized (this) {
                if (!loadingInProgress.get()) {
                    loadingInProgress.set(true);

                    spinnerView.setVisibility(View.VISIBLE);
                    loadingFailedView.setVisibility(View.GONE);

                    spinnerView.startAnimation(spinnerAnimation);

                    loader.onStart();
                }
            }
        }
    }

    @UiThread(delay = 1000)
    public void onLoadingFailed() {
        LoadingFailureAction action = loader.onFailure();

        if (action == LoadingFailureAction.STAY) {
            loadingInProgress.set(false);

            spinnerView.clearAnimation();

            spinnerView.setVisibility(View.INVISIBLE);
            loadingFailedView.setVisibility(View.VISIBLE);
        }
    }
}
