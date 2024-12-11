package com.ruoyi.project.jiewu.service;

import java.util.List;

import com.ruoyi.common.utils.IDCardUtils;
import com.ruoyi.project.jiewu.domain.JwWxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwSportMapper;
import com.ruoyi.project.jiewu.domain.JwSport;

@Service
public class JwSportService {

    @Autowired
    private JwSportMapper jwSportMapper;

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

    public JwSport selectJwSportByIdCard(String idCard, Long id) {
        return jwSportMapper.selectJwSportByIdCard(idCard, id);
    }

    public JwSport selectJwSportByName(String playerName, String idCard, Long createUserId) {
        return jwSportMapper.selectJwSportByName(playerName, idCard, createUserId);
    }

    //根据报名记录获取报名运动员数据
    public List<JwSport> getSportListWithsignRecord(Long signRecordId) {
        return jwSportMapper.getSportListWithsignRecord(signRecordId);
    }

    // 获取所有运动员的报名项目
    public List<JwSport> selectJwSignRecordSportGameItemList(Long matchId, Long teamId) {
        return jwSportMapper.selectJwSignRecordSportGameItemList(matchId, teamId);
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
        jwSport.setSex(IDCardUtils.getGender(jwSport.getIdCard()));
        jwSport.setAge(IDCardUtils.getAge(jwSport.getIdCard()));
        return jwSportMapper.updateJwSport(jwSport);
    }

    public int deleteJwSportByIds(Long[] ids) {
        return jwSportMapper.deleteJwSportByIds(ids);
    }

    public int deleteJwSportById(Long id) {
        return jwSportMapper.deleteJwSportById(id);
    }
}
