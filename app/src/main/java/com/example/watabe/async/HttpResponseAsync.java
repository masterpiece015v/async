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
        StringBuilder sb = new StringBuilder();
        InputStream in = null;
        HttpURLConnection con = null;
        String readSt = null;
        try{
            URL url = new URL(urlSt);
            //Log.d("doInBackground","beforeConnection");
            con = (HttpURLConnection)url.openConnection();
            //Log.d("doInBackground","afterConnection");
            con.setConnectTimeout( 3000 );
            con.setReadTimeout( 3000 );
            con.setRequestMethod("POST");

            con.connect();
            int resCode = con.getResponseCode();
            if( resCode != HttpURLConnection.HTTP_OK){
                throw new IOException("HTTP responseCode:"  + resCode );
            }


            //con.setInstanceFollowRedirects(true);//リダイレクトする
            con.setDoInput(true);//リターンされた値を使う
            con.setDoOutput(true);
            //Log.d("doInBackground","connect start");

            //Log.d("doInBackground","connect end");
            in = con.getInputStream();
            //Log.d( "doInBAckground", "before readInputStream");
            readSt = readInputStream( in );

            byte bodyByte[] = new byte[1024];
            in.read( bodyByte );
            in.close();

        }catch(Exception e){

            Log.d("error",e.toString() );
            //Log.d( "error" , con.getErrorStream().toString() );
        }finally{
            if(con != null ){
                con.disconnect();
            }
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