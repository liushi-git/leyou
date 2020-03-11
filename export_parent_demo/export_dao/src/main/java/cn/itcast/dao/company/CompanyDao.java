package cn.itcast.dao.company;

import cn.itcast.domain.company.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyDao {

	List<Company> findAll();

	//保存企业
	void save(Company company);

	//根据id查询
	Company findById(String id);

	//更新企业
	void update(Company company);

	//根据id删除
	void delete(String id);

	//查询总记录数
	long findCount();

	//分页查询
	List<Company> findPage(@Param("beg") int beg, @Param("end") int end);
}
