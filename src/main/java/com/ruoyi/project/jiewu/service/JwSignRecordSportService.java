package com.ruoyi.project.jiewu.service;

import java.util.Arrays;
import java.util.List;

import com.ruoyi.project.jiewu.domain.JwSport;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwSignRecordSportMapper;
import com.ruoyi.project.jiewu.domain.JwSignRecordSport;
import org.springframework.transaction.annotation.Transactional;


@Service
public class JwSignRecordSportService {

    @Autowired
    private JwSignRecordSportMapper jwSignRecordSportMapper;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    public JwSignRecordSport selectJwSignRecordSportById(Long id) {
        return jwSignRecordSportMapper.selectJwSignRecordSportById(id);
    }

    public List<JwSignRecordSport> selectJwSignRecordSportList(JwSignRecordSport jwSignRecordSport) {
        return jwSignRecordSportMapper.selectJwSignRecordSportList(jwSignRecordSport);
    }

    // 获取报名记录的人员名单
    public List<JwSignRecordSport> selectJwSignRecordSportListById(Long signRecordId) {
        return jwSignRecordSportMapper.selectJwSignRecordSportListById(signRecordId);
    }

    // 获取一个选手的报名记录
    public List<JwSignRecordSport> selectJwSignRecordSportBySportId(Long sportId) {
        return jwSignRecordSportMapper.selectJwSignRecordSportBySportId(sportId);
    }


    @Transactional
    public int insertJwSignRecordSport(JwSignRecordSport jwSignRecordSport) {
        if(jwSignRecordSport != null && jwSignRecordSport.getSportIds() != null && jwSignRecordSport.getSportIds().length > 0){
            Arrays.stream(jwSignRecordSport.getSportIds()).forEach(sportId -> {
                JwSignRecordSport JwSignRecordSportNew = new JwSignRecordSport();
                JwSignRecordSportNew.setMatchId(jwSignRecordSport.getMatchId());
                JwSignRecordSportNew.setTeamId(jwSignRecordSport.getTeamId());
                JwSignRecordSportNew.setGameItemId(jwSignRecordSport.getGameItemId());
                JwSignRecordSportNew.setSignRecordId(jwSignRecordSport.getSignRecordId());
                JwSignRecordSportNew.setSportId(sportId);
                jwSignRecordSportMapper.insertJwSignRecordSport(JwSignRecordSportNew);
            });
            return 1;
        }else{
            return jwSignRecordSportMapper.insertJwSignRecordSport(jwSignRecordSport);
        }

    }

    public int updateJwSignRecordSport(JwSignRecordSport jwSignRecordSport) {
        return jwSignRecordSportMapper.updateJwSignRecordSport(jwSignRecordSport);
    }

    public int deleteJwSignRecordSportByIds(Long[] ids) {
        return jwSignRecordSportMapper.deleteJwSignRecordSportByIds(ids);
    }

    @Transactional
    public int deleteJwSignRecordSportById(Long id) {
        JwSignRecordSport jwSignRecordSport = selectJwSignRecordSportById(id);
        Long signRecordId = jwSignRecordSport.getSignRecordId();
        int i = jwSignRecordSportMapper.deleteJwSignRecordSportById(id);

        // 齐舞得报名选手删完了, 就把报名记录一起删了
        List<JwSignRecordSport> jwSignRecordSportList = selectJwSignRecordSportListById(signRecordId);
        if(jwSignRecordSportList == null || jwSignRecordSportList.size() == 0){
            jwSignRecordService.deleteJwSignRecordById(signRecordId);
        }
        return i;
    }

    // 删除报名记录的选手列表
    public int deleteJwSignRecordSportBySignRecordId(Long signRecordId) {
        return jwSignRecordSportMapper.deleteJwSignRecordSportBySignRecordId(signRecordId);
    }
}
