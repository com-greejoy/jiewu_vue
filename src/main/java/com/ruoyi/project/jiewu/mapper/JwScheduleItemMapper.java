package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwScheduleItem;
import org.apache.ibatis.annotations.Param;

public interface JwScheduleItemMapper {

    public JwScheduleItem selectJwScheduleItemById(Long id);

    public List<JwScheduleItem> selectJwScheduleItemByGameItemId(@Param("gameItemId") Long gameItemId);

    public List<JwScheduleItem> listNoPlaceJwScheduleItem(Long matchId);

    public List<JwScheduleItem> selectJwScheduleItemList(JwScheduleItem jwScheduleItem);

    public int insertJwScheduleItem(JwScheduleItem jwScheduleItem);

    public int updateJwScheduleItem(JwScheduleItem jwScheduleItem);

    public int clearSchedulePlace(@Param("schedulePlaceId") Long schedulePlaceId);

    public int clearSchedulePlaceById(@Param("id") Long id);

    // 锁定一个项目打分
    public int lockScoreByGameItem(Long id);

    public int deleteJwScheduleItemByGameItem(@Param("gameItemId") Long gameItemId);

    public int updateJwScheduleItemPlace(@Param("ids") Long[] ids, @Param("scheduleInfoId") Long scheduleInfoId, @Param("schedulePlaceId") Long schedulePlaceId);

    public int deleteJwScheduleItemById(Long id);

    public int clearMatchScheduleItem(Long matchId);

    public int deleteJwScheduleItemByIds(Long[] ids);

    // 修改项目名字
    public int changeItemName(@Param("gameItemId")Long gameItemId, @Param("oldName")String oldName, @Param("newName")String newName);
}
