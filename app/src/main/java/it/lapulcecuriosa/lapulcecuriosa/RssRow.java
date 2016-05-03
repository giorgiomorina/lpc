package it.lapulcecuriosa.lapulcecuriosa;

/**
 * Created by giorgio.morina on 21/04/2016.
 */
public class RssRow {
    public String postThumbUrl;
    public String postTitle;
    public String postDate;
    public String postContent;
/*
    public RssRow() {
        this(null, null, null, null);
    }

    public RssRow(String postThumbUrl, String postTitle, String postDate, String postContent) {
        this.postDate=postDate;
        this.postThumbUrl=postThumbUrl;
        this.postTitle=postTitle;
        this.postContent=postContent;
    }
*/
    @Override
    public String toString() {
        return "postThumbUrl="+postThumbUrl+" postTitle="+postTitle+" postDate="+postDate+" postContent="+postContent;
    }
}
