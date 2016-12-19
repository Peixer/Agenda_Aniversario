package br.glaicon.agenda_aniversarios;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;

public class ExportarActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void click(View view) throws ParseException {
        publicarContatos();
    }

    private void publicarContatos() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            setupConnectionFactory(factory);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.confirmSelect();
            channel.queueDeclare("task_queue", true, false, false, null);
            ArrayList<ContatoMensageria> listaDeContatosMensageria = obterContatos();
            if (listaDeContatosMensageria == null) {
                finish(); //todo rever os locais aonde pode acontecer erro e ver como poderam ser tratados
            } else {
                for (ContatoMensageria contato : listaDeContatosMensageria) {
                    channel.basicPublish("", "task_queue", MessageProperties.PERSISTENT_TEXT_PLAIN, serialize(contato));
                    channel.waitForConfirmsOrDie();
                }
            }

        } catch (Exception e) {
            Log.d("", "Connection broken: " + e.getClass().getName());
        }

        Toast.makeText(this, "Concluido",Toast.LENGTH_SHORT).show();
    }


    private ArrayList<ContatoMensageria> obterContatos() {
        ContatoDAO contatoDAO = ContatoDAO.getInstance(this);
        try {
            ArrayList<Contato> listaDeContatos = contatoDAO.buscarContatos(TipoOrdenacao.NOME);
            ArrayList<ContatoMensageria> listaDeContatosMensageria = new ArrayList<>();

            for (Contato contato : listaDeContatos) {
                listaDeContatosMensageria.add(new ContatoMensageria(this, contato));
            }

            return listaDeContatosMensageria;
        } catch (ParseException e) {
            e.printStackTrace();
            finish();
        }

        return null;
    }

    private void setupConnectionFactory(ConnectionFactory factory) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        factory.setUri("XXXXXX");
    }

    public byte[] serialize(ContatoMensageria contato) throws IOException {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutputStream o = new ObjectOutputStream(b)) {
                o.writeObject(contato);
            }
            return b.toByteArray();
        }
    }
}
