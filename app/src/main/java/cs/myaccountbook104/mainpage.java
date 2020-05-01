package cs.myaccountbook104;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class mainpage extends AppCompatActivity{

    private Intent intent;
    private Bundle bundle;
    TextView title;
    Button bt1,bt2;
    //Button test;
    public DataBaseHelper dbHelper;
    int buttonNumber;

    private List<ContentModel> mList = new ArrayList<ContentModel>();
    private ListView mListView;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle ; //侧滑菜单状态监听器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        bt1 = findViewById(R.id.opensidebar);
        bt2 = findViewById(R.id.analyze);
        //addrecord = findViewById(R.id.addrecord);
        //test = findViewById(R.id.testbutton);
        intent = this.getIntent();
        bundle = intent.getExtras();
        final String username = bundle.getString("username");
        final int userid = bundle.getInt("userid");
        title = findViewById(R.id.mainpagetitle);
        dbHelper = new DataBaseHelper(this);


        //侧边栏数据初始化
        initData(username, userid);

        mListView = (ListView) findViewById(R.id.left_drawer);
        final ContentAdapter adapter = new ContentAdapter(this, mList);
        mListView.setAdapter(adapter);
        mDrawerLayout = findViewById(R.id.drawer_layout);


        //mainarea初始化
        mainpage_default_mainarea mainarea = new mainpage_default_mainarea();
        final Bundle userinfo = new Bundle();
        userinfo.putInt("userid", userid);
        userinfo.putString("username", username);
        String bookname = mList.get(1).getText();
        userinfo.putString("bookname", bookname);
        mainarea.setArguments(userinfo);
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.content, mainarea);
        transaction.commit();


        bt1.setOnClickListener(new ButtonListener());
        bt2.setOnClickListener(new ButtonListener());
        //addrecord.setOnClickListener(new ButtonListener());
        //test.setOnClickListener(new ButtonListener());

        /**切换listview的item背景色，选中为红色；否则为黑色的方式二。当点击item的时候，就会触发子项item的焦点*/
        mListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    //获得焦点
                    mListView.setSelector(R.color.silver);
                } else {
                    //失去焦点
                    mListView.setSelector(R.color.whitesmoke);
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    bt2.setVisibility(View.INVISIBLE);
                    User_management user_management = new User_management();
                    Bundle userinfo4 = new Bundle();
                    userinfo4.putInt("userid", userid);
                    userinfo4.putString("username",username);
                    user_management.setArguments(userinfo4);
                    replaceFragment(user_management);
                } else if (position == 1) {
                    mainpage_default_mainarea mainarea = new mainpage_default_mainarea();
                    Bundle userinfo = new Bundle();
                    userinfo.putInt("userid", userid);
                    userinfo.putString("username", username);
                    String bookname = mList.get(1).getText();
                    userinfo.putString("bookname", bookname);
                    mainarea.setArguments(userinfo);
                    replaceFragment(mainarea);
                    bt2.setVisibility(View.VISIBLE);
                } else if (position == (buttonNumber - 1)) {
                    Add_new_accountbook newaccount = new Add_new_accountbook();
                    Bundle userinfo3 = new Bundle();
                    userinfo3.putInt("userid", userid);
                    userinfo3.putString("username",username);
                    newaccount.setArguments(userinfo3);
                    replaceFragment(newaccount);
                } else {
                    bt2.setVisibility(View.INVISIBLE);
                    OtherAccountbook test = new OtherAccountbook();
                    Bundle userinfo2 = new Bundle();
                    userinfo2.putInt("userid", userid);
                    userinfo2.putString("username", username);
                    String bookname2 = mList.get(position).getText();
                    userinfo2.putString("bookname", bookname2);
                    test.setArguments(userinfo2);
                    replaceFragment(test);
                }
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });


        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                final Object item = mListView.getItemAtPosition(position);
                //long accountbook_id_for_delete = mListView.getItemIdAtPosition(position);
                final String accountbook_name_for_delete = mList.get(position).getText();
                System.out.println(accountbook_name_for_delete);

                AlertDialog.Builder builder  = new AlertDialog.Builder(mainpage.this);
                builder.setTitle("Do you want to delete this accountbook?" ) ;
                builder.setCancelable(true);
                builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHelper.openDatabase();
                        SQLiteDatabase db = dbHelper.getDatabase();
                        String user_id = Integer.toString(userid);
                        db.execSQL("delete from main.tb_accountbook_info where User_id=? and Accountbook_name=?",new String[]{user_id,accountbook_name_for_delete});
                        dbHelper.closeDatabase();
                        mList.remove(item);
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
    }


    private void initData(String username,int userid) {

        String user_id = Integer.toString(userid);
        mList.add(new ContentModel(R.drawable.userlogo,"manage user information", 1));
        dbHelper.openDatabase();
        SQLiteDatabase db = dbHelper.getDatabase();
        Cursor cur = db.rawQuery("select * from main.tb_accountbook_info where User_id=?",new String[]{user_id});
        cur.moveToPosition(-1);
        int i=2;
        while(cur.moveToNext())
        {
            mList.add(new ContentModel(R.drawable.book,cur.getString(cur.getColumnIndex("Accountbook_name")),i));
            i++;
        }

        //mList.add(new ContentModel(R.drawable.book, "default accountbook", 2));
        mList.add(new ContentModel(R.drawable.addaccountbook, "add a new accountbook",i));
        buttonNumber=i;
        dbHelper.closeDatabase();

    }


    private class ButtonListener implements View.OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.opensidebar:

                    mDrawerLayout.openDrawer(Gravity.LEFT);
                    break;
                case R.id.analyze:
                    Intent intent = new Intent(mainpage.this,analyzepage.class);
                    Bundle userinfo = new Bundle();
                    userinfo.putInt("userid",bundle.getInt("userid"));
                    userinfo.putString("accountbookname",mList.get(1).getText());
                    intent.putExtras(userinfo);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * a function about fragment change
     * @param fragment
     */
    public void replaceFragment(Fragment fragment){
        //1、拿到FragmentManager管理器
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        //2、获取事物
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();

        //3、fragment的替换
        transaction.replace(R.id.content,fragment);


        //4、提交事物
        transaction.commit();
    }



}
