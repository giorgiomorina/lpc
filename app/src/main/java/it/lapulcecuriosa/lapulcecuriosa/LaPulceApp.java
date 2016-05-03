package it.lapulcecuriosa.lapulcecuriosa;

import android.app.Application;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by giorgio.morina on 21/04/2016.
 */
public class LaPulceApp extends Application {
    public static final String LOG_INFO = "LPC_I";

    private static LaPulceApp instance;

    public enum RSSXMLTag {
        TITLE, DATE, LINK, CONTENT, GUID, IGNORETAG;
    }

    public LaPulceApp(){
        instance=this;
    }

    public static LaPulceApp getContext() {
        return instance;
    }

    public Drawable getRandomLoadingAnimation() {
        Class r = R.drawable.class;
        Field[] f=r.getFields();
        List<Drawable> randomResources=new ArrayList();

        for (Field resource :
                f) {
            String resName = resource.getName();

            if (resName.startsWith("rnd_")) {
                Object o = null;
                Drawable d = null;
                try {
                    d = ContextCompat.getDrawable(instance, resource.getInt(o));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                randomResources.add(d);
            }
        }

        int min=0, max=randomResources.size()-1;

        if (randomResources.size()>0) {
            return randomResources.get(new Random().nextInt(max - min +1 ) + min);
        } else {
            return null;
        }

    }
}
