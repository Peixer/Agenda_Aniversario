package br.glaicon.agenda_aniversarios;

import java.util.Comparator;

public class DataComparator implements Comparator<Contato> {

    @Override
    public int compare(Contato contato1, Contato contato2) {
        return contato2.compareTo(contato1);
    }
}
