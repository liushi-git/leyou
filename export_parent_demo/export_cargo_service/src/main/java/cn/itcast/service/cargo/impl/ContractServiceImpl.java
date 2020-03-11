package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.service.cargo.ContractService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ContractServiceImpl implements ContractService{
    @Autowired
    private ContractDao contractDao;

    @Override
    public void save(Contract contract) {
        //设置id
        contract.setId(UUID.randomUUID().toString());
        //其他的属性
        contract.setState(0); // 0 :草稿 (刚刚创建新的合同)
        contract.setProNum(0);//货物批次
        contract.setExtNum(0); //附件数量
        contract.setCreateTime(new Date());
        contract.setTotalAmount(0.0);
        contractDao.insertSelective(contract);
    }

    @Override
    public void update(Contract contract) {
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        contractDao.deleteByPrimaryKey(id);
    }

    @Override
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(int page, int size, ContractExample example) {
        PageHelper.startPage(page,size);
        List<Contract> contracts = contractDao.selectByExample(example);
        return new PageInfo(contracts);
    }
}
