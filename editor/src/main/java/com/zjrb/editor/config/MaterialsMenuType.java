package com.zjrb.editor.config;

/**
 * 素材
 * Created by yyp on 2019.04.11
 */
public enum MaterialsMenuType {

    MATERIALS_IMAGE(0),
    MATERIALS_VIDEO(1),
    MATERIALS_TXT(2),
    LOCAL_IMAGE(3),
    LOCAL_VIDEO(4);

    int value;

    MaterialsMenuType(int value){
        this.value = value;
    }
}
