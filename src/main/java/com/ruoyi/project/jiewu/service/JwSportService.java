package com.ruoyi.project.jiewu.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.ruoyi.common.utils.IDCardUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.jiewu.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwSportMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwSportService {

    @Autowired
    private JwSportMapper jwSportMapper;

    @Autowired
    private JwTeamService jwTeamService;

    @Autowired
    private JwSignRecordSportService jwSignRecordSportService;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    public JwSport selectJwSportById(Long id) {
        return jwSportMapper.selectJwSportById(id);
    }

    public List<JwSport> selectJwSportList(JwSport jwSport) {
        return jwSportMapper.selectJwSportList(jwSport);
    }

    public List<JwSport> selectJwSportByUserId(Long createUserId, String searchName) {
        return jwSportMapper.selectJwSportByUserId(createUserId, searchName);
    }

    public List<JwSport> selectTodayBirthSport(String today){
        return jwSportMapper.selectTodayBirthSport(today);
    }

    public JwSport selectJwSportByIdCard(String idCard, Long id, Long createUserId) {
        return jwSportMapper.selectJwSportByIdCard(idCard, id, createUserId);
    }

    public JwSport selectJwSportByName(String playerName, String idCard, Long createUserId) {
        return jwSportMapper.selectJwSportByName(playerName, idCard, createUserId);
    }

    //根据报名记录获取报名运动员数据
    public List<JwSport> getSportListWithsignRecord(Long signRecordId) {
        return jwSportMapper.getSportListWithsignRecord(signRecordId);
    }

    // 获取所有运动员的报名项目
    public List<JwSport> selectJwSignRecordSportGameItemList(Long matchId, Long teamId, Long createAddId) {
        return jwSportMapper.selectJwSignRecordSportGameItemList(matchId, teamId, createAddId);
    }

    // 获取所有运动员的报名项目
//    public List<JwSport> selectMatchGameItemSportList(Long matchId, Long teamId) {
//        return jwSportMapper.selectMatchGameItemSportList(matchId, teamId);
//    }


    public List<JwSport> getWxSportListWithGameItem(Long createUserId, Long gameItemId, Long minAge, Long maxAge, String sex){
        return jwSportMapper.getWxSportListWithGameItem(createUserId, gameItemId, minAge, maxAge, sex);
    }

    // 获取参赛的人员名单
    public List<JwSport> getWxSportListByMatchTeam(JwSport jwSport){
        return jwSportMapper.getWxSportListByMatchTeam(jwSport);
    }


    public int insertJwSport(JwSport jwSport) {
        jwSport.setSex(IDCardUtils.getGender(jwSport.getIdCard()));
        jwSport.setAge(IDCardUtils.getAge(jwSport.getIdCard()));
        return jwSportMapper.insertJwSport(jwSport);
    }

    public int updateJwSport(JwSport jwSport) {
        if(StringUtils.isNotEmpty(jwSport.getIdCard())){
            jwSport.setSex(IDCardUtils.getGender(jwSport.getIdCard()));
            jwSport.setAge(IDCardUtils.getAge(jwSport.getIdCard()));
        }
        return jwSportMapper.updateJwSport(jwSport);
    }

    // 修改选手的队伍
    @Transactional
    public int changeTeam(Long[] sportIds, Long newTeamId){
        if(sportIds != null && sportIds.length > 0 && newTeamId != null && newTeamId != 0l){

            JwTeam jwTeam = jwTeamService.selectJwTeamById(newTeamId);
            if(jwTeam != null){
                Arrays.stream(sportIds).forEach(sportId->{
                    JwSport jwSport = selectJwSportById(sportId);
                    if(jwSport != null){
                        // 更新选手表的 createUserId
                        JwSport updateSport = new JwSport();
                        updateSport.setId(jwSport.getId());
                        updateSport.setCreateUserId(jwTeam.getCreateUserId());
                        updateJwSport(updateSport);

                        // 更新报名记录选手表的 teamId
                        List<JwSignRecordSport> jwSignRecordSportList = jwSignRecordSportService.selectJwSignRecordSportBySportId(sportId);

                        if(jwSignRecordSportList != null && jwSignRecordSportList.size() > 0){
                            jwSignRecordSportList.forEach(jwSignRecordSport -> {
                                JwSignRecordSport updateSignSport = new JwSignRecordSport();
                                updateSignSport.setId(jwSignRecordSport.getId());
                                updateSignSport.setTeamId(newTeamId);
                                jwSignRecordSportService.updateJwSignRecordSport(updateSignSport);
                            });

                            Long[] signRecordIds = jwSignRecordSportList.stream()
                                    .map(JwSignRecordSport::getSignRecordId)
                                    .filter(Objects::nonNull)
                                    .distinct()
                                    .toArray(Long[]::new);

                            // 更新报名记录的 teamId
                            jwSignRecordService.changeTeam(signRecordIds, newTeamId);
                        }
                    }
                });
            }
        }
        return 1;
    }

    public int updateJwSportByName(JwSport jwSport) {
        return jwSportMapper.updateJwSportByName(jwSport);
    }

    public int deleteJwSportByIds(Long[] ids) {
        return jwSportMapper.deleteJwSportByIds(ids);
    }

    public int deleteJwSportById(Long id) {
        return jwSportMapper.deleteJwSportById(id);
    }
}