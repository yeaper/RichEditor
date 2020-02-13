package com.yyp.editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.yyp.editor.config.EditorOpType;
import com.yyp.editor.interfaces.AfterInitialLoadListener;
import com.yyp.editor.interfaces.OnDecorationStateListener;
import com.yyp.editor.interfaces.OnEditorFocusListener;
import com.yyp.editor.interfaces.OnTextChangeListener;
import com.yyp.editor.utils.ImageUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 富文本编辑器
 * <p>
 * Created by yyp on 2019/02/25.
 */
public class RichEditor extends WebView {

    private final static String TAG = "RichEditor";

    private static final String SETUP_HTML = "file:///android_asset/editor.html";
    private static final String CALLBACK_SCHEME = "re-callback://";
    private static final String STATE_SCHEME = "re-state://";

    private boolean isReady = false; //WebView是否初始化完成的标志
    private String mContents;
    private OnTextChangeListener mTextChangeListener;
    private OnDecorationStateListener mDecorationStateListener;
    private OnEditorFocusListener mOnEditorFocusListener;
    private AfterInitialLoadListener mLoadListener;

    public RichEditor(Context context) {
        this(context, null);
    }

    public RichEditor(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleLeaks();

        //不显示滚动条
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        //允许执行js
        getSettings().setJavaScriptEnabled(true);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(createWebViewClient());
        //打开cookie
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(this, true);
        }
        loadUrl(SETUP_HTML);

        applyAttributes(context, attrs);

        //设置输入框高度
        this.post(new Runnable() {
            public void run() {
                int height = RichEditor.this.getHeight();
                RichEditor.this.setEditorHeight(height);
            }
        });
    }

    /**
     * 请求加token
     *
     * @param url 接口ip
     */
    private void addCookies(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.setCookie(url, String.format("%s=%s", "cookie.name", "cookie.value")); //添加cookie
            cookieManager.flush();
        }
    }

    /**
     * 创建WebViewClient
     *
     * @return 自定义的WebViewClient
     */
    protected EditorWebViewClient createWebViewClient() {
        return new EditorWebViewClient();
    }

    /**
     * 设置内容监听器
     *
     * @param listener .
     */
    public void setOnTextChangeListener(OnTextChangeListener listener) {
        mTextChangeListener = listener;
    }

    /**
     * 设置编辑器状态改变监听
     *
     * @param listener .
     */
    public void setOnDecorationChangeListener(OnDecorationStateListener listener) {
        mDecorationStateListener = listener;
    }

    /**
     * 设置编辑器焦点监听
     *
     * @param onEditorFocusListener .
     */
    public void setOnEditorFocusListener(OnEditorFocusListener onEditorFocusListener) {
        this.mOnEditorFocusListener = onEditorFocusListener;
    }

    /**
     * 设置编辑器初始化完成的监听器
     *
     * @param listener .
     */
    public void setOnInitialLoadListener(AfterInitialLoadListener listener) {
        mLoadListener = listener;
    }

    /**
     * 禁止编辑
     */
    public void disableEdit(){
        disableLongClick(); //禁止长按
        setOnEditorFocusListener(null); //不监听焦点，防止弹出操作菜单
        setEdit(false); //编辑器不可编辑
    }

    /**
     * 禁止长按
     */
    public void disableLongClick() {
        setLongClickable(true);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    /**
     * 其他输入框拿到光标
     *
     * @param editTexts 其他输入框
     */
    public void hideWhenViewFocused(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (mOnEditorFocusListener != null && hasFocus) {
                        mOnEditorFocusListener.onEditorFocus(false);
                    }
                }
            });
        }
    }

    /**
     * 漏洞处理
     */
    private void handleLeaks() {
        // 4.4以下 清除接口引起的远程代码执行漏洞
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            removeJavascriptInterface("searchBoxJavaBridge_");
            removeJavascriptInterface("accessibility");
            removeJavascriptInterface("accessibilityTraversal");
        }

        WebSettings webSettings = getSettings();
        if (webSettings != null) {
            // 需要使用 file 协议
            webSettings.setAllowFileAccess(true);
            // 不允许通过 file url 加载的js代码读取其他的本地文件
            webSettings.setAllowFileAccessFromFileURLs(false);
            // 不允许通过 file url 加载的js访问其他的源(包括http、https等源)
            webSettings.setAllowUniversalAccessFromFileURLs(false);
        }
    }

    /**
     * 执行操作
     *
     * @param trigger
     */
    protected void exec(final String trigger) {
        if (isReady) {
            load(trigger);
        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    exec(trigger);
                }
            }, 100);
        }
    }

    /**
     * 加载js操作
     *
     * @param trigger
     */
    private void load(String trigger) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(trigger, null);
        } else {
            loadUrl(trigger);
        }
    }

    /**
     * 内容的回调处理
     *
     * @param text 编辑的内容
     */
    private void callback(String text) {
        mContents = text.replaceFirst(CALLBACK_SCHEME, "");
        if (mTextChangeListener != null) {
            mTextChangeListener.onTextChange(mContents);
        }
    }

    /**
     * 选中的所有操作的状态
     *
     * @param text
     */
    private void stateCheck(String text) {
        String state = text.replaceFirst(STATE_SCHEME, "").toUpperCase(Locale.ENGLISH);

        List<EditorOpType> types = new ArrayList<>();
        String[] stateParts = state.split(",");
        EditorOpType[] allTypes = EditorOpType.values();

        for (EditorOpType type : allTypes) {
            for (String part : stateParts) {
                if (TextUtils.indexOf(part, type.name()) != -1) {
                    types.add(type);
                    type.setValue(part);
                }
            }
        }

        if (mDecorationStateListener != null) {
            mDecorationStateListener.onStateChange(state, types);
        }
        if (mOnEditorFocusListener != null) {
            mOnEditorFocusListener.onEditorFocus(true);
        }
    }

    /**
     * 处理WebView自身的gravity属性
     *
     * @param context
     * @param attrs
     */
    private void applyAttributes(Context context, AttributeSet attrs) {
        final int[] attrsArray = new int[]{
                android.R.attr.gravity
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);

        int gravity = ta.getInt(0, NO_ID);
        switch (gravity) {
            case Gravity.LEFT:
                exec("javascript:RE.setTextAlign(\"left\")");
                break;
            case Gravity.RIGHT:
                exec("javascript:RE.setTextAlign(\"right\")");
                break;
            case Gravity.TOP:
                exec("javascript:RE.setVerticalAlign(\"top\")");
                break;
            case Gravity.BOTTOM:
                exec("javascript:RE.setVerticalAlign(\"bottom\")");
                break;
            case Gravity.CENTER_VERTICAL:
                exec("javascript:RE.setVerticalAlign(\"middle\")");
                break;
            case Gravity.CENTER_HORIZONTAL:
                exec("javascript:RE.setTextAlign(\"center\")");
                break;
            case Gravity.CENTER:
                exec("javascript:RE.setVerticalAlign(\"middle\")");
                exec("javascript:RE.setTextAlign(\"center\")");
                break;
        }

        ta.recycle();
    }

    /**
     * 设置是否可编辑
     *
     * @param isEdit 是否可编辑
     */
    public void setEdit(boolean isEdit) {
        exec("javascript:RE.setEdit('" + isEdit + "');");
    }

    /**
     * 编辑区获取焦点
     */
    public void focusEditor() {
        requestFocus();
        exec("javascript:RE.focus();");
    }

    /**
     * 编辑区清除焦点
     */
    public void clearFocusEditor() {
        if (mOnEditorFocusListener != null) {
            mOnEditorFocusListener.onEditorFocus(false);
        }
        exec("javascript:RE.blurFocus();");
    }

    /**
     * 插入html
     *
     * @param html html内容
     */
    public void insertHtml(String html) {
        if(!TextUtils.isEmpty(html)){
            html = html.replaceAll("\n", "<br>"); //编辑器不支持换行符\n
        }
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertHTML('" + html + "');");
    }

    /**
     * 插入图片
     *
     * @param url 图片url
     * @param alt 图片描述
     */
    public void insertImage(String url, String alt) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertImage('" + url + "', '" + alt + "');");
    }

    /**
     * 插入视频第一帧
     *
     * @param frameUrl  帧图片url
     * @param videoId   视频id
     * @param videoName 视频文件名
     * @param size      视频大小
     */
    public void insertVideoFrame(String frameUrl, long videoId, String videoName, long size) {
        exec("javascript:RE.prepareInsert();");
        @SuppressLint("DefaultLocale")
        String html = String.format("<img src=\"%s\" " +
                        "videoid=\"%d\" alt=\"%s\" " +
                        "controls=\"controls\" " +
                        "resourcetype=\"video/mp4\" " +
                        "filename=\"%s\" filesize=\"%d\" />",
                frameUrl, videoId, videoName, videoName, size);
        exec("javascript:RE.insertHTML('" + html + "');");
    }

    /**
     * 插入链接
     *
     * @param href  链接
     * @param title 链接说明
     */
    public void insertLink(String href, String title) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertLink('" + href + "', '" + title + "');");
    }

    /**
     * 直接插入html内容
     *
     * @param contents 内容
     */
    public void setHtml(String contents) {
        if (TextUtils.isEmpty(contents)) {
            contents = "";
        }
        contents = contents.replaceAll("\n", "<br>"); //编辑器不支持换行符\n

        try {
            exec("javascript:RE.setHtml('" + URLEncoder.encode(contents, "UTF-8") + "');");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mContents = contents;
    }

    /**
     * 获取网页代码内容
     *
     * @return 网页代码内容
     */
    public String getHtml() {
        return mContents;
    }

    /**
     * 设置整个编辑器的文字颜色
     *
     * @param color int色值
     */
    public void setEditorFontColor(int color) {
        String hex = ImageUtils.convertHexColorString(color);
        exec("javascript:RE.setBaseTextColor('" + hex + "');");
    }

    /**
     * 基础字体大小
     *
     * @param px 单位px
     */
    public void setEditorFontSize(int px) {
        exec("javascript:RE.setBaseFontSize('" + px + "px');");
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        exec("javascript:RE.setPadding('" + left + "px', '" + top + "px', '" + right + "px', '" + bottom
                + "px');");
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        // still not support RTL.
        setPadding(start, top, end, bottom);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(@IdRes int resid) {
        Bitmap bitmap = ImageUtils.decodeResource(getContext(), resid);
        String base64 = ImageUtils.toBase64(bitmap);
        bitmap.recycle();

        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    @Override
    public void setBackground(Drawable background) {
        Bitmap bitmap = ImageUtils.toBitmap(background);
        String base64 = ImageUtils.toBase64(bitmap);
        bitmap.recycle();

        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    /**
     * 设置编辑区背景图
     *
     * @param url 图片url
     */
    public void setBackground(String url) {
        exec("javascript:RE.setBackgroundImage('url(" + url + ")');");
    }

    /**
     * 设置编辑区宽度
     *
     * @param width 单位px
     */
    public void setEditorWidth(int width) {
        exec("javascript:RE.setWidth('" + width + "px');");
    }

    /**
     * 设置编辑区高度
     *
     * @param height 单位px
     */
    public void setEditorHeight(int height) {
        exec("javascript:RE.setHeight('" + height + "px');");
    }

    /**
     * 设置编辑区的 placeholder
     *
     * @param placeholder placeholder内容
     */
    public void setPlaceholder(String placeholder) {
        exec("javascript:RE.setPlaceholder('" + placeholder + "');");
    }

    /**
     * 设置是否允许输入
     *
     * @param inputEnabled 是否允许输入
     */
    public void setInputEnabled(Boolean inputEnabled) {
        exec("javascript:RE.setInputEnabled(" + inputEnabled + ")");
    }

    /**
     * 加载css样式文件
     *
     * @param cssFile css样式文件
     */
    public void loadCSS(String cssFile) {
        String jsCSSImport = "(function() {" +
                "    var head  = document.getElementsByTagName(\"head\")[0];" +
                "    var link  = document.createElement(\"link\");" +
                "    link.rel  = \"stylesheet\";" +
                "    link.type = \"text/css\";" +
                "    link.href = \"" + cssFile + "\";" +
                "    link.media = \"all\";" +
                "    head.appendChild(link);" +
                "}) ();";
        exec("javascript:" + jsCSSImport + "");
    }

    /**
     * 撤销
     */
    public void undo() {
        exec("javascript:RE.undo();");
    }

    /**
     * 反撤销
     */
    public void redo() {
        exec("javascript:RE.redo();");
    }

    /**
     * 粗体
     */
    public void setBold() {
        exec("javascript:RE.setBold();");
    }

    /**
     * 斜体
     */
    public void setItalic() {
        exec("javascript:RE.setItalic();");
    }

    /**
     * 下角标
     */
    public void setSubscript() {
        exec("javascript:RE.setSubscript();");
    }

    /**
     * 上角标
     */
    public void setSuperscript() {
        exec("javascript:RE.setSuperscript();");
    }

    /**
     * 删除线
     */
    public void setStrikeThrough() {
        exec("javascript:RE.setStrikeThrough();");
    }

    /**
     * 下划线
     */
    public void setUnderline() {
        exec("javascript:RE.setUnderline();");
    }

    /**
     * 文字颜色
     *
     * @param color int色值
     */
    public void setTextColor(int color) {
        exec("javascript:RE.prepareInsert();");
        String hex = ImageUtils.convertHexColorString(color);
        exec("javascript:RE.setTextColor('" + hex + "');");
    }

    /**
     * 文字背景色
     *
     * @param color int色值
     */
    public void setTextBackgroundColor(int color) {
        exec("javascript:RE.prepareInsert();");
        String hex = ImageUtils.convertHexColorString(color);
        exec("javascript:RE.setTextBackgroundColor('" + hex + "');");
    }

    /**
     * 文字大小
     *
     * @param fontSize 文字大小 1-7
     */
    public void setFontSize(@IntRange(from = 1, to = 7) int fontSize) {
        exec("javascript:RE.setFontSize('" + fontSize + "');");
    }

    /**
     * 清除所有格式
     */
    public void removeFormat() {
        exec("javascript:RE.removeFormat();");
    }

    /**
     * 设置标题等级
     *
     * @param heading 标题等级
     */
    public void setHeading(int heading) {
        exec("javascript:RE.setHeading('" + heading + "');");
    }

    /**
     * tab操作
     */
    public void setIndent() {
        exec("javascript:RE.setIndent();");
    }

    /**
     * 反tab操作
     */
    public void setOutdent() {
        exec("javascript:RE.setOutdent();");
    }

    /**
     * 左对齐
     */
    public void setAlignLeft() {
        exec("javascript:RE.setJustifyLeft();");
    }

    /**
     * 居中
     */
    public void setAlignCenter() {
        exec("javascript:RE.setJustifyCenter();");
    }

    /**
     * 右对齐
     */
    public void setAlignRight() {
        exec("javascript:RE.setJustifyRight();");
    }

    /**
     * 两端对齐
     */
    public void setAlignFull() {
        exec("javascript:RE.setJustifyFull();");
    }

    /**
     * 引用块
     */
    public void setBlockquote() {
        exec("javascript:RE.setBlockquote();");
    }

    /**
     * 无序列表
     */
    public void setBullets() {
        exec("javascript:RE.setBullets();");
    }

    /**
     * 有序列表
     */
    public void setNumbers() {
        exec("javascript:RE.setNumbers();");
    }

    /**
     * WebViewClient相关处理
     */
    protected class EditorWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            isReady = url.equalsIgnoreCase(SETUP_HTML); //页面加载完成
            if (mLoadListener != null) {
                mLoadListener.onAfterInitialLoad(isReady);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String decode;
            try {
                decode = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return false;
            }

            if (TextUtils.indexOf(url, CALLBACK_SCHEME) == 0) {
                callback(decode);
                exec("javascript:RE.refreshEditingItems();"); //输入改变，需要刷新返回各个配置的状态
                return true;
            } else if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
                stateCheck(decode);
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }


    }
}
