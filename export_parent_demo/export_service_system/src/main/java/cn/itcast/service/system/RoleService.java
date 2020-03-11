package cn.itcast.service.system;

import cn.itcast.domain.system.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoleService {

    PageInfo findAll(int page,int size,String companyId);

    Role findById(String id);

    void save(Role role);

    void update(Role role);

    void delete(String id);


    void updateRoleModule(String roleid, String moduleIds);

    List<Role> findAll(String loginCompanyId);

    List<Role> findByUserId(String id);
}
