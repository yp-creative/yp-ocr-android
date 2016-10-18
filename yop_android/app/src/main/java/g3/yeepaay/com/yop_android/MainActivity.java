package g3.yeepaay.com.yop_android;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.yeepay.yop.library.client.Yopclient;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Button btn_upload;
    Button btn_query;
    TextView tv_response;
    Map map=new TreeMap();
    String result=null;
    private static String IMG_PATH = getSDPath() + java.io.File.separator
            + "ocrtest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_upload=(Button) findViewById(R.id.btn_upload);
        btn_query=(Button)findViewById(R.id.btn_query);
        tv_response=(TextView)findViewById(R.id.response_data);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Yopclient.setAppKey("B112345678901237");
                Yopclient.setSecretKey("nUXQx0Mt0aSKvR0uNOp6kg==");
                Yopclient.setServerUrl("http://10.151.30.88:8064/yop-center");
                Yopclient.setSignRet(true);
                Yopclient.setSignName("SHA1");
                Yopclient.setEncrypt(true);
                Yopclient.setPostUrl("/rest/v1.0/file/upload");
                map.put("fileType", "IMAGE");
                map.put("_file", IMG_PATH+"/temp_cropped.jpg");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        result=Yopclient.uploadFile("/rest/v1.0/file/upload",map);
                    }
                }).start();
            }
        });
//        if(result!=null){
//            tv_response.setText("111111");
//        }
        class myButtonLisener implements View.OnClickListener{
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_query:
                        break;
                    case R.id.btn_upload:
                        break;
                }
            }
        }
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// ��ȡ���Ŀ¼
        }
        return sdDir.toString();
    }

}
