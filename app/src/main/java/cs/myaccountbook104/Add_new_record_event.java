package cs.myaccountbook104;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Add_new_record_event extends Activity {


    private Intent intent;
    private Bundle bundle;
    Spinner account_type2;
    EditText account_name,number;
    Button confirm,cancelaccount;
    private LinearLayout layout1;
    String accountbookname;
    String record_name_event;
    int userid,accountbookid;

    public DataBaseHelper dbHelper;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.add_new_record_time,null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_record_event);
        //account_type = (Spinner) this.findViewById(R.id.spinner1);
        account_type2 = (Spinner) this.findViewById(R.id.spinner2);
        number = (EditText) this.findViewById(R.id.number);
        //date = (DatePicker) this.findViewById(R.id.datepicker);
        confirm = (Button) this.findViewById(R.id.confirm);
        cancelaccount = (Button) this.findViewById(R.id.cancelaccount);
        account_name =  (EditText) this.findViewById(R.id.account_name_event);

        layout1=(LinearLayout)findViewById(R.id.add_record_page);


        intent = this.getIntent();
        bundle = intent.getExtras();
        userid = bundle.getInt("userid");

        accountbookname = bundle.getString("accountbookname");
        //用userid and accountbookname 获取accountbookid
        dbHelper = new DataBaseHelper(this);
        dbHelper.openDatabase();
        SQLiteDatabase db = dbHelper.getDatabase();
        Cursor cursor = db.rawQuery("select * from main.tb_accountbook_info where User_id=? and Accountbook_name=?", new String[]{Integer.toString(userid),accountbookname});
        cursor.moveToPosition(-1);
        if(cursor.moveToNext())
        {
            accountbookid = cursor.getInt(cursor.getColumnIndex("Accountbook_id"));
            //db.execSQL("insert into main.tb_accountbook_info(User_id,Accountbook_type_id,Accountbook_name) values ("+user_id+",1,'default accountbook')");
        }
        dbHelper.closeDatabase();

        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });


        confirm.setOnClickListener(new ButtonListener());
        cancelaccount.setOnClickListener(new ButtonListener());

    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }



    private class ButtonListener implements View.OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.confirm:
                    dbHelper.openDatabase();
                    SQLiteDatabase db = dbHelper.getDatabase();
                    //String account_name = account_type.getSelectedItem().toString();
                    String account_type = account_type2.getSelectedItem().toString();
                    int account_type_to_int;
                    if(account_type.equals("expense"))
                    {
                        account_type_to_int = 1;
                    }
                    else
                    {
                        account_type_to_int = 2;
                    }
                    //String account_type = account_type2.getSe;
                    int numberofmoney = Integer.valueOf(number.getText().toString()).intValue();
                    record_name_event = account_name.getText().toString();
                    //String date = getdate();
                    db.execSQL("insert into main.tb_account_event(Accountbook_id,User_id,Account_name,Account_number,Account_type) values (" + accountbookid + "," + userid + ",'" + record_name_event + "'," + numberofmoney + "," + account_type_to_int + ")");

                    dbHelper.closeDatabase();
                    break;
                case R.id.cancelaccount:
                    //Intent intent2 = new Intent(add_new_record_time.this,mainpage.class);
                    //startActivity(intent2);
                    break;
                default:
                    break;
            }
            Intent intent = new Intent("android.intent.action.CART_BROADCAST");
            intent.putExtra("data","refresh");
            LocalBroadcastManager.getInstance(Add_new_record_event.this).sendBroadcast(intent);
            sendBroadcast(intent);
            finish();
        }
    }

}
