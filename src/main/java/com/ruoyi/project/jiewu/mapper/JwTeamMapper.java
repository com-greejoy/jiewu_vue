package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwTeam;
import org.apache.ibatis.annotations.Param;

/**
 * 代表队Mapper接口
 * 
 * @author ruoyi
 * @date 2024-05-29
 */
public interface JwTeamMapper {

    public JwTeam selectJwTeamById(Long id);

    public JwTeam selectJwTeamByName(@Param("teamName") String teamName);

    public List<JwTeam> selectJwTeamList(JwTeam jwTeam);

    public List<JwTeam> selectJwTeamListWithMatch(JwTeam jwTeam);

    public List<JwTeam> selectJwTeamByUserId(Long createUserId);

    public int insertJwTeam(JwTeam jwTeam);

    public int updateJwTeam(JwTeam jwTeam);

    public int deleteJwTeamById(Long id);

    public int deleteJwTeamByIds(Long[] ids);
}
