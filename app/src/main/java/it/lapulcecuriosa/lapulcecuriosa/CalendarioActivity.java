package it.lapulcecuriosa.lapulcecuriosa;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CalendarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        Button btnCalPrenota=(Button)findViewById(R.id.btn_cal_prenota);

        btnCalPrenota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setClass(LaPulceApp.getContext(),PrenotaActivity.class);
                startActivity(i);
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


    /**
     * Richiamata quando si torna indietro da un'activity figlio. Dipende da
     * android:launchMode=singleTop del manifest
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.e(LaPulceApp.LOG_INFO, this.getClass().toString()+": onNewIntent");
    }


}
