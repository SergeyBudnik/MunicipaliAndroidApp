package acropollis.municipali.bootstrap.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import acropollis.municipali.R;
import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipali.bootstrap.list.adapter.MunicipaliListAdapter;

@EViewGroup(R.layout.bootstrap_view_refreshable_list)
public class MunicipaliRefreshableListView extends RelativeLayout implements SwipeRefreshLayout.OnRefreshListener {
    public interface RowsLoader {
        void load(LoadingListener listener);
    }

    public interface LoadingListener {
        void onReadingFromCache(List<MunicipaliRowData> elements);
        void onSuccess(List<MunicipaliRowData> elements);
        void onFailure();
    }

    @ViewById(R.id.list_refresh)
    SwipeRefreshLayout listRefreshView;
    @ViewById(R.id.list)
    ListView listView;
    @ViewById(R.id.pull_to_retry)
    View pullToRetryView;

    @Bean
    MunicipaliListAdapter listAdapter;

    private RowsLoader rowsLoader;

    private AtomicBoolean loadingInProgress = new AtomicBoolean(false);

    private LoadingListener loadingListener = new LoadingListener() {
        @Override
        public void onReadingFromCache(List<MunicipaliRowData> elements) {
            populateElements(elements);
        }

        @Override
        public void onSuccess(List<MunicipaliRowData> elements) {
            onLoadingSuccessful(elements);
        }

        @Override
        public void onFailure() {
            onLoadingFailed();
        }
    };

    public MunicipaliRefreshableListView(Context context) {
        super(context);
    }

    public MunicipaliRefreshableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(RowsLoader rowsLoader) {
        this.rowsLoader = rowsLoader;

        listView.setAdapter(listAdapter);

        listRefreshView.setOnRefreshListener(this);

        startLoading();
    }

    public void reinit() {
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        pullToRetryView.setVisibility(GONE);

        startLoading();
    }

    @UiThread
    void onLoadingSuccessful(List<MunicipaliRowData> elements) {
        loadingInProgress.set(false);
        listRefreshView.setRefreshing(false);

        populateElements(elements);
    }

    @UiThread
    void populateElements(List<MunicipaliRowData> elements) {
        listAdapter.setElements(elements);
    }

    @UiThread
    void onLoadingFailed() {
        loadingInProgress.set(false);
        listRefreshView.setRefreshing(false);

        if (listAdapter.getCount() == 0) {
            pullToRetryView.setVisibility(VISIBLE);
        }
    }

    private void startLoading() {
        if (loadingInProgress.get()) {
            return;
        }

        synchronized (this) {
            loadingInProgress.set(true);
            listRefreshView.setRefreshing(true);

            rowsLoader.load(loadingListener);
        }
    }
}
