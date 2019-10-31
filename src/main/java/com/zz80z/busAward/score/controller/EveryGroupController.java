package com.zz80z.busAward.score.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zz80z.busAward.common.controller.BaseController;
import com.zz80z.busAward.common.model.Group;
import com.zz80z.busAward.common.model.GroupScore;
import com.zz80z.busAward.common.model.Semester;
import com.zz80z.busAward.common.utils.StringUtils;
import com.zz80z.busAward.core.classs.service.ClasssService;
import com.zz80z.busAward.core.shiro.token.manager.TokenManager;
import com.zz80z.busAward.score.service.ScoreService;
import com.zz80z.busAward.system.service.SemesterService;
import com.zz80z.busAward.user.service.RoleService;

@Controller
@Scope(value = "prototype")
@RequestMapping("score")
public class EveryGroupController extends BaseController {

	@Autowired
	private ScoreService scoreService;
	@Autowired
	private ClasssService classService;
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private RoleService roleService;

	@RequestMapping("everyGroup")
	public ModelAndView showScore(ModelMap modelMap, Map<String, Object> map) {

		List<String> classMarks = new ArrayList<>();
		Set<String> roles = roleService.findRoleByUserId(TokenManager.getUserId());
		if (roles.contains("政教处") || roles.contains("教务处")) {
			classMarks = classService.findClassMark(null);
		} else {
			classMarks = classService.findClassMark(TokenManager.getUserName());
		}
		modelMap.put("classMarks", classMarks);
		return new ModelAndView("score/everyGroup");
	}
	
	//查小组成绩
	@RequestMapping("groupScore")
	@ResponseBody
	public Map<String, Object> groupScore(@RequestParam("classMark") String classMark,
			@RequestParam("group") String group, @RequestParam("semesterId") String semesterId) {
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(classMark);
		System.out.println(group);
		System.out.println(semesterId);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		Map<String, Object> map = new HashMap<>();
		List<GroupScore> groupScores = new ArrayList<>();
		map.put("classMark", classMark);
		map.put("group", group);
		map.put("semesterId", semesterId);
		map.put("examName", "月考一");
		GroupScore groupScore1 = scoreService.findScoreByGroup(map);
		groupScores.add(groupScore1);
		
		map.put("examName", "期中考试");
		GroupScore groupScore2 = scoreService.findScoreByGroup(map);
		groupScores.add(groupScore2);
		
		map.put("examName", "月考二");
		GroupScore groupScore3 = scoreService.findScoreByGroup(map);
		groupScores.add(groupScore3);
		
		map.put("examName", "期末考试");
		GroupScore groupScore4 = scoreService.findScoreByGroup(map);
		groupScores.add(groupScore4);
		
		System.err.println(groupScores.toString());
		
		resultMap.put("groupScores", groupScores);
		return resultMap;
	}
	
	//查小组
	@RequestMapping("selecGroupByClass")
	@ResponseBody
	public Map<String, Object> selecGroupByClass(@RequestParam("classMark") String classMark) {
		List<Semester> semesters = new ArrayList<>();
		List<Group> groups = classService.selecGroupByClass(classMark);
		resultMap.put("groups", groups);

		List<String> findSemester = StringUtils.findSemester(classMark);

		for (String semesterName : findSemester) {
			Semester semester = semesterService.selectBySemesterName(semesterName);
			if (semester != null) {
				semesters.add(semester);
			}
		}
		resultMap.put("semesters", semesters);

		return resultMap;
	}
}
