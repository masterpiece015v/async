package com.example.watabe.async;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Function;

//非同期通信用クラス
public class HttpResponseAsync extends AsyncTask<Void,Void,String> {
    private Function<String , Void> func;
    private String urlSt = null;

    //コンストラクタ
    public HttpResponseAsync(){}
    public HttpResponseAsync(String urlSt){
        this.urlSt = urlSt;
    }
    //メソッド
    public void setUrlSt( String urlSt){
        this.urlSt = urlSt;
    }

    @Override
    protected void onPreExecute(){ }

    //接続のメソッド
    @Override
    protected String doInBackground( Void... params){
        HttpURLConnection con = null;
        URL url = null;
        String readSt = null;
        try{
            url = new URL(urlSt);
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setInstanceFollowRedirects(true);//リダイレクトする
            con.setDoInput(true);//リターンされた値を使う
            con.setDoOutput(true);
            con.connect();
            InputStream in = con.getInputStream();

            readSt = readInputStream( in );

            byte bodyByte[] = new byte[1024];
            in.read( bodyByte );
            in.close();


        }catch(Exception e){
            Log.d("error",e.toString() );
        }

        return readSt;
    }

    @Override
    protected void onPostExecute( String result ){

        super.onPostExecute( result );
        func.apply((String) result);
        //Log.d( "onPostExecute", result );
    }

    //ファンクションの設定
    public void setFunction( Function<String,Void> func ){
        this.func = func;
    }

    //ByteをStringに変換
    public String readInputStream( InputStream in) throws IOException, UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        String st = "";

        BufferedReader br = new BufferedReader( new InputStreamReader(in,"UTF-8"));
        while((st=br.readLine()) != null){ sb.append( st);}
        try{in.close();}catch(Exception e){e.printStackTrace();}
        return sb.toString();
    }
}