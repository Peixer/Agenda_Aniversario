
package br.glaicon.agenda_aniversarios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarAdapter extends BaseAdapter {
    static final int FIRST_DAY_OF_WEEK = 0; // Sunday = 0, Monday = 1

    private Context mContext;
    private java.util.Calendar calendar;
    private Calendar dataSelecionada;
    private ArrayList<Contato> contatos;
    private LayoutInflater inflater;
    public String[] dias;

    public CalendarAdapter(Context context, Calendar monthCalendar) {
        calendar = monthCalendar;
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dataSelecionada = (Calendar) monthCalendar.clone();
        mContext = context;
        this.contatos = new ArrayList<Contato>();

        calcularDias();
    }

    public void setItems(ArrayList<Contato> items) {
        this.contatos = items;
    }

    public int getCount() {
        return dias.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int index, View convertView, ViewGroup parent) {
        View view = convertView;

        if (inflater == null)
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        CalendarViewHolder calendarVH;

        if (view == null) {
            view = inflater.inflate(R.layout.calendar_item, null);

            calendarVH = new CalendarViewHolder();
            calendarVH.imageView = (ImageView) view.findViewById(R.id.date_icon);
            calendarVH.diaView = (TextView) view.findViewById(R.id.date);

            view.setTag(calendarVH);
        } else
            calendarVH = (CalendarViewHolder) view.getTag();

        if (dias[index].equals("")) {
            desabilitarDia(calendarVH);
        } else {
            alterarCorDeFundoDoDia(dias[index], view);
        }

        calendarVH.diaView.setText(dias[index]);

        if (dias[index].length() > 0 && existeContatoDeAniversario(dias[index], calendar.get(Calendar.MONTH)))
            calendarVH.imageView.setVisibility(View.VISIBLE);
        else
            calendarVH.imageView.setVisibility(View.INVISIBLE);

        return view;
    }

    private void desabilitarDia(CalendarViewHolder calendarVH) {
        calendarVH.diaView.setClickable(false);
        calendarVH.diaView.setFocusable(false);
    }

    private void alterarCorDeFundoDoDia(String dia, View view) {
        if (ehMesmaData(dia, dataSelecionada)) {
            view.setBackgroundResource(R.color.accent_material_dark);
        } else {
            view.setBackgroundResource(R.drawable.list_item_background);
        }
    }

    private boolean ehMesmaData(String dia, Calendar data) {
        return calendar.get(Calendar.YEAR) == data.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == data.get(Calendar.MONTH) &&
                dia.equals("" + data.get(Calendar.DAY_OF_MONTH));
    }

    private boolean existeContatoDeAniversario(String data, int mes) {
        if (contatos != null) {
            for (Contato contato : contatos) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(contato.getDate());
                if (contato.getDate().getMonth() == mes && String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).equals(data))
                    return true;
            }
        }
        return false;
    }

    public void calcularDias() {
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = (int) calendar.get(Calendar.DAY_OF_WEEK);

        if (firstDay == 1)
            dias = new String[lastDay + (FIRST_DAY_OF_WEEK * 6)];
        else
            dias = new String[lastDay + firstDay - (FIRST_DAY_OF_WEEK + 1)];

        int j;
        if (firstDay > 1) {
            for (j = 0; j < firstDay - FIRST_DAY_OF_WEEK; j++) {
                dias[j] = "";
            }
        } else {
            for (j = 0; j < FIRST_DAY_OF_WEEK * 6; j++) {
                dias[j] = "";
            }
            j = FIRST_DAY_OF_WEEK * 6 + 1;
        }

        int dayNumber = 1;
        for (int i = j - 1; i < dias.length; i++) {
            dias[i] = "" + dayNumber;
            dayNumber++;
        }
    }

    static class CalendarViewHolder {
        TextView diaView;
        ImageView imageView;
    }
}