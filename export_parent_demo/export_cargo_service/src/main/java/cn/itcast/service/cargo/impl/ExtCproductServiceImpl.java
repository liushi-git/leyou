package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.service.cargo.ExtCproductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class ExtCproductServiceImpl implements ExtCproductService {
    @Autowired
    private ExtCproductDao extCproductDao;
    @Autowired
    private ContractDao contractDao;
    @Override
    public void save(ExtCproduct ExtCproduct) {
        //1.计算附件的总金额
        double money = 0;
        if(ExtCproduct.getPrice()!=null&&ExtCproduct.getCnumber()!=null){
            money = ExtCproduct.getPrice()*ExtCproduct.getCnumber();
        }
        //2.设置附件的总金额和id
        ExtCproduct.setAmount(money);
        ExtCproduct.setId(UUID.randomUUID().toString());
        //3.保存附件
        extCproductDao.insertSelective(ExtCproduct);
        //4.根据合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(ExtCproduct.getContractId());
        //5.设置购销合同的总金额
        contract.setTotalAmount(contract.getTotalAmount()+money);
        //6.设置购销合同的附件数量
        contract.setExtNum(contract.getExtNum()+1);
        //7.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ExtCproduct ExtCproduct) {
        //1.计算更新之后的总金额
        double NewMoney = 0;
        if(ExtCproduct.getPrice()!=null&&ExtCproduct.getCnumber()!=null){
            NewMoney = ExtCproduct.getPrice()*ExtCproduct.getCnumber();
        }
        //2.设置更新之后的总金额
        ExtCproduct.setAmount(NewMoney);
        //3.查询更新之前的总金额
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(ExtCproduct.getId());
        Double OldMoney = extCproduct.getAmount();
        //4.更新附件
        extCproductDao.updateByPrimaryKeySelective(ExtCproduct);
        //5.根据购销合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //6.设置购销合同的总金额
        contract.setTotalAmount(contract.getTotalAmount()-OldMoney+NewMoney);
        //7.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        //1.根据附件id查询附件对象
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        //2.获取要删除附件的总金额
        Double money = extCproduct.getAmount();
        //3.删除附件
        extCproductDao.deleteByPrimaryKey(id);
        //4.查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //5.设置购销合同的总金额
        contract.setTotalAmount(contract.getTotalAmount()-money);
        //6.设置购销合同的附件数
        contract.setExtNum(contract.getExtNum()-1);
        //7.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(ExtCproductExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<ExtCproduct> list = extCproductDao.selectByExample(example);
        return new PageInfo(list);
    }
}
