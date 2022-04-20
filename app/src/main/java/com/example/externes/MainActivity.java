package com.example.externes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<ItemModel> arrayList=new ArrayList<>();
            try {
                JSONObject object=new JSONObject(readJSON());
                JSONArray array=object.getJSONArray("contacts");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject=array.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String first_name = jsonObject.getString("first_name");
                    String last_name = jsonObject.getString("last_name");
                    String job = jsonObject.getString("job");
                    String email = jsonObject.getString("email");
                    String phone = jsonObject.getString("phone");

                    final ItemModel model=new ItemModel();
                    model.setId(id);
                    model.setName(first_name + " " + last_name);
                    model.setJob(job);
                    model.setEmail(email);
                    model.setPhone(phone);

                    arrayList.add(model);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            CustomAdapter customAdapter=new CustomAdapter(this, arrayList);
        ListView listView=findViewById(R.id.listView);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();
            }
        });
    }

    public String readJSON() {
        String json=null;
        try {
            InputStream inputStream=getAssets().open("data.json");
            int size=inputStream.available();
            byte[] buffer=new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json=new String(buffer,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public class CustomAdapter extends BaseAdapter{

        Context context;
        ArrayList<ItemModel> arrayList;

        public CustomAdapter(Context context, ArrayList<ItemModel> arrayList){
            this.context=context;
            this.arrayList=arrayList;
        }
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.list_item,
                        parent, false);
            }
            TextView name, job, email,phone;
            name=convertView.findViewById(R.id.txt_name);
            job =  convertView.findViewById(R.id.txt_job);
            email =  convertView.findViewById(R.id.txt_email);
            phone =  convertView.findViewById(R.id.txt_phone);
            Button callbtn =  convertView.findViewById(R.id.btn_call);

            name.setText(arrayList.get(position).getName());
            job.setText(arrayList.get(position).getJob());
            email.setText(arrayList.get(position).getEmail());
            phone.setText(arrayList.get(position).getPhone());

            final ItemModel model=arrayList.get(position);
            callbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Uri tel=Uri.parse("tel : "+ model.getPhone()); --> that one didn't work for me
                    Intent activity2=new Intent(Intent.ACTION_DIAL);
                    activity2.setData(Uri.parse("tel:"+model.getPhone()));// this is the true ways
                    startActivity(activity2);

                }
            });

            return convertView;
        }
    }

}