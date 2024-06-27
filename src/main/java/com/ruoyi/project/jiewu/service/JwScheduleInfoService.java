package com.ruoyi.project.jiewu.service;

import java.util.List;

import com.ruoyi.project.jiewu.domain.JwSchedulePlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwScheduleInfoMapper;
import com.ruoyi.project.jiewu.domain.JwScheduleInfo;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwScheduleInfoService {

    @Autowired
    private JwScheduleInfoMapper jwScheduleInfoMapper;

    @Autowired
    private JwSchedulePlaceService jwSchedulePlaceService;

    public JwScheduleInfo selectJwScheduleInfoById(Long id) {
        return jwScheduleInfoMapper.selectJwScheduleInfoById(id);
    }

    public List<JwScheduleInfo> selectJwScheduleInfoList(JwScheduleInfo jwScheduleInfo) {
        return jwScheduleInfoMapper.selectJwScheduleInfoList(jwScheduleInfo);
    }

    public int insertJwScheduleInfo(JwScheduleInfo jwScheduleInfo) {
        return jwScheduleInfoMapper.insertJwScheduleInfo(jwScheduleInfo);
    }

    public int updateJwScheduleInfo(JwScheduleInfo jwScheduleInfo) {
        return jwScheduleInfoMapper.updateJwScheduleInfo(jwScheduleInfo);
    }

    public List<JwScheduleInfo> selectJwScheduleInfoByMatchId(Long matchId){
        return jwScheduleInfoMapper.selectJwScheduleInfoByMatchId(matchId);
    }

    public int deleteJwScheduleInfoByIds(Long[] ids) {
        return jwScheduleInfoMapper.deleteJwScheduleInfoByIds(ids);
    }

    @Transactional
    public int deleteJwScheduleInfoById(Long id) {

        // 先删除场次
        JwSchedulePlace query = new JwSchedulePlace();
        query.setScheduleInfoId(id);

        List<JwSchedulePlace> jwSchedulePlaceList = jwSchedulePlaceService.listJwSchedulePlaceWithScheduleItem(query);
        if(jwSchedulePlaceList != null && jwSchedulePlaceList.size() > 0){
            jwSchedulePlaceList.forEach(jwSchedulePlace -> {
                jwSchedulePlaceService.deleteJwSchedulePlaceById(jwSchedulePlace.getId());
            });
        }
        return jwScheduleInfoMapper.deleteJwScheduleInfoById(id);
    }
}
