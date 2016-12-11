package br.glaicon.agenda_aniversarios;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.text.ParseException;
import java.util.ArrayList;

public class BroadcastAniversario extends BroadcastReceiver {

    public BroadcastAniversario() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ContatoDAO contatoDAO = ContatoDAO.getInstance(context);

        try {
            ArrayList<Contato> contatos = contatoDAO.buscarContatos(TipoOrdenacao.DATA);
            ArrayList<String> aniversariantes = new ArrayList<String>();

            for (Contato contato : contatos) {
                if(contato.estaDeAniversario())
                    aniversariantes.add(contato.getNome());
            }

            if (!aniversariantes.isEmpty()) {
                enviarNotificacao(context, aniversariantes);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void enviarNotificacao(Context context, ArrayList<String> aniversariantes) {
        StringBuilder retorno = montarMensagemNotificacao(aniversariantes);

        NotificationCompat.Builder builder = criarBuilder(context, retorno);

        Intent intentMain = new Intent(context, MainActivity.class);
        PendingIntent intentPeding = PendingIntent.getActivity(context, 0, intentMain, 0);
        builder.setContentIntent(intentPeding);

        Notification notification = builder.build();
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(100, notification);
    }

    private NotificationCompat.Builder criarBuilder(Context context, StringBuilder retorno) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(context.getString(R.string.hint_aniversario));
        builder.setContentText(context.getString(R.string.aniversario_de) + retorno.toString());
        builder.setSmallIcon(R.drawable.cake);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);

        return builder;
    }

    private StringBuilder montarMensagemNotificacao(ArrayList<String> aniversariantes) {
        StringBuilder retorno = new StringBuilder();
        for (String nome : aniversariantes) {
            if (retorno.toString() != "")
                retorno.append(", " + nome);
            else
                retorno.append(nome);
        }
        return retorno;
    }
}