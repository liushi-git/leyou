package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import com.github.pagehelper.PageInfo;

public interface ContractService {

    void save(Contract contract);

    void update(Contract contract);

    void delete(String id);

    Contract findById(String id);

    /**
     * 查询全部分页
     *      由于使用了example查询
     */
    PageInfo findAll(int page, int size, ContractExample example);
}
