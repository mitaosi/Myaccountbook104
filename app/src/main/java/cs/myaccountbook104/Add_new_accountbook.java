package cs.myaccountbook104;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
public class Add_new_accountbook extends Fragment {

    Button submitNewAccountbook,cancelNewAccountbook;
    EditText accountbookName;
    Bundle userinfo;
    public DataBaseHelper dbHelper;
    int userid;
    String  username;
    android.support.v4.app.FragmentManager manager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_accountbook, null);
        submitNewAccountbook = view.findViewById(R.id.submitNewAccountbook);
        cancelNewAccountbook = view.findViewById(R.id.cancelNewAccountbook);
        accountbookName = view.findViewById(R.id.newAccountbookName);
        userinfo=getArguments();
        userid=userinfo.getInt("userid");
        username =  userinfo.getString("username");
        submitNewAccountbook.setOnClickListener(new ButtonListener());
        cancelNewAccountbook.setOnClickListener(new ButtonListener());

        dbHelper = new DataBaseHelper(this.getActivity());
        manager = getFragmentManager();

        return view;

    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.submitNewAccountbook:
                    dbHelper.openDatabase();
                    SQLiteDatabase db = dbHelper.getDatabase();
                    db.execSQL("insert into main.tb_accountbook_info(User_id,Accountbook_type_id,Accountbook_name) values (?,?,?)",new String[]{Integer.toString(userid),Integer.toString(2),accountbookName.getText().toString()});
                    dbHelper.closeDatabase();
                    AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                    builder.setTitle("SUCCESS!") ;
                    builder.setMessage("define accountbook success!");
                    builder.setPositiveButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(),mainpage.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("userid",userid);
                            bundle.putString("username",username);
                            //bundle.putString("accountbookname",accountbookname);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    });
                    builder.show();
                    //builder.dismiss();


                    break;
                case R.id.cancelNewAccountbook:
                    break;
                    default:
                        break;
            }
            /**
             * 这个需要再调试，还是有问题
             * android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
            mainpage_default_mainarea main =  new mainpage_default_mainarea();
            transaction.replace(R.id.content, main);
            transaction.addToBackStack(null);
            transaction.commit(); **/

            // startActivityForResult(new Intent(getContext(), mainpage.class),1);
            //getActivity().replaceFragment();
        }
    }



    }
