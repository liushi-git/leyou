package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ModuleService {

    PageInfo findAll(int page,int size);

    Module findById(String id);

    void save(Module module);

    void update(Module module);

    void delete(String id);

    List<Module> findAll();

    List<Module> findByRoleId(String roleId);

    List<Module> findByUserId(String id);
}
