package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.vo.ContractProductVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 货物接口
 */
public interface ContractProductService {
    void save(ContractProduct ContractProduct);

    void update(ContractProduct ContractProduct);

    void delete(String id);

    ContractProduct findById(String id);

    /**
     * 查询全部分页
     *      由于使用了example查询
     */
    PageInfo findAll(int page, int size, ContractProductExample example);

    void saveAll(List<ContractProduct> list);

    List<ContractProductVo> findVoByShipTime(String inputDate);
}
