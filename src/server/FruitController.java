package server;

import biz.FruitService;
import biz.implFruitService;
import com.sun.deploy.net.HttpRequest;
import dao.FruitDAOImpl;
import domain.Fruit;
import utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class FruitController {

    private FruitService fruitService = null;

    private String index(String oper, String keyword, Integer pageNo, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (pageNo == null) {
            pageNo = 1;
        }
        if (StringUtil.isNotEmpty(oper) && "search".equals(oper)) {
            pageNo = 1;
            if (StringUtil.isEmpty(keyword)) {
                keyword = "";
            }
            session.setAttribute("keyword", keyword);
        } else {
            Object keywordObj = session.getAttribute("keyword");
            if (keywordObj != null) {
                keyword = (String) keywordObj;
            } else {
                keyword = "";
            }
        }
        session.setAttribute("pageNo", pageNo);

        List<Fruit> fruitList = fruitService.getFruitList(keyword, pageNo);
        session.setAttribute("fruitList", fruitList);
        int pageCount = fruitService.getPageCount(keyword);

        session.setAttribute("pageCount", pageCount);
        return "index";
    }

    private String add(String fName, Integer price, Integer fcount, String remark) {
        fruitService.addFruit(new Fruit(0, fName, price, fcount, remark));
        return "redirect:fruit.do";
    }

    private String delete(Integer fid) {
        fruitService.delFruit(fid);
        return "redirect:fruit.do";
    }

    private String edit(Integer fid, HttpServletRequest request) {
        Fruit fruitByFid = fruitService.getFruitById(fid);
        request.setAttribute("fruit", fruitByFid);
        return "edit";

    }

    private String update(Integer fid, String fname, Integer price, Integer fcount, String remark) {
        fruitService.updateFruit(new Fruit(fid, fname, price, fcount, remark));
        return "redirect:fruit.do";
    }
}
