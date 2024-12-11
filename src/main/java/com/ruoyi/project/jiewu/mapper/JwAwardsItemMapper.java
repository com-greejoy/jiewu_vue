package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwAwardsItem;

public interface JwAwardsItemMapper {

    public JwAwardsItem selectJwAwardsItemById(Long id);

    public List<JwAwardsItem> selectJwAwardsItemList(JwAwardsItem jwAwardsItem);

    public List<JwAwardsItem> selectJwAwardsItemListByIds(String[] ids);

    public int insertJwAwardsItem(JwAwardsItem jwAwardsItem);

    public int updateJwAwardsItem(JwAwardsItem jwAwardsItem);

    public int deleteJwAwardsItemById(Long id);

    public int deleteJwAwardsItemByIds(Long[] ids);
}
