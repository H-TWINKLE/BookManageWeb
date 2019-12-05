package com.lxs.common.utils;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jfinal.kit.Base64Kit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.lxs.common.model._MappingKit;

import sun.misc.BASE64Decoder;

public enum Utils {

	INSTANCE;

	private final ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 5, 0, TimeUnit.SECONDS,
			new ArrayBlockingQueue<>(20));

	public void addRunnableToPool(Runnable r) {

		pool.execute(r);

	}

	public void pluginStart() {
		DruidPlugin dp = new DruidPlugin(
				"jdbc:mysql://127.0.0.1:3306/books?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull", "root",
				"123");
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		_MappingKit.mapping(arp);

		dp.start();
		arp.start();
	}

	public String encryptionStringByBase64(String value) {

		if (StrKit.isBlank(value))
			return "";

		return Base64Kit.encode(System.currentTimeMillis() + value);

	}

	public String decryptStringByBase64(String value) {	

		if (StrKit.isBlank(value))
			return "";
		
		return Base64Kit.decodeToStr(value).substring(String.valueOf(System.currentTimeMillis()).length());

	}

	public boolean base64ToImage(String imgStr, String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片

		if (StrKit.isBlank(imgStr)) // 图像数据为空
			return false;

		imgStr = imgStr.replace("data:image/png;base64,", "");

		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}

			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();

			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public String sendEmilInText(String admin, String url) {

		StringBuffer content = new StringBuffer();

		content.append("<div><div style='margin-left:4%;'>");
		content.append("<p style='color:red;'>");
		content.append("用户  " + admin + "，您好：</p>");
		content.append("<p style='text-indent: 2em;'>您好！您正在进行邮箱验证，在浏览器地址栏里输入并访问以下激活链接即可进行账户验证：</p>");
		content.append("<a href=\"" + url + "\"><p style='text-indent: 2em;display: block;word-break: break-all;'>"
				+ url + "</p></a>");
		content.append("</div>");
		content.append("<ul style='color: rgb(169, 169, 189);font-size: 18px;'>");
		content.append("<li><h5>请您妥善保管，此邮件无需回复。</h5></li>");
		content.append("</ul>");
		content.append("</div>");

		return content.toString();

	}

	public String getFilePath(Integer id) {

		StringBuffer s = new StringBuffer();

		s.append(id);
		s.append("_");
		s.append("id");
		s.append("_");
		s.append(System.currentTimeMillis());
		s.append(".png");

		return s.toString();

	}

	public String appendVefiUrl(String method, String appName, int port, String code) {

		StringBuffer content = new StringBuffer();

		if (port == 80) { // 80为服务器
			content.append("http://htwinkle.cn/");
			content.append(appName);
			content.append("/");
			content.append(method);
			content.append("?code=");
			content.append(code);
		} else {
			content.append("http://127.0.0.1:");
			content.append(port);
			content.append("/");
			content.append(method);
			content.append("?code=");
			content.append(code);
		}

		return content.toString();

	}

}
