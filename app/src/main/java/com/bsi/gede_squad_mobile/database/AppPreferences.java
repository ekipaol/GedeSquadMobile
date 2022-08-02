package com.bsi.gede_squad_mobile.database;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor spEditor;
    private Context mContext;


    public AppPreferences(Context context) {
        this.mContext = context;
        sharedPref = mContext.getSharedPreferences(Constants.Preferences.MAIN_CONFIG, Context.MODE_PRIVATE);
        spEditor = sharedPref.edit();
    }



    public void setJabatan (String data){
        spEditor.putString(Constants.Login.JABATAN, data);
        spEditor.commit();
    }

    public String getJabatan (){
        return sharedPref.getString(Constants.Login.JABATAN, "jabatan");
    }

}
