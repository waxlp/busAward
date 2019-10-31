package com.zz80z.busAward.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zz80z.busAward.common.model.Award;
import com.zz80z.busAward.common.model.BRecord;
import com.zz80z.busAward.common.model.Classs;
import com.zz80z.busAward.common.model.Coin;
import com.zz80z.busAward.common.model.LastRank;
import com.zz80z.busAward.common.model.Student;
import com.zz80z.busAward.user.bo.ClassWeekAdvanced;
import com.zz80z.busAward.user.bo.StudentAdvancedGroup;

public interface CoinMapper {
    int deleteByPrimaryKey(Integer coinId);

    int insert(Coin record);

    int insertSelective(Coin record);

    Coin selectByPrimaryKey(Integer coinId);

    int updateByPrimaryKeySelective(Coin record);

    int updateByPrimaryKey(Coin record);
    
	//N周先进小组 
	public List<StudentAdvancedGroup> lastWeekAdvancedGroup(Map sqlMap);
	
	//先进下组下的学生
	public List<Student> advancedGroupStudent(Map mapSql);
	
	//添加coin表记录
	public void insertFromCoin(List<Coin> coinList);
	
	//添加brecord记录
	public void insertFromBRecord(List<BRecord> coinList);
	
	//添加coin时判断 是否已存在
	public List<BRecord> selectRecord(Map<String, String>sqlmap);
	
	//查询进步小组
	public StudentAdvancedGroup selectLastRank(Map<String, String>sqlmap);
	
	//根据小组查询学生
	public List<Student>selectStudentByGroup(Map map);
	
	//查询奖励点数
	public Award selectAward();
	
	//根据条件查询学生小组排名
	public List<LastRank> selectLastStuDayRank(Map map);
	
	//查询周先进班级
	public List<ClassWeekAdvanced> selectAdvancedClass(Map sqlMap);
	//查询周先进班级下的所有学生
	public List<Student> selectStudentByClassMark (String [] classMarks);
	
	//查询班级下学生是否已经分配周先进币
	public List<BRecord> selectBrecordByStuIds(Map sqlMaps);
	
	public Classs selectMyClass(String stuNo);
	
}