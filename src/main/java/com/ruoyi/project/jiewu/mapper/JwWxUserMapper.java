package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwWxUser;
import org.apache.ibatis.annotations.Param;

/**
 * 微信用户Mapper接口
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
public interface JwWxUserMapper {

    /**
     * 查询微信用户
     * 
     * @param id 微信用户主键
     * @return 微信用户
     */
    public JwWxUser selectJwWxUserById(Long id);

    public JwWxUser selectZwWxUserByOpenId(@Param("openId") String openId);



    /**
     * 查询微信用户列表
     * 
     * @param jwWxUser 微信用户
     * @return 微信用户集合
     */
    public List<JwWxUser> selectJwWxUserList(JwWxUser jwWxUser);

    /**
     * 新增微信用户
     * 
     * @param jwWxUser 微信用户
     * @return 结果
     */
    public int insertJwWxUser(JwWxUser jwWxUser);

    /**
     * 修改微信用户
     * 
     * @param jwWxUser 微信用户
     * @return 结果
     */
    public int updateJwWxUser(JwWxUser jwWxUser);

    /**
     * 删除微信用户
     * 
     * @param id 微信用户主键
     * @return 结果
     */
    public int deleteJwWxUserById(Long id);

    /**
     * 批量删除微信用户
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteJwWxUserByIds(Long[] ids);
}
