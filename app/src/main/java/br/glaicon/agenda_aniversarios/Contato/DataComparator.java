package br.glaicon.agenda_aniversarios.Contato;

import java.util.Calendar;
import java.util.Comparator;

public class DataComparator implements Comparator<Contato> {

    @Override
    public int compare(Contato contato1, Contato contato2) {

        Calendar calendarAtual = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(contato2.getDate());

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(contato1.getDate());
        int month1 = 0;
        int month2 = 0;

        month1 = calendar1.get(Calendar.MONTH) - calendarAtual.get(Calendar.MONTH);
        month2 = calendar2.get(Calendar.MONTH) - calendarAtual.get(Calendar.MONTH);

        if (month1 == month2) {
            month1 = calendar1.get(Calendar.DAY_OF_MONTH) - calendarAtual.get(Calendar.DAY_OF_MONTH);
            month2 = calendar2.get(Calendar.DAY_OF_MONTH) - calendarAtual.get(Calendar.DAY_OF_MONTH);

            if (calendar1.get(Calendar.MONTH) != calendarAtual.get(Calendar.MONTH)) {
                if (month1 < month2)
                    return 1;
                else
                    return -1;
            }
        } else {
            if (month1 == 0) {
                if (calendar1.get(Calendar.DAY_OF_MONTH) - calendarAtual.get(Calendar.DAY_OF_MONTH) < 0)
                    return -1;
            } else if (month2 == 0) {
                if (calendar2.get(Calendar.DAY_OF_MONTH) - calendarAtual.get(Calendar.DAY_OF_MONTH) < 0)
                    return 1;
            }
        }

        if (month1 >= 0 && month2 < 0) {
            return 1;
        } else if (month2 >= 0 && month1 < 0) {
            return -1;
        } else if (month1 < 0 && month2 < 0) {
            if (month1 < month2)
                return 1;
            else
                return -1;

        } else if (month1 > month2) {
            return -1;
        } else
            return 1;
    }


}
