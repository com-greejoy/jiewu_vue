package com.ruoyi.project.jiewu.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwMatchUserMapper;
import com.ruoyi.project.jiewu.domain.JwMatchUser;

@Service
public class JwMatchUserService {

    @Autowired
    private JwMatchUserMapper jwMatchUserMapper;

    public JwMatchUser selectJwMatchUserByUserId(Long userId) {
        return jwMatchUserMapper.selectJwMatchUserByUserId(userId);
    }

    public List<JwMatchUser> selectJwMatchUserList(JwMatchUser jwMatchUser) {
        return jwMatchUserMapper.selectJwMatchUserList(jwMatchUser);
    }

    public int insertJwMatchUser(JwMatchUser jwMatchUser) {
        if(jwMatchUser.getUserIds() != null && jwMatchUser.getUserIds().length > 0){
            // 清除以前的管理员
            jwMatchUserMapper.deleteJwMatchUserByMatchId(jwMatchUser.getMatchId());
            // 加新的
            for(Long userId : jwMatchUser.getUserIds()){
                JwMatchUser intUser = new JwMatchUser();
                intUser.setMatchId(jwMatchUser.getMatchId());
                intUser.setUserId(userId);
                jwMatchUserMapper.insertJwMatchUser(intUser);
            }
        }
        return 1;
    }

    public int updateJwMatchUser(JwMatchUser jwMatchUser) {
        return jwMatchUserMapper.updateJwMatchUser(jwMatchUser);
    }

    public int deleteJwMatchUserByUserIds(Long[] userIds) {
        return jwMatchUserMapper.deleteJwMatchUserByUserIds(userIds);
    }

    public int deleteJwMatchUserByUserId(Long userId) {
        return jwMatchUserMapper.deleteJwMatchUserByUserId(userId);
    }
}
