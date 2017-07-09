package acropollis.municipali.data.article;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
public class ArticleTranslationInfo implements Serializable {
    @NonNull private String title;
    @NonNull private String text;
    @NonNull private List<String> categories;
}
