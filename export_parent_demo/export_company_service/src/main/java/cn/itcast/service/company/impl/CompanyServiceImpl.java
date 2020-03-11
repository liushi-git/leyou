package cn.itcast.service.company.impl;


import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyDao companyDao;

	@Override
	public List<Company> findAll() {
		return companyDao.findAll();
	}

	//保存企业
	public void save(Company company) {
		//1.设置主键
		company.setId(UUID.randomUUID().toString());
		//2.调用dao
		companyDao.save(company);
	}

	//根据id查询企业
	public Company findById(String id) {
		return companyDao.findById(id);
	}

	//根据id更新企业
	public void update(Company company) {
		companyDao.update(company);
	}

	//根据id删除
	public void delete(String id) {
		companyDao.delete(id);
	}


	//使用pageHepler分页
	public PageInfo findPageHelper(int page, int size) {
		//调用静态方法 .PageHelper.startPage(page,size) ; 设置分页参数
		PageHelper.startPage(page,size);
		//查询全部 : 会自动对面第一个查询语句进行分页
		List<Company> list = companyDao.findAll();
		//构造返回值
		return new PageInfo<>(list);
	}


}
