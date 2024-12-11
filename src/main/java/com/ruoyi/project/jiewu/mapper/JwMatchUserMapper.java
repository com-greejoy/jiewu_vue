package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwMatchUser;

public interface JwMatchUserMapper {

    public JwMatchUser selectJwMatchUserByUserId(Long userId);

    public List<JwMatchUser> selectJwMatchUserList(JwMatchUser jwMatchUser);

    public int insertJwMatchUser(JwMatchUser jwMatchUser);

    public int updateJwMatchUser(JwMatchUser jwMatchUser);

    public int deleteJwMatchUserByUserId(Long userId);

    public int deleteJwMatchUserByMatchId(Long matchId);

    public int deleteJwMatchUserByUserIds(Long[] userIds);
}
