package cn.itcast.service.system;

import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {

	/**
	 * 查询全部
	 *  返回值 : PageInfo
	 *  参数 :
	 *      page,size,companyId
	 */
	PageInfo findAll(int page, int size, String companyId);

	//查询全部
	List<User> findAll(String companyId);

	//保存部门
	void save(User user);

	//根据id查询部门
	User findById(String id);

	//更新部门
	void update(User user);

	//根据id删除
	void delete(String id);

    void changeRole(String userid, String[] roleids);

    User findByEmail(String email);
}
