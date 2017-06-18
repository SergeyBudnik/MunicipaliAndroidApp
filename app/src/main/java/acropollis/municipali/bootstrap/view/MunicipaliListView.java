package acropollis.municipali.bootstrap.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import acropollis.municipali.R;
import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipali.bootstrap.list.adapter.MunicipaliListAdapter;

@EViewGroup(R.layout.bootstrap_view_list)
public class MunicipaliListView extends RelativeLayout {
    @ViewById(R.id.list)
    ListView listView;

    @Bean
    MunicipaliListAdapter listAdapter;

    public MunicipaliListView(Context context) {
        super(context);
    }

    public MunicipaliListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init() {
        listView.setAdapter(listAdapter);
    }

    public void setElements(List<MunicipaliRowData> elements) {
        listAdapter.setElements(elements);
    }

    public void setItemSelected(int position) {
        listAdapter.setItemSelected(position);
    }

    public MunicipaliRowData getElement(int position) {
        return listAdapter.getItem(position);
    }
}
