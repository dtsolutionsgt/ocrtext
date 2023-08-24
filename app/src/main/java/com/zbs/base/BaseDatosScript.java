package com.zbs.base;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.zbs.ocrtext.R;

public class BaseDatosScript {

    private Context vcontext;

    public BaseDatosScript(Context context) {
        vcontext=context;
    }

    public int scriptDatabase(SQLiteDatabase database) {

        String vSQL;

        try {

            vSQL="CREATE TABLE [PARAMS] ("+
                    "ID integer NOT NULL primary key,"+
                    "EMPID     INTEGER  NOT NULL,"+
                    "DBVER     INTEGER  NOT NULL,"+
                    "PARAM1    INTEGER  NOT NULL,"+  //
                    "PARAM2    INTEGER  NOT NULL,"+
                    "PRN       TEXT     NOT NULL,"+
                    "PRNPARAM  TEXT     NOT NULL,"+
                    "PRNSERIE  TEXT     NOT NULL,"+
                    "LIC       TEXT     NOT NULL,"+
                    "LICPARAM  TEXT     NOT NULL,"+
                    "URL       TEXT     NOT NULL,"+
                    "SUCURSAL  INTEGER     NOT NULL,"+
                    "RUTA      INTEGER     NOT NULL);";
            database.execSQL(vSQL);


            vSQL="CREATE TABLE [P_PARAMEXT] ("+
                    "ID integer NOT NULL primary key,"+
                    "NOMBRE TEXT NOT NULL,"+
                    "VALOR  TEXT, " +
                    "RUTA INT);";
            database.execSQL(vSQL);

            return 1;

        } catch (SQLiteException e) {
            msgbox(e.getMessage());
            return 0;
        }

    }


    public int scriptData(SQLiteDatabase db) {

        try {


            return 1;

        } catch (Exception e) {
            msgbox(e.getMessage());return 0;
        }
    }

    @SuppressLint("SuspiciousIndentation")
    public void msgbox(String msg) {
        if (msg.isEmpty()) return;

        try {

            AlertDialog.Builder dialog = new AlertDialog.Builder(vcontext);
            dialog.setTitle(R.string.app_name);
            dialog.setMessage(msg);
            dialog.setCancelable(false);
            dialog.setNeutralButton("OK", (dialog1, which) -> {});
            dialog.show();

        } catch (Exception ex) {
         }
    }

}