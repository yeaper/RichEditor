package com.zjrb.richeditorproject;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zjrb.editor.RichEditor;
import com.zjrb.editor.interfaces.OnTextChangeListener;
import com.zjrb.editor.widget.EditorOpMenuView;

public class MainActivity extends AppCompatActivity {

    private RichEditor mEditor;
    private EditorOpMenuView mEditorOpMenuView;
    private TextView mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditor = findViewById(R.id.editor);
        mEditorOpMenuView = findViewById(R.id.editor_op_menu_view);

        //默认格式
        mEditor.setEditorFontSize(19); //19px
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setEditorHeight(300);
        mEditor.setBackgroundColor(Color.WHITE);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("请填写文章正文内容（必填）");

        mPreview = findViewById(R.id.preview);
        mEditor.setOnTextChangeListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) { //监听编辑内容的变化
                mPreview.setText(text);
            }
        });

        mEditorOpMenuView.setRichEditor(mEditor);
    }
}
