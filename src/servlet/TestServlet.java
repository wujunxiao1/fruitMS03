package servlet;

import dao.FruitDAOImpl;
import domain.Fruit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/111")
public class TestServlet extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        Integer pageNo = 1 ;
        HttpSession session = request.getSession();
        String oper = request.getParameter("oper");
        String keyWord = null;
        if (oper != null && "search".equals(oper)){
            pageNo = 1;
            keyWord = request.getParameter("keyword");
            if (keyWord == null){
                keyWord = "";
            }
            session.setAttribute("keyword",keyWord);
        }else {
            String pageNostr = request.getParameter("pageNo");
            if (pageNostr != null){
                pageNo = Integer.parseInt(pageNostr);
            }
            session.getAttribute();
        }
        FruitDAOImpl fruitDAO = new FruitDAOImpl();

        session.setAttribute("pageNo", pageNo);
        int fruitCount = fruitDAO.getFruitCount();
        int lastPage = (fruitCount + 5 -1) / 5;
        session.setAttribute("lastPage",lastPage);
        List<Fruit> fruitList = fruitDAO.getFruitList(pageNo);
        //保存到session作用域
        session.setAttribute("fruitList",fruitList);
        //此处的视图名称是 index
        //那么thymeleaf会将这个 逻辑视图名称 对应到 物理视图 名称上去
        //逻辑视图名称 ：   index
        //物理视图名称 ：   view-prefix + 逻辑视图名称 + view-suffix
        //所以真实的视图名称是：      /       index       .html
        super.processTemplate("index",request,response);
    }

}
