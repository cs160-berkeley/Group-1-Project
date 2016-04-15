package twerktitans.medagenda;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)//For backward-compability
public class CustomTimePicker extends NumberPicker {
  int textSize;
  String textColor;

  public CustomTimePicker(Context context) {
    super(context);
  }

  public CustomTimePicker(Context context, AttributeSet attrs) {
    super(context, attrs);
    processAttributeSet(attrs);
  }

  public CustomTimePicker(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    processAttributeSet(attrs);
  }
  private void processAttributeSet(AttributeSet attrs) {
    //This method reads the parameters given in the xml file and sets the properties according to it
    this.setMinValue(attrs.getAttributeIntValue(null, "min", 0));
    this.setMaxValue(attrs.getAttributeIntValue(null, "max", 0));
    textSize = attrs.getAttributeIntValue(null, "textSize", 14);
    textColor = attrs.getAttributeValue(null, "textColor");
  }

  @Override
  public void addView(View child) {
    super.addView(child);
    updateView(child);
  }

  @Override
  public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
    super.addView(child, index, params);
    updateView(child);
  }

  @Override
  public void addView(View child, android.view.ViewGroup.LayoutParams params) {
    super.addView(child, params);
    updateView(child);
  }

  private void updateView(View view) {
    if(view instanceof EditText){
      ((EditText) view).setTextSize(18);
      ((EditText) view).setTextColor(Color.parseColor("#000000"));
    }
  }
}