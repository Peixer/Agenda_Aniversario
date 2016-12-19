package br.glaicon.agenda_aniversarios;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public class ImportarActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importar_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void importar_onClick(View view) throws ParseException {
        importarContatos();
    }

    private void importarContatos() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            setupConnectionFactory(factory);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            final ContatoDAO contatoDAO = ContatoDAO.getInstance(this);

            channel.confirmSelect();
            channel.queueDeclare("task_queue", true, false, false, null);

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        ContatoMensageria contato = deserialize(body);
                        contato.GravarFotoDoContato(getApplicationContext());

                        System.out.println("Recebeu o contato : " + contato.getNome());

                        contatoDAO.addContato(new Contato(contato));

                        System.out.println("Salvou o contato : " + contato.getNome());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            };
            channel.basicConsume("task_queue", true, consumer);

        } catch (Exception e) {
            Log.d("", "Connection broken: " + e.getClass().getName());
        }
    }

    private void setupConnectionFactory(ConnectionFactory factory) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        factory.setUri("XXXXXX");
    }

    private ContatoMensageria deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(b)) {
                return (ContatoMensageria) objectInputStream.readObject();
            }
        }
    }
}
