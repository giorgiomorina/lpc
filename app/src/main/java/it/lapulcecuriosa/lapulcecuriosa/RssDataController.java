package it.lapulcecuriosa.lapulcecuriosa;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//import it.lapulcecuriosa.lapulcecuriosa.LaPulceApp.RSSXMLTag;

/**
 * Created by giorgio.morina on 21/04/2016.
 */
public class RssDataController extends AsyncTask<String, Integer, ArrayList<RssRow>> {
    private static final int NUM_ROWS_PER_PAGE = 10;

    private enum RSSXMLTag {
        TITLE, DATE, LINK, CONTENT, GUID, IGNORETAG;
    }


    private RSSXMLTag currentTag; //segna l'attuale tag che sto leggendo
    private RssActivity rssActivity;
    private String errMsg;
    private boolean isFeedEnded=false;
    private ArrayList<RssRow> rssRows;

    public RssDataController(RssActivity rssActivity) {
        this.rssActivity = rssActivity;
    }

    public boolean isFeedEnded(){
        return isFeedEnded;
    }

    @Override
    protected ArrayList<RssRow> doInBackground(String... params) {
        String urlStr=params[0];
        InputStream is=null;
        ArrayList<RssRow> rssRows=new ArrayList<RssRow>();
        int iNumLoadedRows=0;
        int eventType=-1;

        try {

            URL url;
            HttpURLConnection conn;

            //INIZIO codice generico per apertura connessione HTTP
            try {
                url = new URL(urlStr);
            } catch (MalformedURLException e) {
                throw new Exception(rssActivity.getString(R.string.usrmsg_gen_err));
            }

            int response;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10 * 1000);
                conn.setConnectTimeout(10 * 1000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                response = conn.getResponseCode();
            } catch (IOException e) {
                throw new Exception(rssActivity.getString(R.string.usrmsg_gen_err));
            }

            //Log.e(LaPulceApp.LOG_INFO,"response="+response);
            //response=-1;
            if (response != 200) {
                throw new Exception(rssActivity.getString(R.string.usrmsg_gen_err));
            }

            is = conn.getInputStream();
            //FINE

            //Parse XML
            XmlPullParserFactory factory;
            try {
                factory = XmlPullParserFactory.newInstance();
            } catch (XmlPullParserException e) {
                throw new Exception(rssActivity.getString(R.string.usrmsg_gen_err));
            }

            factory.setNamespaceAware(true);

            XmlPullParser xpp=factory.newPullParser();
            xpp.setInput(is,null);

            eventType=xpp.getEventType();

            RssRow rssRow = null;
            SimpleDateFormat dateFormat=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);

            while (eventType!=XmlPullParser.END_DOCUMENT
                    && (iNumLoadedRows%NUM_ROWS_PER_PAGE !=0 || iNumLoadedRows==0)
                    ) {
                String strNode = xpp.getName();

                switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;

                case XmlPullParser.START_TAG:

                    if (strNode.equals("item")) {
                        rssRow=new RssRow();
                        currentTag= RSSXMLTag.IGNORETAG;
                    } else if (strNode.equals("title") ) {
                        currentTag = RSSXMLTag.TITLE;
                    } else if (strNode.equals("link") ) {
                        currentTag= RSSXMLTag.LINK;
                    } else if (strNode.equals("pubDate")) {
                        currentTag= RSSXMLTag.DATE;
                    } else if (strNode.equals("encoded")) {
                        currentTag= RSSXMLTag.CONTENT;
                    }
                    break;

                case XmlPullParser.TEXT:
                    //qui c'è la ciccia!
                    String strContent = xpp.getText().trim();
                    if (rssRow!=null){

                        switch (currentTag) {
                        case TITLE :
                            if (strContent.length() != 0) {
                                if (rssRow.postTitle != null) {
                                    rssRow.postTitle += strContent;
                                } else {
                                    rssRow.postTitle = strContent;
                                }
                            }
                            break;
                        case LINK :
                            if (strContent.length() != 0) {
                                if (rssRow.postThumbUrl != null) {
                                    rssRow.postThumbUrl += strContent;
                                } else {
                                    rssRow.postThumbUrl = strContent;
                                }
                            }
                            break;
                        case DATE :
                            if (strContent.length() != 0) {
                                if (rssRow.postDate != null) {
                                    rssRow.postDate += strContent;
                                } else {
                                    rssRow.postDate = strContent;
                                }
                            }
                            break;
                        case CONTENT:
                            if (strContent.length() != 0) {
                                if (rssRow.postContent != null) {
                                    rssRow.postContent += strContent;
                                } else {
                                    rssRow.postContent = strContent;
                                }
                            }
                            break;
                    }
                }
                break;

                case XmlPullParser.END_TAG:

                    if (strNode.equals("item")) {
                        //se è finito il tag, posso aggiungere i dati alla lista di ritorno
                        //formattiamo subito la data
                        Date postDate = dateFormat.parse(rssRow.postDate);
                        rssRow.postDate = dateFormat.format(postDate);

                        //Tutto è basato sul presupposto che l'XML è esposto dall'articolo più recente al più vecchio.
                        //alla prima apertura, al primo loop, la rssActivityIfc.getdLastLoadedNews() è valorizzato a -1
                        if (rssActivity.getdLastLoadedNews() >  postDate.getTime() || rssActivity.getdLastLoadedNews()==-1) {

                            //lista di ritorno:
                            rssRows.add(rssRow);
                            rssActivity.setdLastLoadedNews(postDate.getTime());
                            iNumLoadedRows++;

                        }

                    } else if (strNode.equals("rss")) {
                        setFeedEnded();
                    } else {
                        //la chiusura dei tag al di fuori di <item> non mi interessano
                        currentTag=RSSXMLTag.IGNORETAG;
                    }
                    break;

                }

                eventType=xpp.next();
            } //while


        } catch (Exception e) {
            setErrMsg(e.getMessage());
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (eventType==XmlPullParser.END_DOCUMENT) {
            setFeedEnded();
        } else {
            setFeedRunning();
        }

        return rssRows;
    }

    private void setFeedRunning() {
        isFeedEnded=false;
    }
    private void setFeedEnded() {
        isFeedEnded=true;
    }

    @Override
    protected void onPostExecute(ArrayList<RssRow> rssRows) {
        if ( (errMsg!=null && !errMsg.isEmpty() ) || (rssRows==null && rssRows.size()==0) ) {
            rssActivity.setUiStatus(rssActivity.STATUS_ERROR);
        } else {
            rssActivity.setUiStatus(rssActivity.STATUS_LOADED);
            setRssRows(rssRows);
        }

        rssActivity.synchUI();
    }

    public ArrayList<RssRow> getFeed() {
        return rssRows;
    }

    private void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }

    private void setRssRows(ArrayList<RssRow> rssRows) {
        this.rssRows = rssRows;
    }
}
