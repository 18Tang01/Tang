package com.dao;

import com.entity.QicaiCollectionEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.view.QicaiCollectionView;

/**
 * 器材收藏 Dao 接口
 *
 * @author 
 */
public interface QicaiCollectionDao extends BaseMapper<QicaiCollectionEntity> {

   List<QicaiCollectionView> selectListView(Pagination page,@Param("params")Map<String,Object> params);

}
