package com.zz80z.busAward.student.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zz80z.busAward.common.dao.StudentMapper;
import com.zz80z.busAward.common.model.BRecord;
import com.zz80z.busAward.common.model.Classs;
import com.zz80z.busAward.common.model.Grade;
import com.zz80z.busAward.common.model.StuDay;
import com.zz80z.busAward.common.model.Student;
import com.zz80z.busAward.common.model.Teacher;
import com.zz80z.busAward.common.model.User;
import com.zz80z.busAward.common.model.UserRole;
import com.zz80z.busAward.core.mybatis.BaseMybatisDao;
import com.zz80z.busAward.core.mybatis.page.Pagination;
import com.zz80z.busAward.student.service.StudentService;
import com.zz80z.busAward.user.bo.StudentScoreBo;
@SuppressWarnings("unchecked")
@Service
public class StudentServiceImpl extends BaseMybatisDao<StudentMapper>  implements StudentService{
	@Autowired
	StudentMapper studentMapper;
	@Override
	public List<Student> selectList(int userid) {
		
		return studentMapper.selectList(userid);
	}
	@Override
	public String selectPositionByUserId(int userid) {
		return studentMapper.selectPositionByUserId(userid);
	}

	@Override
	public List<Classs> selectClasssList() {
		return studentMapper.selectClasssList();
	}
	@Override
	public List<Classs> selectClasssByclassgrade(int gradename) {
		return studentMapper.selectClasssByclassgrade(gradename);
	}
	@Override
	public String selectCmark(int cid) {
		return studentMapper.selectCmark(cid);
	}
	@Override
	public void insertStu(Student stu) {
		studentMapper.insertStu(stu);
	}
	@Override
	public Teacher selectTea() {
		return studentMapper.selectTea();
	}
	@Override
	public void StudentExcelInsert(List<Student> stuList) {
		studentMapper.StudentExcelInsert(stuList);
	}
	@Override
	public void StudentDeleteByStuId(int stuid) {
		studentMapper.StudentDeleteByStuId(stuid);
	}
	@Override
	public Student selectStudentByStuId(int stuid) {
		return studentMapper.selectStudentByStuId(stuid);
	}

	@Override
	public Student selectByStuNo(String stuNo) {
		return studentMapper.selectByStuNo(stuNo);
	}
	@Override
	public void updateStu(Student stu) {
		studentMapper.updateStu(stu);
	}
	@Override
	public Pagination<Student> findPage(Map<String, Object> resultMap, Integer pageNo, Integer pageSize) {
		return super.findPage(resultMap, pageNo, pageSize);
	}
	@Override
	public List<Student> selectByStu(Student stu) {
	
		return studentMapper.selectByStu(stu);
	}
	@Override
	public Classs selectClassByClassNameAndGradeName(Classs classs) {
		
		return studentMapper.selectClassByClassNameAndGradeName(classs);
	}
	@Override
	public void creatStuUser(User user) {
		// TODO 自动生成的方法存根
		studentMapper.creatStuUser(user);
	}
	@Override
	public Student pdStuNo(String stuno) {
	
		return studentMapper.pdStuNo(stuno);
	}
	@Override
	public void insertUserRole(UserRole userRole) {
		
		studentMapper.insertUserRole(userRole);
	}
	@Override
	public Pagination<Student> findStudentAndClassPage(Map<String, Object> resultMap, Integer pageNo,
			Integer pageSize) {
		// TODO 自动生成的方法存根
		return super.findPage("findStudentAndClass", "findCount", resultMap, pageNo, pageSize);
	}
	@Override
	public List<BRecord> selectRecordByUserId(int userId) {
		// TODO 自动生成的方法存根
		return studentMapper.selectRecordByUserId(userId);
	}
	@Override
	public List<Student> selectFromStudent(Map sqlMap) {
		return studentMapper.selectFromStudent(sqlMap);
	}
	@Override
	public List<Grade> selectGrade() {
		return studentMapper.selectGrade();
	}

	

}
