package cn.itcast.service.cargo.impl;


import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.vo.ContractProductVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;


@Service
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public void save(ContractProduct cp) {
        //1.计算货物的总金额
        double money = 0;
        if(cp.getPrice()!=null&&cp.getCnumber()!=null) {
            money = cp.getPrice()*cp.getCnumber();
        }
        //2.设置货物的总金额和id
        cp.setAmount(money);
        cp.setId(UUID.randomUUID().toString());
        //3.保存货物
        contractProductDao.insertSelective(cp);
        //4.根据合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(cp.getContractId());
        //5.设置购销合同的总金额（已有金额+新的货物金额）
        contract.setTotalAmount(contract.getTotalAmount()+money);
        //6.设置购销合同货物数
        contract.setProNum(contract.getProNum()+1);
        //7.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ContractProduct cp) {
        //1.计算新的总金额
        double newMoney = 0.0d;
        if(cp.getPrice() != null && cp.getCnumber() != null) {
            newMoney = cp.getPrice() * cp.getCnumber();
        }
        //2.设置新的总金额
        cp.setAmount(newMoney);
        //3.查询旧的总金额
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(cp.getId());
        Double oldMoney = contractProduct.getAmount();
        //4.更新货物数据
        contractProductDao.updateByPrimaryKeySelective(cp);
        //5.根据购销合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(cp.getContractId());
        //6.设置购销合同的总金额（已有总金额-旧的+新的）
        contract.setTotalAmount(contract.getTotalAmount()-oldMoney+newMoney);
        //7.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        //1.根据货物id查询货物
        ContractProduct cp = contractProductDao.selectByPrimaryKey(id);
        //2.根据货物id查询货物的所有附件
        ExtCproductExample example = new ExtCproductExample();
        ExtCproductExample.Criteria criteria = example.createCriteria();
        criteria.andContractProductIdEqualTo(id);
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(example);
        //3.统计删除货物的总金额
        Double money = cp.getAmount();
        //4.计算附件的总金额，并删除附件/货物
        for (ExtCproduct extCproduct : extCproducts) {
            money = extCproduct.getAmount();
            extCproductDao.deleteByPrimaryKey(extCproduct.getId());
        }
        contractProductDao.deleteByPrimaryKey(id);
        //5.根据购销合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(cp.getContractId());
        //6.设置购销合同的总金额
        contract.setTotalAmount(contract.getTotalAmount()-money);
        //7.设置购销合同的货物数/附件数
        contract.setProNum(contract.getProNum()-1);
        contract.setExtNum(contract.getExtNum()-extCproducts.size());
        //8.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(int page, int size, ContractProductExample example) {
        PageHelper.startPage(page,size);
        List<ContractProduct> list = contractProductDao.selectByExample(example);
        return new PageInfo(list);
    }

    @Override
    public void saveAll(List<ContractProduct> list) {
        for (ContractProduct contractProduct : list) {
            this.save(contractProduct);
        }
    }

    @Override
    public List<ContractProductVo> findVoByShipTime(String inputDate) {
        return contractProductDao.findVoByShipTime(inputDate);
    }
}
