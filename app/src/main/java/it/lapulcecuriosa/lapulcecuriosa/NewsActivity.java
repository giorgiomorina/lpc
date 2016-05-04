package it.lapulcecuriosa.lapulcecuriosa;

import android.os.Bundle;

public class NewsActivity extends RssActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFeedUrl("https://www.facebook.com/feeds/notifications.php?id=1348584122&viewer=1348584122&key=AWj1LM1GkNWlIaYU&format=rss20");
        super.onCreate(savedInstanceState);
    }



}
