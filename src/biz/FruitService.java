package biz;

import domain.Fruit;

import java.util.List;

public interface FruitService {
    //获取指定页面的库存列表信息
    List<Fruit> getFruitList(String Keyword,Integer page);
    //添加库存信息
    void addFruit(Fruit fruit);
    //根据id查看指定库存信息
    Fruit getFruitById(Integer fid);
    //删除特定库存记录
    void delFruit(Integer fid);
    //获取总页数
    Integer getPageCount(String keyWord);
    //修改特定库存记录
    void updateFruit(Fruit fruit);
}
