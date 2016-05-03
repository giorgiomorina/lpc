package it.lapulcecuriosa.lapulcecuriosa;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by giorgio.morina on 02/05/2016.
 */
public class AttListAdapter extends ArrayAdapter<RssRow> {
    Activity context;
    ArrayList<RssRow> attRssRows;

    public AttListAdapter(Context context, ArrayList<RssRow> objects) {
        super(context, R.layout.att_row, objects);
        this.context = (Activity)context;
        this.attRssRows=objects;
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
            convertView=inflater.inflate(R.layout.att_row,null);

            vh = new ViewHolder();
            vh.tvTitolo=(TextView)convertView.findViewById(R.id.att_row_titolo);
            vh.tvData=(TextView)convertView.findViewById(R.id.att_row_data);
            convertView.setTag(vh);

        } else {
            vh=(ViewHolder)convertView.getTag();
        }

        vh.tvTitolo.setText(attRssRows.get(position).postTitle);

        vh.tvData.setText(attRssRows.get(position).postDate);


        return convertView;
    }
    //TODO: tradurre stringa in italiano
    private String translateDate(String strDate) {
        return "";
    }
}
