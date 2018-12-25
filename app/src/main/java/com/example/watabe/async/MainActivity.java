package com.example.watabe.async;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.watabe.async.HttpResponseAsync;
import com.example.watabe.async.R;

import java.util.function.Function;

public class MainActivity extends AppCompatActivity {
    TextView txtSample;
    EditText edtUrl;
    EditText edtTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.txtSample = (TextView)findViewById(R.id.txtSample );
        this.edtUrl = (EditText)findViewById(R.id.edtUrl);
        this.edtTable = (EditText)findViewById(R.id.edtTable);
        this.edtUrl.setText("http://10.0.2.2:8000/polls/gettable/");
        this.edtTable.setText("table_name=student");
    }

    //
    public void btnOnClick(View view){
        HttpResponseAsync async = new HttpResponseAsync(
                edtUrl.getText().toString(),
                edtTable.getText().toString());

        async.setFunction(new Function<String,Void>(){
            @Override
            public Void apply(String s) {
                txtSample.setText( s );
                return null;
            }
        });

        async.execute();
    }

}
