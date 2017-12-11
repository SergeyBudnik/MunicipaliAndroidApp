package acropollis.municipali.view.article;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;
import acropollis.municipalidata.dto.article.TranslatedArticle;

@EViewGroup(R.layout.view_article_categories)
public class ArticleCategoriesView extends LinearLayout {
    @ViewById(R.id.categories_layout)
    View categoriesLayout;
    @ViewById(R.id.category_1)
    TextView categoryView1;
    @ViewById(R.id.category_2)
    TextView categoryView2;
    @ViewById(R.id.category_3)
    TextView categoryView3;

    public ArticleCategoriesView(Context context) {
        super(context);
    }

    public ArticleCategoriesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(TranslatedArticle article) {
        if (article.getCategories().size() == 0) {
            setVisibility(GONE);
        } else {
            showCategory(article, 0, categoryView1);
            showCategory(article, 1, categoryView2);
            showCategory(article, 2, categoryView3);
        }
    }

    private void showCategory(TranslatedArticle article, int index, TextView categoryView) {
        if (article.getCategories().size() > index) {
            String category = article.getCategories().get(index);

            categoriesLayout.setVisibility(View.VISIBLE);

            categoryView.setVisibility(View.VISIBLE);
            categoryView.setText(category);
        }
    }
}
