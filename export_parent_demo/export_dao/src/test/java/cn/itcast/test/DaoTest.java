package cn.itcast.test;

import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DaoTest {

	@Test
	public void testFindAll() throws IOException {
		InputStream resource = Resources.getResourceAsStream("sqlMapConfig.xml");
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		SqlSessionFactory factory = builder.build(resource);
		SqlSession session = factory.openSession();
		CompanyDao companyDao = session.getMapper(CompanyDao.class);
		List<Company> list = companyDao.findAll();
		for (Company company : list) {
			System.out.println(company);
		}
	}
}
