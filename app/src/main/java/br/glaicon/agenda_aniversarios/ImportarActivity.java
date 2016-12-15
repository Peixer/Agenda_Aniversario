package br.glaicon.agenda_aniversarios;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

public class ImportarActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importar_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void importar_onClick(View view) {
        ContatoDAO contatoDAO = ContatoDAO.getInstance(this);

        contatoDAO.importarContatos(this.getApplication());

        Toast.makeText(this, "Contatos importados com sucesso", Toast.LENGTH_SHORT).show();
    }
}
