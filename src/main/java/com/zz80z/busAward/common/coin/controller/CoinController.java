package com.zz80z.busAward.common.coin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.chainsaw.Main;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.zz80z.busAward.common.coin.service.CoinService;
import com.zz80z.busAward.common.dao.SemesterMapper;
import com.zz80z.busAward.common.model.Award;
import com.zz80z.busAward.common.model.BRecord;
import com.zz80z.busAward.common.model.Coin;
import com.zz80z.busAward.common.model.Semester;
import com.zz80z.busAward.common.model.Student;
import com.zz80z.busAward.common.utils.WeekUtils;
import com.zz80z.busAward.student.service.StudentDayService;
import com.zz80z.busAward.system.service.AwardService;
import com.zz80z.busAward.user.bo.ClassWeekAdvanced;
import com.zz80z.busAward.user.bo.StudentAdvancedGroup;

@Controller
@RequestMapping("coin")
public class CoinController {

	@Autowired
	CoinService coinService;
	@Autowired
	StudentDayService stuDayService;
	@Autowired
	AwardService awardService;
	@Autowired
	SemesterMapper semesterMapper;
	@RequestMapping("allotSCoin") 
	public ModelAndView returnScoin(@RequestParam(defaultValue="0",required=false) Integer nWeek,
				@RequestParam(defaultValue="202001",required=false)String classMark,
				@RequestParam(defaultValue="先进",required=false)String coinGroup){
		Map stuCoinMap=stuDayService.getMap(nWeek);
		//获取sql语句条件
		Map<String, String> sqlMap=(Map<String, String>) stuCoinMap.get("nWeekDateMap");
		sqlMap.put("classMark", classMark);	
		stuCoinMap.put("classMark", classMark);
		if(coinGroup.equals("先进")){	
		List<StudentAdvancedGroup> sTGList=coinService.lastWeekAdvancedGroup(sqlMap);
		stuCoinMap.put("sTGList",sTGList);
		stuCoinMap.put("group","先进");
		}else{
			//创建查询条件
			Map<String, String> sqlmap=new HashMap<String, String>();
			Map map=queryTime(nWeek);
			map.put("classMark", classMark);
			sqlmap.put("classMark", classMark);
			StudentAdvancedGroup studentAdvancedGroup=coinService.selectLastRank(map);
			if(studentAdvancedGroup==null){
				stuCoinMap.put("sTGList",new ArrayList<>());
			}else{
				stuCoinMap.put("sTGList",studentAdvancedGroup);
			}
			stuCoinMap.put("group","进步");
		}		
		return new ModelAndView("coin/allotSCoin","stuCoinMap",stuCoinMap);	
	}
	/**
	 * 分配团队币
	 * @param data
	 * @return
	 */
	@RequestMapping(value="insertAdvanced")
	@ResponseBody
	public Map insertAdvanced(String data,HttpServletResponse response){
		response.setContentType("application/json;charset=utf-8");
		Map resultMap=new HashMap<>();
		//解析数据
		String [] Data=data.split(",");
		String flag=Data[0];
		Integer nWeek=Integer.parseInt(Data[1]);
		String classMark=Data[2];
		Map stuCoinMap=stuDayService.getMap(nWeek);
		//获取sql语句条件
		Map<String, String> sqlMap=(Map<String, String>) stuCoinMap.get("nWeekDateMap");
		sqlMap.put("classMark", classMark);
		//查询奖励点数
		Award award=coinService.selectAward();
		if(flag.equals("先进")){
		List<StudentAdvancedGroup> sTGList=coinService.lastWeekAdvancedGroup(sqlMap);
		if(sTGList.size()!=0){
			String [] groupname=new String[sTGList.size()];
			for (int i=0;i<sTGList.size();i++) {
				groupname[i]=sTGList.get(i).getStuGroup();		
			}
			//根据小组和班级查询学生列表
			Map mapSql=new HashMap();
			mapSql.put("classMark", classMark);
			mapSql.put("groupname", groupname);
			List<Student> stuList=coinService.advancedGroupStudent(mapSql);
			//创建巴士币列表
			List<Coin>coinList=new ArrayList<Coin>();
			//创建学生和先进币列表
			List<BRecord>BRecordList=new ArrayList<BRecord>();	
			for(Student student:stuList){
				Coin coin=new Coin();
				BRecord bRecord=new BRecord();
				coin.setStuId(student.getstuId());
				coin.setTeamCoin(1);
				bRecord.setStuId(student.getstuId());
				bRecord.setPoints(award.getAdvancedGroup());
				bRecord.setCoinType("团队币");
				bRecord.setWeek(nWeek);
				bRecord.setCreateTime(new Date());
				bRecord.setDescription("先进小组");
				coinList.add(coin);
				BRecordList.add(bRecord);
			}
			try {
				Map<String, String> sqlmap=new HashMap<String, String>();
				sqlmap.put("nWeek", String.valueOf(nWeek));
				sqlmap.put("classMark", classMark);
				sqlmap.put("coinType", "团队币");
				sqlmap.put("description", "先进小组");
				List<BRecord> bRecordList=coinService.selectRecord(sqlmap);
				//判断信息是否已经存在
				if(bRecordList.size()==0){
					coinService.insertFromCoin(coinList);
					coinService.insertFromBRecord(BRecordList);
					resultMap.put("result", "分配成功");
					return resultMap;
				}else{
					resultMap.put("result", "当前班级已经分配过了");
					return resultMap;
				}		
			} catch (Exception e) { 
				resultMap.put("result", "出错了");
				return resultMap;
			}
		}else{
			resultMap.put("result", "空的操作");
			return resultMap;
		}
		}else if(flag.equals("进步")){
			//创建查询条件
			Map<String, String> sqlmap=new HashMap<String, String>();
			sqlmap.put("nWeek", String.valueOf(nWeek));
			sqlmap.put("classMark", classMark);
			sqlmap.put("description", "进步小组");
			sqlmap.put("coinType", "团队");
			sqlmap.put("rnWeek", String.valueOf(nWeek-1));			
			List<BRecord> bRecordList=coinService.selectRecord(sqlmap);
			if(bRecordList.size()==0){
				Map map=queryTime(nWeek);
				map.put("classMark", classMark);
				StudentAdvancedGroup studentAdvancedGroup=coinService.selectLastRank(map);
				sqlmap.put("group", studentAdvancedGroup.getStuGroup());
				List<Student> stuList=coinService.selectStudentByGroup(sqlmap);
				//创建巴士币列表
				List<Coin>coinList=new ArrayList<Coin>();
				//创建学生和进步币列表
				List<BRecord>BRecordList=new ArrayList<BRecord>();	
				for (Student student : stuList) {
					Coin coin=new Coin();
					BRecord bRecord=new BRecord();
					coin.setStuId(student.getstuId());
					coin.setTeamCoin(1);
					bRecord.setStuId(student.getstuId());
					bRecord.setPoints(award.getProgressGroup());
					bRecord.setCoinType("团队币");
					bRecord.setWeek(nWeek);
					bRecord.setCreateTime(new Date());
					bRecord.setDescription("进步小组");
					coinList.add(coin);
					BRecordList.add(bRecord);
				}
				coinService.insertFromCoin(coinList);
				coinService.insertFromBRecord(BRecordList);
				resultMap.put("result", "分配成功");
				return resultMap;	
			}else{
				resultMap.put("result", "当周已分配过了");
				return resultMap;		
			}
		}
		return null;		
	}
	
	/**
	 * 查询每周先进班级
	 * @param gradeName
	 * @return
	 */
	@RequestMapping("allotCCoin")
	public ModelAndView returnAllotCCoin(@RequestParam(defaultValue="0",required=false) Integer nWeek,@RequestParam(defaultValue="1",required=false)int gradeName){
		Map stuDayMap=stuDayService.getMap(nWeek);
		Map sqlMap=(Map<String, String>) stuDayMap.get("nWeekDateMap");
		sqlMap.put("gradeName", gradeName);
		List<ClassWeekAdvanced> classWeekAdvancedList=coinService.selectAdvancedClass(sqlMap);
		stuDayMap.put("classWeekAdvancedList", classWeekAdvancedList);
		stuDayMap.put("gradeName",gradeName);
		return new ModelAndView("coin/allotCCoin","stuDayMap",stuDayMap);
	}
	
	/**
	 * 分配周先进币
	 */
	@RequestMapping("insertAdvancedClass")
	@ResponseBody
	public Map insertAdvancedClass(String data,HttpServletResponse response){
		response.setContentType("applcation/json;charset=utf-8");
		Map resultMap=new HashMap();
		String [] datas=data.split(",");
		int nWeek=Integer.parseInt(datas[0]);
		int gradeName=Integer.parseInt(datas[1]);
		Map stuDayMap=stuDayService.getMap(nWeek);
		Map sqlMap=(Map<String, String>) stuDayMap.get("nWeekDateMap");
		sqlMap.put("gradeName", gradeName);
		List<ClassWeekAdvanced> classWeekAdvancedList=coinService.selectAdvancedClass(sqlMap);
		String [] classMarks = new String [classWeekAdvancedList.size()];
		for(int i=0;i<classWeekAdvancedList.size();i++){
			classMarks[i]=classWeekAdvancedList.get(i).getStuClassMark();
		}
		List<Student> stuList=coinService.selectStudentByClassMark(classMarks);
		Award award=awardService.selectByPrimaryKey(1);
		int points=award.getAdvancedClass();
		List<BRecord> bRecords = new ArrayList<>();
		String [] stuIds=new String[stuList.size()];
		for (int i=0;i< stuList.size();i++) {
			stuIds[i]=String.valueOf(stuList.get(i).getstuId());
		}
		Map sqlMaps=new HashMap<>();
		sqlMaps.put("stuIds", stuIds);
		sqlMaps.put("nWeek", nWeek);
		sqlMaps.put("coinType", "周先进币");
		Boolean result=coinService.selectBrecordByStuIds(sqlMaps);
		if(result){
			for (Student student : stuList) {
				BRecord bRecord=new BRecord();
				bRecord.setStuId(student.getstuId());
				bRecord.setCoinType("周先进币");
				bRecord.setPoints(points);
				bRecord.setCreateTime(new Date());
				bRecord.setDescription("周先进班级");
				bRecord.setWeek(nWeek);
				bRecords.add(bRecord);
			}
			coinService.insertFromBRecord(bRecords);
			resultMap.put("result", "添加成功");
			return resultMap;
		}else{
			resultMap.put("result", "已添加过了");
			return resultMap;
		}
	}
	/**
	 * 根据当传入的周查询时间段 和上周时间段
	 * @param nWeek
	 * @return
	 */
	public Map queryTime(int nWeek){
		WeekUtils weekUtils =new WeekUtils();
		List<Semester> semesters=semesterMapper.findSemesters();
		Map<Integer, Integer> thisWeek= stuDayService.getNWeek(semesters.get(0).getWhatWeek()-1);
		Map map=new HashMap<>();
			if (thisWeek.size()<nWeek) {
				Map thisBeginAndEndTime=weekUtils.getNTimeInterval(thisWeek.get(nWeek+1));
				Map lastBeginAndEndTime=weekUtils.getNTimeInterval(thisWeek.get(nWeek));
				map.put("thisBeginDate", thisBeginAndEndTime.get("beginTime"));
				map.put("thisEndDate", thisBeginAndEndTime.get("endTime"));
				map.put("lastBeginDate", lastBeginAndEndTime.get("beginTime"));
				map.put("lastEndDate", lastBeginAndEndTime.get("endTime"));
				return map;
			}else{
				map.put("thisBeginDate", "null");
				map.put("thisEndDate", "null");
				map.put("lastBeginDate", "null");
				map.put("lastEndDate", "null");
				return map;
			}
	}
}
