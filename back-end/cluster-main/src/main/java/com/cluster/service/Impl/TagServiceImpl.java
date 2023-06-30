package com.cluster.service.Impl;

import com.cluster.exception.RecordNotFoundException;
import com.cluster.mapper.TagMapper;
import com.cluster.pojo.Tag;
import com.cluster.service.TagService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    /**
     * 获取tag
     * @param tagName
     * @return
     */
    @Override
    public Tag getTagByName(String tagName) {
        Tag tag = tagMapper.getTagByName(tagName);
        if(tag == null)
        {
            throw new RecordNotFoundException("你查找的标签:" + tagName + "不存在!");
        }
        return tag;
    }


    /**
     * 获取所有tag
     * @return
     */
    @Override
    public List<Tag> getAllTags() {
        return tagMapper.getAllTags();
    }

    /**
     * 创建tag
     * @param tag
     */
    @Override
    @Transactional
    public void createTag(Tag tag) {
        if(tag == null)
        {
            throw new IllegalArgumentException("你创建的标签不能为空!");
        }
        tagMapper.insert(tag);

    }


    /**
     * 删除tag
     * @param tagName
     */
    @Override
    public void deleteByName(String tagName) {
        int rowAffected = tagMapper.deleteByName(tagName);
        if(rowAffected == 0)
        {
            throw new RecordNotFoundException("你要删除的标签:" + tagName +"不存在!");
        }

    }
}
