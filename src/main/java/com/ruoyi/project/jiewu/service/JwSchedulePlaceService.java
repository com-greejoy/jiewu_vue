package com.ruoyi.project.jiewu.service;

import java.util.List;

import com.ruoyi.project.jiewu.domain.JwScheduleItem;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwSchedulePlaceMapper;
import com.ruoyi.project.jiewu.domain.JwSchedulePlace;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwSchedulePlaceService {

    @Autowired
    private JwSchedulePlaceMapper jwSchedulePlaceMapper;

    @Autowired
    private JwScheduleItemService jwScheduleItemService;

    public JwSchedulePlace selectJwSchedulePlaceById(Long id) {
        return jwSchedulePlaceMapper.selectJwSchedulePlaceById(id);
    }

    // 获取一个单元的最后一场比赛的 order
    public JwSchedulePlace getLastJwSchedulePlace(JwSchedulePlace jwSchedulePlace) {
        return jwSchedulePlaceMapper.getLastJwSchedulePlace(jwSchedulePlace);
    }


    public List<JwSchedulePlace> selectJwSchedulePlaceList(JwSchedulePlace jwSchedulePlace) {
        return jwSchedulePlaceMapper.selectJwSchedulePlaceList(jwSchedulePlace);
    }

    // 获取每个单元的场次和小项
    public List<JwSchedulePlace> listJwSchedulePlaceWithScheduleItem(JwSchedulePlace jwSchedulePlace) {
        return jwSchedulePlaceMapper.listJwSchedulePlaceWithScheduleItem(jwSchedulePlace);
    }

    public int insertJwSchedulePlace(JwSchedulePlace jwSchedulePlace) {
        // 获取一个单元的最后一场比赛的 order
        JwSchedulePlace lastJwSchedulePlace = jwSchedulePlaceMapper.getLastJwSchedulePlace(jwSchedulePlace);
        if(lastJwSchedulePlace == null){
            jwSchedulePlace.setPlaceOrder(1l);
        }else{
            jwSchedulePlace.setPlaceOrder(lastJwSchedulePlace.getPlaceOrder()+1);
        }
        return jwSchedulePlaceMapper.insertJwSchedulePlace(jwSchedulePlace);
    }

    public int updateJwSchedulePlace(JwSchedulePlace jwSchedulePlace) {
        return jwSchedulePlaceMapper.updateJwSchedulePlace(jwSchedulePlace);
    }

    public int deleteJwSchedulePlaceByIds(Long[] ids) {
        return jwSchedulePlaceMapper.deleteJwSchedulePlaceByIds(ids);
    }

    @Transactional
    public int deleteJwSchedulePlaceById(Long id) {

        jwScheduleItemService.clearSchedulePlace(id);

        return jwSchedulePlaceMapper.deleteJwSchedulePlaceById(id);
    }
}
