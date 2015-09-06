package com.example.krystian892.truss;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.SeekBar;


import com.example.truss2.R;


public class NumPicker extends DialogPreference implements SeekBar.OnSeekBarChangeListener {

    SeekBar picker;
    Integer initialValue;
    int value;
    public NumPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
//    @Override
//    protected void onBindDialogView(View view) {
//        super.onBindDialogView(view);
//        this.picker = (NumberPicker)view.findViewById(R.id.pref_num_picker);
//// TODO this should be an XML parameter:
//        picker.setMaxValue(7);
//        picker.setMinValue(1);
//        if ( this.initialValue != null ) picker.setValue(3);
//        Log.wtf("BOO","BOO1");
//    }

    @Override
    protected View onCreateDialogView() {
       super.onCreateDialogView();
        View v = View.inflate(getContext(), R.layout.num_picker,null);
        this.picker = (SeekBar)v.findViewById(R.id.seekBar1);
// TODO this should be an XML parameter:
       // picker.setVerticalScrollbarPosition(10);

        if(this.initialValue!=null)
          picker.setProgress(initialValue);
      //  if ( this.initialValue != null ) picker.setValue(8);
       // Log.wtf("BOO","BOO1");
        return  v;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        if ( which == DialogInterface.BUTTON_POSITIVE ) {
            this.initialValue = picker.getProgress();
         //   Log.wtf("Storing", "value " +initialValue );
            persistInt( initialValue );
            callChangeListener( initialValue );
        }
    }
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
                                     Object defaultValue) {
        int def = ( defaultValue instanceof Number ) ? (Integer)defaultValue
                : ( defaultValue != null ) ? Integer.parseInt(defaultValue.toString()) : 1;
        if ( restorePersistedValue ) {
            this.initialValue = getPersistedInt(def);
        }
        else this.initialValue = (Integer)defaultValue;
    }
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 8);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        value = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
