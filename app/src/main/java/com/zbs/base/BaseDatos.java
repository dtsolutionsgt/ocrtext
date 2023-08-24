package com.zbs.base;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import com.zbs.ocrtext.R;

import java.util.ArrayList;
import java.util.List;

public class BaseDatos extends SQLiteOpenHelper {

      //SQLiteDatabase.enableWriteAheadLogging()

	  public SQLiteDatabase vDatabase;	
	  public Context vcontext;
	  public int Created;
	  public Insert Ins;
	  public Update Upd;
	  private BaseDatosScript DBScript;

	  private static final String DATABASE_NAME =
			               Environment.getExternalStorageDirectory().getPath() + "/ocrtext.db";
	  private static final int DATABASE_VERSION = 1;
	  	
	  public BaseDatos(Context context) {
		 		  	  
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    		       
	    Ins=new Insert();
	    Upd=new Update();
	    
	    DBScript=new BaseDatosScript(context);
	    
	    Created=0;
	    vcontext=context;

	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
		 		 	   
		Created=1;
					
		if (scriptDatabase(database)==0) {
			Created=-1;return;
		}
	    
		if (scriptData(database)==0) {
			Created=-1;return;
		}
		
		dbCreated();
		
	  }

	  private int scriptDatabase(SQLiteDatabase database) {
		 int Rslt;
		  
		 Rslt=DBScript.scriptDatabase(database);
		 return Rslt;
	  }
	 
	  private int scriptData(SQLiteDatabase database) {
		 int Rslt;
			  
		 Rslt=DBScript.scriptData(database);
		 return Rslt;
	  } 

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		  Toast.makeText(vcontext,"UPDATE DB", Toast.LENGTH_SHORT).show();
	  }
	  
	  public Cursor OpenDT(String pSQL) {

	  	Cursor vCursor = null;
        String vError="";

		  try {

		  	  if (!vDatabase.isOpen()) vDatabase = getWritableDatabase();

			  vCursor = vDatabase.rawQuery(pSQL, null);
			  if (vCursor != null){
				  vCursor.moveToLast();
			  } else {
			  	throw new Exception("No se obtuvo el registro " + pSQL);
			  }

		  } catch(Exception ex){
		  	 msgbox(new Object() {}.getClass().getEnclosingMethod().getName() + " . " + ex.getMessage()+" : "+ pSQL +"\n\n" +
                     "REINICIE EL EQUIPO Y REPITE LA OPERACION");
			  vError = ex.getMessage();
		  }

		  return vCursor;
	  }
	  
	  // Public class Insert
	  
	  public class Insert {
			
		  private List<String> clFList = new ArrayList<String>();
		  private List<String> clVList = new ArrayList<String>();
		  private String clTable;
		  
		  public Insert() {
			  clFList.clear(); 
		      clVList.clear();
		      clTable = "";
		  }
		  
		  public void init(String TableName) {
			  clFList.clear(); 
		      clVList.clear();
		      clTable = TableName;
		  }

		  public void add(String pField, String pValue , String pTipo) {

			  String SV;
			  
			  try  {

				  if (pField == "") return;
				  if (pTipo == "") return;
					
				  pValue=pValue.replace("'", "");
				  SV="'" + pValue + "'";
				  
				  if (pTipo == "S") SV="'" +  pValue + "'";
				  if (pTipo == "N") SV= pValue;
				  				  
				  clFList.add(pField);
		          clVList.add(SV);
				  
			  } catch (Exception e) { }
			  
		  }
		  
		  public void add(String pField, String pValue ) {

			  String SV;
			  
			  try  {
				  //if (pField == "") return;
				  
				  pValue=pValue.replace("'", "");

				  if (pValue.equals("null")){
				  	pValue="";
				  }

				  SV="'" + pValue + "'";
				  				  
				  clFList.add(pField);
		          clVList.add(SV);
				  
			  } catch (Exception e) { }
			  
		  }

		  public void add(String pField, int pValue) {

			  String SV;
			  
			  try 
			  {
				  if (pField == "") {return;}
				  SV= String.valueOf(pValue);
				  				  
				  clFList.add(pField);
		          clVList.add(SV);
				  
			  } catch (Exception e) { }
			  
		  }

		  public void add(String pField, long pValue) {

			  String SV;

			  try{

				  if (pField == "") {return;}
				  SV= ""+pValue;

				  clFList.add(pField);
				  clVList.add(SV);

			  } catch (Exception e) { }

		  }

		  public void add(String pField, double pValue) {

			  String SV;
			  
			  try 
			  {
				  if (pField == "") {return;}
				  SV= String.valueOf(pValue);
				  				  
				  clFList.add(pField);
		          clVList.add(SV);
				  
			  } catch (Exception e) { }
			  
		  }

          public void add(String pField, boolean pValue) {

              String SV;

              try  {

                  clFList.add(pField);
                  if (pValue) clVList.add("1");else clVList.add("0");

              } catch (Exception e) { }

          }
		   
		  public String sql() {

			  String sVal, S, SF, SV;
			  			  
			  if (clTable == "") {return "";}
			  if (clFList.isEmpty()) {return "";}
			  
			  try  {

				  SV="";
				  SF="";
				  S="INSERT INTO " + clTable + " (";

				  for(int I = 0; I < clFList.size() ; I = I+1) {

					  sVal=clFList.get(I);
					  SF=SF + sVal; 
					  if (I < clFList.size()-1) {SF=SF + ",";}
					  
					  sVal=clVList.get(I);
					  SV=SV + sVal; 
					  if (I < clFList.size()-1) {SV=SV + ",";}
				  }
				  
				  S = S + SF + ") VALUES (" + SV + ")";
				  
				  return S;
				 				  
			  } catch (Exception e) { 
				  return "";
			  }
			  
		  }
		  
	} 

	  // Public class Update
	  
	  public class Update {
			
		  private List<String> clFList = new ArrayList<String>();
		  private String clTable,vWhere;
		  
		  public Update() {
			  clFList.clear(); 
		      clTable = "";
		  }
		  
		  public void init(String TableName) {
			  clFList.clear(); 
		      clTable = "UPDATE " + TableName + " SET ";
		  }
	 
		  
		  public void add(String pField, String pValue , String pTipo) {

			  String SV;
			  
			  try  {

				  if (pField == "") return;
				  if (pTipo == "") return;
					
				  pValue=pValue.replace("'", "");
				  SV="'" + pValue + "'";
				  
				  if (pTipo == "S") SV="'" +  pValue + "'";
				  if (pTipo == "N") SV= pValue;
				  
				  clFList.add(pField + " = "+ SV);
				  
			  } catch (Exception e) { }
			  
		  }
		  
		  public void add(String pField, String pValue) {

			  String SV;
			  
			  try 
			  {
				  if (pField == "") return;
				  
				  pValue=pValue.replace("'", "");
				  SV="'" + pValue + "'";
				  clFList.add(pField + " = "+ SV);

			  } catch (Exception e) { }
			  
		  }

		  public void add(String pField, boolean pValue ) {

              String SV;

              try  {
                  if (pField == "") return;
                  if (pValue) SV="1";else SV="0";
                  clFList.add(pField + " = "+ SV);
              } catch (Exception e) { }

          }

          public void add(String pField, int pValue ) {

              String SV;

              try  {
                  if (pField == "") return;
                  SV= String.valueOf(pValue);
                  clFList.add(pField + " = "+ SV);
              } catch (Exception e) { }

          }
		  
		  public void add(String pField, double pValue ) {

			  String SV;
			  
			  try  {
				  if (pField == "") return;
				  SV= String.valueOf(pValue);
				  clFList.add(pField + " = "+ SV);
			  } catch (Exception e) { }
			  
		  }
		  
		  public void Where(String pWhere) {
			  vWhere = " WHERE " + pWhere;
		  }
	      	  
		  public String sql() {

			  String sVal,vUpDate;
			  			  
			  if (clTable == "") return "";
			  if (clFList.isEmpty()) return "";
			  
			  try  {

				  vUpDate = clTable;
						  
				  for(int I = 0; I < clFList.size() ; I = I+1) {
					  sVal=clFList.get(I);
					  vUpDate=vUpDate + sVal; 
					  if (I < clFList.size()-1) {vUpDate=vUpDate + ",";}
				  }
				  
				  vUpDate=vUpDate + vWhere;
				  
				  return vUpDate;
				 				  
			  } catch (Exception e) { 
				  return "";
			  }
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

	private void dbCreated() {
      		msgbox("La base de datos ha sido creada.");
	}
}


	  