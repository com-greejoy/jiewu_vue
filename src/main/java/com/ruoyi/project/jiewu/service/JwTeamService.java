package com.ruoyi.project.jiewu.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.ruoyi.common.utils.BigDecimalUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.jiewu.domain.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwTeamMapper;

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

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwGameItemService jwGameItemService;


    public JwTeam selectJwTeamById(Long id) {
        return jwTeamMapper.selectJwTeamById(id);
    }

    public JwTeam selectJwTeamByName(String teamName) {
        return jwTeamMapper.selectJwTeamByName(teamName);
    }

    public List<JwTeam> selectJwTeamList(JwTeam jwTeam) {
        if(StringUtils.isLongNotNull(jwTeam.getMatchId())){
            // 计算每个队伍的费用
            List<JwTeam> list = jwTeamMapper.selectJwTeamListWithMatch(jwTeam);
            if(list != null){
                list.forEach(jwTeam1 -> {
                    BigDecimal allFee = new BigDecimal("0");
                    List<JwSignRecord> jwSignRecords = jwSignRecordService.selectJwSignRecordListWithUserMatch(jwTeam1.getId(), jwTeam.getMatchId());
                    if (jwSignRecords != null && jwSignRecords.size() > 0) {
                        for (JwSignRecord jwSignRecord : jwSignRecords) {
                            JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(jwSignRecord.getGameItemId());
                            jwSignRecord.setJwGameItem(jwGameItem);

//                            if ("1".equals(jwGameItem.getSportLimit())) {
//                                if(BigDecimalUtil.isNotNull(jwGameItem.getFee())) allFee = allFee.add(jwGameItem.getFee());
//                            } else if ("3".equals(jwGameItem.getSportLimit())) {
//                                // 多人
//                                if (jwSignRecord.getJwSignRecordSportList() != null && jwSignRecord.getJwSignRecordSportList().size() > 0) {
//                                    int sportCount = jwSignRecord.getJwSignRecordSportList().size();
//                                    BigDecimal fee = jwGameItem.getFee();
//                                    // 如果报名人数超过规定人数, 就重新计算价格   价格 / 规定人数 * 实际人数
//                                    if (sportCount > jwGameItem.getFeeMaxSport() && BigDecimalUtil.isNotNull(fee)) {
//                                        fee = fee.multiply(new BigDecimal(sportCount)).divide(new BigDecimal(jwGameItem.getFeeMaxSport()), 0, RoundingMode.DOWN);
//                                    }
//                                    allFee = allFee.add(fee);
//                                }
//                            }

                            allFee = allFee.add(getSignRecordFee(jwGameItem, jwSignRecord));
                        }

                        jwTeam1.setBackNums(jwSignRecords.stream()
                                .map(JwSignRecord::getBackNumber) // 获取每个Person的backNum
                                .distinct()              // 去重
                                .sorted()                // 排序
                                .map(String::valueOf)    // 转换为String类型
                                .collect(Collectors.joining(",")));
                    }
                    jwTeam1.setAllFee(allFee);


                });
            }
            return list;
        }else{
            return jwTeamMapper.selectJwTeamList(jwTeam);
        }
    }

    public BigDecimal getSignRecordFee(JwGameItem jwGameItem, JwSignRecord jwSignRecord){
        BigDecimal fee = new BigDecimal("0");
        // 单人  双人的 直接就是组别价格
        if ("1".equals(jwGameItem.getSportLimit()) || "2".equals(jwGameItem.getSportLimit())) {
            if(BigDecimalUtil.isNotNull(jwGameItem.getFee())) {
                fee = jwGameItem.getFee();
            }
            // 李金鑫星曜同辉的  按实际人数收费
            if(jwGameItem.getMatchId().equals(13l) && Long.valueOf(jwGameItem.getCode()) >= 8 && Long.valueOf(jwGameItem.getCode()) <= 12){
                if(jwSignRecord.getJwSignRecordSportList() != null ){
                    if(jwSignRecord.getJwSignRecordSportList().size() == 1){
                        fee = new BigDecimal("580");
                    }else if(jwSignRecord.getJwSignRecordSportList().size() == 2){
                        fee = new BigDecimal("960");
                    }else if(jwSignRecord.getJwSignRecordSportList().size() == 3 || jwSignRecord.getJwSignRecordSportList().size() == 4){
                        fee = new BigDecimal("380").multiply(new BigDecimal(jwSignRecord.getJwSignRecordSportList().size()));
                    }
                }
            }
        } else if ("3".equals(jwGameItem.getSportLimit())) {
            // 多人
            if (jwSignRecord.getJwSignRecordSportList() != null && jwSignRecord.getJwSignRecordSportList().size() > 0) {
                int sportCount = jwSignRecord.getJwSignRecordSportList().size();
                 fee = jwGameItem.getFee();
                // 如果报名人数超过规定人数, 就重新计算价格   价格 / 规定人数 * 实际人数
                if (sportCount > jwGameItem.getFeeMaxSport() && BigDecimalUtil.isNotNull(fee)) {
                    fee = fee.multiply(new BigDecimal(sportCount)).divide(new BigDecimal(jwGameItem.getFeeMaxSport()), 0, RoundingMode.DOWN);
                }
            }

            // 王磊的计算  团体舞每个节目人数不少于 5人，少于 5人的按单双人收费 每人 560
//            if(jwGameItem.getMatchId().equals(12l) && Long.valueOf(jwGameItem.getCode()) <= 27){
//                if(jwSignRecord.getJwSignRecordSportList() != null && jwSignRecord.getJwSignRecordSportList().size() < 5 &&  jwSignRecord.getJwSignRecordSportList().size() >= 1){
//                    fee = new BigDecimal("560").multiply(new BigDecimal(jwSignRecord.getJwSignRecordSportList().size()));
//                }
//            }


        }
        return fee;
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
