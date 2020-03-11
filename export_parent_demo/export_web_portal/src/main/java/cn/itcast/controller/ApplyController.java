package cn.itcast.controller;

import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplyController {
    /**
     * 默认dubbo在请求失败或者超时的时候会触发重试机制，默认会重试两次
     *  将重试次数设置为0  在查询的时候是可以的，增删改不行
     */
    @Reference(retries = 0)
    private CompanyService companyService;
    @RequestMapping("/apply")
    public @ResponseBody String apply(Company company){
        try {
            companyService.save(company);
            return "1";
        } catch (Exception e) {
            return "2";
        }
    }
}
