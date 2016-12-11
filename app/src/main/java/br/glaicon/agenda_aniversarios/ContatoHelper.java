package br.glaicon.agenda_aniversarios;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContatoHelper extends SQLiteOpenHelper {

    static ContatoHelper instance;

    protected ContatoHelper(Context context) {
        super(context, ConstantesSQL.DB_NAME, null, ConstantesSQL.DB_VERSION);
    }

    public static ContatoHelper getInstance(Context context) {
        if (instance == null)
            instance = new ContatoHelper(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConstantesSQL.DB_DROP);
        db.execSQL(ConstantesSQL.DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
