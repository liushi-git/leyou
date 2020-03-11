package cn.itcast.service.system;

import cn.itcast.domain.system.Dept;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DeptService {

	/**
	 * 查询全部
	 *  返回值 : PageInfo
	 *  参数 :
	 *      page,size,companyId
	 */
	PageInfo findAll(int page,int size,String companyId);

	//查询全部
	List<Dept> findAll(String companyId);

	//保存部门
	void save(Dept dept);

	//根据id查询部门
	Dept findById(String id);

	//更新部门
	void update(Dept dept);

	//根据id删除
	void delete(String id);
}
