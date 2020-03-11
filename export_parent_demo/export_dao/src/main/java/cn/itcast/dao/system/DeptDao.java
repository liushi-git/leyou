package cn.itcast.dao.system;

import cn.itcast.domain.system.Dept;

import java.util.List;

public interface DeptDao {

	/**
	 * 查询全部
	 *  条件 : 企业id
	 */
	List<Dept> findAll(String companyId);

	/**
	 * 根据id查询
	 */
	Dept findById(String id);


	/**
	 * 保存部门
	 */
	void save(Dept dept);



	/**
	 * 更新部门
	 */
	void update(Dept dept);



	/**
	 * 删除部门
	 */
	void delete(String id);
}
