package cn.itcast.web.controller.company;

import cn.itcast.common.entity.PageResult;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.print.attribute.standard.PageRanges;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {

	@Autowired
	private CompanyService companyService;


	/**
	 * 查询全部
	 *      传统分页: 改造
	 *          参数 :
	 *              当前页
	 *              每页查询条数
	 *          数据响应:
	 *              PageResult
	 *
	 */
	@RequiresPermissions("企业管理")
	@RequestMapping("/list")
	public String list(@RequestParam(defaultValue = "1") int page ,
	                   @RequestParam(defaultValue = "10") int size) {
		//List<Company> list = companyService.findAll();
		//PageResult pr =  companyService.findPage(page,size);
		PageInfo pi = companyService.findPageHelper(page,size);
		request.setAttribute("page",pi);
		return "company/company-list"; //
	}


	/**
	 * 进入到保存企业的页面
	 *      * 通过springmvc跳转到受保护的页面上
	 */
	@RequestMapping("/toAdd")
	public String toAdd() {
		//页面跳转
		return "company/company-add";
	}

	/**
	 * 进入修改企业的页面
	 *      参数 : 企业id
	 *      业务 :
	 *             1.调用service查询企业
	 *             2.保存到request域中
	 *             3.跳转到更新页面
	 *
	 */
	@RequestMapping("/toUpdate")
	public String toUpdate(String id) {
		//1.调用service查询企业
		Company company = companyService.findById(id);
		//2.保存到request域中
		request.setAttribute("company",company);
		//3.跳转到更新页面
		return "company/company-update";
	}

	/**
	 *  保存企业
	 *      1.参数  company对象
	 *      2.业务  调用service保存
	 *      3.跳转  保存成功之后--重定向到列表
	 *
	 *  * 保存或更新
	 *      参数 :company
	 *          保存 : 没有id  -- 执行save
	 *          更新 : 有id    -- 执行update
	 *
	 *
	 */
	@RequestMapping("/edit")
	public String edit(Company company) {
		//if(company.getId() != null && "".equals(company.getId()))  // 有id
		if(StringUtils.isEmpty(company.getId())) { //返回true : 没有id
			//没有id,保存
			companyService.save(company);
		}else{
			companyService.update(company);
		}
		return "redirect:/company/list.do";
	}

	/**
	 * 删除企业
	 *  参数: id
	 *  业务 : 调用service
	 *  跳转 : 重定向到列表
	 */
	@RequestMapping("/delete")
	public String delete(String id) {
		companyService.delete(id);
		return "redirect:/company/list.do";
	}



}
