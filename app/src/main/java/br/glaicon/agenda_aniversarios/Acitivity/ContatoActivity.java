package br.glaicon.agenda_aniversarios.Acitivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import br.glaicon.agenda_aniversarios.Contato.Contato;
import br.glaicon.agenda_aniversarios.Crop.Crop;
import br.glaicon.agenda_aniversarios.DAO.ContatoDAO;
import br.glaicon.agenda_aniversarios.ImagemHelper;
import br.glaicon.agenda_aniversarios.R;
import br.glaicon.agenda_aniversarios.RoundImage;
import br.glaicon.agenda_aniversarios.Volley.AppController;
import br.glaicon.agenda_aniversarios.Volley.BitmapCache;

public class ContatoActivity extends ActionBarActivity {

    public static final int EDITAR = 101;
    public static int ADICIONAR = 100;

    EditText edtNome;
    EditText edtEmail;
    Button btnAdicionar;
    EditText edtAniversario;
    ImageView image;
    ImagemHelper imagemHelper;

    Contato contato;
    ContatoDAO contatoDAO;
    long date = 0;
    Uri Uri = android.net.Uri.EMPTY;
    BitmapCache bitmapCache = AppController.getInstance().getBitmapCache();

    public Contato getContato() {
        if (contato == null) contato = new Contato();

        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public String obterNomeDoContato() {
        return edtNome.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contatoDAO = ContatoDAO.getInstance(getApplicationContext());


        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnAdicionar = (Button) findViewById(R.id.btnAdd);
        edtAniversario = (EditText) findViewById(R.id.edtAniversario);

        edtAniversario.setText(DateFormat.getDateInstance().format(new Date()));
        edtAniversario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edtAniversario.callOnClick();
            }
        });

        imagemHelper = new ImagemHelper(this);
        image = (ImageView) findViewById(R.id.imageView);

        extrairContatoDoExtra();
    }

    private void extrairContatoDoExtra() {
        final Contato contato = (Contato) getIntent().getSerializableExtra("contato");
        if (contato != null) {
            setContato(contato);

            edtNome.setText(contato.getNome());
            edtEmail.setText(contato.getEmail());
            edtAniversario.setText(DateFormat.getDateInstance().format(contato.getDate()));

            if (contato.getUriFoto() != "") {
                image.setImageDrawable(new RoundImage(imagemHelper.obterBitmapDaImagemDoContato(contato.getUriFoto())));
            }

            btnAdicionar.setText("Editar");
        }
    }

    public void adicionarContato_onClick(View view) throws IOException {
        getContato().setNome(obterNomeDoContato());
        getContato().setDate(new Date(date));
        getContato().setEmail(edtEmail.getText().toString());
        getContato().setUriFoto(imagemHelper.obterCaminhoDaImagemDoContato(getContato().getUUID().toString()));

        if (getContato().getID() > 0) {
            contatoDAO.atualizarContato(getContato());
        } else {
            contatoDAO.addContato(getContato());
        }

        Intent returnIntent = new Intent(this, MainActivity.class);
        returnIntent.putExtra("contato", getContato());
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void exibirDatePickerDialog_onClick(View view) {
        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Concluir", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    date = datePickerDialog.getDatePicker().getCalendarView().getDate();

                    DateFormat dateFormat = DateFormat.getDateInstance();
                    String result = dateFormat.format(new Date(date));
                    edtAniversario.setText(result);
                }
            }
        });

        datePickerDialog.show();
    }

    public void recortarImagem_onClick(View view) {
        Crop.pickImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Crop.REQUEST_PICK)
                try {
                    Crop.of(data.getData(), android.net.Uri.fromFile(imagemHelper.obterImagemPeloNomeDoContato(getContato().getUUID().toString()))).asSquare().start(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else if (requestCode == Crop.REQUEST_CROP) {
                setarImagemAoContato(data);
            }
        }
    }

    private void setarImagemAoContato(Intent data) {
        Bundle bundle = data.getExtras();
        Uri = (Uri) bundle.get(MediaStore.EXTRA_OUTPUT);

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(Uri.getPath());
            Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
            bitmapCache.remove(obterNomeDoContato());
            image.setImageDrawable(new RoundImage(myBitmap));
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
    }
}
