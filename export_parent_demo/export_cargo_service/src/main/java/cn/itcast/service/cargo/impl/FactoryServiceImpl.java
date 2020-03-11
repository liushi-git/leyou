package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.FactoryDao;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.FactoryService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class FactoryServiceImpl implements FactoryService {
    @Autowired
    private FactoryDao factoryDao;
    @Override
    public List findAll(FactoryExample example) {
        return factoryDao.selectByExample(example);
    }
}
