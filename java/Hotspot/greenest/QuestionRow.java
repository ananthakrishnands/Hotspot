package greenest.greenest;

import android.graphics.Bitmap;

/**
 * Created by admin on 1/30/2016.
 */
public class QuestionRow {
    String name;
    Bitmap dp;
    String question;

    public QuestionRow(String name, Bitmap dp, String question) {
        this.name = name;
        this.dp = dp;
        this.question = question;
    }
}
