package cn.itcast.dao.stat;

import java.util.List;
import java.util.Map;

public interface StatDao {
    /**
     * 查询厂家销售数据
     */
    List<Map> getFactoryData(String companyId);
    /**
     * 查询产品销量的排行榜
     */
    List<Map> getSellData(String companyId);
    /**
     * 查询系统访问压力图
     */
    List<Map> getOnLineData(String companyId);
}
