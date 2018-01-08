package com.borisruzanov.retrofitexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private TextView output;
    private Button translateBtn;
    private final String URL = "https://translate.yandex.ru";
    private final String KEY = "trnsl.1.1.20180106T043221Z.6c34575af2d7a4c2.7bdd7b5b4cce4b83df95d9e50e8e3a92662d1e0e";


    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL)
            .build();

    private Link intf = retrofit.create(Link.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.input);
        output = (TextView) findViewById(R.id.output);
        translateBtn = (Button) findViewById(R.id.translate_button);

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> mapJson = new HashMap<String, String>();
                mapJson.put("key", KEY);
                mapJson.put("text", input.getText().toString());
                mapJson.put("lang", "en-ru");

                Call<Object> call = intf.translate(mapJson);

                try {
                    Response<Object> response = call.execute();

                    //Convert from JSON to Map
                    Map <String, String> map = gson.fromJson(response.body().toString(),Map.class);

                    //Getting needed data and insert it in needed field
                    for (Map.Entry e : map.entrySet()){
                        if(e.getKey().equals("text")){
                            output.setText(e.getValue().toString());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
