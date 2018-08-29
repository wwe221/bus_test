package com.something.bus_test;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;
public class MainActivity extends AppCompatActivity {
    Document Doc;
    ListView listview ;
    ListViewAdapter adapter;
    String s = "";
    String bus;
    String a = "7016";
    String b= "7018";
    String c= "1711";
    String d= "1020";
    String f ="7212";
    String A[] = {a,b,c,d,f};
    String Bus[] ={"1","2","3","4","5"};
    String texts[] = new String[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview1);
        adapter = (ListViewAdapter)new ListViewAdapter() ;
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;
                String titleStr = item.getTitle() ;
                String descStr = item.getDesc() ;
                // TODO : use item data.
            }
        }) ;
        new GetXMLTask().execute();
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private class GetXMLTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {

                String q="100000180";
                // 100000180 상명대 정문
                // 100000040 상명대 입구 7018
                // 100000189 상명대 입구
                // 100000021 경복궁역
                url = new URL("http://ws.bus.go.kr/api/rest/arrive/getLowArrInfoByStId?ServiceKey=ZUpYRcYG5aEM7ugMSQoNmjtAc0leLgZoJJqH2Z5feXJGJkYa1EGV3Ag6evY6QU%2FKczL6URU8QkG7lO%2FO%2FAkZCA%3D%3D&stId=100000023");
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Doc = db.parse(new InputSource(url.openStream()));
                Doc.getDocumentElement().normalize();
            } catch (Exception e) {
                Log.i("try","wrong");
            }
            return Doc;
        }
        @Override
        protected void onPostExecute(Document doc) {
            Log.i("Root element :",doc.getDocumentElement().getNodeName());// root tag
            NodeList nodeList = doc.getElementsByTagName("itemList");
            Log.i("파싱할 리스트 수 : ",nodeList.getLength()+"개");
            for(int i = 0; i< nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                Element eElement = (Element) node;
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    bus=getTagValue("rtNm", eElement);
                    for(int ii=0;ii<5;ii++)
                    if(bus.equals(A[ii])) {
                        Bus[ii]=bus;
                        s = "이번: " + getTagValue("arrmsg1", eElement) + "     ";
                        s += "탑승: " + getTagValue("reride_Num1", eElement) + "명" + "\n";
                        s += "다음: " + getTagValue("arrmsg2", eElement) + "     ";
                        s += "탑승: " + getTagValue("reride_Num2", eElement) + "명" + "\n";
                        texts[ii]=s;
                    }
                }
            }
            super.onPostExecute(doc);
            print();
        }
    }
    public void print(){
        for(int i=0;i<5;i++){
            adapter.addItem(Bus[i],texts[i]);
        }
    }
    private static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null)
            return null;
        return nValue.getNodeValue();
    }
}
