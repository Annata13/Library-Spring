package library.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("library") // Сканируем пакет "library" для поиска аннотированных компонентов
@EnableWebMvc // Включаем поддержку Spring MVC
@PropertySource("classpath:hibernate.properties") // Загрузка проперти файла для конфигурации Hibernate
@EnableTransactionManagement // Включаем управление транзакциями в Spring
@EnableJpaRepositories("library.repositories") // Включаем поддержку JPA репозиториев и указываем пакет для поиска репозиториев
public class SpringConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;
    private final Environment environment; // Для доступа к переменным окружения

    @Autowired
    public SpringConfig(ApplicationContext applicationContext, Environment environment){
        this.applicationContext = applicationContext;
        this.environment = environment;
    }

    // Настраиваем Thymeleaf шаблонизатор
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    // Настраиваем движок шаблонов Thymeleaf
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    // Настраиваем разрешение представлений
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry){
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setContentType("text/html; charset=UTF-8");
    }

    // Настраиваем источник данных
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        // Конфигурация БД
        dataSource.setDriverClassName(environment.getRequiredProperty("hibernate.driver_class"));
        dataSource.setUrl(environment.getRequiredProperty("hibernate.connection.url"));
        dataSource.setUsername(environment.getRequiredProperty("hibernate.connection.username"));
        dataSource.setPassword(environment.getRequiredProperty("hibernate.connection.password"));

        return dataSource;
    }

    // Настраиваем свойства Hibernate
    private Properties hibernateProperties(){
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));

        return properties;
    }

    // Настраиваем фабрику EntityManager
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        // Создаем экземпляр LocalContainerEntityManagerFactoryBean
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        // Устанавливаем источник данных
        em.setDataSource(dataSource());
        // Указываем пакет, в котором находятся наши сущности (Entity классы)
        em.setPackagesToScan("library.model");
        // Создаем и настраиваем адаптер Hibernate JPA
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        // Устанавливаем свойства Hibernate
        em.setJpaProperties(hibernateProperties());
        // Возвращаем настроенный экземпляр фабрики EntityManager
        return em;
    }

    // Настраиваем менеджер транзакций
    @Bean
    public PlatformTransactionManager transactionManager(){
        // Создаем экземпляр JpaTransactionManager
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        // Устанавливаем фабрику EntityManager, которая будет использоваться для транзакций
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        // Возвращаем настроенный экземпляр менеджера транзакций
        return transactionManager;
    }
}