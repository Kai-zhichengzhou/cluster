package com.cluster.service;

import com.cluster.enums.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryService {

    /**
     * 获取分类
     * @param category
     * @return
     */
    Category getCategoryByName(Category category);

    /**
     * 根据分类获得分类下cluster数量
     * @param category
     * @return
     */
    Integer getClusterCountByCategory(Category category);

    /**
     * 获取所有分类
     * @return
     */
    List<Category> getAllCategory();

    /**
     * 创建新的分类
     * @param category
     */
    void createCategory(Category category);

    /**
     * 更新分类
     * @param category
     */
    void updateCategory(Integer id,  Category category);

    /**
     * 更新分类数量
     * @param category
     */
    void updateCount(Category category,Integer count);

    /**
     * 删除分类
     * @param id
     */
    void deleteById(Integer id);
}
