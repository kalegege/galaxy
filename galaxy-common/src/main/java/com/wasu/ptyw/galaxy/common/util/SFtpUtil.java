/**
 * 
 */
package com.wasu.ptyw.galaxy.common.util;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.compress.utils.IOUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author wenguang
 * @date 2015年11月24日
 */
public class SFtpUtil {
	// sftp 地址
	private String host;
	// sftp端口
	private int port;
	// 用户名
	private String username;
	// 密码
	private String password;

	Session session = null;
	Channel channel = null;

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
	 * 
	 */
	public SFtpUtil(String host, int port, String userName, String password) {
		this.host = host;
		this.port = port;
		this.username = userName;
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
		// http://blog.csdn.net/rangqiwei/article/details/9002046
		try {
			ChannelSftp chSftp = getChannel(60000);
			chSftp.get(remotePath + remoteFileName, localPath);
			chSftp.quit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close();
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
		FileInputStream in = null;
		try {
			// http://www.cnblogs.com/longyg/archive/2012/06/25/2556576.html
			ChannelSftp chSftp = getChannel(60000);
			in = new FileInputStream(localFile);
			chSftp.put(in, remotePath + remoteFileName, ChannelSftp.OVERWRITE);
			chSftp.quit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			IOUtils.closeQuietly(in);
			close();
		}
	}

	public ChannelSftp getChannel(int timeout) throws JSchException {
		JSch jsch = new JSch();
		session = jsch.getSession(username, host, port);
		session.setPassword(password); // 设置密码
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接

		channel = session.openChannel("sftp"); // 打开SFTP通道
		channel.connect(); // 建立SFTP通道的连接
		return (ChannelSftp) channel;
	}

	public void close() {
		try {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		} catch (Exception e) {
		}
	}
}
