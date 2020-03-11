package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("system/role")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private ModuleService moduleService;
    @RequestMapping("list")
    public String findAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size){
        PageInfo all = roleService.findAll(page, size, getLoginCompanyId());
        request.setAttribute("page",all);
        return "system/role/role-list";
    }
    /**
     * 进入到添加页面
     */
    @RequestMapping("toAdd")
    public String toAdd(){
        return "system/role/role-add";
    }
    /**
     * 进入到更新的页面
     */
    @RequestMapping("toUpdate")
    public String toUpdate(String id){
        Role role = roleService.findById(id);
        request.setAttribute("role",role);
        return "system/role/role-update";
    }
    /**
     * 保存或更新角色
     */
    @RequestMapping("edit")
    public String edit(Role role){
        role.setCompanyId(getLoginCompanyId());
        role.setCompanyName(getLoginCompanyName());
        if(StringUtils.isEmpty(role.getId())){
            roleService.save(role);
        }else{
            roleService.update(role);
        }
        return "redirect:/system/role/list.do";
    }
    /**
     * 删除角色
     */
    @RequestMapping("delete")
    public String delete(String id){
        roleService.delete(id);
        return "redirect:/system/role/list.do";
    }
    /**
     * 进入到分配菜单的权限
     */
    @RequestMapping("roleModule")
    public String roleModule(String roleid){
        Role list = roleService.findById(roleid);
        request.setAttribute("role",list);
        return "system/role/role-module";
    }
    /**
     * 接收前端页面发送的ajax请求,获取所有的模块数据
     *     返回的json数据
     *        var zNodes =[
     *			 { id:111, pId:11, name:"随意勾选 1-1-1"},
     *			 { id:112, pId:11, name:"随意勾选 1-1-2"}
     *       }
     *     方法的返回值需要以 @ResponseBody修饰
     *     方法的返回值 : List<Map>
     *     参数 : roleId
     */
    @RequestMapping("/getZtreeNodes")
    public @ResponseBody List<Map> getZtreeNodes(String roleId) {
        List<Map> list = new ArrayList<>();
        //查询所有的模块
        List<Module> modules = moduleService.findAll();                     //合同管理, 用户管理 ,权限管理
        //查询当前角色的所有的模块数据
        List<Module> roleModules = moduleService.findByRoleId(roleId);      //合同管理,用户管理
        //循环模块列表,构造返回数据
        for (Module module : modules) {     //
            Map map = new HashMap();
            map.put("id",module.getId());
            map.put("pId",module.getParentId());
            map.put("name",module.getName());
            if(roleModules.contains(module)){
                map.put("checked",true);
            }
            list.add(map);
        }
        return list;
    }
    /**
     * 分配权限
     *  参数:
     *      roleid  : 当前的角色id
     *      moduleIds : 所有勾选的模块id,中间使用","分割
     */
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleid,String moduleIds) {
        roleService.updateRoleModule(roleid,moduleIds);
        return "redirect:/system/role/list.do";
    }

}
