package cn.itcast.service.system.impl;

import cn.itcast.common.utils.Encrypt;
import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public PageInfo findAll(int page, int size, String companyId) {
		//1.静态方法  PageHelper.startPage(page,size);
		PageHelper.startPage(page,size);
		//2.查询全部
		List<User> list = userDao.findAll(companyId);
		//3.构造返回值
		return new PageInfo(list);
	}

	public List<User> findAll(String companyId) {
		return userDao.findAll(companyId);
	}

	//保存用户
	public void save(User user) {
		//1.设置id
		user.setId(UUID.randomUUID().toString());
		//2.对添加的用户密码进行加密
		String password = Encrypt.md5(user.getPassword(),user.getEmail());
		user.setPassword(password);
		//2.调用dao保存用户
		userDao.save(user);
	}

	//根据id查询用户
	public User findById(String id) {
		return userDao.findById(id);
	}

	//更新用户
	public void update(User user) {
		//2.对修改的用户密码进行加密
		String password = Encrypt.md5(user.getPassword(),user.getEmail());
		user.setPassword(password);
		userDao.update(user);
	}

	//根据id删除
	public void delete(String id) {
		userDao.delete(id);
	}

	@Override
	public void changeRole(String userid, String[] roleids) {
		//1.根据用户id删除所有的中间表数据
		userDao.deleteUserRole(userid);
		//2.循环所有的角色向中间表中保存数据
		for (String roleid : roleids) {
			userDao.saveUserRole(userid,roleid);
		}
	}

	@Override
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}
}



