package cn.itcast.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义类型转化器
 *  实现Converter接口
 */
public class StringToDateConverter implements Converter<String,Date> {

	/**
	 * 将传入的String类型数据转化为date数据并输出
	 */
	public Date convert(String source) {
		Date date = null ;
		try {

			if(source.contains("/")) {
				date = new SimpleDateFormat("yyyy/MM/dd").parse(source);
			}else{
				date = new SimpleDateFormat("yyyy-MM-dd").parse(source);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
}
