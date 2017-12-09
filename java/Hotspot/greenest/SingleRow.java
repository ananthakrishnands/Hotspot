package greenest.greenest;

/**
 * Created by admin on 1/25/2016.
 */
public class SingleRow {
    String name;
    String date;
    String message;
    String image;
    String dp;
   // short btnLike_status=0;
   // short btnDislike_status=0;

    public SingleRow(String name,String message, String image,String dp,String date) {
        this.dp = dp;
        this.image = image;
        this.message = message;
        this.name = name;
        this.date=date;
        //this.btnLike_status=st;
        //this.btnDislike_status=dst;
    }
}
