package com.cluster.service.Impl;

import com.cluster.enums.Category;
import com.cluster.exception.RecordNotFoundException;
import com.cluster.mapper.CategoryMapper;
import com.cluster.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category getCategoryByName(Category category) {
        Category curr= categoryMapper.getCategoryByName(category);
        if(curr == null)
        {
            throw new RecordNotFoundException("当前分类不存在");
        }
        return curr;
    }


    /**
     * 根据分类获得分类下cluster数量
     * @param category
     * @return
     */
    @Override
    public Integer getClusterCountByCategory(Category category) {
        Integer count = categoryMapper.getClusterCountByCategory(category);
        if(count == null)
        {
            throw new RecordNotFoundException("当前分类不存在");
        }
        return count;
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryMapper.getAllCategory();
    }

    @Override
    @Transactional
    public void createCategory(Category category) {
        if(category == null)
        {
            throw new IllegalArgumentException("当前要创建的分类为空");
        }
        categoryMapper.insert(category);

    }

    @Override
    @Transactional
    public void updateCategory(Integer id, Category category) {
        if(category == null)
        {
            throw new IllegalArgumentException("当前要更新的分类为空");
        }
        categoryMapper.update(id,category);

    }

    /**
     * 常会被调用的方法，用于更新当前分类下cluster的数量
     * @param category
     * @param count
     */
    @Override
    @Transactional
    public void updateCount(Category category, Integer count) {
        if(category == null)
        {
            throw new IllegalArgumentException("当前要更新cluster数量的分类为空");
        }
        Integer newCount = getClusterCountByCategory(category);
        newCount++;
        categoryMapper.updateCount(category,newCount);

    }

    @Override
    public void deleteById(Integer id) {
        int rowsAffected = categoryMapper.deleteById(id);
        if(rowsAffected == 0)
        {
            throw new RecordNotFoundException("你要删除的分类不存在!");
        }
    }
}
