package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwGameItem;
import org.apache.ibatis.annotations.Param;

public interface JwGameItemMapper {

    public JwGameItem selectJwGameItemById(Long id);

    public List<JwGameItem> selectJwGameItemList(JwGameItem jwGameItem);

    public List<JwGameItem> selectJwGameItemListWithCount(JwGameItem jwGameItem);

    public List<JwGameItem> selectJwGameItemListByMatchId(@Param("matchId") Long matchId, @Param("gameItemId") Long gameItemId);

    public int insertJwGameItem(JwGameItem jwGameItem);

    public int updateJwGameItem(JwGameItem jwGameItem);

    public JwGameItem selectJwGameItemByName(@Param("name") String name, @Param("matchId")Long matchId);

    public JwGameItem selectJwGameItemByCode(@Param("code") String code, @Param("matchId")Long matchId);

    public int deleteJwGameItemById(Long id);

    public int deleteJwGameItemByIds(Long[] ids);
}
