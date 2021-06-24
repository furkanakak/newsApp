package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Adapter adapter;

    ArrayList<model> modelList;

    ArrayList<String> titles;
    ArrayList<String> links;
    ArrayList<String> descriptions;
    ArrayList<String> pubDates;
    ArrayList<String> images_urls;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Search");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        titles = new ArrayList<>();
        links = new ArrayList<>();
        descriptions = new ArrayList<>();
        pubDates = new ArrayList<>();
        images_urls = new ArrayList<>();
        modelList = new ArrayList<>();

        new ProcessInBackground().execute();
    }

    public InputStream getInputStream(URL url)
    {
        try
        {
            return url.openConnection().getInputStream();

        }
        catch (IOException e)
        {
            return null;
        }
    }
    public class ProcessInBackground extends AsyncTask<Integer,Integer,Exception>
    {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                progressDialog.setMessage("Buys loading rss feed...please wait...");
                progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... integers) {
            try
            {

                URL url=new URL("https://www.aa.com.tr/tr/rss");
                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp=factory.newPullParser();
                xpp.setInput(getInputStream(url),"UTF_8");
                boolean insideItem=false;
                int eventType= xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT){
                    if (eventType==XmlPullParser.START_TAG){
                        if (xpp.getName().equalsIgnoreCase("item")){

                            insideItem=true;

                        }else if (xpp.getName().equalsIgnoreCase("title")){

                            if(insideItem){

                                titles.add(xpp.nextText());
                            }

                        }else if (xpp.getName().equalsIgnoreCase("link")){
                            if (insideItem){




                                links.add(xpp.nextText());
                            }

                        }else if(xpp.getName().equalsIgnoreCase("pubDate")){
                            if (insideItem){

                                pubDates.add(dateEdit(xpp.nextText()));
                            }
                        }else if(xpp.getName().equalsIgnoreCase("image")){
                            if (insideItem){

                                images_urls.add(xpp.nextText());
                            }
                        }




                    }else if (eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){

                        insideItem=false;
                    }
                    eventType=xpp.next();
                }
            }
            catch (MalformedURLException e)
            {
                exception = e;
            }
            catch (XmlPullParserException e)
            {
                exception = e;
            }
            catch (IOException e)
            {
                exception = e;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);

            for (int i=0;i<titles.size();i++){


                String source=links.get(i).substring(12,14).toUpperCase();
                model mmodel=new model(titles.get(i),links.get(i),pubDates.get(i),images_urls.get(i));
                modelList.add(mmodel);
            }


            adapter = new Adapter(MainActivity.this,modelList);
            recyclerView.setAdapter(adapter);
            //adaptöre oluşturma
            // adaptör set etme
            progressDialog.dismiss();

        }
    }
    public String dateEdit(String date)  {

        SimpleDateFormat input = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

        Date date1= null;
        try {
            date1 = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output.format(date1);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar,menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        System.out.println("gonderilen arama sonucu  " + query);


        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        System.out.println("anlik gonderilen arama sonucu  " + newText);
        adapter.getFilter().filter(newText);
        return false;
    }
}