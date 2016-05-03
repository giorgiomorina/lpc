package it.lapulcecuriosa.lapulcecuriosa;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by giorgio.morina on 21/04/2016.
 */
public class NewsListAdapter extends ArrayAdapter<RssRow> {
    Activity context;
    ArrayList<RssRow> newsRssRows;

    public NewsListAdapter(Context context, ArrayList<RssRow> objects) {
        super(context, R.layout.news_row, objects);
        this.context = (Activity)context;
        this.newsRssRows=objects;
    }

    /**
     * Classe statica per mantenere i puntamenti alle view di ogni riga della list view
     * Usata in getView
     */
    static class ViewHolder {
        TextView tvTitolo;
        TextView tvData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        //gestione del riciclo:
        if (convertView == null) {
            LayoutInflater inflater=context.getLayoutInflater();
            convertView=inflater.inflate(R.layout.news_row,null);

            vh = new ViewHolder();
            vh.tvTitolo=(TextView)convertView.findViewById(R.id.news_row_titolo);
            vh.tvData=(TextView)convertView.findViewById(R.id.news_row_data);
            convertView.setTag(vh);

        } else {
            vh=(ViewHolder)convertView.getTag();
        }

        vh.tvTitolo.setText(newsRssRows.get(position).postTitle);

        vh.tvData.setText(newsRssRows.get(position).postDate);


        return convertView;
    }
    //TODO: tradurre stringa in italiano
    private String translateDate(String strDate) {
        return "";
    }
}
