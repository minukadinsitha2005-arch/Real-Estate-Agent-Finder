package com.realestatefinder.listener;

import com.realestatefinder.util.FileStorageUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String dataPath = sce.getServletContext().getRealPath("/WEB-INF/data");
        FileStorageUtil.initialize(dataPath);
    }
}
