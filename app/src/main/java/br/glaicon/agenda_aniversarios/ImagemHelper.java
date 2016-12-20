package br.glaicon.agenda_aniversarios;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import br.glaicon.agenda_aniversarios.Contato.Contato;
import br.glaicon.agenda_aniversarios.Volley.BitmapCache;
import br.glaicon.agenda_aniversarios.Volley.ImageUtil;

public class ImagemHelper {

    private Context context;

    public ImagemHelper(Context context) {
        this.context = context;
    }

    private String obterDiretorioBaseDasImagens() {
        return this.context.getFilesDir() + "//Imagem";
    }

    public String obterCaminhoDaImagemDoContato(String uuid) {
        return obterDiretorioBaseDasImagens() + "//" + uuid;
    }

    public File obterImagemPeloNomeDoContato(String uuid) throws IOException {
        File diretorioBase = new File(obterDiretorioBaseDasImagens());
        if (!diretorioBase.exists())
            diretorioBase.mkdir();

        File arquivoImagem = new File(obterDiretorioBaseDasImagens(), uuid);
        if (!arquivoImagem.exists())
            arquivoImagem.createNewFile();

        return arquivoImagem;
    }

    public byte[] obterBytesDaImagemDeContato(String uuid) throws IOException {

        try {
            File arquivo = new File(obterDiretorioBaseDasImagens(), uuid);
            FileInputStream inputStream = new FileInputStream(arquivo);
            byte[] bytesDaImagem = new byte[(int) arquivo.length()];
            inputStream.read(bytesDaImagem);

            return bytesDaImagem;
        } catch (Exception e) {
            //todo adicionar notificação para o raygun aqui
            e.printStackTrace();
        }

        return new byte[0];
    }

    public RoundImage obterRoundImagemDoContato(Contato contato, BitmapCache bitmapCache, Resources resource) {
        Bitmap bitmap = null;
        if (contato.getUriFoto() != "") {
            bitmap = bitmapCache.getBitmap(contato.getUUID().toString());

            if (bitmap == null) {
                try {
                    bitmap = BitmapFactory.decodeFile(contato.getUriFoto());

                    bitmap = ImageUtil.ResizedBitmap(bitmap);

                    bitmapCache.putBitmap(contato.getUUID().toString(), bitmap);
                } catch (Exception e) {
                    bitmap = ((BitmapDrawable) resource.getDrawable(R.drawable.user)).getBitmap();
                }
            }
        } else {
            bitmap = ((BitmapDrawable) resource.getDrawable(R.drawable.user)).getBitmap();
        }

        return new RoundImage(bitmap);
    }

    public RoundImage obterRoundImageDeUmaURI(Uri uri, Contato contato, BitmapCache bitmapCache, Resources resource) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(uri.getPath());
            Bitmap bitmapDaURI = BitmapFactory.decodeStream(inputStream);
            bitmapCache.remove(contato.getUUID().toString());
            return new RoundImage(bitmapDaURI);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        Bitmap bitmap = ((BitmapDrawable) resource.getDrawable(R.drawable.user)).getBitmap();
        return new RoundImage(bitmap);
    }
}

