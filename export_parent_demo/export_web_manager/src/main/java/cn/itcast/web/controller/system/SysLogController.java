package cn.itcast.web.controller.system;

import cn.itcast.service.system.SysLogService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController {
    @Autowired
    private SysLogService sysLogService;

    @RequestMapping("list")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size){
        PageInfo list = sysLogService.findAll(page, size, getLoginCompanyId());
        request.setAttribute("page",list);
        return "system/log/log-list";
    }
}
