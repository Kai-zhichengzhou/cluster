package com.cluster.mapper;

import com.cluster.enums.Category;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface CategoryMapper {

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
    void insert(Category category);

    /**
     * 更新分类
     * @param category
     */
    void update(@Param("id") Integer id, @Param("category") Category category);

    /**
     * 更新分类数量
     * @param category
     */
    void updateCount(@Param("category") Category category,@Param("count")Integer count);

    /**
     * 删除分类
     * @param id
     */
    Integer deleteById(Integer id);
}
