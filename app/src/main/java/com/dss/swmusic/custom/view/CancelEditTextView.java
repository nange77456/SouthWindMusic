package com.dss.swmusic.custom.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dss.swmusic.R;

public class CancelEditTextView extends FrameLayout {

    private EditText inputEditText;

    private ImageView cancelButton;

    private OnSearchListener onSearchListener;

    public CancelEditTextView(@NonNull Context context) {
        super(context);
    }

    public CancelEditTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.custom_view_cancel_edittext,this);
        inputEditText = view.findViewById(R.id.inputEditText);
        cancelButton = view.findViewById(R.id.ic_cancel);

        //监听用户输入，有输入则显示cancelButton
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    cancelButton.setVisibility(INVISIBLE);
                }else{
                    cancelButton.setVisibility(VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //点击cancelButton删除用户输入
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                inputEditText.setText("");
            }
        });

        //设置回车键搜索
        inputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH || (event!=null&&event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
                    //搜索回调
                    if(onSearchListener!=null){
                        onSearchListener.onSearch(inputEditText.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });

    }

    public String getText(){
        return inputEditText.getText().toString();
    }

    public void setOnSearchListener(OnSearchListener onSearchListener){
        this.onSearchListener = onSearchListener;
    }

    public interface OnSearchListener{
        void onSearch(String key);
    }

    public CancelEditTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String key){
        inputEditText.setText(key);
    }

    public void setHint(String text){
        inputEditText.setHint(text);
    }

}
