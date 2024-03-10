package com.example.bai07_sitemap.service;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.bai07_sitemap.MainActivity;
import com.example.bai07_sitemap.database.MyDB;
import com.example.bai07_sitemap.interfaces.SiteMapService;
import com.example.bai07_sitemap.model.SiteMap;
import com.example.bai07_sitemap.utils.ConvertData;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.net.URL;

public class SiteMapServiceImpl extends AsyncTask implements SiteMapService {
    private Context context;
    private Dialog dialog;
    public SiteMapServiceImpl(Context context, Dialog dialog1) {
        this.context = context;
        this.dialog = dialog1;
    }
    @Override
    public boolean siteMapParser() {
            try {
                //===   Khai báo 1 chuỗi url và khởi tạo đối tượng url  ====
                String urlStr = "https://timoday.edu.vn/post-sitemap.xml";
                URL url = new URL(urlStr);

                //===   Tạo một instance của DocumentBuilderFactory và sử dụng nó để tạo một DocumentBuilder ===
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

                //===   Phân tích cứ pháp từ XML từ URL đã cho và tạo 1 document mới    ===
                Document doc = dBuilder.parse(url.openStream());

                //===   Chuẩn hóa xml và loại bỏ khoảng trắng thừa  ===
                doc.getDocumentElement().normalize();

                //===   Lấy tất cả các phần tử có tên tag là url    ===
                NodeList nList = doc.getElementsByTagName("url");

                MyDB db = new MyDB(context);
                db.deleteData();
                //===   Lặp qua tất cả các phần tử trong nodelist   ===
                for (int i = 0; i < nList.getLength(); i++) {

                    //===   Lấy phần tử hiện tại từ nodelist và ép kiểu thành element   ===
                    Element eElement = (Element) nList.item(i);

                    //===   in ra nội dung phần tử con  ===
                    SiteMap siteMap = new SiteMap();

                    String strUrl = eElement.getElementsByTagName("loc").item(0).getTextContent();

                    String lastChange =  ConvertData.convertTimeFormat(eElement.getElementsByTagName("lastmod").item(0).getTextContent());

                    Float prority = ConvertData.convertPrority(eElement.getElementsByTagName("priority").item(0).getTextContent());

                    String cFreg =  eElement.getElementsByTagName("changefreq").item(0).getTextContent();

                    byte[] img = null;
                    //===   Tương tự với các phần tử image làm giống với url    ===
                    NodeList imageList = eElement.getElementsByTagName("image:image");
                    for (int j = 0; j < imageList.getLength(); j++) {
                        Element imageElement = (Element) imageList.item(j);
                        img = ConvertData.convertImg(imageElement.getElementsByTagName("image:loc").item(0).getTextContent());
                        if(img==null)
                            continue;
                        break;
                    }
                    if(img==null)
                        img = new byte[0];
                    db.insertData(strUrl, img, prority, cFreg, lastChange);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

             dialog.dismiss();
             return true;

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return siteMapParser();
    }
}
