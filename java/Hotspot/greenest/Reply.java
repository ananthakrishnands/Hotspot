package greenest.greenest;

/**
 * Created by admin on 3/15/2016.
 */
public class Reply {
    String name;
    String message;
    String dp;
    // short btnLike_status=0;
    // short btnDislike_status=0;

    public Reply(String name,String message, String dp) {
        this.dp= dp;
        this.message = message;
        this.name = name;

    }
}
