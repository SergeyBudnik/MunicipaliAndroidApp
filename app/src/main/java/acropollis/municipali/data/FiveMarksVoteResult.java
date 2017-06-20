package acropollis.municipali.data;

import java.io.Serializable;
import java.util.Locale;

import acropollis.municipali.R;
import lombok.Getter;

public enum FiveMarksVoteResult implements Serializable {
    VOTE_1(1, 1, R.string.five_marks_strongly_disagree_text),
    VOTE_2(2, 2, R.string.five_marks_disagree_text),
    VOTE_3(3, 3, R.string.five_marks_neutral_text),
    VOTE_4(4, 4, R.string.five_marks_agree_text),
    VOTE_5(5, 5, R.string.five_marks_strongly_agree_text);

    @Getter int index;
    @Getter int segmentsAmount;
    @Getter int voteValueTextId;

    FiveMarksVoteResult(int index, int segmentsAmount, int voteValueTextId) {
        this.index = index;
        this.segmentsAmount = segmentsAmount;
        this.voteValueTextId = voteValueTextId;
    }

    public static FiveMarksVoteResult getVoteResult(int index) {
        for (FiveMarksVoteResult o : values()) {
            if (o.getIndex() == index) {
                return o;
            }
        }

        throw new RuntimeException(String.format(Locale.ENGLISH, "Unexpected index '%d'", index));
    }
}
