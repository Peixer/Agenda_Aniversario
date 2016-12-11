package br.glaicon.agenda_aniversarios;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import br.glaicon.agenda_aniversarios.Volley.AppController;
import br.glaicon.agenda_aniversarios.Volley.BitmapCache;
import br.glaicon.agenda_aniversarios.Volley.ImageUtil;

public class ContatoAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Contato> contatos;
    private Resources resource;
    private TipoOrdenacao tipo;
    BitmapCache bitmapCache = AppController.getInstance().getBitmapCache();

    public ContatoAdapter(Activity activity, List<Contato> contatos, TipoOrdenacao tipo) {
        this.activity = activity;
        this.contatos = contatos;
        this.tipo = tipo;
    }

    @Override
    public int getCount() {
        return contatos.size();
    }

    @Override
    public Object getItem(int location) {
        return contatos.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateList() {
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (resource == null)
            resource = activity.getResources();

        ContatoViewHolder contatoVH;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_adapter, null);

            contatoVH = new ContatoViewHolder();
            contatoVH.imageView = (ImageView) convertView.findViewById(R.id.imgView);
            contatoVH.txtNome = (TextView) convertView.findViewById(R.id.txtNome);
            contatoVH.txtEmail = (TextView) convertView.findViewById(R.id.txtEmail);
            convertView.setTag(contatoVH);
        } else
            contatoVH = (ContatoViewHolder) convertView.getTag();

        Contato contato = (Contato) getItem(position);
        setContato(contato, contatoVH);

        return convertView;
    }

    private void setContato(Contato contato, ContatoViewHolder contatoVH) {
        contatoVH.txtNome.setText(contato.getNome());

        if (tipo == TipoOrdenacao.NOME)
            contatoVH.txtEmail.setText(contato.getEmail());
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(contato.getDate());
            contatoVH.txtEmail.setText(DateFormat.getDateInstance().format(calendar.getTime()));
        }

        contatoVH.imageView.setImageDrawable(getBitmap(contato));
    }

    private RoundImage getBitmap(Contato contato) {
        Bitmap bitmap = null;
        if (contato.getUriFoto() != "") {
            bitmap = bitmapCache.getBitmap(contato.getNome());

            if (bitmap == null) {
                bitmap = BitmapFactory.decodeFile(contato.getUriFoto());

                bitmap = ImageUtil.ResizedBitmap(bitmap);

                bitmapCache.putBitmap(contato.getNome(), bitmap);
            }
        } else
            bitmap = ((BitmapDrawable) resource.getDrawable(R.drawable.user)).getBitmap();

        return new RoundImage(bitmap);
    }

    static class ContatoViewHolder {
        TextView txtNome;
        TextView txtEmail;
        ImageView imageView;
    }
}
