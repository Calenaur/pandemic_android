package com.calenaur.pandemic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calenaur.pandemic.R;

public class TitledTextView extends LinearLayout {

    private String title = "Title";
    private String text = "Text";
    private TextView titleView;
    private TextView textView;

    public TitledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TitledTextView, 0, 0);
        try {
            if (a.hasValue(R.styleable.TitledTextView_title)) {
                title = a.getString(R.styleable.TitledTextView_title);
            }

            if (a.hasValue(R.styleable.TitledTextView_text)) {
                text = a.getString(R.styleable.TitledTextView_text);
            }
        } finally {
            a.recycle();
        }

        initLayout();
    }

    private void initLayout() {
        inflate(getContext(), R.layout.view_titled_text_view, this);
        titleView = findViewById(R.id.name);
        textView = findViewById(R.id.text);
        titleView.setText(title);
        textView.setText(text);
    }

    public void setTitle(String text) {
        titleView.setText(text);
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
