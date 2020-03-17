package com.uboxol.cloud.pandorasBox.cfg;

import com.alibaba.druid.pool.DruidDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

import static com.uboxol.cloud.pandorasBox.cfg.ZcgDataSourceConfig.JPA_PACKAGE;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/18 17:22
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = JPA_PACKAGE)
public class ZcgDataSourceConfig {

    private static final String ENTITY_LOCATION = "com.uboxol.cloud.pandorasBox.db.entity.zcg";

    static final String JPA_PACKAGE = "com.uboxol.cloud.pandorasBox.db.repository.zcg";

    @Primary
    @Bean(name = {"zcgDataSource", "datasource"})
    @ConfigurationProperties(prefix = "spring.datasource.zcg")
    public DataSource oracleDataSource() {
        return new DruidDataSource();
    }

    @Primary
    @Bean(name = {"zcgEntityManagerFactory", "entityManagerFactory"})
    public EntityManagerFactory kefuEntityManagerFactory(JpaProperties jpaProperties,
	            @Qualifier("zcgDataSource") DataSource zcgDataSource) {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(zcgDataSource);
		factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		
		Map<String, String> properties = jpaProperties.getProperties();
		properties.put("hibernate.default_catalog","pandorasbox");
		factory.setJpaPropertyMap(properties);
		adapter.setShowSql(jpaProperties.isShowSql());
		factory.setJpaVendorAdapter(adapter);
		factory.setPackagesToScan(ENTITY_LOCATION);
		factory.afterPropertiesSet();
		return factory.getObject();
	}
    
    @Bean
    public EntityManager entityManager(@Qualifier("entityManagerFactory") EntityManagerFactory factory) {
        return factory.createEntityManager();
    }
    
    @Primary
    @Bean(name = {"zcgTransactionManager", "transactionManager"})
    public PlatformTransactionManager transactionManager(
        @Qualifier("zcgEntityManagerFactory") EntityManagerFactory zcgEntityManagerFactory) {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(zcgEntityManagerFactory);
        return manager;
    }
}

