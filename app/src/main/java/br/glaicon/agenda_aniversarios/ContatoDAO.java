package br.glaicon.agenda_aniversarios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContatoDAO {
    private ContatoHelper helper;
    static ContatoDAO instance;

    protected ContatoDAO() {
    }

    public ContatoDAO(Context context) {
        helper = ContatoHelper.getInstance(context.getApplicationContext());
    }

    public void open() throws SQLException {
        helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public static ContatoDAO getInstance(Context context) {
        if (instance == null)
            instance = new ContatoDAO(context);

        return instance;
    }

    public ArrayList<Contato> buscarContatos(TipoOrdenacao tipo) throws ParseException {

        SQLiteDatabase dataBase = helper.getReadableDatabase();
        ArrayList<Contato> result = new ArrayList<>();
        Cursor cursor = null;

        if (tipo == TipoOrdenacao.DATA)
            cursor = dataBase.query(ConstantesSQL.DB_TABLE, ConstantesSQL.COLUMNS, null, null, null, null, ConstantesSQL.COLUMN_DATE);
        else
            cursor = dataBase.query(ConstantesSQL.DB_TABLE, ConstantesSQL.COLUMNS, null, null, null, null, ConstantesSQL.COLUMN_NOME);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                result.add(getContato(cursor));
                cursor.moveToNext();
            }
        }

        cursor.close();
        return result;
    }

    public Contato getContato(int id, TipoOrdenacao tipo) throws ParseException {
        SQLiteDatabase dataBase = helper.getReadableDatabase();
        Contato result = null;
        Cursor cursor = null;

        if (tipo == TipoOrdenacao.DATA)
            dataBase.query(ConstantesSQL.DB_TABLE, ConstantesSQL.COLUMNS, "_id = ?", new String[]{String.valueOf(id)}, null, null, ConstantesSQL.COLUMN_DATE);
        else
            dataBase.query(ConstantesSQL.DB_TABLE, ConstantesSQL.COLUMNS, "_id = ?", new String[]{String.valueOf(id)}, null, null, ConstantesSQL.COLUMN_NOME);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                result = getContato(cursor);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return result;
    }

    public void addContato(Contato contato) {
        SQLiteDatabase dataBase = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ConstantesSQL.COLUMN_NOME, contato.getNome());
        values.put(ConstantesSQL.COLUMN_DATE, contato.getDate().getTime());
        values.put(ConstantesSQL.COLUMN_EMAIL, contato.getEmail());
        values.put(ConstantesSQL.COLUMN_URI, contato.getUriFoto());

        dataBase.insert(ConstantesSQL.DB_TABLE, null, values);
    }

    public void DropDataBase() {
        SQLiteDatabase dataBase = helper.getWritableDatabase();
        dataBase.delete(ConstantesSQL.DB_TABLE, null, null);
    }

    private Contato getContato(Cursor cursor) {
        String nome = cursor.getString(1);
        Date date = new Date(cursor.getLong(2));
        String email = cursor.getString(3);
        String uri = cursor.getString(4);

        return new Contato(nome, date, email, uri);
    }

    public void importarContatos(Context context) {
        for (Contato contato : obterVariosContatos(context)) {
            addContato(contato);
        }
    }


    private List<Contato> obterVariosContatos(Context context) {

        List<Contato> contatos = new ArrayList<Contato>();
        contatos.add(new Contato("Joao", new Date(2016, 1, 12), "joao@agenda.com", getDirectoryBase(context, "Glaicon Jose Peixer")));
        contatos.add(new Contato("Maria", new Date(1992, 2, 11), "maria@agenda.com", getDirectoryBase(context, "Glaicon Jose Peixer")));
        contatos.add(new Contato("Andre", new Date(1993, 3, 30), "andre@agenda.com", getDirectoryBase(context, "Glaicon Jose Peixer")));
        contatos.add(new Contato("Carlos", new Date(1994, 4, 12), "carlos@agenda.com", getDirectoryBase(context, "Glaicon Jose Peixer")));
        contatos.add(new Contato("Rodrigo", new Date(1999, 5, 22), "rodrigo@agenda.com", getDirectoryBase(context, "Glaicon Jose Peixer")));
        contatos.add(new Contato("Argeu", new Date(1995, 6, 10), "argeu@agenda.com", getDirectoryBase(context, "Glaicon Jose Peixer")));
        contatos.add(new Contato("Fernando", new Date(1980, 7, 11), "fernando@agenda.com", getDirectoryBase(context, "Glaicon Jose Peixer")));
        contatos.add(new Contato("Daniel", new Date(1997, 8, 1), "daniel@agenda.com", getDirectoryBase(context, "Glaicon Jose Peixer")));
        contatos.add(new Contato("Renan", new Date(1993, 9, 12), "renan@agenda.com", getDirectoryBase(context, "Glaicon Jose Peixer")));
        contatos.add(new Contato("Bruna", new Date(1992, 10, 17), "bruna@agenda.com", getDirectoryBase(context, "Glaicon Jose Peixer")));
        contatos.add(new Contato("Paulo", new Date(2002, 11, 18), "paulo@agenda.com", getDirectoryBase(context, "Glaicon Jose Peixer")));
        contatos.add(new Contato("Gabriel", new Date(2000, 12, 10), "gabriel@agenda.com", getDirectoryBase(context, "Glaicon Jose Peixer")));

        return contatos;
    }

    private String getDirectoryBase(Context context, String nome) {
        return context.getFilesDir() + "//Imagem//" + nome;
    }
}