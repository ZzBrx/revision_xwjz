package com.caiger.module.sys.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.caiger.module.sys.entity.Acredit;
import com.caiger.module.sys.entity.AlarmInfo;
import com.caiger.module.sys.entity.Analytic;
import com.caiger.module.sys.entity.Dse;
import com.caiger.module.sys.entity.PowerStatus;
import com.caiger.module.sys.entity.RecMsgEntity;
import com.caiger.module.sys.service.AlarmInfoService;

/**
 * 
 * @类功能说明： 常用工具 @类修改者： @修改日期： @修改说明：
 * 
 * @公司名称：常州太平通讯科技有限公司
 * @作者：kaijie huang
 * @创建时间：2017年5月10日 下午2:18:00 @版本：V1.0
 */
public class ToolUtil {

	/**
	 * 
	 * @方法功能说明： UUID生成 @创建：2017年5月23日 by huang kaijie @修改：日期 by
	 * 修改者 @修改内容： @参数： @return @return String @throws
	 */
	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

	/**
	 * 随机生成六位数验证码
	 * 
	 * @return
	 */
	public static int getRandomNum() {
		Random r = new Random();
		return r.nextInt(900000) + 100000;// (Math.random()*(999999-100000)+100000)
	}

	/**
	 * 检测字符串是否不为空(null,"","null")
	 * 
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s) {
		return s != null && !"".equals(s) && !"null".equals(s);
	}

	/**
	 * 检测字符串是否为空(null,"","null")
	 * 
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s) || "null".equals(s);
	}

	/**
	 * 字符串转换为字符串数组
	 * 
	 * @param str        字符串
	 * @param splitRegex 分隔符
	 * @return
	 */
	public static String[] str2StrArray(String str, String splitRegex) {
		if (isEmpty(str)) {
			return null;
		}
		return str.split(splitRegex);
	}

	/**
	 * 用默认的分隔符(,)将字符串转换为字符串数组
	 * 
	 * @param str 字符串
	 * @return
	 */
	public static String[] str2StrArray(String str) {
		return str2StrArray(str, ",\\s*");
	}

	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
	 * 
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String date2Str(Date date) {
		return date2Str(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date str2Date(String date) {
		if (notEmpty(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return new Date();
		} else {
			return null;
		}
	}

	/**
	 * 按照参数format的格式，日期转字符串
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2Str(Date date, String format) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} else {
			return "";
		}
	}

	/**
	 * 把时间根据时、分、秒转换为时间段
	 * 
	 * @param StrDate
	 */
	public static String getTimes(String StrDate) {
		String resultTimes = "";

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date now;

		try {
			now = new Date();
			java.util.Date date = df.parse(StrDate);
			long times = now.getTime() - date.getTime();
			long day = times / (24 * 60 * 60 * 1000);
			long hour = (times / (60 * 60 * 1000) - day * 24);
			long min = ((times / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long sec = (times / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

			StringBuffer sb = new StringBuffer();
			// sb.append("发表于：");
			if (hour > 0) {
				sb.append(hour + "小时前");
			} else if (min > 0) {
				sb.append(min + "分钟前");
			} else {
				sb.append(sec + "秒前");
			}

			resultTimes = sb.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return resultTimes;
	}

	/**
	 * 写txt里的单行内容
	 * 
	 * @param filePath 文件路径
	 * @param content  写入的内容
	 */
	public static void writeFile(String fileP, String content) {
		String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../"; // 项目路径
		filePath = (filePath.trim() + fileP.trim()).substring(6).trim();
		if (filePath.indexOf(":") != 1) {
			filePath = File.separator + filePath;
		}
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(content);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean checkMobileNumber(String mobileNumber) {
		boolean flag = false;
		try {
			Pattern regex = Pattern
					.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
			Matcher matcher = regex.matcher(mobileNumber);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 读取txt里的单行内容
	 * 
	 * @param filePath 文件路径
	 */
	public static String readTxtFile(String fileP) {
		try {

			String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../"; // 项目路径
			filePath = filePath.replaceAll("file:/", "");
			filePath = filePath.replaceAll("%20", " ");
			filePath = filePath.trim() + fileP.trim();
			if (filePath.indexOf(":") != 1) {
				filePath = File.separator + filePath;
			}
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding); // 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					return lineTxt;
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件,查看此路径是否正确:" + filePath);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
		}
		return "";
	}

	/**
	 * byte转成16进制
	 * 
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

	// 检查是否7E头7E尾
	public static boolean checkHeadTail(String msg) {
		if (msg.length() > 8 && (msg.startsWith("7E") || msg.startsWith("7e"))
				&& (msg.endsWith("7E") || msg.endsWith("7e"))) {
			return true;
		}
		return false;
	}

	// 取出需要校验的内容
	public static String getNeedCheckBody(String msg) {
		String needCheckBody = msg.substring(2, msg.length() - 6);
		return needCheckBody;
	}

	/**
	 * 检查CRC
	 * 
	 * @param
	 * 
	 * @return
	 */
	public static boolean checkCrc(String msg) {

		// 自己计算的CRC
		String crcResult = encryptCRC(getNeedCheckBody(msg));

		// 这个是报文中的CRC
		String checkStr = msg.substring(msg.length() - 6, msg.length() - 2);
		// 不区分大小写比较原始校验码与计算出的校验码是否一致
		if (crcResult.equalsIgnoreCase(checkStr)) {
			return true;
		}
		return false;
	}

	/**
	 * 接收时 7D 5E ==> 7E,7D 5D ==> 7D
	 */
	public static String convertToSingleString(String msg) {

		if (msg.length() >= 4) {

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
			return newStr.toString().toUpperCase();

		}
		return "";

	}

	/**
	 * 将数据CRC验证
	 * 
	 * @param msg  需要校验的数据 接收时是完整的一条报文，所以能够截取出CRC域
	 *             发送时需要先计算出CRC域再组装成一条报文，所以传入的参数只是供CRC计算的域的值
	 * @param type 是发送还是接收
	 * @return
	 */
	/**
	 * 将数据CRC验证
	 *
	 * @param msg
	 * @return
	 */
	public static String encryptCRC(String msg) {

		// 报文转成byte形式
		byte[] messageByte = resolveStringToByte(msg);

		return changeHighLow(
				addLeftZero(Integer.toHexString(CRC16.getCRC16(messageByte, messageByte.length)).toUpperCase(), 4));

	}

	/**
	 * 用于分解发送的报文组成新的byte数组
	 *
	 * @param str
	 * @return
	 */
	public static byte[] resolveStringToByte(String str) {

		byte[] byteMsg = new byte[str.length() / 2];

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
	 * 用于分解发送的报文组成新的byte数组
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static byte[] resolveStringToByte(String str, int length) {

		byte[] byteMsg = new byte[length];

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
	 * 转成integer型以后补零
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String addLeftZero(String str, int length) {
		if (str == null || str == "") {
			str = "";
		} else {
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
	 * 高低位转换
	 * 
	 * @param str
	 * @return
	 */
	public static String changeHighLow(String str) {
		int strLen = str.length();
		StringBuffer sbHighLow = new StringBuffer();
		for (int i = 0; i < strLen / 4; i++) {
			sbHighLow.append((str.substring(i * 4, (i + 1) * 4)).substring(2, 4)
					+ (str.substring(i * 4, (i + 1) * 4)).substring(0, 2));
		}

		return sbHighLow.toString();
	}

	/**
	 * 将数据发送时 7E ==> 7D 5E,7D ==> 7D 5D
	 */
	public static String convertToDoubleString(String msg) {

		if (msg != "") {
			String newMsg = msg.substring(2, msg.length() - 2);
			String[] str = new String[newMsg.length() / 2];

			for (int i = 0; i < newMsg.length() / 2; i++) {
				str[i] = newMsg.substring(i * 2, i * 2 + 2);
			}

			for (int i = 0; i < str.length; i++) {

				if ("7E".equalsIgnoreCase(str[i])) {
					str[i] = "7D5E";
				}
				if ("7D".equalsIgnoreCase(str[i])) {
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
			return newStr.toString().toUpperCase();
		}
		return "";
	}

	/**
	 * 下挂设备位置二进制标识 0010
	 */

	public static String getDeviceBinaryIndex(String str, int length) {
		StringBuffer sbBinary = new StringBuffer();
		sbBinary.append(addLeftZero(String.valueOf(Integer.toBinaryString(Integer.parseInt(str, 16))), length));

		return sbBinary.toString();
	}

	/**
	 * 反向排列
	 */
	public static String getReverseArrange(String a) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < a.length(); i++) {
			sb.append(a.substring(a.length() - (1 + i), a.length() - i));

		}
		return sb.toString();
	}

	/**
	 * 计数某字段在字符串中的个数
	 * 
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
			String c = a.substring(0, b.length());
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
		if (deviceNumbersSb.toString().length() > 0) {
			return deviceNumbersSb.toString().substring(0, deviceNumbersSb.toString().length() - 1);
		}
		return "";
	}

	// 分解收到的消息

	public static RecMsgEntity getMessage(String ip, int port, String info) {

		RecMsgEntity msg = new RecMsgEntity();

		// 帧头
		String fh = info.substring(0, 2);

		// 协议版本号
		String ver = info.substring(2, 6);

		// 报文系列号
		String sn = info.substring(6, 10);

		// 发送地址
		String add = info.substring(10, 34);

		// 命令码
		String cmdCode = info.substring(34, 38);

		// 命令状态码
		String statusCode = info.substring(38, 40);

		// 数据体
		String body = info.substring(40, info.length() - 6);

		// 检验位
		String checkBit = info.substring(info.length() - 6, info.length() - 2);

		// 帧尾
		String ft = info.substring(info.length() - 2, info.length());

		msg.setIp(ip);
		msg.setPort(port);
		msg.setFh(fh);
		msg.setVer(ver);
		msg.setSn(sn);
		msg.setAddressInfo(add);
		msg.setCmdCode(cmdCode);
		msg.setStatusCode(statusCode);
		msg.setBody(body);
		msg.setCheckBit(checkBit);
		msg.setFt(ft);

		return msg;

	}

	/*
	 * //分解收到的消息
	 * 
	 * public static ElockStatusEntity getElockStatusMessage(String info) {
	 * 
	 * ElockStatusEntity elockStatusEntity = new ElockStatusEntity(); String
	 * light_res = info.substring(0,2);
	 * 
	 * String spring_bolt = info.substring(2,4);
	 * 
	 * String travek_switch = info.substring(4,6);
	 * 
	 * String voltage_low = info.substring(6,8);
	 * 
	 * String over_current = info.substring(8,10);
	 * 
	 * String water_out = info.substring(10,12);
	 * 
	 * String voltageLow = info.substring(12,14); String voltageHigh =
	 * info.substring(14,16); String voltage =
	 * String.valueOf(Integer.parseInt(voltageHigh+voltageLow,16));
	 * 
	 * elockStatusEntity.setLight_res(light_res);
	 * elockStatusEntity.setSpring_bolt(spring_bolt);
	 * elockStatusEntity.setTravek_switch(travek_switch);
	 * elockStatusEntity.setVoltage_low(voltage_low);
	 * elockStatusEntity.setOver_current(over_current);
	 * elockStatusEntity.setWater_out(water_out);
	 * elockStatusEntity.setVoltage(voltage);
	 * 
	 * return elockStatusEntity;
	 * 
	 * }
	 */
	public static String getAlarmInfo(String alarmType, String voltage) {

		if ("LIGHT_RES".equals(alarmType)) {
			return "光敏告警";
		} else if ("VOLTAGE_LOW".equals(alarmType)) {
			return "电压低告警 当前电压：" + voltage + "mv";
		} else if ("WATER_OUT".equals(alarmType)) {
			return "水浸告警";
		}
		return "";
	}

	public static List<String> getDseStatusList(String content) {

		List<String> dseStatusList = new ArrayList<String>();

		String anti_thunder = String.valueOf(Integer.parseInt(content.substring(0, 2)));
		dseStatusList.add(anti_thunder);
		String electric_supply = String.valueOf(Integer.parseInt(content.substring(2, 4)));
		dseStatusList.add(electric_supply);
		String smoke = String.valueOf(Integer.parseInt(content.substring(4, 6)));
		dseStatusList.add(smoke);
		String entrance_guard = String.valueOf(Integer.parseInt(content.substring(6, 8)));
		dseStatusList.add(entrance_guard);
		String temperature_control = String.valueOf(Integer.parseInt(content.substring(8, 10)));
		dseStatusList.add(temperature_control);
		String water_out = String.valueOf(Integer.parseInt(content.substring(10, 12)));
		dseStatusList.add(water_out);
		String incline = String.valueOf(Integer.parseInt(content.substring(12, 14)));
		dseStatusList.add(incline);
		String temperature = content.substring(14, 18);
		dseStatusList.add(getAnalogValue(temperature));
		String humidity = content.substring(18, 22);
		dseStatusList.add(getAnalogValue(humidity));
		String ups_voltage = content.substring(22, 26);
		dseStatusList.add(getVoltageValue(ups_voltage));
		return dseStatusList;

	}

	public static String getAnalogValue(String value) {
		String str = ToolUtil.changeHighLow(value);
		return String.valueOf((double) (Integer.parseInt(str, 16)) / 10);

	}

	public static String getVoltageValue(String value) {
		String str = ToolUtil.changeHighLow(value);
		return String.valueOf((double) (Integer.parseInt(str, 16)) / 100);

	}

	/**
	 * string转unicode
	 * 
	 * @param string
	 * @return
	 */
	public static String string2Unicode(String string) {
		StringBuffer unicode = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			// 取出每一个字符
			char c = string.charAt(i);
			// 转换为unicode
			unicode.append(addLeftZero(Integer.toHexString(c), 4));
		}
		return unicode.toString();
	}

	public static String getMainStatus(String str) {

		if (str.equals("1")) {
			return "009";
		} else if (str.equals("2")) {
			return "008";
		} else if (str.equals("3")) {
			return "007";
		}
		return "010";
	}

	/**
	 * 每两个字节添加一个空格
	 * 
	 * @param msg
	 * @return
	 */
	public static String separateBlank(String msg) {

		msg = replaceAllBlank(msg);
		StringBuffer msgSb = new StringBuffer();

		for (int i = 0; i < msg.length() / 2; i++) {
			msgSb.append(msg.substring(i * 2, i * 2 + 2) + " ");
		}
		return msgSb.toString().trim();

	}

	/**
	 * 去除所有空格
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceAllBlank(String str) {
		return str.replaceAll(" ", "");
	}

	/**
	 * 解析方法
	 * 
	 * @param msgBody
	 * @return
	 */
	public static Analytic parseMessage(String msgBody) {

		Analytic analytic = new Analytic();
		String amaddress = msgBody.substring(0, 12);
		String quantity = keepDecimal(String.valueOf(Integer.parseInt(msgBody.substring(12, 20))), 2);
		String voltage = keepDecimal(String.valueOf(Integer.parseInt(msgBody.substring(20, 24))), 1);
		String current = keepDecimal(String.valueOf(Integer.parseInt(msgBody.substring(24, 30))), 3);
		String power = keepDecimal(String.valueOf(Integer.parseInt(msgBody.substring(30, 36))), 4);
		String softver = ToolUtil.changeHighLow(msgBody.substring(36, 40));
		String hardver = ToolUtil.changeHighLow(msgBody.substring(40, 44));
		String ratedpower = String.valueOf(Integer.parseInt(ToolUtil.changeHighLow(msgBody.substring(44, 48)), 16));
		String mainstatus = getMainStatus(
				String.valueOf(Integer.parseInt(ToolUtil.changeHighLow(msgBody.substring(48, 52)), 16)));
		String statusiden = isStatusAlarm(getDeviceBinaryIndex(ToolUtil.changeHighLow(msgBody.substring(52, 56)), 16));
		// *************状态标识
		Map<String, Map<String, String>> statusMap = getBitStatus("statusIden", "status",
				getReverseArrange(getDeviceBinaryIndex(ToolUtil.changeHighLow(msgBody.substring(52, 56)), 16)));
		String failureiden = isMalAlarm(getDeviceBinaryIndex(ToolUtil.changeHighLow(msgBody.substring(56, 60)), 16));
		// *************错误标识
		Map<String, Map<String, String>> malMap = getBitMal("malIden", "mal",
				getReverseArrange(getDeviceBinaryIndex(ToolUtil.changeHighLow(msgBody.substring(56, 60)), 16)));
		// 电源温度
		String powerTemperature = String.valueOf(Integer.parseInt(changeHighLow(msgBody.substring(60, 64)), 16));
		// AC输入功率
		String acInputPower = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(msgBody.substring(64, 68)), 16)),
				10);
		// 输出电压
		String outputVoltage = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(msgBody.substring(68, 72)), 16)),
				100);
		// 输出电流
		String outputCurrent = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(msgBody.substring(72, 76)), 16)),
				100);
		// 电池电流
		String batteryCurrent = keepPoint(
				String.valueOf(new BigInteger(changeHighLow(msgBody.substring(76, 80)), 16).shortValue()), 100);
		// 加热电流
		String heaterCurrent = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(msgBody.substring(80, 84)), 16)),
				100);
		// 负载电流
		String loadCurrent = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(msgBody.substring(84, 88)), 16)),
				100);
		;
		// 电池温度
//		String batteryTemperature = String
//				.valueOf(new BigInteger(changeHighLow(msgBody.substring(88, 92)), 16).shortValue());
		float format = ((float) (new BigInteger(changeHighLow(msgBody.substring(88, 92)), 16).shortValue()) / 100);
		String batteryTemperature = String.format("%.1f", format);
		// 目标电压
		String targetVoltage = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(msgBody.substring(92, 96)), 16)),
				100);
		// 额定电流
		String ratedCurrent = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(msgBody.substring(96, 100)), 16)),
				100);
		// 充电限流
		String currentLimit = keepPoint(
				String.valueOf(Integer.parseInt(changeHighLow(msgBody.substring(100, 104)), 16)), 100);
		// 电池放电时间
		String batteryDischargeTime = String.valueOf(Integer.parseInt(changeHighLow(msgBody.substring(104, 108)), 16));
		// 电池电量
		float data = (float) Integer.parseInt(changeHighLow(msgBody.substring(108, 112)), 16) / 2000 * 100;
		String batteryQuantity = String.valueOf(data < 0 ? "未合闸" : String.format("%.1f", data));

		String bmsiden = isBmsAlarm(getDeviceBinaryIndex(ToolUtil.changeHighLow(msgBody.substring(112, 116)), 16));
		// *************BMS标识
		Map<String, Map<String, String>> bmsMap = getBitBms("bmsIden", "bms",
				getReverseArrange(getDeviceBinaryIndex(ToolUtil.changeHighLow(msgBody.substring(112, 116)), 16)));
		// 防雷针
		String ptNeedle = String.valueOf(Integer.parseInt(msgBody.substring(116, 118), 16));
		// 停电告警
		String blackoutAlarm = String.valueOf(Integer.parseInt(msgBody.substring(118, 120), 16));
		if (ptNeedle.equals("0")) {
			ptNeedle = "012";
		} else {
			ptNeedle = "013";
		}

		if (blackoutAlarm.equals("0")) {
			blackoutAlarm = "005";
		} else {
			blackoutAlarm = "004";
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		analytic.setEmaddress(amaddress);
		analytic.setQuantity(quantity);
		analytic.setVoltage(voltage);
		analytic.setCurrent(current);
		analytic.setPower(power);
		analytic.setSoftver(softver);
		analytic.setHardver(hardver);
		analytic.setRatedpower(ratedpower);
		analytic.setMainstatus(mainstatus);
		analytic.setStatusiden(statusiden);
		analytic.setFailureiden(failureiden);
		analytic.setStatusMap(statusMap);
		analytic.setMalMap(malMap);
		analytic.setPowerTemperature(powerTemperature);
		analytic.setAcInputPower(acInputPower);
		analytic.setOutputVoltage(outputVoltage);
		analytic.setOutputCurrent(outputCurrent);
		analytic.setBatteryCurrent(batteryCurrent);
		analytic.setHeaterCurrent(heaterCurrent);
		analytic.setLoadCurrent(loadCurrent);
		analytic.setBatteryTemperature(batteryTemperature);
		analytic.setTargetVoltage(targetVoltage);
		analytic.setRatedCurrent(ratedCurrent);
		analytic.setCurrentLimit(currentLimit);
		analytic.setBatteryDischargeTime(batteryDischargeTime);
		analytic.setBatteryQuantity(batteryQuantity);
		analytic.setBmsiden(bmsiden);
		analytic.setBmsMap(bmsMap);
		analytic.setPtNeedle(ptNeedle);
		analytic.setBlackoutAlarm(blackoutAlarm);
//		analytic.setCreatedate(sdf.format(new Date()));

		return analytic;
	}

	public static String isStatusAlarm(String status) {
		String a = status.substring(15, 16);
		if (a.equals("0")) {
			return "013";
		}
		String b = status.substring(14, 15);
		String c = status.substring(13, 14);
		String d = status.substring(12, 13);
		String e = status.substring(11, 12);
		String f = status.substring(10, 11);
		String g = status.substring(9, 10);
		String h = status.substring(8, 9);
		String i = status.substring(7, 8);
		if (i.equals("0")) {
			return "013";
		}
		String j = status.substring(6, 7);
		String k = status.substring(5, 6);
		if (k.equals("1")) {
			return "013";
		}
		String l = status.substring(4, 5);
		if (l.equals("1")) {
			return "013";
		}
		String m = status.substring(3, 4);
		if (m.equals("0")) {
			return "013";
		}
		String n = status.substring(2, 3);
		if (n.equals("0")) {
			return "013";
		}
		String o = status.substring(1, 2);
		if (o.equals("0")) {
			return "013";
		}
		String p = status.substring(0, 1);
		if (p.equals("0")) {
			return "013";
		}

		return "012";

	}

	public static String isMalAlarm(String status) {
		String a = status.substring(0, 1);
		if (a.equals("1")) {
			return "013";
		}
		String b = status.substring(1, 2);
		if (b.equals("1")) {
			return "013";
		}
		String c = status.substring(2, 3);
		if (c.equals("1")) {
			return "013";
		}
		String d = status.substring(3, 4);
		if (d.equals("1")) {
			return "013";
		}
		String e = status.substring(4, 5);
		if (e.equals("1")) {
			return "013";
		}
		String f = status.substring(5, 6);
		if (f.equals("1")) {
			return "013";
		}
		String g = status.substring(6, 7);
		if (g.equals("1")) {
			return "013";
		}
		String h = status.substring(7, 8);
		if (h.equals("1")) {
			return "013";
		}
		String i = status.substring(8, 9);
		if (i.equals("1")) {
			return "013";
		}
		String j = status.substring(9, 10);
		if (j.equals("1")) {
			return "013";
		}
		String k = status.substring(10, 11);
		if (k.equals("1")) {
			return "013";
		}
		String l = status.substring(11, 12);
		if (l.equals("1")) {
			return "013";
		}
		String m = status.substring(12, 13);
		if (m.equals("1")) {
			return "013";
		}
		String n = status.substring(13, 14);
		if (n.equals("1")) {
			return "013";
		}
		String o = status.substring(14, 15);
		if (o.equals("1")) {
			return "013";
		}
		String p = status.substring(15, 16);
		if (p.equals("1")) {
			return "013";
		}

		return "012";
	}

	public static String isBmsAlarm(String status) {
		String a = status.substring(15, 16);
		if (a.equals("1")) {
			return "013";
		}
		String b = status.substring(14, 15);
		if (b.equals("1")) {
			return "013";
		}
		String c = status.substring(13, 14);
		if (c.equals("1")) {
			return "013";
		}
//		String d = status.substring(12, 13);
//		if (d.equals("1")) {
//			return "013";
//		}
		String e = status.substring(11, 12);
		if (e.equals("1")) {
			return "013";
		}
		String f = status.substring(10, 11);
		if (f.equals("1")) {
			return "013";
		}
		String g = status.substring(9, 10);
		if (g.equals("1")) {
			return "013";
		}
		String h = status.substring(8, 9);
		if (h.equals("1")) {
			return "013";
		}
		String i = status.substring(7, 8);
		if (i.equals("1")) {
			return "013";
		}
		String j = status.substring(6, 7);
		if (j.equals("1")) {
			return "013";
		}
		String k = status.substring(5, 6);
		if (k.equals("1")) {
			return "013";
		}
		String l = status.substring(4, 5);
		String m = status.substring(3, 4);
		if (m.equals("1")) {
			return "013";
		}
		String n = status.substring(2, 3);
		if (n.equals("0")) {
			return "013";
		}
		String o = status.substring(1, 2);
		if (o.equals("0")) {
			return "013";
		}
		String p = status.substring(0, 1);
		if (p.equals("0")) {
			return "013";
		}
		return "012";
	}

	public static Map<String, Map<String, String>> getBitStatus(String mainKey, String key, String status) {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

		Map<String, String> mapStatus = new HashMap<String, String>();

		String a = status.substring(0, 1).equals("1") ? "开机" : "停机";
		String b = status.substring(1, 2).equals("1") ? "交流" : "直流";
//		String c = status.substring(2, 3).equals("1") ? "保留" : "保留";
//		String d = status.substring(3, 4).equals("1") ? "保留" : "保留";
//		String e = status.substring(4, 5).equals("1") ? "保留" : "保留";
//		String f = status.substring(5, 6).equals("1") ? "保留" : "保留";
//		String g = status.substring(6, 7).equals("1") ? "保留" : "保留";
//		String h = status.substring(7, 8).equals("1") ? "保留" : "保留";
		String i = status.substring(8, 9).equals("1") ? "开机" : "停机";
//		String j = status.substring(9, 10).equals("1") ? "保留" : "保留";
		String k = status.substring(10, 11).equals("1") ? "错误" : "正常";
		String l = status.substring(11, 12).equals("1") ? "错误" : "正常";
		String m = status.substring(12, 13).equals("1") ? "降额" : "未降额";
		String n = status.substring(13, 14).equals("1") ? "降额" : "未降额";
		String o = status.substring(14, 15).equals("1") ? "错峰模式" : "非错峰";
		String p = status.substring(15, 16).equals("1") ? "已充满" : "未充满";

		mapStatus.put(key + "_d0", a);
		mapStatus.put(key + "_d1", b);
//		mapStatus.put(key + "_d2", c);
//		mapStatus.put(key + "_d3", d);
//		mapStatus.put(key + "_d4", e);
//		mapStatus.put(key + "_d5", f);
//		mapStatus.put(key + "_d6", g);
//		mapStatus.put(key + "_d7", h);
		mapStatus.put(key + "_d8", i);
//		mapStatus.put(key + "_d9", j);
		mapStatus.put(key + "_d10", k);
		mapStatus.put(key + "_d11", l);
		mapStatus.put(key + "_d12", m);
		mapStatus.put(key + "_d13", n);
		mapStatus.put(key + "_d14", o);
		mapStatus.put(key + "_d15", p);

		map.put(mainKey, mapStatus);

		return map;
	}

	public static Map<String, Map<String, String>> getBitMal(String mainKey, String key, String status) {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

		Map<String, String> mapStatus = new HashMap<String, String>();

		String a = status.substring(0, 1).equals("1") ? "故障" : "正常";
		String b = status.substring(1, 2).equals("1") ? "故障" : "正常";
		String c = status.substring(2, 3).equals("1") ? "故障" : "正常";
		String d = status.substring(3, 4).equals("1") ? "故障" : "正常";
		String e = status.substring(4, 5).equals("1") ? "故障" : "正常";
		String f = status.substring(5, 6).equals("1") ? "故障" : "正常";
		String g = status.substring(6, 7).equals("1") ? "故障" : "正常";
//		String h = status.substring(7, 8).equals("1") ? "故障" : "正常";
		String i = status.substring(8, 9).equals("1") ? "故障" : "正常";
		String j = status.substring(9, 10).equals("1") ? "故障" : "正常";
		String k = status.substring(10, 11).equals("1") ? "故障" : "正常";
		String l = status.substring(11, 12).equals("1") ? "故障" : "正常";
//		String m = status.substring(12, 13).equals("1") ? "保留" : "保留";
//		String n = status.substring(13, 14).equals("1") ? "保留" : "保留";
//		String o = status.substring(14, 15).equals("1") ? "保留" : "保留";
		String p = status.substring(15, 16).equals("1") ? "故障" : "正常";

		mapStatus.put(key + "_d0", a);
		mapStatus.put(key + "_d1", b);
		mapStatus.put(key + "_d2", c);
		mapStatus.put(key + "_d3", d);
		mapStatus.put(key + "_d4", e);
		mapStatus.put(key + "_d5", f);
		mapStatus.put(key + "_d6", g);
//		mapStatus.put(key + "_d7", h);
		mapStatus.put(key + "_d8", i);
		mapStatus.put(key + "_d9", j);
		mapStatus.put(key + "_d10", k);
		mapStatus.put(key + "_d11", l);
//		mapStatus.put(key + "_d12", m);
//		mapStatus.put(key + "_d13", n);
//		mapStatus.put(key + "_d14", o);
		mapStatus.put(key + "_d15", p);

		map.put(mainKey, mapStatus);

		return map;
	}

	public static Map<String, Map<String, String>> getBitBms(String mainKey, String key, String status) {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

		Map<String, String> mapStatus = new HashMap<String, String>();

		String a = status.substring(0, 1).equals("1") ? "运行" : "停机";
		String b = status.substring(1, 2).equals("1") ? "在位" : "不在位";
		String c = status.substring(2, 3).equals("1") ? "接通" : "未接通";
		String d = status.substring(3, 4).equals("1") ? "均充" : "浮充";
		String e = status.substring(4, 5).equals("1") ? "充电过流保护" : "正常";
		String f = status.substring(5, 6).equals("1") ? "放电过流保护" : "正常";
		String g = status.substring(6, 7).equals("1") ? "短路保护" : "正常";
		String h = status.substring(7, 8).equals("1") ? "MOS高温保护" : "正常";
		String i = status.substring(8, 9).equals("1") ? "高温保护" : "正常";
		String j = status.substring(9, 10).equals("1") ? "低温保护" : "正常";
		String k = status.substring(10, 11).equals("1") ? "Pack供电" : "正常";
		String l = status.substring(11, 12).equals("1") ? "故障" : "正常";
		String m = status.substring(12, 13).equals("1") ? "故障" : "正常";
//		String n = status.substring(13, 14).equals("1") ? "保留" : "保留";
//		String o = status.substring(14, 15).equals("1") ? "保留" : "保留";
		String p = status.substring(15, 16).equals("1") ? "故障" : "正常";

		mapStatus.put(key + "_d0", a);
		mapStatus.put(key + "_d1", b);
		mapStatus.put(key + "_d2", c);
		mapStatus.put(key + "_d3", d);
		mapStatus.put(key + "_d4", e);
		mapStatus.put(key + "_d5", f);
		mapStatus.put(key + "_d6", g);
		mapStatus.put(key + "_d7", h);
		mapStatus.put(key + "_d8", i);
		mapStatus.put(key + "_d9", j);
		mapStatus.put(key + "_d10", k);
		mapStatus.put(key + "_d11", l);
		mapStatus.put(key + "_d12", m);
//		mapStatus.put(key + "_d13", n);
//		mapStatus.put(key + "_d14", o);
		mapStatus.put(key + "_d15", p);

		map.put(mainKey, mapStatus);

		return map;
	}

	public static String keepDecimal(String str, int bit) {
		String result = str;
		if (str.length() >= bit) {
			String part1 = str.substring(0, str.length() - bit);
			String part2 = str.substring(str.length() - bit, str.length());
			if (part1.isEmpty()) {
				part1 = "0";
			}
			result = part1 + "." + part2;
		} else {
			result = "0." + addLeftZero(str, bit);
		}
		return result;
	}

	public static String getfSendTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String date = sdf.format(new Date());
		String year = addLeftZero(Integer.toHexString(Integer.parseInt(date.substring(0, 2))), 2);
		String month = addLeftZero(Integer.toHexString(Integer.parseInt(date.substring(2, 4))), 2);
		String day = addLeftZero(Integer.toHexString(Integer.parseInt(date.substring(4, 6))), 2);
		String hour = addLeftZero(Integer.toHexString(Integer.parseInt(date.substring(6, 8))), 2);
		String minute = addLeftZero(Integer.toHexString(Integer.parseInt(date.substring(8, 10))), 2);
		String second = addLeftZero(Integer.toHexString(Integer.parseInt(date.substring(10, 12))), 2);
		String fSendTime = year + month + day + hour + minute + second;

		return fSendTime;
	}

	// 开关电源状态
	public static PowerStatus getPowerStatus(String str) {

		PowerStatus status = new PowerStatus();
		String powertemp = String.valueOf(Integer.parseInt(changeHighLow(str.substring(0, 4)), 16));
		String acinputpower = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(str.substring(4, 8)), 16)), 10);
		String retain = changeHighLow(str.substring(8, 12));
		String outputvoltage = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(str.substring(12, 16)), 16)),
				100);
		String outputelectricity = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(str.substring(16, 20)), 16)),
				100);
		String batteryelectricity = keepPoint(
				String.valueOf(new BigInteger(changeHighLow(str.substring(20, 24)), 16).shortValue()), 100);
		String heatelectricity = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(str.substring(24, 28)), 16)),
				100);
		String loadelectricity = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(str.substring(28, 32)), 16)),
				100);
		String batterytemp = String.valueOf(new BigInteger(changeHighLow(str.substring(32, 36)), 16).shortValue());
		String targetvoltage = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(str.substring(36, 40)), 16)),
				100);
		String ratedelectricity = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(str.substring(40, 44)), 16)),
				100);
		String limitcharge = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(str.substring(44, 48)), 16)), 100);
		String batterydischargetime = String.valueOf(Integer.parseInt(changeHighLow(str.substring(48, 52)), 16));

		status.setPowerTemp(powertemp);
		status.setAcInputPower(acinputpower);
		status.setRetain(retain);
		status.setOutputVoltage(outputvoltage);
		status.setOutputElectricity(outputelectricity);
		status.setBatteryElectricity(batteryelectricity);
		status.setHeatElectricity(heatelectricity);
		status.setLoadElectricity(loadelectricity);
		status.setBatteryTemp(batterytemp);
		status.setTargetVoltage(targetvoltage);
		status.setRatedElectricity(ratedelectricity);
		status.setLimitCharge(limitcharge);
		status.setBatteryDischargetime(batterydischargetime);

		return status;

	}

	public static String keepPoint(String str, int multiple) {

		BigDecimal bg = new BigDecimal(str);
		String d = String.valueOf(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() / multiple);
		return d;

	}

	/**
	 * 解析交流开关电源状态
	 * 
	 * @param str
	 */
	public static Map<String, Map<String, String>> resolverPowerAcStatus(String mainKey, String key, String status) {

		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		Map<String, String> mapStatus = new HashMap<String, String>();
		String status01 = changeHighLow(status.substring(0, 4));
		String status02 = changeHighLow(status.substring(4, 8));
		String status03 = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(status.substring(8, 12)), 16)), 100);
		String status04 = keepPoint(
				String.valueOf(new BigInteger(changeHighLow(status.substring(12, 16)), 16).shortValue()), 100);
		String status05 = String.valueOf(new BigInteger(changeHighLow(status.substring(16, 20)), 16).shortValue());
		String status06 = String.valueOf(Integer.parseInt(changeHighLow(status.substring(20, 24)), 16));
		String status07 = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(status.substring(24, 28)), 16)), 100);
		String status08 = String.valueOf(Integer.parseInt(changeHighLow(status.substring(28, 32)), 16));
		String status09 = String.valueOf(Integer.parseInt(changeHighLow(status.substring(32, 36)), 16));
		String status10 = keepPoint(String.valueOf(Integer.parseInt(changeHighLow(status.substring(36, 40)), 16)), 100);
		String status11 = String.valueOf(Integer.parseInt(changeHighLow(status.substring(40, 44)), 16));

		mapStatus.put(key + "_01", status01);
		mapStatus.put(key + "_02", status02);
		mapStatus.put(key + "_03", status03);
		mapStatus.put(key + "_04", status04);
		mapStatus.put(key + "_05", status05);
		mapStatus.put(key + "_06", status06);
		mapStatus.put(key + "_07", status07);
		mapStatus.put(key + "_08", status08);
		mapStatus.put(key + "_09", status09);
		mapStatus.put(key + "_10", status10);
		mapStatus.put(key + "_11", status11);

		map.put(mainKey, mapStatus);
		return map;

	}

	/**
	 * 解析交流开关电源状态
	 * 
	 * @param str
	 */
	public static Map<String, Map<String, String>> resolverPowerAcStatus1(String mainKey, String key, String status) {

		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		Map<String, String> mapStatus = new HashMap<String, String>();
		String status01 = status.substring(0, 1);
		String status02 = status.substring(1, 2);
		String status03 = status.substring(2, 3);
		String status04 = status.substring(3, 4);
		String status05 = status.substring(4, 5);
		String status06 = status.substring(5, 6);
		String status07 = status.substring(6, 7);
		String status08 = status.substring(7, 8);

		mapStatus.put(key + "_01", status01);
		mapStatus.put(key + "_02", status02);
		mapStatus.put(key + "_03", status03);
		mapStatus.put(key + "_04", status04);
		mapStatus.put(key + "_05", status05);
		mapStatus.put(key + "_06", status06);
		mapStatus.put(key + "_07", status07);
		mapStatus.put(key + "_08", status08);

		map.put(mainKey, mapStatus);
		return map;

	}

	public static String isPowerAc1Error(Map<String, Map<String, String>> map) {
		if (!map.get("powerAc1").get("status_01").equals("1")) {
			return "1";
		}

		if (!map.get("powerAc1").get("status_04").equals("1")) {
			return "1";
		}

		if (!map.get("powerAc1").get("status_05").equals("1")) {
			return "1";
		}

		if (!map.get("powerAc1").get("status_06").equals("1")) {
			return "1";
		}

		if (!map.get("powerAc1").get("status_07").equals("1")) {
			return "1";
		}

		return "0";
	}

	public static String isPowerAc2Error(Map<String, Map<String, String>> map) {

		if (!map.get("powerAc2").get("status_01").equals("1")) {
			return "1";
		}
		if (!map.get("powerAc2").get("status_02").equals("1")) {
			return "1";
		}
		if (!map.get("powerAc2").get("status_03").equals("1")) {
			return "1";
		}

		if (!map.get("powerAc2").get("status_05").equals("1")) {
			return "1";
		}
		if (!map.get("powerAc2").get("status_06").equals("1")) {
			return "1";
		}

		return "0";
	}

	public static String isPowerAc3Error(Map<String, Map<String, String>> map) {
		if (!map.get("powerAc3").get("status_01").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc3").get("status_02").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc3").get("status_03").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc3").get("status_04").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc3").get("status_05").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc3").get("status_06").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc3").get("status_07").equals("0")) {
			return "1";
		}

		return "0";
	}

	public static String isPowerAc4Error(Map<String, Map<String, String>> map) {
		if (!map.get("powerAc4").get("status_01").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc4").get("status_02").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc4").get("status_03").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc4").get("status_04").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc4").get("status_05").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc4").get("status_06").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc4").get("status_07").equals("0")) {
			return "1";
		}

		return "0";
	}

	public static String isPowerAc5Error(Map<String, Map<String, String>> map) {
		if (!map.get("powerAc5").get("status_01").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc5").get("status_02").equals("0")) {
			return "1";
		}
		if (!map.get("powerAc5").get("status_03").equals("0")) {
			return "1";
		}

		return "0";
	}

	public static void judgeAlarmStatus(Analytic analytic, AlarmInfo alarmInfo, Dse dse, SimpleDateFormat format,
			AlarmInfoService alarmInfoService) throws ParseException {
		String alarmInfoFlag = "0";
		String blackoutAlarm = analytic.getBlackoutAlarm(); // 停电告警
		String mainstatus = analytic.getMainstatus(); // 主运行状态
		String ptNeedle = analytic.getPtNeedle(); // 避雷器告警
		String statusiden = analytic.getStatusiden(); // 状态标志组
		String failureiden = analytic.getFailureiden(); // 故障标志组
		String bmsiden = analytic.getBmsiden(); // BMS标志组

		alarmInfo.setDev_id(dse.getId());

		// 市电告警
		if (blackoutAlarm.equals("004")) {
			alarmInfo.setAlarmType("003");
			alarmInfo.setAlarmInfo(blackoutAlarm);
			alarmInfoFlag = "1";
			alarmInfo.setAlarmFlag(alarmInfoFlag);
			// 查找alarmInfo表中是否已存在该告警
			AlarmInfo returnInfo = alarmInfoService.get(alarmInfo);
			if (returnInfo == null) {
				String alarmInfo_id = ToolUtil.get32UUID();
				alarmInfo.setId(alarmInfo_id);
				alarmInfo.setStartTime(dse.getCreateDate());
//				alarmInfo.setStartTime(format.parse(analytic.getCreatedate()));
				// 如果不存在该告警，则保存
				alarmInfoService.savestatus(alarmInfo);
			}
		} else if (blackoutAlarm.equals("005")) {
			// 如果此时数据库中的alarmInfo为 004 ，则更新alarmFlag 和 结束时间
			alarmInfo.setAlarmType("003");
			alarmInfo.setAlarmInfo("004");
			alarmInfoFlag = "1";
			alarmInfo.setAlarmFlag(alarmInfoFlag);
			// 查找alarmInfo表中是否已存在该告警
			AlarmInfo returnInfo = alarmInfoService.get(alarmInfo);
			if (returnInfo != null) {
				alarmInfo.setId(returnInfo.getId());
				alarmInfo.setAlarmFlag("0");
				alarmInfo.setEndTime(dse.getCreateDate());
//				alarmInfo.setEndTime(format.parse(analytic.getCreatedate()));
				// 如果存在，则更新
				alarmInfoService.updateById(alarmInfo);
			}
		}

		// 防雷器告警
		if (ptNeedle.equals("013")) {
			alarmInfo.setAlarmType("002");
			alarmInfo.setAlarmInfo(ptNeedle);
			alarmInfoFlag = "1";
			alarmInfo.setAlarmFlag(alarmInfoFlag);
			// 查找alarmInfo表中是否已存在该告警
			AlarmInfo returnInfo = alarmInfoService.get(alarmInfo);
			if (returnInfo == null) {
				String alarmInfo_id = ToolUtil.get32UUID();
				alarmInfo.setId(alarmInfo_id);
				alarmInfo.setStartTime(dse.getCreateDate());
//				alarmInfo.setStartTime(format.parse(analytic.getCreatedate()));
				// 如果不存在该告警，则保存
				alarmInfoService.savestatus(alarmInfo);
			}
		} else if (ptNeedle.equals("012")) {
			alarmInfo.setAlarmType("002");
			alarmInfo.setAlarmInfo("013");
			alarmInfoFlag = "1";
			alarmInfo.setAlarmFlag(alarmInfoFlag);
			// 查找alarmInfo表中是否已存在该告警
			AlarmInfo returnInfo = alarmInfoService.get(alarmInfo);
			if (returnInfo != null) {
				alarmInfo.setId(returnInfo.getId());
				alarmInfo.setAlarmFlag("0");
				alarmInfo.setEndTime(dse.getCreateDate());
//				alarmInfo.setEndTime(format.parse(analytic.getCreatedate()));
				// 如果存在，则更新
				alarmInfoService.updateById(alarmInfo);
			}
		}

		// 状态标志组异常
		if (statusiden.equals("013")) {
			alarmInfo.setAlarmType("015");
			alarmInfo.setAlarmInfo(statusiden);
			alarmInfoFlag = "1";
			alarmInfo.setAlarmFlag(alarmInfoFlag);
			// 查找alarmInfo表中是否已存在该告警
			AlarmInfo returnInfo = alarmInfoService.get(alarmInfo);
			if (returnInfo == null) {
				String alarmInfo_id = ToolUtil.get32UUID();
				alarmInfo.setId(alarmInfo_id);
				alarmInfo.setStartTime(dse.getCreateDate());
//				alarmInfo.setStartTime(format.parse(analytic.getCreatedate()));
				// 如果不存在该告警，则保存
				alarmInfoService.savestatus(alarmInfo);
			}
		} else if (statusiden.equals("012")) {
			alarmInfo.setAlarmType("015");
			alarmInfo.setAlarmInfo("013");
			alarmInfoFlag = "1";
			alarmInfo.setAlarmFlag(alarmInfoFlag);
			// 查找alarmInfo表中是否已存在该告警
			AlarmInfo returnInfo = alarmInfoService.get(alarmInfo);
			if (returnInfo != null) {
				alarmInfo.setId(returnInfo.getId());
				alarmInfo.setAlarmFlag("0");
				alarmInfo.setEndTime(dse.getCreateDate());
//				alarmInfo.setEndTime(format.parse(analytic.getCreatedate()));
				// 如果存在，则更新
				alarmInfoService.updateById(alarmInfo);
			}
		}

		// 故障标志组异常
		if (failureiden.equals("013")) {
			alarmInfo.setAlarmType("016");
			alarmInfo.setAlarmInfo(failureiden);
			alarmInfoFlag = "1";
			alarmInfo.setAlarmFlag(alarmInfoFlag);
			// 查找alarmInfo表中是否已存在该告警
			AlarmInfo returnInfo = alarmInfoService.get(alarmInfo);
			if (returnInfo == null) {
				String alarmInfo_id = ToolUtil.get32UUID();
				alarmInfo.setId(alarmInfo_id);
				alarmInfo.setStartTime(dse.getCreateDate());
//				alarmInfo.setStartTime(format.parse(analytic.getCreatedate()));
				// 如果不存在该告警，则保存
				alarmInfoService.savestatus(alarmInfo);
			}
		} else if (failureiden.equals("012")) {
			alarmInfo.setAlarmType("016");
			alarmInfo.setAlarmInfo("013");
			alarmInfoFlag = "1";
			alarmInfo.setAlarmFlag(alarmInfoFlag);
			// 查找alarmInfo表中是否已存在该告警
			AlarmInfo returnInfo = alarmInfoService.get(alarmInfo);
			if (returnInfo != null) {
				alarmInfo.setId(returnInfo.getId());
				alarmInfo.setAlarmFlag("0");
				alarmInfo.setEndTime(dse.getCreateDate());
//				alarmInfo.setEndTime(format.parse(analytic.getCreatedate()));
				// 如果存在，则更新
				alarmInfoService.updateById(alarmInfo);
			}
		}

		// BMS标志组异常
		if (bmsiden.equals("013")) {
			alarmInfo.setAlarmType("017");
			alarmInfo.setAlarmInfo(bmsiden);
			alarmInfoFlag = "1";
			alarmInfo.setAlarmFlag(alarmInfoFlag);
			// 查找alarmInfo表中是否已存在该告警
			AlarmInfo returnInfo = alarmInfoService.get(alarmInfo);
			if (returnInfo == null) {
				String alarmInfo_id = ToolUtil.get32UUID();
				alarmInfo.setId(alarmInfo_id);
				alarmInfo.setStartTime(dse.getCreateDate());
//				alarmInfo.setStartTime(format.parse(analytic.getCreatedate()));
				// 如果不存在该告警，则保存
				alarmInfoService.savestatus(alarmInfo);
			}
		} else if (bmsiden.equals("012")) {
			alarmInfo.setAlarmType("017");
			alarmInfo.setAlarmInfo("013");
			alarmInfoFlag = "1";
			alarmInfo.setAlarmFlag(alarmInfoFlag);
			// 查找alarmInfo表中是否已存在该告警
			AlarmInfo returnInfo = alarmInfoService.get(alarmInfo);
			if (returnInfo != null) {
				alarmInfo.setId(returnInfo.getId());
				alarmInfo.setAlarmFlag("0");
				alarmInfo.setEndTime(dse.getCreateDate());
//				alarmInfo.setEndTime(format.parse(analytic.getCreatedate()));
				// 如果存在，则更新
				alarmInfoService.updateById(alarmInfo);
			}
		}

		// 主运行状态告警
		if (mainstatus.equals("007") || mainstatus.equals("008")) {
			alarmInfo.setAlarmType("001");
			alarmInfo.setAlarmInfo(mainstatus);
			alarmInfoFlag = "1";
			alarmInfo.setAlarmFlag(alarmInfoFlag);
			// 查找alarmInfo表中是否已存在该告警
			AlarmInfo returnInfo = alarmInfoService.get(alarmInfo);
			if (returnInfo == null) {
				String alarmInfo_id = ToolUtil.get32UUID();
				alarmInfo.setId(alarmInfo_id);
				alarmInfo.setStartTime(dse.getCreateDate());
//				alarmInfo.setStartTime(format.parse(analytic.getCreatedate()));
				// 如果不存在该告警，则保存
				alarmInfoService.savestatus(alarmInfo);
			}
		} else if (mainstatus.equals("009") || mainstatus.equals("010")) {
			// 如果此时数据库中的alarmInfo为 007或者008 ，则更新alarmFlag 和 结束时间
			alarmInfo.setAlarmType("001");
			alarmInfo.setAlarmFlag("1");

			String[] AlarmInfo = { "007", "008" };
			for (String alarmInfoType : AlarmInfo) {
				alarmInfo.setAlarmInfo(alarmInfoType);
				// 查找alarmInfo表中是否已存在该告警
				AlarmInfo returnInfo = alarmInfoService.get(alarmInfo);

				if (returnInfo != null) {
					alarmInfo.setId(returnInfo.getId());
					alarmInfo.setAlarmFlag("0");
					alarmInfo.setEndTime(dse.getCreateDate());
//					alarmInfo.setEndTime(format.parse(analytic.getCreatedate()));
					// 如果存在，则更新
					alarmInfoService.updateById(alarmInfo);
				}
			}
		}
	}

	/**
	 * 通过value拿到key
	 * 
	 * @param map
	 * @param value
	 * @return
	 */
	public static String getKey(Map map, Object value) {
		Set set = map.entrySet(); // 通过entrySet()方法把map中的每个键值对变成对应成Set集合中的一个对象
		Iterator<Map.Entry<Object, Object>> iterator = set.iterator();
		StringBuffer key = new StringBuffer();
		while (iterator.hasNext()) {
			// Map.Entry是一种类型，指向map中的一个键值对组成的对象
			Map.Entry<Object, Object> entry = iterator.next();
			if (entry.getValue().equals(value)) {
				key.append(entry.getKey());
			}
		}
		return key.toString();
	}

	public static List<Acredit> getFiltrateUnauthorizedDevList(List<Acredit> acreditList, Acredit acredit) {
		if (acreditList.size() > 0 && !acredit.getEmaddress().equals("")) {

			for (int i = 0; i <= acreditList.size(); i++) {
				if (!acreditList.get(i).getEmaddress().equals(acredit.getEmaddress())) {
					acreditList.remove(i);
				}
			}

		}

		return acreditList;
	}
}
