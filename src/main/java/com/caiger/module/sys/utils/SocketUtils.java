package com.caiger.module.sys.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.caiger.common.lang.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * SOCKET
 * @author kaifa12
 *
 */
@Slf4j
public class SocketUtils {
	
	
	public static final Integer PORT = 5556;

	public static Map<String, Socket> socketMap = new HashMap<String, Socket>();
	
	public static Map<String, OutputStream> osMap = new HashMap<String, OutputStream>();
	
	public static Map<String, InputStream> isMap = new HashMap<String, InputStream>();
	
	public static Socket socket = null;
	
	public static SocketAddress remoteAddr = null;
	
	public static InputStream is = null;
	
	public static OutputStream os = null;
	

	/**
	 * 发送指令
	 * @param sendMsg
	 * @param socket
	 * @param os
	 * @param is
	 * @return
	 * @throws IOException 
	 */
	public static synchronized String sendMessage(String name, String sendMsg, String ip) throws Exception {
		
		    //受信
			String recMsg ="";
		    if (StringUtils.isNotBlank(sendMsg)) {
		    	// 将发送的报文转成byte[]
				byte[] sendMsgByte = Utils.resolveStringToByte(sendMsg);
		    	
		    	socket = socketMap.get(ip);
				if (socket == null) {
					log.info("IP地址："+ ip +"新建socket连接....");
					socket = new Socket();
					remoteAddr = new InetSocketAddress(ip, PORT);
					socket.connect(remoteAddr, 5000);
					socket.setSoTimeout(10000);
					socketMap.put(ip, socket);
				}
				 os = osMap.get(ip);
				 if (os == null) {
					 os = socketMap.get(ip).getOutputStream();
					 osMap.put(ip, os);
				}
				//os = socket.getOutputStream();
				log.info("====> 送信："+ip + ","+ name +": ["+sendMsg+"] ");
				os.write(sendMsgByte);
				os.flush();
				is = isMap.get(ip);
				if(is == null) {
					is = socket.getInputStream();
					isMap.put(ip, is);
				}
				
				byte[] b = new byte[1024];
				int read = is.read(b);
				byte[] receiveByteMessage = Arrays.copyOf(b, read);
				recMsg = Utils.AnalysisReceiveMessageResult(receiveByteMessage);
			    log.info("<==== 受信："+ip + ","+ name +": ["+recMsg+"] ");
			}
		
		   
			return recMsg;
	}
	
	
	public static void close(String ip) {
		
		Socket socket = socketMap.get(ip);
		if (socket!=null) {
			try {
				InputStream is = isMap.get(ip);
				if (is!=null) {
					is.close();
					isMap.remove(ip);
				}
				OutputStream os = osMap.get(ip);
				if (os!=null) {
					os.close();
					osMap.remove(ip);
				}
				socket.close();
				socketMap.remove(ip);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	

}
