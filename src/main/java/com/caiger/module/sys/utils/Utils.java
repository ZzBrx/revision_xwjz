package com.caiger.module.sys.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Utils {
	
	private static Logger log = LoggerFactory.getLogger(Utils.class);
	
	
	
	/**
	 * 将HEX转成IP
	 * @return
	 */
	public static String convertHexToIp(String ipHex) {

		StringBuffer sb = new StringBuffer();
		//每两个字节分开
		if(ipHex.length()>2&&ipHex.length()%2==0){

			int ipIndexSize = ipHex.length()/2;
			for (int i = 0;i<ipIndexSize;i++){
				if (i<ipIndexSize-1){
					sb.append(String.valueOf(Integer.parseInt(ipHex.substring(i*2,(i+1)*2),16)));
					sb.append(".");
				}else {
					sb.append(String.valueOf(Integer.parseInt(ipHex.substring(i*2,(i+1)*2),16)));
				}

			}
		}else {

			sb.append("0.0.0.0");
		}
		return sb.toString();
	}
	
	/**
	 * 将ip转成byte
	 * @return
	 */
	public static byte[] convertIpToByte(String ip) {

		String[] ips = ip.split("\\.");
		byte[] ipbs = new byte[4];
		for (int j = 0; j < ipbs.length; j++) {
			int m = Integer.parseInt(ips[j]);
			byte b = (byte) m;
			ipbs[j] = b;

		}
		return ipbs;

	}

	/**
	 * 检查是否为IP地址
	 * @param ip
	 * @return
     */

	public static boolean matchIp(String ip){

		String pattern = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(ip);
		return m.matches();

	}

	/**
	 * 接收时 7D 5E ==> 7E,7D 5D ==> 7D
	 */
	public static String convertToSingleString(String msg) {

		// 去掉头尾7E
		String newMsg = msg.substring(2, msg.length() - 2);
		// 声明一个字符串数组
		String[] str = new String[newMsg.length() / 2];

		// 给字符串数组赋值
		for (int i = 0; i < newMsg.length() / 2; i++) {
			str[i] = newMsg.substring(i * 2, i * 2 + 2);
		}

		// 计数
		for (int i = 0; i < str.length; i++) {
			if (i + 1 == str.length) {
				break;
			}
			if ("7D".equals(str[i]) && "5E".equals(str[i + 1])) {
				str[i] = "7E";
				str[i + 1] = "";
			}
			if ("7D".equals(str[i]) && "5D".equals(str[i + 1])) {
				str[i] = "7D";
				str[i + 1] = "";
			}
		}
		// 声明一个StringBuffer用来存放转译以后的字符串
		StringBuffer newStr = new StringBuffer();
		// 将帧头接上
		newStr.append("7E");
		for (int i = 0; i < str.length; i++) {
			newStr.append(str[i]);
		}
		// 将帧尾接上
		newStr.append("7E");
		return newStr.toString();
	}

	/**
	 * 将数据发送时 7E ==> 7D 5E,7D ==> 7D 5D
	 */
	public static String convertToTDoubleString(String msg) {

		String newMsg = msg.substring(2, msg.length() - 2);
		String[] str = new String[newMsg.length() / 2];

		for (int i = 0; i < newMsg.length() / 2; i++) {
			str[i] = newMsg.substring(i * 2, i * 2 + 2);
		}

		for (int i = 0; i < str.length; i++) {

			if ("7E".equals(str[i])) {
				str[i] = "7D5E";
			}
			if ("7D".equals(str[i])) {
				str[i] = "7D5D";
			}
		}

		// 声明一个StringBuffer用来存放转译以后的字符串
		StringBuffer newStr = new StringBuffer();
		// 将帧头接上
		newStr.append("7E");
		for (int i = 0; i < str.length; i++) {
			newStr.append(str[i]);
		}
		newStr.append("7E");
		// 将帧尾接上
		return newStr.toString();
	}

	/**
	 * 检查报文是否完整
	 * 
	 * @param msg
	 *            收到的转译以后的报文
	 * @return
	 */
	public static boolean checkReceiveMsg(String msg) {
		// 如果报文以7E开头并且以7E结尾 38是报文到命令码的长度
		if (msg.length() >= 4 && "7E".equals(msg.substring(0, 2)) && "7E".equals(msg.substring(msg.length() - 2, msg.length()))) {

			// 自己计算的CRC
			String crcResult = encryptCRC(msg, "REC");

			// 这个是报文中的CRC
			String checkStr = msg.substring(msg.length() - 6, msg.length() - 2);
			// 不区分大小写比较原始校验码与计算出的校验码是否一致
			if (crcResult.equalsIgnoreCase(checkStr)) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * 将数据CRC验证
	 * 
	 * @param msg
	 *            需要校验的数据 接收时是完整的一条报文，所以能够截取出CRC域
	 *            发送时需要先计算出CRC域再组装成一条报文，所以传入的参数只是供CRC计算的域的值
	 * @param type
	 *            是发送还是接收
	 * @return
	 */
	public static String encryptCRC(String msg, String type) {

		// 报文转成byte形式
		byte[] messageByte = resolveStringToByte(msg);

		CRC crc = new CRC();
		String cacResultData = "";
		if ("REC".equals(type)) {
			// 去掉头尾还有CRC校验场
			byte[] newByte = new byte[messageByte.length - 4];

			for (int i = 0; i < newByte.length; i++) {
				newByte[i] = messageByte[i + 1];
			}
			// CRC校验场结果
			cacResultData = changeHighLow(addLeftZero(Integer.toHexString(crc.getCRC16(newByte, newByte.length)).toUpperCase(), 4));
		} 
		else if ("SEND".equals(type)) {
			cacResultData = changeHighLow(addLeftZero(Integer.toHexString(crc.getCRC16(messageByte, messageByte.length)).toUpperCase(), 4));
		}

		return cacResultData;
	}

	/**
	 * 用于分解发送的报文组成新的byte数组
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static byte[] resolveStringToByte(String str) {

		byte[] byteMsg = new byte[str.length()/2];

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < str.length() / 2; i++) {
			list.add(str.substring(i * 2, i * 2 + 2));
		}

		for (int i = 0; i < list.size(); i++) {
			byteMsg[i] = (byte) Integer.parseInt(list.get(i), 16);
		}

		return byteMsg;

	}

	/**
	 * 高低位转换
	 * 
	 * @param str
	 * @return
	 */
	public static String changeHighLow(String str) {
		int strLen = str.length();
		StringBuffer sbHighLow = new StringBuffer();
		for (int i = 0; i < strLen/4; i++) {
			sbHighLow.append((str.substring(i*4,(i+1)*4)).substring(2, 4)+(str.substring(i*4,(i+1)*4)).substring(0, 2));
		}
		
		return sbHighLow.toString();
	}
	/**
	 * 转成integer型以后补零
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String addLeftZero(String str, int length) {
		if (str==null||str=="") {
			str = "";
		}else {
			for (int i = 0; i < length; i++) {
				if (str.length() >= length) {
					break;
				}
				str = "0" + str;
			}

		}
		return str;
	}

	
	/**
	 * 转成integer型以后补零
	 * 
	 * @param str
	 * @param length
	 * @return
	 */

	public static String addRightZero(String str, int strLength) {
	    int strLen = str.length();
	    if (strLen < strLength) {
	        while (strLen < strLength) {
	            StringBuffer sb = new StringBuffer();
	            sb.append(str).append("0");//右补0
	            str = sb.toString();
	            strLen = str.length();
	        }
	    }
	
	    return str;
	}

	
	
	/**
	 * 按照指定长度分割字段
	 *  @param str 字段 
	 *  @param length 按照长度分割
	 * @return
	 */
	public static List<String> accordLengthSpilt(String str,int length){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < str.length() / length; i++) {
			list.add(str.substring(i * length, i * length + length));
		}
		
		return list;
	}
	
	/**
	 * 计数某字段在字符串中的个数
	 * @param str1 字符串
	 * @param str2 字段
	 * @return
	 */
	/**
	 * 在线设备的个数
	 */
	public static int getDeviceSize(String a, String b) {

		int sum = 0;
		while (a.indexOf(b) != -1) {
			String c = a.substring(0,b.length());
			if (c.equals(b)) {
				sum++;
			}
			a = a.substring(b.length());
		}
		return sum;
	}
	
	/**
	 * 获取在线设备端口编号
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static String getDeviceNumbers(String a, String b) {
		StringBuffer deviceNumbersSb = new StringBuffer();
		for (int i = 0; i < a.length(); i++) {
			if (b.equals(a.substring(i, i + 1))) {
				deviceNumbersSb.append(i + 1);
				deviceNumbersSb.append(",");
			}
		}
		if (deviceNumbersSb.toString().length()>0) {
			return deviceNumbersSb.toString().substring(0, deviceNumbersSb.toString().length() - 1);
		}
		return "";
	}
	
	
	/**
	 * 下挂设备位置二进制标识
	 * 0010
	 */
	
	public static String getDeviceBinaryIndex(String str){
		int strLen = str.length();
		StringBuffer sbBinary = new StringBuffer();
		for (int i = 0; i < strLen/4; i++) {
			sbBinary.append(addLeftZero(String.valueOf(Integer.toBinaryString(Integer.parseInt(str.substring(i*4,(i+1)*4), 16))), 16));
		}
		
		return sbBinary.toString();
	}
	
	/**
	 * 反向排列
	 */
	public static String getReverseArrange(String a) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < a.length(); i++) {
        	sb.append(a.substring(a.length()-(1+i),a.length()-i));
        	
		}
        return sb.toString();
	}
	

	/**
	 * 删除设备在父设备中的位置
	 * @param a 字符串
	 * @param b 指定字符串
	 * @param split 分隔符
	 * @return
	 */
	public static String deleteIndex(String a,String b,String split) {
		String newStr = "";
		StringBuffer newSb = new StringBuffer();
		
			String[] temp1 = a.split(split);
			for (int i = 0; i < temp1.length; i++) {
				if (!temp1[i].equals(b)) {
					newSb.append(temp1[i]+split);
				}
			}
		
			String[] temp2 = (newSb.toString().trim()).split(split);
			if (temp2.length>=1) {
				if (!"".equals(temp2[0])) {
					newStr =  (newSb.toString()).substring(0,newSb.toString().length()-1);
				}
			}
			
			return newStr;
	}
	
	

	/**
	 * 追加设备在父设备中的位置
	 * @param a 字符串
	 * @param b 指定字符串
	 * @param split 分隔符
	 * @return
	 */
	public static String addIndex(String a,String b,String split) {
		String newStr = "";
		String[] indexs = a.split(split);
		int count=0;
		for (int i = 0; i < indexs.length; i++) {
			if (indexs[i].equals(b)) {
				count++;
			}
		}
		//不包含b
		if (count==0) {
			//a中本身就没有值
			if (a.length()==0) {
				newStr=b;
			}
			//a本身有值
			else {
				a += (split+b);
				String[] indexs2 = a.split(",");
				    //从小到大排序
				for(int i=0;i<indexs2.length-1;i++){  
			        for(int j=0;j<indexs2.length-1-i;j++){
							if (Integer.parseInt(indexs2[j])>Integer.parseInt(indexs2[j+1])) {
								String temp;
								temp = indexs2[j];
								indexs2[j] = indexs2[j+1];
								indexs2[j+1] = temp;
							}
						}
						
					}
					//拼接成新值
					for (int i = 0; i < indexs2.length; i++) {
						newStr += (indexs2[i]+split);
					}
					newStr = newStr.substring(0,newStr.length()-1);
				}
			}
		   //a中包含b
		    else {
		    	newStr = a;
			}
		return newStr;
		
	}
	
	/**
	 * 创建时间
	 */
	public static String getCreateTime(){
		
		
		return (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
	}
	
	/**
	 * 创建时间
	 */
	public static String getTime(){
		
		
		return (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
	}
	
	/**
	 * 获取设备端口个数
	 * @param deviceType
	 * @return
	 */
	public static int getDevicePortSize(String deviceType){
		
		int portSize = 0;
		String deviceTypeSub = deviceType.substring(0,2);
		if ("F1".equals(deviceTypeSub)
				||"B4".equals(deviceTypeSub)
				||"B5".equals(deviceTypeSub)
				||"B3".equals(deviceTypeSub)
				||"B7".equals(deviceTypeSub)) 
		{
			portSize = 6;
		}else if ("B1".equals(deviceTypeSub)
				||"F2".equals(deviceTypeSub)
				||"B2".equals(deviceTypeSub)
				||"B6".equals(deviceTypeSub)) {
			
			portSize = 12;
		}else if ("F3".equals(deviceTypeSub)) {
			portSize = 16;
		}
		
		return portSize;
	}
	
	/**
	 * 判断一个字符串数组是否包含该字符串
	 */
	
	public static boolean isInclude(String[] str,String str2){
		
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals(str2)) {
				return true;
			}
		}
		return false;
	}
	
	public static void sleep(long time){
		
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
     
     /**
 	 * @param receiveData 收到的消息
 	 * @return
 	 */
 	public static String AnalysisReceiveMessageResult(byte[] receiveData) {
 		
 		String msg = "{";
 		for (int i = 0; i < receiveData.length; i++) {
 			if (i<receiveData.length-1) {
 				msg += (int)receiveData[i]+",";
			}else {
				msg += (int)receiveData[i];
			}
		}
 		msg +="}";
 		log.info("<==== 收到原始数据：" + msg);
 		
 		if (receiveData!=null && receiveData.length>0 ) {
	 		// 将byte转成16进制
	 		String receiveMessage = Utils.byteConvertToHex(receiveData).toUpperCase();
	 		// 7D 5E ==> 7E ,7D 5D ==> 7D
	 		receiveMessage = Utils.convertToSingleString(receiveMessage);
	 		// CRC校验  检查报文
	 		boolean checkMessage = Utils.checkReceiveMsg(receiveMessage);
	 		if (checkMessage) {
	 			return receiveMessage;
	 		}else {
	 			log.info("收到并CRC校验数据："+receiveMessage+" 失败");
	 		}
		}
 		
 		return "";
 	}
     
     public static String toStringHex(String s) {
 		if ("0x".equals(s.substring(0, 2))) {
 			s = s.substring(2);
 		}
 		byte[] baKeyword = new byte[s.length() / 2];
 		for (int i = 0; i < baKeyword.length; i++) {
 			try {
 				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
 						i * 2, i * 2 + 2), 16));
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 		}

 		try {
 			s = new String(baKeyword, "gbk");//UTF-16le:Not
 		} catch (Exception e1) {
 			e1.printStackTrace();
 		}
 		return s;
 	}
     
   /**
      * 转成16进制
      * @param data
      * @return
      */
     public static String byteConvertToHex(byte[] data) {
 		StringBuffer sb = new StringBuffer();

 		for (int i = 0; i < data.length; i++) {
 			String hexStr = Integer.toHexString(data[i] & 0xFF);
 			if (hexStr.length() == 1) {
 				hexStr = "0" + hexStr;
 				sb.append(hexStr);
 			} else {
 				sb.append(hexStr.toUpperCase());
 			}
 		}

 		return sb.toString();

 	}
     public static String getUUID(){

 		StringBuffer sb = new StringBuffer();
 		String uuid = String.valueOf(UUID.randomUUID());

 		String[] uuidSplit = uuid.split("-");

 		for(int i =0;i<uuidSplit.length;i++){
 			sb.append(uuidSplit[i]);

 		}
 		return sb.toString();
 	}
     
}
