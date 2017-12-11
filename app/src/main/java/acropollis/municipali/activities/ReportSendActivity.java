package acropollis.municipali.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import acropollis.municipali.R;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.rest.wrappers.omega.ReportRestWrapper;
import acropollis.municipali.service.UserService;
import acropollis.municipali.utls.BitmapUtils;
import acropollis.municipali.view.HeaderView;
import acropollis.municipalibootstrap.views.MunicipaliButtonView;
import acropollis.municipalibootstrap.views.MunicipaliLoadableImageView;

import static acropollis.municipali.utls.BitmapUtils.iconToBytes;
import static acropollis.municipali.utls.BitmapUtils.rotate;
import static acropollis.municipali.utls.BitmapUtils.scale;

@EActivity(R.layout.activity_report_send)
public class ReportSendActivity extends BaseActivity {
    private static final int COMMENTS_REQUEST_CODE = 1;

    @ViewById(R.id.root)
    DrawerLayout rootView;

    @ViewById(R.id.header)
    HeaderView headerView;
    @ViewById(R.id.photo)
    MunicipaliLoadableImageView photoView;
    @ViewById(R.id.comment)
    TextView commentView;

    @ViewById(R.id.send)
    MunicipaliButtonView sendButtonView;

    @ViewById(R.id.progress_container)
    View progressContainerView;
    @ViewById(R.id.progress_spinner)
    View progressSpinnerView;

    @Extra("photoUri")
    String photoUri;

    @Bean
    UserService userService;

    @Bean
    ReportRestWrapper reportRestWrapper;

    @AnimationRes(R.anim.spinner)
    Animation spinnerAnimation;

    private byte [] photoToSendBytes;

    private Bitmap initialBitmap;

    private int angle = 0;

    private AtomicBoolean rotateIsInProgress = new AtomicBoolean(false);

    @AfterViews
    void init() {
        headerView.setStyle(HeaderView.Style.TRANSPARENT);
        headerView.setTitle(R.string.article);
        headerView.setLeftButton(R.drawable.back_button, it -> finishRedirectForResult(0, 0, RESULT_CANCELED));

        if (photoUri != null) {
            try {
                initialBitmap = getInitialBitmap();

                photoView.configure(
                        "ReportPhoto",
                        R.color.gray_2,
                        () -> BitmapUtils.iconToBytes(initialBitmap),
                        () -> null
                );
            } catch (IOException e) {
                finishRedirectForResult(0, 0, RESULT_CANCELED);
            }
        }
    }

    private void onRotateClick() {
        if (!rotateIsInProgress.get()) {
            synchronized (this) {
                if (!rotateIsInProgress.get()) {
                    sendButtonView.setEnabled(false);
                    headerView.removeRightButton();

                    rotateIsInProgress.set(true);

                    angle += 90;

                    if (angle >= 360) {
                        angle = 0;
                    }

                    processUIBitmap();
                }
            }
        }
    }

    @TextChange(R.id.comment)
    void onCommentChange() {
        String comment = commentView.getText().toString().trim();

        sendButtonView.setEnabled(!comment.isEmpty());
    }

    @Click(R.id.photo)
    void onPhotoClick() {
        onRotateClick();
    }

    @Click(R.id.send)
    void onSendButtonClick() {
        if (!rotateIsInProgress.get()) {
            synchronized (this) {
                if (!rotateIsInProgress.get()) {
                    sendButtonView.setEnabled(false);

                    progressContainerView.setVisibility(View.VISIBLE);
                    progressSpinnerView.startAnimation(spinnerAnimation);

                    sendReport();
                }
            }
        }
    }

    @Background
    void sendReport() {
        String comment = commentView.getText().toString().trim();

        reportRestWrapper.postReport(photoToSendBytes, comment, 0.0, 0.0, new RestListener<Void>() {
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
    void successRedirect()  {
        finishRedirectForResult(0, 0, RESULT_OK);
    }

    @UiThread
    void failureRedirect() {
        progressSpinnerView.clearAnimation();
        progressContainerView.setVisibility(View.INVISIBLE);

        finishRedirectForResult(0, 0, RESULT_CANCELED);
    }

    @Background
    void processUIBitmap() {
        Bitmap bitmap = rotate(initialBitmap, angle);

        bitmap = scale(bitmap, 800, bitmap.getHeight() * 800 / bitmap.getWidth());

        photoToSendBytes = iconToBytes(bitmap);

        bitmap = scale(
                bitmap,
                bitmap.getWidth() * photoView.getHeight() / bitmap.getHeight(),
                photoView.getHeight()
        );

        onUiBitmapProcessed(bitmap);
    }

    @UiThread
    void onUiBitmapProcessed(Bitmap bitmap) {
        photoView.setImageBitmap(bitmap);

        rotateIsInProgress.set(false);
    }

    private Bitmap getInitialBitmap() throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                getContentResolver(),
                Uri.parse(photoUri)
        );

        int minSize = Math.min(bitmap.getWidth(), bitmap.getHeight());

        return scale(
                bitmap,
                bitmap.getWidth() * 800 / minSize,
                bitmap.getHeight() * 800 / minSize
        );
    }
}
