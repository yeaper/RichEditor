package com.yyp.editor.config;

/**
 * 素材
 * Created by yyp on 2019.04.11
 */
public enum MaterialsMenuType {

    MATERIALS_IMAGE(0),
    MATERIALS_VIDEO(1),
    MATERIALS_TXT(2),
    LOCAL_IMAGE(3),
    LOCAL_VIDEO(4),

    //大的类型
    TYPE_IMAGE(5),
    TYPE_IMAGE_TXT(6),
    TYPE_VIDEO(7),
    TYPE_ALL(8);

    int value;

    MaterialsMenuType(int value){
        this.value = value;
    }
}
