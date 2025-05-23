package com.dao;

import com.entity.ChangdiWeihuEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.view.ChangdiWeihuView;

/**
 * 场地维护 Dao 接口
 *
 * @author 
 */
public interface ChangdiWeihuDao extends BaseMapper<ChangdiWeihuEntity> {

   List<ChangdiWeihuView> selectListView(Pagination page,@Param("params")Map<String,Object> params);

}
