package acropollis.municipali;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

import acropollis.municipali.bootstrap.view.MunicipaliRowView;
import acropollis.municipali.bootstrap_adapter.ArticleBootstrapAdapter;
import acropollis.municipali.data.article.TranslatedArticle;
import acropollis.municipali.drawers.history.HistoryData;
import acropollis.municipali.drawers.history.HistoryDrawer;
import acropollis.municipali.service.ArticlesService;

@EActivity(R.layout.activity_article_history)
public class ArticleHistoryActivity extends BaseActivity {
    private enum HistoryMode {
        DAILY, WEEKLY, MONTHLY
    }

    @ViewById(R.id.article_info)
    MunicipaliRowView articleInfoView;
    @ViewById(R.id.history_graph)
    ImageView historyGraphView;

    @ViewById(R.id.day)
    CardView dayView;
    @ViewById(R.id.week)
    CardView weekView;
    @ViewById(R.id.month)
    CardView monthView;

    @Bean
    ArticlesService articlesService;

    @Bean
    ArticleBootstrapAdapter articleBootstrapAdapter;

    @Extra("articleId")
    long articleId;

    private HistoryMode historyMode = HistoryMode.WEEKLY;
    private HistoryDrawer drawer = new HistoryDrawer();

    @AfterViews
    void init() {
        TranslatedArticle article = articlesService.getArticle(articleId);

        articleInfoView.bind(articleBootstrapAdapter.getArticleRowInfo(article, false));

        initHistory();
        drawHistory();
    }

    @Click(R.id.day)
    void onDayClick() {
        historyMode = HistoryMode.DAILY;

        dayView.setCardBackgroundColor(getResources().getColor(R.color.primary_orange));
        weekView.setCardBackgroundColor(getResources().getColor(R.color.primary_gray));
        monthView.setCardBackgroundColor(getResources().getColor(R.color.primary_gray));

        drawHistory();
    }

    @Click(R.id.week)
    void onWeekClick() {
        historyMode = HistoryMode.WEEKLY;

        dayView.setCardBackgroundColor(getResources().getColor(R.color.primary_gray));
        weekView.setCardBackgroundColor(getResources().getColor(R.color.primary_orange));
        monthView.setCardBackgroundColor(getResources().getColor(R.color.primary_gray));

        drawHistory();
    }

    @Click(R.id.month)
    void onMonthClick() {
        historyMode = HistoryMode.MONTHLY;

        dayView.setCardBackgroundColor(getResources().getColor(R.color.primary_gray));
        weekView.setCardBackgroundColor(getResources().getColor(R.color.primary_gray));
        monthView.setCardBackgroundColor(getResources().getColor(R.color.primary_orange));

        drawHistory();
    }

    private void initHistory() {
        drawer.setSelectedPoint(0);

        historyGraphView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                drawer.setSelectedPoint((int) event.getX());

                drawHistory();

                return true;
            }
        });
    }

    private void drawHistory() {
        historyGraphView.post(new Runnable() {
            @Override
            public void run() {
                int w = historyGraphView.getWidth();
                int h = historyGraphView.getHeight();

                if (w == 0 || h == 0) {
                    return;
                }

                Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);

                Canvas c = new Canvas(bm);

                switch (historyMode) {
                    case DAILY:
                        drawer.setHistory(getDailyHistory());
                        break;
                    case WEEKLY:
                        drawer.setHistory(getWeeklyHistory());
                        break;
                    case MONTHLY:
                        drawer.setHistory(getMonthlyHistory());
                        break;
                }

                drawer.draw(ArticleHistoryActivity.this, c);

                historyGraphView.setImageBitmap(bm);
            }
        });
    }

    @Click(R.id.back_button)
    void onBackButtonClick() {
        finish();
    }

    private HistoryData [] getDailyHistory() {
        long day = 24 * 3600000L;

        return new HistoryData [] {
                new HistoryData(3.1, new Date().getTime() - 7 * day),
                new HistoryData(4.1, new Date().getTime() - 6 * day),
                new HistoryData(3.8, new Date().getTime() - 5 * day),
                new HistoryData(3.5, new Date().getTime() - 4 * day),
                new HistoryData(2.2, new Date().getTime() - 3 * day),
                new HistoryData(3.6, new Date().getTime() - 2 * day),
                new HistoryData(4.2, new Date().getTime() - day),
        };
    }

    private HistoryData [] getWeeklyHistory() {
        long week = 7 * 24 * 3600000L;

        return new HistoryData [] {
                new HistoryData(4.1, new Date().getTime() - 7 * week),
                new HistoryData(2.1, new Date().getTime() - 6 * week),
                new HistoryData(3.6, new Date().getTime() - 5 * week),
                new HistoryData(4.5, new Date().getTime() - 4 * week),
                new HistoryData(2.8, new Date().getTime() - 3 * week),
                new HistoryData(3.9, new Date().getTime() - 2 * week),
                new HistoryData(4.7, new Date().getTime() - week),
        };
    }

    private HistoryData[] getMonthlyHistory() {
        long month = 30 * 24 * 3600000L;

        return new HistoryData [] {
                new HistoryData(1.1, new Date().getTime() - 7 * month),
                new HistoryData(4.1, new Date().getTime() - 6 * month),
                new HistoryData(5.0, new Date().getTime() - 5 * month),
                new HistoryData(3.1, new Date().getTime() - 4 * month),
                new HistoryData(3.9, new Date().getTime() - 3 * month),
                new HistoryData(3.2, new Date().getTime() - 2 * month),
                new HistoryData(4.9, new Date().getTime() - month),
        };
    }
}
