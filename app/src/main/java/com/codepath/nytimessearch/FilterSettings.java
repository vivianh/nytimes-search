package com.codepath.nytimessearch;

import org.parceler.Parcel;

@Parcel
public class FilterSettings {
    String beginDate;
    String sortOrder;
    boolean newsDeskArt;
    boolean newsDeskFashionStyle;
    boolean newsDeskSports;

    public FilterSettings() {

    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isNewsDeskArt() {
        return newsDeskArt;
    }

    public void setNewsDeskArt(boolean newsDeskArt) {
        this.newsDeskArt = newsDeskArt;
    }

    public boolean isNewsDeskFashionStyle() {
        return newsDeskFashionStyle;
    }

    public void setNewsDeskFashionStyle(boolean newsDeskFashionStyle) {
        this.newsDeskFashionStyle = newsDeskFashionStyle;
    }

    public boolean isNewsDeskSports() {
        return newsDeskSports;
    }

    public void setNewsDeskSports(boolean newsDeskSports) {
        this.newsDeskSports = newsDeskSports;
    }
}
