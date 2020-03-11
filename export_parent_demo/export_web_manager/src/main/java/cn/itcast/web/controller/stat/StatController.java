package cn.itcast.web.controller.stat;

import cn.itcast.service.stat.StatService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/stat")
@Controller
public class StatController extends BaseController{
    @Reference
    private StatService statService;

    @RequestMapping("/toCharts")
    public String toCharts(String chartsType){
        return "stat/stat-"+chartsType;
    }
    /**
     * 获取生产厂家的销售数据
     */
    @RequestMapping("/getFactoryData")
    public @ResponseBody List getFactoryData(){
        return statService.getFactoryData(getLoginCompanyId());
    }

    @RequestMapping("getSellData")
    public @ResponseBody List getSellData(){
        return statService.getSellData(getLoginCompanyId());
    }
    @RequestMapping("getOnlineData")
    public @ResponseBody List getOnlineData(){
        return statService.getOnLineData(getLoginCompanyId());
    }
}
