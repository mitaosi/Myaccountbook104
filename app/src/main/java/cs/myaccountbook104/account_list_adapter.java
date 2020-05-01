package cs.myaccountbook104;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class account_list_adapter extends BaseAdapter {


    private Context context;
    private List<account_list_model> list;
    public DataBaseHelper dbHelper;

    public account_list_adapter(Context context, List<account_list_model> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold;
        if (convertView == null) {
            hold = new ViewHold();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.accountlist_item, null);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        hold.imageView = (ImageView) convertView
                .findViewById(R.id.account_imageview);
        hold.account_time = (TextView) convertView.findViewById(R.id.account_time);
        hold.account_name = (TextView) convertView.findViewById(R.id.account_name);
        hold.account_number = (TextView) convertView.findViewById(R.id.account_number);
       // hold.DeleteButton = (View) convertView.findViewById(R.id.delete_button);
        //hold.deleteaccount = (ImageButton)convertView.findViewById(R.id.deleteaccountitem);

        hold.imageView.setImageResource(list.get(position).getImageView());
        hold.account_time.setText(list.get(position).getAccount_time());
        hold.account_name.setText(list.get(position).getAccount_name());
        hold.account_number.setText(list.get(position).getAccount_number());

        dbHelper = new DataBaseHelper(this.context);


        return convertView;
    }

    class ViewHold {
        public ImageView imageView;
        public TextView account_time;
        public TextView account_name;
        public TextView account_number;
        //View DeleteButton;
        //public ImageButton deleteaccount;
    }


}
