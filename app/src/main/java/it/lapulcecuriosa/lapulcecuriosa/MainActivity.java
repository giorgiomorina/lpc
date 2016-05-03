package it.lapulcecuriosa.lapulcecuriosa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        Log.e(LaPulceApp.LOG_INFO, this.getClass().toString() + ":onCreate");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent();
        Class c;

        switch(item.getItemId()) {
            case R.id.menu_calendario:
                c=CalendarioActivity.class;
                break;
            case R.id.menu_news:
                c=NewsActivity.class;
                break;
            case R.id.menu_nostre_att:
                c=AttivitaActivity.class;
                break;
            case R.id.menu_contatti:
                c=ContattiActivity.class;
                break;
            default:
                c=null;
                break;
        }

        i.setClass(this, c);
        startActivity(i);

        return super.onOptionsItemSelected(item);
    }

    /**
     * Richiamata quando si torna indietro da un'activity figlio. Dipende da
     * android:launchMode=singleTop del manifest
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.e(LaPulceApp.LOG_INFO, this.getClass().toString() + ": onNewIntent");
    }


}
