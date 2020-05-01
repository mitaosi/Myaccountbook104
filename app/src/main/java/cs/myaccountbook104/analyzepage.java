package cs.myaccountbook104;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class analyzepage extends Activity {

    BarChart chart1;
    LineChart chart2;
    BarData chart1data;
    LineData chart2Data;
    public DataBaseHelper dbHelper;
    private Random random;//用于产生随机数字
    private Intent intent;
    private Bundle bundle;
    int userid;
    String bookname;
    //这两个是字体替换
    //protected Typeface mTfRegular;
    //protected Typeface mTfLight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        //mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        setContentView(R.layout.activity_analyzepage);
        intent = this.getIntent();
        bundle = intent.getExtras();
        userid=bundle.getInt("userid");
        bookname=bundle.getString("accountbookname");

        dbHelper = new DataBaseHelper(this);

        chart1 = (BarChart) findViewById(R.id.chart1);
        chart2 = (LineChart) findViewById(R.id.chart2);

        //ButterKnife.bind(this);
        //random = new Random();
        initBarChart();

        initLineChart();



    }

    private void initLineChart() {


        //1.设置x轴和y轴的点
        List<Entry> entries1 = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();

        /**假数据
         * for (int i = 1; i <= 12; i++) {
         entries.add(new Entry(i, new Random().nextInt(300)));
         entries2.add(new Entry(i, 200));
         } **/

        //数据获取&处理

        Calendar calendar = Calendar.getInstance();
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        dbHelper.openDatabase();
        SQLiteDatabase db1 = dbHelper.getDatabase();
        int accountbookId = 0;
        Cursor cur = db1.rawQuery("select * from main.tb_accountbook_info where User_id=? and Accountbook_name=?", new String[]{Integer.toString(userid), bookname});
        cur.moveToPosition(-1);
        if (cur.moveToNext()) {
            accountbookId = cur.getInt(cur.getColumnIndex("Accountbook_id"));
            //db.execSQL("insert into main.tb_accountbook_info(User_id,Accountbook_type_id,Accountbook_name) values ("+user_id+",1,'default accountbook')");
        }
        //为每个月赋值（包括支出和收入）
        int account_number_mon_income = 0;
        int account_number_mon_outcome = 0;
        for (int i = 1; i <= 12; i++) {
            account_number_mon_income = 0;
            account_number_mon_outcome = 0;

            Cursor curx = db1.rawQuery("select * from main.tb_account_time where User_id=? and Accountbook_id=? and Account_time like '%/" + Integer.toString(i) + "/" + year + "'", new String[]{Integer.toString(userid), Integer.toString(accountbookId)});
            curx.moveToPosition(-1);

            while (curx.moveToNext()) {

                if (curx.getInt(curx.getColumnIndex("Account_type")) == 1) {
                    account_number_mon_outcome = account_number_mon_outcome + curx.getInt(curx.getColumnIndex("Account_number"));
                } else {
                    account_number_mon_income = account_number_mon_income + curx.getInt(curx.getColumnIndex("Account_number"));
                }
                entries1.add(new Entry(i, account_number_mon_outcome));
                entries2.add(new Entry(i, account_number_mon_income));
            }
        }

            dbHelper.closeDatabase();

            LineDataSet dataSet = new LineDataSet(entries1, "Monthly expenses"); // add entries to dataset
            LineDataSet dataset2 = new LineDataSet(entries2, "Monthly income");
            dataSet.setColor(R.color.slategray);
            dataset2.setColor(R.color.cadetblue);
            LineData lineData = new LineData(dataSet);
            lineData.addDataSet(dataset2);
            chart2.setData(lineData);


            XAxis xAxis = chart2.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(10f);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisMinimum(0f);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(12, false);
            // set a custom value formatter
            //xAxis.setXValueFormatter(new MyCustomFormatter());
            xAxis.setValueFormatter(new MylineChartFormatter());
            xAxis.setLabelRotationAngle(-60);


            chart2.getAxisRight().setEnabled(false);//右侧不显示Y轴
            chart2.getAxisLeft().setAxisMinValue(0.0f);//设置Y轴显示最小值，不然0下面会有空隙
            chart2.getAxisLeft().setDrawGridLines(false);//不设置Y轴网格
            chart2.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            chart2.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);;
            //chart2.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);//设置注解的位置在左上方
            chart2.getLegend().setForm(Legend.LegendForm.CIRCLE);//这是左边显示小图标的形状

            //chart1.setDescription("No Deal");//设置描述
            //chart1.setDescriptionTextSize(20.f);//设置描述字体
            chart2.animateXY(1500, 2500);//设置动画
            chart2.invalidate(); // refresh

            // chart2.setMode(LineDataSet.Mode.CUBIC_BEZIER);


    }


    private void initBarChart(){
        ArrayList<BarEntry> yVals = new ArrayList<>();//Y轴方向第一组数组
        ArrayList<BarEntry> yVals2 = new ArrayList<>();//Y轴方向第二组数组
        //ArrayList<BarEntry> yVals3 = new ArrayList<>();//Y轴方向第三组数组
       // ArrayList<String> xVals = new ArrayList<>();//X轴数据

        //先获取accountbook——id
        dbHelper.openDatabase();
        SQLiteDatabase db = dbHelper.getDatabase();
        int accountbookid = 0;
        Cursor cursor = db.rawQuery("select * from main.tb_accountbook_info where User_id=? and Accountbook_name=?", new String[]{Integer.toString(userid),bookname});
        cursor.moveToPosition(-1);
        if(cursor.moveToNext())
        {
            accountbookid = cursor.getInt(cursor.getColumnIndex("Accountbook_id"));
            //db.execSQL("insert into main.tb_accountbook_info(User_id,Accountbook_type_id,Accountbook_name) values ("+user_id+",1,'default accountbook')");
        }
        //开始为每一列赋值
        float account_number_sum;
        for (int i = 1; i <= 8; i++) {//添加数据源
            //xVals.add((i + 1) + "月");
            //yVals.add(new BarEntry(i,random.nextInt(10000)));
            account_number_sum=0;
            switch(i)
            {
                case 1:
                    Cursor cur1 = db.rawQuery("select * from main.tb_account_time where User_id=? and Accountbook_id=? and Account_name='meals'", new String[]{Integer.toString(userid),Integer.toString(accountbookid)});
                    cur1.moveToPosition(-1);

                    while(cur1.moveToNext()) {
                        //cur1.getString(cur1)
                        account_number_sum = account_number_sum + cur1.getInt(cur1.getColumnIndex("Account_number"));

                    }
                    break;
                case 2:
                    Cursor cur2 = db.rawQuery("select Account_number from main.tb_account_time where User_id=? and Accountbook_id=? and Account_name='shopping'", new String[]{Integer.toString(userid),Integer.toString(accountbookid)});
                    cur2.moveToPosition(-1);

                    while(cur2.moveToNext()) {
                        account_number_sum = account_number_sum + cur2.getInt(cur2.getColumnIndex("Account_number"));

                    }
                    break;
                case 3:
                    Cursor cur3 = db.rawQuery("select Account_number from main.tb_account_time where User_id=? and Accountbook_id=? and Account_name='commodity'", new String[]{Integer.toString(userid),Integer.toString(accountbookid)});
                    cur3.moveToPosition(-1);

                    while(cur3.moveToNext()) {
                        account_number_sum = account_number_sum + cur3.getInt(cur3.getColumnIndex("Account_number"));

                    }
                    break;
                case 4:
                    Cursor cur4 = db.rawQuery("select Account_number from main.tb_account_time where User_id=? and Accountbook_id=? and Account_name='transportation'", new String[]{Integer.toString(userid),Integer.toString(accountbookid)});
                    cur4.moveToPosition(-1);

                    while(cur4.moveToNext()) {
                        account_number_sum = account_number_sum + cur4.getInt(cur4.getColumnIndex("Account_number"));

                    }
                    break;
                case 5:
                    Cursor cur5 = db.rawQuery("select Account_number from main.tb_account_time where User_id=? and Accountbook_id=? and Account_name='travelling'", new String[]{Integer.toString(userid),Integer.toString(accountbookid)});
                    cur5.moveToPosition(-1);

                    while(cur5.moveToNext()) {
                        account_number_sum = account_number_sum + cur5.getInt(cur5.getColumnIndex("Account_number"));

                    }
                    break;
                case 6:
                    Cursor cur6 = db.rawQuery("select Account_number from main.tb_account_time where User_id=? and Accountbook_id=? and Account_name='study'", new String[]{Integer.toString(userid),Integer.toString(accountbookid)});
                    cur6.moveToPosition(-1);

                    while(cur6.moveToNext()) {
                        account_number_sum = account_number_sum + cur6.getInt(cur6.getColumnIndex("Account_number"));

                    }
                    break;
                case 7:
                    Cursor cur7 = db.rawQuery("select Account_number from main.tb_account_time where User_id=? and Accountbook_id=? and Account_name='medical'", new String[]{Integer.toString(userid),Integer.toString(accountbookid)});
                    cur7.moveToPosition(-1);

                    while(cur7.moveToNext()) {
                        account_number_sum = account_number_sum + cur7.getInt(cur7.getColumnIndex("Account_number"));

                    }
                    break;
                case 8:
                    Cursor cur8 = db.rawQuery("select Account_number from main.tb_account_time where User_id=? and Accountbook_id=? and Account_name='donate'", new String[]{Integer.toString(userid),Integer.toString(accountbookid)});
                    cur8.moveToPosition(-1);

                    while(cur8.moveToNext()) {
                        account_number_sum = account_number_sum + cur8.getInt(cur8.getColumnIndex("Account_number"));

                    }
                    break;
                    default:
                        break;

            }
            yVals.add(new BarEntry(i,account_number_sum));

        }

        dbHelper.closeDatabase();

        BarDataSet barDataSet = new BarDataSet(yVals, "Expenses this month");
        barDataSet.setColor(R.color.cadetblue);//设置第一组数据颜色

        // BarDataSet barDataSet2 = new BarDataSet(yVals2, "每月收入");
        // barDataSet2.setColor(Color.GREEN);//设置第二组数据颜色

        //BarDataSet barDataSet3 = new BarDataSet(yVals3, "小蔡每月支出");
        //barDataSet3.setColor(Color.YELLOW);//设置第三组数据颜色

        ArrayList<IBarDataSet> threebardata = new ArrayList<>();//IBarDataSet 接口很关键，是添加多组数据的关键结构，LineChart也是可以采用对应的接口类，也可以添加多组数据
        threebardata.add(barDataSet);
        //threebardata.add(barDataSet2);
        //threebardata.add(barDataSet3);

        BarData bardata = new BarData(threebardata);
        chart1.setData(bardata);
        chart1.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        chart1.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);;
        //chart1.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);//设置注解的位置在左上方
        chart1.getLegend().setForm(Legend.LegendForm.CIRCLE);//这是左边显示小图标的形状

        //chart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的位置
        //chart1.getXAxis().setDrawGridLines(false);//不显示网格
        XAxis xAxis = chart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        // set a custom value formatter
        //xAxis.setXValueFormatter(new MyCustomFormatter());
        xAxis.setValueFormatter(new MyCustomFormatter());
        xAxis.setLabelRotationAngle(-60);


        chart1.getAxisRight().setEnabled(false);//右侧不显示Y轴
        chart1.getAxisLeft().setAxisMinValue(0.0f);//设置Y轴显示最小值，不然0下面会有空隙
        chart1.getAxisLeft().setDrawGridLines(false);//不设置Y轴网格

        //chart1.setDescription("No Deal");//设置描述
        //chart1.setDescriptionTextSize(20.f);//设置描述字体
        chart1.animateXY(1000, 2000);//设置动画
    }


}
