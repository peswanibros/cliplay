package com.hoanganhtuan95ptit.editphoto.rotate;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by Hoang Anh Tuan on 12/6/2017.
 */

public class RotateView extends AppCompatImageView {

    private int rotate;

    public RotateView(Context context) {
        super(context);
    }

    public RotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
        setRotation(rotate);
    }

}
