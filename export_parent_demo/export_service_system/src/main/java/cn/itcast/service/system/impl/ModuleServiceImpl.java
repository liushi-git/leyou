package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    private ModuleDao moduleDao;
    @Autowired
    private UserService userService;
    @Override
    public PageInfo findAll(int page, int size) {
        PageHelper.startPage(page, size);
        List<Module> list = moduleDao.findAll();
        return new PageInfo(list);
    }

    @Override
    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    @Override
    public void save(Module module) {
        module.setId(UUID.randomUUID().toString());
        moduleDao.save(module);
    }

    @Override
    public void update(Module module) {
        moduleDao.update(module);
    }

    @Override
    public void delete(String id) {
        moduleDao.delete(id);
    }

    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }



    @Override
    public List<Module> findByRoleId(String roleId) {
        return moduleDao.findByRoleId(roleId);
    }

    /**
     * 根据用户查询用户可以操作的模块
     *      1.根据用户id查询用户
     *      2.获取用户的degree等级
     *      2.1 degree==0 saas管理员：查询belong=0的所有模块
     *      2.2 degree==1 企业管理元 ：查询belong=1的所有模块
     */
    public List<Module> findByUserId(String id) {
        //1.根据用户id查询用户
        User user = userService.findById(id);
        //2.获取用户的degree等级
        Integer degree = user.getDegree();
        if(degree==0){
            //2.1 degree==0 saas管理员：查询belong=0的所有模块
            return moduleDao.findByBelong(0);
        }else if (degree==1){
            //2.2 degree==1 企业管理元 ：查询belong=1的所有模块
            return moduleDao.findByBelong(1);
        }else {
            //2.3企业普通员工，根据rbac查询模块
            return moduleDao.findByUserId(id);
        }
    }
}
