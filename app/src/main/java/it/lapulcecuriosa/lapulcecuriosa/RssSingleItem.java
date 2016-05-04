package it.lapulcecuriosa.lapulcecuriosa;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

public class RssSingleItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_single_item);

        TextView tvSingleItem=(TextView)findViewById(R.id.rss_sngl_content);

        //Articolo
        CharSequence strContent=null;
        try {
            strContent=(CharSequence)Html.fromHtml(getIntent().getStringExtra("content"));
        } catch (NullPointerException e) {

        } finally {
            tvSingleItem.setText(strContent);
        }

        this.setTitle(getIntent().getStringExtra("title"));
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

}
