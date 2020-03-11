package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {

	@Autowired
	private DeptService deptService;


	/**
	 * 分页查询部门列表
	 *      参数 :  page,size
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size) {
		//1.调用service查询数据
		PageInfo info = deptService.findAll(page, size, getLoginCompanyId());
		//2.将对象数据存入request域
		request.setAttribute("page",info);
		//3.页面跳转
		return "system/dept/dept-list";
	}

	/**
	 * 进入到保存部门的页面
	 */
	@RequestMapping("/toAdd")
	public String toAdd() {
		//1.为了构造下拉框数据,需要查询所有的部门
		List<Dept> list = deptService.findAll(getLoginCompanyId());
		//2.部门列表传入到request域中
		request.setAttribute("deptList",list);
		return "system/dept/dept-add";
	}


	/**
	 * 进入到更新部门页面
	 */
	@RequestMapping("/toUpdate")
	public String toUpdate(String id) {
		//1.根据id查询部门
		Dept dept = deptService.findById(id);
		request.setAttribute("dept",dept);
		//2.查询所有的部门
		String companyId = "1";
		List<Dept> list = deptService.findAll(companyId);
		request.setAttribute("deptList",list);
		return "system/dept/dept-update";
	}

	/**
	 * 保存或者更新部门
	 *   参数 : dept
	 *   业务 :
	 *      1.如果存在id ==> 更新部门
	 *      2.如果不存在id ==> 保存部门
	 */
	@RequestMapping("/edit")
	public String edit(Dept dept) {
		//业务逻辑
		//设置保存部门所属的企业信息
		dept.setCompanyId(getLoginCompanyId());
		dept.setCompanyName(getLoginCompanyName());
		if(StringUtils.isEmpty(dept.getId())) {
			//保存部门
			deptService.save(dept);
		}else{
			//更新部门
			deptService.update(dept);
		}
		//页面跳转
		return "redirect:/system/dept/list.do";
	}

	/**
	 * 根据id删除部门
	 *      参数 : id
	 */
	@RequestMapping("/delete")
	public String delete(String id) {
		deptService.delete(id);
		return "redirect:/system/dept/list.do";
	}
}
