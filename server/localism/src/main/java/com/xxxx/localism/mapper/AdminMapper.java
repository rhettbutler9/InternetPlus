package com.xxxx.localism.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.localism.pojo.Admin;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper extends BaseMapper<Admin> {


    Admin getAdminByUserName(String username);

    Integer addStar(@Param("Id") Integer Id, @Param("adminId") Integer adminId, @Param("table1") String table1, @Param("table2") String table2, @Param("id") String id);

    Integer deleteStar(@Param("videoId") Integer videoId, @Param("adminId") Integer adminId, @Param("table1") String table1, @Param("table2") String table2, @Param("id") String id);

    Integer addCollect(@Param("videoId") Integer videoId, @Param("adminId") Integer adminId, String table1, String table2, String id);

    Integer deleteCollect(@Param("videoId") Integer videoId, @Param("adminId") Integer adminId, String table1, String table2, String id);

    /**
     * 注册
     * @param adminLoginParam
     * @return
     */
}
