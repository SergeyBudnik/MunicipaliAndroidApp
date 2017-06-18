package acropollis.municipali;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;

import acropollis.municipali.binders.MenuBinder;
import acropollis.municipali.utls.BitmapUtils;
import acropollis.municipali.view.common.BackInterceptableRelativeLayout;

@EActivity(R.layout.activity_report_write_comment)
public class ReportWriteCommentActivity extends BaseActivity {
    @Extra("photoUri")
    String photoUri;
    @Extra("comment")
    String comment;

    @ViewById(R.id.container)
    BackInterceptableRelativeLayout backInterceptableRelativeLayout;

    @ViewById(R.id.photo_layout)
    RelativeLayout photoLayoutView;
    @ViewById(R.id.photo)
    ImageView photoView;
    @ViewById(R.id.comment)
    EditText commentView;

    @Bean
    MenuBinder menuBinder;

    @AfterViews
    void init() {
        if (photoUri != null) {
            photoLayoutView.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                getContentResolver(),
                                Uri.parse(photoUri)
                        );

                        if (bitmap.getWidth() > bitmap.getHeight()) {
                            bitmap = BitmapUtils.rotate(bitmap, 90);
                        }

                        bitmap = BitmapUtils.scale(
                                bitmap,
                                bitmap.getWidth() * photoLayoutView.getHeight() / bitmap.getHeight(),
                                photoLayoutView.getHeight()
                        );

                        photoView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        /* ToDo: Ex handling */
                    }
                }
            });
        }

        commentView.setText(comment);

        if (commentView.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        backInterceptableRelativeLayout.setSearchActivity(this);
    }

    @Override
    public void onBackPressed() {
        doFinish();
    }

    @Click(R.id.back_button)
    void onBackButtonClick() {
        doFinish();
    }

    private void doFinish() {
        finishRedirectForResult(0, 0, RESULT_OK,
                Collections.<String, Serializable>singletonMap("comment", commentView.getText().toString()));
    }
}
