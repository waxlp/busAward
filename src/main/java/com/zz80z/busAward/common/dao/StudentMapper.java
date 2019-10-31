package com.zz80z.busAward.common.dao;

import java.util.List;
import java.util.Map;

import com.zz80z.busAward.common.model.BRecord;
import com.zz80z.busAward.common.model.Classs;
import com.zz80z.busAward.common.model.Grade;
import com.zz80z.busAward.common.model.StuDay;
import com.zz80z.busAward.common.model.Student;
import com.zz80z.busAward.common.model.StudentChart;
import com.zz80z.busAward.common.model.Teacher;
import com.zz80z.busAward.common.model.User;
import com.zz80z.busAward.common.model.UserRole;
import com.zz80z.busAward.core.mybatis.page.Pagination;

public interface StudentMapper {
    int deleteByPrimaryKey(Integer stuId);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(Integer stuId);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);

    
	//查询用户下的学生列表
	public List<Student> selectList(int userid);
	
	//根据当前登录用户ID查询其职位
	public String selectPositionByUserId(int userid);
	
	//根据年级和班级查询班级id
	public Classs selectClassByClassNameAndGradeName(Classs classs);
		
   
    Student selectByStuNo(String stuNo);
    
	//查询全部班级
	public List<Classs> selectClasssList();
	
	//查询全部年级对象
	public List<Grade> selectGrade();
	
	//根据年纪名称查询班级
	public List<Classs> selectClasssByclassgrade(int gradename);
	
	//根据班级ID查询班级唯一标识
	public String selectCmark(int cid);
	
	//添加学生信息
	public void insertStu(Student stu);
	
	public Teacher selectTea();

	
	//Excel插入学生信息
	public void StudentExcelInsert(List<Student> stuList);
	
	//根据学生id传删除学生
	public void StudentDeleteByStuId(int stuid);
	
	//根据学生id查询学生
	public Student selectStudentByStuId(int stuid);

	//修改学生信息
	public void updateStu(Student stu);
	
	Pagination<Student> findPage(Map<String, Object> resultMap, Integer pageNo,
			Integer pageSize);
	
	//根据学生或学号查询学生
	public List<Student> selectByStu(Student stu);
	

	//根据添加的学生创建学生用户
	public void creatStuUser(User user);
	
	//判断学号是否存在
	public Student pdStuNo(String stuno);
	
	//添加用户号角色关联信息
	public void insertUserRole(UserRole userRole);
	
	//查询当天的结果
	public List<StudentChart> stuDayChartList(Map sqlMap);

	//查询当周的结果
	public List<StudentChart> stuWeekChartList(Map sqlMap);
	
	//查询当月的结果
	public List<StudentChart> stuMothChartList(Map sqlMap);
	
	//查询学期的结果
	public List<StudentChart> stuSemesterChartList(Map sqlMap);
	
	//查询卫生平均分排名
	public List<StudentChart> stuHealthList(Map sqlMap);
	
	//查询纪律平均分排名
	public List<StudentChart> stuDisciplineList(Map sqlMap);
	
	//查询作业平均分排名
	public List<StudentChart> stuTaskList(Map sqlMap);
	
	//查询当前学生下巴士币
	public List<BRecord> selectRecordByUserId(int userId);
	
	 //条件查询学生
    public List<Student> selectFromStudent(Map sqlMap);
    
  //查询班级下全部小组
  	public String[] selectStuGroupByClassId(String classId);
  	
  //查询班级下全部学生Id
  		public String[] selectStuIdByClassId(String classId);
}