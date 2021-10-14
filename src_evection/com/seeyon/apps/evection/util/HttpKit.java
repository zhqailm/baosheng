package com.seeyon.apps.evection.util;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import bsh.ParseException;

import javax.net.ssl.SSLContext;


//Http套件工具类
public class HttpKit {
	//HttpClient高并发请求连接池
	private static PoolingHttpClientConnectionManager cm;
	private static String EMPTY_STR = "";

	//初始化连接池方法
	private static void init() {
		if (cm == null) {
			cm = new PoolingHttpClientConnectionManager();
			cm.setMaxTotal(50);// 整个连接池最大连接数
			cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2
		}
	}

	/**
	 * 通过连接池获取HttpClient
	 */
	private static CloseableHttpClient getHttpClient() {
		init();
		return HttpClients.custom().setConnectionManager(cm).build();
	}

	public static String get(String url) throws Exception {
		HttpGet httpGet = new HttpGet(url);
		return getResult(httpGet);
	}

	public static String get(String url, Map<String, Object> params) throws Exception {
		URIBuilder ub = new URIBuilder();
		ub.setPath(url);

		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		ub.setParameters(pairs);

		HttpGet httpGet = new HttpGet(ub.build());
		return getResult(httpGet);
	}

	public static String get(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
		URIBuilder ub = new URIBuilder();
		ub.setPath(url);

		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		ub.setParameters(pairs);

		HttpGet httpGet = new HttpGet(ub.build());
		for (Map.Entry<String, Object> param : headers.entrySet()) {
			httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
		}
		return getResult(httpGet);
	}

	public static String post(String url) throws Exception {
		HttpPost httpPost = new HttpPost(url);
		return getResult(httpPost);
	}

	public static String post(String url, Map<String, Object> params) throws Exception {
		HttpPost httpPost = new HttpPost(url);
		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
		return getResult(httpPost);
	}
	
	public static String post(String url, String json) throws Exception {
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(json, "UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		return getResult(httpPost);

	}

	public static String post(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
		HttpPost httpPost = new HttpPost(url);

		for (Map.Entry<String, Object> param : headers.entrySet()) {
			httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
		}

		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF_8"));

		return getResult(httpPost);
	}

	private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> param : params.entrySet()) {
			pairs.add(new BasicNameValuePair(param.getKey(), String
					.valueOf(param.getValue())));
		}

		return pairs;
	}
	
	public static String send(String url, JSONObject jsonObject, String encoding,String token) throws Exception {
	      String body = "";

	      //创建httpclient对象
	      CloseableHttpClient client = getSingleSSLConnection();
	      //创建post方式请求对象
	      HttpPost httpPost = new HttpPost(url);

	      //装填参数
	      StringEntity s = new StringEntity(jsonObject.toString(), "utf-8");
	      s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
	            "application/json"));
	      //设置参数到请求对象中
	      httpPost.setEntity(s);
//	      System.out.println("请求地址："+url);
//	        System.out.println("请求参数："+nvps.toString());

	      //设置header信息
	      //指定报文头【Content-type】、【User-Agent】
//	        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
	      httpPost.setHeader("Content-type", "application/json");
		  httpPost.setHeader("access_token", token);


	      //执行请求操作，并拿到结果（同步阻塞）
	      CloseableHttpResponse response = client.execute(httpPost);
	      //获取结果实体
	      HttpEntity entity = response.getEntity();
	      if (entity != null) {
	         //按指定编码转换结果实体为String类型
	         body = EntityUtils.toString(entity, encoding);
	      }
	      EntityUtils.consume(entity);
	      //释放链接
	      response.close();
	      return body;
	   }

	/**
	 * 处理Http请求
	 * 
	 * @param request
	 * @return
	 */
	private static String getResult(HttpRequestBase request) throws Exception{
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		//过连接池获取HttpClient
//		CloseableHttpClient httpClient = getHttpClient();
		CloseableHttpClient httpClient = getSingleSSLConnection();
		CloseableHttpResponse response = httpClient.execute(request);
		// response.getStatusLine().getStatusCode();
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// long len = entity.getContentLength();// -1 表示长度未知
			String result = EntityUtils.toString(entity);
			response.close();
			httpClient.close();
			return result;
		}
		return EMPTY_STR;
	}


	//忽略ssl认证获取client
	private static CloseableHttpClient getSingleSSLConnection()
			throws Exception {

			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
					return true;
				}
			}).build();

			CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
			return client;
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//			return HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(requestConfig).build();
	}
	
}
