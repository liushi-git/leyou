package cn.itcast.common.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class UploadUtils {

	private static final String bucket = "heima107-saas";        //空间名称
	private static final String accessKey = "COuoDRVa7JLsuurzIvQSI_pEDceHDw3yGfJEmvwv";
	private static final String secretKey = "3RWpTjB5Jxg3QosUFr4mxbHXJ5JR2m6AHQqYsSlr";
	private static final String url = "http://pz79b813e.bkt.clouddn.com/";

	/**
	 * 将字节数组上传到七牛云上
	 *  返回值 :上传图片的请求路径
	 */
	public static String upload(byte [] bytes) {
		//构造一个带指定 Region 对象的配置类
		Configuration cfg = new Configuration(Region.region0());
		//...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		//...生成上传凭证，然后准备上传
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = null;
		try {
			Auth auth = Auth.create(accessKey, secretKey);
			String upToken = auth.uploadToken(bucket);
			try {
				Response response = uploadManager.put(bytes, key, upToken);
				//解析上传成功的结果
				DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
				key =putRet.key;
			} catch (QiniuException ex) {
				Response r = ex.response;
				System.err.println(r.toString());
				try {
					System.err.println(r.bodyString());
				} catch (QiniuException ex2) {
					//ignore
				}
			}
		} catch (Exception ex) {
			//ignore
		}
		return url + key;
	}

	public static void main(String[] args) throws Exception {
		File file = new File("D:\\log.jpg");
		InputStream input = new FileInputStream(file);
		byte[] byt = new byte[input.available()];
		input.read(byt);
		System.out.println(new UploadUtils().upload(byt));
	}

}
