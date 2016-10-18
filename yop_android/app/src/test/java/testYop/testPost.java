package testYop;


import com.yeepay.yop.library.client.Yopclient;
import com.yeepay.yop.library.encrypt.Base64;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



/**
 * title: <br/>
 * description:描述<br/>
 * Copyright: Copyright (c)2016<br/>
 * Company: 易宝支付(YeePay)<br/>
 *
 * @author guoliang.li
 * @version 1.0.0
 * @since 16/9/22 下午3:54
 */
public class testPost {
    @Test
    public void testPostData() {
        Map<String, String> map = new HashMap<String, String>();
//        Yopclient.setAppKey("B112345678901237");
        Yopclient.setCustomerNo("10040011444");
        Yopclient.setSecretKey("8intulgnqibv77f1t8q9j0hhlkiy6ei6c82sknv63vib3zhgyzl8uif9ky7a");
        Yopclient.setServerUrl("http://10.151.30.87:8064/yop-center");
        Yopclient.setSignRet(true);
        Yopclient.setSignName("SHA1");
        Yopclient.setEncrypt(true);
        Yopclient.setPostUrl("/rest/v1.0/file/upload");
        map.put("fileType", "IMAGE");
        map.put("_file", "/Users/yp-tc-m-2645/Desktop/a.png");
        String result = Yopclient.uploadFile("/rest/v1.0/file/upload", map);
        System.out.println(result);
    }

    @Test
    public void testPostData2() {
        Map<String, String> map = new HashMap<String, String>();
        Yopclient.setCustomerNo("10040011444");
        Yopclient.setSecretKey("8intulgnqibv77f1t8q9j0hhlkiy6ei6c82sknv63vib3zhgyzl8uif9ky7a");
        Yopclient.setServerUrl("http://10.151.30.88:8064/yop-center");
        Yopclient.setSignRet(true);
        Yopclient.setSignName("SHA1");
        Yopclient.setEncrypt(true);
        map.put("requestId", "124");
        map.put("platformUserNo", "8880222");
        Yopclient.setPostUrl("/rest/v1.0/member/queryAccount");
        String result = Yopclient.postData("/rest/v1.0/member/queryAccount", map);
        System.out.println(result);
    }

    @Test
    public void testIdCardAuth() throws Exception {
//        YopRequest request = new YopRequest(null, "cGB2CeC3YmwSWGoVz0kAvQ==", "http://172.17.102.177:7777/yop-center/");
        Map map = new HashMap();
        Yopclient.setAppKey("yop-boss");
        Yopclient.setSecretKey("cGB2CeC3YmwSWGoVz0kAvQ==");
        Yopclient.setServerUrl("http://172.17.102.173:8064/yop-center");
        Yopclient.setPostUrl("/rest/v1.1/ocr/idcardauth");
        Yopclient.setSignRet(true);
        Yopclient.setEncrypt(false);
        Yopclient.setSignName("SHA-256");
        map.put("requestFlowId", "test123456");
        map.put("name", "张泳秋");
        map.put("idCardNumber", "441701198911130484");
        map.put("requestSystem", "FT");
        map.put("fileType", "IMAGE");
//        request.addParam("testDate", "2012-10-09 12:13:14");
//        InputStream sourceFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("/Users/yp-tc-m-2645/Desktop/a.png");
//        Object[] os = new Object[1];
//        os[0] = IOUtils.toByteArray(sourceFile);
//        map.put("headPortraitBase64", imageToBytes("/Users/yp-tc-m-2645/Desktop/a.png"));
//        map.put("headPortraitBase64", Base64.encode(imageToBytes("/Users/yp-tc-m-2645/Desktop/a.png")));

        map.put("headPortraitBase64", Base64.encode(new String(imageToBytes("/Users/yp-tc-m-2645/Desktop/a.png"))));
        String result = Yopclient.postData("/rest/v1.1/ocr/idcardauth", map);
        System.out.println(result);
    }
    static byte[] imageToBytes(String imgSrc) throws Exception
    {
        FileInputStream fin = new FileInputStream(new File(imgSrc));
        //可能溢出,简单起见就不考虑太多,如果太大就要另外想办法，比如一次传入固定长度byte[]
        byte[] bytes  = new byte[fin.available()];
        //将文件内容写入字节数组，提供测试的case
        fin.read(bytes);
        fin.close();
        return bytes;
    }
}

