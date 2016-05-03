package it.lapulcecuriosa.lapulcecuriosa;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

public class AttivitaActivity extends AppCompatActivity implements RssActivity {

    private ViewFlipper viewFlipper;
    private TextView tvMsg;
    private ArrayList<RssRow> lvAttRssRows;
    private AttListAdapter lvAttAdapter;

    private long dLastLoadedNews=-1;
    private int nbrLoads=0;
    private String errMsg;
    private Button btnLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attivita);

        viewFlipper = (ViewFlipper)findViewById(R.id.att_view_flipper);
        tvMsg = (TextView)findViewById(R.id.att_tv_usrmsg);

        ImageView v = (ImageView)findViewById(R.id.att_anim_waiting_placeholder);
        AnimationDrawable animation = (AnimationDrawable)v.getBackground();
        animation.start();
        viewFlipper.setDisplayedChild(2);

        final ListView lvAttivita = (ListView)findViewById(R.id.lv_attivita);

        lvAttRssRows=new ArrayList<RssRow>();

        lvAttAdapter = new AttListAdapter(this,lvAttRssRows);

        lvAttivita.setAdapter(lvAttAdapter);

        lvAttivita.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //con la getItemAtPosition della ListView stessa ottengo l'oggetto sul quale l'adapter si basa (il record)
                RssRow nr = (RssRow) lvAttivita.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(), NewsSingleItem.class);
                //passo il content all'altra activity
                i.putExtra("content", nr.postContent);
                i.putExtra("title", nr.postTitle);
                startActivity(i);
            }
        });

        RssDataController rss=new RssDataController(this);
        rss.execute("http://www.lapulcecuriosa.it/feed/");
        //rss.execute("https://www.facebook.com/feeds/notifications.php?id=1348584122&viewer=1348584122&key=AWj1LM1GkNWlIaYU&format=rss20");

        //Come footer della ListView aggiungiamo il tasto per caricare altro
        btnLoadMore = new Button(this);
        btnLoadMore.setText(R.string.btn_load_more);
        lvAttivita.addFooterView(btnLoadMore);

        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoadMore.setEnabled(false);
                btnLoadMore.setText(R.string.btn_load_more_loading);

                new RssDataController(AttivitaActivity.this).execute("http://www.lapulcecuriosa.it/feed/");
                //new RssDataController(AttivitaActivity.this).execute("https://www.facebook.com/feeds/notifications.php?id=1348584122&viewer=1348584122&key=AWj1LM1GkNWlIaYU&format=rss20");
            }
        });

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

    @Override
    public long getdLastLoadedNews() {
        return dLastLoadedNews;
    }

    @Override
    public void setdLastLoadedNews(long time) {
        this.dLastLoadedNews=time;
    }

    @Override
    public void setErrMsg(String message) {
        this.errMsg=message;
    }

    @Override
    public void incrLoads() {
        nbrLoads++;
    }

    /**
     * Richiamata alla fine del thread di caricamento (onPostExecute)
     */

    @Override
    public void synchUI(ArrayList<RssRow> attRssRows) {
        if (errMsg==null || errMsg.isEmpty()) {
            for (int i = 0; i < attRssRows.size(); i++) {
                lvAttRssRows.add(attRssRows.get(i));
            }

            lvAttAdapter.notifyDataSetChanged();
            viewFlipper.setDisplayedChild(0);
            btnLoadMore.setEnabled(true);
            btnLoadMore.setText(R.string.btn_load_more);

        } else {
            //if (nbrLoads>1) {
            tvMsg.setText(errMsg);
            viewFlipper.setDisplayedChild(1);
            //}
        }

    }
}
