package com.yyp.richeditor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.yyp.editor.RichEditor;
import com.yyp.editor.bean.MaterialsMenuBean;
import com.yyp.editor.interfaces.OnEditorFocusListener;
import com.yyp.editor.interfaces.OnMaterialsItemClickListener;
import com.yyp.editor.interfaces.OnTextChangeListener;
import com.yyp.editor.widget.EditorOpMenuView;

public class MainActivity extends AppCompatActivity {

    private RichEditor mEditor;
    private EditorOpMenuView mEditorOpMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditor = findViewById(R.id.editor);
        mEditorOpMenuView = findViewById(R.id.editor_op_menu_view);


        mEditor.setPlaceholder("请填写文章正文内容（必填）"); //设置占位文字
        mEditor.setEditorFontSize(16); //设置文字大小
        mEditor.setPadding(10, 10, 10, 10); //设置编辑器内边距
        mEditor.setBackgroundColor(getResources().getColor(R.color._ffffff)); //设置编辑器背景色
        mEditor.hideWhenViewFocused((EditText) findViewById(R.id.et_title)); //设置焦点变化监听
        mEditor.setOnEditorFocusListener(new OnEditorFocusListener() {
            @Override
            public void onEditorFocus(boolean isFocus) {
                mEditorOpMenuView.displayMaterialsMenuView(false); //编辑器重获焦点，素材菜单要隐藏
                mEditorOpMenuView.setVisibility(isFocus ? View.VISIBLE : View.GONE);
            }
        });
        mEditor.setOnTextChangeListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) { //输入文本回调监听

            }
        });
        //绑定编辑器
        mEditorOpMenuView.setRichEditor(mEditor);
        //监听素材菜单点击事件
        mEditorOpMenuView.setOnMaterialsItemClickListener(new OnMaterialsItemClickListener() {

            @Override
            public void onMaterialsItemClick(MaterialsMenuBean bean) {
                switch (bean.getId()){
                    case MATERIALS_IMAGE: //从素材图片库选择 最大3个
                        mEditor.insertImage("", ""); //插入图片到编辑器
                        break;
                    case MATERIALS_VIDEO: //从素材视频库选择 最大3个
                        mEditor.insertVideoFrame("视频封面地址",
                                123, "视频名字", 32); //插入视频到编辑器
                        break;
                    case MATERIALS_TXT: //从素材文字库选择 最大1个
                        mEditor.insertHtml("新增文本内容"); //插入文本到编辑器
                        break;
                }
            }
        });

        mEditor.setHtml("拔份儿发热父加好友恢复日发货人官方规划区");
//        mEditor.disableEdit();
    }
}
