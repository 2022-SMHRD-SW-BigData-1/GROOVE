package com.example.groove.activity;

import static com.example.groove.activity.Login.my_url;
import static com.example.groove.activity.MainActivity.user_seq;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;
import com.example.groove.adapter.Fav_Songs_Selection_Adapter;
import com.example.groove.adapter.Main_Home_RecyclerView_Adapter;
import com.example.groove.adapter.Search_Adapter;
import com.example.groove.data.Main_Item;
import com.example.groove.data.RecyclerItemClickListener;
import com.example.groove.data.View_Type_Code;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchList extends AppCompatActivity {

    private ListView mSearchList;
    TextView tv_pl_title;
    AppCompatImageButton btn_pre;
    private ArrayList<Main_Item> mMainItemList;
    private Fav_Songs_Selection_Adapter mSearchListAdapter;
    GridLayoutManager gridLayoutManager;
    RequestQueue requestQueue;
    String item_song_id;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likelist);

        tv_pl_title = findViewById(R.id.tv_pl_title);
        mSearchList = findViewById(R.id.like_menuList);
        btn_pre = findViewById(R.id.btn_pre1);
        tv_pl_title.setText("?????? ??????");

        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        intent = getIntent();
        String search_content = intent.getStringExtra("search_content");
        Log.d("?????????",search_content);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        String url = "http://"+my_url+":3001/SearchList";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("??????2", response);

                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray song_id = json.getJSONArray("song_id");
                            JSONArray song_title = json.getJSONArray("song_title");
                            JSONArray artist_id = json.getJSONArray("artist_id");
                            JSONArray album_id = json.getJSONArray("album_id");

                            // ?????? ??? ??????????????????
                            mMainItemList = new ArrayList<>();
                            for(int i=0;i<song_id.length();i++){
                                mMainItemList.add(new Main_Item(song_title.getString(i), artist_id.getString(i), getResources().getIdentifier("album_"+ album_id.getInt(i), "drawable", getApplicationContext().getPackageName())));
                            }
                            Log.d("??????????????????", String.valueOf(mMainItemList.get(0).getArtistName()));
                            mSearchListAdapter = new Fav_Songs_Selection_Adapter(getApplicationContext().getApplicationContext(), R.layout.item_favsong, mMainItemList);
                            mSearchList.setAdapter(mSearchListAdapter);
                            mSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Log.d("????????????", String.valueOf(adapterView.getAdapter().getItem(i)));
                                    Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                                    try {
                                        intent.putExtra("song_id",song_id.getString(i));
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    startActivity(intent);

                                }
                            });



                        } catch (JSONException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("??????", "??????");
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
                params.put("user_seq", user_seq);
                params.put("search_content", search_content);
                // key-value ??? ???????????? params ????????? ??????!
                return params;
            }
        };

        requestQueue.add(request);


    }
}