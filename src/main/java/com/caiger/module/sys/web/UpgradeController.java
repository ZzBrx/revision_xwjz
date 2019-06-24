package com.caiger.module.sys.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.druid.support.json.JSONUtils;
import com.caiger.common.global.Global;
import com.caiger.common.lang.StringUtils;
import com.caiger.common.web.BaseController;
import com.caiger.module.sys.entity.Dse;
import com.caiger.module.sys.entity.UpdateData;
import com.caiger.module.sys.entity.UpdateFlag;
import com.caiger.module.sys.entity.UpgradeFile;
import com.caiger.module.sys.service.UpdateDataService;
import com.caiger.module.sys.service.UpdateFlagService;
import com.caiger.module.sys.service.UpgradeService;
import com.caiger.module.sys.utils.ToolUtil;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("upgrade/")
public class UpgradeController extends BaseController {

	@Autowired
	private UpgradeService upgradeService;

	@Autowired
	private UpdateDataService updateDataService;
	
	@Autowired
	private UpdateFlagService updateFlagService;

	@RequestMapping(value = "list")
	public String list(Model model) {
		return "modules/dse/dse_mgt/upgradefile";
	}
	
	@RequestMapping(value = "form")
	public String form(Model model,Dse dse) {
		model.addAttribute("dse", dse);
		return "modules/dse/dse_mgt/upgradefile02";
	}

	// 查找数据库中所有的升级文件
	@RequestMapping(value = "listData")
	@ResponseBody
	public PageInfo<UpgradeFile> listData(UpgradeFile file, HttpServletRequest request, HttpServletResponse response) {
		startPage(request);
		List<UpgradeFile> fileList = upgradeService.getList(file);
		PageInfo<UpgradeFile> pageInfo = new PageInfo<UpgradeFile>(fileList);
		return pageInfo;
	}

	// 删除升级文件
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(UpgradeFile file) {
		upgradeService.delete(file);
		UpdateData updateData = new UpdateData();
		updateData.setFile_id(file.getId());
		updateDataService.delete(updateData);
		return renderResult(Global.TRUE, text("删除文件【" + file.getFileName() + "】成功"));
	}
	
	// 取消升级
	@RequestMapping(value = "cancel")
	@ResponseBody
	public Object cancel(UpdateFlag updateFlag) {
		HashMap<Object, Object> hashMap = new HashMap<>();
		UpdateFlag updateFlagByDseId = updateFlagService.getUpdateFlagByDseId(updateFlag);
		if(updateFlagByDseId == null) {
			hashMap.put("result", "nothingness");
		}else {
			try {
				updateFlagService.deleteUpdateFlagByDseId(updateFlag);
				hashMap.put("result", "success");
			} catch (Exception e) {
				hashMap.put("result", "error");
				e.printStackTrace();
			}
		}
		return JSONUtils.toJSONString(hashMap);		
	}
	
	// 判断是否在升级
	@RequestMapping(value = "/update")
    @ResponseBody
   	public Object updateFile(UpdateFlag updateFlag, UpdateData updateData, HttpServletResponse response, Model model) {
		HashMap<Object, Object> hashMap = new HashMap<>();
    	try {
    		//根据文件ID查询有多少包
			List<UpdateData> updateDataList = updateDataService.getUpdateDataByFileId(updateData);
    		//先查询下是否有正在升级的...
    		UpdateFlag udf = updateFlagService.getUpdateFlagByDseId(updateFlag);
    		updateFlag.setPackageNo(0);
	    	updateFlag.setPackageSize(updateDataList.size());
	    	updateFlag.setFlag("01");
	    	// 如果存在升级
			if (udf!=null) {
				hashMap.put("result", "reiteration");
				return JSONUtils.toJSONString(hashMap);
			}
			// 如果不存在升级
			else{
				updateFlagService.saveUpdateFlag(updateFlag);
				hashMap.put("result", "success");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return JSONUtils.toJSONString(hashMap);
    }
	
	
	@RequestMapping(value = "/progress")
    @ResponseBody
   	public Object progress(HttpServletRequest request, HttpServletResponse response, Model model) {
   		HashMap<Object, Object> hashMap = new HashMap<>();
   		try {
			List<UpdateFlag> listFlag = updateFlagService.getUpdateFlag();
			hashMap.put("size", listFlag.size());
			hashMap.put("result", listFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}  		
   		return hashMap;
   	}
	
	
	@RequestMapping(value = "/finished")
    @ResponseBody
   	public void finished(HttpServletRequest request, HttpServletResponse response, Model model) {
		String flag = request.getParameter("flag");
		String dse_id = request.getParameter("dse_id");
		if(flag .equals("03")) {
			// 删除升级文件
		UpdateFlag updateFlag = new UpdateFlag();
		updateFlag.setDse_id(dse_id);
		updateFlagService.deleteUpdateFlagByDseId(updateFlag);
		}
   	}

	/*
	 * 采用spring提供的上传文件的方法
	 */
	@RequestMapping(value = "upload")
	@ResponseBody
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile uploadFile,
			HttpSession session) {
		UpgradeFile upgradeFile = new UpgradeFile();
		BufferedInputStream bis = null;
		HashMap<Object, Object> hashMap = new HashMap<>();
		
		 //获取文件名作为保存到服务器的文件名
    	if (uploadFile.getSize()>0) {
    		String fileName = StringUtils.trim(uploadFile.getOriginalFilename());
    		
    		if (fileName.endsWith("bin")) {
    			
    			// 判断数据库是否已经有该文件
    			upgradeFile.setFileName(fileName);
    			UpgradeFile updateFileByName = upgradeService.getUpdateFileByName(upgradeFile);
    			if(updateFileByName != null) {
    				hashMap.put("status", "exist");
    				return JSONUtils.toJSONString(hashMap);
    			}
    			
   			String filePath = "D:/微站升级文件/bin";
   			System.out.println(filePath);
    			try {
    				File file = new File(new File(filePath).getAbsolutePath() + "/" +fileName);
    				if (!file.exists()) {
    					file.mkdirs();
					}else {
						file.delete();
					}
    				if(file.exists()) {
    					file.delete();
    				}
    				uploadFile.transferTo(file.getAbsoluteFile());
    				
    				upgradeFile.setFileName(fileName);
					upgradeFile.setUrl(filePath + "//" + fileName);
					upgradeFile.setType("交流-直流");
					upgradeFile.setCreateDate(new Date());
					upgradeService.save(upgradeFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					hashMap.put("status", "error");
					return JSONUtils.toJSONString(hashMap);
				}
			}
			
		}

		try {

			UpgradeFile updatefile = upgradeService.getUpdateFileByName(upgradeFile);
			if (updatefile != null) {
				// 拆包后的升级消息信息集合
				List<String> updateInfos = new ArrayList<String>();
				List<byte[]> list = new ArrayList<byte[]>();
				String updateFileUrl = updatefile.getUrl();
				int fileByteLength = 0;
				File file = new File(updateFileUrl);
				if (file.exists()) {

					bis = new BufferedInputStream(new FileInputStream(file));
					byte[] dataFile = new byte[100];
					int read = -1;
					while ((read = bis.read(dataFile, 0, dataFile.length)) != -1) {

						byte[] newDataFile = new byte[read];
						for (int i = 0; i < newDataFile.length; i++) {
							newDataFile[i] = dataFile[i];
						}

						fileByteLength += read;
						list.add(newDataFile);

					}
					int count = 0;
					byte[] dataByte = new byte[fileByteLength];
					for (int i = 0; i < list.size(); i++) {

						byte[] b = list.get(i);
						for (int j = 0; j < b.length; j++) {
							dataByte[count++] = b[j];
						}
					}

					int mo1 = dataByte.length % 4;
					byte[] myDataByte = null;
					if (mo1 > 0) {
						int moLen = 4 - mo1;
						myDataByte = new byte[dataByte.length + moLen];
					} else {
						myDataByte = new byte[dataByte.length];
					}

					System.out.println(myDataByte.length);

					for (int i = 0; i < myDataByte.length; i++) {

						if (i < dataByte.length) {
							myDataByte[i] = dataByte[i];

						} else {
							myDataByte[i] = (byte) 0;
						}
					}

					int sum = 0;
					for (int ii = 0; ii < myDataByte.length; ii += 4) {
						int s = ((myDataByte[ii] & 0xff) + ((myDataByte[ii + 1] & 0xff) << 8)
								+ ((myDataByte[ii + 2] & 0xff) << 16) + ((myDataByte[ii + 3] & 0xff) << 24));
						sum += s;
					}

					String programDataCkeck = Integer.toHexString((0xFFFFFFFF - sum) ^ 0x55AAAA55);

					byte[] dataCheck = ToolUtil.resolveStringToByte(programDataCkeck, programDataCkeck.length() / 2);

					// 倒序排列
					byte[] dataInverted = new byte[dataCheck.length];
					for (int i = 0; i < dataInverted.length; i++) {
						dataInverted[i] = dataCheck[dataInverted.length - i - 1];

					}

					byte[] newDataByte = new byte[myDataByte.length + dataInverted.length];

					for (int i = 0; i < myDataByte.length; i++) {
						newDataByte[i] = myDataByte[i];
					}

					for (int i = 0; i < dataInverted.length; i++) {
						newDataByte[myDataByte.length + i] = dataInverted[i];
					}

					int mo = newDataByte.length % 200;
					int size = newDataByte.length / 200;
					if (size > 0) {
						if (mo == 0) {
							updateInfos = new ArrayList<String>();

							for (int i = 0; i < size; i++) {
								byte[] newData = new byte[200];

								for (int j = 0; j < newData.length; j++) {

									newData[j] = newDataByte[j + i * newData.length];
								}

								updateInfos.add(ToolUtil.byteConvertToHex(newData));
							}

						} else {
							updateInfos = new ArrayList<String>();

							for (int i = 0; i < size; i++) {
								byte[] newData = new byte[200];

								for (int j = 0; j < newData.length; j++) {

									newData[j] = newDataByte[j + i * newData.length];
//			                            logger.info("待发送数据："+ToolUtil.separateBlank(ToolUtil.byteConvertToHex(newData)));
								}
								updateInfos.add(ToolUtil.byteConvertToHex(newData));
							}

							byte[] moData = new byte[mo];

							for (int i = 0; i < mo; i++) {
								moData[i] = newDataByte[200 * size + i];

							}
							updateInfos.add(ToolUtil.byteConvertToHex(moData));
						}

					} else {
						updateInfos = new ArrayList<String>();

						byte[] moData = new byte[mo];
						for (int i = 0; i < mo; i++) {
							moData[i] = newDataByte[i];
						}
						updateInfos.add(ToolUtil.byteConvertToHex(moData));

					}

					System.out.println(updateInfos.size());
					// 下载文件
					for (int i = 0; i < updateInfos.size(); i++) {
						UpdateData updateData = new UpdateData();
						updateData.setFile_id(updatefile.getId());
						updateData.setPackage_no(i + 1);
						updateData.setData(updateInfos.get(i));

						// 待发送数据
						System.out.println("待发送数据:" + ToolUtil.separateBlank(updateInfos.get(i)));
						// 存储发送数据
						updateDataService.save(updateData);

					}

				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		hashMap.put("status", "success");
		return JSONUtils.toJSONString(hashMap);
	}
}
