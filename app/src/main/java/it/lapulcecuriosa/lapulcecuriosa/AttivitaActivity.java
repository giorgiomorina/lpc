package it.lapulcecuriosa.lapulcecuriosa;

import android.os.Bundle;

public class AttivitaActivity extends RssActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFeedUrl("http://www.lapulcecuriosa.it/feed/");
        super.onCreate(savedInstanceState);
    }

}
