package com.zbs.ocrtext;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import com.zbs.base.AppMethods;
import com.zbs.base.BaseDatos;
import com.zbs.base.DateUtils;
import com.zbs.base.MiscUtils;
import com.zbs.base.appGlobals;
import com.zbs.base.clsClasses;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class PBase extends FragmentActivity {

	public SQLiteDatabase db;
	public BaseDatos Con;
	protected BaseDatos.Insert ins;
	protected BaseDatos.Update upd;
	protected String sql;

	public appGlobals gl;
	public MiscUtils mu;
	public DateUtils du;
	public AppMethods app;
	protected Application vApp;
	protected clsClasses clsCls = new clsClasses();
	protected InputMethodManager keyboard;	
	
	protected int itemid,browse,mode;
	protected int selid,selidx,deposito;
	protected long fecha;
	protected String s,ss;

	public ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plist_base);

	}

	public void InitBase(){
		
		Con = new BaseDatos(this);

	    opendb();

	    ins=Con.Ins;upd=Con.Upd;

		vApp=this.getApplication();
		gl=(appGlobals) this.getApplication();

		mu=new MiscUtils(this,"Q");
		du=new DateUtils();
		app=new AppMethods(this,gl,Con,db);
		
		keyboard = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);

	    browse=0;
	}

	public void ProgressDialog(String mensaje){
		progress=new ProgressDialog(this);
		progress.setMessage(mensaje);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setIndeterminate(true);
		progress.setProgress(0);
		progress.show();
	}

	// CallBacks
	
	protected void wsCallBack(Boolean throwing,String errmsg) throws Exception {
		if (throwing) throw new Exception(errmsg);
	}

    public void wsCallBack(Boolean throwing,String errmsg,int errlevel) throws Exception {
		if (throwing) throw new Exception(errmsg);
	}

    public void felCallBack() throws Exception {

	}

    public void felProgress(String msg) throws Exception {
//		Handler mtimer = new Handler();
//		Runnable mrunner = () -> lbl1.setText(msg);
//		mtimer.postDelayed(mrunner,50);
	}


    // Aux
	
	protected void closekeyb(){
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
		
	protected void showkeyb(){
		if (keyboard != null) {
			keyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}
	}
		
	protected void hidekeyb() {
		keyboard.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}	
	
	protected void msgbox(String msg){
        try{
            //mu.msgbox2(msg);
            mu.msgbox(msg);
        }catch (Exception ex){
            addlog(new Object(){}.getClass().getEnclosingMethod().getName(),ex.getMessage(),"");
        }
    }

    protected void msgbox2(String msg){
        try{
            mu.msgbox2(msg);
        }catch (Exception ex){
            addlog(new Object(){}.getClass().getEnclosingMethod().getName(),ex.getMessage(),"");
        }
    }

    protected void msgboxex(String msg){
        try{
            mu.msgbox(msg);
        }catch (Exception ex){
            addlog(new Object(){}.getClass().getEnclosingMethod().getName(),ex.getMessage(),"");
        }
    }

	protected void toast(String msg) {
		toastcent(msg);
	}
	
	protected void toast(double val) {
		toastcent(""+val);
	}
	
	protected void toastlong(String msg) {
		Toast toast= Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

    protected void toastlongtop(String msg) {
        Toast toast= Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

	protected void toastcent(String msg) {

		if (mu.emptystr(msg)) return;

		Toast toast= Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT);  
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

    protected void toastcentlong(String msg) {

        if (mu.emptystr(msg)) return;

        Toast toast= Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    protected void toasttop(String msg) {

        if (mu.emptystr(msg)) return;

        Toast toast= Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
	
	public void addlog(final String methodname, String msg, String info) {
        /*
		final String vmethodname = methodname;
		final String vmsg = msg;
		final String vinfo = info;

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				setAddlog(vmethodname,vmsg, vinfo);
			}
		}, 500);
        */
	}

	protected void setAddlog(String methodname,String msg,String info) {

		BufferedWriter writer = null;
		FileWriter wfile;

		try {

			String fname = Environment.getExternalStorageDirectory()+"/roadlog.txt";
			wfile=new FileWriter(fname,true);
			writer = new BufferedWriter(wfile);

			writer.write("Método: " + methodname + " Mensaje: " +msg + " Info: "+ info );
			writer.write("\r\n");
			writer.close();

		} catch (Exception e) {
			msgbox("Error " + e.getMessage());
		}
	}

	protected String iif(boolean condition,String valtrue,String valfalse) {
		if (condition) return valtrue;else return valfalse;
	}

	protected double iif(boolean condition,double valtrue,double valfalse) {
		if (condition) return valtrue;else return valfalse;
	}

	protected double iif(boolean condition,int valtrue,int valfalse) {
		if (condition) return valtrue;else return valfalse;
	}



	// Activity Events
	
	@Override
 	protected void onResume() {
		try {
			opendb();
		} catch(Exception ex) {}
		super.onResume();
	}

	@Override
	protected void onPause() {
		try {
			Con.close();
		} catch (Exception e) { }
	    super.onPause();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	public void opendb() {
		try {
			db = Con.getWritableDatabase();
			if (db!= null) {
				Con.vDatabase=db;
  			} else {
			}
	    } catch (Exception e) {
			String ss=e.getMessage();
			ss=ss+"";
	    	//mu.msgbox(e.getMessage());
	    }
	}			

}
