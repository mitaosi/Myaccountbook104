package cs.myaccountbook104;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class User_management extends Fragment{


    EditText user_new_pwd,user_new_pwd2;
    Button submit_new_user_info,cancel_submit_new_user_info;
    String user_pwd_new,user_pwd_new2;
    Bundle userinfo;
    //Intent intent;
    int userid;
    String  username;
    public DataBaseHelper dbHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_management,null);
        user_new_pwd = view.findViewById(R.id.User_pwd_new);
        user_new_pwd2 = view.findViewById(R.id.User_pwd_new2);
        submit_new_user_info = view.findViewById(R.id.submit_new_user_info);
        cancel_submit_new_user_info = view.findViewById(R.id.cancel_submit_new_user_info);
        cancel_submit_new_user_info.setOnClickListener(new BUttonListener());
        submit_new_user_info.setOnClickListener(new BUttonListener());

        //intent = this.getIntent();
        userinfo = getArguments();
        userid = userinfo.getInt("userid");
        username =  userinfo.getString("username");
        dbHelper = new DataBaseHelper(this.getActivity());

        return view;
    }

    private class BUttonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch(view.getId())
            {
                case R.id.submit_new_user_info:
                    user_pwd_new = user_new_pwd.getText().toString();
                    user_pwd_new2 = user_new_pwd2.getText().toString();
                    if(user_pwd_new.equals(user_pwd_new2)) {
                        dbHelper.openDatabase();
                        SQLiteDatabase db = dbHelper.getDatabase();
                        db.execSQL("update main.tb_user_info set User_pwd='"+user_pwd_new+"' where User_id="+userid);
                        dbHelper.closeDatabase();
                        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setTitle("SUCCESS!") ;
                        builder.setMessage("change password success!");
                        builder.setPositiveButton("close" ,  new DialogInterface.OnClickListener() {
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
                    }

                    else
                    {
                        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setTitle("ERROR!") ;
                        builder.setMessage("Enter the password differently!");
                        builder.setPositiveButton("retry", null);
                        builder.show();
                    }
                    break;
                case R.id.cancel_submit_new_user_info:
                    break;
                    default:
                        break;
            }

        }
    }
}
