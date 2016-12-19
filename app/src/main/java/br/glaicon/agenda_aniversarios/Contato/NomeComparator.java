package br.glaicon.agenda_aniversarios.Contato;

import java.util.Comparator;

public class NomeComparator implements Comparator<Contato> {
    @Override
    public int compare(Contato contato1, Contato contato2) {
        return contato1.getNome().compareTo(contato2.getNome());
    }
}
