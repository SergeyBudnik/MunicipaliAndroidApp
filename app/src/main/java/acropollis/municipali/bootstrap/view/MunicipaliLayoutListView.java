package acropollis.municipali.bootstrap.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import acropollis.municipali.R;
import acropollis.municipali.bootstrap.data.MunicipaliRowData;

@EViewGroup(R.layout.bootstrap_view_layout_list)
public class MunicipaliLayoutListView extends LinearLayout {
    public interface RowClickListener {
        void onClick(int position, MunicipaliRowData rowData);
    }

    @ViewById(R.id.list_layout_root)
    ViewGroup listLayoutRoot;

    private int selectedPosition = -1;

    public MunicipaliLayoutListView(Context context) {
        super(context);
    }

    public MunicipaliLayoutListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setElements(List<MunicipaliRowData> elements, final RowClickListener rowClickListener) {
        int position = 0;

        listLayoutRoot.removeAllViews();

        for (final MunicipaliRowData e : elements) {
            final int currentPosition = position;

            MunicipaliRowView v = MunicipaliRowView_.build(getContext());

            v.bind(e);
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    rowClickListener.onClick(currentPosition, e);
                }
            });

            listLayoutRoot.addView(v);

            position++;
        }
    }

    public void setItemSelected(int position) {
        for (int i = 0; i < listLayoutRoot.getChildCount(); i++) {
            MunicipaliRowView v = ((MunicipaliRowView) listLayoutRoot.getChildAt(i));

            if (i == position && selectedPosition != position) {
                v.setRedPalette();
            } else {
                v.setWhitePalette();
            }
        }

        selectedPosition = selectedPosition == position ? -1 : position;
    }

    public MunicipaliRowData getElement(int position) {
        return ((MunicipaliRowView) listLayoutRoot.getChildAt(position)).getRowData();
    }
}
