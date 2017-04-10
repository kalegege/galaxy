/**
 * 
 */
package com.wasu.ptyw.galaxy.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * 用来操作ftp的综合类。<br/>
 * 主要依赖jar包commons-net-3.3.jar。
 * 
 * @author wenguang
 * @date 2015年11月14日
 */
public class FtpBatch {
	// ftp 地址
	private String url;
	// ftp端口
	private int port;
	// 用户名
	private String userName;
	// 密码
	private String password;

	FTPClient ftp = null;

	public static FtpBatch create(String url, int port, String userName, String password) throws SocketException,
			IOException {
		return new FtpBatch(url, port, userName, password).open();
	}

	/**
	 * 构造函数
	 * 
	 * @param url
	 *            ftp地址
	 * @param port
	 *            ftp端口
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @throws IOException
	 * @throws SocketException
	 * 
	 */
	public FtpBatch(String url, int port, String userName, String password) throws SocketException, IOException {
		this.url = url;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}

	/**
	 * 从FTP服务器下载指定文件名的文件。
	 * 
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param remoteFileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return 成功下载返回true，否则返回false。
	 */
	public boolean download(String remotePath, String remoteFileName, String localPath) {
		OutputStream is = null;
		try {
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			is = new FileOutputStream(localPath + "/" + remoteFileName);
			ftp.retrieveFile(remoteFileName, is);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * 本地上传文件到FTP服务器
	 * 
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param remoteFileName
	 *            FTP服务器上的文件名
	 * @param localFile
	 *            本地文件
	 * @return 成功上传返回true，否则返回false。
	 */
	public boolean upload(String remotePath, String remoteFileName, String localFile) {
		InputStream in = null;
		try {
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			in = new FileInputStream(new File(localFile));
			ftp.storeFile(remoteFileName, in);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * 从FTP服务器列出指定文件夹下文件名列表
	 */
	public List<String> getFileNameList(String remotePath) throws IOException {
		// 目录列表记录
		List<String> fileNames = new ArrayList<String>();
		try {
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile file : fs) {
				fileNames.add(file.getName());
			}
		} catch (IOException e) {
			throw e;
		} finally {
		}
		return fileNames;
	}

	private FtpBatch open() throws SocketException, IOException {
		ftp = new FTPClient();
		int reply;
		ftp.connect(url, port);
		// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
		ftp.login(userName, password);// 登录
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			throw new SocketException("replyCode invalid");
		}
		// ftp.setBufferSize(100000); //不设置的话为1024,影响下载速度
		ftp.setControlEncoding("UTF-8");
		// 设置PassiveMode传输
		ftp.enterLocalPassiveMode();
		// 设置以二进制流的方式传输
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		return this;
	}

	public void close() {
		if (ftp == null)
			return;
		try {
			ftp.logout();
			ftp.disconnect();
		} catch (Exception e) {
		}
	}
}