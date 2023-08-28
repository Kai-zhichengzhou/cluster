package com.cluster.controller;


import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.Event;
import com.cluster.pojo.User;
import com.cluster.service.ClusterService;
import com.cluster.service.EventService;
import com.cluster.service.FileStorageService;
import com.cluster.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ClusterService clusterService;
    @Autowired
    private EventService eventService;

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${file.storage-location}")
    private String location;


    @ApiOperation(value = "返回所有用户")
    @GetMapping("/")
    public List<User> getAllUsers()
    {

        List<User> users =  userService.getAllUsers();
        users.forEach( user ->{
            user.setAvatarPath(userService.getFullAvatarUrl(user.getAvatarPath()));
        });
        return users;
    }


    @ApiOperation(value = "上传头像")
    @PostMapping("/upload")
    public ApiResponse uploadAvatar(@RequestParam("file") MultipartFile file)
    {
        try
        {
            //存储文件并获取新的文件名
            String fileName =fileStorageService.storeFile(file, location);
            //获取当前用户
            Integer userId = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            userService.uploadAvatar(userId, fileName);
            return ApiResponse.success("头像更新成功");
        }catch(Exception e)
        {
            e.printStackTrace();
            return ApiResponse.error("头像上传失败");
        }
    }


    @ApiOperation(value = "更改用户自己的个人文档")
    @PutMapping("/updateProfile")
    public ApiResponse updateUserProfile(@RequestBody User user)
    {
        try
        {
            userService.updateUser(user.getId(), user);
            return ApiResponse.success("更改信息成功!");
        }catch(IllegalArgumentException e)
        {
            e.printStackTrace();
            return ApiResponse.error("当前更新的信息不能为空");
        }catch(AccessDeniedException e)
        {
            e.printStackTrace();
            return ApiResponse.error("你无权修改他人的信息!");
        }

    }


    @ApiOperation(value = "查看自己的个人文档")
    @GetMapping("/myInfo")
    public ApiResponse viewMyProfile()
    {
        User user = userService.getCurrentUser();
        user.setAvatarPath(userService.getFullAvatarUrl(user.getAvatarPath()));
        return ApiResponse.success("已显示个人信息",user);
    }

    @ApiOperation(value = "查看已加入的cluster")
    @GetMapping("/clusters")
    public ApiResponse viewMyClusters(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize)
    {
        return ApiResponse.success("已获取你加入的所有cluster", clusterService.viewMyClusters(page, pageSize));
    }

    @ApiOperation(value = "查看自己加入过的事件")
    @GetMapping("/events")
    public ApiResponse viewMyEvents()
    {
        return ApiResponse.success("已获取你参与的所有Events",eventService.viewMyEvents());
    }


    @ApiOperation(value = "获取当前页的所有用户")
    @GetMapping(value = "/page")
    public ApiResponse getUserByPage(@RequestParam("page")Integer page)
    {
        int pageSize = 8;

        int offset = (page - 1) * pageSize;

        List<User> users= userService.getUserByPage(page, pageSize).getList();
        users.forEach( user ->{
            user.setAvatarPath(userService.getFullAvatarUrl(user.getAvatarPath()));
        });

        return ApiResponse.success("获取当前页的clusters成功", users);

    }
}
