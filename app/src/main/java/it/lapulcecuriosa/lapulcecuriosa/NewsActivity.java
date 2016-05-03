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

public class NewsActivity extends AppCompatActivity implements RssActivity {
    private ArrayList<RssRow> lvNewsRssRows;
    private NewsListAdapter lvNewsAdapter;
    private ViewFlipper viewFlipper;
    private TextView tvMsg;

    private long dLastLoadedNews=-1;
    private int nbrLoads=0;
    private String errMsg;
    private Button btnLoadMore;
    private ImageView loadingAnim;

    @Override
    public long getdLastLoadedNews() {
        return dLastLoadedNews;
    }

    @Override
    public void setdLastLoadedNews(long dLastLoadedNews) {
        this.dLastLoadedNews = dLastLoadedNews;
    }

    @Override
    public void incrLoads() {
        this.nbrLoads++;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //TODO: capire se mettere icona in action bar o meno
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        Log.e("INF", "onCreate");

        viewFlipper = (ViewFlipper)findViewById(R.id.news_view_flipper);
        tvMsg = (TextView)findViewById(R.id.news_tv_usrmsg);

        loadingAnim = (ImageView)findViewById(R.id.news_anim_waiting_placeholder_2);

        Drawable drawable=((LaPulceApp) getApplication()).getRandomLoadingAnimation();

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            loadingAnim.setBackgroundDrawable(drawable);
        } else {
            loadingAnim.setBackground(drawable);
        }

        AnimationDrawable animation = (AnimationDrawable) loadingAnim.getBackground();
        animation.start();

        loadingAnim.setVisibility(View.VISIBLE);
        viewFlipper.setDisplayedChild(0);

        final ListView lvNews = (ListView)findViewById(R.id.lv_news);

        lvNewsRssRows=new ArrayList<RssRow>();

        lvNewsAdapter = new NewsListAdapter(this,lvNewsRssRows);

        lvNews.setAdapter(lvNewsAdapter);

        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //con la getItemAtPosition della ListView stessa ottengo l'oggetto sul quale l'adapter si basa (il record)
                RssRow nr = (RssRow) lvNews.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(), NewsSingleItem.class);
                //passo il content all'altra activity
                i.putExtra("content", nr.postContent);
                i.putExtra("title", nr.postTitle);
                i.putExtra("asd", nr.postTitle);
                startActivity(i);
            }
        });

        RssDataController rss=new RssDataController(this);
        rss.execute("http://www.lapulcecuriosa.it/blog-post/feed/");
        //rss.execute("https://www.facebook.com/feeds/notifications.php?id=1348584122&viewer=1348584122&key=AWj1LM1GkNWlIaYU&format=rss20");

        //Come footer della ListView aggiungiamo il tasto per caricare altro
        btnLoadMore = new Button(this);
        btnLoadMore.setText(R.string.btn_load_more);
        btnLoadMore.setVisibility(View.INVISIBLE);

        lvNews.addFooterView(btnLoadMore);

        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoadMore.setEnabled(false);
                btnLoadMore.setText("Caricando");
                loadingAnim.setVisibility(View.VISIBLE);
                new RssDataController(NewsActivity.this).execute("http://www.lapulcecuriosa.it/blog-post/feed/");
                //new RssDataController(NewsActivity.this).execute("https://www.facebook.com/feeds/notifications.php?id=1348584122&viewer=1348584122&key=AWj1LM1GkNWlIaYU&format=rss20");
            }
        });
    }

    /**
     * Richiamata alla fine del thread di caricamento (onPostExecute)
     */
    @Override
    public void synchUI(ArrayList<RssRow> newsRssRows) {
        if (errMsg==null || errMsg.isEmpty()) {
            for (int i = 0; i < newsRssRows.size(); i++) {
                lvNewsRssRows.add(newsRssRows.get(i));
            }
            btnLoadMore.setEnabled(true);
            btnLoadMore.setText(R.string.btn_load_more);

            lvNewsAdapter.notifyDataSetChanged();
            btnLoadMore.setVisibility(View.VISIBLE);
            loadingAnim.setVisibility(View.INVISIBLE);
            //viewFlipper.setDisplayedChild(0);
        } else {
            //if (nbrLoads>1) {
                tvMsg.setText(errMsg);
                viewFlipper.setDisplayedChild(1);
            //}
        }
    }

    @Override
    public void setErrMsg(String msg) {
        errMsg=msg;
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

    private void resetUI (){
        setdLastLoadedNews(-1);
        setErrMsg(null);
        nbrLoads=0;
    }

    public void reloadActivity(View view) {
        resetUI();
        recreate();
    }
}
