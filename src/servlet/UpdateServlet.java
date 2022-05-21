package servlet;

import dao.FruitDAOImpl;
import domain.Fruit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/update.do")
public class UpdateServlet extends ViewBaseServlet {
    private FruitDAOImpl fruitDAO = new FruitDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        response.sendRedirect("111");
    }
}
