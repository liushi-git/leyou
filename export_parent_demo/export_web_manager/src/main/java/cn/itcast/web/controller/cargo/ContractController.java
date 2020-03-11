package cn.itcast.web.controller.cargo;

import cn.itcast.common.utils.DownloadUtil;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.vo.ContractProductVo;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    @Reference
    private ContractService contractService;
    @Reference
    private ContractProductService contractProductService;
    /**
     * 查看所有购销合同
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("list")
    public String list(@RequestParam(defaultValue = "1") int page ,
                       @RequestParam(defaultValue = "10") int size){
        ContractExample example = new ContractExample();
        ContractExample.Criteria criteria = example.createCriteria();
        //添加条件
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        //根据用户的等级，添加不同的查询条件
        User user = getLoginUser();
        Integer degree = user.getDegree();
        if(degree==4){ //普通员工，根据创建人id查询
            criteria.andCreateByEqualTo(user.getId());
        }else if (degree==3){ //管理本部门，根据创建人所在部门查询
            criteria.andCreateDeptEqualTo(user.getDeptId());
        }else if (degree==2){ //管理所有下属部门和人员 ：根据部门id进行模糊查询
            criteria.andCreateDeptLike(user.getDeptId()+"%");
        }
        //在example对象中可以条件排序
        example.setOrderByClause("create_time DESC");//排序 , 排序的sql片段
        PageInfo list = contractService.findAll(page, size, example);
        request.setAttribute("page",list);
        return "cargo/contract/contract-list";
    }
    /**
     * 进入到新增购销合同的页面
     */
    @RequestMapping("toAdd")
    public String toAdd(){
        return "cargo/contract/contract-add";
    }
    /**
     * 进入到修改购销合同的页面
     */
    @RequestMapping("toUpdate")
    public String toUpdate(String id){
        Contract contract = contractService.findById(id);
        request.setAttribute("contract",contract);
        return "cargo/contract/contract-update";
    }
    /**
     * 保存或修改
     * @param contract
     * @return
     */
    @RequestMapping("edit")
    public String edit(Contract contract){
        contract.setCompanyId(getLoginCompanyId());
        contract.setCompanyName(getLoginCompanyName());
        if(StringUtils.isEmpty(contract.getId())){
            /**
             * 添加创建人和创建人所在部门
             */
            User user = getLoginUser();
            contract.setCreateBy(user.getId()); //设置创建人id
            contract.setCreateDept(user.getDeptId()); //设置创建人所在部门id
            contractService.save(contract);
        }else {
            contractService.update(contract);
        }
        return "redirect:/cargo/contract/list.do";
    }
    @RequestMapping("delete")
    public String delete(String id){
        contractService.delete(id);
        return "redirect:/cargo/contract/list.do";
    }
    /**
     * 提交合同状态
     *      //1.根据id查询合同
     *      //2.判断当前合同状态是否为0 (草稿)
     *      //2.1 如果状态=0 : 将状态改为1
     *      //2.2 如果状态!=0 : 返回
     */
    @RequestMapping("/submit")
    public String submit(String id) {
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(1);
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }


    //状态取消 : 将状态由1变为0
    @RequestMapping("/cancel")
    public String cancel(String id) {
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(0);
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }
    /**
     * 进入到出货表打印的页面
     */
    @RequestMapping("print")
    public String print(){
        return "cargo/print/contract-print";
    }
    /**
     * 出货表打印
     * 不需要返回值，以outputStream的形式下载文件，不需要跳转页面
     */
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws IOException {
        //I:根据船期查询所有的数据
        List<ContractProductVo> list = contractProductService.findVoByShipTime(inputDate);
        System.out.println(list.size());
        //II:使用POi构造Excel
        //1.创建工作簿
        Workbook wb = new XSSFWorkbook();
        //2.创建sheet页
        Sheet sheet = wb.createSheet();
        sheet.setColumnWidth(1,26 * 256);
        sheet.setColumnWidth(2,12* 256);
        sheet.setColumnWidth(3,30* 256);
        sheet.setColumnWidth(4,12* 256);
        sheet.setColumnWidth(5,15* 256);
        sheet.setColumnWidth(6,10* 256);
        sheet.setColumnWidth(7,10* 256);
        sheet.setColumnWidth(8,8* 256);
        //合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
        //3.创建大标题
        Row row = sheet.createRow(0);
        row.setHeightInPoints(36);//设置单元格高度

        Cell cell = row.createCell(1);       //2012年8月份出货表  2015-01 --> 2015-1 -->2015年1
        cell.setCellValue(inputDate.replaceAll("-0","-").replaceAll("-","年")+"月份出货表"); //2012年8月份出货表  2015-01 --> 2015-1 -->2015年1
        cell.setCellStyle(bigTitle(wb));


        //4.创建小标题
        String titles [] = new String[]{"","客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        row = sheet.createRow(1);
        row.setHeightInPoints(26);
        for(int i=1;i<9;i++) { //计算单元格的索引
            cell = row.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(title(wb));
        }
        //5.循环数据列表,创建数据行
        int index = 2;
        for (ContractProductVo vo : list) {
                //创建每一行
                row = sheet.createRow(index);
                row.setHeightInPoints(24);
                //创建行中的每一个单元格,设置数据
                // 客户
                cell = row.createCell(1);
                cell.setCellValue(vo.getCustomName());
                cell.setCellStyle(text(wb));
                // 订单号
                cell = row.createCell(2);
                cell.setCellValue(vo.getContractNo());
                cell.setCellStyle(text(wb));
                // 货号
                cell = row.createCell(3);
                cell.setCellValue(vo.getProductNo());
                cell.setCellStyle(text(wb));
                // 数量
                cell = row.createCell(4);
                cell.setCellValue(vo.getCnumber());
                cell.setCellStyle(text(wb));
                // 工厂
                cell = row.createCell(5);
                cell.setCellValue(vo.getFactoryName());
                cell.setCellStyle(text(wb));
                // 工厂交期
                cell = row.createCell(6);
                cell.setCellValue(vo.getDeliveryPeriod());
                cell.setCellStyle(text(wb));
                // 船期
                cell = row.createCell(7);
                cell.setCellValue(vo.getShipTime());
                cell.setCellStyle(text(wb));
                // 贸易条款
                cell = row.createCell(8);
                cell.setCellValue(vo.getTradeTerms());
                cell.setCellStyle(text(wb));
                index++;
        }
        //III.下载excel文件
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        wb.write(baos);
        new DownloadUtil().download(baos,response,"出货表.xlsx"); //bytearrayoutputstream , response,文件名
    }
    //大标题的样式
    public CellStyle bigTitle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short)10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);				//横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线

        return style;
    }


}
