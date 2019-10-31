package com.zz80z.busAward.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zz80z.busAward.common.dao.ClassTeacherMapper;
import com.zz80z.busAward.common.dao.TeacherMapper;
import com.zz80z.busAward.common.model.ClassTeacher;
import com.zz80z.busAward.common.model.Teacher;
import com.zz80z.busAward.common.utils.StringUtils;
import com.zz80z.busAward.core.mybatis.BaseMybatisDao;
import com.zz80z.busAward.core.mybatis.page.Pagination;
import com.zz80z.busAward.system.service.TeacherService;
import com.zz80z.busAward.user.bo.TeacherClassBo;

@Service
@SuppressWarnings("unchecked")
public class TeacherServiceImpl extends BaseMybatisDao<TeacherMapper> implements TeacherService {

	@Autowired
	TeacherMapper teacherMapper;
	@Autowired
	ClassTeacherMapper classTeacherMapper;

	@Override
	public Teacher selectByTchNo(String tchNo) {

		return teacherMapper.selectByTchNo(tchNo);
	}

	@Override
	public int deleteByPrimaryKey(Integer tchId) {

		return teacherMapper.deleteByPrimaryKey(tchId);
	}

	@Override
	public int insert(Teacher record) {
		return teacherMapper.insert(record);
	}

	@Override
	public int insertSelective(Teacher record) {
		return teacherMapper.insertSelective(record);
	}

	@Override
	public Teacher selectByPrimaryKey(Integer tchId) {
		return teacherMapper.selectByPrimaryKey(tchId);
	}

	@Override
	public int updateByPrimaryKeySelective(Teacher record) {
		return teacherMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Teacher record) {
		return teacherMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<Integer> findClassTeacherByTchId(Integer tchId) {
		List<ClassTeacher> classTeacher = classTeacherMapper.findClassTeacherByTchId(tchId);
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < classTeacher.size(); i++) {
			list.add(classTeacher.get(i).getClassId());
		}
		return list;
	}

	@Override
	public Pagination<TeacherClassBo> findTchPage(Map<String, Object> resultMap, Integer pageNo, Integer pageSize) {
		return super.findByPageBySqlId("selectTeacherAndClass", resultMap, pageNo, pageSize);
	}

	@Override
	public void leadTeacher(List<Teacher> teachers) {

		teacherMapper.leadTeacher(teachers);
	}

	@Override
	public Map<String, Object> addClass2Teacher(Integer tchId, String ids) {

		return executeClass(tchId, ids);
	}

	private Map<String, Object> executeClass(Integer tchId, String ids) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int count = 0;
		try {
			if (StringUtils.isNotBlank(ids)) {
				String[] idArray = null;

				if (StringUtils.contains(ids, ",")) {
					idArray = ids.split(",");
				} else {
					idArray = new String[] { ids };
				}
				for (String classId : idArray) {
					if (StringUtils.isNotBlank(tchId)) {
						ClassTeacher entity = new ClassTeacher(tchId, new Integer(classId));
						count += classTeacherMapper.insertSelective(entity);
					}
				}
			}
			resultMap.put("status", 200);
			resultMap.put("message", "操作成功");
		} catch (Exception e) {
			resultMap.put("status", 200);
			resultMap.put("message", "操作失败，请重试！");
		}
		resultMap.put("count", count);
		return resultMap;

	}

	@Override
	public List<TeacherClassBo> selectTeacherAndClass() {
		return teacherMapper.selectTeacherAndClass();
	}

	@Override
	public int updateTchByTchNo(Teacher teacher) {
		Teacher selectByTchNo = teacherMapper.selectByTchNo(teacher.getTchNo());
		System.out.println("2222222222222"+selectByTchNo);
		teacher.setTchId(selectByTchNo.getTchId());
		System.out.println("3333333333333"+teacher);
		int count = teacherMapper.updateByPrimaryKeySelective(teacher);
		System.out.println("444444444444444"+count);
		return count;
	}

}
