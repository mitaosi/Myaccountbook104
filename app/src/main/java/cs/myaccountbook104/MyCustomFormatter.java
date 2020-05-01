package cs.myaccountbook104;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

class MyCustomFormatter extends ValueFormatter implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        //System.out.println(value);
        String result = "";
        switch ((int) value)
        {
            case 1:
                result="meals";
                break;
            case 2:
                result = "shopping";
                break;
            case 3:
                result = "commodity";
                break;
            case 4:
                result = "transportation";
                break;
            case 5:
                result = "travelling";
                break;
            case 6:
                result = "study";
                break;
            case 7:
                result = "medical";
                break;
            case 8:
                result = "donate";
                break;
                default:
                    break;

        }
        return result;
    }
}
