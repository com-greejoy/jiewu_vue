package com.ruoyi.project.jiewu.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.DataSource;
import com.ruoyi.framework.aspectj.lang.enums.DataSourceType;
import com.ruoyi.project.jiewu.domain.JwEight;
import com.ruoyi.project.jiewu.domain.JwHaiScore;
import com.ruoyi.project.jiewu.domain.JwSignRecord;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwGameItemMapper;
import com.ruoyi.project.jiewu.domain.JwGameItem;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwGameItemService {

    @Autowired
    private JwGameItemMapper jwGameItemMapper;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwScheduleItemService jwScheduleItemService;

    @Autowired
    private JwEightService jwEightService;

    @Autowired
    private JwHaiScoreService jwHaiScoreService;

    @Autowired
    private JwEightScoreService jwEightScoreService;

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

    public List<JwGameItem> selectJwGameItemListByMatchId(Long matchId, Long gameItemId) {
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
        if (StringUtils.isNotEmpty(oldGameItem.getName()) && !oldGameItem.getName().equals(jwGameItem.getName()) && StringUtils.isNotEmpty(jwGameItem.getName())) {
            jwScheduleItemService.changeItemName(jwGameItem.getId(), oldGameItem.getName(), jwGameItem.getName());
        }
        jwGameItem.setUpdateTime(DateUtils.getNowDate());
        return jwGameItemMapper.updateJwGameItem(jwGameItem);
    }

    // 同步一个组别的成绩

    public int uploadGameItemGrade(JwSignRecord jwSignRecord) {
        // 同步海选成绩 可以通过 gameItemId 和 backNum 确定一条记录
        // 同步报名记录的成绩
        List<JwSignRecord> jwSignRecordList = jwSignRecordService.selectJwSignRecordList(jwSignRecord);
        // 同步海选打分成绩
        List<JwHaiScore> jwHaiScoreList = jwHaiScoreService.selectJwHaiScoreListByGameItem(jwSignRecord.getGameItemId());

        // 同步对阵表数据
        JwEight query = new JwEight();
        query.setGameItemId(jwSignRecord.getGameItemId());
        List<JwEight> jwEightList = jwEightService.selectJwEightList(query);

        doUploadGameItemGrade(jwSignRecord, jwSignRecordList, jwEightList);
        return 1;
    }

    @Transactional
    @DataSource(value = DataSourceType.SLAVE)
    public void doUploadGameItemGrade(JwSignRecord jwSignRecord, List<JwSignRecord> jwSignRecordList, List<JwEight> jwEightList) {
        // 同步报名记录的成绩
        if (jwSignRecordList != null && jwSignRecordList.size() > 0) {
            jwSignRecordService.clearScoreByGameItemUp(jwSignRecord.getGameItemId());
            jwSignRecordList.forEach(jwSignRecord1 -> {
                JwSignRecord jwSignRecordUpLoad = new JwSignRecord();
                jwSignRecordUpLoad.setId(jwSignRecord1.getId());
                jwSignRecordUpLoad.setBackNumber(jwSignRecord1.getBackNumber());
                jwSignRecordUpLoad.setAvgScore(jwSignRecord1.getAvgScore());
                jwSignRecordUpLoad.setAllScore(jwSignRecord1.getAllScore());
                jwSignRecordUpLoad.setRankOrder(jwSignRecord1.getRankOrder());
                int re = jwSignRecordService.updateJwSignRecordUpScore(jwSignRecordUpLoad);
                if (re <= 0) {
                    throw new GlobalException(jwSignRecord1.getBackNumber() + "：同步失败");
                }
            });
        }

        // 同步海选打分成绩

        // 同步对阵表数据
        jwEightService.deleteAllJwEightUp(jwSignRecord.getGameItemId());
        if (jwEightList != null && jwEightList.size() > 0) {
            jwEightList.forEach(jwEight -> {
                jwEight.setId(null);
                int re = jwEightService.insertJwEightUp(jwEight);
                if (re <= 0) {
                    throw new GlobalException("对阵数据：同步失败");
                }
            });
        }

        // 同步决赛打分成绩


    }


    // 根据项目名获取小项
    public JwGameItem selectJwGameItemByName(String name, Long matchId) {
        return jwGameItemMapper.selectJwGameItemByName(name, matchId);
    }

    public JwGameItem selectJwGameItemByCode(String code, Long matchId) {
        return jwGameItemMapper.selectJwGameItemByCode(code, matchId);
    }

    public int deleteJwGameItemByIds(Long[] ids) {
        return jwGameItemMapper.deleteJwGameItemByIds(ids);
    }

    public int deleteJwGameItemById(Long id) {
        return jwGameItemMapper.deleteJwGameItemById(id);
    }
}