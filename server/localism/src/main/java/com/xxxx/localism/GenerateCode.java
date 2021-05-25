package com.xxxx.localism;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

/**
 * 代码生成器
 */
public class GenerateCode {

    public static void main(String[] args) {

        AutoGenerator mpg = new AutoGenerator();

        //1.全局配置
        GlobalConfig gc=new GlobalConfig();
        gc.setAuthor("xxx");
        String path = System.getProperty("user.dir");
        gc.setOutputDir(path+"/src/main/java");
        gc.setOpen(false);                      //是否打开资源管理器
        gc.setFileOverride(false);              //是否覆盖
        gc.setBaseResultMap(true);              //xml开启 BaseResultMap
        gc.setBaseColumnList(true);             //xml 开启BaseColumnList
        gc.setIdType(IdType.AUTO);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);
        //2.数据源配置
        DataSourceConfig dsc=new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl("jdbc:mysql://120.76.57.148:3306/localism?useUnicode=true&characterEncoding=UTF8&allowMultiQueries=true&serverTimezone=Asia/Shanghai");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);
        //3.包的配置
        PackageConfig pc=new PackageConfig();
        pc.setParent("com.xxxx");
        pc.setModuleName("localism");
        pc.setEntity("pojo");
        pc.setMapper("mapper");
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setXml("mapper");
        mpg.setPackageInfo(pc);
        //4.策略配置
        StrategyConfig strategy = new StrategyConfig();

        strategy.setInclude("t_dynamic");                           //填写表名
        strategy.setTablePrefix("t_");
        strategy.setNaming(NamingStrategy.underline_to_camel);   //下划线转驼峰
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);                    //是否使用Lombok注解
        strategy.setRestControllerStyle(true);
        strategy.setVersionFieldName("version");                //乐观锁
        strategy.setLogicDeleteFieldName("deleted");            //逻辑删除
        /**
         * 自动填充策略
         */
        TableFill gmtCreate = new TableFill("create_time", FieldFill.INSERT);
        TableFill gmtModified = new TableFill("modified_time", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> arrayList = new ArrayList<>();
        strategy.setTableFillList(arrayList);
        mpg.setStrategy(strategy);
        mpg.execute();
    }
}
