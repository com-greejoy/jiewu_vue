package com.ruoyi.project.jiewu.mapper;

import com.ruoyi.project.jiewu.domain.JwMatch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JwMatchMapper {

    public JwMatch selectJwMatchById(Long id);

    public List<JwMatch> selectFutureIngList();

    public List<JwMatch> listWxUserMatchList(@Param("userId") Long userId);

    public List<JwMatch> selectJwMatchList(JwMatch jwMatch);

    public int insertJwMatch(JwMatch jwMatch);

    public int updateJwMatch(JwMatch jwMatch);

    public int updateJwMatchInvitationList(@Param("matchId") Long matchId, @Param("invitationList") String invitationList);

    public int updateJwMatchLastBackNum(@Param("matchId") Long matchId, @Param("lastBackNum") Long lastBackNum);

    public int deleteJwMatchById(Long id);

    public int deleteJwMatchByIds(Long[] ids);
}
