package cn.enjoyedu.rwseparation.config;

import cn.enjoyedu.rwseparation.dbutils.MyRoutingDataSource;
import cn.enjoyedu.rwseparation.vo.DBTypeEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.jdbc.DataSourceBuilder.create;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：数据源的相关配置
 */
@Configuration
public class DataSourceConfig {
    /*主库的数据源*/
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    /*从库1的数据源*/
    @Bean
    @ConfigurationProperties("spring.datasource.slave1")
    public DataSource slave1DataSource() {
        // import static org.springframework.boot.jdbc.DataSourceBuilder.create;
        return create().build();
    }

    /*自定义数据源，内部持有了主库和从库的数据源，
通过某种机制让应用程序在进行数据读写时，按业务情况走主库或者从库*/
    @Bean
    public DataSource myRoutingDataSource(
            @Qualifier("masterDataSource")DataSource masterDataSource,
            @Qualifier("slave1DataSource")DataSource slaveDataSource){
        Map<Object, Object> targetDataSources = new HashMap<>();
        //MASTER SLAVE 和DataSource的映射关系
        targetDataSources.put(DBTypeEnum.MASTER,masterDataSource);
        targetDataSources.put(DBTypeEnum.SLAVE,slaveDataSource);

        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        /*当执行的方法没有被Aop拦截时，缺省使用的数据源*/
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        return myRoutingDataSource;
    }

//MyRoutingDataSource extends AbstractRoutingDataSource
//AbstractRoutingDataSource 里包含了  DataSource 映射关系map = targetDataSources
/*
* 当需要使用到DataSource时，会调用规则方法determineCurrentLookupKey()
* *********************************************
* determineCurrentLookupKey 会从ThreadLocal对象中获取当前线程持有的变量，方法返回时会将该变量反映射成对应的DataSource
* */

}
