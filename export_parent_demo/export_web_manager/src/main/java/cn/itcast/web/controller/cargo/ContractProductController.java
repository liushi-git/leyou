package cn.itcast.web.controller.cargo;

import cn.itcast.common.utils.UploadUtils;
import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;
    @Reference
    private FactoryService factoryService;
    @RequestMapping("list")
    public String list(@RequestParam(defaultValue = "1") int page ,
                       @RequestParam(defaultValue = "10") int size,String contractId){
        //1.查询所有的生产厂家
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        criteria.andCtypeEqualTo("货物");
        List list = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",list);
        //2.查询当前购销合同下的所有货物:根据购销合同id查询货物
        ContractProductExample example = new ContractProductExample();
        ContractProductExample.Criteria exampleCriteria = example.createCriteria();
        exampleCriteria.andContractIdEqualTo(contractId);
        PageInfo pageInfo = contractProductService.findAll(page, size, example);
        request.setAttribute("page",pageInfo);
        //3.为了保存时能够方便的获取合同id
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-list";
    }
    /**
     * 进入到更新的页面
     */
    @RequestMapping("toUpdate")
    public String toUpdate(String id){
        //1.查询所有的厂家
        FactoryExample example = new FactoryExample();
        FactoryExample.Criteria criteria = example.createCriteria();
        criteria.andCtypeEqualTo("货物");
        List list = factoryService.findAll(example);
        request.setAttribute("factoryList",list);
        //2.根据id查询出货物信息
        ContractProduct contractProduct = contractProductService.findById(id);
        request.setAttribute("contractProduct",contractProduct);
        return "cargo/product/product-update";
    }
    /**
     * 保存或更新
     */
    @RequestMapping("edit")
    public String edit(ContractProduct contractProduct,MultipartFile productPhoto) throws IOException {
        contractProduct.setCompanyId(getLoginCompanyId());
        contractProduct.setCompanyName(getLoginCompanyName());
        if(StringUtils.isEmpty(contractProduct.getId())){
            //判断是否存在文件
            if(!productPhoto.isEmpty()) {
                String imageUrl = UploadUtils.upload(productPhoto.getBytes());
                contractProduct.setProductImage(imageUrl);
            }
            contractProductService.save(contractProduct);
        }else {
            contractProductService.update(contractProduct);
        }
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractProduct.getContractId();
    }
    /**
     * 删除货物
     */
    @RequestMapping("delete")
    public String delete(String id,String contractId){
        contractProductService.delete(id);
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }
    /**
     * 进入到上传货物的页面
     */
    @RequestMapping("/toImport")
    public String toImport(String contractId){
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-import";
    }
    /**
     * 实现货物的批量上传
     */
    @RequestMapping("import")
    public String importExcel(String contractId,MultipartFile file) throws Exception {
        //I.解析excel获取货物列表
        List<ContractProduct> list = new ArrayList<>();
        //1.加载上传的文件构建工作簿
        Workbook wb = new XSSFWorkbook(file.getInputStream()) ;//2007及以上
        //2.获取工作簿中的第一页
        Sheet sheet = wb.getSheetAt(0);
        //3.循环所有的数据行
        for(int i=1 ; i<= sheet.getLastRowNum() ; i++) {
            Row row = sheet.getRow(i);
            //4.循环每行中的所有的数据单元格
            Object objs[] = new Object[row.getLastCellNum()];
            for(int j=1 ; j< row.getLastCellNum() ; j++) {
                Cell cell = row.getCell(j);
                objs[j] = getCellValue(cell);
            }
            ContractProduct cp = new ContractProduct(objs,getLoginCompanyId(),getLoginCompanyName());
            cp.setContractId(contractId); //设置购销合同id
            list.add(cp);
        }
        //II.调用service批量保存货物列表
        contractProductService.saveAll(list);
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }
    /**
     * 对单元格的数据进行处理
     */
    private  Object getCellValue(Cell cell) {
        Object obj = null;
        //1.获取当前单元格数据类型
        CellType cellType = cell.getCellType();
        //2.根据不同的类型,使用不同的方法获取数据
        switch (cellType) {
            case STRING:
                obj = cell.getStringCellValue();
                break;
            case BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)) {
                    //判断数据是否是日期
                    obj = cell.getDateCellValue();
                }else{
                    //数字
                    obj = cell.getNumericCellValue();
                }
                break;
            case FORMULA: //函数
                break;
        }
        return obj;
    }
}
