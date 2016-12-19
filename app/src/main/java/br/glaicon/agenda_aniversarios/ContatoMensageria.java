package br.glaicon.agenda_aniversarios;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

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
    }

    private void writeObject(ObjectOutputStream o)
            throws IOException {

        o.writeObject(nome);
        o.writeObject(date);
        o.writeObject(email);
        o.writeObject(getImagemHelper().obterBytesDaImagemDeContato(nome));
    }

    private void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException {

        nome = (String) o.readObject();
        date = (Date) o.readObject();
        email = (String) o.readObject();
        bytesDaFoto = (byte[]) o.readObject();
    }

    public void GravarFotoDoContato(Context context) throws IOException {
        this.context = context;

        File arquivoDaImagem = getImagemHelper().obterImagemPeloNomeDoContato(nome);
        FileOutputStream out = new FileOutputStream(arquivoDaImagem);
        out.write(bytesDaFoto);
        out.close();

        uriFoto = arquivoDaImagem.getAbsolutePath();
    }
}
