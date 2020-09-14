package com.ydc.basepack.util;

import com.ydc.basepack.model.dto.CategoryTree;
import com.ydc.basepack.model.entity.Category;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 此种算法待优化
 * 思路：首先将parentId=0的category加入结果集trees,然后针对每个category遍历，得到parentId=x的，setChildren
 *
 *
 * @author: ydc
 * @date: 2018/10/17 13:38
 * @description: 树形工具类
 */
public class TreeUtil {

    /**
     * 数组转树形结构
     * @param categories
     * @param root
     * @return
     */
    public static List<CategoryTree> list2Tree(List<Category> categories, Long root){
        // 普通对象转树节点
        List<CategoryTree> resourceList = buildTree(categories);
        List<CategoryTree> trees = new ArrayList<>();
        for (CategoryTree tree: resourceList) {
            if( root.equals(tree.getParentId())) {
                trees.add(tree);
            }

            for (CategoryTree t : resourceList) {
                if (tree.getId().equals(t.getParentId())) {
                    if (tree.getChildren() == null) {
                        tree.setChildren(new ArrayList<>());
                    }
                    tree.addChildren(t);
                }
            }
        }
        return trees;
    }


    public static List<CategoryTree> list2Tree2(List<Category> categories, Integer root){
        // 普通对象转树节点
        List<CategoryTree> resourceList = buildTree(categories);

        //结果lsit
        List<CategoryTree> trees = new ArrayList<>();
        for (CategoryTree tree: resourceList) {
            if( root.equals(tree.getId())) {
                trees.add(tree);
            }





        }
        return trees;
    }


    /**
     * 对象转树节点
     * @param sysResources
     * @return
     */
    public static List<CategoryTree> buildTree(List<Category> sysResources){
        List<CategoryTree> trees = new ArrayList<>();
        sysResources.forEach( resource->{
            CategoryTree tree = new CategoryTree();
            BeanUtils.copyProperties(resource, tree);
            trees.add(tree);
        });
        return trees;
    }
}
