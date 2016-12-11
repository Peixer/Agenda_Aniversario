package br.glaicon.agenda_aniversarios;

import java.util.Comparator;

class NomeComparator implements Comparator<Contato> {
    @Override
    public int compare(Contato contato1, Contato contato2) {
        return contato1.getNome().compareTo(contato2.getNome());
    }
}
