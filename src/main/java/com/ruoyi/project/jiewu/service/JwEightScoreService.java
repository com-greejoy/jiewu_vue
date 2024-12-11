package com.ruoyi.project.jiewu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.jiewu.domain.JwScheduleItem;
import com.ruoyi.project.jiewu.domain.JwSignRecord;
import com.ruoyi.project.jiewu.mapper.JwEightMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwEightScoreMapper;
import com.ruoyi.project.jiewu.domain.JwEightScore;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
public class JwEightScoreService {

    @Autowired
    private JwEightScoreMapper jwEightScoreMapper;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwScheduleItemService jwScheduleItemService;

    @Autowired
    private JwEightService jwEightService;

    @Autowired
    private JwEightMapper jwEightMapper;

    public JwEightScore selectJwEightScoreById(Long id) {
        return jwEightScoreMapper.selectJwEightScoreById(id);
    }

    public List<JwEightScore> selectJwEightScoreList(JwEightScore jwEightScore) {
        return jwEightScoreMapper.selectJwEightScoreList(jwEightScore);
    }

    @Transactional
    public synchronized int saveEightScore(String judgeId, String currentPkGroup, Long jinJiId, Long lun) {
        if(!StringUtils.isLongNotNull(lun)){
            lun = 1l;
        }

        JwSignRecord jwSignRecord = jwSignRecordService.selectJwSignRecordById(jinJiId);
        Long gameItemId = jwSignRecord.getGameItemId();
        int row = 0;
        JwEightScore oldJwScore = jwEightScoreMapper.selectJwEightScore(judgeId, currentPkGroup, gameItemId, lun);

        JwEightScore jwScore = new JwEightScore();

        jwScore.setJudgeId(Long.valueOf(judgeId));
        jwScore.setPlayerPkGroup(currentPkGroup);
        jwScore.setPlayerId(jinJiId);
        jwScore.setPlayerGroup(gameItemId);
        jwScore.setCreateTime(DateUtils.getNowDate());
        jwScore.setLun(lun);
//        jwScore.setJudgeNum(judgeNum);

        List<JwEightScore> jwEightScoreList;

        if (oldJwScore != null) {
            jwScore.setId(oldJwScore.getId());
            row = jwEightScoreMapper.updateJwEightScore(jwScore);
        } else {
            jwEightScoreList = jwEightScoreMapper.selectJwEightScoreListByPkGroup(currentPkGroup, gameItemId);
//            if(jwEightScoreList != null && jwEightScoreList.size() >= judgeNum){
//                return 10;
//            }else{
            row = jwEightScoreMapper.insertJwEightScore(jwScore);
//            }
        }
//
        // 计算排名
//        jwEightScoreList = jwEightScoreMapper.selectJwEightScoreListByPkGroup(currentPkGroup, gameItemId);
//        String[] ps = currentPkGroup.split("\\.");
//        if (jwEightScoreList != null && jwEightScoreList.size() >= 1) {
//            Long s1 = null;
//            Long s2 = null;
//            List<JwEightScore> js1 = new ArrayList<>(), js2 = new ArrayList<>();
//
//            Map<Long, List<JwEightScore>> groupBy = jwEightScoreList.stream().collect(Collectors.groupingBy(JwEightScore::getPlayerId));
//
//            if (groupBy != null && groupBy.size() == 2) {
//                for (Map.Entry<Long, List<JwEightScore>> entry : groupBy.entrySet()) {
//
//                    if (s2 == null || s2 == 0l) {
//                        s2 = entry.getKey();
//                        js2 = entry.getValue();
//                    } else {
//                        s1 = entry.getKey();
//                        js1 = entry.getValue();
//                    }
//                }
//                if (js1.size() == js2.size()) {
//                    // 出现 平分
////                    jwEightScoreMapper.updateEqually(currentPkGroup, playerGroup);
//                } else if (js1.size() > js2.size()) {
//                    // Long gameItemId, Long playerId, String playerPosition, String playerIndex
//                    jwEightService.saveEightPro(gameItemId, js1.get(0).getPlayerId(), ps[0], ps[1]);
//                    completeEightOrder(currentPkGroup, gameItemId);
//                } else if (js1.size() < js2.size()) {
//                    jwEightService.saveEightPro(gameItemId, js2.get(0).getPlayerId(), ps[0], ps[1]);
//                    completeEightOrder(currentPkGroup, gameItemId);
//                }
//            } else if (groupBy.size() == 1) {
//                // 全部裁判选同一个人
//                jwEightService.saveEightPro(gameItemId, jwEightScoreList.get(0).getPlayerId(), ps[0], ps[1]);
//                completeEightOrder(currentPkGroup, gameItemId);
//            }
//        }
        return row;
    }

    // 计算对阵排名
    public void jisuanDuiZhengPaiMing() {

    }

    private void completeEightOrder(String currentPkGroup, Long playerGroup) {
        if ("2.1".equals(currentPkGroup)) {
            jwEightMapper.setJiJunOne(currentPkGroup, playerGroup);
        }
        if ("2.2".equals(currentPkGroup)) {
            jwEightMapper.setJiJunTwo(currentPkGroup, playerGroup);
        }
        if ("2.0".equals(currentPkGroup)) {
            jwEightMapper.setJiJunOrder(currentPkGroup, playerGroup);
        }
        if ("1.1".equals(currentPkGroup)) {
            jwEightMapper.setYaJunOrder(currentPkGroup, playerGroup);
        }

//        if ("4.1".equals(currentPkGroup) || "4.2".equals(currentPkGroup) || "4.3".equals(currentPkGroup) || "4.4".equals(currentPkGroup)) {
//            if (jwEightMapper.getFourCount(playerGroup) == 0) {
//                jwEightMapper.setFourOrder(currentPkGroup, playerGroup);
//            }
//        }
//        jwEightScoreMapper.removeEqually(currentPkGroup, playerGroup);
    }


    public int insertJwEightScore(JwEightScore jwEightScore) {
        jwEightScore.setCreateTime(DateUtils.getNowDate());
        return jwEightScoreMapper.insertJwEightScore(jwEightScore);
    }

    public int updateJwEightScore(JwEightScore jwEightScore) {
        return jwEightScoreMapper.updateJwEightScore(jwEightScore);
    }

    public int deleteJwEightScoreByIds(Long[] ids) {
        return jwEightScoreMapper.deleteJwEightScoreByIds(ids);
    }

    public int deleteJwEightScoreById(Long id) {
        return jwEightScoreMapper.deleteJwEightScoreById(id);
    }
}
