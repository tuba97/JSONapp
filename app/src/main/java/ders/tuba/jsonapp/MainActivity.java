package ders.yasin.jsonapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ListView lvCompanies;
    ImageView ivLogo;
    String [] companyInfo;
    String [] imageURLs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvCompanies=findViewById(R.id.lv_Companies);
        ivLogo=findViewById(R.id.iv_Logo);
        lvCompanies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Picasso.get().load(imageURLs[position]).into(ivLogo);

            }
        });
        String URL="https://web.karabuk.edu.tr/yasinortakci/dokumanlar/web_dokumanlari/recyle.json";
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray companies=jsonObject.getJSONArray("Companies");
                            int length=companies.length();
                            companyInfo=new String[length];
                            imageURLs=new String[length];
                            for(int i=0;i<length;i++){
                                JSONObject company=companies.getJSONObject(i);
                                String heading=company.getString("Heading");
                                String detail=company.getString("Detail");
                                companyInfo[i]=heading+" - "+detail;
                                imageURLs[i]=company.getString("ImageURL");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,companyInfo);
                        lvCompanies.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("Error",error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
}