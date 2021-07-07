package com.ssm.mall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.CategoryMapper;
import com.ssm.mall.dao.pojo.Category;
import com.ssm.mall.service.iservice.ICategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service("categoryService")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerRes addCategory(Integer parentId, String categoryName) {
        //1-保证相同父级目录下，不能出现重复子目录
        int flag = categoryMapper.checkNameInParentId(parentId, categoryName);
        if (flag > 0) {
            return ServerRes.error(Result.CATEGORY_CONFLICT_NAME_IN_SAME_PARENTID);
        }
        Category category = new Category();

        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(1);
        int insertFlag = categoryMapper.insertSelective(category);
        return insertFlag > 0 ? ServerRes.success(Result.RESULT_SUCCESS) : ServerRes.error(Result.RESULT_ERROR);
    }

    @Override
    public ServerRes updateCategory(Integer categoryId, String categoryName) {
        if(StringUtils.isBlank(categoryName)||categoryId==null){
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        Category category=new Category(categoryId,categoryName);
        int addFlag=categoryMapper.updateByPrimaryKeySelective(category);
        if(addFlag>0){
            return ServerRes.success(Result.CATEGORY_UPDATE_SUCCESS);
        }
        return ServerRes.error(Result.CATEGORY_UPDATE_ERROR);
    }

   @Override
    public ServerRes<List<Category>> getParallelCategories(Integer parentId) {
         if(parentId==null){
             return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
         }
         List<Category> childrenCategory=categoryMapper.selectByParentId(parentId);
         if(CollectionUtils.isEmpty(childrenCategory)){
             return ServerRes.error(Result.CATEGORY_NO_CHILDREN);
         }
         return ServerRes.success(Result.RESULT_SUCCESS,childrenCategory);
    }

    @Override
    public ServerRes<List<Category>> getDeepCategory(Integer categoryId) {
        if(categoryId == null){
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        Set<Category> categorySet = Sets.newHashSet();//调用guava工具中的Sets，创建HashSet对象
        recursionDeepCategory(categoryId,categorySet);//递归查询的节点都将放入categorySet中
        //JSON转换时，List集合更为遍历，因此此处要将Set集合转换为List集合
        List<Category> categoryList = Lists.newArrayList();//调用guava工具中的Lists，创建ArrayList对象
        for(Category item:categorySet){
            categoryList.add(item);
        }
        return ServerRes.success(Result.RESULT_SUCCESS,categoryList);
    }

/*    public void recursionDeepCategory(Integer categoryId, Set<Category> categorySet) {
        Category categoryNow=categoryMapper.selectByPrimaryKey(categoryId);
        if(categoryNow!=null){
            categorySet.add(categoryNow);
        }
        List<Category> childrens=getParallelCategories(categoryId).getData();
        for(Category child:childrens){
            recursionDeepCategory(child.getCategoryId(),categorySet);
        }

    }*/

    private Set<Category> recursionDeepCategory(Integer categoryId,Set<Category> categorySet){
        //1-在集合中添加当前节点
        Category categoryNow = categoryMapper.selectByPrimaryKey(categoryId);
        if(categoryNow != null){
            categorySet.add(categoryNow);
        }
        //2-把当前节点作为父节点，查询出下一级所有平级子节点
        //注意，此处调用了MyBatis的方法，默认不会返回null集合，因此后边的遍历不用加入非空判断
        List<Category> childrenCategoryList = categoryMapper.selectByParentId(categoryId);
        //3-递归查询，生成Set集合
        for(Category item:childrenCategoryList){
            recursionDeepCategory(item.getCategoryId(),categorySet);
        }
        return categorySet;

    }




}