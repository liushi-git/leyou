package cn.itcast.service.system.impl;

import cn.itcast.dao.system.DeptDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptDao deptDao;

	@Override
	public PageInfo findAll(int page, int size, String companyId) {
		//1.静态方法  PageHelper.startPage(page,size);
		PageHelper.startPage(page,size);
		//2.查询全部
		List<Dept> list = deptDao.findAll(companyId);
		//3.构造返回值
		return new PageInfo(list);
	}

	public List<Dept> findAll(String companyId) {
		return deptDao.findAll(companyId);
	}

	//保存部门
	public void save(Dept dept) {
		//1.设置id
		dept.setId(UUID.randomUUID().toString());
		//2.调用dao保存部门
		deptDao.save(dept);
	}

	//根据id查询部门
	public Dept findById(String id) {
		return deptDao.findById(id);
	}

	//更新部门
	public void update(Dept dept) {
		deptDao.update(dept);
	}

	//根据id删除
	public void delete(String id) {
		deptDao.delete(id);
	}
}
