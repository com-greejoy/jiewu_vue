package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwEight;
import org.apache.ibatis.annotations.Param;

public interface JwEightMapper {

    public JwEight selectJwEightById(Long id);

    public List<JwEight> selectJwEightList(JwEight jwEight);

    public List<JwEight> selectJwEightListWithSport(JwEight jwEight);

    public List<JwEight> getEightOrder(@Param("gameItemId") Long gameItemId);

    // 保存对阵晋级选手
    public int saveEightPro(@Param("gameItemId") Long gameItemId, @Param("playerId") Long playerId, @Param("playerPosition") String playerPosition, @Param("playerIndex") String playerIndex);

    public int clearEightPro(@Param("gameItemId") Long gameItemId , @Param("playerPosition") String playerPosition );

    public int updateJwEightSport(@Param("playerId") String playerId, @Param("playerPosition") String playerPosition, @Param("playerGroup") Long playerGroup);

    public int insertJwEight(JwEight jwEight);

    public int updateJwEight(JwEight jwEight);

    public int deleteJwEightById(Long id);

    public int deleteJwEightByIds(Long[] ids);

    public int deleteAllJwEight(@Param("gameItemId") Long gameItemId);



    public int setJiJunOne(@Param("playerPkGroup") String playerPkGroup, @Param("playerGroup") Long playerGroup);

    public int setJiJunTwo(@Param("playerPkGroup") String playerPkGroup, @Param("playerGroup") Long playerGroup);

    public int setJiJunOrder(@Param("playerPkGroup") String playerPkGroup, @Param("playerGroup") Long playerGroup);

    public int setYaJunOrder(@Param("playerPkGroup") String playerPkGroup, @Param("playerGroup") Long playerGroup);

    public int setFourOrder(@Param("playerPkGroup") String playerPkGroup, @Param("playerGroup") Long playerGroup);

    public Long getFourCount(@Param("playerGroup") Long playerGroup);


}
