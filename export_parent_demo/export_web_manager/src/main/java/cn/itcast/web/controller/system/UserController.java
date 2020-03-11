package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.UserService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@Autowired
	private DeptService deptService;



	/**
	 * 分页查询部门列表
	 *      参数 :  page,size
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size) {
		//1.调用service查询数据
		PageInfo info = userService.findAll(page, size, getLoginCompanyId());
		//2.将对象数据存入request域
		request.setAttribute("page",info);
		//3.页面跳转
		return "system/user/user-list";
	}

	/**
	 * 进入到保存部门的页面
	 */
	@RequestMapping("/toAdd")
	public String toAdd() {
		//1.为了构造下拉框数据,需要查询所有的部门
		List<User> list = userService.findAll(getLoginCompanyId());
		//2.部门列表传入到request域中
		request.setAttribute("userList",list);

		//1.为了构造下拉框数据,需要查询所有的部门
		List<Dept> deptList = deptService.findAll(getLoginCompanyId());
		//2.部门列表传入到request域中
		request.setAttribute("deptList",deptList);
		return "system/user/user-add";
	}


	/**
	 * 进入到更新部门页面
	 */
	@RequestMapping("/toUpdate")
	public String toUpdate(String id) {
		//1.根据id查询部门
		User user = userService.findById(id);
		request.setAttribute("user",user);
		//2.查询所有的部门
		List<User> list = userService.findAll(getLoginCompanyId());
		request.setAttribute("userList",list);

		//1.为了构造下拉框数据,需要查询所有的部门
		List<Dept> deptList = deptService.findAll(getLoginCompanyId());
		//2.部门列表传入到request域中
		request.setAttribute("deptList",deptList);
		return "system/user/user-update";
	}

	/**
	 * 保存或者更新部门
	 *   参数 : user
	 *   业务 :
	 *      1.如果存在id ==> 更新部门
	 *      2.如果不存在id ==> 保存部门
	 */
	@RequestMapping("/edit")
	public String edit(User user) {
		//业务逻辑
		//设置保存部门所属的企业信息
		user.setCompanyId(getLoginCompanyId());
		user.setCompanyName(getLoginCompanyName());
		if(StringUtils.isEmpty(user.getId())) {
			//保存部门
			userService.save(user);
		}else{
			//更新部门
			userService.update(user);
		}
		//页面跳转
		return "redirect:/system/user/list.do";
	}

	/**
	 * 根据id删除部门
	 *      参数 : id
	 */
	@RequestMapping("/delete")
	public String delete(String id) {
		userService.delete(id);
		return "redirect:/system/user/list.do";
	}
	/**
	 * 进入到分配角色的页面
	 */
	@RequestMapping("roleList")
	public String roleList(String id){
		//根据用户id查询用户的数据
		User user = userService.findById(id);
		request.setAttribute("user",user);
		//查询所有的角色数据
		List<Role> roleList = roleService.findAll(getLoginCompanyId());
		request.setAttribute("roleList",roleList);
		//3.根据用户的id查询用户所具有的所有角色
		List<Role> userRoles = roleService.findByUserId(id);
		String roleIds = "";
		for (Role userRole : userRoles) {
			roleIds+= userRole.getId()+",";
		}
		request.setAttribute("roleIds",roleIds);
		return "/system/user/user-role";
	}

	/**
	 * 对用户分配角色
	 * @param userid
	 * @param roleIds
	 * @return
	 */
	@RequestMapping("changeRole")
	public String changeRole(String userid,String[] roleIds){
		userService.changeRole(userid,roleIds);
		return "redirect:/system/user/list.do";
	}
}
