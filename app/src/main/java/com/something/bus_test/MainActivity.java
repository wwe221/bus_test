package com.something.bus_test;

import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

public class MainActivity extends AppCompatActivity {
    Document Doc;
    TextView b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        b= (TextView)findViewById(R.id.text1);
        new GetXMLTask().execute();
    }
    private class GetXMLTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... urls) {
            URL url;

            try {
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
            String s = "";
            Log.i("Root element :",doc.getDocumentElement().getNodeName());// root tag
            NodeList nodeList = doc.getElementsByTagName("itemList");
            Log.i("파싱할 리스트 수 : ",nodeList.getLength()+"개");
            for(int i = 0; i< nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                Element eElement = (Element) node;
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    s += getTagValue("rtNm", eElement)+"\n";
                    s+="이번: " + getTagValue("arrmsg1", eElement)+"\n";
                    s+="탑승: " + getTagValue("reride_Num1", eElement) + "명"+"\n";
                    s+="다음: " + getTagValue("arrmsg2", eElement)+"\n";
                    s+="탑승: " + getTagValue("reride_Num2", eElement) + "명"+"\n";
                }
            }
            b.setText(s);

            super.onPostExecute(doc);
        }
    }
    public static String getsomething(){
        String numb;
        String str="";
        String a = "7016";
        String b= "7018";
        String c= "1711";
        String d= "1020";
        String f ="7212";
        String A[] = {a,b,c,d,f};


        return str;
    }
    private static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null)
            return null;
        return nValue.getNodeValue();
    }
}
