package com.prize.webgame.bean;

import com.prize.webgame.utils.DateUtils;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by prize on 2018/11/21.
 */
@Table(name = "game_record")
public class GameBean implements Serializable {
    public static final String XINPIN = "TYPE_XINPIN";
    public static final String JINGDIAN = "TYPE_JINGDIAN";
    public static final String RENQI = "TYPE_RENQI";

    @Column(name = "id")
    private String id = "";

    @Column(name = "icon_url")
    private String icon_url = "";

    @Column(name = "name")
    private String name = "";

    @Column(name = "brief")
    private String brief = "";

    @Column(name = "catid")
    private String catid = "";

    @Column(name = "description")
    private String description = "";

    @Column(name = "source")
    private String source = "";

    @Column(name = "h5_url", isId = true)
    private String h5_url = "";

    @Column(name = "use_count")
    private String use_count = "";

    @Column(name = "developer")
    private String developer = "";

    @Column(name = "tag")
    private String tag = "";

    @Column(name = "lang")
    private String lang = "";

    @Column(name = "remark")
    private String remark = "";

    @Column(name = "catname")
    private String catname = "";

    @Column(name = "ctime")
    private String ctime = "";

    @Column(name = "utime")
    private String utime = "";

    @Column(name = "screen")
    private String screen = "";

    @Column(name = "state")
    private String state = "";

    @Column(name = "e_name")
    private String e_name = "";
    private String typeCategory = XINPIN;
    private int type = 0;
    private boolean isPlayed = false;

    @Column(name = "isHot")
    private boolean isHot = false;

    @Column(name = "last_modify_time")
    private long last_modify_time = 0;

    public GameBean() {

    }

    public GameBean(int type, String typeCategory) {
        this.typeCategory = typeCategory;
        this.type = type;
    }

    public long getLast_modify_time() {
        return last_modify_time;
    }

    public String getLast_modify_time_by_format() {
        if (last_modify_time <= 0)
            last_modify_time = System.currentTimeMillis();

        return DateUtils.getTimeDisplay(last_modify_time);
    }

    public void setLast_modify_time(long last_modify_time) {
        this.last_modify_time = last_modify_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getH5_url() {
        return h5_url;
    }

    public void setH5_url(String h5_url) {
        this.h5_url = h5_url;
    }

    public String getUse_count() {
        return use_count;
    }

    public void setUse_count(String use_count) {
        this.use_count = use_count;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getE_name() {
        return e_name;
    }

    public void setE_name(String e_name) {
        this.e_name = e_name;
    }

    public String getTypeCategory() {
        return typeCategory;
    }

    public void setTypeCategory(String typeCategory) {
        this.typeCategory = typeCategory;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }
}
