package br.glaicon.agenda_aniversarios.DAO;


public final class ConstantesSQL {
    public static String DB_NAME = "agenda_aniversario";
    public static String DB_TABLE = "anivesario";
    public static String COLUMN_ID = "_ID";
    public static String COLUMN_NOME = "NOME";
    public static String COLUMN_DATE = "DATA";
    public static String COLUMN_EMAIL = "EMAIL";
    public static String COLUMN_URI = "URI";
    public static String COLUMN_UUID = "UUID";

    public static int DB_VERSION = 3;

    public static String[] COLUMNS = new String[]{COLUMN_ID, COLUMN_NOME, COLUMN_DATE, COLUMN_EMAIL, COLUMN_URI, COLUMN_UUID};

    public static String DB_DROP = "DROP TABLE IF EXISTS " + DB_TABLE;
    public static String DB_CREATE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s LONG, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL);",
            DB_TABLE, COLUMN_ID, COLUMN_NOME, COLUMN_DATE, COLUMN_EMAIL, COLUMN_URI, COLUMN_UUID);
}
