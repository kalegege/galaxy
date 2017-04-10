/**
 * 
 */
package com.wasu.ptyw.galaxy.web.jsonp;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.wasu.ptyw.galaxy.common.util.DigestUtil;

/**
 * 支持JSONP的Fastjson的消息转换器
 * 
 * @author wenguang
 * @date 2015年6月29日
 */
public class FastJsonHttpMessageConverter extends com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter {

	@Override
	protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		if (obj instanceof JSONPObject) {
			JSONPObject jsonp = (JSONPObject) obj;
			OutputStream out = outputMessage.getBody();
			String text = jsonp.getFunction() + "(" + JSON.toJSONString(jsonp.getJson(), getFeatures()) + ")";
			text = DigestUtil.native2Ascii(text);
			byte[] bytes = text.getBytes(getCharset());
			out.write(bytes);
		} else {
			super.writeInternal(obj, outputMessage);
		}
	}
}
