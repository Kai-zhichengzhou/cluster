package com.cluster.service;

import com.cluster.pojo.Tag;

import java.util.List;

public interface TagService {

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
    void createTag(Tag tag);


    /**
     * 删除tag
     * @param tagName
     */
    void deleteByName(String tagName);



}
