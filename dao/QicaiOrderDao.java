package com.dao;

import com.entity.QicaiOrderEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.view.QicaiOrderView;

/**
 * 器材租赁 Dao 接口
 *
 * @author 
 */
public interface QicaiOrderDao extends BaseMapper<QicaiOrderEntity> {

   List<QicaiOrderView> selectListView(Pagination page,@Param("params")Map<String,Object> params);

}
