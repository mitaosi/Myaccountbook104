package cs.myaccountbook104;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.*;

public class MainActivity extends Activity{


    Button login, register;
    TextView username, userpwd;
    public DataBaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        username = findViewById(R.id.username);
        userpwd = findViewById(R.id.userpwd);

        dbHelper = new DataBaseHelper(this);
        dbHelper.openDatabase();
        dbHelper.closeDatabase();

        login.setOnClickListener(new ButtonListener());
        register.setOnClickListener(new ButtonListener());

    }

    private class ButtonListener implements View.OnClickListener
    {
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.login:
                    //verify the username and password
                    dbHelper.openDatabase();
                    SQLiteDatabase db = dbHelper.getDatabase();
                    //ContentValues values = new ContentValues();
                    String Username = username.getText().toString();

                    Cursor cursor = db.rawQuery("select User_pwd,User_id from main.tb_user_info where User_name=?", new String[]{Username});
                    cursor.moveToPosition(-1);
                    dbHelper.closeDatabase();
                    String user_pwd_db=null;
                    int user_id = 0;
                    if(cursor.moveToNext()) {
                        user_pwd_db = cursor.getString(cursor.getColumnIndex("User_pwd"));
                        user_id = cursor.getInt(cursor.getColumnIndex("User_id"));

                    }
                    else {
                        AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("ERROR" ) ;
                        builder.setMessage("username or password error!");
                        builder.setPositiveButton("retry" ,  null );
                        builder.show();
                    }
                    cursor.close();

                    String Userpwd = userpwd.getText().toString();
                    if (Userpwd.equals(user_pwd_db)) {
                            Intent intent1 = new Intent(MainActivity.this, mainpage.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("username",username.getText().toString());
                            bundle.putInt("userid",user_id);
                            intent1.putExtras(bundle);
                            startActivity(intent1);
                    }

                    else {
                        AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("ERROR" ) ;
                        builder.setMessage("username or password error!");
                        builder.setPositiveButton("retry" ,  null );
                        builder.show();
                    }
                    break;
                case R.id.register:
                    Intent intent2 = new Intent(MainActivity.this,registerpage.class);
                    startActivity(intent2);
                    break;
                    default:
                        break;
            }
        }

    }


}
