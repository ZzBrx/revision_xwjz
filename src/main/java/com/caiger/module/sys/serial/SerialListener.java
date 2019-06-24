package com.caiger.module.sys.serial;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;


/**
 * 以内部类形式创建一个串口监听类
 * @author zhong
 *
 */
public class SerialListener implements SerialPortEventListener {
	
	public SerialPort serialPort = null;
	
	public StringBuffer sbMsg = new StringBuffer();
	
	public String dataOriginal;
	
	
	public SerialListener(SerialPort serialPort ){
		this.serialPort = serialPort;
		
	}


	public String getDataOriginal() {
		if (dataOriginal==null) {
			dataOriginal = "";
		}
		return dataOriginal;
	}

	public void setDataOriginal(String dataOriginal) {
		this.dataOriginal = dataOriginal;
	}

	public void serialEvent(SerialPortEvent arg0) {
    	
        switch (arg0.getEventType()) {

            case SerialPortEvent.BI: // 10 通讯中断
            	System.out.println("与串口设备通讯中断");
            	break;

            case SerialPortEvent.OE: // 7 溢位（溢出）错误

            case SerialPortEvent.FE: // 9 帧错误

            case SerialPortEvent.PE: // 8 奇偶校验错误

            case SerialPortEvent.CD: // 6 载波检测

            case SerialPortEvent.CTS: // 3 清除待发送数据

            case SerialPortEvent.DSR: // 4 待发送数据准备好了

            case SerialPortEvent.RI: // 5 振铃指示

            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
            	break;
            
            case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
            
				byte[] data = null;
				
				try {
					if (serialPort == null) {
						System.out.println("串口对象为空！监听失败！");
					}
					else {
						data = SerialTool.readFromPort(serialPort);	//读取数据，存入字节数组
						//自定义解析过程
						if (data == null || data.length < 1) {	//检查数据是否读取正确	
							System.out.println("读取数据过程中未获取到有效数据！请检查设备或程序！");
							break;
						}
						else {
							String serialData = new String(data,"GBK");	//将字节数组数据转换位为保存了原始数据的字符串
							String recData = serialData.trim();
							System.out.println("串口收到的数据"+recData);
							if ((recData.equals("SMS_SEND")||recData.equals("SMS_SEND_SUCESS")||recData.equals("SMS_SEND_FAIL"))&&sbMsg.toString().isEmpty()) {
									sbMsg.append(recData);
							}else {
								if (sbMsg.toString().equals("SMS_SEND")&&("_SUCESS".equals(recData.trim())||"_FAIL".equals(recData))) {
									sbMsg.append(recData);
								}
							}
							
						    if (sbMsg.toString().trim().equals("SMS_SEND_SUCESS")||sbMsg.toString().trim().equals("SMS_SEND_FAIL")) {
						    	setDataOriginal(sbMsg.toString().trim());
						    	System.out.println("DataOriginal的数据:"+getDataOriginal());
								sbMsg = new StringBuffer();
							}
							
						}
					}						
					
				} catch (Exception e) {
					System.out.println("从串口读取数据时出错！");
				}	
	            
				break;

        }

    }


    }


