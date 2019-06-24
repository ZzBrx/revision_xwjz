package com.caiger.module.sys.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import com.caiger.common.web.BaseController;
import com.caiger.module.sys.entity.Acredit;
import com.caiger.module.sys.entity.AlarmInfo;
import com.caiger.module.sys.entity.Analytic;
import com.caiger.module.sys.entity.BmsIden;
import com.caiger.module.sys.entity.Dse;
import com.caiger.module.sys.entity.MalIden;
import com.caiger.module.sys.entity.RecMsgEntity;
import com.caiger.module.sys.entity.SendMsgEntity;
import com.caiger.module.sys.entity.Sms;
import com.caiger.module.sys.entity.StatusIden;
import com.caiger.module.sys.entity.UpdateData;
import com.caiger.module.sys.entity.UpdateFlag;
import com.caiger.module.sys.entity.User;
import com.caiger.module.sys.serial.SerialListener;
import com.caiger.module.sys.serial.SerialTool;
import com.caiger.module.sys.service.AlarmInfoService;
import com.caiger.module.sys.service.BmsIdenService;
import com.caiger.module.sys.service.DseService;
import com.caiger.module.sys.service.MalIdenService;
import com.caiger.module.sys.service.StatusIdenService;
import com.caiger.module.sys.service.UpdateDataService;
import com.caiger.module.sys.service.UpdateFlagService;
import com.caiger.module.sys.service.UserService;
import com.caiger.module.sys.utils.AlarmType;
import com.caiger.module.sys.utils.RetrySendThread;
import com.caiger.module.sys.utils.SendMessage;
import com.caiger.module.sys.utils.ToolUtil;

import gnu.io.SerialPort;

@Controller
public class DseUdpController extends BaseController {

	public Logger logger = Logger.getLogger(DseUdpController.class);

	private DatagramSocket datagramSocket = null;
	private DatagramPacket receiveDatagramPacket = null;

	private static List<Sms> smsList = new LinkedList<Sms>();

	SendMsgEntity sendMsgEntity = new SendMsgEntity();

	StatusIden sid = new StatusIden();

	MalIden mid = new MalIden();

	BmsIden bms = new BmsIden();

//	AlarmInfo alarmInfo = new AlarmInfo();
	
	private RetrySendThread thread = new RetrySendThread();

	private SerialListener serialListener = null;

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private List<RecMsgEntity> receiveMessageList = new LinkedList<RecMsgEntity>();

	@Autowired
	private DseService dseService;

	@Autowired
	private StatusIdenService statusIdenService;

	@Autowired
	private MalIdenService malIdenService;

	@Autowired
	private BmsIdenService bmsIdenService;

	@Autowired
	private AlarmInfoService alarmInfoService;
	
	@Autowired
	private UpdateFlagService updateFlagService;
	
	@Autowired
	private UpdateDataService updateDataService;

	@Autowired
	private UserService userService;

//	@Autowired
//	private Dse dse;

//	@Autowired
//	private AlarmInfo alarmInfo;

	@Autowired
	private Acredit acredit;

	public DseUdpController() {
		ReceiveMessageThread receiveThread = new ReceiveMessageThread();
		receiveThread.start();
		ProcessMessageThread processMessageThread = new ProcessMessageThread();
		processMessageThread.start();
		ClearLinkThread clearLinkThread = new ClearLinkThread();
		clearLinkThread.start();
//		SendSerialThread sendSerialThread = new SendSerialThread();
//		sendSerialThread.start();
//		OnlineThread onlineThread = new OnlineThread();
//		onlineThread.start();
	}

	public class ReceiveMessageThread extends Thread {
		public ReceiveMessageThread() {
		}

		public void run() {
			try {
				while (true) {
					sleep(10);
					if (datagramSocket == null) {
						datagramSocket = new DatagramSocket(9002);
					}

					byte[] buf = new byte[1024];

					receiveDatagramPacket = new DatagramPacket(buf, buf.length);

					datagramSocket.receive(receiveDatagramPacket);

					String ip = receiveDatagramPacket.getAddress().getHostAddress();

					int port = receiveDatagramPacket.getPort();

					byte[] receiveData = Arrays.copyOf(receiveDatagramPacket.getData(),
							receiveDatagramPacket.getLength());

					String receiveMessage = ToolUtil.byteConvertToHex(receiveData).toUpperCase();
					logger.info("收到数据 IP地址：" + ip + ";端口号：" + port + ";消息：" + receiveMessage);
					System.out.println("收到数据 IP地址：" + ip + ";端口号：" + port + ";消息：" + receiveMessage);

					if (ToolUtil.checkHeadTail(receiveMessage)) {
						receiveMessage = ToolUtil.convertToSingleString(receiveMessage);
						if (ToolUtil.checkCrc(receiveMessage)) {
							RecMsgEntity msg = ToolUtil.getMessage(ip, port, receiveMessage);
							synchronized (receiveMessageList) {
								receiveMessageList.add(msg);
								receiveMessageList.notifyAll();
							}
						} else {
							logger.info("收到数据 IP地址：" + ip + ";端口号：" + port + ";消息CRC检验失败！");
							System.out.println("收到数据 IP地址：" + ip + ";端口号：" + port + ";消息CRC检验失败！");
						}
					} else {
						logger.info("收到数据 IP地址：" + ip + ";端口号：" + port + ";消息7E头尾校验失败！");
						System.out.println("收到数据 IP地址：" + ip + ";端口号：" + port + ";消息7E头尾校验失败！");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param dse
	 * @param alarmContent 需要发送的信息
	 */
	public static void sendfSms(Dse dse, String alarmContent) {
		Sms sms = new Sms();
		sms.setDevname(dse.getDevName() == null ? "未设置" : dse.getDevName());
		sms.setDevid(dse.getDevNumber() == null ? "未设置" : dse.getDevNumber());
		sms.setDevlocation(dse.getDevLocation() == null ? "未设置" : dse.getDevLocation());
		sms.setStatus(alarmContent);
		sms.setAddress(dse.getEmaddress());
		sms.setDate(ToolUtil.date2Str(new Date()));
		smsList.add(sms);
	}

	class ProcessMessageThread extends Thread {

		public void run() {
			while (true) {
				try {
					sleep(10);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				synchronized (receiveMessageList) {
					while (receiveMessageList.isEmpty()) {
						try {
							receiveMessageList.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				RecMsgEntity recMsgEntity = (RecMsgEntity) receiveMessageList.remove(0);
				if (ToolUtil.changeHighLow(recMsgEntity.getCmdCode()).equals("4401")) {
					sendMsgEntity = new SendMsgEntity();

					sendMsgEntity.setIp(recMsgEntity.getIp());
					sendMsgEntity.setPort(recMsgEntity.getPort());
					sendMsgEntity.setFh(recMsgEntity.getFh());
					sendMsgEntity.setVer(recMsgEntity.getVer());
					sendMsgEntity.setSn(recMsgEntity.getSn());
					sendMsgEntity.setAddressInfo(recMsgEntity.getAddressInfo());
					sendMsgEntity.setCmdCode(recMsgEntity.getCmdCode());
					sendMsgEntity.setStatusCode("FF");
					sendMsgEntity.setBody(ToolUtil.getfSendTime());
					sendMsgEntity.setFt(recMsgEntity.getFt());

					// SendMessage.send(datagramSocket, sendMsgEntity);

					Analytic analytic = ToolUtil.parseMessage(recMsgEntity.getBody());
					Dse dse = new Dse();
					AlarmInfo alarmInfo = new AlarmInfo();

					// 判断数据库中是否存在该设备芯片的全球唯一码，如果存在则保存数据，否则抛弃该条数据
					dse.setGuc(recMsgEntity.getAddressInfo());
					Dse ds = dseService.getDseByAddress(dse);
					if (ds != null) {
						dse.setId(ds.getId());
						try {
							if ((ds.getEmaddress() != null) && (!ds.getEmaddress().equals(""))) {
								// 判断数据库数据的onlineflag
								if (StringUtils.isNotBlank(ds.getOnlineFlag()) && (!ds.getOnlineFlag().equals("上线"))) {
//									ds.setId(dse.getId());
									ds.setOnlineFlag("上线");
									dseService.updateDseOnlineById(ds);
									// 告警设置断线
									Sms sms = new Sms();
									sms.setDevname(ds.getDevName() == null ? "未设置" : ds.getDevName());
									sms.setDevid(ds.getDevNumber() == null ? "未设置" : ds.getDevNumber());
									sms.setDevlocation(ds.getDevLocation() == null ? "未设置" : ds.getDevLocation());
									sms.setStatus("设备上线");
									sms.setDate(ToolUtil.date2Str(new Date()));
									smsList.add(sms);
								}

								try {
									dse.setCreateDate(new Date());
									ToolUtil.judgeAlarmStatus(analytic, alarmInfo, dse, format,alarmInfoService);
//									ToolUtil.judgeAlarmStatus(analytic, alarmInfo, dse.getId(), format,alarmInfoService);
								} catch (ParseException e1) {
									e1.printStackTrace();
								}
								
								dse.setEmaddress(analytic.getEmaddress());
								dse.setQuantity(analytic.getQuantity());
								dse.setVoltage(analytic.getVoltage());
								dse.setCurrent(analytic.getCurrent());
								dse.setPower(analytic.getPower());
								dse.setSoftver(analytic.getSoftver());
								dse.setHardver(analytic.getHardver());
								dse.setRatedPower(analytic.getRatedpower());
								dse.setMainStatus(analytic.getMainstatus());
								dse.setStatusIden(analytic.getStatusiden());
								dse.setMalIden(analytic.getFailureiden());
								// 电源温度
								dse.setPowerTemperature(analytic.getPowerTemperature());
								dse.setAcInputPower(analytic.getAcInputPower());
								dse.setOutputVoltage(analytic.getOutputVoltage());
								dse.setOutputCurrent(analytic.getOutputCurrent());
								dse.setBatteryCurrent(analytic.getBatteryCurrent());
								dse.setHeaterCurrent(analytic.getHeaterCurrent());
								dse.setLoadCurrent(analytic.getLoadCurrent());
								dse.setBatteryTemperature(analytic.getBatteryTemperature());
								dse.setTargetVoltage(analytic.getTargetVoltage());
								dse.setRatedCurrent(analytic.getRatedCurrent());
								dse.setCurrentLimit(analytic.getCurrentLimit());
								dse.setBatteryDischargeTime(analytic.getBatteryDischargeTime());
								dse.setBatteryQuantity(analytic.getBatteryQuantity());
								dse.setBmsIden(analytic.getBmsiden());
								dse.setPtNeedle(analytic.getPtNeedle());
								dse.setBlackOutAlarm(analytic.getBlackoutAlarm());
								dse.setOnlineFlag("上线");
								dse.setCreateDate(new Date());
								dseService.updateById(dse);

								// 市电告警
								String blackoutAlarm = dse.getBlackOutAlarm();
								// 停机告警
								String mainstatus = dse.getMainStatus();
								// 防雷针异常告警
								String ptNeedle = dse.getPtNeedle();

								Sms sms = new Sms();
								boolean runFlag = true;
								boolean ptFlag = true;
								if (blackoutAlarm.equals("004") && analytic.getBlackoutAlarm().equals("005")) {
									String alarmContent = AlarmType.ALARM_POWER_NORMAL;
									if ((mainstatus.equals("007") || mainstatus.equals("008"))
											&& (analytic.getMainstatus().equals("010")
													|| analytic.getMainstatus().equals("009"))) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_RUN_NORMAL;
										runFlag = false;
									} else if ((mainstatus.equals("010") || mainstatus.equals("009"))
											&& (analytic.getMainstatus().equals("007")
													|| analytic.getMainstatus().equals("008"))) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_RUN_ABNORMAL;
										runFlag = false;
									}

									// 防雷器异常告警
									if (ptNeedle.equals("013") && analytic.getPtNeedle().equals("012")) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_SPD_NORMAL;
										ptFlag = false;
									} else if (ptNeedle.equals("012") && analytic.getPtNeedle().equals("013")) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_SPD_ABNORMAL;
										ptFlag = false;
									}
									sendfSms(dse, alarmContent);

								} else if (blackoutAlarm.equals("005") && analytic.getBlackoutAlarm().equals("004")) {
									String alarmContent = AlarmType.ALARM_POWER_ABNORMAL;
									// 停机告警
									if ((mainstatus.equals("007") || mainstatus.equals("008"))
											&& (analytic.getMainstatus().equals("010")
													|| analytic.getMainstatus().equals("009"))) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_POWER_NORMAL;
										runFlag = false;
									} else if ((mainstatus.equals("010") || mainstatus.equals("009"))
											&& (analytic.getMainstatus().equals("007")
													|| analytic.getMainstatus().equals("008"))) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_RUN_ABNORMAL;
										runFlag = false;
									}

									// 防雷器异常告警
									if (ptNeedle.equals("013") && analytic.getPtNeedle().equals("012")) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_SPD_NORMAL;
										ptFlag = false;
									} else if (ptNeedle.equals("012") && analytic.getPtNeedle().equals("013")) {
										sms.setStatus(AlarmType.ALARM_SPD_ABNORMAL);
										alarmContent = alarmContent + "," + AlarmType.ALARM_SPD_ABNORMAL;
										ptFlag = false;
									}
									sendfSms(dse, alarmContent);
								}

								// 停机告警
								if (runFlag) {
									if ((mainstatus.equals("007") || mainstatus.equals("008"))
											&& (analytic.getMainstatus().equals("010")
													|| analytic.getMainstatus().equals("009"))) {
										String alarmContent = AlarmType.ALARM_RUN_NORMAL;

										// 防雷器异常告警
										if (ptNeedle.equals("013") && analytic.getPtNeedle().equals("012")) {
											alarmContent = alarmContent + "," + AlarmType.ALARM_SPD_NORMAL;
											ptFlag = false;
										} else if (ptNeedle.equals("012") && analytic.getPtNeedle().equals("013")) {
											alarmContent = alarmContent + "," + AlarmType.ALARM_SPD_ABNORMAL;
											ptFlag = false;
										}
										sendfSms(dse, alarmContent);
									} else if ((mainstatus.equals("010") || mainstatus.equals("009"))
											&& (analytic.getMainstatus().equals("007")
													|| analytic.getMainstatus().equals("008"))) {
										String alarmContent = AlarmType.ALARM_RUN_ABNORMAL;

										// 防雷器异常告警
										if (ptNeedle.equals("013") && analytic.getPtNeedle().equals("012")) {
											alarmContent = alarmContent + "," + AlarmType.ALARM_SPD_NORMAL;
											ptFlag = false;
										} else if (ptNeedle.equals("012") && analytic.getPtNeedle().equals("013")) {
											alarmContent = alarmContent + "," + AlarmType.ALARM_SPD_ABNORMAL;
											ptFlag = false;
										}
										sendfSms(dse, alarmContent);
									}
								}
								// 防雷器告警
								if (ptFlag) {
									if (ptNeedle.equals("013") && analytic.getPtNeedle().equals("012")) {
										String alarmContent = "," + AlarmType.ALARM_SPD_NORMAL;
										sendfSms(dse, alarmContent);
									} else if (ptNeedle.equals("012") && analytic.getPtNeedle().equals("013")) {
										String alarmContent = "," + AlarmType.ALARM_SPD_ABNORMAL;
										sendfSms(dse, alarmContent);
									}
								}

								sid.setDev_id(dse.getId());
								sid.setStatusd0((analytic.getStatusMap().get("statusIden")).get("status_d0"));
								sid.setStatusd1((analytic.getStatusMap().get("statusIden")).get("status_d1"));
								sid.setStatusd2((analytic.getStatusMap().get("statusIden")).get("status_d8"));
								sid.setStatusd3((analytic.getStatusMap().get("statusIden")).get("status_d10"));
								sid.setStatusd4((analytic.getStatusMap().get("statusIden")).get("status_d11"));
								sid.setStatusd5((analytic.getStatusMap().get("statusIden")).get("status_d12"));
								sid.setStatusd6((analytic.getStatusMap().get("statusIden")).get("status_d13"));
								sid.setStatusd7((analytic.getStatusMap().get("statusIden")).get("status_d14"));
								sid.setStatusd8((analytic.getStatusMap().get("statusIden")).get("status_d15"));
								statusIdenService.updateById(sid);

								mid.setDev_id(dse.getId());
								mid.setMald0((analytic.getMalMap().get("malIden")).get("mal_d0"));
								mid.setMald1((analytic.getMalMap().get("malIden")).get("mal_d1"));
								mid.setMald2((analytic.getMalMap().get("malIden")).get("mal_d2"));
								mid.setMald3((analytic.getMalMap().get("malIden")).get("mal_d3"));
								mid.setMald4((analytic.getMalMap().get("malIden")).get("mal_d4"));
								mid.setMald5((analytic.getMalMap().get("malIden")).get("mal_d5"));
								mid.setMald6((analytic.getMalMap().get("malIden")).get("mal_d6"));
								mid.setMald7((analytic.getMalMap().get("malIden")).get("mal_d8"));
								mid.setMald8((analytic.getMalMap().get("malIden")).get("mal_d9"));
								mid.setMald9((analytic.getMalMap().get("malIden")).get("mal_d10"));
								mid.setMald10((analytic.getMalMap().get("malIden")).get("mal_d11"));
								mid.setMald11((analytic.getMalMap().get("malIden")).get("mal_d15"));
								malIdenService.updateById(mid);

								bms.setDev_id(dse.getId());
								bms.setBmsd0((analytic.getBmsMap().get("bmsIden")).get("bms_d0"));
								bms.setBmsd1((analytic.getBmsMap().get("bmsIden")).get("bms_d1"));
								bms.setBmsd2((analytic.getBmsMap().get("bmsIden")).get("bms_d2"));
								bms.setBmsd3((analytic.getBmsMap().get("bmsIden")).get("bms_d3"));
								bms.setBmsd4((analytic.getBmsMap().get("bmsIden")).get("bms_d4"));
								bms.setBmsd5((analytic.getBmsMap().get("bmsIden")).get("bms_d5"));
								bms.setBmsd6((analytic.getBmsMap().get("bmsIden")).get("bms_d6"));
								bms.setBmsd7((analytic.getBmsMap().get("bmsIden")).get("bms_d7"));
								bms.setBmsd8((analytic.getBmsMap().get("bmsIden")).get("bms_d8"));
								bms.setBmsd9((analytic.getBmsMap().get("bmsIden")).get("bms_d9"));
								bms.setBmsd10((analytic.getBmsMap().get("bmsIden")).get("bms_d10"));
								bms.setBmsd11((analytic.getBmsMap().get("bmsIden")).get("bms_d11"));
								bms.setBmsd12((analytic.getBmsMap().get("bmsIden")).get("bms_d12"));
								bms.setBmsd13((analytic.getBmsMap().get("bmsIden")).get("bms_d15"));
								bmsIdenService.updateById(bms);
								
								// 从数据库中查看该设备是否需要升级
								UpdateFlag updateFlag = new UpdateFlag();
								updateFlag.setDse_id(dse.getId());								
								UpdateFlag ufd = updateFlagService.getUpdateFlagByDseId(updateFlag);
								if (ufd != null && ufd.getPackageNo() == 0) {
									// 发送升级准备
									sendMsgEntity.setIp(recMsgEntity.getIp());
									sendMsgEntity.setPort(recMsgEntity.getPort());
									sendMsgEntity.setFh(recMsgEntity.getFh());
									sendMsgEntity.setVer(recMsgEntity.getVer());
									sendMsgEntity.setSn(recMsgEntity.getSn());
									sendMsgEntity.setAddressInfo(recMsgEntity.getAddressInfo());
									sendMsgEntity.setCmdCode(ToolUtil.changeHighLow("4412"));
									sendMsgEntity.setStatusCode("FF");
									sendMsgEntity.setBody("01" + ToolUtil.changeHighLow(ToolUtil.addLeftZero(
											Integer.toHexString(ufd.getPackageSize()).toUpperCase(), 4)));
									sendMsgEntity.setFt(recMsgEntity.getFt());
									SendMessage.send(datagramSocket, sendMsgEntity);
									thread = new RetrySendThread(datagramSocket, sendMsgEntity, true, new Date());
									thread.start();
								}
							
							} else {
								dse.setEmaddress(analytic.getEmaddress());
								dse.setQuantity(analytic.getQuantity());
								dse.setVoltage(analytic.getVoltage());
								dse.setCurrent(analytic.getCurrent());
								dse.setPower(analytic.getPower());
								dse.setSoftver(analytic.getSoftver());
								dse.setHardver(analytic.getHardver());
								dse.setRatedPower(analytic.getRatedpower());
								dse.setMainStatus(analytic.getMainstatus());
								dse.setStatusIden(analytic.getStatusiden());
								dse.setMalIden(analytic.getFailureiden());
								// 电源温度
								dse.setPowerTemperature(analytic.getPowerTemperature());
								dse.setAcInputPower(analytic.getAcInputPower());
								dse.setOutputVoltage(analytic.getOutputVoltage());
								dse.setOutputCurrent(analytic.getOutputCurrent());
								dse.setBatteryCurrent(analytic.getBatteryCurrent());
								dse.setHeaterCurrent(analytic.getHeaterCurrent());
								dse.setLoadCurrent(analytic.getLoadCurrent());
								dse.setBatteryTemperature(analytic.getBatteryTemperature());
								dse.setTargetVoltage(analytic.getTargetVoltage());
								dse.setRatedCurrent(analytic.getRatedCurrent());
								dse.setCurrentLimit(analytic.getCurrentLimit());
								dse.setBatteryDischargeTime(analytic.getBatteryDischargeTime());
								dse.setBatteryQuantity(analytic.getBatteryQuantity());
								dse.setBmsIden(analytic.getBmsiden());
								dse.setPtNeedle(analytic.getPtNeedle());
								dse.setBlackOutAlarm(analytic.getBlackoutAlarm());
								dse.setOnlineFlag(null);
								dse.setCreateDate(new Date());
								dseService.updateById(dse);

								try {
									ToolUtil.judgeAlarmStatus(analytic, alarmInfo, dse, format,
											alarmInfoService);
//									ToolUtil.judgeAlarmStatus(analytic, alarmInfo, dse.getId(), format,
//											alarmInfoService);
								} catch (ParseException e1) {
									e1.printStackTrace();
								}

								Dse dse_1 = (Dse) dseService.getDseByAddress(dse);
								// 上线告警
								if (dse_1.getOnlineFlag() == null) {
									// 将设备置为上线
//									dse_1.setId(dse_1.getId());
									dse_1.setOnlineFlag("上线");
									dseService.updateDseOnlineById(dse_1);
									String alarmContent = "设备上线";

									String blackoutAlarm = analytic.getBlackoutAlarm();
									String mainstatus = analytic.getMainstatus();
									String ptNeedle = analytic.getPtNeedle();

									// 1.主运行状态、市电正常，防雷器异常
									if (blackoutAlarm.equals("005")
											&& (!mainstatus.equals("008") && !mainstatus.equals("007"))
											&& ptNeedle.equals("013")) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_SPD_ABNORMAL;
									}

									// 2.市电状态、防雷器正常，主运行状态异常
									if (blackoutAlarm.equals("005")
											&& (mainstatus.equals("008") || mainstatus.equals("007"))
											&& ptNeedle.equals("012")) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_RUN_ABNORMAL;
									}

									// 3.主运行状态正常，市电、防雷器异常
									if (blackoutAlarm.equals("004")
											&& (!mainstatus.equals("008") && !mainstatus.equals("007"))
											&& ptNeedle.equals("013")) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_POWER_ABNORMAL + ","
												+ AlarmType.ALARM_SPD_ABNORMAL;
									}

									// 4.主运行状态异常，市电、防雷器正常
									if (blackoutAlarm.equals("005")
											&& (mainstatus.equals("008") && mainstatus.equals("007"))
											&& ptNeedle.equals("012")) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_RUN_ABNORMAL;
									}

									// 5.主运行状态、防雷器异常，市电正常
									if (blackoutAlarm.equals("005")
											&& (mainstatus.equals("008") && mainstatus.equals("007"))
											&& ptNeedle.equals("013")) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_RUN_ABNORMAL + ","
												+ AlarmType.ALARM_SPD_ABNORMAL;
									}

									// 6.主运行状态、市电异常，防雷器正常
									if (blackoutAlarm.equals("004")
											&& (mainstatus.equals("008") && mainstatus.equals("007"))
											&& ptNeedle.equals("012")) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_RUN_ABNORMAL + ","
												+ AlarmType.ALARM_POWER_ABNORMAL;
									}

									// 7.主运行状态、市电、防雷器异常
									if (blackoutAlarm.equals("004")
											&& (mainstatus.equals("008") || mainstatus.equals("007"))
											&& ptNeedle.equals("013")) {
										alarmContent = alarmContent + "," + AlarmType.ALARM_RUN_ABNORMAL + ","
												+ AlarmType.ALARM_POWER_ABNORMAL + "," + AlarmType.ALARM_SPD_ABNORMAL;
									}

									sendfSms(dse_1, alarmContent);
								}

								String statusId = ToolUtil.get32UUID();
								sid.setId(statusId);
								sid.setDev_id(dse.getId());
								sid.setStatusd0((analytic.getStatusMap().get("statusIden")).get("status_d0"));
								sid.setStatusd1((analytic.getStatusMap().get("statusIden")).get("status_d1"));
								sid.setStatusd2((analytic.getStatusMap().get("statusIden")).get("status_d8"));
								sid.setStatusd3((analytic.getStatusMap().get("statusIden")).get("status_d10"));
								sid.setStatusd4((analytic.getStatusMap().get("statusIden")).get("status_d11"));
								sid.setStatusd5((analytic.getStatusMap().get("statusIden")).get("status_d12"));
								sid.setStatusd6((analytic.getStatusMap().get("statusIden")).get("status_d13"));
								sid.setStatusd7((analytic.getStatusMap().get("statusIden")).get("status_d14"));
								sid.setStatusd8((analytic.getStatusMap().get("statusIden")).get("status_d15"));
								statusIdenService.savestatus(sid);

								String malId = ToolUtil.get32UUID();
								mid.setId(malId);
								mid.setDev_id(dse.getId());
								mid.setMald0((analytic.getMalMap().get("malIden")).get("mal_d0"));
								mid.setMald1((analytic.getMalMap().get("malIden")).get("mal_d1"));
								mid.setMald2((analytic.getMalMap().get("malIden")).get("mal_d2"));
								mid.setMald3((analytic.getMalMap().get("malIden")).get("mal_d3"));
								mid.setMald4((analytic.getMalMap().get("malIden")).get("mal_d4"));
								mid.setMald5((analytic.getMalMap().get("malIden")).get("mal_d5"));
								mid.setMald6((analytic.getMalMap().get("malIden")).get("mal_d6"));
								mid.setMald7((analytic.getMalMap().get("malIden")).get("mal_d8"));
								mid.setMald8((analytic.getMalMap().get("malIden")).get("mal_d9"));
								mid.setMald9((analytic.getMalMap().get("malIden")).get("mal_d10"));
								mid.setMald10((analytic.getMalMap().get("malIden")).get("mal_d11"));
								mid.setMald11((analytic.getMalMap().get("malIden")).get("mal_d15"));
								malIdenService.savestatus(mid);

								String bmsId = ToolUtil.get32UUID();
								bms.setId(bmsId);
								bms.setDev_id(dse.getId());
								bms.setBmsd0((analytic.getBmsMap().get("bmsIden")).get("bms_d0"));
								bms.setBmsd1((analytic.getBmsMap().get("bmsIden")).get("bms_d1"));
								bms.setBmsd2((analytic.getBmsMap().get("bmsIden")).get("bms_d2"));
								bms.setBmsd3((analytic.getBmsMap().get("bmsIden")).get("bms_d3"));
								bms.setBmsd4((analytic.getBmsMap().get("bmsIden")).get("bms_d4"));
								bms.setBmsd5((analytic.getBmsMap().get("bmsIden")).get("bms_d5"));
								bms.setBmsd6((analytic.getBmsMap().get("bmsIden")).get("bms_d6"));
								bms.setBmsd7((analytic.getBmsMap().get("bmsIden")).get("bms_d7"));
								bms.setBmsd8((analytic.getBmsMap().get("bmsIden")).get("bms_d8"));
								bms.setBmsd9((analytic.getBmsMap().get("bmsIden")).get("bms_d9"));
								bms.setBmsd10((analytic.getBmsMap().get("bmsIden")).get("bms_d10"));
								bms.setBmsd11((analytic.getBmsMap().get("bmsIden")).get("bms_d11"));
								bms.setBmsd12((analytic.getBmsMap().get("bmsIden")).get("bms_d12"));
								bms.setBmsd13((analytic.getBmsMap().get("bmsIden")).get("bms_d15"));
								bmsIdenService.savestatus(bms);

								// 从数据库中查看该设备是否需要升级
								UpdateFlag updateFlag = new UpdateFlag();
								updateFlag.setDse_id(dse.getId());								
								UpdateFlag ufd = updateFlagService.getUpdateFlagByDseId(updateFlag);
								if (ufd != null && ufd.getPackageNo() == 0) {
									// 发送升级准备
									sendMsgEntity.setIp(recMsgEntity.getIp());
									sendMsgEntity.setPort(recMsgEntity.getPort());
									sendMsgEntity.setFh(recMsgEntity.getFh());
									sendMsgEntity.setVer(recMsgEntity.getVer());
									sendMsgEntity.setSn(recMsgEntity.getSn());
									sendMsgEntity.setAddressInfo(recMsgEntity.getAddressInfo());
									sendMsgEntity.setCmdCode(ToolUtil.changeHighLow("4412"));
									sendMsgEntity.setStatusCode("FF");
									sendMsgEntity.setBody("01" + ToolUtil.changeHighLow(ToolUtil.addLeftZero(
											Integer.toHexString(ufd.getPackageSize()).toUpperCase(), 4)));
									sendMsgEntity.setFt(recMsgEntity.getFt());
									SendMessage.send(datagramSocket, sendMsgEntity);
									thread = new RetrySendThread(datagramSocket, sendMsgEntity, true, new Date());
									thread.start();
								}

							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("该数据已抛弃");
					}

				}

				// 收到准备升级
				else if (ToolUtil.changeHighLow(recMsgEntity.getCmdCode()).equals("4412")) {

					sendMsgEntity = new SendMsgEntity();
					String updateType = recMsgEntity.getBody().substring(0, 2);
					String addressInfo = recMsgEntity.getAddressInfo();
					Dse dseInfo = new Dse();
					dseInfo.setGuc(addressInfo);
					try {
						Dse dse = dseService.getDseByAddress(dseInfo);
						String dse_id = dse.getId();

						if (dse_id != null && dse_id != "") {
							UpdateFlag updateFlagInfo = new UpdateFlag();
							updateFlagInfo.setDse_id(dse_id);							
							UpdateFlag updateFlag = updateFlagService.getUpdateFlagByDseId(updateFlagInfo);
							if (updateFlag != null) {
//								if (updateType.equals("01")) {
//									String updateResult = recMsgEntity.getBody().substring(2, 4);
//									if (updateResult.equals("01")) {
//										updateFlagInfo.setFlag("02");
//										updateFlagService.updateFlagByDseId(updateFlagInfo);
//									}
//								} 
								if (updateType.equals("02")) {
//									else if (updateType.equals("02")) {
									thread.cancel();
									String updateResult = recMsgEntity.getBody().substring(2, 4);
									if (updateResult.equals("01")) {
										updateFlagInfo.setFlag("02");
										updateFlagService.updateFlagByDseId(updateFlagInfo);
									}
									updateFlag = updateFlagService.getUpdateFlagByDseId(updateFlagInfo);

									updateFlagInfo.setFile_id(updateFlag.getFile_id());
									int packageNo = Integer.parseInt(
											ToolUtil.changeHighLow(recMsgEntity.getBody().substring(2, 6)), 16);
									updateFlagInfo.setPackage_no(packageNo);
									UpdateData updateData = updateDataService.getUpdateDataByFileIdAndPackageNo(updateFlagInfo);

									sendMsgEntity.setIp(recMsgEntity.getIp());
									sendMsgEntity.setPort(recMsgEntity.getPort());
									sendMsgEntity.setFh(recMsgEntity.getFh());
									sendMsgEntity.setVer(recMsgEntity.getVer());
									sendMsgEntity.setSn(recMsgEntity.getSn());
									sendMsgEntity.setAddressInfo(recMsgEntity.getAddressInfo());
									sendMsgEntity.setCmdCode(ToolUtil.changeHighLow("4412"));
									sendMsgEntity.setStatusCode("FF");
									sendMsgEntity.setBody("02"
											+ ToolUtil.changeHighLow(ToolUtil
													.addLeftZero(Integer.toHexString(packageNo).toUpperCase(), 4))
											+ updateData.getData());
									sendMsgEntity.setFt(recMsgEntity.getFt());
									SendMessage.send(datagramSocket, sendMsgEntity);

									updateFlagService.updatePackageNoByDseId(updateFlagInfo);

								} else if (updateType.equals("03")) {
									updateFlagInfo.setFlag("03");
									updateFlagService.updateFlagByDseId(updateFlagInfo);
									updateFlagService.updatePackageNoByDseId(updateFlagInfo);
									sendMsgEntity.setIp(recMsgEntity.getIp());
									sendMsgEntity.setPort(recMsgEntity.getPort());
									sendMsgEntity.setFh(recMsgEntity.getFh());
									sendMsgEntity.setVer(recMsgEntity.getVer());
									sendMsgEntity.setSn(recMsgEntity.getSn());
									sendMsgEntity.setAddressInfo(recMsgEntity.getAddressInfo());
									sendMsgEntity.setCmdCode(ToolUtil.changeHighLow("4412"));
									sendMsgEntity.setStatusCode("FF");
									sendMsgEntity.setBody("0301");
									sendMsgEntity.setFt(recMsgEntity.getFt());
									SendMessage.send(datagramSocket, sendMsgEntity);
									updateFlagService.updatePackageNoByDseId(updateFlagInfo);

								}

							}

						}

					} catch (Exception e) {
                         System.out.println(e.getMessage());
					}

				}

			}
		}
	}

	@PropertySource(value = "calsspath:port.properties")
	class SendSerialThread extends Thread {

		boolean openPort = true;
		SerialPort serialPort = null;
		
		// 串口
		String port = "COM7";
		
		// 波特率
		int baudrate = 9600;
		boolean runFlag = true;
		int sendCount = 0;

		@Override
		public void run() {
        
			System.out.println("port值："+port);
			while (runFlag) {
				try {
					sleep(100);
					if (openPort) {
						if (StringUtils.isNotBlank(port)) {
							// 打开端口
							serialPort = SerialTool.openPort(port, baudrate);
							// 设置监听
							serialListener = new SerialListener(serialPort);
							SerialTool.addListener(serialPort, serialListener);
						}
						openPort = false;
					}
					if (smsList.size() > 0) {
						System.out.println("开始发送短信，当前短信条数：" + smsList.size());
						Thread.sleep(100);					
						// 通过当前设备id查看对应的管理员
						Sms sms = smsList.get(0);
						acredit.setEmaddress(sms.getAddress());
						List<User> adminList = userService.getUserByDevid(acredit);
						if (adminList != null) {
							for (int i = 0; i < adminList.size(); i++) {
								// 内容
								String content = adminList.get(i).getTel() + ":0:" + "设备编号：-" + sms.getDevid()
										+ ";设备名称：" + sms.getDevname() + ";设备地址：" + sms.getDevlocation() + "硬件地址："
										+ sms.getAddress() + ";提示类型:" + sms.getStatus() + ";时间：" + sms.getDate();
								System.out.println("当前短信目标地址：" + adminList.get(i).getTel() + "；短信内容：" + content);
								// 短信发送时间
								long sendTime = new Date().getTime();
								SerialTool.sendToPort(serialPort, content);
								boolean flag = true;
								while (flag) {
									sleep(100);
									if (serialListener.getDataOriginal().equals("SMS_SEND_SUCESS")) {
										System.out.println("短信发送已经完成。");
										serialListener.setDataOriginal("");
										flag = false;
									} else if (serialListener.getDataOriginal().equals("SMS_SEND_FAIL")) {
										System.out.println("短信发送失败。");
										serialListener.setDataOriginal("");
										flag = false;
									} else {
										// 当前
										long nowTime = new Date().getTime();
										if (nowTime - sendTime >= 60 * 1000) {
											serialListener.setDataOriginal("");
											System.out.println("短信发送超时...");
											flag = false;
											break;
										}
									}

								}
							}
							removeSmsList();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	// 判断是否在线
	class OnlineThread extends Thread {

		@Override
		public void run() {

			while (true) {
				try {
					sleep(100);
					long nowTime = new Date().getTime();
					Thread.sleep(1000);
					List<Dse> dseList = dseService.getAllDseList(new Dse());
					for (int i = 0; i < dseList.size(); i++) {
						Dse dse = dseList.get(i);
//						long createTime = dse.getCreateDate().getTime();
						long createTime = dse.getCreateDate().getTime();
						if (nowTime - createTime > 8 * 1000) {
							if (dse != null) {
								// 目前在线状态
								if (dse.getOnlineFlag().equals("上线")) {
									// 将设备置为断线
									dse.setId(dse.getId());
									dse.setOnlineFlag("断线");
									dseService.updateDseOnlineById(dse);
									// 告警设置断线
									Sms sms = new Sms();
									sms.setDevname(dse.getDevName() == null ? "未设置" : dse.getDevName());
									sms.setDevid(dse.getDevNumber() == null ? "未设置" : dse.getDevNumber());
									sms.setDevlocation(dse.getDevLocation() == null ? "未设置" : dse.getDevLocation());
									sms.setAddress(dse.getEmaddress());
									sms.setStatus("断线告警");
									sms.setDate(ToolUtil.date2Str(new Date()));
									smsList.add(sms);
//										logger.info("当前短信个数：" + smsList.size());
									System.out.println("当前短信个数：" + smsList.size());
								}
							}

						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

	}

	// 清理消息缓存
	class ClearLinkThread extends Thread {
		@Override
		public void run() {
			while (true) {
				if (smsList.size() > 10000) {
					removeSmsList();
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	// 同步，防止被同时使用
	public synchronized void removeSmsList() {
		if (smsList.size() > 0) {
			smsList.remove(0);
		}
	}
}
