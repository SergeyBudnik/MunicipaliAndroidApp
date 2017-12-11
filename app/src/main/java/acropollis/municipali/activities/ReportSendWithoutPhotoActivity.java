package acropollis.municipali.activities;

import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import acropollis.municipali.R;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.rest.wrappers.omega.ReportRestWrapper;
import acropollis.municipali.view.HeaderView;
import acropollis.municipalibootstrap.views.MunicipaliButtonView;

@EActivity(R.layout.activity_report_send_without_photo)
public class ReportSendWithoutPhotoActivity extends BaseActivity {
    @ViewById(R.id.header)
    HeaderView headerView;
    @ViewById(R.id.comment)
    EditText commentView;
    @ViewById(R.id.send)
    MunicipaliButtonView sendButtonView;

    @ViewById(R.id.progress_container)
    View progressContainerView;
    @ViewById(R.id.progress_spinner)
    View progressSpinnerView;

    @Bean
    ReportRestWrapper reportRestWrapper;

    @AnimationRes(R.anim.spinner)
    Animation spinnerAnimation;

    @AfterViews
    void init() {
        headerView.setStyle(HeaderView.Style.DEFAULT);
        headerView.setTitle(R.string.title_report);
        headerView.setLeftButton(R.drawable.back_button, it -> finish());

        sendButtonView.setEnabled(false);
    }

    @TextChange(R.id.comment)
    void onCommentChange() {
        String comment = commentView.getText().toString().trim();

        sendButtonView.setEnabled(!comment.isEmpty());
    }

    @Click(R.id.send)
    void onSend() {
        String comment = commentView.getText().toString().trim();

        progressContainerView.setVisibility(View.VISIBLE);

        progressSpinnerView.setAnimation(spinnerAnimation);

        reportRestWrapper.postReport(null, comment, 0.0, 0.0, new RestListener<Void>() {
            @Override
            public void onSuccess(Void o) {
                successRedirect();
            }

            @Override
            public void onFailure() {
                failureRedirect();
            }
        });
    }

    @UiThread
    void successRedirect() {
        progressSpinnerView.clearAnimation();
        progressContainerView.setVisibility(View.INVISIBLE);

        doSuccessRedirect();
    }

    @UiThread
    void doSuccessRedirect()  {
        finishRedirectForResult(0, 0, RESULT_OK);
    }

    @UiThread
    void failureRedirect() {
        progressSpinnerView.clearAnimation();
        progressContainerView.setVisibility(View.INVISIBLE);

        finishRedirectForResult(0, 0, RESULT_CANCELED);
    }
}
