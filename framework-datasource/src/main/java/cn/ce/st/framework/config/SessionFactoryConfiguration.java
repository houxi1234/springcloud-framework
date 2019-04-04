package cn.ce.st.framework.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @ClassName SessionFactoryConfiguration
 * @Descrition SqlSessionFactory 配置
 * @Author houxi
 * @Date 2019/3/27 15:21
 * Version 1.0
 **/
@SpringBootConfiguration
public class SessionFactoryConfiguration {

    @Value("${mybatis.mapper-locations}")
    private String mapperXMLConfigPath;

    @Value("${mybatis.type-aliases-package}")
    private String mapperPackagePath;

    @Autowired
    private DataSource dataSource;

    @Bean
    public SqlSessionFactoryBean createSqlSessionFactory() throws IOException {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mapperXMLConfigPath));
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage(mapperPackagePath);

        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);

        return sqlSessionFactoryBean;
    }
}
