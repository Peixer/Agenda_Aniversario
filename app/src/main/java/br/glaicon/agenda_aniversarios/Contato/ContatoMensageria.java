package br.glaicon.agenda_aniversarios.Contato;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import br.glaicon.agenda_aniversarios.ImagemHelper;

public class ContatoMensageria implements Serializable {

    public ContatoMensageria(Context context) {
        this.context = context;
        imagemHelper = new ImagemHelper(context);
    }

    private Context context;
    private ImagemHelper imagemHelper;

    private String nome;
    private Date date;
    private String email;
    private String uriFoto;
    private byte[] bytesDaFoto;
    private java.util.UUID UUID;

    public String getUriFoto() {
        return uriFoto;
    }

    public String getNome() {
        return nome;
    }

    public Date getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }

    public java.util.UUID getUUID() {
        return UUID;
    }

    public ImagemHelper getImagemHelper() {
        if (imagemHelper == null) {
            Thread thread = new Thread();

            imagemHelper = new ImagemHelper(context);
        }

        return imagemHelper;
    }

    public ContatoMensageria(Context context, Contato contato) {
        this(context);

        nome = contato.getNome();
        email = contato.getEmail();
        date = contato.getDate();
        uriFoto = contato.getUriFoto();
        UUID = java.util.UUID.fromString(contato.getUUID().toString());
    }

    private void writeObject(ObjectOutputStream o)
            throws IOException {

        o.writeObject(nome);
        o.writeObject(date);
        o.writeObject(email);
        o.writeObject(UUID.toString());
        o.writeObject(getImagemHelper().obterBytesDaImagemDeContato(UUID.toString()));
    }

    private void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException {

        nome = (String) o.readObject();
        date = (Date) o.readObject();
        email = (String) o.readObject();
        UUID = java.util.UUID.fromString((String) o.readObject());
        bytesDaFoto = (byte[]) o.readObject();
    }

    public void GravarFotoDoContato(Context context) throws IOException {
        this.context = context;

        File arquivoDaImagem = getImagemHelper().obterImagemPeloNomeDoContato(UUID.toString());
        FileOutputStream out = new FileOutputStream(arquivoDaImagem);
        out.write(bytesDaFoto);
        out.close();

        uriFoto = arquivoDaImagem.getAbsolutePath();
    }
}
