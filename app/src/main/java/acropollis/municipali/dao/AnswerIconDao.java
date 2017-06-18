package acropollis.municipali.dao;

import android.content.Context;
import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.IOException;

import acropollis.municipali.utls.StorageUtils;

@EBean(scope = EBean.Scope.Singleton)
public class AnswerIconDao {
    @RootContext
    Context context;

    public byte [] getIcon(long questionId, long answerId) {
        try {
            return StorageUtils.readData(context, getFileName(questionId, answerId));
        } catch (IOException e) {
            return null;
        }
    }

    public void addIcon(long questionId, long answerId, byte [] iconBytes) {
        try {
            StorageUtils.writeData(context, getFileName(questionId, answerId), iconBytes);
        } catch (IOException e) {
            Log.w(AnswerIconDao.class.getSimpleName(), "Icon save failed");
        }
    }

    public void removeIcon(long questionId, long answerId) {
        try {
            StorageUtils.clearData(context, getFileName(questionId, answerId));
        } catch (IOException e) {
            Log.w(AnswerIconDao.class.getSimpleName(), "Icon removal failed");
        }
    }

    private String getFileName(long questionId, long answerId) {
        return AnswerIconDao.class.getCanonicalName() + "." + questionId + "." + answerId;
    }
}
