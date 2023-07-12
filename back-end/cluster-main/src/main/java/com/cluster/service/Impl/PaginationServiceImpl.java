package com.cluster.service.Impl;


import com.cluster.service.PaginationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Service
public class PaginationServiceImpl implements PaginationService {


    public <T>  PageInfo<T> getEntityByPage(Integer page, Integer size, Supplier<List<T>> supplier)
    {
        PageHelper.startPage(page, size);

        List<T> entities = supplier.get();

        PageInfo<T> pageInfo = new PageInfo<>(entities);
        //处理分页逻辑
        //如果当前要查询的页面已经超出了总数据量，返回空list
        if(page > pageInfo.getPages() && pageInfo.getPages() != 0)
        {
            pageInfo.setList(Collections.emptyList());
        }

        return pageInfo;

    }


}
