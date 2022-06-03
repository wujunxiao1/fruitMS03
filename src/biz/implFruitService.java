package biz;

import dao.FruitDAOImpl;
import domain.Fruit;

import java.util.List;

public class implFruitService implements FruitService {
    private FruitDAOImpl fruitDAO = null;

    @Override
    public List<Fruit> getFruitList(String keyword, Integer page) {
        return fruitDAO.getFruitList(keyword, page);
    }

    @Override
    public void addFruit(Fruit fruit) {
        fruitDAO.addFruit(fruit);
    }

    @Override
    public Fruit getFruitById(Integer fid) {
        return fruitDAO.getFruitByFid(fid);
    }

    @Override
    public void delFruit(Integer fid) {
        fruitDAO.delFruit(fid);
    }

    @Override
    public Integer getPageCount(String keyWord) {
        int fruitCount = fruitDAO.getFruitCount(keyWord);
        int pageCount = (fruitCount + 5 - 1) / 5;
        return pageCount;
    }

    @Override
    public void updateFruit(Fruit fruit) {
        fruitDAO.updateFruit(fruit);
    }
}
