package br.glaicon.agenda_aniversarios;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Contato implements Serializable, Comparable<Contato> {

    private String nome;
    private Date date;
    private String email;
    private String uriFoto;

    public Contato(String nome, Date date, String email) {
        setDate(date);
        setEmail(email);
        setNome(nome);
    }

    public Contato(String nome, Date date, String email, String uriFoto) {
        setDate(date);
        setEmail(email);
        setNome(nome);
        setUriFoto(uriFoto);
    }

    public Contato(ContatoMensageria contato) {
        this.nome = contato.getNome();
        this.email = contato.getEmail();
        this.date = contato.getDate();
        this.uriFoto = contato.getUriFoto();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int compareTo(Contato contato) {

        Calendar calendarAtual = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(contato.getDate());
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

    public String getUriFoto() {
        return uriFoto;
    }

    public void setUriFoto(String uriFoto) {
        this.uriFoto = uriFoto;
    }

    public boolean estaDeAniversario() {
        Calendar calendarAtual = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        return calendar.get(Calendar.DAY_OF_MONTH) == calendarAtual.get(Calendar.DAY_OF_MONTH) && calendar.get(Calendar.MONTH) == calendarAtual.get(Calendar.MONTH);
    }

}
