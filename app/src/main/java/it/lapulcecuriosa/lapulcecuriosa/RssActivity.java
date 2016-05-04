package it.lapulcecuriosa.lapulcecuriosa;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

public abstract class RssActivity extends AppCompatActivity {
    private String feedUrl;
    private ArrayList<RssRow> arrRssRows;
    private RssListAdapter lvAdapter;
    private ViewFlipper viewFlipper;
    private TextView tvMsg;

    private RssDataController rss;
    private ListView lvRss;

    private long dLastLoadedNews=-1;
    private Button btnLoadMore;
    private ImageView loadingAnim;
    private int uiStatus=0;

    public static final int STATUS_LOADING=0;
    public static final int STATUS_LOADED=1;
    public static final int STATUS_ERROR=2;
    public static final int STATUS_LOAD_MORE = 3;


    protected void setFeedUrl(String feedUrl) {
        this.feedUrl=feedUrl;
    }

    public long getdLastLoadedNews() {
        return dLastLoadedNews;
    }

    public void setdLastLoadedNews(long dLastLoadedNews) {
        this.dLastLoadedNews = dLastLoadedNews;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        //TODO: capire se mettere icona in action bar o meno
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        Log.e("INF", "onCreate");

        viewFlipper = (ViewFlipper)findViewById(R.id.rss_view_flipper);
        tvMsg = (TextView)findViewById(R.id.rss_tv_usrmsg);

        loadingAnim = (ImageView)findViewById(R.id.rss_anim_waiting_placeholder);

        lvRss = (ListView)findViewById(R.id.lv_news);

        //Come footer della ListView aggiungiamo il tasto per caricare altro
        btnLoadMore = new Button(this);
        lvRss.addFooterView(btnLoadMore);

        arrRssRows =new ArrayList<RssRow>();
        lvAdapter = new RssListAdapter(this, arrRssRows);
        lvRss.setAdapter(lvAdapter);

        lvRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //con la getItemAtPosition della ListView stessa ottengo l'oggetto sul quale l'adapter si basa (il record)
                RssRow nr = (RssRow) lvRss.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(), RssSingleItem.class);
                //passo il content all'altra activity
                i.putExtra("content", nr.postContent);
                i.putExtra("title", nr.postTitle);
                startActivity(i);
            }
        });

        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUiStatus(STATUS_LOAD_MORE);
                synchUI();
                rss = new RssDataController(RssActivity.this);
                rss.execute(feedUrl);
            }
        });

        setUiStatus(STATUS_LOADING);
        synchUI();

        rss = new RssDataController(this);
        rss.execute(feedUrl);
    }



    /**
     * In base a uiStatus, organizza la GUI
     */
    public void synchUI() {

        if (uiStatus==STATUS_LOADING) {
            Drawable drawable=getApp().getRandomLoadingAnimation();

            getApp().setLoadingAnim(loadingAnim, drawable);
            AnimationDrawable animation = (AnimationDrawable) loadingAnim.getBackground();
            animation.start();
            loadingAnim.setVisibility(View.VISIBLE);

            btnLoadMore.setVisibility(View.INVISIBLE);
            btnLoadMore.setEnabled(false);

            viewFlipper.setDisplayedChild(0);

        } else if (uiStatus==STATUS_LOADED) {
            loadingAnim.setVisibility(View.INVISIBLE);

            arrRssRows.addAll(arrRssRows.size(),rss.getFeed());

            btnLoadMore.setEnabled(true);
            btnLoadMore.setText(R.string.btn_load_more);

            if (rss.isFeedEnded()) {
                btnLoadMore.setVisibility(View.INVISIBLE);
            } else {
                btnLoadMore.setVisibility(View.VISIBLE);
            }

            lvAdapter.notifyDataSetChanged();

        } else if (uiStatus==STATUS_LOAD_MORE) {
            btnLoadMore.setEnabled(false);
            btnLoadMore.setText(R.string.btn_load_more_loading);
            loadingAnim.setVisibility(View.VISIBLE);
        }
        else if (uiStatus==STATUS_ERROR) {
            tvMsg.setText(rss.getErrMsg());
            viewFlipper.setDisplayedChild(1);
        }

    }

    /**
     * La overrido per gestire il ritorno all'activity parent (la freccetta back sull'action bar
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //link alla textview del messaggio di errore
    public void reloadActivity(View view) {
        recreate();
    }


    public void setUiStatus(int uiStatus) {
        this.uiStatus = uiStatus;
    }

    public LaPulceApp getApp() {
        return LaPulceApp.getContext();
    }
}
