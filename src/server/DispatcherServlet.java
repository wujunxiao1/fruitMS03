package server;

import bean.BeanFactory;
import bean.ClassPathXmlApplicationContext;
import utils.StringUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {
    private BeanFactory beanFactory = null;

    public DispatcherServlet() {
    }

    public void init() {
        try {
            super.init();
//            beanFactory = new ClassPathXmlApplicationContext();
            beanFactory = (BeanFactory) getServletContext().getAttribute("beanFactory");
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        //request.setCharacterEncoding("UTF-8");
        //假设url是：  http://localhost:8080/pro15/hello.do
        //那么servletPath是：    /hello.do
        // 我的思路是：
        // 第1步： /hello.do ->   hello   或者  /fruit.do  -> fruit
        // 第2步： hello -> HelloController 或者 fruit -> FruitController
        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, lastDotIndex);

        Object controllerBeanObj = beanFactory.getBean(servletPath);

        String operate = request.getParameter("operate");
        if (StringUtil.isEmpty(operate)) {
            operate = "index";
        }
        Object[] parameterValues = null;
        try {
            Method[] declaredMethods = controllerBeanObj.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.getName().equals(operate)) {
                    Parameter[] parameters = method.getParameters();
                    parameterValues = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        String parameterName = parameter.getName();
                        if ("request".equals(parameterName)) {
                            parameterValues[i] = request;
                        } else if ("response".equals(parameterName)) {
                            parameterValues[i] = response;
                        } else if ("session".equals(parameterName)) {
                            HttpSession session = request.getSession();
                            parameterValues[i] = session;
                        } else {
                            String parameterValue = request.getParameter(parameterName);
                            String parameterTypeName = parameter.getType().getName();
                            Object parameterObj = parameterValue;
                            if (parameterValue != null) {
                                if ("java.lang.Integer".equals(parameterTypeName)) {
                                    parameterObj = Integer.parseInt(parameterValue);
                                }
                            }
                            parameterValues[i] = parameterObj;
                        }

                    }
                    method.setAccessible(true);
                    String methodReturnStr = (String) method.invoke(controllerBeanObj, parameterValues);
                    if (methodReturnStr.startsWith("redirect:")) {
                        String methodReturnStr1 = methodReturnStr.substring("redirect:".length());
                        response.sendRedirect(methodReturnStr1);
                    } else {
                        super.processTemplate(methodReturnStr, request, response);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
