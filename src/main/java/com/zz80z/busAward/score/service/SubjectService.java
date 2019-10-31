package com.zz80z.busAward.score.service;

import java.util.List;
import java.util.Map;

import com.zz80z.busAward.common.model.Subject;

public interface SubjectService {

	int deleteByPrimaryKey(Integer subjectId);

    int insert(Subject record);

    int insertSelective(Subject record);

    Subject selectByPrimaryKey(Integer subjectId);

    int updateByPrimaryKeySelective(Subject record);

    int updateByPrimaryKey(Subject record);
    
    Subject selectBySubjectName(String subjectName);
    
    List<Subject> findSubjectList(Integer gradeId);
    
    List<Subject> findSubjects();

	boolean checkSubject(String subjectName);

	Map<String, Object> addSubject2Grade(Integer gradeId, String ids);

	List<String> findSubjectNames();


}
