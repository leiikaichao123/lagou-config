package com.lagou.service.impl;

import com.lagou.dao.RoleMapper;
import com.lagou.domain.*;
import com.lagou.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;


    @Override
    public List<Role> findAllRole(Role role) {
        List<Role> allRole = roleMapper.findAllRole(role);
        return allRole;
    }

    @Override
    public List<Integer> findMenuByRoleId(Integer roleid) {
        List<Integer> menuByRoleId = roleMapper.findMenuByRoleId(roleid);

        return menuByRoleId;
    }

    @Override
    public void roleContextMenu(RoleMenuVo roleMenuVo) {

        //1. 清空中间表的关联关系
        roleMapper.deleteRoleContextMenu(roleMenuVo.getRoleId());

        //2. 为角色分配菜单

        for (Integer mid : roleMenuVo.getMenuIdList()) {

            Role_menu_relation role_menu_relation = new Role_menu_relation();
            role_menu_relation.setMenuId(mid);
            role_menu_relation.setRoleId(roleMenuVo.getRoleId());

            //封装数据
            Date date = new Date();
            role_menu_relation.setCreatedTime(date);
            role_menu_relation.setUpdatedTime(date);

            role_menu_relation.setCreatedBy("system");
            role_menu_relation.setUpdatedby("system");


            roleMapper.roleContextMenu(role_menu_relation);
        }

    }

    @Override
    public void deleteRole(Integer roleid) {

        // 调用根据roleid清空中间表关联关系
        roleMapper.deleteRoleContextMenu(roleid);

        roleMapper.deleteRole(roleid);
    }


    //根据角色id查询资源分类和资源信息
    @Override
    public List<ResourceCategory> findResourceListByRoleId(Integer roleid) {

        List<Resource> resourceByRoleId = roleMapper.findResourceByRoleId(roleid);

        List<ResourceCategory> resourceCategoryByRoleId = roleMapper.findResourceCategoryByRoleId(roleid);

        //将查询到的资源信息封装到资源分类的list集合里
        for (ResourceCategory resourceCategory:resourceCategoryByRoleId){
            resourceCategory.setResourceList(resourceByRoleId);
        }
        return resourceCategoryByRoleId;
    }



    @Override
    public void roleContextResource(RoleResourceVo roleResourceVo) {

        //1. 清空中间表的关联关系
        roleMapper.deleteRoleContextResource(roleResourceVo.getRoleId());

        //2. 为角色分配资源

        //把资源id遍历进list集合
        for (Integer reid : roleResourceVo.getResourceIdList()) {

            //根据将角色id和资源id封装
            RoleResourceRelation roleResourceRelation = new RoleResourceRelation();
            roleResourceRelation.setResourceId(reid);
            roleResourceRelation.setRoleId(roleResourceVo.getRoleId());

            //补充封装数据
            Date date = new Date();
            roleResourceRelation.setCreatedTime(date);
            roleResourceRelation.setUpdatedTime(date);

            roleResourceRelation.setCreatedBy("system");
            roleResourceRelation.setUpdatedby("system");
            roleMapper.roleContextResource(roleResourceRelation);
        }

    }

}
