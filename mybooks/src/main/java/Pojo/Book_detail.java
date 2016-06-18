package Pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Richie on 14-05-2016.
 */
public class Book_detail {

    private String author_name;
    private String categories;
    private String title;
    private String publishers;
    private String lastCheckedOut;
    private String lastCheckedBy;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLastCheckedBy() {
        return lastCheckedBy;
    }

    public void setLastCheckedBy(String lastCheckedBy) {
        this.lastCheckedBy = lastCheckedBy;
    }

    public String getLastCheckedOut() {
        return lastCheckedOut;
    }

    public void setLastCheckedOut(String lastCheckedOut) {
        this.lastCheckedOut = lastCheckedOut;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishers() {
        return publishers;
    }

    public void setPublishers(String publishers) {
        this.publishers = publishers;
    }

}
