package listener;

import bean.BeanFactory;
import bean.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        BeanFactory classPathXmlApplicationContext = new ClassPathXmlApplicationContext();
        ServletContext applicationContext = servletContextEvent.getServletContext();
        applicationContext.setAttribute("beanFactory",classPathXmlApplicationContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("发现上下文销毁");
    }
}
