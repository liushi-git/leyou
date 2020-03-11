package cn.itcast.service.system.impl;

import cn.itcast.dao.system.RoleDao;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Override
    public PageInfo findAll(int page, int size, String companyId) {
        PageHelper.startPage(page,size);
        List<Role> list = roleDao.findAll(companyId);
        return new PageInfo<>(list);
    }

    @Override
    public Role findById(String id) {
        Role byId = roleDao.findById(id);
        return byId;
    }

    @Override
    public void save(Role role) {
        role.setId(UUID.randomUUID().toString());
        roleDao.save(role);
    }

    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    @Override
    public void delete(String id) {
        roleDao.delete(id);
    }

    @Override
    public void updateRoleModule(String roleId, String moduleIds) {
        //根据角色id删除中间表数据
        roleDao.deleteRoleModule(roleId);
        //拆分模块的id数组
        String[] ids = moduleIds.split(",");
        //循环id数组，像中间表保存数据
        for (String moduleId : ids) {
            roleDao.saveRoleModule(roleId,moduleId);
        }
    }

    @Override
    public List<Role> findAll(String loginCompanyId) {
        List<Role> all = roleDao.findAll(loginCompanyId);
        return all;
    }

    @Override
    public List<Role> findByUserId(String id) {
        return roleDao.findByUserId(id);
    }
}
