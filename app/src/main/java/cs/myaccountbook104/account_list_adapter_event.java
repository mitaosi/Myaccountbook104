package cs.myaccountbook104;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class account_list_adapter_event extends BaseAdapter {


    private Context context;
    private List<account_list_model_event> list;
    public DataBaseHelper dbHelper;

    public account_list_adapter_event(Context context, List<account_list_model_event> list) {
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
        account_list_adapter_event.ViewHold hold;
        if (convertView == null) {
            hold = new account_list_adapter_event.ViewHold();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.accountlist_item_event, null);
            convertView.setTag(hold);
        } else {
            hold = (account_list_adapter_event.ViewHold) convertView.getTag();
        }

        hold.imageView = (ImageView) convertView
                .findViewById(R.id.account_imageview2);
        //hold.account_time = (TextView) convertView.findViewById(R.id.account_time);
        hold.account_name = (TextView) convertView.findViewById(R.id.account_name2);
        hold.account_number = (TextView) convertView.findViewById(R.id.account_number2);
        //hold.DeleteButton2 = (View) convertView.findViewById(R.id.delete_button2);
        //hold.deleteaccount = (ImageButton)convertView.findViewById(R.id.deleteaccountitem);


        hold.imageView.setImageResource(list.get(position).getImageView());
        //hold.account_time.setText(list.get(position).getAccount_time());
        hold.account_name.setText(list.get(position).getAccount_name());
        hold.account_number.setText(list.get(position).getAccount_number());

        dbHelper = new DataBaseHelper(this.context);



        //hold.deleteaccount.setImageResource(list.get(position).getImageView());
        return convertView;
    }

    class ViewHold {
        public ImageView imageView;
        //public TextView account_time;
        public TextView account_name;
        public TextView account_number;
        //View DeleteButton2;
        //public ImageButton deleteaccount;
    }


}
