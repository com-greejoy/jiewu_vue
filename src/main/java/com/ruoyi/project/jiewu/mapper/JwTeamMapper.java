package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwTeam;

/**
 * 代表队Mapper接口
 * 
 * @author ruoyi
 * @date 2024-05-29
 */
public interface JwTeamMapper {

    /**
     * 查询代表队
     * 
     * @param id 代表队主键
     * @return 代表队
     */
    public JwTeam selectJwTeamById(Long id);

    /**
     * 查询代表队列表
     * 
     * @param jwTeam 代表队
     * @return 代表队集合
     */
    public List<JwTeam> selectJwTeamList(JwTeam jwTeam);

    public List<JwTeam> selectJwTeamByUserId(Long createUserId);
    /**
     * 新增代表队
     * 
     * @param jwTeam 代表队
     * @return 结果
     */
    public int insertJwTeam(JwTeam jwTeam);

    /**
     * 修改代表队
     * 
     * @param jwTeam 代表队
     * @return 结果
     */
    public int updateJwTeam(JwTeam jwTeam);

    /**
     * 删除代表队
     * 
     * @param id 代表队主键
     * @return 结果
     */
    public int deleteJwTeamById(Long id);

    /**
     * 批量删除代表队
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteJwTeamByIds(Long[] ids);
}
