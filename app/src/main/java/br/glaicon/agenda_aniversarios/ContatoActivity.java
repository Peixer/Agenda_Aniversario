package br.glaicon.agenda_aniversarios;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import br.glaicon.agenda_aniversarios.Crop.Crop;
import br.glaicon.agenda_aniversarios.Volley.AppController;
import br.glaicon.agenda_aniversarios.Volley.BitmapCache;

public class ContatoActivity extends ActionBarActivity {

    EditText edtNome;
    EditText edtEmail;
    EditText edtAniversario;
    ImageView image;
    long date = 0;
    Uri Uri = android.net.Uri.EMPTY;
    public static int ADICIONAR = 100;
    BitmapCache bitmapCache = AppController.getInstance().getBitmapCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtAniversario = (EditText) findViewById(R.id.edtAniversario);
        edtAniversario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edtAniversario.callOnClick();
            }
        });

        image = (ImageView) findViewById(R.id.imageView);

        final Contato contato = (Contato) getIntent().getSerializableExtra("contato");
        if (contato != null) {
            edtNome.setText(contato.getNome());
            edtEmail.setText(contato.getEmail());
            edtAniversario.setText(DateFormat.getDateInstance().format(contato.getDate()));

            if (contato.getUriFoto() != "") {
                AbreImagem(contato.getUriFoto());
            }
        }
    }

    public void adicionar(View view) throws IOException {
        Date dateAdd = null;
        if (date != 0)
            dateAdd = new Date(date);
        else
            dateAdd = new Date();

        Contato contato = new Contato(edtNome.getText().toString(), dateAdd, edtEmail.getText().toString(), getCaminhoImagem());
        Intent returnIntent = new Intent();
        returnIntent.putExtra("contato", contato);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private String getCaminhoImagem() throws IOException {
        return getDirectoryBase() + "//" + edtNome.getText();
    }

    private void AbreImagem(String result) {
        try {
            FileInputStream inputStream = new FileInputStream(result);
            Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
            image.setImageDrawable(new RoundImage(myBitmap));
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adicionaAniversario(View view) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        Crop.pickImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Crop.REQUEST_PICK)
                try {
                    Crop.of(data.getData(), android.net.Uri.fromFile(getFile())).asSquare().start(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else if (requestCode == Crop.REQUEST_CROP) {
                Bundle bundle = data.getExtras();
                Uri = (Uri) bundle.get(MediaStore.EXTRA_OUTPUT);

                FileInputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(Uri.getPath());
                    Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                    bitmapCache.remove(edtNome.getText().toString());
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
    }

    private File getFile() throws IOException {
        File file = new File(getDirectoryBase());
        if (!file.exists())
            file.mkdir();

        File fileImagem = new File(getDirectoryBase() + "//" + edtNome.getText());
        if (!fileImagem.exists())
            fileImagem.createNewFile();

        return fileImagem;
    }

    private String getDirectoryBase() {
        return getFilesDir() + "//Imagem";
    }
}
