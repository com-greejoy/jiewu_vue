package com.ruoyi.project.jiewu.service;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwTeamMapper;
import com.ruoyi.project.jiewu.domain.JwTeam;

/**
 * 代表队Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-29
 */
@Service
public class JwTeamService {

    @Autowired
    private JwTeamMapper jwTeamMapper;

    /**
     * 查询代表队
     * 
     * @param id 代表队主键
     * @return 代表队
     */
    public JwTeam selectJwTeamById(Long id) {
        return jwTeamMapper.selectJwTeamById(id);
    }

    /**
     * 查询代表队列表
     * 
     * @param jwTeam 代表队
     * @return 代表队
     */
    public List<JwTeam> selectJwTeamList(JwTeam jwTeam) {
        return jwTeamMapper.selectJwTeamList(jwTeam);
    }

    public List<JwTeam> selectJwTeamByUserId(Long createUserId) {
        return jwTeamMapper.selectJwTeamByUserId(createUserId);
    }


    /**
     * 新增代表队
     * 
     * @param jwTeam 代表队
     * @return 结果
     */
    public int insertJwTeam(JwTeam jwTeam) {
        jwTeam.setCreateTime(DateUtils.getNowDate());
        return jwTeamMapper.insertJwTeam(jwTeam);
    }

    /**
     * 修改代表队
     * 
     * @param jwTeam 代表队
     * @return 结果
     */
    public int updateJwTeam(JwTeam jwTeam) {
        jwTeam.setUpdateTime(DateUtils.getNowDate());
        return jwTeamMapper.updateJwTeam(jwTeam);
    }

    /**
     * 批量删除代表队
     * 
     * @param ids 需要删除的代表队主键
     * @return 结果
     */
    public int deleteJwTeamByIds(Long[] ids) {
        return jwTeamMapper.deleteJwTeamByIds(ids);
    }

    /**
     * 删除代表队信息
     * 
     * @param id 代表队主键
     * @return 结果
     */
    public int deleteJwTeamById(Long id) {
        return jwTeamMapper.deleteJwTeamById(id);
    }
}
