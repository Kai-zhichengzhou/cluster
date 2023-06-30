package com.cluster.controller;


import com.cluster.exception.RecordNotFoundException;
import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.Tag;
import com.cluster.service.TagService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @ApiOperation(value = "获取所有的标签")
    @GetMapping("/list")
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }



    @ApiOperation(value = "获取特定的标签")
    @GetMapping("/{tagName}")
    public ApiResponse getTagByName(@PathVariable String tagName) {
        try {
            Tag tag = tagService.getTagByName(tagName);
            return ApiResponse.success("查找标签成功", tag);
        } catch (RecordNotFoundException exception) {
            exception.printStackTrace();
            return ApiResponse.error("你查找的标签不存在");
        }
    }

    @ApiOperation(value = "创建新的标签")
    @PostMapping("/")
    public ApiResponse createTag(@RequestBody Tag tag) {
        try {
            tagService.createTag(tag);
            return ApiResponse.success("创建标签成功!");
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
            return ApiResponse.error("很抱歉，创建标签失败");
        }
    }


    @ApiOperation(value = "删除对应的标签")
    @DeleteMapping("/{tagName}")
    public ApiResponse deleteTag(@PathVariable String tagName) {
        try {
            tagService.deleteByName(tagName);
            return ApiResponse.success("删除标签成功!");
        } catch (RecordNotFoundException e) {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，删除标签失败");
        }
    }





}
