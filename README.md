# 富文本编辑器模块

## 1.控件使用

RichEditor是富文本编辑器，EditorOpMenuView是操作栏控件，两个需要配合使用，xml引用方式如下：

```xml
<com.yyp.editor.RichEditor
    android:id="@+id/editor"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>

<com.yyp.editor.widget.EditorOpMenuView
    android:id="@+id/editor_op_menu_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

两个控件最后需要绑定

```java
mEditorOpMenuView.setRichEditor(mEditor);
```

## 2.编辑器相关设置

```java
//设置占位文字
mEditor.setPlaceholder("请填写文章正文内容（必填）");
//设置编辑器文字大小
mEditor.setEditorFontSize(16);
//设置编辑器内边距
mEditor.setPadding(10, 10, 10, 10);
//设置编辑器背景色
mEditor.setBackgroundColor(UIUtils.getResources().getColor(R.color._ffffff));
//禁止编辑 包括长按复制、双击选中、点击
mEditor.disableEdit();
//配置同一界面的焦点切换，可传多个输入框控件
mEditor.hideWhenViewFocused((editText1, editText2, editText3, ...);
```

## 3.编辑器操作

```java
//撤销
mEditor.undo();
//反撤销
mEditor.redo();
//加粗
mEditor.setBold();
//斜体
mEditor.setItalic();
//删除线
mEditor.setStrikeThrough();
//下划线
mEditor.setUnderline();
//设置文字颜色 传int色值
mEditor.setTextColor(color);
//设置文字大小 支持1-7字号
mEditor.setFontSize(size);
//左对齐
mEditor.setAlignLeft();
//居中对齐
mEditor.setAlignCenter();
//右对齐
mEditor.setAlignRight();
//两端对齐
mEditor.setAlignFull();
//有序列表
mEditor.setNumbers();
//无序列表
mEditor.setBullets();
//清除所有格式
mEditor.removeFormat();

//插入图片
mEditor.insertImage("图片地址", "提示文字");
//插入视频
mEditor.insertVideoFrame("视频封面地址", videoId, "视频名字", size);
//插入文本
mEditor.insertHtml("文本内容");
```

## 4.监听接口使用

```java
//编辑器焦点监听
mEditor.setOnEditorFocusListener(new OnEditorFocusListener() {
    @Override
    public void onEditorFocus(boolean isFocus) {
        mEditorOpMenuView.displayMaterialsMenuView(false); //编辑器重获焦点，素材菜单要隐藏
        mEditorOpMenuView.setVisibility(isFocus ? View.VISIBLE : View.GONE);
    }
});
//编辑器文本输入回调
mEditor.setOnTextChangeListener(new OnTextChangeListener() {
    @Override
    public void onTextChange(String text) {
        text.length();
    }
});
//监听素材菜单点击事件
mEditorOpMenuView.setOnMaterialsItemClickListener(new OnMaterialsItemClickListener() {

    @Override
    public void onMaterialsItemClick(MaterialsMenuBean bean) {
        switch (bean.getId()){
            case MATERIALS_IMAGE: //从素材图片库选择
                break;
            case MATERIALS_VIDEO: //从素材视频库选择
                break;
            case MATERIALS_TXT: //从素材文字库选择
                break;
            case LOCAL_IMAGE: //从本地图片库选择
                break;
            case LOCAL_VIDEO: //从本地视频库选择
                break;
        }
    }
});
```

## 5.遇到的问题与解决方案

**问题：**编辑器展示的图片访问不了，需要传cookie验证

**解决方案：**先前试过在WebViewClient的shouldInterceptRequest方法中，针对url设置cookie，但是cookie同步不及时，导致部分图片无法加载，后来考虑在加载图片前，先设置cookie，那么得出解决方案，先清除原来的cookie，然后为图片的ip地址设置新cookie，就可以访问了，这个方法在Webview.loadUrl()方法前调用即可。（注：android5.0以上记得打开cookie开关）

```java
 //打开cookie
android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
cookieManager.setAcceptCookie(true);
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    cookieManager.setAcceptThirdPartyCookies(this, true);
}

private void addCookies(String url) {
    CookieManager cookieManager = CookieManager.getInstance();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        cookieManager.removeSessionCookies(null);
        PersistentCookieStore cookieStore = new PersistentCookieStore(UIUtils.getContext());
        for (Cookie cookie : cookieStore.getCookies()) { //添加cookie
            cookieManager.setCookie(url, String.format("%s=%s", cookie.name(), cookie.value()));
        }
        cookieManager.flush();
    }
}
```

**问题：**为了方便编辑，图片、视频之间需要保持间距

**解决方案：**css样式文件中，设置margin边距即可

```css
/* 图片和视频：宽度占满，高度自适应，下边距3px */
IMAGE,img,video {
    width: 100% !important;
    height: auto;
    margin: 0px 0px 3px 0px !important;
}
```



