package acropollis.municipalidata.dto.article;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
public class ArticleTranslationInfo implements Serializable {
    @NonNull private String title;
    @NonNull private String description;
    @NonNull private String text;
    @NonNull private List<String> categories;
}
