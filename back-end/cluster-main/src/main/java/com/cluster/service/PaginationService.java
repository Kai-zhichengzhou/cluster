package com.cluster.service;

import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.function.Supplier;

public interface PaginationService {
    <T> PageInfo<T> getEntityByPage(Integer page, Integer size, Supplier<List<T>> supplier);
}
