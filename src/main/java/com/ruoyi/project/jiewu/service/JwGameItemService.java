package com.ruoyi.project.jiewu.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwGameItemMapper;
import com.ruoyi.project.jiewu.domain.JwGameItem;

/**
 * 比赛项目Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
@Service
public class JwGameItemService {

    @Autowired
    private JwGameItemMapper jwGameItemMapper;

    @Autowired
    private JwSignRecordService jwSignRecordService;


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


    /**
     * 新增比赛项目
     * 
     * @param jwGameItem 比赛项目
     * @return 结果
     */
    public int insertJwGameItem(JwGameItem jwGameItem) {
        jwGameItem.setCreateTime(DateUtils.getNowDate());
        return jwGameItemMapper.insertJwGameItem(jwGameItem);
    }

    /**
     * 修改比赛项目
     * 
     * @param jwGameItem 比赛项目
     * @return 结果
     */
    public int updateJwGameItem(JwGameItem jwGameItem) {
        jwGameItem.setUpdateTime(DateUtils.getNowDate());
        return jwGameItemMapper.updateJwGameItem(jwGameItem);
    }

    /**
     * 批量删除比赛项目
     * 
     * @param ids 需要删除的比赛项目主键
     * @return 结果
     */
    public int deleteJwGameItemByIds(Long[] ids) {
        return jwGameItemMapper.deleteJwGameItemByIds(ids);
    }

    /**
     * 删除比赛项目信息
     * 
     * @param id 比赛项目主键
     * @return 结果
     */
    public int deleteJwGameItemById(Long id) {
        return jwGameItemMapper.deleteJwGameItemById(id);
    }
}
