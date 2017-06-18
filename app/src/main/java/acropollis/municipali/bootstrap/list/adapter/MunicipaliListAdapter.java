package acropollis.municipali.bootstrap.list.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipali.bootstrap.view.MunicipaliRowView;
import acropollis.municipali.bootstrap.view.MunicipaliRowView_;

@EBean
public class MunicipaliListAdapter extends BaseAdapter {
    @RootContext
    Context context;

    private Integer selectedItemPosition = null;
    private List<MunicipaliRowData> elements = new ArrayList<>();

    public void setElements(List<MunicipaliRowData> elements) {
        this.elements = elements;

        notifyDataSetChanged();
    }

    public void setItemSelected(int position) {
        if (selectedItemPosition == null || selectedItemPosition != position) {
            selectedItemPosition = position;
        } else {
            selectedItemPosition = null;
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public MunicipaliRowData getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MunicipaliRowView getView(int position, View convertView, ViewGroup parent) {
        MunicipaliRowView v = convertView == null ?
                MunicipaliRowView_.build(context) :
                (MunicipaliRowView) convertView;

        v.bind(getItem(position));

        if (selectedItemPosition != null && selectedItemPosition.equals(position)) {
            v.setRedPalette();
        } else {
            v.setWhitePalette();
        }

        return v;
    }
}
