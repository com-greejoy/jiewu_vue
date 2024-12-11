package com.ruoyi.project.jiewu.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwGameItemMapper;
import com.ruoyi.project.jiewu.domain.JwGameItem;

@Service
public class JwGameItemService {

    @Autowired
    private JwGameItemMapper jwGameItemMapper;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwScheduleItemService jwScheduleItemService;


    public JwGameItem selectJwGameItemById(Long id) {
        return jwGameItemMapper.selectJwGameItemById(id);
    }

    public List<JwGameItem> selectJwGameItemList(JwGameItem jwGameItem) {
        return jwGameItemMapper.selectJwGameItemList(jwGameItem);
    }

    // 查询比赛项目数据 计算每组报名人数
    public List<JwGameItem> selectJwGameItemListWithCount(JwGameItem jwGameItem) {
        return jwGameItemMapper.selectJwGameItemListWithCount(jwGameItem);
    }

    public List<JwGameItem> selectJwGameItemListByMatchId(Long  matchId, Long gameItemId) {
        return jwGameItemMapper.selectJwGameItemListByMatchId(matchId, gameItemId);
    }

    // 获取代表队每个组别的报名数据
    public List<JwGameItem> selectJwGameItemListWithTeamSignRecordByMatchId(Long matchId, Long gameItemId, Long teamId, String backNumber) {

        List<JwGameItem> jwGameItemList = jwGameItemMapper.selectJwGameItemListByMatchId(matchId, gameItemId);

        jwGameItemList.forEach(jwGameItem -> {
            jwGameItem.setSportList(jwSignRecordService.selectJwSignRecordListWithUserGameItem(teamId, jwGameItem.getId(), backNumber));
        });
        jwGameItemList = jwGameItemList.stream().filter(jwGameItem -> jwGameItem.getSportList() != null && jwGameItem.getSportList().size() > 0).collect(Collectors.toList());
        return jwGameItemList;
    }

    public int insertJwGameItem(JwGameItem jwGameItem) {
        jwGameItem.setCreateTime(DateUtils.getNowDate());
        return jwGameItemMapper.insertJwGameItem(jwGameItem);
    }

    public int updateJwGameItem(JwGameItem jwGameItem) {
        JwGameItem oldGameItem = selectJwGameItemById(jwGameItem.getId());
        // 如果改了项目名字, 则对应修改 赛程小项名字
        if(!oldGameItem.getName().equals(jwGameItem.getName()) && StringUtils.isNotEmpty(jwGameItem.getName())){
            jwScheduleItemService.changeItemName(jwGameItem.getId(), oldGameItem.getName(), jwGameItem.getName());
        }
        jwGameItem.setUpdateTime(DateUtils.getNowDate());
        return jwGameItemMapper.updateJwGameItem(jwGameItem);
    }

    // 根据项目名获取小项
    public JwGameItem selectJwGameItemByName(String name, Long matchId) {
        return jwGameItemMapper.selectJwGameItemByName(name, matchId);
    }


    public int deleteJwGameItemByIds(Long[] ids) {
        return jwGameItemMapper.deleteJwGameItemByIds(ids);
    }

    public int deleteJwGameItemById(Long id) {
        return jwGameItemMapper.deleteJwGameItemById(id);
    }
}
