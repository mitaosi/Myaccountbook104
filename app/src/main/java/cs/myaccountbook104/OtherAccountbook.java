package cs.myaccountbook104;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class OtherAccountbook extends Fragment {


    Button add_record_event_button;
    Bundle userinfo;
    String accountbookname;
    int accountbookid,userid;
    ListView accountList;
    account_list_adapter_event adapter;
    public DataBaseHelper dbHelper;
    private List<account_list_model_event> accountListValue = new ArrayList<account_list_model_event>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otheraccountbook,null);
        add_record_event_button = (Button) view.findViewById(R.id.add_record_event_button);

        userinfo = getArguments();
        userid = userinfo.getInt("userid");
        accountbookname = userinfo.getString("bookname");

        dbHelper = new DataBaseHelper(this.getActivity());
        setListValue(userid,accountbookname);
        accountList = (ListView) view.findViewById(R.id.account_list_order_by_event);
        adapter = new account_list_adapter_event(this.getActivity(),accountListValue);
        accountList.setAdapter(adapter);
        add_record_event_button.setOnClickListener(new ButtonListener());
        /**切换listview的item背景色，选中为红色；否则为黑色的方式二。当点击item的时候，就会触发子项item的焦点*/
        accountList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == true){
                    //获得焦点
                    accountList.setSelector(android.R.color.holo_red_light) ;
                }   else{
                    //失去焦点
                    accountList.setSelector(android.R.color.black) ;
                }
            }
        });


        //设置刷新函数
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh".equals(msg)){
                    refresh();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);

        accountList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                final Object item = accountList.getItemAtPosition(position);
                //long accountbook_id_for_delete = mListView.getItemIdAtPosition(position);
                final String account_name_for_delete_event = accountListValue.get(position).getAccount_name();
                final String account_number_for_delete_event = Integer.toString(Math.abs(Integer.parseInt(String.valueOf(accountListValue.get(position).getAccount_number()))));

                System.out.println(account_name_for_delete_event+"     "+account_number_for_delete_event);

                AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                builder.setTitle("Do you want to delete this record?" ) ;
                builder.setCancelable(true);
                builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHelper.openDatabase();
                        SQLiteDatabase db = dbHelper.getDatabase();
                        String user_id = Integer.toString(userid);
                        db.execSQL("delete from main.tb_account_event where User_id=? and Account_name=? and Account_number=?",new String[]{user_id,account_name_for_delete_event,account_number_for_delete_event});
                        dbHelper.closeDatabase();
                        accountListValue.remove(item);
                        // System.out.println(accountbook_id_for_delete);
                        adapter.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();

                return true;
            }

        });



        return view;
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.add_record_event_button:
                    Intent intent = new Intent(getActivity(),Add_new_record_event.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("userid",userid);
                    bundle.putString("accountbookname",accountbookname);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    //System.out.println(" add reacord success!");
                    break;
                default:
                    break;


            }
        }
    }

    private void setListValue(int userid,String accountbookname)
    {
        String user_id = Integer.toString(userid);
        dbHelper.openDatabase();
        SQLiteDatabase db = dbHelper.getDatabase();
        //get accountbook_id
        Cursor cursor = db.rawQuery("select * from main.tb_accountbook_info where User_id=? and Accountbook_name=?", new String[]{Integer.toString(userid),accountbookname});
        cursor.moveToPosition(-1);
        if(cursor.moveToNext())
        {
            accountbookid = cursor.getInt(cursor.getColumnIndex("Accountbook_id"));
            //db.execSQL("insert into main.tb_accountbook_info(User_id,Accountbook_type_id,Accountbook_name) values ("+user_id+",1,'default accountbook')");
        }

        Cursor cursor1 = db.rawQuery("select * from main.tb_account_event where User_id=? and Accountbook_id=?  ORDER BY Account_id2 DESC",new String[]{Integer.toString(userid),Integer.toString(accountbookid)});
        cursor1.moveToPosition(-1);
        int i=1;
        while(cursor1.moveToNext())
        {
            int number;
            if(cursor1.getInt(cursor1.getColumnIndex("Account_type"))==1)
                number = -cursor1.getInt(cursor1.getColumnIndex("Account_number"));
            else
                number = cursor1.getInt(cursor1.getColumnIndex("Account_number"));
            accountListValue.add(new account_list_model_event(R.drawable.account,cursor1.getString(cursor1.getColumnIndex("Account_name")),Integer.toString(number),i));

        }

        dbHelper.closeDatabase();



    }




    private void refresh() {
        refreshaccountList();
        //initData();
    }


    private void refreshaccountList()
    {
        accountListValue.clear();
        dbHelper = new DataBaseHelper(this.getActivity());
        setListValue(userid,accountbookname);
        //accountList.deferNotifyDataSetChanged();
        //accountList = (ListView) view.findViewById(R.id.accountList);
        adapter = new account_list_adapter_event(this.getActivity(),accountListValue);
        accountList.setAdapter(adapter);
    }



}
