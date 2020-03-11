package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ExtCproductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController{
    @Reference
    private ExtCproductService extCproductService;
    @Reference
    private FactoryService factoryService;

    @RequestMapping("list")
    public String list(@RequestParam(defaultValue = "1") int page ,
                       @RequestParam(defaultValue = "10") int size,String contractId,String contractProductId){
        //1.查询所有的生产厂家
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        criteria.andCtypeEqualTo("附件");
        List factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",factoryList);
        //2.查询当前货物下的所有附件
        ExtCproductExample example = new ExtCproductExample();
        ExtCproductExample.Criteria criteria1 = example.createCriteria();
        criteria1.andContractProductIdEqualTo(contractProductId);
        PageInfo list = extCproductService.findAll(example, page, size);
        request.setAttribute("page",list);
        request.setAttribute("contractProductId",contractProductId);
        request.setAttribute("contractId",contractId);
        return "cargo/extc/extc-list";
    }
    /**
     * 进入到修改的界面
     */
    @RequestMapping("toUpdate")
    public String toUpdate(String id,String contractId,String contractProductId){
        //1.查询所有的生产厂家
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        criteria.andCtypeEqualTo("附件");
        List factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",factoryList);
        //2.根据id查询附件
        ExtCproduct extCproduct = extCproductService.findById(id);
        request.setAttribute("extCproduct",extCproduct);
        request.setAttribute("contractProductId",contractProductId);
        request.setAttribute("contractId",contractId);
        return "cargo/extc/extc-update";
    }
    /**
     * 保存或更新
     */
    @RequestMapping("edit")
    public String edit(ExtCproduct extCproduct){
        extCproduct.setCompanyId(getLoginCompanyId());
        extCproduct.setCompanyName(getLoginCompanyName());
        if(StringUtils.isEmpty(extCproduct.getId())){
            extCproductService.save(extCproduct);
        }else {
            extCproductService.update(extCproduct);
        }
        return "redirect:/cargo/extCproduct/list.do?contractId="+extCproduct.getContractId()+"&contractProductId="+extCproduct.getContractProductId();
    }
    /**
     * 删除附件
     */
    @RequestMapping("delete")
    public String delete(String id,String contractId,String contractProductId){
        extCproductService.delete(id);
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }
}
