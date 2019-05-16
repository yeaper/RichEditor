package com.zjrb.richeditorproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.editor.RichEditor;
import com.zjrb.editor.bean.MaterialsMenuBean;
import com.zjrb.editor.interfaces.OnEditorFocusListener;
import com.zjrb.editor.interfaces.OnMaterialsItemClickListener;
import com.zjrb.editor.interfaces.OnTextChangeListener;
import com.zjrb.editor.widget.EditorOpMenuView;
import com.zjrb.resource.bean.MaterialsFile;
import com.zjrb.resource.selector.ResourceSelector;

import java.util.List;

import static com.zjrb.richeditorproject.config.Code.REQUEST_CODE_LOCAL_IMG;
import static com.zjrb.richeditorproject.config.Code.REQUEST_CODE_LOCAL_VIDEO;
import static com.zjrb.richeditorproject.config.Code.REQUEST_CODE_MATERIALS_IMG;
import static com.zjrb.richeditorproject.config.Code.REQUEST_CODE_MATERIALS_TXT;
import static com.zjrb.richeditorproject.config.Code.REQUEST_CODE_MATERIALS_VIDEO;

public class MainActivity extends AppCompatActivity {

    private RichEditor mEditor;
    private EditorOpMenuView mEditorOpMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditor = findViewById(R.id.editor);
        mEditorOpMenuView = findViewById(R.id.editor_op_menu_view);


        mEditor.setPlaceholder("请填写文章正文内容（必填）");
        mEditor.setEditorFontSize(16);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setBackgroundColor(UIUtils.getResources().getColor(R.color._ffffff));
        mEditor.hideWhenViewFocused((EditText) findViewById(R.id.et_title));
        mEditor.setOnEditorFocusListener(new OnEditorFocusListener() {
            @Override
            public void onEditorFocus(boolean isFocus) {
                mEditorOpMenuView.displayMaterialsMenuView(false); //编辑器重获焦点，素材菜单要隐藏
                if(isFocus){
                    mEditorOpMenuView.setVisibility(View.VISIBLE);
                }else{
                    mEditorOpMenuView.setVisibility(View.GONE);
                }
            }
        });
        mEditor.setOnTextChangeListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                text.length();
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
                        ResourceSelector.create(MainActivity.this)
                                .selectType(2)
                                .selectNum(3)
                                .forResult(REQUEST_CODE_MATERIALS_IMG);
                        break;
                    case MATERIALS_VIDEO: //从素材视频库选择 最大3个
                        ResourceSelector.create(MainActivity.this)
                                .selectType(3)
                                .selectNum(3)
                                .forResult(REQUEST_CODE_MATERIALS_VIDEO);
                        break;
                    case MATERIALS_TXT: //从素材文字库选择 最大1个
                        ResourceSelector.create(MainActivity.this)
                                .selectType(1)
                                .selectNum(1)
                                .forResult(REQUEST_CODE_MATERIALS_TXT);
                        break;
                    case LOCAL_IMAGE: //从本地图片库选择 最大3个
                        PictureSelector.create(MainActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .theme(R.style.picture_me_edit_style)
                                .maxSelectNum(3)
                                .imageSpanCount(3)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .previewImage(true)
                                .isZoomAnim(true)
                                .isGif(true)
                                .forResult(REQUEST_CODE_LOCAL_IMG);
                        break;
                    case LOCAL_VIDEO: //从本地视频库选择 最大1个
                        PictureSelector.create(MainActivity.this)
                                .openGallery(PictureMimeType.ofVideo())
                                .theme(R.style.picture_me_edit_style)
                                .maxSelectNum(1)
                                .imageSpanCount(3)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .previewImage(true)
                                .isZoomAnim(true)
                                .isGif(true)
                                .forResult(REQUEST_CODE_LOCAL_VIDEO);
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){ //处理素材结果
                case REQUEST_CODE_MATERIALS_IMG:
                    List<MaterialsFile> materialsImgs = ResourceSelector.obtainMultipleResult(data);
                    for(MaterialsFile file: materialsImgs){
                        mEditor.insertImage(file.getUrl(), ""); //插入图片到编辑器
                    }
                    break;
                case REQUEST_CODE_MATERIALS_VIDEO:
                    List<MaterialsFile> materialsVideos = ResourceSelector.obtainMultipleResult(data);
                    for(MaterialsFile file : materialsVideos){
                        String html = String.format("<img src=\"%s\" " +
                                        "videoid=\"%d\" alt=\"\" " +
                                        "style=\"width:100%%;height:auto;\" " +
                                        "controls=\"controls\" " +
                                        "resourcetype=\"video/mp4\" " +
                                        "filename=\"%s\" filesize=\"%d\" />",
                                file.getThumbUrl(), file.getId(), file.getName(), file.getSize());
                        mEditor.insertHtml(html); //插入视频到编辑器
                    }
                    break;
                case REQUEST_CODE_MATERIALS_TXT:
                    List<MaterialsFile> materialsTxts = ResourceSelector.obtainMultipleResult(data);
                    if(materialsTxts != null && !materialsTxts.isEmpty()){
                        MaterialsFile file = materialsTxts.get(0);
                        if(file != null){
                            mEditor.insertHtml(file.getContent()); //插入文本到编辑器
                        }
                    }
                    break;
                case REQUEST_CODE_LOCAL_IMG:
                    List<LocalMedia> localImgs = PictureSelector.obtainMultipleResult(data);
                    break;
                case REQUEST_CODE_LOCAL_VIDEO:
                    List<LocalMedia> localVideos = PictureSelector.obtainMultipleResult(data);
                    break;
            }
        }
    }
}
