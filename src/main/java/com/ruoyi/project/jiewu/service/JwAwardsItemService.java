package com.ruoyi.project.jiewu.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwAwardsItemMapper;
import com.ruoyi.project.jiewu.domain.JwAwardsItem;

@Service
public class JwAwardsItemService {

    @Autowired
    private JwAwardsItemMapper jwAwardsItemMapper;

    public JwAwardsItem selectJwAwardsItemById(Long id) {
        return jwAwardsItemMapper.selectJwAwardsItemById(id);
    }

    public List<JwAwardsItem> selectJwAwardsItemListByIds(String[] ids) {
        return jwAwardsItemMapper.selectJwAwardsItemListByIds(ids);
    }

    public List<JwAwardsItem> selectJwAwardsItemList(JwAwardsItem jwAwardsItem) {
        return jwAwardsItemMapper.selectJwAwardsItemList(jwAwardsItem);
    }

    public int insertJwAwardsItem(JwAwardsItem jwAwardsItem) {
        return jwAwardsItemMapper.insertJwAwardsItem(jwAwardsItem);
    }

    public int updateJwAwardsItem(JwAwardsItem jwAwardsItem) {
        return jwAwardsItemMapper.updateJwAwardsItem(jwAwardsItem);
    }

    public int deleteJwAwardsItemByIds(Long[] ids) {
        return jwAwardsItemMapper.deleteJwAwardsItemByIds(ids);
    }

    public int deleteJwAwardsItemById(Long id) {
        return jwAwardsItemMapper.deleteJwAwardsItemById(id);
    }
}
