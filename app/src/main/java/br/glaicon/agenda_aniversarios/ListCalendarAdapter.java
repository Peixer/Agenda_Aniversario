package br.glaicon.agenda_aniversarios;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
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

public class ListCalendarAdapter extends BaseAdapter {
    ArrayList<Contato> contatos;
    Context context;
    Resources resource;
    LayoutInflater inflater = null;

    public ListCalendarAdapter(Context context, ArrayList<Contato> contatos) {
        this.contatos = contatos;
        this.context = context;
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
        circularImagemDoAniversariante(calendarVH, resource.getDrawable(R.drawable.user));

        return convertView;
    }

    private void circularImagemDoAniversariante(CalendarViewHolder contato, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        Canvas c = new Canvas(circleBitmap);

        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setAntiAlias(true);
        paint.setShader(shader);

        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);

        contato.image.setImageBitmap(circleBitmap);
    }

    static class CalendarViewHolder {
        TextView txtNome;
        ImageView image;
    }
}
