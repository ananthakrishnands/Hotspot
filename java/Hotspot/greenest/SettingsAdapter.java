package greenest.greenest;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by admin on 2/4/2016.
 */
public class SettingsAdapter extends BaseAdapter {
    ArrayList<String> list;
    String []contents={"Account","Feedback","About"};
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {





        return null;




    }

    public SettingsAdapter() {
        this.contents = contents;
        list=new ArrayList<String>();
        for (int i=0;i<3;i++){
            list.add(contents[i]);
        }
    }
}
