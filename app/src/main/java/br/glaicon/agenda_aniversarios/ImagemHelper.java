package br.glaicon.agenda_aniversarios;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImagemHelper {

    private Context context;

    public ImagemHelper(Context context) {
        this.context = context;
    }

    private String obterDiretorioBaseDasImagens() {
        return this.context.getFilesDir() + "//Imagem";
    }

    public String obterCaminhoDaImagemDoContato(String nome) {
        return obterDiretorioBaseDasImagens() + "//" + nome;
    }

    public File obterImagemPeloNomeDoContato(String nome) throws IOException {
        File diretorioBase = new File(obterDiretorioBaseDasImagens());
        if (!diretorioBase.exists())
            diretorioBase.mkdir();

        File arquivoImagem = new File(obterDiretorioBaseDasImagens(), nome);
        if (!arquivoImagem.exists())
            arquivoImagem.createNewFile();

        return arquivoImagem;
    }

    public byte[] obterBytesDaImagemDeContato(String nome) throws IOException {

        File arquivo = new File(obterDiretorioBaseDasImagens(), nome);
        FileInputStream inputStream = new FileInputStream(arquivo);
        byte[] bytesDaImagem = new byte[(int) arquivo.length()];
        inputStream.read(bytesDaImagem);

        return bytesDaImagem;
    }

    public Bitmap obterBitmapDaImagemDoContato(String uriFoto){
        Bitmap bitmapDaImagem = null;
        try {
            FileInputStream inputStream = new FileInputStream(uriFoto);
            bitmapDaImagem = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmapDaImagem;
    }
}

