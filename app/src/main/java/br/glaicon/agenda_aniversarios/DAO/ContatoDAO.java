package br.glaicon.agenda_aniversarios.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import br.glaicon.agenda_aniversarios.Contato.Contato;

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
        values.put(ConstantesSQL.COLUMN_UUID, contato.getUUID().toString());

        long id = dataBase.insert(ConstantesSQL.DB_TABLE, null, values);
        contato.setID(id);
    }

    public void atualizarContato(Contato contato) {
        SQLiteDatabase dataBase = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ConstantesSQL.COLUMN_NOME, contato.getNome());
        values.put(ConstantesSQL.COLUMN_DATE, contato.getDate().getTime());
        values.put(ConstantesSQL.COLUMN_EMAIL, contato.getEmail());
        values.put(ConstantesSQL.COLUMN_URI, contato.getUriFoto());
        values.put(ConstantesSQL.COLUMN_UUID, contato.getUUID().toString());

        dataBase.update(ConstantesSQL.DB_TABLE, values, "_ID = ?", new String[]{String.valueOf(contato.getID())});
    }

    private Contato getContato(Cursor cursor) {
        long id = cursor.getLong(0);
        String nome = cursor.getString(1);
        Date date = new Date(cursor.getLong(2));
        String email = cursor.getString(3);
        String uri = cursor.getString(4);
        String uuid = cursor.getString(5);

        return new Contato(id, nome, date, email, uri, uuid);
    }
}