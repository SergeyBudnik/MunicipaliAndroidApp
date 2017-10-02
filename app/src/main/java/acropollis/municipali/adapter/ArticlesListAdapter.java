package acropollis.municipali.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import acropollis.municipali.comparators.ArticlesComparator;
import acropollis.municipali.view.ArticlesListRowView;
import acropollis.municipali.view.ArticlesListRowView_;
import acropollis.municipalidata.dto.article.ArticleType;
import acropollis.municipalidata.dto.article.TranslatedArticle;

@EBean
public class ArticlesListAdapter extends BaseAdapter {
    @RootContext
    Context context;

    private List<TranslatedArticle> articles = new ArrayList<>();

    public void setElements(ArticleType articlesType, Collection<TranslatedArticle> articles) {
        this.articles = Stream.of(articles)
                .filter(it -> it.getType() == articlesType)
                .sorted(new ArticlesComparator())
                .collect(Collectors.toList());

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public TranslatedArticle getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ArticlesListRowView getView(int position, View convertView, ViewGroup parent) {
        ArticlesListRowView v = convertView == null ?
                ArticlesListRowView_.build(context) :
                (ArticlesListRowView) convertView;

        v.bind(getItem(position));

        return v;
    }
}
