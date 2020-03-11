package cn.itcast.dao.system;

import cn.itcast.domain.system.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

	/**
	 * 查询全部
	 *  条件 : 企业id
	 */
	List<User> findAll(String companyId);

	/**
	 * 根据id查询
	 */
	User findById(String id);


	/**
	 * 保存部门
	 */
	void save(User user);



	/**
	 * 更新部门
	 */
	void update(User user);



	/**
	 * 删除部门
	 */
	void delete(String id);

    void deleteUserRole(String userid);

	void saveUserRole(@Param("userId") String userid, @Param("roleIds")String roleid);

    User findByEmail(String email);
}
