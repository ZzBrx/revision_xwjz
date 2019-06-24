package com.caiger.module.sys.web;

import java.util.Arrays;
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
import com.caiger.common.web.BaseController;
import com.caiger.module.sys.entity.AlarmInfo;
import com.caiger.module.sys.entity.Dse;
import com.caiger.module.sys.service.AlarmInfoService;
import com.caiger.module.sys.service.DicService;
import com.caiger.module.sys.utils.ToolUtil;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("alarm/")
public class AlarmController extends BaseController {

	@Autowired
	private AlarmInfoService alarmInfoService;

	@Autowired
	private DicService dicService;

	@RequestMapping(value = "historyAlarmList")
	public String historyAlarmList(Dse dse, Model model) {
		return "modules/alarm/alarm_msg/historyAlarmList";
	}

	/**
	 * 查询历史告警信息
	 *
	 */
	@RequestMapping(value = "historyAlarmListData")
	@ResponseBody
	public PageInfo<AlarmInfo> historyAlarmListData(AlarmInfo alarmInfo, HttpServletRequest request,
			HttpServletResponse response) {
		HashMap<String, String> dicMap = dicService.queryAll();
		alarmInfo.setAlarmType(ToolUtil.getKey(dicMap,alarmInfo.getAlarmType()));
		startPage(request);
		List<AlarmInfo> alarmList = alarmInfoService.getStatusList("history", dicMap, alarmInfo);
		PageInfo<AlarmInfo> pageInfo = new PageInfo<AlarmInfo>(alarmList);
		return pageInfo;
	}

	@RequestMapping(value = "currentAlarmList")
	public String currentAlarmList(Dse dse, Model model) {
		return "modules/alarm/alarm_msg/currentAlarmList";
	}

	/**
	 * 查询当前告警信息
	 *
	 */
	@RequestMapping(value = "currentAlarmListData")
	@ResponseBody
	public PageInfo<AlarmInfo> currentAlarmListData(AlarmInfo alarmInfo, HttpServletRequest request,
			HttpServletResponse response) {
		HashMap<String, String> dicMap = dicService.queryAll();
		alarmInfo.setAlarmType(ToolUtil.getKey(dicMap,alarmInfo.getAlarmType()));
		startPage(request);
		List<AlarmInfo> alarmList = alarmInfoService.getStatusList("current", dicMap, alarmInfo);
		PageInfo<AlarmInfo> pageInfo = new PageInfo<AlarmInfo>(alarmList);
		return pageInfo;
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AlarmInfo alarmInfo) {
		alarmInfoService.delete(alarmInfo);
		return renderResult(Global.TRUE, text("删除【{0}】成功", alarmInfo.getId()));
	}

	/**
	 * 批量删除
	 *
	 */
	@RequestMapping(value = "deleteBatches")
	@ResponseBody
	public String deleteBatches(AlarmInfo alarmInfo) {
		if (!alarmInfo.getId().equals("")) {
			String[] split = alarmInfo.getId().split(",");
			List<String> idList = Arrays.asList(split);
			alarmInfoService.deleteBatches(idList);
			return renderResult(Global.TRUE, text("删除成功"));
		}
		return null;
	}

}
