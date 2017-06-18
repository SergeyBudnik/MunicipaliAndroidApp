package acropollis.municipali.binders;

import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;

import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class MenuBinder {
    public void bind(final DrawerLayout root) {
        root.setScrimColor(Color.TRANSPARENT);
    }
}