package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.*;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.vo.ExportProductResult;
import cn.itcast.vo.ExportResult;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportDao exportDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ExportProductDao exportProductDao;
    @Autowired
    private ExtCproductDao extCproductDao;
    @Autowired
    private ExtEproductDao extEproductDao;
    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Export export) {
        //I.保存报运单
        //1.获取传递的购销合同的id集合字符串
        String contractIds = export.getContractIds();
        //2.根据字符串获取购销合同id数组
        String [] cids = contractIds.split(",");
        //3.循环查询购销合同
        String contractNo = "";
        for (String cid : cids) {
            Contract contract = contractDao.selectByPrimaryKey(cid);
            //4.获取每个购销合同的合同号构造字符串
            contractNo += contract.getContractNo()+" ";
            //5.更新购销合同的状态
            contract.setState(2);
            contractDao.updateByPrimaryKeySelective(contract);
        }
        //设置报运单中的合同号集合
        export.setCustomerContract(contractNo);
        //6.设置报运单id
        export.setId(UUID.randomUUID().toString());
        //II.生成报运单商品
        //1.查询勾选的购销合同下的所有货物
        ContractProductExample cpexample = new ContractProductExample();
        ContractProductExample.Criteria cpcriteria = cpexample.createCriteria();
        cpcriteria.andContractIdIn(Arrays.asList(cids));
        List<ContractProduct> list = contractProductDao.selectByExample(cpexample);
        //2.循环查询的所有货物
        Map<String,String> map = new HashMap();
        for (ContractProduct contractProduct : list ) {
            //3.一个货物构造一个商品对象
            ExportProduct ep = new ExportProduct();
            //4.设置商品对象中的所有属性
            //工具类 将前面的对象同名属性copy到后面的属性中
            BeanUtils.copyProperties(contractProduct,ep);
            ep.setId(UUID.randomUUID().toString());
            ep.setExportId(export.getId());
            //5.保存商品
            exportProductDao.insertSelective(ep);
            map.put(contractProduct.getId(),ep.getId());
        }
        //III.生成报运单附件
        //1.查询勾选购销合同下的的所有附件
        ExtCproductExample extcExample = new ExtCproductExample();
        ExtCproductExample.Criteria criteria = extcExample.createCriteria();
        criteria.andContractIdIn(Arrays.asList(cids));
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extcExample);
        //2.循环所有的购销合同附件
        for (ExtCproduct extCproduct : extCproducts) {
            //3.一个购销合同附件创建一个报运单附件
            ExtEproduct extEproduct = new ExtEproduct();
            //4.设置报运单附件的基本属性和id，报运单id，商品id
            BeanUtils.copyProperties(extCproduct,extEproduct);
            extEproduct.setId(UUID.randomUUID().toString());
            extEproduct.setExportId(export.getId());
            //报运单商品id
            extEproduct.setExportProductId(map.get(extCproduct.getContractProductId()));
            //5.保存附件
            extEproductDao.insertSelective(extEproduct);
        }
        //设置报运单中的商品和附件数量
        export.setProNum(list.size());
        export.setExtNum(extCproducts.size());
        //设置状态
        export.setState(0);
        //保存报运单
        exportDao.insertSelective(export);

    }

    @Override
    public void update(Export export) {
        //1.更新报运单信息
        exportDao.updateByPrimaryKeySelective(export);
        //2.循环报运单中的商品进行更新
        if(export.getExportProducts()!=null){
            for (ExportProduct exportProduct : export.getExportProducts()) {
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public PageInfo findAll(ExportExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<Export> list = exportDao.selectByExample(example);
        return new PageInfo(list);
    }

    //处理海关报运结果
    public void updateE(ExportResult result) {
        //1.更新报运单信息
        Export export = new Export();
        export.setId(result.getExportId());
        export.setState(result.getState());
        export.setRemark(result.getRemark());
        exportDao.updateByPrimaryKeySelective(export);
        //2.循环更新每个商品的税收
        for (ExportProductResult exportProductResult : result.getProducts()) {
            ExportProduct ep = new ExportProduct();
            ep.setTax(exportProductResult.getTax());
            ep.setId(exportProductResult.getExportProductId());
            exportProductDao.updateByPrimaryKeySelective(ep);
        }
    }
}
