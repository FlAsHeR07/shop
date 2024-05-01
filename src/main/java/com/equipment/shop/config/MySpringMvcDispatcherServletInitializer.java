package com.equipment.shop.config;

import com.equipment.shop.ShopApplication;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MySpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {ShopApplication.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
}
