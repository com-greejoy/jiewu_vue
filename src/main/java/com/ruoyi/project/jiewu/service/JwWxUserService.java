package com.ruoyi.project.jiewu.service;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwWxUserMapper;
import com.ruoyi.project.jiewu.domain.JwWxUser;

/**
 * 微信用户Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
@Service
public class JwWxUserService {

    @Autowired
    private JwWxUserMapper jwWxUserMapper;

    /**
     * 查询微信用户
     * 
     * @param id 微信用户主键
     * @return 微信用户
     */
    public JwWxUser selectJwWxUserById(Long id) {
        return jwWxUserMapper.selectJwWxUserById(id);
    }

    public JwWxUser selectZwWxUserByOpenId(String openId) {
        return jwWxUserMapper.selectZwWxUserByOpenId(openId);
    }

    /**
     * 查询微信用户列表
     * 
     * @param jwWxUser 微信用户
     * @return 微信用户
     */
    public List<JwWxUser> selectJwWxUserList(JwWxUser jwWxUser) {
        return jwWxUserMapper.selectJwWxUserList(jwWxUser);
    }

    /**
     * 新增微信用户
     * 
     * @param jwWxUser 微信用户
     * @return 结果
     */
    public int insertJwWxUser(JwWxUser jwWxUser) {
        jwWxUser.setCreateTime(DateUtils.getNowDate());
        return jwWxUserMapper.insertJwWxUser(jwWxUser);
    }

    /**
     * 修改微信用户
     * 
     * @param jwWxUser 微信用户
     * @return 结果
     */
    public int updateJwWxUser(JwWxUser jwWxUser) {
        jwWxUser.setUpdateTime(DateUtils.getNowDate());
        return jwWxUserMapper.updateJwWxUser(jwWxUser);
    }

    /**
     * 批量删除微信用户
     * 
     * @param ids 需要删除的微信用户主键
     * @return 结果
     */
    public int deleteJwWxUserByIds(Long[] ids) {
        return jwWxUserMapper.deleteJwWxUserByIds(ids);
    }

    /**
     * 删除微信用户信息
     * 
     * @param id 微信用户主键
     * @return 结果
     */
    public int deleteJwWxUserById(Long id) {
        return jwWxUserMapper.deleteJwWxUserById(id);
    }
}
