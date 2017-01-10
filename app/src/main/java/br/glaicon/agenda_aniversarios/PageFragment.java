package br.glaicon.agenda_aniversarios;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import br.glaicon.agenda_aniversarios.Acitivity.ContatoActivity;
import br.glaicon.agenda_aniversarios.Calendar.CalendarView;
import br.glaicon.agenda_aniversarios.Contato.Contato;
import br.glaicon.agenda_aniversarios.Contato.ContatoAdapter;
import br.glaicon.agenda_aniversarios.Contato.DataComparator;
import br.glaicon.agenda_aniversarios.Contato.NomeComparator;
import br.glaicon.agenda_aniversarios.DAO.ContatoDAO;
import br.glaicon.agenda_aniversarios.DAO.TipoOrdenacao;

import static android.app.Activity.RESULT_OK;

public class PageFragment extends Fragment {

    private int position;

    ContatoDAO contatoDAO;
    ListView listViewContatos;
    ContatoAdapter contatoAdapterContatos;
    ArrayList<Contato> contatos;

    DataComparator dataComparator = new DataComparator();
    NomeComparator nomeComparator = new NomeComparator();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contatoDAO = ContatoDAO.getInstance(getActivity());

        try {
            contatos = contatoDAO.buscarContatos(getTipoOrdenacao());

            Collections.sort(contatos, getComparator());

            contatoAdapterContatos = new ContatoAdapter(getActivity(), contatos, getTipoOrdenacao());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getPosition() {
        return position;
    }

    public void refreshContatos() throws ParseException {
        if (contatos != null) {
            contatos.clear();

            contatos.addAll(contatoDAO.buscarContatos(getTipoOrdenacao()));
            Collections.sort(contatos, getComparator());

            contatoAdapterContatos.updateList();
        }
    }

    public void addContato(Contato contato) {
        contatos.add(contato);
        Collections.sort(contatos, getComparator());
        contatoAdapterContatos.updateList();
    }

    public void atualizarContato(Contato contato) {
        atualizarContatoComMesmoID(contato);
        Collections.sort(contatos, getComparator());
        contatoAdapterContatos.updateList();
    }

    private int atualizarContatoComMesmoID(Contato contatoParaEditar) {

        for (int i = 0; i < contatos.size(); i++) {
            if (contatos.get(i).getID() == contatoParaEditar.getID())
                contatos.set(i, contatoParaEditar);
        }

        return 0;
    }

    private Comparator getComparator() {
        if (position == 0)
            return dataComparator;

        return nomeComparator;
    }

    public ListView getListViewContatos() {
        return listViewContatos;
    }

    private TipoOrdenacao getTipoOrdenacao() {
        if (position == 0)
            return TipoOrdenacao.DATA;

        return TipoOrdenacao.NOME;
    }

    public void addContato(ArrayList<Contato> addContatos) {
        contatos.clear();
        contatos.addAll(addContatos);

        Collections.sort(contatos, getComparator());
        contatoAdapterContatos.updateList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutsRes = 0;
        if (position == 0) {
            layoutsRes = R.layout.fragment_aniversariantes;
        } else if (position == 1) {
            layoutsRes = R.layout.fragment_contatos;
        }

        View view = inflater.inflate(layoutsRes, container, false);

        if (position == 0)
            listViewContatos = getListViewAniversariantes(view);
        else if (position == 1)
            listViewContatos = getListViewContatos(view);

        return view;
    }

    private ListView getListViewContatos(View view) {
        ListView listViewContatos = (ListView) view.findViewById(R.id.listView_contatos);
        listViewContatos.setAdapter(contatoAdapterContatos);
        listViewContatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = (Contato) contatoAdapterContatos.getItem(position);
                Toast.makeText(getActivity(), String.format("Contato %s - Email %s", contato.getNome(), contato.getEmail()), Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        listViewContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ContatoActivity.class);
                intent.putExtra("contato", (Contato) contatoAdapterContatos.getItem(position));

                getActivity().startActivityForResult(intent, ContatoActivity.EDITAR);
            }
        });

        return listViewContatos;
    }

    private ListView getListViewAniversariantes(View view) {
        ListView listViewContatos = (ListView) view.findViewById(R.id.listView_aniversariantes);
        listViewContatos.setAdapter(contatoAdapterContatos);
        listViewContatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = (Contato) contatoAdapterContatos.getItem(position);
                Toast.makeText(getActivity(), String.format("Contato %s - Data %s", contato.getNome(), DateFormat.getDateInstance().format(contato.getDate())), Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        listViewContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ContatoActivity.class);
                intent.putExtra("contato", (Contato) contatoAdapterContatos.getItem(position));

                getActivity().startActivityForResult(intent, ContatoActivity.EDITAR);
            }
        });

        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fragment_calendar);
        fragment.getView().setVisibility(View.INVISIBLE);
        ((CalendarView) fragment).setItens(contatos);

        return listViewContatos;
    }

    public static PageFragment newInstance(int index) {
        PageFragment f = new PageFragment();
        f.position = index;
        return f;
    }
}
