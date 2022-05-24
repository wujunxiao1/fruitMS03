package server;

import dao.FruitDAOImpl;
import domain.Fruit;
import utils.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class FruitController extends ViewBaseServlet {

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        try {
            super.init(servletContext);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private FruitDAOImpl fruitDAO = new FruitDAOImpl();
    private void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // 设置当前页，默认值1
        Integer pageNo = 1;

        String oper = request.getParameter("oper");

        //如果oper!=null 说明 通过表单的查询按钮点击过来的
        //如果oper是空的，说明 不是通过表单的查询按钮点击过来的
        String keyword = null;
        if (StringUtil.isNotEmpty(oper) && "search".equals(oper)) {
            //说明是点击表单查询发送过来的请求
            //此时，pageNo应该还原为1 ， keyword应该从请求参数中获取
            pageNo = 1;
            keyword = request.getParameter("keyword");
            //如果keyword为null，需要设置为空字符串""，否则查询时会拼接成 %null% , 我们期望的是 %%
            if (StringUtil.isEmpty(keyword)) {
                keyword = "";
            }
            //将keyword保存（覆盖）到session中
            session.setAttribute("keyword", keyword);
        } else {
            //说明此处不是点击表单查询发送过来的请求（比如点击下面的上一页下一页或者直接在地址栏输入网址）
            //此时keyword应该从session作用域获取
            String pageNoStr = request.getParameter("pageNo");
            if (StringUtil.isNotEmpty(pageNoStr)) {
                pageNo = Integer.parseInt(pageNoStr);   //如果从请求中读取到pageNo，则类型转换。否则，pageNo默认就是1
            }
            //如果不是点击的查询按钮，那么查询是基于session中保存的现有keyword进行查询
            Object keywordObj = session.getAttribute("keyword");
            if (keywordObj != null) {
                keyword = (String) keywordObj;
            } else {
                keyword = "";
            }
        }

        // 重新更新当前页的值
        session.setAttribute("pageNo", pageNo);

        List<Fruit> fruitList = fruitDAO.getFruitList(keyword, pageNo);
        session.setAttribute("fruitList", fruitList);

        //总记录条数
        int fruitCount = fruitDAO.getFruitCount(keyword);
        //总页数
        int pageCount = (fruitCount + 5 - 1) / 5;
        /*
        总记录条数       总页数
        1               1
        5               1
        6               2
        10              2
        11              3
        fruitCount      (fruitCount+5-1)/5
         */
        session.setAttribute("pageCount", pageCount);

        //此处的视图名称是 index
        //那么thymeleaf会将这个 逻辑视图名称 对应到 物理视图 名称上去
        //逻辑视图名称 ：   index
        //物理视图名称 ：   view-prefix + 逻辑视图名称 + view-suffix
        //所以真实的视图名称是：      /       index       .html
        super.processTemplate("index", request, response);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String fname = request.getParameter("fname");
        int price = Integer.parseInt(request.getParameter("price"));
        int fcount = Integer.parseInt(request.getParameter("fcount"));
        String remark = request.getParameter("remark");
        fruitDAO.addFruit(new Fruit(0, fname, price, fcount, remark));
        response.sendRedirect("fruit.do");
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fidStr = request.getParameter("fid");
        int fid = Integer.parseInt(fidStr);
        fruitDAO.delFruit(fid);
        response.sendRedirect("fruit.do");
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fid_privous = request.getParameter("fid");
        int fid = Integer.parseInt(fid_privous);
        FruitDAOImpl fruitDAO = new FruitDAOImpl();
        Fruit fruitByFid = fruitDAO.getFruitByFid(fid);
        request.setAttribute("fruit", fruitByFid);
        super.processTemplate("edit", request, response);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //暂时先不写，等把del和add写了再写。。。。
        request.setCharacterEncoding("utf-8");
        String fname = request.getParameter("fname");
        String priceStr = request.getParameter("price");
        int price = Integer.parseInt(priceStr);
        String fcountStr = request.getParameter("fcount");
        int fcount = Integer.parseInt(fcountStr);
        String remark = request.getParameter("fremark");
        String fidstr = request.getParameter("fid");
        int fid = Integer.parseInt(fcountStr);
        fruitDAO.updateFruit(new Fruit(fid, fname, price, fcount, remark));
        //super.processTemplate("index", request, response);
        response.sendRedirect("fruit.do");
    }
}
