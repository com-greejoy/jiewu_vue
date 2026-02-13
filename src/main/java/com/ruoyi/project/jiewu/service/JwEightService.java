package com.ruoyi.project.jiewu.service;

import java.util.List;

import com.ruoyi.framework.aspectj.lang.annotation.DataSource;
import com.ruoyi.framework.aspectj.lang.enums.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwEightMapper;
import com.ruoyi.project.jiewu.domain.JwEight;

@Service
public class JwEightService {

    @Autowired
    private JwEightMapper jwEightMapper;

    public JwEight selectJwEightById(Long id) {
        return jwEightMapper.selectJwEightById(id);
    }

    public List<JwEight> selectJwEightList(JwEight jwEight) {
        return jwEightMapper.selectJwEightList(jwEight);
    }

    public List<JwEight> selectJwEightListWithSport(JwEight jwEight) {
        return jwEightMapper.selectJwEightListWithSport(jwEight);
    }

    public int insertJwEight(JwEight jwEight) {
        return jwEightMapper.insertJwEight(jwEight);
    }

    public int updateJwEight(JwEight jwEight) {
        return jwEightMapper.updateJwEight(jwEight);
    }

    // 保存对阵晋级选手
    public int saveEightPro(Long gameItemId, Long playerId, String playerPosition, String playerIndex) {
        return jwEightMapper.saveEightPro(gameItemId, playerId, playerPosition, playerIndex);
    }
    public int clearEightPro(Long gameItemId , String playerPosition ) {
        return jwEightMapper.clearEightPro(gameItemId, playerPosition);
    }


    // 裁判打对阵分数
    public int updateJwEightSport(String playerId, String playerPosition, Long playerGroup) {
        return jwEightMapper.updateJwEightSport(playerId, playerPosition, playerGroup);
    }

    public int deleteJwEightByIds(Long[] ids) {
        return jwEightMapper.deleteJwEightByIds(ids);
    }

    public int deleteJwEightById(Long id) {
        return jwEightMapper.deleteJwEightById(id);
    }

    public int deleteAllJwEight(Long gameItemId) {
        return jwEightMapper.deleteAllJwEight(gameItemId);
    }

    // 删除线上的对阵名单
    @DataSource(value = DataSourceType.SLAVE)
    public int deleteAllJwEightUp(Long gameItemId) {
        return jwEightMapper.deleteAllJwEight(gameItemId);
    }
    // 同步线上的对阵名单
    @DataSource(value = DataSourceType.SLAVE)
    public int insertJwEightUp(JwEight jwEight) {
        return jwEightMapper.insertJwEight(jwEight);
    }

}
