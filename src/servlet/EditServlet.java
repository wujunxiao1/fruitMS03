package servlet;

import dao.FruitDAOImpl;
import domain.Fruit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/edit.do")
public class EditServlet extends ViewBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fid_privous = request.getParameter("fid");
        int fid = Integer.parseInt(fid_privous);
        FruitDAOImpl fruitDAO = new FruitDAOImpl();
        Fruit fruitByFid = fruitDAO.getFruitByFid(fid);
        request.setAttribute("fruit", fruitByFid);
        super.processTemplate("edit", request, response);

    }
}
