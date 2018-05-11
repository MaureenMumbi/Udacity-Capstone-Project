package com.easyoffer.android.easyofferapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Mauryn on 5/9/2018.
 */

@SuppressLint("AppCompatCustomView")
public class ThreeTwoImageView extends ImageView {

    public ThreeTwoImageView(Context context) {
        super(context);
    }
    public ThreeTwoImageView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }
    public ThreeTwoImageView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context,attributeSet,defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int threeTwoHeight = MeasureSpec.getSize(widthMeasureSpec)*2/3;
        int threeTwoHeightSpec =MeasureSpec.makeMeasureSpec(threeTwoHeight,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, threeTwoHeightSpec);
    }
}
