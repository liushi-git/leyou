package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.vo.ExportProductVo;
import cn.itcast.vo.ExportResult;
import cn.itcast.vo.ExportVo;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("cargo/export")
public class ExportController extends BaseController{

    @Reference
    private ContractService contractService;
    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;

    @RequestMapping("contractList")
    public String contractList(@RequestParam(defaultValue = "1") int page ,
                               @RequestParam(defaultValue = "10") int size){
        ContractExample example = new ContractExample();
        ContractExample.Criteria criteria = example.createCriteria();
        //查询条件 企业id ，状态是已上报（1）
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        criteria.andStateEqualTo(1);
        PageInfo list = contractService.findAll(page, size, example);
        request.setAttribute("page",list);
        return "cargo/export/export-contractList";
    }
    /**
     * 进入到出口报运的页面
     */
    @RequestMapping("list")
    public String list(@RequestParam(defaultValue = "1") int page ,
                               @RequestParam(defaultValue = "10") int size){
        ExportExample example = new ExportExample();
        ExportExample.Criteria criteria = example.createCriteria();
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        PageInfo list = exportService.findAll(example, page, size);
        request.setAttribute("page",list);
        return "cargo/export/export-list";
    }

    /**
     * 进入到新增报运单的页面
     */
    @RequestMapping("toExport")
    public String toExport(String id){
        request.setAttribute("id",id);
        return "cargo/export/export-toExport";
    }
    /**
     * 进入到修改报运单的页面
     */
    @RequestMapping("toUpdate")
    public String toUpdate(String id){
        //1.查询报运单
        Export export = exportService.findById(id);
        request.setAttribute("export",export);
        //2.查询此报运单对应的商品
        ExportProductExample example = new ExportProductExample();
        ExportProductExample.Criteria criteria = example.createCriteria();
        criteria.andExportIdEqualTo(id);
        List list = exportProductService.findAll(example);
        request.setAttribute("eps",list);
        return "cargo/export/export-update";
    }
    /**
     * 保存或更新报运单
     */
    @RequestMapping("edit")
    public String edit(Export export){
        export.setCompanyId(getLoginCompanyId());
        export.setCompanyName(getLoginCompanyName());
        if(StringUtils.isEmpty(export.getId())){
            exportService.save(export);
        }else {
            exportService.update(export);
        }
        return "redirect:/cargo/export/list.do";
    }
    /**
     * 提交
     */
    @RequestMapping("submit")
    public String submit(String id){
        Export export = new Export();
        export.setId(id);
        export.setState(1);
        exportService.update(export);
        return "redirect:/cargo/export/list.do";
    }
    /**
     * 取消
     */
    @RequestMapping("cancel")
    public String cancel(String id){
        Export export = new Export();
        export.setId(id);
        export.setState(0);
        exportService.update(export);
        return "redirect:/cargo/export/list.do";
    }
    /**
     * 电子报运
     */
    @RequestMapping("exportE")
    public String exportE(String id){
        //1.根据id查询报运单
        Export export = exportService.findById(id);
        //2.创建海关报运的ExportVo对象
        ExportVo exportVo = new ExportVo();
        BeanUtils.copyProperties(export,exportVo);
        exportVo.setExportId(export.getId());
        //3.根据报运单id查询所有商品
        ExportProductExample example = new ExportProductExample();
        ExportProductExample.Criteria criteria = example.createCriteria();
        criteria.andExportIdEqualTo(id);
        List<ExportProduct> list = exportProductService.findAll(example);
        //4.创建海关报运的ExportPorductVo对象
        List<ExportProductVo> epvos = new ArrayList<>();
        for (ExportProduct ep : list) {
            ExportProductVo epvo = new ExportProductVo();
            BeanUtils.copyProperties(ep,epvo);
            epvo.setExportProductId(ep.getId());
            epvo.setEid(ep.getExportId());
            epvos.add(epvo);
        }
        exportVo.setProducts(epvos); //设置报运单和商品的关系，报运单中有商品的集合
        //5.通过webclient工具类完成海关报运功能
        WebClient webClient = WebClient.create("http://127.0.0.1:9090/ws/export/user");
        webClient.post(exportVo);
        //6.通过webclient工具类查询报运结果(ExportResult)
        webClient = WebClient.create("http://127.0.0.1:9090/ws/export/user/"+id);
        ExportResult result = webClient.get(ExportResult.class);
        //7.调用service更新数据库
        exportService.updateE(result);
        return "redirect:/cargo/export/list.do";
    }
}
