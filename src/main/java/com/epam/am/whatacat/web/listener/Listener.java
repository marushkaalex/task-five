package com.epam.am.whatacat.web.listener;

import com.epam.am.whatacat.action.ActionFactory;
import com.epam.am.whatacat.db.ConnectionPool;
import com.epam.am.whatacat.db.ConnectionPoolException;
import com.epam.am.whatacat.validation.FormValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebListener()
public class Listener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(Listener.class);

    public void contextInitialized(ServletContextEvent sce) {
        FormValidatorFactory.init();
        Properties poolProperties = new Properties();
        try {
            InputStream resourceAsStream = Listener.class.getResourceAsStream("/pool.properties");
            poolProperties.load(resourceAsStream);
            ConnectionPool.init(poolProperties);
            ActionFactory.init();
            LOG.info("Initialization was successful");
        } catch (IOException | ConnectionPoolException e) {
            throw new RuntimeException("Unable to configure connection pool properties", e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        try {
            ConnectionPool.getInstance().shutDown();
        } catch (ConnectionPoolException e) {
            throw new RuntimeException("Unable to shutdown connection pool", e);
        }
    }
}
