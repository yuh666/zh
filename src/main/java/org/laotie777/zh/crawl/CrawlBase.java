package org.laotie777.zh.crawl;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import utils.CharsetUtil;

import java.io.*;
import java.net.URI;
import java.util.*;

/**
 * @Author yuh
 * @Date Created in 上午10:05 2018/2/10
 * @Description 爬虫基类 定义Http基本连接信息
 */
public class CrawlBase {

    private static Logger logger = Logger.getLogger(CrawlBase.class);

    /**
     * 链接源代码
     */
    private String pageSourceCode = "";
    /**
     * 响应头信息
     */
    private Header[] responseHeaders = null;
    /**
     * 连接超时时间
     */
    private static int connectTimeout = 3500;
    /**
     * 连接获取时间
     */
    private static int readTimeout = 3500;
    /**
     * 最大访问次数
     */
    private static int maxConnectTimes = 3;
    /**
     * 网页默认编码方式
     */
    private static String ISO_8859_1 = "iso-8859-1";
    /**
     * 连接池连接数
     */
    private static int MAX_TOTAL = 10;
    /**
     * httpClient HTTP连接池
     */

    private static PoolingHttpClientConnectionManager cm;


    static {
        /**
         *为连接添加http(s)协议
         */
        PlainConnectionSocketFactory socketFactory = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory ssl = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", socketFactory)
                .register("https", ssl)
                .build();
        cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(MAX_TOTAL);
        cm.setDefaultMaxPerRoute(cm.getMaxTotal());
    }

    /**
     * 获取一个连接
     *
     * @return
     */
    public HttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    /**
     * 连接配置
     *
     * @return
     */
    public RequestConfig getRequestConfig() {
        return RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(readTimeout).build();
    }

    /**
     * 创建GET请求
     *
     * @param url
     * @param charset
     * @param params
     * @param headers
     * @return
     */
    private HttpGet createGetMethod(String url, String charset, Map<String, String> params, Map<String, String> headers) {

        HttpGet httpGet = new HttpGet(url);
        if (!Objects.isNull(params) && params.size() != 0) {
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            List<NameValuePair> pairs = new ArrayList<>();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            try {
                String paramsString = EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
                StringBuilder sb = new StringBuilder(url);
                sb.append(url.indexOf("?") > 0 ? "&" : "?").append(paramsString);
                httpGet.setURI(new URI(sb.toString()));
            } catch (Exception e) {
                logger.error("get请求编码失败 => {}", e);
            }
        }

        if (!Objects.isNull(headers) && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return httpGet;
    }

    /**
     * 创建POST请求
     *
     * @param url
     * @param charset
     * @param params
     * @param headers
     * @return
     */
    private HttpPost createPostMethod(String url, String charset, Map<String, String> params, Map<String, String> headers) {

        HttpPost httpPost = new HttpPost(url);
        if (!Objects.isNull(params) && params.size() != 0) {
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            List<NameValuePair> pairs = new ArrayList<>();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
            } catch (Exception e) {
                logger.error("post请求编码失败 => {}", e);
            }
        }

        if (!Objects.isNull(headers) && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return httpPost;
    }

    /**
     * 执行http请求
     *
     * @param httpRequest
     * @return
     */
    public boolean execute(HttpUriRequest httpRequest) {

        int n = maxConnectTimes;
        while (n > 0) {
            try {
                HttpClient httpClient = getHttpClient();
                HttpResponse response = httpClient.execute(httpRequest);
                responseHeaders = response.getAllHeaders();
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                //抓取数据并且以ISO8859_1编码
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content, ISO_8859_1));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                pageSourceCode = sb.toString();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pageSourceCode.getBytes(ISO_8859_1));
                String streamCharset = CharsetUtil.getStreamCharset(byteArrayInputStream, ISO_8859_1);
                pageSourceCode = new String(pageSourceCode.getBytes(ISO_8859_1), streamCharset);
                byteArrayInputStream.close();
                content.close();
                return true;
            } catch (Exception e) {
                logger.error("请求失败 => {}", e);
                n--;
            }
        }
        return false;
    }

}
