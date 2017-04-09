package com.example.alex.yandextranslator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 08.04.17.
 */

public class PostModel {

    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("elementPureHtml")
    @Expose
    private String elementPureHtml;


}
