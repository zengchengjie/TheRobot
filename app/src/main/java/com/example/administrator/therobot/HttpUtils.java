package com.example.administrator.therobot;

import android.util.Log;

import com.example.administrator.therobot.bean.ChatMessage;
import com.example.administrator.therobot.bean.CommonException;
import com.example.administrator.therobot.bean.Result;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;


public class HttpUtils
{
//	private static String API_KEY = "534dc342ad15885dffc10d7b5f813451";//官网的key
//	private static String URL = "http://www.tuling123.com/openapi/api";//使用官网数据

private static String API_KEY = "5c6c80fe46ef5b53a36548eacd0ff520";//聚合数据的key
	private static String URL = "http://op.juhe.cn/robot/index";//使用聚合数据
public static String text;

	/**
	 * 发送一个消息，并得到返回的消息 （其实就是一个get请求）
	 * @param msg
	 * @return
	 */
	public static ChatMessage sendMsg(String msg)//定义一个（chatMessage类型）发送消息的方法
	{
		ChatMessage message = new ChatMessage();//实例化chatMessage对象
		String url = setParams(msg);//调用setparams方法  将拼接的url传入
		String res = doGet(url);//调用doget方法获得返回数据 将setparams中拼接完成的url传入
		try {
			JSONObject json = new JSONObject(res);
			JSONObject jsons = json.getJSONObject("result");
				text = jsons.getString("text");
		} catch (JSONException e) {
			e.printStackTrace();
		}

//		if (result.getCode() > 400000 || result.getText() == null
//				|| result.getText().trim().equals(""))
//		{
//			message.setMsg("该功能等待开发，请重新输入");
//		}else
//		{
		message.setMsg(text);
		message.setType(ChatMessage.Type.INPUT);//设置消息的类型
		message.setDate(new Date());//为接收到的消息设置日期

		return message;//返回message对象
	}

	/**
	 * 拼接Url
	 * @param msg
	 * @return
	 */
	private static String setParams(String msg)//传入的参数是返回的消息（未解析过的）
	{
		try
		{
			msg = URLEncoder.encode(msg, "UTF-8");//设置编码格式 使用encode方法
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return URL + "?info=" + msg + "&key=" + API_KEY;//将需要的url拼接起来
//		return URL + "?key=" + API_KEY + "&info=" + msg;//将需要的url拼接起来
	}

	/**
	 * Get请求，获得返回数据
	 * @param urlStr
	 * @return
	 */
	private static String doGet(String urlStr)
	{
		URL url = null;
		HttpURLConnection conn = null;
		StringBuilder response;
		try
		{
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(5 * 1000);
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");

			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			response = new StringBuilder();
			String line;
			if ((line = reader.readLine()) != null) {
				response.append(line);

			}


//				is = conn.getInputStream();
//				baos = new ByteArrayOutputStream();
//				int len = -1;
//				byte[] buf = new byte[128];
//
//				while ((len = is.read(buf)) != -1)
//				{
//					baos.write(buf, 0, len);
//				}
//				baos.flush();
//				return baos.toString();
			 else
			{
				throw new CommonException("服务器连接错误！");
			}
//
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new CommonException("服务器连接错误！");
		}
//		finally
//		{
//			try
//			{
//				if (is != null)
//					is.close();
//			} catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//
//			try
//			{
//				if (baos != null)
//					baos.close();
//			}
//		catch (IOException e)
//			{
//				e.printStackTrace();
//			}
			conn.disconnect();
//		}
		return response.toString();
	}

}
