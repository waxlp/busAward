package com.zz80z.busAward.student.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.zz80z.busAward.common.model.Classs;
import com.zz80z.busAward.common.model.Semester;
import com.zz80z.busAward.common.model.StudentChart;
import com.zz80z.busAward.common.utils.WeekUtils;
import com.zz80z.busAward.student.service.StudentChartService;
import com.zz80z.busAward.student.service.StudentService;
import com.zz80z.busAward.system.service.SemesterService;

@Controller
@RequestMapping("StuDayChart")
public class StudentChartController {
	
	@Autowired
	StudentChartService studentChartService;
	@Autowired
	StudentService studentService;
	@Autowired
	SemesterService semesterService;
	//每个人当学期、当周、当月、当学期结果排名
	@RequestMapping("returnStuDayChart")
	@ResponseBody
	public ModelAndView returnStuDayChart(@RequestParam(defaultValue="14",required=false)String classId){
		WeekUtils weekUtils=new WeekUtils();
 		Date dayDate=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String dayDates=sdf.format(dayDate);
		Map sqlMap=new HashMap();
		//获取班级列表
	    List<Classs> classList=studentService.selectClasssList();
	    Map stuChartMap=new HashMap();
	    stuChartMap.put("classList",classList);
	    stuChartMap.put("classId", classId);
		sqlMap.put("classId", classId);
			sqlMap.put("dayDate", dayDates);
			//当天学生列表
			Map<Integer, StudentChart> stuDayCharts=studentChartService.stuDayChartList(sqlMap);
			stuChartMap.put("stuDayCharts", stuDayCharts);
			//当周学生列表
			Map dateMap=weekUtils.getNTimeInterval(0);
			dateMap.put("classId", classId);
			 Map<Integer, StudentChart> stuWeekCharts=studentChartService.stuWeekChartList(dateMap);
			stuChartMap.put("stuWeekCharts", stuWeekCharts);
			//当月学生列表
			Map mothMap=weekUtils.getThisMothBeginAndEndTime();
			mothMap.put("classId", classId);
			Map<Integer, StudentChart> stuMothCharts=studentChartService.stuMothChartList(mothMap);
			stuChartMap.put("stuMothCharts", stuMothCharts);
			//当学期学生列表
			List<Semester> semesters=semesterService.findSemesters();
			Map semesterMap=new HashMap();
			semesterMap.put("beginTime",sdf.format(semesters.get(0).getCreatTime()));
			semesterMap.put("endTime",sdf.format(semesters.get(0).getRecessTime()));
			semesterMap.put("classId",classId);
			Map<Integer, StudentChart> stuSemesterCharts=studentChartService.stuSemesterChartList(semesterMap);
			if(stuDayCharts.isEmpty()||stuWeekCharts.isEmpty()||stuMothCharts.isEmpty()||semesters.isEmpty()){
				stuChartMap.put("stuSemesterCharts", null);
			}else{
				stuChartMap.put("stuSemesterCharts", stuSemesterCharts);
			}
			return new ModelAndView("stu/chart","stuChartMap",stuChartMap);
	}
	
	/**
	 * 每个小组当周、当月、当学期卫生、纪律、作业的平均分及排名  柱状图、扇面图
	 * @param classMark
	 */
	@RequestMapping("stuGroupCharts")
	@ResponseBody
	public Map stuGroupChart(String data){
		String classId=data;
		Date dayDate=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		WeekUtils weekUtils=new WeekUtils();
		//获取当周时间段
		Map dateMap=weekUtils.getNTimeInterval(0);
		dateMap.put("classId", classId);
		Map stuWeekCount=new HashMap<>();
		//当周卫生统计
		Map<String,StudentChart> stuHealthWeekList=studentChartService.stuHealthList(dateMap);
		//当周纪律统计
		Map<String,StudentChart> stuDisciplineWeekList=studentChartService.stuDisciplineList(dateMap);
		//当周作业统计
		Map<String,StudentChart> stuTaskWeekList=studentChartService.stuTaskList(dateMap);
		
		
		
		//获取当月时间段
		Map mothMap=weekUtils.getThisMothBeginAndEndTime();
		mothMap.put("classId", classId);
		//当月卫生统计
		Map<String,StudentChart> stuHealthMothList=studentChartService.stuHealthList(mothMap);
		//当月纪律统计
		Map<String,StudentChart> stuDisciplineMothList=studentChartService.stuDisciplineList(mothMap);
		//当月作业统计
		Map<String,StudentChart> stuTaskMothList=studentChartService.stuTaskList(mothMap);
		
		
		//当学期统计
		List<Semester> semesters=semesterService.findSemesters();
		Map semesterMap=new HashMap();
		semesterMap.put("beginTime",sdf.format(semesters.get(0).getCreatTime()));
		semesterMap.put("endTime",sdf.format(semesters.get(0).getRecessTime()));
		semesterMap.put("classId",classId);
		//当学期卫生统计
		Map<String,StudentChart>stuHealthSemesterList=studentChartService.stuHealthList(semesterMap);
		//当学期纪律统计
		Map<String,StudentChart> stuDisciplineSemesterList=studentChartService.stuDisciplineList(semesterMap);
		//当学期作业统计
		Map<String,StudentChart> stuTaskSemesterList=studentChartService.stuTaskList(semesterMap);
		Map<String,Map> stuScoreChart=new HashMap<String,Map>();
		String [] stuGroup=studentChartService.selectStuGroupByClassId(classId);
		for (String key : stuGroup) {
			Map stuCharts=new HashMap<>();
			if(stuHealthWeekList.get(key)!=null){
				stuCharts.put("stuHealthWeek", stuHealthWeekList.get(key));
				stuScoreChart.put(key, stuCharts);
			}
			if(stuDisciplineWeekList.get(key)!=null){
				stuCharts.put("stuDisciplineWeek", stuDisciplineWeekList.get(key));
				stuScoreChart.put(key, stuCharts);
			}
			if( stuTaskWeekList.get(key)!=null){
				stuCharts.put("stuTaskWeek", stuTaskWeekList.get(key));
				stuScoreChart.put(key, stuCharts);
			}
			if(  stuHealthMothList.get(key)!=null){
				stuCharts.put("stuHealthMoth", stuHealthMothList.get(key));
				stuScoreChart.put(key, stuCharts);
			}
			if( stuDisciplineMothList.get(key)!=null){
				stuCharts.put("stuDisciplineMoth", stuDisciplineMothList.get(key));
				stuScoreChart.put(key, stuCharts);
			}
			if( stuTaskMothList.get(key)!=null){
				stuCharts.put("stuTaskMoth", stuTaskMothList.get(key));
				stuScoreChart.put(key, stuCharts);
			}
			if(stuHealthSemesterList.get(key)!=null){
				stuCharts.put("stuHealthSemester", stuHealthSemesterList.get(key));
				stuScoreChart.put(key, stuCharts);
			}
			if(stuDisciplineSemesterList.get(key)!=null){
				stuCharts.put("stuDisciplineSemester", stuDisciplineSemesterList.get(key));
				stuScoreChart.put(key, stuCharts);
			}
			if(stuTaskSemesterList.get(key)!=null){
				stuCharts.put("stuTaskSemester", stuTaskSemesterList.get(key));
				stuScoreChart.put(key, stuCharts);
			}
			
		}
		return stuScoreChart;
	}
	
	/**
	 * ajax学生日常报表
	 */
	@RequestMapping("studentDayCharts")
	@ResponseBody
	public Map studentDayCharts(String data){
		String classId=data;
		WeekUtils weekUtils=new WeekUtils();
 		Date dayDate=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String dayDates=sdf.format(dayDate);
		Map sqlMap=new HashMap();
		//获取班级列表
	    List<Classs> classList=studentService.selectClasssList();
	    Map stuChartMap=new HashMap();
	    stuChartMap.put("classList",classList);
	    stuChartMap.put("classId", classId);
		sqlMap.put("classId", classId);
			sqlMap.put("dayDate", "2018-04-30");
			//当天学生列表
			Map<Integer, StudentChart> stuDayCharts=studentChartService.stuDayChartList(sqlMap);
			
			//当周学生列表
			Map dateMap=weekUtils.getNTimeInterval(0);
			dateMap.put("beginTime", "2018-04-30");
			dateMap.put("endTime", "2018-05-06");
			dateMap.put("classId", classId);
			Map<Integer, StudentChart> stuWeekCharts=studentChartService.stuWeekChartList(dateMap);
			
			//当月学生列表
			Map mothMap=weekUtils.getThisMothBeginAndEndTime();
			mothMap.put("beginTime","2018-05-01");
			mothMap.put("endTime","2018-05-31");
			mothMap.put("classId", classId);
			Map<Integer, StudentChart> stuMothCharts=studentChartService.stuMothChartList(mothMap);
		
			//当学期学生列表
			List<Semester> semesters=semesterService.findSemesters();
			Map semesterMap=new HashMap();
			semesterMap.put("beginTime",sdf.format(semesters.get(0).getCreatTime()));
			semesterMap.put("endTime",sdf.format(semesters.get(0).getRecessTime()));
			/*semesterMap.put("beginTime","2018-04-30");
			semesterMap.put("endTime","2018-05-04");*/
			semesterMap.put("classId",classId);
			Map<Integer, StudentChart> stuSemesterCharts=studentChartService.stuSemesterChartList(semesterMap);
			
			
			Map <String,Map>studentCharMap=new HashMap<>();
			String [] stuIds=studentChartService.selectStuIdByClassId(classId);
			for (String id : stuIds) {
				Map charts=new HashMap();
				if(null!=stuDayCharts.get(Integer.parseInt(id))){
				charts.put("stuDayCharts", stuDayCharts.get(Integer.parseInt(id)));
				studentCharMap.put(id, charts);
				}
				if(null!=stuWeekCharts.get(Integer.parseInt(id))){
					charts.put("stuWeekCharts", stuWeekCharts.get(Integer.parseInt(id)));
					studentCharMap.put(id, charts);
				}
				if(null!=stuMothCharts.get(Integer.parseInt(id))){
					charts.put("stuMothCharts", stuMothCharts.get(Integer.parseInt(id)));
					studentCharMap.put(id, charts);
				}
				if(null!=stuSemesterCharts.get(Integer.parseInt(id))){
					charts.put("stuSemesterCharts", stuSemesterCharts.get(Integer.parseInt(id)));
					studentCharMap.put(id, charts);
				}
			}
		return studentCharMap;
	}
}
