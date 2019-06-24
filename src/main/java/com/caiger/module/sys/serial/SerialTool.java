package com.caiger.module.sys.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class SerialTool {
	
	public static SerialTool serialTool = null;
	
	 static SerialPort serialPort = null;
	
	static{
		if(serialTool==null){
			serialTool = new SerialTool();
		}
	}
	

	 
    /**
     * 打开串口
     * @param portName 端口名称
     * @param baudrate 波特率
     * @return 串口对象
     * @throws Exception 
     * @throws SerialPortParameterFailure 设置串口参数失败
     * @throws NotASerialPort 端口指向设备不是串口类型
     * @throws NoSuchPort 没有该端口对应的串口设备
     * @throws PortInUse 端口已被占用
     */
    public static final SerialPort openPort(String portName, int baudrate) throws Exception {

        try {

            //通过端口名识别端口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

            //打开端口，并给端口名字和一个timeout（打开操作的超时时间）
            CommPort commPort = portIdentifier.open(portName, 2000);

            //判断是不是串口
            if (commPort instanceof SerialPort) {
            	
                 serialPort = (SerialPort) commPort;
                
                try {                    	
                    //设置一下串口的波特率等参数
                    serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);                              
                } catch (UnsupportedCommOperationException e) {  
                	System.out.println("设置串口参数失败！打开串口操作未完成！");
                }
                System.out.println("打开串口 " + portName + " 成功 !");
            }        
            else {
            	//不是串口
            	 System.out.println("端口指向设备不是串口类型！打开串口操作失败！");
            }
        } catch (NoSuchPortException e1) {
        	 System.out.println("没有找到与该端口名匹配的串口设备！打开串口操作失败！");
        } catch (PortInUseException e2) {
        	 System.out.println("端口已被占用！打开串口操作失败！");
        }
        return serialPort;
    }
	
    
    /**
     * 关闭串口
     * @param serialport 待关闭的串口对象
     */
    public static void closePort(SerialPort serialPort) {
    	if (serialPort != null) {
    		serialPort.close();
    		serialPort = null;
    	}
    }
    
    
    /**
     * 往串口发送数据
     * @param serialPort 串口对象
     * @param order	待发送数据
     * @throws SendDataToSerialPortFailure 向串口发送数据失败
     * @throws SerialPortOutputStreamCloseFailure 关闭串口对象的输出流出错
     */
    public static void sendToPort(SerialPort serialPort, String order)  {

    	OutputStream out = null;
    	
        try {
            out = serialPort.getOutputStream();
            out.write(order.getBytes("GBK"), 0, order.getBytes("GBK").length);
            out.flush();
            
        } catch (IOException e) {
        	System.out.println("发送数据到端口失败！");
        } finally {
        	try {
        		if (out != null) {
        			out.close();
        			out = null;
        		}				
			} catch (IOException e) {
				System.out.println("端口关闭失败！");
			}
        }
        
    }
    
    /**
     * 从串口读取数据
     * @param serialPort 当前已建立连接的SerialPort对象
     * @return 读取到的数据
     * @throws ReadDataFromSerialPortFailure 从串口读取数据时出错
     * @throws SerialPortInputStreamCloseFailure 关闭串口对象输入流出错
     */
    public static byte[] readFromPort(SerialPort serialPort) {

    	InputStream in = null;
        byte[] bytes = null;

        try {
        	
        	in = serialPort.getInputStream();
        	int bufflenth = in.available();		//获取buffer里的数据长度
            
        	while (bufflenth != 0) {                             
                bytes = new byte[bufflenth];	//初始化byte数组为buffer中数据的长度
                in.read(bytes);
                bufflenth = in.available();
        	} 
        } catch (IOException e) {
        	System.out.println("从串口读取数据时出错！");
        } finally {
        	try {
            	if (in != null) {
            		in.close();
            		in = null;
            	}
        	} catch(IOException e) {
        		System.out.println("关闭串口对象输入流（InputStream）时出错！");
        	}

        }

        return bytes;

    }
    
    /**
     * 添加监听器
     * @param port     串口对象
     * @param listener 串口监听器
     * @throws TooManyListeners 监听类对象过多
     */
    public static void addListener(SerialPort port, SerialPortEventListener listener) {

        try {
        	
            //给串口添加监听器
            port.addEventListener(listener);
            //设置当有数据到达时唤醒监听接收线程
            port.notifyOnDataAvailable(true);
          //设置当通信中断时唤醒中断线程
            port.notifyOnBreakInterrupt(true);

        } catch (TooManyListenersException e) {
        	System.out.println("串口监听类数量过多！添加操作失败！");
        }
    }
    
    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
	

}
