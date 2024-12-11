package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwGameItemAwardItem;

public interface JwGameItemAwardItemMapper {

    public JwGameItemAwardItem selectJwGameItemAwardItemByGameItemId(Long gameItemId);

    public List<JwGameItemAwardItem> selectJwGameItemAwardItemList(JwGameItemAwardItem jwGameItemAwardItem);

    public int insertJwGameItemAwardItem(JwGameItemAwardItem jwGameItemAwardItem);

    public int updateJwGameItemAwardItem(JwGameItemAwardItem jwGameItemAwardItem);

    public int deleteJwGameItemAwardItemByGameItemId(Long gameItemId);

    public int deleteJwGameItemAwardItemByGameItemIds(Long[] gameItemIds);
}
