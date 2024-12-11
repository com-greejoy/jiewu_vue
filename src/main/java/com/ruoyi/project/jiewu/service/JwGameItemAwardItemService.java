package com.ruoyi.project.jiewu.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwGameItemAwardItemMapper;
import com.ruoyi.project.jiewu.domain.JwGameItemAwardItem;

@Service
public class JwGameItemAwardItemService {

    @Autowired
    private JwGameItemAwardItemMapper jwGameItemAwardItemMapper;

    public JwGameItemAwardItem selectJwGameItemAwardItemByGameItemId(Long gameItemId) {
        return jwGameItemAwardItemMapper.selectJwGameItemAwardItemByGameItemId(gameItemId);
    }

    public List<JwGameItemAwardItem> selectJwGameItemAwardItemList(JwGameItemAwardItem jwGameItemAwardItem) {
        return jwGameItemAwardItemMapper.selectJwGameItemAwardItemList(jwGameItemAwardItem);
    }

    public int insertJwGameItemAwardItem(JwGameItemAwardItem jwGameItemAwardItem) {
        return jwGameItemAwardItemMapper.insertJwGameItemAwardItem(jwGameItemAwardItem);
    }

    public int updateJwGameItemAwardItem(JwGameItemAwardItem jwGameItemAwardItem) {
        return jwGameItemAwardItemMapper.updateJwGameItemAwardItem(jwGameItemAwardItem);
    }

    public int deleteJwGameItemAwardItemByGameItemIds(Long[] gameItemIds) {
        return jwGameItemAwardItemMapper.deleteJwGameItemAwardItemByGameItemIds(gameItemIds);
    }

    public int deleteJwGameItemAwardItemByGameItemId(Long gameItemId) {
        return jwGameItemAwardItemMapper.deleteJwGameItemAwardItemByGameItemId(gameItemId);
    }
}
