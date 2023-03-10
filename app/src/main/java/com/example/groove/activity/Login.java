package com.example.groove.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Login extends AppCompatActivity {
    public static String user_seq;
    public static String my_url = "sash0604.iptime.org";
    EditText login_id, login_pw;
    AppCompatButton btn_joinform, btn_login;
    RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);
        btn_joinform = findViewById(R.id.btn_joinform);
        btn_login = findViewById(R.id.btn_login);


        btn_joinform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Join.class);

                startActivity(intent);
                finish();
            }
        });

        // Node.js??? ??????
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputId = login_id.getText().toString();
                String inputPw = login_pw.getText().toString();

                String url = "http://"+my_url+":3001/Login";

                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("??????", response);



                                try {
                                    JSONObject json = new JSONObject(response);

                                    String result = json.getString("result");
                                    String nick = json.getString("userNick");
                                    String favart = json.getString("favArt");
                                    String favsong = json.getString("favSong");
                                    String user_seq = json.getString("user_seq");

                                    Log.d("???????????????", result);
                                    Log.d("?????????", nick);
                                    Log.d("?????? ????????????", favart);
                                    Log.d("?????? ???", String.valueOf(favsong.length()));


                                    // ????????? ???????????? ??? ?????? ??????????????? null?????? ?????? ??????????????? ????????? ????????? ?????? ?????????
                                    if(result.equals("????????? ??????")){
                                        Intent intent;
                                        if(favart.equals("null") || favsong.equals("null") || favart.equals("") || favsong.equals("")){
                                            Log.d("?????????????",favart);
                                            intent = new Intent(getApplicationContext(), Fav_Artist_Selection.class);
                                            intent.putExtra("user_seq", user_seq);
                                        } else{
                                            intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("nick", nick);
                                            intent.putExtra("favart", favart);
                                            intent.putExtra("favsong", favsong);
                                            intent.putExtra("user_seq", user_seq);
                                        }
                                        startActivity(intent);
                                        finish();
                                    } else{
                                        Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("??????", String.valueOf(error));
                            }
                        }
                ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        // getParams --> post ???????????? ???????????? ?????? ??? ???????????? ?????????!
                        // ???????????? key - value ????????? ???????????? ??????????????????
                        Map<String,String> params = new HashMap<String,String>();
                        // params -> key-value ????????? ????????????
                        params.put("id", inputId);
                        params.put("pw", inputPw);

                        // key-value ??? ???????????? params ????????? ??????!
                        return params;
                    }
                };

                requestQueue.add(request);
            }
        });

    }
}