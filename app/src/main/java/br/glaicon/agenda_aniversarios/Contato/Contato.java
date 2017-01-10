package br.glaicon.agenda_aniversarios.Contato;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Contato implements Serializable {

    private String nome;
    private Date date;
    private String email;
    private String uriFoto;
    private UUID UUID;
    private long ID;

    public Contato(){
        UUID = java.util.UUID.randomUUID();
    }

    public Contato(String nome, Date date, String email) {
        this();

        setDate(date);
        setEmail(email);
        setNome(nome);
    }

    public Contato(long id, String nome, Date date, String email, String uriFoto, String uuid) {
        setID(id);
        setDate(date);
        setEmail(email);
        setNome(nome);
        setUriFoto(uriFoto);
        setUUID(java.util.UUID.fromString(uuid));
    }
    public Contato(ContatoMensageria contato) {
        this.nome = contato.getNome();
        this.email = contato.getEmail();
        this.date = contato.getDate();
        this.uriFoto = contato.getUriFoto();
        this.UUID = contato.getUUID();
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

    public java.util.UUID getUUID() {
        return UUID;
    }

    public void setUUID(java.util.UUID UUID) {
        this.UUID = UUID;
    }

    public String getUriFoto() {
        return uriFoto;
    }

    public void setUriFoto(String uriFoto) {
        this.uriFoto = uriFoto;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public boolean estaDeAniversario() {
        Calendar calendarAtual = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        return calendar.get(Calendar.DAY_OF_MONTH) == calendarAtual.get(Calendar.DAY_OF_MONTH) && calendar.get(Calendar.MONTH) == calendarAtual.get(Calendar.MONTH);
    }
}
