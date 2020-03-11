package cn.itcast.service.company;

import cn.itcast.common.entity.PageResult;
import cn.itcast.domain.company.Company;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CompanyService {

	List<Company> findAll();

	//保存企业
	void save(Company company);

	//根据id查询企业
	Company findById(String id);

	//根据id更新企业
	void update(Company company);

	//根据id删除
	void delete(String id);

	PageResult findPage(int page, int size);

	//使用pageHepler分页
	PageInfo findPageHelper(int page, int size);
}
