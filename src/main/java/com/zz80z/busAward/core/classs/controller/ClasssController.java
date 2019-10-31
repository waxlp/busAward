package com.zz80z.busAward.core.classs.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zz80z.busAward.common.controller.BaseController;
import com.zz80z.busAward.common.model.ClassDay;
import com.zz80z.busAward.common.model.ClassTeacher;
import com.zz80z.busAward.common.model.Classs;
import com.zz80z.busAward.common.model.Semester;
import com.zz80z.busAward.common.model.Subject;
import com.zz80z.busAward.common.model.Teacher;
import com.zz80z.busAward.common.utils.ExcelUtils;
import com.zz80z.busAward.core.classs.service.ClasssService;
import com.zz80z.busAward.core.shiro.token.manager.TokenManager;
import com.zz80z.busAward.student.service.StudentDayService;
import com.zz80z.busAward.student.service.StudentService;
import com.zz80z.busAward.system.service.SemesterService;
import com.zz80z.busAward.user.bo.classAndClassDay;

@SuppressWarnings("all")
@Controller
@RequestMapping("classs")
public class ClasssController  extends BaseController{
	@Autowired
	ClasssService	classService;
	@Autowired
	SemesterService semesterService;
	@Autowired
	StudentDayService stuDayService;
	@Autowired
	StudentService stuService;
	/**
	 * 班级日常跳转
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("classsDay")
	public ModelAndView classsDay(@RequestParam(defaultValue="0",required=false)
			Integer nWeek,
		@RequestParam(defaultValue="",required=false)
			String classMark,
		@RequestParam(value = "pn", defaultValue = "1")
			Integer pn) throws ParseException{
		Map ClassDayMap=stuDayService.getMap(nWeek);
		Map<String, String> sqlMap=(Map<String, String>) ClassDayMap.get("nWeekDateMap");
		sqlMap.put("classMark", classMark);
		//设置分页参数
		PageHelper.startPage(pn, 5);
		List<ClassDay> classDayList=classService.countClassDayAndClass(sqlMap);
		PageInfo pageInfo = new PageInfo(classDayList);
		ClassDayMap.put("pageInfo", pageInfo);
		ClassDayMap.put("classMark",classMark);
		ClassDayMap.put("nWeek",nWeek);
		return new ModelAndView("class/classDay","ClassDayMap",ClassDayMap);
	}
	/**
	 * 班级列表跳转
	 * @return
	 */
	@RequestMapping(value="classUp")
	public ModelAndView classList(
		@RequestParam(defaultValue="",required=false)String gradeNameOrClassName,
		@RequestParam(defaultValue="1",required=false)int gradeName,
		@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
		Map classUp=new HashMap();
		List <Teacher>teacherList=classService.selectTeacherAndSubject();
		//Pagination<Classs> page = classService.findPage(resultMap, pageNo, pageSize);
		PageHelper.startPage(pn, 8);
		Map sqlMap=new HashMap<>();
		sqlMap.put("className", gradeNameOrClassName);
		sqlMap.put("reserve", "1");
		List<Classs> classList=classService.selectClassAndTeacher(sqlMap);
		PageInfo pageInfo = new PageInfo(classList);
		classUp.put("pageInfo", pageInfo);
		classUp.put("teacherList", teacherList);
		//classUp.put("page", page);
	/*	List<Subject>subjectList=classService.subjectList(1);
		Map<String,List<Teacher>> teacherMapByGrade=insertClass(subjectList);
		classUp.put("teacherMapByGrade", teacherMapByGrade);*/
		return new ModelAndView("class/classUp","classUp",classUp);
	}
	/**
	 * 创建Excel导入模板
	 */
	@RequestMapping(value = "creatClasssDayExcel", method = RequestMethod.GET)
	public void creatStudentExcel(HttpServletRequest request,HttpServletResponse response){
		ExcelUtils excel=new ExcelUtils();
		String fileName = "班级日常表模板.xls";
		String header="班级日常模板";
		List<String> columns=new ArrayList<String>();
		columns.add("年级");
		columns.add("班级");
		columns.add("纪律");
		columns.add("卫生");
		columns.add("团队活动");
		columns.add("日常活动");
		columns.add("日期");
		try {
			excel.creatExcel(response, request, fileName, header, columns);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	/**
	 * 班级日常统计跳转
	 */
	@RequestMapping("classChart")
	public ModelAndView returnClassChart(){
		return new ModelAndView("/class/classChart");
	}
	@RequestMapping("classChats")
	@ResponseBody
	public List classCharts(@RequestParam(defaultValue="",required=false)String data){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		List<Semester> semesters=semesterService.findSemesters();
		Map sqlMap= new HashMap<>();
		sqlMap.put("beginTime", sdf.format(semesters.get(0).getCreatTime()));
		sqlMap.put("endTime",sdf.format(semesters.get(0).getRecessTime()));
		List<classAndClassDay> classCharts=classService.selectClassChart(sqlMap);
		return classCharts;
	}
	/**
	 * 班级日常模板导入
	 * @throws IOException 
	 * 
	 */
	@RequestMapping("upfile")
	@ResponseBody
	public String classDayExcel(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException{
		List<Map<String, String>> list = ExcelUtils.readExcel(file);
		List<ClassDay> classDayList=new ArrayList<ClassDay>();
		for (Map<String,String> map : list) {
			ClassDay classDay=new ClassDay();
			int gradeName=0;
			switch (map.get("年级")) {
			case "七年级":
				gradeName=1;
				break;
			case "八年级":
				gradeName=2;
				break;
			case "九年级":
				gradeName=3;
				break;
			}
			Map sqlMap=new HashMap<>();
			sqlMap.put("gradeName", gradeName);
			sqlMap.put("className", map.get("班级"));
			Classs classs=classService.selectClassByGradeNameAndClassName(sqlMap);
			classDay.setClassId(classs.getClassId());
			classDay.setClassMark(classs.getStuClassMark());
			classDay.setDiscipline(Integer.parseInt(map.get("纪律")));
			classDay.setHealth(Integer.parseInt(map.get("卫生")));
			classDay.setTeamActivity(Integer.parseInt(map.get("团队活动")));
			classDay.setDutyActivity(Integer.parseInt(map.get("日常活动")));
			classDay.setDay(new Date());
			classDayList.add(classDay);
		}
		try {
			classService.classExcel(classDayList);
			return "1";
		} catch (Exception e) {
			return "0";
		}	
	}
	
	/**
	 * 高级div变化+班级教师回显
	 * 
	 */
	@RequestMapping(value="divChange")
	@ResponseBody
	public Map divChange(String data){
		String [] datas=data.split(",");
		String str=datas[0];
		String classId=datas[1];
		List<Subject>subjectList=classService.subjectList(Integer.parseInt(str));
		Map<String,List<Teacher>> teacherMapByGrade=insertClass(subjectList);
		List<Teacher> teacherAndSubjectList=classService.selectTeacherAndSubjectByClassId(Integer.parseInt(classId));
		Classs classs=classService.selectClassByClassId(Integer.parseInt(classId));
		Map classDownTeacherMap=new HashMap<>();
		for (Teacher teacher : teacherAndSubjectList) {
				classDownTeacherMap.put(teacher.getSubject().getSubjectName(), teacher.getTchId());
		}
		classDownTeacherMap.put("headTeacher", classs.getHeadTch());
		Map<String,Map> listMap=new HashMap<String,Map>();
		listMap.put("classDownTeacherMap", classDownTeacherMap);
		listMap.put("teacherMapByGrade", teacherMapByGrade);
		return listMap;
	}
	/**
	 * 查询不同教不同科目的教师
	 * @param gradeName
	 * @return
	 * 
	 */
	public Map<String,List<Teacher>> insertClass(List<Subject> subjectList){
		List<Teacher> tacherList=classService.selectTeacherAndSubject();
		List<Teacher> chineseList=new ArrayList<Teacher>();
		List<Teacher> mathList=new ArrayList<Teacher>();
		List<Teacher> englishList=new ArrayList<Teacher>();
		List<Teacher> politicsList=new ArrayList<Teacher>();
		List<Teacher> historyList=new ArrayList<Teacher>();
		List<Teacher> geographyList=new ArrayList<Teacher>();
		List<Teacher> physicsList=new ArrayList<Teacher>();
		List<Teacher> chemistryList=new ArrayList<Teacher>();
		List<Teacher> organismsList=new ArrayList<Teacher>();
		List<Teacher> musicLlist=new ArrayList<Teacher>();
		List<Teacher> sportsList=new ArrayList<Teacher>();
		List<Teacher> fineArtsList=new ArrayList<Teacher>();
		List<Teacher> microComputerList=new ArrayList<Teacher>();
		List<Teacher> psychologyList=new ArrayList<Teacher>();
		for (Teacher teacher : tacherList) {
			String subjectName=teacher.getSubject().getSubjectName();
			switch (subjectName) {
			case "语文":
				chineseList.add(teacher);
				break;
			case "数学":
				mathList.add(teacher);
				break;
			case "外语":
				englishList.add(teacher);
				break;
			case "政治":
				politicsList.add(teacher);
				break;
			case "历史":
				historyList.add(teacher);
				break;
			case "地理":
				geographyList.add(teacher);
				break;
			case "物理":
				physicsList.add(teacher);
				break;
			case "化学":
				chemistryList.add(teacher);
				break;
			case "生物":
				organismsList.add(teacher);			
				break;
			case "音乐":
				musicLlist.add(teacher);			
				break;
			case "体育":
				sportsList.add(teacher);			
				break;
			case "美术":
				fineArtsList.add(teacher);			
				break;
			case "微机":
				microComputerList.add(teacher);			
				break;
			case "心理":
				psychologyList.add(teacher);			
			break;
			}
		}
		Map<String, List>teacherMap=new HashMap<String, List>();
		teacherMap.put("语文", chineseList);
		teacherMap.put("数学", mathList);
		teacherMap.put("外语", englishList);
		teacherMap.put("政治", politicsList);
		teacherMap.put("历史", historyList);
		teacherMap.put("地理", geographyList);
		teacherMap.put("物理", physicsList);
		teacherMap.put("化学", chemistryList);
		teacherMap.put("生物", organismsList);
		teacherMap.put("音乐", musicLlist);
		teacherMap.put("美术", fineArtsList);
		teacherMap.put("体育", sportsList);
		teacherMap.put("微机", microComputerList);
		teacherMap.put("心理", psychologyList);
		Map<String,List<Teacher>> teacherMapByGrade=new HashMap<String,List<Teacher>> ();
		for (String key:teacherMap.keySet()) {
		for (Subject subject : subjectList) {
					teacherMapByGrade.put(subject.getSubjectName(),teacherMap.get(subject.getSubjectName()));
			}
		}
		return teacherMapByGrade;
	}
	/**
	 * 添加班级 判断班级名称和唯一标识是否有重复
	 */
	@ResponseBody
	@RequestMapping(value = "insertClass")
	public Map<String,String> insertClass(Classs classs,HttpServletResponse response){
		System.out.println("我要添加班级信息了");
		response.setContentType("application/json;charset=utf-8");
		List<Classs> classsList=classService.selectClassByClassMarkAndName(classs);
		Map<String,String> resultMap=new HashMap<String,String>();
		if(classsList.size()==0){
			classs.setReserve("1");
			classService.insertClass(classs);
			resultMap.put("result", "添加成功");
			return resultMap;
		}else{
			resultMap.put("result","班级名称或标识已存在");
			return resultMap ;	
		}
	}
	/**
	 * 添加班级教师
	 */
	@ResponseBody
	@RequestMapping(value="insertClassTeacher")
	public Map<String, String> insertClassTeacer(HttpServletRequest req,HttpServletResponse response){
		response.setContentType("application/json;charset=utf-8");
		String data=req.getParameter("data");
		String [] datas=data.split(",");
		int classId=Integer.parseInt(datas[0]);
		String tchId=datas[1];
		if(tchId.equals("0")){
			tchId="0";
		}
		List<ClassTeacher> classTeachers=new LinkedList<ClassTeacher>();
		for (int i = 1; i < datas.length; i++) {
			if(datas[i].matches("-?\\d+")){
				ClassTeacher classTeacher=new ClassTeacher();
				classTeacher.setClassId(classId);
				classTeacher.setTchId(Integer.parseInt(datas[i]));
				classTeachers.add(classTeacher);
			}
		}
		Map sqlMap=new HashMap();
		sqlMap.put("classId", classId);
		classTeachers.remove(0);
		sqlMap.put("classTeachers", classTeachers);
		sqlMap.put("tchIds", tchId);
		Map<String, String> resultMap=new HashMap<String,String>();
		try {
			classService.updatOrInsertTchByClassId(sqlMap);
			classService.deleteClassTeacherByClassId(classId);
			if(classTeachers.size()!=0){
				classService.insertClassTeacher(sqlMap);
			}
		} catch (Exception e) {
			resultMap.put("result", "更改失败");
			return resultMap;
		}
		resultMap.put("result", "更改成功");
		return resultMap;
	}
	
	//查询班级下学科和对应教师 和班主任
	@RequestMapping("selectTeacherByClassId")
	@ResponseBody
	public Map selectTeacherByClassId(String classId){
		List<Teacher> teacherAndSubjectList=classService.selectTeacherAndSubjectByClassId(Integer.parseInt(classId));
		Classs classs=classService.selectClassByClassId(Integer.parseInt(classId));
		Map classDownTeacherMap=new HashMap<>();
		for (Teacher teacher : teacherAndSubjectList) {
				classDownTeacherMap.put(teacher.getSubject().getSubjectName(), teacher.getTchName());
		}
		classDownTeacherMap.put("班主任", classs.getTeacher().getTchName());
		return classDownTeacherMap;
	}
	
	/**
	 * 班级升级提交
	 */
	@RequestMapping("classUpUp")
	@ResponseBody
	public Map classUpUp(String data,HttpServletResponse response){
		response.setContentType("appliaction/json;charset=utf-8");
		Map resultMap=new HashMap<>();
		String[] datas=data.split(",");
		String gradeName=datas[0];
		String className=datas[1];
		String classMark=datas[2];
		Map sqlMap= new HashMap();
		sqlMap.put("classMark",classMark);
		if(datas[3].equals("毕业")){
			sqlMap.put("classId",'0');
			sqlMap.put("reserve",datas[3]);
			classService.updateUpClass(sqlMap);
			classService.updataUpStudentReasrveAndClassId(sqlMap);
			resultMap.put("result", "毕业了");
			return resultMap;
		}else{
			sqlMap.put("reserve",0);
			classService.updateUpClass(sqlMap);
			Classs classs=new Classs();
			classs.setClassName(className);
			classs.setGradeName(Integer.parseInt(gradeName));
			classs.setStuClassMark(classMark);
			classs.setReserve("1");
			classs.setClassCategory(datas[3]);
			classService.insert(classs);
			sqlMap.put("classId",classs.getClassId());
			classService.updataUpStudentClassId(sqlMap);
			resultMap.put("result", "升级成功");
		}
		return resultMap;
	}
	
	/**
	 * 全部班级升级
	 */
	@RequestMapping("upupclass")
	@ResponseBody
	public Map upupclass(){
		List<Classs> classList=classService.selectClass();
		if(classList.size()<1){
			resultMap.put("result", "没有班级可升级了");
			return resultMap;
		}else {
		List<Classs> newclassList=new ArrayList<>();
		String [] stuClassMark=new String [classList.size()];
		Map resultMap=new HashMap<>();
		Map sqlMap=new HashMap<>();
		for (Classs classs : classList) {
			int gradeName=classs.getGradeName();
			int classId=classs.getClassId();
			sqlMap.put("classMark", classs.getStuClassMark());
			if(gradeName==1){
				Classs newClass=new Classs();
				newClass=classs;
				newClass.setGradeName(classs.getGradeName()+1);
				sqlMap.put("reserve", "0");
				//修改升级前的班级状态
				classService.updateUpClass(sqlMap);
			//添加新班级
				classService.insert(newClass);
				sqlMap.put("newClassId", newClass.getClassId());
				sqlMap.put("classId", classId);
				//修改升级后班级下的学生ClassId
			classService.updataUpStudentClassId(sqlMap);
			//修改升级后的教师ClassId
				classService.updataUpClassTeacher(sqlMap);
			}else
			 if(gradeName==2){
			Classs newClass=new Classs();
			newClass=classs;
			newClass.setGradeName(classs.getGradeName()+1);
			sqlMap.put("reserve", "0");
			//修改升级前的班级状态
			classService.updateUpClass(sqlMap);
			//添加新班级
			classService.insert(newClass);
			sqlMap.put("newClassId", newClass.getClassId());
			sqlMap.put("classId", classId);
			//修改升级后班级下的学生ClassId
			classService.updataUpStudentClassId(sqlMap);
			//修改升级后的教师ClassId
			classService.updataUpClassTeacher(sqlMap);
			}else if(gradeName==3){
				Classs newClass=new Classs();
				newClass=classs;
				sqlMap.put("reserve", "毕业");
				sqlMap.put("classId", "0");
				//修改升级前的班级状态
				classService.updateUpClass(sqlMap);
				//修改升级后班级下的学生ClassId和状态
				classService.updataUpStudentReasrveAndClassId(sqlMap);
			}
			
			}	
		//清空班级日常
		classService.deleteClassDay();
		resultMap.put("result", "升级成功");
		return resultMap;
		}
	}
	/**
	 * 我的班级
	 */
	@RequestMapping("myClass")
	public ModelAndView myClass(){
		TokenManager tokenManager=new TokenManager();	
		int stuNo=Integer.parseInt(tokenManager.getUserName());
		Classs classs=classService.selectMyClass(stuNo);
		return new ModelAndView("class/show","classs",classs);
	}
}
