package com.wasu.ptyw.galaxy.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrcodeUtil {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	public static final int WIDTH = 300;
	public static final int HEIGHT = 300;
	private static final String FORMAT = "png";
	private static Map<EncodeHintType, Object> HINTS = Maps.newHashMap();
	static {
		HINTS.put(EncodeHintType.CHARACTER_SET, "utf-8");
		HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // 容错率
		HINTS.put(EncodeHintType.MARGIN, 0); // 二维码边框宽度，这里文档说设置0-4
	}

	private QrcodeUtil() {
	}

	/**
	 * 生成二维码并保存到文件
	 * 
	 * @param content
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static boolean build(String content, String filePath) {
		return build(content, filePath, WIDTH, HEIGHT);
	}

	/**
	 * 生成二维码并保存到文件
	 * 
	 * @param content
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static boolean build(String content, String filePath, int width, int height) {
		try {
			//BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, HINTS);
			BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, HINTS);
			writeToFile(bitMatrix, FORMAT, new File(filePath));
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * 生成二维码并输出到流
	 * 
	 */
	public static boolean build(String content, OutputStream outStream) {
		return build(content, outStream, WIDTH, HEIGHT);
	}

	/**
	 * 生成二维码并输出到流
	 * 
	 */
	public static boolean build(String content, OutputStream outStream, int width, int height) {
		try {
			//BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, HINTS);
			BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, HINTS);
			writeToStream(bitMatrix, FORMAT, outStream);
		} catch (Exception e) {

		}
		return false;
	}

	private static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file.getAbsolutePath());
		}
	}

	private static void writeToStream(BitMatrix matrix, String format, OutputStream outStream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, outStream)) {
			throw new IOException("Could not write an image of format " + format + " to outStream");
		}
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static void main(String[] args) throws Exception {
		String content = "weixin://wxpay/bizpayurl?sign=B4D00A3B50986C4C286463E79A17373C&mch_id=1232509802&product_id=TV_310&appid=wxa0fc98381e4bddd6&nonce_str=i9p5yonv5zh5toufa3ialc081cvs27nl&time_stamp=1436858993";
		// 生成二维码
		content = "123456789";
		build(content, "d:" + File.separator + "qrcode.png", 280, 280);
	}
}
