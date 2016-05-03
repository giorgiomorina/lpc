package it.lapulcecuriosa.lapulcecuriosa;

import java.util.ArrayList;

/**
 * Created by giorgio.morina on 02/05/2016.
 */
public interface RssActivity {

    long getdLastLoadedNews();

    void setdLastLoadedNews(long time);

    void setErrMsg(String message);

    void incrLoads();

    void synchUI(ArrayList<RssRow> rssRows);

    String getString(int usrmsg_gen_err);
}
