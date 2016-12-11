package br.glaicon.agenda_aniversarios;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarView extends Fragment {

    final String FORMATO_DATA = "MMMM yyyy";

    public Calendar calendar;
    public CalendarAdapter calendarAdapter;
    ArrayList<Contato> contatos;
    TextView titulo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calendar, container, false);
        calendar = Calendar.getInstance();
        calendarAdapter = new CalendarAdapter(getActivity(), calendar);

        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(calendarAdapter);

        titulo = (TextView) view.findViewById(R.id.title);

        atualizarTitulo();

        TextView botaoMesAnterior = (TextView) view.findViewById(R.id.previous);
        botaoMesAnterior.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (calendar.get(Calendar.MONTH) == calendar.getActualMinimum(Calendar.MONTH)) {
                    calendar.set((calendar.get(Calendar.YEAR) - 1), calendar.getActualMaximum(Calendar.MONTH), 1);
                } else {
                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
                }

                refreshCalendar();
            }
        });

        TextView botaoProximoMes = (TextView) view.findViewById(R.id.next);
        botaoProximoMes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (calendar.get(Calendar.MONTH) == calendar.getActualMaximum(Calendar.MONTH)) {
                    calendar.set((calendar.get(Calendar.YEAR) + 1), calendar.getActualMinimum(Calendar.MONTH), 1);
                } else {
                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
                }

                refreshCalendar();
            }
        });

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView date = (TextView) v.findViewById(R.id.date);
                if (date instanceof TextView && !date.getText().equals("")) {
                    ImageView imageView = (ImageView) v.findViewById(R.id.date_icon);
                    if (imageView.getVisibility() == View.VISIBLE) {
                        mostrarAniversariantes(date);
                    }
                }
            }

            private void mostrarAniversariantes(TextView date) {
                ArrayList<Contato> contatosDeAniversario = new ArrayList<Contato>();
                for (Contato contato : contatos) {
                    Calendar calendarDataAniversariante = Calendar.getInstance();
                    calendarDataAniversariante.setTime(contato.getDate());

                    if (String.valueOf(calendarDataAniversariante.get(Calendar.DAY_OF_MONTH)).equals(date.getText()) &&
                            calendarDataAniversariante.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))
                        contatosDeAniversario.add(contato);
                }

                exibirDialogAniversariantes(contatosDeAniversario);
            }

            private void exibirDialogAniversariantes(ArrayList<Contato> contatosDeAniversario) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setTitle(getString(R.string.aniversariantes));
                dialog.setContentView(R.layout.calendar_listview);

                ListView listViewAniversariantes = (ListView) dialog.findViewById(R.id.list_calendar);
                listViewAniversariantes.setAdapter(new ListCalendarAdapter(getActivity(), contatosDeAniversario));

                dialog.show();
            }
        });

        return view;
    }

    public void setItens(ArrayList<Contato> itemsContato) {
        contatos = itemsContato;

        calendarAdapter.setItems(contatos);
        calendarAdapter.notifyDataSetChanged();
    }

    public void refreshCalendar() {
        calendarAdapter.calcularDias();
        calendarAdapter.notifyDataSetChanged();

        setItens(contatos);
        atualizarTitulo();
    }

    private void atualizarTitulo() {
        if (titulo != null)
            titulo.setText(DateFormat.format(FORMATO_DATA, calendar));
    }
}