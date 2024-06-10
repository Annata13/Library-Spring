package library.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import java.util.EnumSet;

public class MySpringMvcDispatcherServetInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // Определяем корневые конфигурационные классы. Возвращаем null, так как у нас их нет.
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    // Определяем конфигурационные классы для DispatcherServlet.
    // Возвращаем SpringConfig.class, который содержит конфигурацию приложения.
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class};
    }

    // Определяем маппинги для DispatcherServlet. "/" указывает, что он будет обрабатывать все запросы.
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    // Переопределяем метод onStartup для настройки фильтров при старте приложения.
    // Добавляем фильтры CharacterEncodingFilter и HiddenHttpMethodFilter.
    @Override
    public void onStartup (ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        registerCharacterEncodingFilter(aServletContext); // Регистрация фильтра для кодировки символов
        registerHiddenFieldFilter(aServletContext); // Регистрация фильтра для обработки скрытых HTTP методов
    }

    // Регистрация HiddenHttpMethodFilter, который добавляется для всех URL.
    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null,true,"/*");
    }

    // Регистрация CharacterEncodingFilter для обработки кодировки символов.
    private void registerCharacterEncodingFilter(ServletContext aContext){
        // Устанавливаем типы Dispatcher, для которых будет работать фильтр.
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);

        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8"); // Устанавливаем кодировку UTF-8
        characterEncodingFilter.setForceEncoding(true); // Принуждаем использовать указанную кодировку

        // Добавляем фильтр к контексту сервлета и устанавливаем его для всех URL.
        FilterRegistration.Dynamic caracterEncoding = aContext.addFilter("characterEncoding", characterEncodingFilter);
        caracterEncoding.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
    }
}