package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwEightScore;
import org.apache.ibatis.annotations.Param;

public interface JwEightScoreMapper {

    public JwEightScore selectJwEightScoreById(Long id);

    public List<JwEightScore> selectJwEightScoreList(JwEightScore jwEightScore);

    public JwEightScore selectJwEightScore(@Param("judgeId") String judgeId, @Param("currentPkGroup") String currentPkGroup, @Param("playerGroup") Long playerGroup, @Param("lun") Long lun);

    public List<JwEightScore> selectJwEightScoreListByPkGroup(@Param("playerPkGroup") String playerPkGroup, @Param("playerGroup") Long playerGroup);

    public int insertJwEightScore(JwEightScore jwEightScore);

    public int updateJwEightScore(JwEightScore jwEightScore);

    public int deleteJwEightScoreById(Long id);

    public int deleteJwEightScoreByIds(Long[] ids);
}
