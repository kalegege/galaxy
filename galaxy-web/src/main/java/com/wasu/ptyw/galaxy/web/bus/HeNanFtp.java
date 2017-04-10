/**
 * 
 */
package com.wasu.ptyw.galaxy.web.bus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wasu.ptyw.galaxy.common.util.FormatUtil;
import com.wasu.ptyw.galaxy.common.util.FtpUtil;
import com.wasu.ptyw.galaxy.common.util.SFtpUtil;

/**
 * @author wenguang
 * @date 2015年11月19日
 */
@Controller
@RequestMapping("/henan")
public class HeNanFtp {
	static String savePath = "/data/ftp/henan/";
	static String rootXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><RecommendList>{0}</RecommendList>";
	static String elementXml = "<Recommend type=\"3\" title=\"{0}\" Code=\"{1}\" poster=\"{2}\" playURL07=\"{3}\" detailURL07=\"{4}\" />";
	static String url = "http://172.30.93.225:8080/PlayOTT?clientIp=&amp;returnUrl=&amp;format=&amp;startPoint=0&amp;csi=12000009&amp;stamp={0}&amp;token={1}&amp;assetId={2}&amp;productId={3}&amp;assetName={4}";// tgt
	static String redirectUrl = "http://wasutv.henancatv.com/template_images/jump/tuijian_hn.jsp?LmId={0}&amp;ZcID={1}&amp;ZcType={2}";
	static String productId = "PT20150828145417168";
	// 服务提供商ID(请填写正确)
	static String csi = "12000009";
	// secretKey (请填写正确)
	static String secretKey = "12000009";

	@RequestMapping(value = "sync")
	public ModelAndView sync() {
		return new ModelAndView("common/error");
	}

	private static String getPlayUrl(String assetId, String assetName, String assetType, String productId) {
		String stamp = new Date().getTime() + "";
		String token = "";
		try {
			StringBuilder buffer = new StringBuilder(csi).append("_").append(secretKey).append("_").append(stamp);
			token = DigestUtils.md5Hex(buffer.toString().getBytes("utf-8")).toUpperCase();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return FormatUtil.formatMsg(url, stamp, token, assetId, productId, assetName);
	}

	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		String assetType = "36";
		String lmId="8648";
		String[] titles = { "反贪风暴", "暴走神探", "我爱的是你爱我", "北漂鱼" };
		String[] pics = {
				"http://125.210.29.19:8080/data/picture/201511/20151109212433_fantanfengbao_12539213991447391430985.jpg",
				"http://125.210.29.19:8080/data/picture/201511/20151105222526_hbbaozoushentan_12517289911447749948690.jpg",
				"http://125.210.29.19:8080/data/picture/201511/20151105034733_woaideshiniaiwo_12512191721446800160391.jpg",
				"http://125.210.29.19:8080/data/picture/201511/20151105222256_hbbeipiaoyu_12517246731446792925807.jpg" };
		String[] assetIDs = { "20460430", "20237405", "20222454", "20237190" };
		String[] vodIDs = { "VODC1511131245002001", "VODC1511061432195301", "VODC1511060921345601",
				"VODC1511061430555201" };
		for (int i = 0; i < 4; i++) {
			sb.append(FormatUtil.formatMsg(elementXml, titles[i], assetIDs[i], pics[i], getPlayUrl(vodIDs[i], titles[i], assetType, productId),
					FormatUtil.formatMsg(redirectUrl, lmId, assetIDs[i], assetType)));
		}
		String xml = FormatUtil.formatMsg(rootXml, sb.toString());
		// xml = XMLConverUtil.convertToXML(root);
		System.out.println(xml);
		String saveFile = writeToFile(xml);
		//uploadToFtp(saveFile);
		System.out.println(saveFile);
		
		download("d:\\data\\");
	}

	/**
	 * 保存到文件
	 */
	private static String writeToFile(String xml) {
		FileOutputStream out = null;
		String saveFile;
		try {
			File file = new File(savePath);
			if (!file.exists())
				file.mkdirs();
			saveFile = savePath + "12000009_recommend.xml";
			out = new FileOutputStream(saveFile);
			out.write(xml.getBytes("UTF-8"));
			out.flush();
			
			return saveFile;
		} catch (Exception e) {

		} finally {
			IOUtils.closeQuietly(out);
		}
		return null;
	}

	static String ftpUrl = "125.210.135.171";
	static int ftpPort = 5188;
	static String ftpUserName = "root";
	static String ftpPassword = "wasuPtywVideo#1";
	static String remotePath = "/root/";
	static String remoteFileName = csi + "_recommend.xml";
//ftp://root:wasuPtywVideo#1@125.210.135.171:5188/root/
	private static boolean uploadToFtp(String localFile) {
		SFtpUtil ftp = new SFtpUtil(ftpUrl, ftpPort, ftpUserName, ftpPassword);
		return ftp.upload(remotePath, remoteFileName, localFile);
	}
	private static boolean  download(String localPath) {
		SFtpUtil ftp = new SFtpUtil(ftpUrl, ftpPort, ftpUserName, ftpPassword);
		return ftp.download(remotePath, remoteFileName, localPath);
	}
}
