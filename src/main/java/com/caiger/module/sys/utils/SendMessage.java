package com.caiger.module.sys.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.caiger.module.sys.entity.SendMsgEntity;

public class SendMessage
{
  public static void send(DatagramSocket datagramSocket, SendMsgEntity sendMsgEntity)
  {
    String ip = sendMsgEntity.getIp();
    
    int port = sendMsgEntity.getPort();
    
    String fh = sendMsgEntity.getFh();
    
    String ver = sendMsgEntity.getVer();
    
    String sn = sendMsgEntity.getSn();
    
    String addressInfo = sendMsgEntity.getAddressInfo();
    
    String cmdCode = sendMsgEntity.getCmdCode();
    
    String statusCode = sendMsgEntity.getStatusCode();
    
    String body = sendMsgEntity.getBody();
    
    String ft = sendMsgEntity.getFt();
    
    String encryptBody = ver + sn + addressInfo + cmdCode + statusCode + body;
    
    String crcCode = ToolUtil.encryptCRC(encryptBody);
    String sendMessage = fh + encryptBody + crcCode + ft;
    
    String sendMsg = ToolUtil.convertToDoubleString(sendMessage.toString());
    
    byte[] sendMsgByte = ToolUtil.resolveStringToByte(sendMsg);
    System.out.println("发送目标IP地址：" + ip + ";端口号:" + port + ";数据：" + sendMsg);
    
    processSend(datagramSocket, ip, port, sendMsgByte);
  }
  
  public static void processSend(DatagramSocket datagramSocket, String ip, int port, byte[] buf)
  {
    try
    {
      InetAddress address = InetAddress.getByName(ip);
      DatagramPacket sendDatagramPacket = new DatagramPacket(buf, buf.length, address, port);
      datagramSocket.send(sendDatagramPacket);
    } catch (SocketException ex) {
      ex.printStackTrace();
    }
    catch (IOException localIOException) {}
  }
}
