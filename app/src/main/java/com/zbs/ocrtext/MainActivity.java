package com.zbs.ocrtext;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends PBase {

    private EditText txtcod,txttext;

    private WebService ws;
    private wsCommit wscom;
    private Runnable rnUpdate;

    private String s,ss,tx,url,sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            super.InitBase();

            txtcod = findViewById(R.id.editTextText);
            txttext = findViewById(R.id.editTextText2);

            tx="DEFINITIVA No";
            url="http://52.41.114.122/MPosWS_QA/Mposws.asmx";

            ws=new WebService(MainActivity.this,url);
            wscom =new wsCommit(url);

            rnUpdate= () -> { resultadoUpdate();};


            //txttext.setText("DOCUMENTO DE EGRESO\n\n\nDEFINITIVA No. \n\n   6576576576576 \n\n hhjjj");

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

    }

    public void onBtnText(View view) {
        try {
             s=txttext.getText().toString();
             if (s.isEmpty()) toast("FALTA TEXTO");else exctractText();
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    public void onBtnCod(View view) {
        try {
            s=txtcod.getText().toString();
            if (s.isEmpty()) toast("FALTA codigo");else buscaCodigo();

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    private void exctractText() {
        int pp,ii;
        String[] sp;

        txtcod.setText("");
        try {
            pp=s.indexOf(tx);
            if (pp<0) {
                msgbox("No encontrado texto "+tx);return;
            }

            pp=pp+tx.length()+1;
            s=s.substring(pp);
            sp=s.split("\n");

            ii=sp.length;
            if (ii==0) {
                msgbox("Codigo no encontrado.");return;
            }

            for (int i = 0; i <ii; i++) {
                if (sp[i].length()>8) {
                    txtcod.setText(sp[i]);return;
                }
            }

            msgbox("Codigo no encontrado.");

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    private void buscaCodigo() {
        try {
            sql="SELECT Nombre FROM P_UNIDAD";
            ws.openDT(sql);
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    @Override
    protected void wsCallBack(Boolean throwing,String errmsg) {
        try {
            super.wsCallBack(throwing, errmsg);

            toast("Rows : " + ws.openDTCursor.getCount());

            sql="UPDATE P_UNIDAD SET user_agr=1 WHERE CODIGO_UNIDAD='OZ'";
            wscom.execute(sql,rnUpdate);

        } catch (Exception e) {
            msgbox(new Object() {}.getClass().getEnclosingMethod().getName() + " . " + e.getMessage());
        }

    }

    private void resultadoUpdate() {
        try {
            if (wscom.errflag) {
                msgbox(wscom.error);
            } else {
                msgbox("Registro correcto");
            }
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }


}