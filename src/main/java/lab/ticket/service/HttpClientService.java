/*
 * 12306-hunter: Java Swing C/S版本12306订票助手
 * 本程序完全开放源代码，仅作为技术学习交流之用，不得用于任何商业用途
 */
package lab.ticket.service;

import com.alibaba.fastjson.JSONObject;
import lab.ticket.TicketMainFrame;
import lab.ticket.model.PassengerData;
import lab.ticket.model.SingleTrainOrderVO;
import lab.ticket.model.TicketData;
import lab.ticket.model.TrainQuery;
import lab.ticket.model.UserData;
import lab.ticket.util.TicketUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.alibaba.fastjson.JSON.parseObject;

public class HttpClientService {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientService.class);

    // 获取登录提交的随机数
//	public static final String POST_UTL_LOGINACTION_LOGINAYSNSUGGEST = "https://dynamic.12306.cn/otsweb/loginAction.do?method=loginAysnSuggest";
    public static final String POST_UTL_LOGINACTION_LOGINAYSNSUGGEST = "https://dynamic.12306.cn/otsweb/loginAction.do?method=loginAysnSuggest";
    // 登录
//	public static final String POST_UTL_LOGINACTION = "https://dynamic.12306.cn/otsweb/loginAction.do?method=login";
    public static final String POST_UTL_LOGINACTION = "https://kyfw.12306.cn/otn/login/loginAysnSuggest";
    public static final String GET_UTL_INITMY12303 = "https://kyfw.12306.cn/otn/index/initMy12306";
    //检测验证码
    public static final String CHECKRANDCODEANSYN = "https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn";

    //获取验证码
    public static final String CODE_IMAGE = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand&0.24729695293402476";

    //订单验证码
    public static final String CODE_IMAGE_ORDER = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=passenger&rand=randp&0.31897424832791277";

    // 获取TOKEN URL
    public static final String GET_URL_USERTOKEN = "https://dynamic.12306.cn/otsweb/order/querySingleAction.do?method=init";
    // 查询余票URL
    //public static final String GET_URL_QUERYTICKET_QT = "https://dynamic.12306.cn/otsweb/order/querySingleAction.do?method=qt";
//	public static final String GET_URL_QUERYTICKET = "https://dynamic.12306.cn/otsweb/order/querySingleAction.do?method=queryLeftTicket";
    //这个地址一直在变 queryA queryZ
    public static final String GET_URL_QUERYTICKET = "https://kyfw.12306.cn/otn/leftTicket/queryZ";


    public static final String GET_URL_LEFTTICKET_INIT = "https://kyfw.12306.cn/otn/leftTicket/init";


    //查询URL
    public static final String GET_URL_QUERY = "https://dynamic.12306.cn/otsweb/order/querySingleAction.do?method=queryLeftTicket";
    // 提交火车车次信息 提交订单
//	public static final String POST_URL_SUBMUTORDERREQUEST = "https://dynamic.12306.cn/otsweb/order/querySingleAction.do?method=submutOrderRequest";
    public static final String POST_URL_SUBMUTORDERREQUEST = "https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
    public static final String GET_URL_LOG = "https://kyfw.12306.cn/otn/leftTicket/log";

    //验证登录，
    public static final String POST_URL_CHECKUSER = "https://kyfw.12306.cn/otn/login/checkUser";
    //预提交订单，
    public static final String POST_URL_SUBMITORDERREQUEST = "https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";
    //模拟跳转页面InitDc，
    public static final String POST_URL_INITDC = "https://kyfw.12306.cn/otn/confirmPassenger/initDc";
    //常用联系人确定，
    public static final String POST_URL_GETPASSENGERDTOS = "https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";

    // 获取验证码(登录)
    public static final String GET_LOGINURL_PASSCODE = "https://dynamic.12306.cn/otsweb/passCodeNewAction.do?module=login&rand=sjrand";
    // 获取验证码(提交订单)
    public static final String GET_SUBMITURL_PASSCODE = "https://dynamic.12306.cn/otsweb/passCodeNewAction.do?module=passenger&rand=randp";
    // 获取联系人(解析html能获得)和提交令牌
    public static final String GET_URL_CONFIRMPASSENGER = "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=init";
    // 获取火车票数量
//	public static final String GET_URL_GETQUEUECOUNT = "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=getQueueCount";
    public static final String GET_URL_GETQUEUECOUNT = "https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount";
    // 检查订单URL
//	public static final String POST_URL_CHECKORDERINFO = "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=checkOrderInfo&rand=";
    public static final String POST_URL_CHECKORDERINFO = "https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
    // 提交订单信息
//	public static final String POST_URL_CONFIRMSINGLEFORQUEUE = "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=confirmSingleForQueue";
    public static final String POST_URL_CONFIRMSINGLEFORQUEUE = "https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue";
    //快成功了！每隔4秒循环Post
    public static final String POST_URL_queryOrderWaitTime = "https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime";


    private static X509TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };


    /**
     * 构建HttpClient对象
     *
     * @return
     */
    public static HttpClient buildHttpClient() {
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(sslcontext);
            ClientConnectionManager ccm = new DefaultHttpClient().getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
            HttpParams params = new BasicHttpParams();
            params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 8000);
            params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 8000);
            HttpClient httpclient = new DefaultHttpClient(ccm, params);
            httpclient.getParams().setParameter(HTTP.USER_AGENT,
                    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)");
            return httpclient;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    public HttpResponse getHttpRequest(HttpClient httpClient, String url, List<NameValuePair> parameters,
                                       Map<String, String> cookieData, Map<String, String> headers) {
        // 创建GET请求
        try {
            logger.debug("------------------------------------------------------------------------");
            if (parameters != null && parameters.size() > 0) {
                String paramURL = URLEncodedUtils.format(parameters, HTTP.UTF_8);
                if (url.indexOf("?") > -1) {
                    url = url + "&" + paramURL;
                } else {
                    url = url + "?" + paramURL;
                }
            }
            logger.debug("GET URL: " + url);

            HttpGet get = new HttpGet(url);
//			get.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)");
            get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
            if (cookieData != null) {
                boolean first = true;
                StringBuilder cookie = new StringBuilder();
                for (Map.Entry<String, String> me : cookieData.entrySet()) {
                    if (first) {
                        first = false;
                    } else {
                        cookie.append(";");
                    }
                    cookie.append(me.getKey() + "=" + me.getValue());
                }
                get.setHeader("Cookie", cookie.toString());
            }
//			get.setHeader("Accept", "*/*");
//			get.setHeader("Accept-Encoding", "gzip, deflate, br");
//			get.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
//			get.setHeader("Cache-Control", "no-cache");
//			get.setHeader("Connection", "keep-alive");
//			get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//			get.setHeader("Host", "kyfw.12306.cn");
//			get.setHeader("Origin", "https://kyfw.12306.cn");
//			get.setHeader("Pragma", "no-cache");

            if (headers != null) {
                for (String key : headers.keySet()) {
                    get.setHeader(key, headers.get(key));
                }
            }

            if (logger.isDebugEnabled()) {

                if (parameters != null) {
                    logger.debug(" + Request parameters: ");

                    for (NameValuePair param : parameters) {
                        logger.debug("   - " + param.getName() + " : " + param.getValue());
                    }
                }
                logger.debug(" + Request headers: ");
                for (Header header : get.getAllHeaders()) {
                    logger.debug("   - " + header.getName() + " : " + header.getValue());
                }

            }

            HttpResponse response = httpClient.execute(get);
            if (logger.isDebugEnabled()) {
                logger.debug(" + Response headers: ");
                for (Header header : response.getAllHeaders()) {
                    logger.debug("   - " + header.getName() + " : " + header.getValue());
                }
            }
            logger.debug("***********************************************************************");
            return response;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 返回GET请求响应字符串
     *
     * @param httpClient
     * @param url
     * @param parameters
     * @param cookieData
     * @return
     */
    private String getHttpRequestAsString(HttpClient httpClient, String url, List<NameValuePair> parameters,
                                          Map<String, String> cookieData) {
        try {
            HttpResponse response = getHttpRequest(httpClient, url, parameters, cookieData, null);
            HttpEntity entity = response.getEntity();
            String responseHTML = EntityUtils.toString(entity).trim();
            TicketMainFrame.appendMessage("GET: " + url);
            String message = null;
            if (responseHTML.length() > 300) {
                message = " + Response HTML(0-300):\n" + responseHTML.substring(0, 100);
            } else {
                message = " + Response HTML:\n" + responseHTML;
            }
            TicketMainFrame.appendMessage(message);
            logger.debug(" + Response HTML (ALL):\n" + responseHTML);
            return responseHTML;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String getHttpRequestAsString(HttpClient httpClient, String url, List<NameValuePair> parameters,
                                          Map<String, String> cookieData, Map<String, String> headers) {
        try {
            HttpResponse response = getHttpRequest(httpClient, url, parameters, cookieData, headers);
            HttpEntity entity = response.getEntity();
            String responseHTML = EntityUtils.toString(entity).trim();
            TicketMainFrame.appendMessage("GET: " + url);
            String message = null;
            if (responseHTML.length() > 300) {
                message = " + Response HTML(0-300):\n" + responseHTML.substring(0, 100);
            } else {
                message = " + Response HTML:\n" + responseHTML;
            }
            TicketMainFrame.appendMessage(message);
            logger.debug(" + Response HTML (ALL):\n" + responseHTML);
            return responseHTML;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * POST请求
     *
     * @param httpclient
     * @param url
     * @param parameters
     * @param cookieData
     * @return
     */
    private HttpResponse postHttpRequest(HttpClient httpclient, String url, List<NameValuePair> parameters,
                                         Map<String, String> cookieData, Map<String, String> headers) {
        try {
            logger.debug("------------------------------------------------------------------------");
            logger.debug("POST URL: " + url);

            HttpPost post = new HttpPost(url);
//			post.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)");
            post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
            if (cookieData != null) {
                boolean first = true;
                StringBuilder cookie = new StringBuilder();
                for (Map.Entry<String, String> me : cookieData.entrySet()) {
                    if (first) {
                        first = false;
                    } else {
                        cookie.append("; ");
                    }
                    cookie.append(me.getKey() + "=" + me.getValue());
                }
                post.setHeader("Cookie", cookie.toString());
            }
//			post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            if (headers != null) {
                for (String key : headers.keySet()) {
                    post.setHeader(key, headers.get(key));
                }
            }

            if (parameters != null) {
                UrlEncodedFormEntity uef = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
                post.setEntity(uef);
            }
//			if (logger.isDebugEnabled()) {
            if (parameters != null) {
                logger.debug(" + Request parameters: ");

                for (NameValuePair param : parameters) {
                    logger.debug("   - " + param.getName() + " : " + param.getValue());
                }
            }
            logger.debug(" + Request headers: ");
            for (Header header : post.getAllHeaders()) {
                logger.debug("   - " + header.getName() + " : " + header.getValue());
            }
//			}
            HttpResponse response = httpclient.execute(post);
            if (logger.isDebugEnabled()) {
                logger.debug(" + Response headers: ");
                for (Header header : response.getAllHeaders()) {
                    logger.debug("   - " + header.getName() + " : " + header.getValue());
                }
            }
            logger.debug("***********************************************************************");
            return response;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private HttpResponse loginPostHttpRequest(HttpClient httpclient, String url, List<NameValuePair> parameters,
                                              Map<String, String> cookieData) {
        try {
            logger.debug("------------------------------------------------------------------------");
            logger.debug("POST URL: " + url);

            HttpPost post = new HttpPost(url);
//			post.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)");
            post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
            if (cookieData != null) {
                boolean first = true;
                StringBuilder cookie = new StringBuilder();
                for (Map.Entry<String, String> me : cookieData.entrySet()) {
                    if (first) {
                        first = false;
                    } else {
                        cookie.append("; ");
                    }
                    cookie.append(me.getKey() + "=" + me.getValue());
                }
                post.setHeader("Cookie", cookie.toString());

            }

            post.setHeader("Accept", "*/*");
            post.setHeader("Accept-Encoding", "gzip, deflate, br");
            post.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            post.setHeader("Cache-Control", "no-cache");
            post.setHeader("Connection", "keep-alive");
            post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            post.setHeader("Host", "kyfw.12306.cn");
            post.setHeader("Origin", "https://kyfw.12306.cn");
            post.setHeader("Pragma", "no-cache");
            post.setHeader("Referer", "https://kyfw.12306.cn/otn/login/init");
            post.setHeader("X-Requested-With", "XMLHttpRequest");

            if (parameters != null) {
                UrlEncodedFormEntity uef = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
                post.setEntity(uef);
            }
//			if (logger.isDebugEnabled()) {
            if (parameters != null) {
                logger.debug(" + Request parameters: ");

                for (NameValuePair param : parameters) {
                    logger.debug("   - " + param.getName() + " : " + param.getValue());
                }
            }
            logger.debug(" + Request headers: ");
            for (Header header : post.getAllHeaders()) {
                logger.debug("   - " + header.getName() + " : " + header.getValue());
            }
//			}
            HttpResponse response = httpclient.execute(post);
            if (logger.isDebugEnabled()) {
                logger.debug(" + Response headers: ");
                for (Header header : response.getAllHeaders()) {
                    logger.debug("   - " + header.getName() + " : " + header.getValue());
                }
            }
            logger.debug("***********************************************************************");
            return response;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 返回POST请求响应字符串
     *
     * @param httpClient
     * @param url
     * @param parameters
     * @param cookieData
     * @return
     */
    private String postHttpRequestAsString(HttpClient httpClient, String url, List<NameValuePair> parameters,
                                           Map<String, String> cookieData) {
        try {
            HttpResponse response = postHttpRequest(httpClient, url, parameters, cookieData, null);
            HttpEntity entity = response.getEntity();
            String responseHTML = EntityUtils.toString(entity).trim();
            TicketMainFrame.appendMessage("GET: " + url);
            String message = null;
            if (responseHTML.length() > 300) {
                message = " + Response HTML(0-300):\n" + responseHTML.substring(0, 100);
            } else {
                message = " + Response HTML:\n" + responseHTML;
            }
            TicketMainFrame.appendMessage(message);
            logger.debug(" + Response HTML (ALL):\n" + responseHTML);
            return responseHTML;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String postHttpRequestAsString(HttpClient httpClient, String url, List<NameValuePair> parameters,
                                           Map<String, String> cookieData, Map<String, String> headers) {
        try {
            HttpResponse response = postHttpRequest(httpClient, url, parameters, cookieData, headers);
            HttpEntity entity = response.getEntity();
            String responseHTML = EntityUtils.toString(entity).trim();
            TicketMainFrame.appendMessage("GET: " + url);
            String message = null;
            if (responseHTML.length() > 300) {
                message = " + Response HTML(0-300):\n" + responseHTML.substring(0, 100);
            } else {
                message = " + Response HTML:\n" + responseHTML;
            }
            TicketMainFrame.appendMessage(message);
            logger.debug(" + Response HTML (ALL):\n" + responseHTML);
            return responseHTML;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String loginPostHttpRequestAsString(HttpClient httpClient, String url, List<NameValuePair> parameters,
                                                Map<String, String> cookieData) {
        try {
            HttpResponse response = loginPostHttpRequest(httpClient, url, parameters, cookieData);
            HttpEntity entity = response.getEntity();
            String responseHTML = EntityUtils.toString(entity).trim();
            TicketMainFrame.appendMessage("GET: " + url);
            String message = null;
            if (responseHTML.length() > 300) {
                message = " + Response HTML(0-300):\n" + responseHTML.substring(0, 100);
            } else {
                message = " + Response HTML:\n" + responseHTML;
            }
            TicketMainFrame.appendMessage(message);
            logger.debug(" + Response HTML (ALL):\n" + responseHTML);
            return responseHTML;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 初始化登录用户Cookie数据
     *
     * @return
     */
    public Map<String, String> initCookie() {
        HttpClient httpClient = buildHttpClient();
        Map<String, String> cookieMap = new HashMap<String, String>();
        try {
//			HttpResponse response = getHttpRequest(httpClient, "https://dynamic.12306.cn/otsweb/main.jsp", null, null);
            Map<String, String> map = new HashMap<>();
            map.put("Referer", "https://kyfw.12306.cn/otn/login/init");

            HttpResponse response = getHttpRequest(httpClient, "https://kyfw.12306.cn/otn/login/init", null, null, map);
//			HttpResponse response = getHttpRequest(httpClient, "https://kyfw.12306.cn/otn/leftTicket/init", null, null);
            // 获取消息头的信息
            Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].getName().equals("Set-Cookie")) {
                    String cookie = headers[i].getValue();
                    String cookieName = cookie.split("=")[0];
                    String cookieValue = cookie.split("=")[1].split(";")[0];
                    cookieMap.put(cookieName, cookieValue);
                }
            }
            HttpEntity entity = response.getEntity();
            String htmlcontent = EntityUtils.toString(entity, "utf-8");
            extractKey(htmlcontent, cookieMap);
//			cookieMap.put("current_captcha_type", "Z");

//			JSESSIONID=3B3C8AAF2C1ABD8B62B20F49EE8067CB; BIGipServerotn=267321866.64545.0000; current_captcha_type=Z
//			cookieMap.put("JSESSIONID", "3B3C8AAF2C1ABD8B62B20F49EE8067CB");
//			cookieMap.put("BIGipServerotn", "267321866.64545.0000");
//			cookieMap.put("current_captcha_type", "Z");

//			cookieMap.put("BIGipServerportal", "3151233290.16671.0000");
            TicketMainFrame.appendMessage("初始化获取Cookie：" + cookieMap);
            return cookieMap;
        } catch (Exception e) {
            e.printStackTrace();
            return cookieMap;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public void extractKey(String htmlContent, Map<String, String> cookieMap) {
        try {
            Pattern idP = Pattern.compile("<script src=\"(/otn/dynamicJs/.*?)\" type=\"text/javascript\" xml:space=\"preserve\"></script>");
            Matcher foundId = idP.matcher(htmlContent);
            String dynamicJsUrl = null;
            while (foundId.find()) {
                dynamicJsUrl = foundId.group(1);  //  /otn/dynamicJs/lgiirhu
            }

            String url = "https://kyfw.12306.cn" + dynamicJsUrl;

            HttpClient httpClient = buildHttpClient();
            HttpResponse httpRequest = getHttpRequest(httpClient, url, null, cookieMap, null);
            String jsContent = EntityUtils.toString(httpRequest.getEntity(), "utf-8");

            Pattern idP2 = Pattern.compile("(function bin216.*?)function aj");
            Matcher foundId2 = idP2.matcher(jsContent);
            String jsEncode = null;
            while (foundId2.find()) {
                jsEncode = foundId2.group(1);  //  /otn/dynamicJs/lgiirhu
            }
            idP2 = Pattern.compile("var key='(.*?)");
            foundId2 = idP2.matcher(jsContent);
            String loginKey = null;
            while (foundId2.find()) {
                loginKey = foundId2.group(1);  //  /otn/dynamicJs/lgiirhu
            }

//            encode32(bin216(Base32.encrypt(keyVlues[1], keyVlues[0])))


//            jsMatch=re.search("(function bin216.*?)function aj", jsContent)
//            jsEncode= jsMatch.group(1)#获取加密的js内容
//                    keyMatch=re.search("var key='(.*?)'",jsContent)
//            loginKey= keyMatch.group(1)#获取登录的key


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initOrderCodeImage(Map<String, String> cookieData) {
//		CODE_IMAGE_ORDER
        HttpClient httpClient = buildHttpClient();
        try {
            String url = HttpClientService.CODE_IMAGE_ORDER;
            Map<String, String> map = new HashMap<>();
            map.put("Referer", "https://kyfw.12306.cn/otn/leftTicket/init");
            HttpResponse response = getHttpRequest(httpClient, url, null, cookieData, map);
            HttpEntity entity = response.getEntity();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 基于登录用户Cookie数据构建登陆验证码图片
     *
     * @param cookieData
     * @return
     */
    public File buildLoginCodeImage(Map<String, String> cookieData) {
        HttpClient httpClient = buildHttpClient();
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + cookieData.get("JSESSIONID")
                + ".login.jpg");
        try {
//			String url = "https://dynamic.12306.cn/otsweb/passCodeNewAction.do?module=login&rand=sjrand";
            String url = HttpClientService.CODE_IMAGE;
            HttpResponse response = getHttpRequest(httpClient, url, null, cookieData, null);
            HttpEntity entity = response.getEntity();

            Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].getName().equals("Set-Cookie")) {
                    String cookie = headers[i].getValue();
                    String cookieName = cookie.split("=")[0];
                    String cookieValue = cookie.split("=")[1].split(";")[0];
                    if (cookieName.equals("current_captcha_type")) {
                        cookieData.put(cookieName, cookieValue);
                    }
                }
            }


            InputStream instream = entity.getContent();
            OutputStream out = new FileOutputStream(file);
            byte[] tmp = new byte[1];
            while ((instream.read(tmp)) != -1) {
                out.write(tmp);
            }
            out.close();
            instream.close();
            TicketMainFrame.appendMessage("更新登录验证码图像:" + file.getAbsolutePath());
            return file;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * 基于登录用户Cookie数据构建下单验证码图片
     *
     * @param cookieData
     * @return
     */
    public File buildSubmitCodeImage(Map<String, String> cookieData) {
        HttpClient httpClient = buildHttpClient();
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + cookieData.get("JSESSIONID")
                + ".submit.jpg");
        try {
            String url = "https://dynamic.12306.cn/otsweb/passCodeNewAction.do?module=passenger&rand=randp";
            HttpResponse response = getHttpRequest(httpClient, url, null, cookieData, null);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            OutputStream out = new FileOutputStream(file);
            byte[] tmp = new byte[1];
            while ((instream.read(tmp)) != -1) {
                out.write(tmp);
            }
            out.close();
            instream.close();
            TicketMainFrame.appendMessage("更新下单验证码图像:" + file.getAbsolutePath());
            return file;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private String loginAysnSuggest(String loginUser, String loginPasswd, String loginCode,
                                    Map<String, String> cookieData) {
        HttpClient httpClient = buildHttpClient();
        try {
            String responseHTML = postHttpRequestAsString(httpClient, POST_UTL_LOGINACTION_LOGINAYSNSUGGEST, null,
                    cookieData);
            //{"loginRand":"628","randError":"Y"}
            return StringUtils.substringBetween(responseHTML, "{\"loginRand\":\"", "\",\"randError\":\"Y\"}");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    /******************* 登录相关参数 **************************/
    public static final String LOGIN_LOGINRAND = "loginRand";
    public static final String LOGIN_USERNAME = "loginUserDTO.user_name";
    public static final String LOGIN_RANDCODE = "randCode";
    public static final String LOGIN_PASSWORD = "userDTO.password";
    public static final String LOGIN_NAMEERRORFOCUS = "nameErrorFocus";
    public static final String LOGIN_PASSWORDERRORFOCUS = "passwordErrorFocus";
    public static final String LOGIN_RANDERRORFOCUS = "randErrorFocus";
    public static final String LOGIN_REFUNDFLAG = "refundFlag";
    public static final String LOGIN_REFUNDLOGIN = "refundLogin";

    public boolean Login(String loginUser, String loginPasswd, String loginCode, Map<String, String> cookieData) {
        HttpClient httpClient = buildHttpClient();
        try {

//			String loginRand = this.loginAysnSuggest(loginUser, loginPasswd, loginCode, cookieData);

            //首先检测验证码
            checkRandCodeAnsyn(loginCode, cookieData);
//			checkRandCodeAnsyn(loginCode, cookieData);

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//			parameters.add(new BasicNameValuePair(LOGIN_LOGINRAND, loginRand));
            parameters.add(new BasicNameValuePair(LOGIN_USERNAME, loginUser));
            parameters.add(new BasicNameValuePair(LOGIN_RANDCODE, loginCode));
            parameters.add(new BasicNameValuePair(LOGIN_PASSWORD, loginPasswd));
//			parameters.add(new BasicNameValuePair(LOGIN_NAMEERRORFOCUS, ""));
//			parameters.add(new BasicNameValuePair(LOGIN_PASSWORDERRORFOCUS, ""));
//			parameters.add(new BasicNameValuePair(LOGIN_RANDERRORFOCUS, ""));
//			parameters.add(new BasicNameValuePair(LOGIN_REFUNDFLAG, "Y"));
//			parameters.add(new BasicNameValuePair(LOGIN_REFUNDLOGIN, "N"));


            String responseHTML = loginPostHttpRequestAsString(httpClient, POST_UTL_LOGINACTION, parameters, cookieData);
//			if (responseHTML.contains("欢迎您登录中国铁路客户服务中心网站")) {
            JSONObject jsonObject = parseObject(responseHTML);
            if ("Y".equals(jsonObject.getJSONObject("data").get("loginCheck"))) {
                TicketMainFrame.appendMessage("用户[" + loginUser + "]登录成功");

                //模拟跳转到 模拟跳转initMy12306


                return true;
            } else {
                TicketMainFrame.appendMessage("用户[" + loginUser + "]登录失败，请查看详细的日志输出");
                logger.error(" + Response ERROR HTML:\n" + responseHTML);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private void initMy12306(Map<String, String> cookieData) {
        HttpClient httpClient = buildHttpClient();
        try {
            Map<String, String> map = new HashMap<>();
            map.put("Content-type", "text/html");
            getHttpRequest(httpClient, GET_UTL_INITMY12303, null, cookieData, map);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

    }

    private void checkRandCodeAnsyn(String randCode, Map<String, String> cookieData) {
        HttpClient httpClient = buildHttpClient();
        try {
            String rand = "sjrand";
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair(LOGIN_RANDCODE, randCode));
            parameters.add(new BasicNameValuePair("rand", rand));
            String responseHTML = postHttpRequestAsString(httpClient, CHECKRANDCODEANSYN, parameters, cookieData);

            StringUtils.substringBetween(responseHTML, "{\"loginRand\":\"", "\",\"randError\":\"Y\"}");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    /******************* 查询余票相关参数 ************************/
    // 学生票
    public static final String QUERY_INCLUDESTUDENT = "includeStudent";
    // 始发站
    public static final String QUERY_FROM_STATION_TELECODE = "leftTicketDTO.from_station";
    // 查询时间区间段
    public static final String QUERY_START_TIME_STR = "orderRequest.start_time_str";
    // 到站
    public static final String QUERY_TO_STATION_TELECODE = "leftTicketDTO.to_station";
    // 出发日期
    public static final String QUERY_TRAIN_DATE = "leftTicketDTO.train_date";
    // 火车编号
    public static final String QUERY_TRAIN_NO = "orderRequest.train_no";
    // 未知
    public static final String QUERY_SEATTYPEANDNUM = "seatTypeAndNum";
    // 查询类型
    public static final String QUERY_TRAINCLASS = "trainClass";
    // 查询全部
    public static final String QUERY_TRAINPASSTYPE = "trainPassType";
    //
    public static final String PURPOSE_CODES = "purpose_codes";

    public List<TrainQuery> queryTrain(TicketData ticketData, UserData userData, String trainDate) {
        HttpClient httpClient = buildHttpClient();
        try {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(QUERY_TRAIN_DATE, trainDate));
            parameters.add(new BasicNameValuePair(QUERY_FROM_STATION_TELECODE, ticketData.getTrainFromCode()));
            parameters.add(new BasicNameValuePair(QUERY_TO_STATION_TELECODE, ticketData.getTrainToCode()));
            parameters.add(new BasicNameValuePair(PURPOSE_CODES, "ADULT"));//成人票
//			parameters.add(new BasicNameValuePair(PURPOSE_CODES, "0X00")); //学生票
//			parameters.add(new BasicNameValuePair(QUERY_TRAIN_NO, ""));
//			parameters.add(new BasicNameValuePair(QUERY_TRAINPASSTYPE, "QB"));
//			parameters.add(new BasicNameValuePair(QUERY_TRAINCLASS, "QB#D#Z#T#K#QT#"));
//			parameters.add(new BasicNameValuePair(QUERY_INCLUDESTUDENT, "00"));
//			parameters.add(new BasicNameValuePair(QUERY_SEATTYPEANDNUM, ""));
//			parameters.add(new BasicNameValuePair(QUERY_START_TIME_STR, "00:00--24:00"));
            Map<String, String> headers = new HashMap<>();
            headers.put("Referer", "https://kyfw.12306.cn/otn/leftTicket/init");
            HttpResponse reps = getHttpRequest(httpClient, GET_URL_LOG, parameters,
                    userData.getCookieData(), headers);
            HttpEntity entity = reps.getEntity();
            String responseHTML = EntityUtils.toString(entity).trim();
            TicketMainFrame.appendMessage(responseHTML);
            JSONObject jsonObject = parseObject(responseHTML);
            if (jsonObject != null) {
                "true".equals(jsonObject.get("status"));
            }

            Thread.sleep(2 * 1000);

            httpClient = buildHttpClient();
            String response = getHttpRequestAsString(httpClient, GET_URL_QUERYTICKET, parameters,
                    userData.getCookieData(), headers);

//			return TicketUtil.parserQueryInfo(response);
            return TicketUtil.parserQuery(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public static void main(String[] args) {
        HttpClient httpClient = buildHttpClient();
        try {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(QUERY_TRAIN_DATE, "2016-12-29"));
            parameters.add(new BasicNameValuePair(QUERY_FROM_STATION_TELECODE, "IZQ"));
//            parameters.add(new BasicNameValuePair(QUERY_TO_STATION_TELECODE, "BJP"));
            parameters.add(new BasicNameValuePair(QUERY_TO_STATION_TELECODE, "CSQ"));
            parameters.add(new BasicNameValuePair(PURPOSE_CODES, "ADULT"));//成人票


            String response = new HttpClientService().getHttpRequestAsString(httpClient, GET_URL_QUERYTICKET, parameters, null);

//			return TicketUtil.parserQueryInfo(response);
            List<TrainQuery> trainQueryInfos = TicketUtil.parserQuery(response);
            System.out.println(trainQueryInfos.size());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**************** 验证登录 *********************/
    public JSONObject checkUser(Map<String, String> cookieMap) {
        String title = "[检测用户登录] ";
        TicketMainFrame.appendMessage(title);

        HttpClient httpClient = buildHttpClient();
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//        parameters.add(new BasicNameValuePair("_json_att", ""));

        try {
            Map<String, String> map = new HashMap<>();
            map.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            map.put("If-Modified-Since", "0");
            map.put("Cache-Control", "no-cache");
            HttpResponse response = getHttpRequest(httpClient, POST_URL_CHECKUSER, parameters, cookieMap, map);
            String responseHTML = EntityUtils.toString(response.getEntity()).trim();
            return parseObject(responseHTML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**************** 提交订单相关参数 *********************/
//    public void submitOrderRequest(SingleTrainOrderVO singleTrainOrderVO) {
//        String title = "[登录用户：" + singleTrainOrderVO.getLoginUser() + "] ";
//        HttpClient httpClient = buildHttpClient();
//        TrainQueryInfo trainQueryInfo = singleTrainOrderVO.getTrainQueryInfo();
//        try {
//            // 提交预定的车次 一共25个参数
//            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//            parameters.add(new BasicNameValuePair(ORDER_ARRIVE_TIME, trainQueryInfo.getEndTime()));
//            parameters.add(new BasicNameValuePair(ORDER_FROM_STATION_NAME, trainQueryInfo.getFromStation()));
//            parameters.add(new BasicNameValuePair(ORDER_FROM_STATION_NO, trainQueryInfo.getFormStationNo()));
//            parameters.add(new BasicNameValuePair(ORDER_FROM_STATION_TELECODE, trainQueryInfo.getFromStationCode()));
//            parameters
//                    .add(new BasicNameValuePair(ORDER_FROM_STATION_TELECODE_NAME, trainQueryInfo.getFromStationName()));
//            parameters.add(new BasicNameValuePair(ORDER_INCLUDE_STUDENT, "00"));
//            parameters.add(new BasicNameValuePair(ORDER_LISHI, trainQueryInfo.getTakeTime()));
//            parameters.add(new BasicNameValuePair(ORDER_LOCATIONCODE, trainQueryInfo.getLocationCode()));
//            parameters.add(new BasicNameValuePair(ORDER_MMSTR, trainQueryInfo.getMmStr()));
//            parameters.add(new BasicNameValuePair(ORDER_ROUND_START_TIME_STR, "00:00--24:00"));
//            parameters.add(new BasicNameValuePair(ORDER_ROUND_TRAIN_DATE, singleTrainOrderVO.getTrainDate()));
//            parameters.add(new BasicNameValuePair(ORDER_SEATTYPE_NUM, ""));
//            parameters.add(new BasicNameValuePair(ORDER_SINGLE_ROUND_TYPE, trainQueryInfo.getSingle_round_type()));
//            parameters.add(new BasicNameValuePair(ORDER_START_TIME_STR, "00:00--24:00"));
//            parameters.add(new BasicNameValuePair(ORDER_STATION_TRAIN_CODE, trainQueryInfo.getTrainNo()));
//            parameters.add(new BasicNameValuePair(ORDER_TO_STATION_NAME, trainQueryInfo.getToStation()));
//            parameters.add(new BasicNameValuePair(ORDER_TO_STATION_NO, trainQueryInfo.getToStationNo()));
//            parameters.add(new BasicNameValuePair(ORDER_TO_STATION_TELECODE, trainQueryInfo.getToStationCode()));
//            parameters.add(new BasicNameValuePair(ORDER_TO_STATION_TELECODE_NAME, trainQueryInfo.getToStationName()));
//            parameters.add(new BasicNameValuePair(ORDER_TRAIN_CLASS_ARR, "QB#D#Z#T#K#QT#"));
//            parameters.add(new BasicNameValuePair(ORDER_TRAIN_DATE, singleTrainOrderVO.getTrainDate()));
//            parameters.add(new BasicNameValuePair(ORDER_TRAIN_PASS_TYPE, "QB"));
//            parameters.add(new BasicNameValuePair(ORDER_TRAIN_START_TIME, trainQueryInfo.getStartTime()));
//            parameters.add(new BasicNameValuePair(ORDER_TRAINNO4, trainQueryInfo.getTrainno4()));
//            parameters.add(new BasicNameValuePair(ORDER_YPINFODETAIL, trainQueryInfo.getYpInfoDetail()));
//
//            TicketMainFrame.appendMessage(title + "提交订票请求, 车次：" + singleTrainOrderVO.getTrainNo() + ",席别："
//                    + singleTrainOrderVO.getSeatType());
//            HttpResponse response = postHttpRequest(httpClient, POST_URL_SUBMUTORDERREQUEST, parameters,
//                    singleTrainOrderVO.getCookieData());
//
//            int statusCode = response.getStatusLine().getStatusCode();
//
//            // 返回码 301 或 302 转发到location的新地址
//            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
//                Header locationHeader = response.getFirstHeader("location");
//                String redirectUrl = locationHeader.getValue();
//
//                TicketMainFrame.appendMessage(title + "订票请求响应代码：" + statusCode + ", 转向获取订票凭证数据");
//                logger.debug("Response StatusCode: " + statusCode, "Redirect to URL: " + redirectUrl);
//                HttpClient httpClient2 = buildHttpClient();
//                String responseBody = postHttpRequestAsString(httpClient2, redirectUrl, null,
//                        singleTrainOrderVO.getCookieData());
//                httpClient2.getConnectionManager().shutdown();
//                String leftTicketStr = TicketUtil.getCredential(responseBody);
//                if (StringUtils.isBlank(leftTicketStr)) {
//                    throw new IllegalArgumentException(title + "未取到有效的leftTicketStr数据");
//                } else {
//                    TicketMainFrame.appendMessage(title + "成功获取到leftTicketStr凭证数据：" + leftTicketStr);
//                }
//                singleTrainOrderVO.setSubmitOrderRequestLeftTicketStr(leftTicketStr);
//                String token = TicketUtil.getToken(responseBody);
//                if (StringUtils.isBlank(leftTicketStr)) {
//                    throw new IllegalArgumentException(title + "未取到有效的token数据");
//                } else {
//                    TicketMainFrame.appendMessage(title + "成功获取到Token凭证数据：" + token);
//                }
//                singleTrainOrderVO.setSubmitOrderRequestToken(token);
//            }
//        } finally {
//            httpClient.getConnectionManager().shutdown();
//        }
//    }
//	public static final String ORDER_ARRIVE_TIME = "arrive_time";
//	public static final String ORDER_FROM_STATION_NAME = "from_station_name";
//	public static final String ORDER_FROM_STATION_NO = "from_station_no";
//	public static final String ORDER_FROM_STATION_TELECODE = "from_station_telecode";
//	public static final String ORDER_FROM_STATION_TELECODE_NAME = "from_station_telecode_name";
//	public static final String ORDER_INCLUDE_STUDENT = "include_student";
//	public static final String ORDER_LISHI = "lishi";
//	public static final String ORDER_LOCATIONCODE = "locationCode";
//	public static final String ORDER_MMSTR = "mmStr";
//	public static final String ORDER_ROUND_START_TIME_STR = "round_start_time_str";
//	public static final String ORDER_ROUND_TRAIN_DATE = "round_train_date";
//	public static final String ORDER_SEATTYPE_NUM = "seattype_num";
//	public static final String ORDER_SINGLE_ROUND_TYPE = "single_round_type";
//	public static final String ORDER_START_TIME_STR = "start_time_str";
//	public static final String ORDER_STATION_TRAIN_CODE = "station_train_code";
//	public static final String ORDER_TO_STATION_NAME = "to_station_name";
//	public static final String ORDER_TO_STATION_NO = "to_station_no";
//	public static final String ORDER_TO_STATION_TELECODE = "to_station_telecode";
//	public static final String ORDER_TO_STATION_TELECODE_NAME = "to_station_telecode_name";
//	public static final String ORDER_TRAIN_CLASS_ARR = "train_class_arr";
//	public static final String ORDER_TRAIN_DATE = "train_date";
//	public static final String ORDER_TRAIN_PASS_TYPE = "train_pass_type";
//	public static final String ORDER_TRAIN_START_TIME = "train_start_time";
//	public static final String ORDER_TRAINNO4 = "trainno4";
//	public static final String ORDER_YPINFODETAIL = "ypInfoDetail";

    //secretStr=ffxOBg%2FFr8n3oENAyO2YRdXiw41q2APdhofw3LoOU3Qj%2Fv%2F1zset0gRwJmJdRrMjTuA%2B5pVOg7L5%0AYBEoNjbLeuvOaUCv8xx8yvHf9G7hTDc4C0kVKpAyh2UIcZkakw2pVUySDsoNG4osx27LICq%2F8U%2F6%0AQB1tIRk5AfUopFIN8CupgrbSkRwt3MXs%2FVG%2BOK6qBiBIgJaKGOb1PT%2BjQRFPhpPUM09AMkQLsAAd%0AMaGEC%2Bu0oxPrfOglgV3ziODcF8TqRGLLkbHg6ogGr4LhwNx8486%2BZaAhLJwX1pwpAJaALQVPn4KF%0AQv8DoK9Bwls%3D
    // &train_date=2016-12-29
    // &back_train_date=2016-12-27
    // &tour_flag=dc&
    // purpose_codes=ADULT&
    // query_from_station_name=南宁&
    // query_to_station_name=贵阳&
    // undefined

    public static final String ORDER_SECRETSTR = "secretStr";
    public static final String ORDER_TRAIN_DATE = "train_date";
    public static final String ORDER_BACK_TRAIN_DATE = "back_train_date";
    public static final String ORDER_TOUR_FLAG = "tour_flag";
    public static final String ORDER_PURPOSE_CODES = "purpose_codes";
    public static final String ORDER_QUERY_FROM_STATION_NAME = "query_from_station_name";
    public static final String ORDER_QUERY_TO_STATION_NAME = "query_to_station_name";


    /**
     * 预订单
     *
     * @param singleTrainOrderVO
     */
    public void submitOrderRequest2(SingleTrainOrderVO singleTrainOrderVO) {
        String title = "[登录用户：" + singleTrainOrderVO.getLoginUser() + "] ";
        TrainQuery trainQueryInfo = singleTrainOrderVO.getTrainQuery();
//        TrainQuery.QueryLeftNewDTOBean queryLeftNewDTO = trainQueryInfo.getQueryLeftNewDTO();
        TicketData ticketData = singleTrainOrderVO.getTicketData();
        HttpClient httpClient = buildHttpClient();
        try {
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair(ORDER_SECRETSTR, trainQueryInfo.getSecretStr()));
            parameters.add(new BasicNameValuePair(ORDER_TRAIN_DATE, singleTrainOrderVO.getTrainDate()));
            parameters.add(new BasicNameValuePair(ORDER_BACK_TRAIN_DATE, "2017-01-05"));
            parameters.add(new BasicNameValuePair(ORDER_TOUR_FLAG, "dc"));//dc 表示单程票
            parameters.add(new BasicNameValuePair(ORDER_PURPOSE_CODES, "ADULT"));
            parameters.add(new BasicNameValuePair(ORDER_QUERY_FROM_STATION_NAME, ticketData.getTrainFrom()));
            parameters.add(new BasicNameValuePair(ORDER_QUERY_TO_STATION_NAME, ticketData.getTrainTo()));
            parameters.add(new BasicNameValuePair("undefined", null));

            TicketMainFrame.appendMessage(title + "提交订票请求, 车次：" + singleTrainOrderVO.getTrainNo() + ",席别："
                    + singleTrainOrderVO.getSeatType());
            //            Accept:*/*
//Accept-Encoding:gzip, deflate, br
//Accept-Language:zh-CN,zh;q=0.8
//Cache-Control:no-cache
//Connection:keep-alive
//Content-Length:514
//Content-Type:application/x-www-form-urlencoded; charset=UTF-8
//Cookie:JSESSIONID=18C17277CF9AED21D1CC6DC576B57169; BIGipServerotn=602407178.24610.0000; BIGipServerportal=3134456074.16671.0000; current_captcha_type=Z; _jc_save_fromStation=%u5357%u5B81%2CNNZ; _jc_save_toStation=%u8D35%u9633%2CKQW; _jc_save_fromDate=2016-12-29; _jc_save_toDate=2016-12-27; _jc_save_wfdc_flag=dc
//Host:kyfw.12306.cn
//Origin:https://kyfw.12306.cn
//Pragma:no-cache
            Map<String, String> headers = new HashMap<>();
//            headers.put("Accept","*/*");
//            headers.put("Accept-Encoding","gzip, deflate, br");
//            headers.put("Accept-Language","zh-CN,zh;q=0.8");
//            headers.put("Cache-Control","no-cache");
//            headers.put("Connection","keep-alive");
//            headers.put("Host","kyfw.12306.cn");
//            headers.put("Origin","https://kyfw.12306.cn");
//            headers.put("Pragma","no-cache");
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            headers.put("Referer", "https://kyfw.12306.cn/otn/leftTicket/init");
//            headers.put("X-Requested-With","XMLHttpRequest");
            HttpResponse response = postHttpRequest(httpClient, POST_URL_SUBMITORDERREQUEST, parameters,
                    singleTrainOrderVO.getCookieData(), headers);

            String resp = EntityUtils.toString(response.getEntity()).toString();

            JSONObject o = JSONObject.parseObject(resp);
            if (o != null && !"true".equals(o.get("status"))) {
                throw new IllegalArgumentException("预提交失败");
            }

            int statusCode = response.getStatusLine().getStatusCode();

            // 返回码 301 或 302 转发到location的新地址
            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                Header locationHeader = response.getFirstHeader("location");
                String redirectUrl = locationHeader.getValue();

                TicketMainFrame.appendMessage(title + "订票请求响应代码：" + statusCode + ", 转向获取订票凭证数据");
                logger.debug("Response StatusCode: " + statusCode, "Redirect to URL: " + redirectUrl);
                HttpClient httpClient2 = buildHttpClient();
                String responseBody = postHttpRequestAsString(httpClient2, redirectUrl, null,
                        singleTrainOrderVO.getCookieData());
                httpClient2.getConnectionManager().shutdown();
                String leftTicketStr = TicketUtil.getCredential(responseBody);
                if (StringUtils.isBlank(leftTicketStr)) {
                    throw new IllegalArgumentException(title + "未取到有效的leftTicketStr数据");
                } else {
                    TicketMainFrame.appendMessage(title + "成功获取到leftTicketStr凭证数据：" + leftTicketStr);
                }
                singleTrainOrderVO.setSubmitOrderRequestLeftTicketStr(leftTicketStr);
                String token = TicketUtil.getToken(responseBody);
                if (StringUtils.isBlank(leftTicketStr)) {
                    throw new IllegalArgumentException(title + "未取到有效的token数据");
                } else {
                    TicketMainFrame.appendMessage(title + "成功获取到Token凭证数据：" + token);
                }
                singleTrainOrderVO.setSubmitOrderRequestToken(token);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public String initDc(Map<String, String> cookieData) {
        HttpClient httpClient = buildHttpClient();
        try {
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("_json_att", null));
            Map<String, String> headers = new HashMap<>();
            headers.put("Referer", "https://kyfw.12306.cn/otn/leftTicket/init");
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            String html = postHttpRequestAsString(httpClient, POST_URL_INITDC, parameters,
                    cookieData, headers);

            return submitToken(html, cookieData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public String submitToken(String htmlContent, Map<String, String> cookieMap) {
        try {
            Pattern idP = Pattern.compile("var globalRepeatSubmitToken = '(.*?)';");
            Matcher foundId = idP.matcher(htmlContent);
            String globalRepeatSubmitToken = null;
            while (foundId.find()) {
                globalRepeatSubmitToken = foundId.group(1);  //  /otn/dynamicJs/lgiirhu
            }

            return globalRepeatSubmitToken;
//			Pattern idP3 = Pattern.compile("'key_check_isChange':'(.*?)'");
//			Matcher foundId3 = idP3.matcher(htmlContent);
//			String key_check_isChange = null;
//			while (foundId.find()) {
//				key_check_isChange = foundId.group(1);  //  /otn/dynamicJs/lgiirhu
//			}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //常用联系人
    public void getPassengerDTOs(String ticketToken, Map<String, String> cookieData) {
        HttpClient httpClient = buildHttpClient();
        try {
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("_json_att", null));
            parameters.add(new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", ticketToken));
            Map<String, String> headers = new HashMap<>();
            headers.put("Referer", "https://kyfw.12306.cn/otn/confirmPassenger/initDc");
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            String html = postHttpRequestAsString(httpClient, POST_URL_GETPASSENGERDTOS, parameters,
                    cookieData, headers);
            JSONObject jsonObject = JSONObject.parseObject(html);


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**************** 获取火车票数量 ***********************/
    public static final String GETQUEUECOUNT_FROM = "fromStationTelecode";
    public static final String GETQUEUECOUNT_TO = "toStationTelecode";
    public static final String GETQUEUECOUNT_TRAIN_DATE = "train_date";
    public static final String GETQUEUECOUNT_TRAIN_NO = "train_no";
    public static final String GETQUEUECOUNT_STATIONTRAINCODE = "stationTrainCode";
    public static final String GETQUEUECOUNT_SEATTYPE = "seatType";
    public static final String GETQUEUECOUNT_LEFTTICKET = "leftTicket";
    public static final String GETQUEUECOUNT_PURPOSE_CODES = "purpose_codes";
    public static final String GETQUEUECOUNT_TRAIN_LOCATION = "train_location";
    public static final String GETQUEUECOUNT_REPEAT_SUBMIT_TOKEN = "REPEAT_SUBMIT_TOKEN";
    public static final String GETQUEUECOUNT_JSON_ATT = "_json_att";

    /**
     * //获取队列数量
     * //https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount
     * //            train_date:Thu Dec 29 2016 00:00:00 GMT+0800 (中国标准时间)
     * //                    train_no:710000K14209
     * //            stationTrainCode:K142
     * //            seatType:3
     * //            fromStationTelecode:NNZ
     * //            toStationTelecode:GIW
     * //            leftTicket:N3IG6hLQxi6g3CD6qtFSaaHgpVNy6d7nOPKwQkE7xRtenqq9ZHgYrH8IsX0%3D
     * //            purpose_codes:00
     * //            train_location:Z1
     * //            _json_att:
     * //            REPEAT_SUBMIT_TOKEN:cf06e17f728699b95be8b2a5b69aa5ff
     *
     * @param ticketData
     * @param singleTrainOrderVO
     * @return
     */
    public String getQueueCount(TicketData ticketData, SingleTrainOrderVO singleTrainOrderVO) {
        String title = "[登录用户：" + singleTrainOrderVO.getLoginUser() + "] ";
        HttpClient httpClient = buildHttpClient();
        TrainQuery trainQueryInfo = singleTrainOrderVO.getTrainQuery();
        try {
            TrainQuery.QueryLeftNewDTOBean queryLeftNewDTO = trainQueryInfo.getQueryLeftNewDTO();
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(GETQUEUECOUNT_FROM, ticketData.getTrainFromCode()));
            parameters.add(new BasicNameValuePair(GETQUEUECOUNT_TO, ticketData.getTrainToCode()));

            parameters.add(new BasicNameValuePair(GETQUEUECOUNT_TRAIN_DATE, new Date(singleTrainOrderVO.getTrainDate()).toString()));
            parameters.add(new BasicNameValuePair(GETQUEUECOUNT_TRAIN_NO, queryLeftNewDTO.getTrain_no()));

            parameters.add(new BasicNameValuePair(GETQUEUECOUNT_STATIONTRAINCODE, queryLeftNewDTO.getStation_train_code()));
            parameters.add(new BasicNameValuePair(GETQUEUECOUNT_SEATTYPE, queryLeftNewDTO.getSeat_types()));
//			parameters.add(new BasicNameValuePair(GETQUEUECOUNT_PURPOSE_CODES, queryLeftNewDTO));
//			parameters.add(new BasicNameValuePair(GETQUEUECOUNT_TRAIN_LOCATION, queryLeftNewDTO));
//			parameters.add(new BasicNameValuePair(GETQUEUECOUNT_LEFTTICKET, queryLeftNewDTO));
//			parameters.add(new BasicNameValuePair(GETQUEUECOUNT_JSON_ATT, queryLeftNewDTO));
//			parameters.add(new BasicNameValuePair(GETQUEUECOUNT_REPEAT_SUBMIT_TOKEN, queryLeftNewDTO));


            TicketMainFrame.appendMessage(title + "下单余票数查询请求, 车次：" + singleTrainOrderVO.getTrainNo() + ",席别："
                    + singleTrainOrderVO.getSeatType());
            String response = getHttpRequestAsString(httpClient, GET_URL_GETQUEUECOUNT, parameters,
                    singleTrainOrderVO.getCookieData());
            return response;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    /******************* 提交购票信息 **************************/
    public static final String SUBMIT_LEFTTICKETSTR = "leftTicketStr";
    // 传入参数（姓名，证件类型，证件号码）
    public static final String SUBMIT_OLDPASSENGERS = "oldPassengers";
    // 传入参数 000000000000000000000000000000
    public static final String SUBMIT_BED_LEVEL_ORDER_NUM = "orderRequest.bed_level_order_num";
    public static final String SUBMIT_CANCEL_FLAG = "orderRequest.cancel_flag";
    public static final String SUBMIT_END_TIME = "orderRequest.end_time";
    public static final String SUBMIT_FROM_STATION_NAME = "orderRequest.from_station_name";
    public static final String SUBMIT_FROM_STATION_TELECODE = "orderRequest.from_station_telecode";
    // 传入参数 Y
    public static final String SUBMIT_ID_MODE = "orderRequest.id_mode";
    public static final String SUBMIT_RESERVE_FLAG = "orderRequest.reserve_flag";
    public static final String SUBMIT_SEAT_DETAIL_TYPE_CODE = "orderRequest.seat_detail_type_code";
    public static final String SUBMIT_START_TIME = "orderRequest.start_time";
    public static final String SUBMIT_STATION_TRAIN_CODE = "orderRequest.station_train_code";
    public static final String SUBMIT_TICKET_TYPE_ORDER_NUM = "orderRequest.ticket_type_order_num";
    public static final String SUBMIT_TO_STATION_NAME = "orderRequest.to_station_name";
    public static final String SUBMIT_TO_STATION_TELECODE = "orderRequest.to_station_telecode";
    public static final String SUBMIT_TO_SEAT_TYPE_CODE = "orderRequest.seat_type_code";
    public static final String SUBMIT_TOKEN = "org.apache.struts.taglib.html.TOKEN";
    // 传入参数（seat,seat_detail,ticket,姓名,cardtype,mobileno,Y）
    public static final String SUBMIT_PASSENGERTICKETS = "passengerTickets";
    public static final String SUBMIT_RANDCODE = "randCode";
    public static final String SUBMIT_TEXTFIELD = "textfield";
    public static final String SUBMIT_TFLAG = "tFlag";

    public String confirmSingleForQueueOrder(TicketData ticketData, SingleTrainOrderVO singleTrainOrderVO,
                                             String randCode, boolean justCheck) {
        String title = "[登录用户：" + singleTrainOrderVO.getLoginUser() + "] ";
        HttpClient httpClient = buildHttpClient();
        TrainQuery trainQueryInfo = singleTrainOrderVO.getTrainQuery();
        try {

            String url = null;
            if (justCheck) {
                url = POST_URL_CHECKORDERINFO + randCode;
            } else {
                url = POST_URL_CONFIRMSINGLEFORQUEUE;
            }

            List<PassengerData> validPassengerDatas = ticketData.getValidPassengerDatas();

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            for (int i = 0; i < validPassengerDatas.size(); i++) {
                parameters.add(new BasicNameValuePair("checkbox" + i, Integer.toString(i)));
            }
            parameters.add(new BasicNameValuePair("checkbox9", "Y"));
            parameters.add(new BasicNameValuePair("checkbox9", "Y"));
            parameters.add(new BasicNameValuePair("checkbox9", "Y"));
            parameters.add(new BasicNameValuePair("checkbox9", "Y"));
            parameters.add(new BasicNameValuePair("checkbox9", "Y"));
            parameters.add(new BasicNameValuePair(SUBMIT_LEFTTICKETSTR, singleTrainOrderVO
                    .getSubmitOrderRequestLeftTicketStr()));
            for (int i = 0; i < validPassengerDatas.size(); i++) {
                parameters.add(new BasicNameValuePair(SUBMIT_OLDPASSENGERS, validPassengerDatas.get(i).getShortText()));
            }
            for (int i = 0; i < (5 - validPassengerDatas.size()); i++) {
                parameters.add(new BasicNameValuePair(SUBMIT_OLDPASSENGERS, ""));
            }
            parameters.add(new BasicNameValuePair(SUBMIT_BED_LEVEL_ORDER_NUM, "000000000000000000000000000000"));
            parameters.add(new BasicNameValuePair(SUBMIT_CANCEL_FLAG, "1"));
//			parameters.add(new BasicNameValuePair(SUBMIT_END_TIME, trainQueryInfo.getEndTime()));
//			parameters.add(new BasicNameValuePair(SUBMIT_FROM_STATION_NAME, trainQueryInfo.getFromStation()));
//			parameters.add(new BasicNameValuePair(SUBMIT_FROM_STATION_TELECODE, trainQueryInfo.getFromStationCode()));
//			parameters.add(new BasicNameValuePair(SUBMIT_ID_MODE, "Y"));
//			parameters.add(new BasicNameValuePair(SUBMIT_RESERVE_FLAG, "A"));
//			parameters.add(new BasicNameValuePair(SUBMIT_TO_SEAT_TYPE_CODE, ""));
//			parameters.add(new BasicNameValuePair(SUBMIT_START_TIME, trainQueryInfo.getStartTime()));
//			parameters.add(new BasicNameValuePair(SUBMIT_STATION_TRAIN_CODE, trainQueryInfo.getTrainNo()));
//			parameters.add(new BasicNameValuePair(SUBMIT_TICKET_TYPE_ORDER_NUM, ""));
//			parameters.add(new BasicNameValuePair(SUBMIT_TO_STATION_NAME, trainQueryInfo.getToStation()));
//			parameters.add(new BasicNameValuePair(SUBMIT_TO_STATION_TELECODE, trainQueryInfo.getToStationCode()));
//			parameters.add(new BasicNameValuePair(QUERY_TRAIN_DATE, singleTrainOrderVO.getTrainDate()));
//			parameters.add(new BasicNameValuePair(QUERY_TRAIN_NO, trainQueryInfo.getTrainno4()));
            parameters.add(new BasicNameValuePair(SUBMIT_TOKEN, singleTrainOrderVO.getSubmitOrderRequestToken()));
            for (int i = 0; i < validPassengerDatas.size(); i++) {
                parameters.add(new BasicNameValuePair(SUBMIT_PASSENGERTICKETS, validPassengerDatas.get(i).getLongText(
                        singleTrainOrderVO.getSeatType())));

                parameters.add(new BasicNameValuePair("passenger_" + (i + 1) + "_cardno", validPassengerDatas.get(i)
                        .getCardNo()));
                parameters.add(new BasicNameValuePair("passenger_" + (i + 1) + "_cardtype", validPassengerDatas.get(i)
                        .getCardType().getValue()));
                parameters.add(new BasicNameValuePair("passenger_" + (i + 1) + "_mobileno", validPassengerDatas.get(i)
                        .getMobile()));
                parameters.add(new BasicNameValuePair("passenger_" + (i + 1) + "_name", validPassengerDatas.get(i)
                        .getName()));
                parameters.add(new BasicNameValuePair("passenger_" + (i + 1) + "_seat", singleTrainOrderVO
                        .getSeatType().getValue()));
                parameters.add(new BasicNameValuePair("passenger_" + (i + 1) + "_ticket", validPassengerDatas.get(i)
                        .getTicketType().getValue()));

            }
            parameters.add(new BasicNameValuePair(SUBMIT_RANDCODE, randCode));
            // 检查订单
            if (justCheck) {
                parameters.add(new BasicNameValuePair(SUBMIT_TFLAG, "dc"));
            }
            parameters.add(new BasicNameValuePair(SUBMIT_TEXTFIELD, "中文或拼音首字母"));

            TicketMainFrame.appendMessage(title + "提交订单, 车次：" + singleTrainOrderVO.getTrainNo() + ",席别："
                    + singleTrainOrderVO.getSeatType() + ",日期：" + singleTrainOrderVO.getTrainDate());
            String responseBody = postHttpRequestAsString(httpClient, url, parameters,
                    singleTrainOrderVO.getCookieData());
            return responseBody;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public void leftTicketInit(Map<String, String> cookieData) {
        HttpClient httpClient = buildHttpClient();
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Referer", "https://kyfw.12306.cn/otn/leftTicket/init");
            headers.put("Accept-Encoding", "gzip, deflate, sdch, br");
            HttpResponse httpRequest = getHttpRequest(httpClient, GET_URL_LEFTTICKET_INIT, null, cookieData, headers);
            HttpEntity entity = httpRequest.getEntity();
            String htmlConent = EntityUtils.toString(entity).trim();

            this.extractKey(htmlConent, cookieData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public JSONObject checkOrderInfo(TicketData ticketData, SingleTrainOrderVO singleTrainOrderVO, String submitCode, String token) {
        String title = "[登录用户：" + singleTrainOrderVO.getLoginUser() + "] ";
        HttpClient httpClient = buildHttpClient();
        TrainQuery trainQueryInfo = singleTrainOrderVO.getTrainQuery();
        TrainQuery.QueryLeftNewDTOBean queryLeftNewDTO = trainQueryInfo.getQueryLeftNewDTO();
        try {

            List<PassengerData> passengerDatas = ticketData.getPassengerDatas();
            StringBuffer passengerStr=new StringBuffer();
            StringBuffer oldPassengerStr=new StringBuffer();
            for (int i = 0; i < passengerDatas.size(); i++) {
                PassengerData userInfo = passengerDatas.get(i) ;
                if(userInfo.isSelected()){
                    passengerStr.append(queryLeftNewDTO.getSeat_types()).append(",0,").append(userInfo.getTicketType().getValue())
                            .append(",").append(userInfo.getName())
                            .append(userInfo.getCardType().getValue())
                            .append(",").append(userInfo.getCardNo())
                            .append(",").append(userInfo.getMobile()).append(",N_")
                    ;
                    oldPassengerStr.append(userInfo.getName()).append(",")
                            .append(userInfo.getCardType().getValue())
                            .append(",").append(userInfo.getCardNo())
                    .append(",1_");
                }
            }
            if(passengerStr.length()>0){
                passengerStr.deleteCharAt(passengerStr.length()-1);
            }

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("cancel_flag", "2"));
            parameters.add(new BasicNameValuePair("bed_level_order_num", "000000000000000000000000000000"));
            //3,0,1,汤云汉,1,522121199204163015,,N
            //seatType,0,票类型（成人票填1）,乘客名,passenger_id_type_code,passenger_id_no,mobile_no,’N’
            //多个乘车人用’_’隔开
            parameters.add(new BasicNameValuePair("passengerTicketStr", passengerStr.toString()));
            //汤云汉,1,522121199204163015,1_
            //oldPassengerStr组成的格式：乘客名,passenger_id_type_code,passenger_id_no,passenger_type，’_’
//            多个乘车人用’_’隔开，注意最后的需要多加一个’_’。
            parameters.add(new BasicNameValuePair("oldPassengerStr", oldPassengerStr.toString()));
            parameters.add(new BasicNameValuePair("tour_flag", "dc"));
            parameters.add(new BasicNameValuePair("randCode", ""));
            parameters.add(new BasicNameValuePair("_json_att", null));
            parameters.add(new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", token));

            TicketMainFrame.appendMessage(title + "提交订单, 车次：" + singleTrainOrderVO.getTrainNo() + ",席别："
                    + singleTrainOrderVO.getSeatType() + ",日期：" + singleTrainOrderVO.getTrainDate());
            String responseBody = postHttpRequestAsString(httpClient, POST_URL_CHECKUSER, parameters,
                    singleTrainOrderVO.getCookieData());
            return JSONObject.parseObject(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

        /**
         * @param args
         */
//	public static void main(String[] args) {
//		HttpClientService httpClientService = new HttpClientService();
//		//httpClientService.initCookie();
//
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair(GETQUEUECOUNT_FROM, "BPX"));
//		parameters.add(new BasicNameValuePair(GETQUEUECOUNT_SEAT, "4"));
//		parameters.add(new BasicNameValuePair(GETQUEUECOUNT_STATION, "Z3"));
//		parameters.add(new BasicNameValuePair(GETQUEUECOUNT_TICKET, "40484502902027450262"));
//		parameters.add(new BasicNameValuePair(GETQUEUECOUNT_TO, "JBN"));
//		parameters.add(new BasicNameValuePair(GETQUEUECOUNT_TRAIN_DATE, "2013-11-21"));
//		parameters.add(new BasicNameValuePair(GETQUEUECOUNT_TRAIN_NO, "24000000Z306"));
//
//		Map<String, String> cookieData = new HashMap<String, String>();
//		cookieData.put("JSESSIONID", "81BFCA3C5F2AE80241FD81E7AA23B410");
//		cookieData.put("BIGipServerotsweb", "2262040842.48160.0000");
//
//		httpClientService.getHttpRequestAsString(buildHttpClient(), GET_URL_GETQUEUECOUNT, parameters, cookieData);
//	}

    }
