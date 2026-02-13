package com.ruoyi.project.jiewu.service;

import com.ruoyi.common.utils.BigDecimalUtil;
import com.ruoyi.project.jiewu.domain.*;
import com.ruoyi.project.jiewu.mapper.JwMatchMapper;
import com.ruoyi.project.jiewu.mapper.JwTeamMapper;
import org.apache.ibatis.annotations.Param;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class JwMatchService {

    @Autowired
    private JwMatchMapper jwMatchMapper;

    @Autowired
    private JwTeamMapper jwTeamMapper;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwTeamService jwTeamService;

    public JwMatch selectJwMatchById(Long id) {
        return jwMatchMapper.selectJwMatchById(id);
    }

    // 获取没开始的比赛和正在报名的比赛
    public List<JwMatch> selectFutureIngList() {
        return jwMatchMapper.selectFutureIngList();
    }

    // 获取微信用户管理的比赛
    public List<JwMatch> listWxUserMatchList(Long userId) {
        return jwMatchMapper.listWxUserMatchList(userId);
    }

    // 获取比赛的报名统计数据
    public JwMatchSignData getMatchSignInfo(Long matchId) {
        JwMatchSignData result = new JwMatchSignData();
        JwTeam queryTeam = new JwTeam();
        queryTeam.setMatchId(matchId);
        List<JwTeam> list = jwTeamMapper.selectJwTeamListWithMatch(queryTeam);
        if (list == null) list = new ArrayList<>();
        result.setMatchId(matchId);
        result.setTeamCount(list.size());
        Long signCount = 0l;
        Long signSportCount = 0l;
        BigDecimal allFee = new BigDecimal("0");

        for (JwTeam jwTeam : list) {
            List<JwSignRecord> jwSignRecords = jwSignRecordService.selectJwSignRecordListWithUserMatch(jwTeam.getId(), matchId);

            if (jwSignRecords != null && jwSignRecords.size() > 0) {
                signCount += jwSignRecords.size();
                for (JwSignRecord jwSignRecord : jwSignRecords) {
                    JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(jwSignRecord.getGameItemId());
                    jwSignRecord.setJwGameItem(jwGameItem);
                    if ("1".equals(jwGameItem.getSportLimit())) {
                        //  单人
                        signSportCount++;
//                        if (BigDecimalUtil.isNotNull(jwGameItem.getFee())) {
//                            allFee = allFee.add(jwGameItem.getFee());
//                        }

                    } else if ("2".equals(jwGameItem.getSportLimit()) || "3".equals(jwGameItem.getSportLimit()) || "4".equals(jwGameItem.getSportLimit())) {
                        // 多人
                        if (jwSignRecord.getJwSignRecordSportList() != null && jwSignRecord.getJwSignRecordSportList().size() > 0) {
                            int sportCount = jwSignRecord.getJwSignRecordSportList().size();
                            signSportCount += sportCount;
//                            BigDecimal fee = jwGameItem.getFee();
//                            // 如果报名人数超过规定人数, 就重新计算价格   价格 / 规定人数 * 实际人数
//                            if (sportCount > jwGameItem.getFeeMaxSport() && BigDecimalUtil.isNotNull(fee)) {
//                                fee = fee.multiply(new BigDecimal(sportCount)).divide(new BigDecimal(jwGameItem.getFeeMaxSport()), 0, RoundingMode.DOWN);
//                            }
//                            if (BigDecimalUtil.isNotNull(fee)) {
//                                allFee = allFee.add(fee);
//                            }
                        }
                    }
                    allFee = allFee.add(jwTeamService.getSignRecordFee(jwGameItem, jwSignRecord));
                }
            }
        }
        result.setAllFee(allFee);
        result.setSignCount(signCount);
        result.setSignSportCount(signSportCount);

        return result;
    }

    public List<JwMatch> selectJwMatchList(JwMatch jwMatch) {
        return jwMatchMapper.selectJwMatchList(jwMatch);
    }

    public int insertJwMatch(JwMatch jwMatch) {
        return jwMatchMapper.insertJwMatch(jwMatch);
    }

    public int updateJwMatch(JwMatch jwMatch) {
        return jwMatchMapper.updateJwMatch(jwMatch);
    }

    public int updateJwMatchInvitationList(Long matchId, String invitationList){
        return jwMatchMapper.updateJwMatchInvitationList(matchId, invitationList);
    }

    public int updateJwMatchLastBackNum(Long matchId, Long lastBackNum){
        return jwMatchMapper.updateJwMatchLastBackNum(matchId, lastBackNum);
    }

    @Transactional
    public int deleteJwMatchByIds(Long[] ids) {
        // 删除比赛
        if(ids != null && ids.length > 0){
            for(Long id : ids){
                deleteJwMatchById(id);
            }
        }
        return 1;
    }

    public int deleteJwMatchById(Long id) {
        return jwMatchMapper.deleteJwMatchById(id);
    }
}