package br.glaicon.agenda_aniversarios.Calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.glaicon.agenda_aniversarios.Contato.Contato;
import br.glaicon.agenda_aniversarios.ImagemHelper;
import br.glaicon.agenda_aniversarios.R;
import br.glaicon.agenda_aniversarios.RoundImage;
import br.glaicon.agenda_aniversarios.Volley.AppController;
import br.glaicon.agenda_aniversarios.Volley.BitmapCache;
import br.glaicon.agenda_aniversarios.Volley.ImageUtil;

public class ListCalendarAdapter extends BaseAdapter {
    ArrayList<Contato> contatos;
    Context context;
    Resources resource;
    LayoutInflater inflater = null;
    ImagemHelper imagemHelper;
    BitmapCache bitmapCache = AppController.getInstance().getBitmapCache();

    public ListCalendarAdapter(Context context, ArrayList<Contato> contatos) {
        this.contatos = contatos;
        this.context = context;

        imagemHelper = new ImagemHelper(context);
    }

    @Override
    public int getCount() {
        return contatos.size();
    }

    @Override
    public Object getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CalendarViewHolder calendarVH;

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_calendaritem, null);

            calendarVH = new CalendarViewHolder();
            calendarVH.txtNome = (TextView) convertView.findViewById(R.id.textView_calendar);
            calendarVH.image = (ImageView) convertView.findViewById(R.id.imageView_calendar);
            convertView.setTag(calendarVH);
        } else
            calendarVH = (CalendarViewHolder) convertView.getTag();


        if (resource == null)
            resource = context.getResources();

        Contato contato = (Contato) getItem(position);
        calendarVH.txtNome.setText(contato.getNome());
        calendarVH.image.setImageDrawable(imagemHelper.obterRoundImagemDoContato(contato, bitmapCache, resource));

        return convertView;
    }

    static class CalendarViewHolder {
        TextView txtNome;
        ImageView image;
    }
}
