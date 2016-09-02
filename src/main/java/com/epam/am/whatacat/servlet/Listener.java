package com.epam.am.whatacat.servlet;

import com.epam.am.whatacat.validation.FormValidatorFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener()
public class Listener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        FormValidatorFactory.init();
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
