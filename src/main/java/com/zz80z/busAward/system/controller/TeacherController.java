package com.zz80z.busAward.system.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zz80z.busAward.common.controller.BaseController;
import com.zz80z.busAward.common.model.Classs;
import com.zz80z.busAward.common.model.Subject;
import com.zz80z.busAward.common.model.Teacher;
import com.zz80z.busAward.common.model.User;
import com.zz80z.busAward.common.utils.ExcelUtils;
import com.zz80z.busAward.common.utils.LoggerUtils;
import com.zz80z.busAward.core.classs.service.ClasssService;
import com.zz80z.busAward.score.controller.ScoreController;
import com.zz80z.busAward.score.service.SubjectService;
import com.zz80z.busAward.student.service.StudentDayService;
import com.zz80z.busAward.student.service.StudentService;
import com.zz80z.busAward.system.service.SemesterService;
import com.zz80z.busAward.system.service.TeacherService;
import com.zz80z.busAward.user.bo.TeacherClassBo;
import com.zz80z.busAward.user.manager.UserManager;
import com.zz80z.busAward.user.service.UserService;

@SessionAttributes(value="resultMap")
@Controller
@Scope(value="prototype")
@RequestMapping("sys")
public class TeacherController extends BaseController{

	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private UserService userService;
	@Autowired
	ClasssService	classService;
	@Autowired
	StudentService stuService;
	/**
	 * 教师信息列表
	 * @return
	 */
	
	@RequestMapping(value = "tchUp")
	public ModelAndView teacherList(@RequestParam(value = "pn", defaultValue = "1") Integer pn,
			ModelMap modelMap) {
		List<Subject> subjects = subjectService.findSubjects();
		modelMap.put("subjects", subjects);
		PageHelper.startPage(pn, 10);
		List<TeacherClassBo> list = teacherService.selectTeacherAndClass();
		PageInfo<TeacherClassBo> page = new PageInfo<TeacherClassBo>(list, 5);
		modelMap.addAttribute("pageInfo", page);
		System.out.println(page);
		return new ModelAndView("sys/tchUp");
	}
	//教师信息跳转
	@RequestMapping(value="teacherUp")
	public ModelAndView teacherList(
		@RequestParam(value="teacherNo",defaultValue="",required=false)String teacherNo,
		@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
		System.out.println(teacherNo);
		PageHelper.startPage(pn, 10);
		PageInfo pageInfo =null;
			if(!teacherNo.equals("")){
				Teacher tch =teacherService.selectByTchNo(teacherNo);	
				  List<Teacher> list = new ArrayList<Teacher>();
				  if(tch != null){
					  list.add(tch);
					  pageInfo = new PageInfo(list,5);
				  }else{
					  System.out.println("没有查到该工号信息");
				  }
		    }else{
		    	List<TeacherClassBo> list1 = teacherService.selectTeacherAndClass();
		        pageInfo = new PageInfo(list1, 5);
		    }
		return new ModelAndView("sys/tchUp","pageInfo",pageInfo);
	}
	/**
	 * 导入教师信息
	 * @return
	 */
	@RequestMapping(value = "leadTch")
	@ResponseBody
	public Map<String , Object> leadTch(@RequestParam("file") MultipartFile file, HttpServletRequest re) {
		List<Teacher> teachers = new ArrayList<>();
		List<Map<String, String>> readExcel = null;
		List<User> users = new ArrayList<>();
		Teacher teacher = null;
		User user = null;
		try {
			readExcel = ExcelUtils.readExcel(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < readExcel.size(); i++) {
			Subject subject = subjectService.selectBySubjectName(readExcel.get(i).get("科目"));
			teacher = new Teacher(readExcel.get(i).get("姓名"), readExcel.get(i).get("工号"), readExcel.get(i).get("联系方式"),subject.getSubjectId());
			teachers.add(teacher);
		}
		for (int i = 0; i < teachers.size(); i++) {
			user = new User();
			user.setUserName(teachers.get(i).getTchNo());
			user.setLastLoginTime(new Date());
			user.setSatus(1);
			user.setType("B");
			user.setUserPwd("123456");
			users.add(UserManager.md5Pswd(user));
		}
		try {
			teacherService.leadTeacher(teachers);
			userService.leadUser(users);
			resultMap.put("status", 200);
			resultMap.put("message", "导入成功 ");
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "导入失败，请刷新后再试！");
			LoggerUtils.fmtError(getClass(), e, "解析教师信息表表出错..[%s]", file.toString());
		}
		
		return resultMap;
	}
	
	/**
	 * 查询单条教师信息
	 * @return
	 */
	@RequestMapping(value = "selectTchById",method=RequestMethod.POST)
	@ResponseBody
	public Map<String , Object> selectTchById(String tchId){
		Teacher teacher = teacherService.selectByPrimaryKey(Integer.parseInt(tchId));
		resultMap.put("status", 200);
		resultMap.put("teacher", teacher);
		return resultMap;
		
	}
	
	/**
	 * 更新教师信息
	 * @return
	 */
	@RequestMapping(value = "updateTchByTchNo")
	@ResponseBody
	public Map<String , Object> updateTch(Teacher teacher,HttpServletRequest request){
			System.out.println("11111111111111"+teacher);
			try {
				request.setCharacterEncoding("UTF-8");
				int count = teacherService.updateTchByTchNo(teacher);
				System.out.println("555555555"+count);
				resultMap.put("status", 200);
				resultMap.put("successCount", count);
				resultMap.put("message", "修改成功！");
			} catch (Exception e) {
				resultMap.put("status", 500);
				resultMap.put("message", "修改失败，请刷新后再试！");
				System.out.println("555555555修改中出现的错误信息"+e);
			}
		
		return resultMap;
	}
	
	/**
	 * 删除教师信息
	 * @return
	 */
	@RequestMapping(value = "deleteTch",method=RequestMethod.GET)
	@ResponseBody
	public Map<String , Object> deleteTch(Integer tcheId){
		System.out.println("*************"+tcheId);
			try {
				int count = teacherService.deleteByPrimaryKey(tcheId);
				resultMap.put("status", 200);
				resultMap.put("successCount", count);
				resultMap.put("message", "删除成功");
			} catch (Exception e) {
				resultMap.put("status", 500);
				resultMap.put("message", "删除失败，请刷新后再试！");
			}
		return resultMap;
	}
	/**
	 * 添加教师信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addTch")
	public Map<String , Object> addTch(Teacher teacher,HttpServletResponse response){
		response.setContentType("application/json;charset=utf-8");
		Teacher selectByTchNo = teacherService.selectByTchNo(teacher.getTchNo());
		System.out.println(selectByTchNo);
		if (null == selectByTchNo) {
			try {
				int count = teacherService.insertSelective(teacher);
				resultMap.put("status", 200);
				resultMap.put("successCount", count);
				resultMap.put("scuess", "添加成功");
			} catch (Exception e) {
				resultMap.put("status", 500);
				resultMap.put("message", "添加失败，请刷新后再试！");
				LoggerUtils.fmtError(getClass(), e, "添加教师报错。source[%s]", teacher.toString());
			}
		}else {
			resultMap.put("status", 500);
			resultMap.put("message", "工号"+teacher.getTchNo()+"已存在,请不要重复增加！");
		}
		return resultMap;
	}
	/**
	 * 创建教师信息导入模板
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "creatTchExcel", method = RequestMethod.GET)
	public void creatScoreExcel(HttpServletResponse response, HttpServletRequest request) {
		String fileName = "教师信息表.xls";
		String header = "教师信息表";
		List<String> columns = new ArrayList<>();
		columns.add(0, "工号");
		columns.add(1, "姓名");
		columns.add(2, "联系方式");
		columns.add(3, "科目");
		try {
			ExcelUtils.creatExcel(response, request, fileName, header, columns);
		} catch (IOException e) {
			LoggerUtils.error(ScoreController.class, "创建教师导入模板失败" + e);
		}
	}
	
}
