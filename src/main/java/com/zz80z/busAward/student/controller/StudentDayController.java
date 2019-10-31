package com.zz80z.busAward.student.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zz80z.busAward.common.coin.service.CoinService;
import com.zz80z.busAward.common.model.Award;
import com.zz80z.busAward.common.model.BRecord;
import com.zz80z.busAward.common.model.Coin;
import com.zz80z.busAward.common.model.StuDay;
import com.zz80z.busAward.common.model.Student;
import com.zz80z.busAward.common.utils.ExcelUtils;
import com.zz80z.busAward.student.service.StudentDayService;
import com.zz80z.busAward.student.service.StudentService;
import com.zz80z.busAward.system.service.SemesterService;

@Controller
@RequestMapping("studay")
public class StudentDayController {

	@Autowired
	StudentService stuService;
	@Autowired
	StudentDayService stuDayService;
	@Autowired
	SemesterService semesterService;
	@Autowired
	CoinService coinService;

	/**
	 * 学生日常跳转
	 * @throws ParseException String转DATE
	 */
	@RequestMapping("stuDay")
	public ModelAndView stuDay(@RequestParam(defaultValue="0",required=false) Integer nWeek,@RequestParam(defaultValue="1",required=false)String classMark,@RequestParam(value = "pn", defaultValue = "1") Integer pn) throws ParseException{			
		System.out.println("123");
		//获取学生列表及需要展示的关联信息
		Map stuDayMap=stuDayService.getMap(nWeek);
		//获取sql语句条件
		Map<String, String> sqlMap=(Map<String, String>) stuDayMap.get("nWeekDateMap");
		sqlMap.put("classMark", classMark);
		//设置分页参数
		PageHelper.startPage(pn, 5);
        List<StuDay> countLastWeekStuDay=stuDayService.countLastWeekStuDay(sqlMap);
        PageInfo pageInfo = new PageInfo(countLastWeekStuDay);
        stuDayMap.put("classMark", classMark);
        stuDayMap.put("pageInfo", pageInfo);       
		return new ModelAndView("stu/stuDay","stuDayMap",stuDayMap);
	}
 /**
  * 下载学生日常模板
  * @param response
  * @param request
  */
	@RequestMapping("creatStuDayExcel")
	public void creatStuDayExcel(HttpServletResponse response,HttpServletRequest request){
		ExcelUtils excel=new ExcelUtils();
		String fileName = "学生日常表模板.xls";
		String header="学生日常模板";
		List<String> columns=new ArrayList<String>();
		columns.add("学号");
		columns.add("姓名");
		columns.add("出勤");
		columns.add("仪容");
		columns.add("早读");
		columns.add("课堂纪律");
		columns.add("违纪处分");
		columns.add("语文");
		columns.add("数学");
		columns.add("英语");
		columns.add("政治");
		columns.add("历史");
		columns.add("地理");
		columns.add("生物");
		columns.add("物理");
		columns.add("化学");
		columns.add("间操");
		columns.add("集会");
		columns.add("升旗");
		columns.add("卫生");
		columns.add("日期");
		try {
			excel.creatExcel(response, request, fileName, header, columns);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	
	}
	/**
	 * 学生日常导入
	 * @param file
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping("upstyDay")
	@ResponseBody
	public String upstyDay(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException, ParseException{
		List<Map<String, String>> list = ExcelUtils.readExcel(file);
		List<StuDay> stuDayList=new ArrayList<StuDay>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (Map<String, String> map : list) {
			StuDay stuDay=new StuDay();		
			Student student=stuService.selectByStuNo(map.get("学号"));
			//根据学号查询学生
			stuDay.setStuId(student.getstuId());
			stuDay.setAttendance(Integer.parseInt(map.get("出勤")));
			stuDay.setAppearance(Integer.parseInt(map.get("仪容")));
			stuDay.setMorning(Integer.parseInt(map.get("早读")));
			stuDay.setDiscipline(Integer.parseInt(map.get("课堂纪律")));
			stuDay.setPunishment(Integer.parseInt(map.get("违纪处分")));
			stuDay.setAssembly(Integer.parseInt(map.get("集会")));
			stuDay.setFlag(Integer.parseInt(map.get("升旗")));
			stuDay.setHealth(Integer.parseInt(map.get("卫生")));
			stuDay.setChinese(Integer.parseInt(map.get("语文")));
			stuDay.setMath(Integer.parseInt(map.get("数学")));
			stuDay.setEnglish(Integer.parseInt(map.get("英语")));
			stuDay.setPolitics(Integer.parseInt(map.get("政治")));
			stuDay.setHistory(Integer.parseInt(map.get("历史")));
			stuDay.setGeography(Integer.parseInt(map.get("地理")));
			stuDay.setBiology(Integer.parseInt(map.get("生物")));
			stuDay.setPhysics(Integer.parseInt(map.get("物理")));
			stuDay.setChemistry(Integer.parseInt(map.get("化学")));
			String birthday=String.valueOf(map.get("日期"));
			Calendar calendar = new GregorianCalendar(1900,0,-1);  
			Date d = calendar.getTime();  
		    Date dd = DateUtils.addDays(d,Integer.valueOf(birthday)); 
			stuDay.setDay(sdf.parse(sdf.format(dd)));
			stuDay.setCexercise(Integer.parseInt(map.get("间操")));
			stuDayList.add(stuDay);
		}
		try {
			stuDayService.insertStuDayList(stuDayList);
			return "1";
		} catch (Exception e) {
			return "0";
		}
	}
	/**
	 * 分配巴士币
	 * @param data
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "insertCoin")
	@ResponseBody
	public Map insertCion(String data,HttpServletResponse response){
		response.setContentType("application/json;charset=utf-8");
		Map resultMap = new HashMap<>();
	//解析数据
		String [] datas=data.split(",");
		//巴士币类型
		String coinType=datas[datas.length-1];
		//促进币点数
		Integer points=0;
		int [] stuId = new int[datas.length-3];
		if(datas.length==5){
			points=Integer.parseInt(datas[datas.length-4]);
			stuId=new int[datas.length-4];
		}
		//查询奖励点数
		Award award=coinService.selectAward();
		//班级标识
		String classMark=datas[datas.length-2];
		//周
		String week=datas[datas.length-3];
		//学生id存放
		
		for(int i=0;i<stuId.length;i++){
			stuId[i]=Integer.parseInt(datas[i]);
		}
		//前台选择学生个数
		int stu=stuId.length;
		//创建查询条件
		Map sqlMap=new HashMap();
		sqlMap.put("classMark", classMark);
		sqlMap.put("week", week);
		sqlMap.put("stuId", stuId);
		switch (coinType) {
		case "关爱":
			sqlMap.put("coinType", "星级币");
			sqlMap.put("description", "关爱之星");
			String careFor=createCoinList(coinType,"星级币", stuId, Integer.parseInt(week),sqlMap,award.getLevel());
			resultMap.put("result", careFor);
			return resultMap;
		case "服务":
			sqlMap.put("coinType", "星级币");
			sqlMap.put("description", "服务之星");
			String service=createCoinList(coinType,"星级币", stuId, Integer.parseInt(week), sqlMap,award.getLevel());
			resultMap.put("result", service);
			return resultMap;
		case "正义":
			sqlMap.put("coinType", "星级币");
			sqlMap.put("description", "正义之星");
			String justice=createCoinList(coinType,"星级币", stuId, Integer.parseInt(week), sqlMap,award.getLevel());
			resultMap.put("result", justice);
			return resultMap;
		case "诚信":
			sqlMap.put("coinType", "星级币");
			sqlMap.put("description", "诚信之星");
			String honesty=createCoinList(coinType,"星级币", stuId, Integer.parseInt(week), sqlMap,award.getLevel());
			resultMap.put("result", honesty);
			return resultMap;
		case "进步":
			sqlMap.put("coinType", "星级币");
			sqlMap.put("description", "进步之星");
			String progress=createCoinList(coinType, "星级币",stuId, Integer.parseInt(week), sqlMap,award.getLevel());
			resultMap.put("result", progress);
			return resultMap;
		case "促进":
			sqlMap.put("coinType", "灵活币");
			sqlMap.put("description", "促进之星");
			String promote=createCoinList(coinType,"灵活币", stuId, Integer.parseInt(week), sqlMap,points);
			resultMap.put("result", promote);
			return resultMap;
		}
		return null;
	}
	//创建添加列表
	public String createCoinList(String coinType,String description,int [] stuId,int week,Map sqlMap,Integer  points){
		List<Student> studentList=stuDayService.selectBrcord(sqlMap);
		if(studentList.size()==0){
		List<BRecord> bRcordsLists=stuDayService.selectBrcordList(sqlMap);
		int num=bRcordsLists.size();
		int stu=stuId.length;
		if(stu>(3-num)){
			return "分配失败,本周"+coinType+"之星还剩"+(3-num)+"个名额";
		}else{
		//创建巴士币列表
		List<Coin>coinList=new ArrayList<Coin>();
		//创建学生和先进币列表
		List<BRecord>bRecordList=new ArrayList<BRecord>();
		for (int stuid : stuId) {
			Coin coin=new Coin();
			coin.setStuId(stuid);
			coin.setLevelCoin(1);
			coinList.add(coin);
			BRecord bRecord=new BRecord();
			bRecord.setStuId(stuid);
			bRecord.setPoints(points);
			bRecord.setCoinType(description);
			bRecord.setWeek(week);
			bRecord.setCreateTime(new Date());
			bRecord.setDescription(coinType+"之星");
			bRecordList.add(bRecord);
		}
		coinService.insertFromCoin(coinList);
		coinService.insertFromBRecord(bRecordList);
		return "分配成功";
		}
		}else{
			String stuName="";
			for (Student student : studentList) {
				stuName+=student.getrealName()+",";
			}
			return 	"本周："+stuName+"已分配，请从重新选择";
		}
	}
}
