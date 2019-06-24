package com.caiger.module.sys.web;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.caiger.common.global.Global;
import com.caiger.common.lang.StringUtils;
import com.caiger.common.web.BaseController;
import com.caiger.module.sys.entity.Acredit;
import com.caiger.module.sys.entity.AlarmInfo;
import com.caiger.module.sys.entity.BmsIden;
import com.caiger.module.sys.entity.Dse;
import com.caiger.module.sys.entity.MalIden;
import com.caiger.module.sys.entity.StatusIden;
import com.caiger.module.sys.service.AcreditService;
import com.caiger.module.sys.service.AlarmInfoService;
import com.caiger.module.sys.service.BmsIdenService;
import com.caiger.module.sys.service.DicService;
import com.caiger.module.sys.service.DseService;
import com.caiger.module.sys.service.MalIdenService;
import com.caiger.module.sys.service.StatusIdenService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("dev/mgt/")
public class DseController extends BaseController {

//	String dseid;

	@Autowired
	private DseService dseService;

	@Autowired
	private StatusIdenService statusIdenService;

	@Autowired
	private MalIdenService malIdenService;

	@Autowired
	private BmsIdenService bmsIdenService;

	@Autowired
	private DicService dicService;
	
	@Autowired
	private AcreditService acreditService;
	
	@Autowired
	private AlarmInfoService alarmInfoService;
	
	@Autowired
	private Acredit acredit;
	
	@Autowired
	private AlarmInfo alarmInfo;

	@RequestMapping(value = "list")
	public String list(Dse dse, Model model) {
		return "modules/dse/dse_mgt/dselist";
	}

	// 查找数据库中所有的设备信息
	@RequestMapping(value = "listData")
	@ResponseBody
	public PageInfo<Dse> listData(Dse dse, HttpServletRequest request, HttpServletResponse response) {
		startPage(request);
		List<Dse> dselList = dseService.getList(dse);
		PageInfo<Dse> pageInfo = new PageInfo<Dse>(dselList);
		return pageInfo;
	}

	@RequestMapping(value = "form")
	public String form(Dse dse, Model model) {
		if (StringUtils.isNotBlank(dse.getId())) {
			// 从数据库中拿到指定id的dse
			Dse dsedata = dseService.get(dse);
			model.addAttribute("dse", dsedata);
		}

		return "modules/dse/dse_mgt/dseForm";
	}

	@RequestMapping(value = "save")
	@ResponseBody
	public String save(Dse dse, HttpServletRequest request, Model model) {
		dse.setGuc(dse.getGuc().replaceAll(" ", ""));
		dseService.save(dse);
		// 判断user_dev中是否有相应的数据，如果有，也同时更新
		if(dse.getId() != null && (!dse.getId().equals(""))) {
			acredit.setDevid(dse.getId());
			acredit.setDevName(dse.getDevName());
			acredit.setDevNumber(dse.getDevNumber());
			acredit.setDevLocation(dse.getDevLocation());		
			acreditService.updateById(acredit);
		}
		return renderResult(Global.TRUE, text("保存【{0}】成功", dse.getDevName()));
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Dse dse) {
		acredit.setDevid(dse.getId());
		alarmInfo.setDev_id(dse.getId());
		dseService.deleteAll(dse);
		acreditService.delete(acredit);
		alarmInfoService.delete(alarmInfo);
		return renderResult(Global.TRUE, text("删除【{0}】成功", dse.getDevName()));
	}

	@RequestMapping(value = "statusList")
	public String statusList(Dse dse, Model model) {
		return "modules/dse/dse_mgt/statusList";
	}

	// 查找数据库中所有的设备状态信息
	@RequestMapping(value = "statusListData")
	@ResponseBody
	public PageInfo<Dse> statusListData(Dse dse, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> dicMap = dicService.queryAll();	
		startPage(request);
		List<Dse> dseList = dseService.getStatusList(dicMap,dse);
		PageInfo<Dse> pageInfo = new PageInfo<Dse>(dseList);
		return pageInfo;
	}

	@RequestMapping(value = "statusiden")
	public String statusiden(Dse dse, Model model) {
		if (StringUtils.isNotBlank(dse.getId())) {
			// 从数据库中拿到指定id的dse
			dse = dseService.get(dse);
		}
		model.addAttribute("dse", dse);
		return "modules/dse/dse_mgt/statusidenForm";
	}

	// 状态标志组
	@RequestMapping(value = "statusidenData")
	@ResponseBody
	public List<StatusIden> statusidenData(Dse dse, Model model) {

		StatusIden statusiden = new StatusIden();
		statusiden.setDev_id(dse.getId());
		List<StatusIden> statusidenList = statusIdenService.getList(statusiden);
		model.addAttribute(statusidenList);
		return statusidenList;
	}

	@RequestMapping(value = "maliden")
	public String maliden(Dse dse, Model model) {
		if (StringUtils.isNotBlank(dse.getId())) {
			// 从数据库中拿到指定id的dse
			dse = dseService.get(dse);
		}
		model.addAttribute("dse", dse);
		return "modules/dse/dse_mgt/malForm";
	}

	// 故障标志组
	@RequestMapping(value = "malidenData")
	@ResponseBody
	public List<MalIden> malidenData(Dse dse, Model model) {

		MalIden malIden = new MalIden();
		malIden.setDev_id(dse.getId());
		List<MalIden> malidenList = malIdenService.getList(malIden);
		model.addAttribute(malidenList);
		return malidenList;
	}

	@RequestMapping(value = "bmsiden")
	public String bmsiden(Dse dse, Model model) {
		if (StringUtils.isNotBlank(dse.getId())) {
			// 从数据库中拿到指定id的dse
			dse = dseService.get(dse);
		}
		model.addAttribute("dse", dse);
		return "modules/dse/dse_mgt/bmsidenForm";
	}

	// BMS标志组
	@RequestMapping(value = "bmsidenData")
	@ResponseBody
	public List<BmsIden> bmsidenData(Dse dse, Model model) {
		BmsIden bmsIden = new BmsIden();
		bmsIden.setDev_id(dse.getId());
		List<BmsIden> bmsidenList = bmsIdenService.getList(bmsIden);
		model.addAttribute(bmsidenList);
		return bmsidenList;
	}

	@RequestMapping(value = "otherInfo")
	public String otherInfo(Dse dse, Model model) {
		if (StringUtils.isNotBlank(dse.getId())) {
			// 从数据库中拿到指定id的dse
			dse = dseService.get(dse);
		}
		model.addAttribute("dse", dse);
		return "modules/dse/dse_mgt/otherInfoForm";
	}

	// 其他信息
	@RequestMapping(value = "otherInfoData")
	@ResponseBody
	public List<Dse> otherInfoData(Dse dse, Model model) {
		model.addAttribute("dse", dse);
		List<Dse> otherList = dseService.getOtherInfoList(dse);
		model.addAttribute(otherList);
		return otherList;
	}
}
