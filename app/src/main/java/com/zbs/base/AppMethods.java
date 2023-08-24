package com.zbs.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Toast;


import com.zbs.ocrtext.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AppMethods {

    public String errstr;

    public ArrayList<String> citems= new ArrayList<String>();


	private Context cont;
	private appGlobals gl;
	private SQLiteDatabase db;
	private BaseDatos.Insert ins;
	private BaseDatos.Update upd;
	private BaseDatos Con;
	private String sql;
	private String sp;


	public AppMethods(Context context, appGlobals global, BaseDatos dbconnection, SQLiteDatabase database) {
		try {
			cont=context;
			gl=global;
			Con=dbconnection;
			db=database;

			ins=Con.Ins;
			upd=Con.Upd;

			//rep=new clsRepBuilder(context,gl.prw,true,gl.peMon,gl.peDecImp, "");
		} catch (Exception e) {
			msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
		}
	}

	public void reconnect(BaseDatos dbconnection, SQLiteDatabase database) 	{
		Con=dbconnection;
		db=database;

		ins=Con.Ins;
		upd=Con.Upd;
	}


    //region Common
	
	protected void toast(String msg) {
		Toast toast= Toast.makeText(cont,msg, Toast.LENGTH_SHORT);  
		toast.setGravity(Gravity.TOP, 0, 0);
		toast.show();
	}

    protected void toastlong(String msg) {
        Toast toast= Toast.makeText(cont,msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

	@SuppressLint("SuspiciousIndentation")
	public void msgbox(String msg) {
		if (msg.isEmpty()) return;

		try {

			AlertDialog.Builder dialog = new AlertDialog.Builder(cont);
			dialog.setTitle(R.string.app_name);
			dialog.setMessage(msg);
			dialog.setCancelable(false);
			dialog.setNeutralButton("OK", (dialog1, which) -> {});
			dialog.show();

		} catch (Exception ex) {
			toast(ex.getMessage());
		}
	}


	//endregion
}