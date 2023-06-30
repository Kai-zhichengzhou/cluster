package com.cluster.mapper;

import com.cluster.pojo.Tag;

import java.util.List;

public interface TagMapper{

    /**
     * 获取tag
     * @param tagName
     * @return
     */
    Tag getTagByName(String tagName);

    /**
     * 获取所有tag
     * @return
     */
    List<Tag> getAllTags();

    /**
     * 创建tag
     * @param tag
     */
    void insert(Tag tag);


    /**
     * 删除tag
     * @param tagName
     */
    Integer deleteByName(String tagName);



}
