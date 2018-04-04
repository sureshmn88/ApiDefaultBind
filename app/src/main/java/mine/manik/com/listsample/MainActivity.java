package mine.manik.com.listsample;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ProductAdapter mProductAdapter;
    ArrayList<Product> mList;

    Button btn1,btn2,btn3;
    JSONArray productArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView= (RecyclerView) findViewById(R.id.product_recyclerview);
        btn1= (Button) findViewById(R.id.btn_1);
        btn2= (Button) findViewById(R.id.btn_2);
        btn3= (Button) findViewById(R.id.btn_3);

        //fetchDetails();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productArray != null) {
                    spiltDetails(0);
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productArray != null) {
                    spiltDetails(1);
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productArray != null) {
                    spiltDetails(2);
                }
            }
        });

        new SendPostRequest().execute();
    }

    void fetchDetails(String result) {

        try {

            //Web Service Data
            //JSONObject jsonObject=new JSONObject(result);

            // Sample Data
            JSONObject jsonObject=new JSONObject(SampleJSON.ProductJson);
            productArray=jsonObject.getJSONArray("ProductDetails");
            spiltDetails(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void spiltDetails(int position) {

        try {

            JSONObject jsonObject=productArray.getJSONObject(position);
            JSONArray jArray=jsonObject.getJSONArray("items");

            mList=new ArrayList<>();
            for(int i=0;i<jArray.length();i++) {

                JSONObject jObj=jArray.getJSONObject(i);
                Product item=new Product();
                item.setId(jObj.getString("id"));
                item.setName(jObj.getString("name"));
                item.setCategory(jObj.getString("category"));
                item.setPrice(jObj.getString("price"));
                mList.add(item);

            }

            prepareDetails();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void prepareDetails() {

        if(mList.size()>0) {

            if (mProductAdapter == null) {

                mProductAdapter = new ProductAdapter(this, mList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                mRecyclerView.setAdapter(mProductAdapter);
                mProductAdapter.notifyDataSetChanged();

                mProductAdapter.setOnClickListener(new ProductAdapter.OnClickListener() {
                    @Override
                    public void onLayoutClick(int position) {

                    }
                });
            } else {
                mProductAdapter.setProductList(mList);
                mProductAdapter.notifyDataSetChanged();
            }
        }

    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://studytutorial.in/post.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name", "abc");
                postDataParams.put("email", "abc@gmail.com");
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            /*Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();*/

            Toast.makeText(getApplicationContext(), "Data Return Successfully",
                    Toast.LENGTH_LONG).show();

            fetchDetails(result);

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
