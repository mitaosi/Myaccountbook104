package cs.myaccountbook104;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.*;


public class registerpage extends Activity {

    Button submit,cancel;
    TextView User_name,User_pwd,User_pwd2;
    public DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage);

        submit = findViewById(R.id.submit);
        cancel = findViewById(R.id.cancel);
        User_name = findViewById(R.id.User_name);
        User_pwd = findViewById(R.id.User_pwd);
        User_pwd2 = findViewById(R.id.User_pwd2);

        dbHelper = new DataBaseHelper(this);
        //dbHelper.openDatabase();
        //dbHelper.closeDatabase();

        submit.setOnClickListener(new ButtonListener());
        cancel.setOnClickListener(new ButtonListener());


    }

    private class ButtonListener implements View.OnClickListener
    {
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.submit:
                    String username = User_name.getText().toString();
                    String userpwd = User_pwd.getText().toString();
                    String userpwd2 = User_pwd2.getText().toString();

                    if(userpwd.equals(userpwd2))
                    {
                        dbHelper.openDatabase();
                        SQLiteDatabase db = dbHelper.getDatabase();
                        try{
                        db.execSQL("insert into main.tb_user_info(User_name,User_pwd) values (?,?)",new String[]{username,userpwd});
                        Cursor cursor = db.rawQuery("select User_id from main.tb_user_info where User_name=?", new String[]{username});
                        cursor.moveToPosition(-1);
                        if(cursor.moveToNext())
                        {
                            int user_id = cursor.getInt(cursor.getColumnIndex("User_id"));
                            db.execSQL("insert into main.tb_accountbook_info(User_id,Accountbook_type_id,Accountbook_name) values ("+user_id+",1,'default accountbook')");
                        }

                        dbHelper.closeDatabase();
                        AlertDialog.Builder builder  = new AlertDialog.Builder(registerpage.this);
                        builder.setTitle("SUCCESS!") ;
                        builder.setMessage("define user success!");
                        builder.setPositiveButton("close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(registerpage.this,MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        builder.show();
                        }catch (Exception e)
                        {
                            dbHelper.closeDatabase();
                            AlertDialog.Builder builder  = new AlertDialog.Builder(registerpage.this);
                            builder.setTitle("ERROR" ) ;
                            builder.setMessage("Database error!");
                            builder.setPositiveButton("retry" ,  null );
                            builder.show();
                        }

                    }
                    else
                    {
                        AlertDialog.Builder builder  = new AlertDialog.Builder(registerpage.this);
                        builder.setTitle("ERROR" ) ;
                        builder.setMessage("Enter the password differently!");
                        builder.setPositiveButton("retry" ,  null );
                        builder.show();
                    }

                    break;
                case R.id.cancel:
                    Intent intent2 = new Intent(registerpage.this,MainActivity.class);
                    startActivity(intent2);
                    break;
                default:
                    break;
            }
        }

    }
}
