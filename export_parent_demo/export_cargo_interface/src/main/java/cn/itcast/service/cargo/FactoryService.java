package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.FactoryExample;

import java.util.List;

public interface FactoryService {

    //查询全部
    List findAll(FactoryExample example);
}
