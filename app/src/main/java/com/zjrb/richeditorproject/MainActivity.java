package com.zjrb.richeditorproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zjrb.editor.RichEditor;
import com.zjrb.editor.bean.MaterialsMenuBean;
import com.zjrb.editor.config.MaterialsMenuType;
import com.zjrb.editor.interfaces.OnMaterialsItemClickListener;
import com.zjrb.editor.interfaces.OnTextChangeListener;
import com.zjrb.editor.widget.EditorOpMenuView;

public class MainActivity extends AppCompatActivity {

    private RichEditor mEditor;
    private EditorOpMenuView mEditorOpMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditor = findViewById(R.id.editor);
        mEditorOpMenuView = findViewById(R.id.editor_op_menu_view);

        //默认格式
        mEditor.setEditorFontSize(16); //16px
        mEditor.setEditorHeight(300);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("请填写文章正文内容（必填）");

        mEditor.setOnTextChangeListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) { //监听编辑内容的变化
            }
        });

        mEditorOpMenuView.initMaterialsMenuView(MaterialsMenuType.TYPE_IMAGE);
        mEditorOpMenuView.setRichEditor(mEditor);
        //监听素材菜单点击事件
        mEditorOpMenuView.setOnMaterialsItemClickListener(new OnMaterialsItemClickListener() {

            @Override
            public void onMaterialsItemClick(MaterialsMenuBean bean) {
                Toast.makeText(MainActivity.this, bean.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
