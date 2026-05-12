package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwSchedulePlace;

public interface JwSchedulePlaceMapper {

    public JwSchedulePlace selectJwSchedulePlaceById(Long id);

    public JwSchedulePlace getLastJwSchedulePlace(JwSchedulePlace jwSchedulePlace);

    public List<JwSchedulePlace> selectJwSchedulePlaceList(JwSchedulePlace jwSchedulePlace);

    public List<JwSchedulePlace> listJwSchedulePlaceWithScheduleItem(JwSchedulePlace jwSchedulePlace);

    public List<JwSchedulePlace> listJwSchedulePlaceWithScheduleItemSimple(JwSchedulePlace jwSchedulePlace);

    public int insertJwSchedulePlace(JwSchedulePlace jwSchedulePlace);

    public int updateJwSchedulePlace(JwSchedulePlace jwSchedulePlace);

    public int deleteJwSchedulePlaceById(Long id);

    public int deleteJwSchedulePlaceByIds(Long[] ids);
}
