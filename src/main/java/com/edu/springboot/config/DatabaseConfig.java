/**
 * 파일위치: src/main/java/com/edu/springboot/config/DatabaseConfig.java
 * 기능전체: 데이터베이스 연결 관리(HikariCP) 및 MyBatis 설정을 담당합니다.
 * 💡 중요 수정: MyBatis가 일반 인터페이스(Service)를 Mapper로 착각하지 않도록, 
 * @Mapper 어노테이션이 붙은 클래스만 스캔하도록 annotationClass 속성을 추가했습니다.
 */
package com.edu.springboot.config;

import javax.sql.DataSource;
import jakarta.annotation.PostConstruct;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
// ✅ 핵심 해결책: domain 패키지 하위에서 오직 @Mapper 가 붙은 인터페이스만 스캔하도록 강제합니다!
@MapperScan(basePackages = "com.edu.springboot.domain", annotationClass = Mapper.class)
@EnableTransactionManagement
public class DatabaseConfig {

    @PostConstruct
    public void init() {
        System.out.println("✅ [config] Database 설정 완료 (Mapper 정확도 향상)");
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        
        // 매퍼 XML 위치 설정
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/**/*.xml"));
        sessionFactory.setTypeAliasesPackage("com.edu.springboot.domain.**.vo");
        
        org.apache.ibatis.session.Configuration mybatisConfig = new org.apache.ibatis.session.Configuration();
        mybatisConfig.setMapUnderscoreToCamelCase(true);
        sessionFactory.setConfiguration(mybatisConfig);
        
        return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}