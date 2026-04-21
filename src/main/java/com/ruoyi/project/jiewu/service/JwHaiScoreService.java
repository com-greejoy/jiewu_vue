package com.ruoyi.project.jiewu.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.DataSource;
import com.ruoyi.framework.aspectj.lang.enums.DataSourceType;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.jiewu.domain.*;
import com.ruoyi.project.jiewu.mapper.JwEightMapper;
import com.ruoyi.project.jiewu.mapper.JwSignRecordMapper;
import com.ruoyi.project.jiewu.mapper.JwTeamMapper;
import io.swagger.annotations.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwHaiScoreMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwHaiScoreService {

    @Autowired
    private JwHaiScoreMapper jwHaiScoreMapper;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwScheduleItemService jwScheduleItemService;

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwEightService jwEightService;

    @Autowired
    private JwTeamMapper jwTeamMapper;

    @Autowired
    private JwAwardsItemService jwAwardsItemService;

    @Autowired
    private JwEightMapper jwEightMapper;

    @Autowired
    private JwMatchTeamService jwMatchTeamService;

    @Autowired
    private JwSignRecordMapper jwSignRecordMapper;

    public JwHaiScore selectJwHaiScoreById(Long id) {
        return jwHaiScoreMapper.selectJwHaiScoreById(id);
    }

    public JwHaiScore selectJwHaiScoreByJudgeAndSport(Long judgeId, Long sportId) {
        return jwHaiScoreMapper.selectJwHaiScoreByJudgeAndSport(judgeId, sportId);
    }

    public List<JwHaiScore> selectJwHaiScoreList(JwHaiScore jwHaiScore) {
        return jwHaiScoreMapper.selectJwHaiScoreList(jwHaiScore);
    }

    public List<JwHaiScore> selectJwHaiScoreListBySport(Long sportId) {
        return jwHaiScoreMapper.selectJwHaiScoreListBySport(sportId);
    }

    public List<JwHaiScore> selectJwHaiScoreListByGameItem(Long gameItemId) {
        return jwHaiScoreMapper.selectJwHaiScoreListByGameItem(gameItemId);
    }

    public List<JwHaiScore> selectJwHaiScoreListByScheduleItem(Long scheduleItemId) {
        return jwHaiScoreMapper.selectJwHaiScoreListByScheduleItem(scheduleItemId);
    }

    public int insertJwHaiScore(JwHaiScore jwHaiScore) {
        jwHaiScore.setCreateTime(DateUtils.getNowDate());
        return jwHaiScoreMapper.insertJwHaiScore(jwHaiScore);
    }

    public int updateJwHaiScore(JwHaiScore jwHaiScore) {
        return jwHaiScoreMapper.updateJwHaiScore(jwHaiScore);
    }

    public int deleteJwHaiScoreByIds(Long[] ids) {
        return jwHaiScoreMapper.deleteJwHaiScoreByIds(ids);
    }

    public int deleteJwHaiScoreById(Long id) {
        return jwHaiScoreMapper.deleteJwHaiScoreById(id);
    }

    // 保存裁判打分
    @Transactional
    public synchronized int saveJudgeScore(Long judgeId, String score, Long sportId) {
        JwSignRecord jwSignRecord = jwSignRecordService.selectJwSignRecordById(sportId);

        JwScheduleItem jwScheduleItem = jwScheduleItemService.selectJwScheduleItemById(jwSignRecord.getScheduleItemId());
        if (jwScheduleItem == null || "Y".equals(jwScheduleItem.getLockScore()) || "Y".equals(jwSignRecord.getLockJudgeScore())) {
            return 1;
        }

        if (jwSignRecord != null) {
            JwHaiScore oldJwScore = selectJwHaiScoreByJudgeAndSport(judgeId, sportId);

            if (StringUtils.isEmpty(score) && oldJwScore != null) {
                // 删除打分
                deleteJwHaiScoreById(oldJwScore.getId());
            } else {
                JwHaiScore jwHaiScore = new JwHaiScore(jwSignRecord.getMatchId(), jwSignRecord.getGameItemId(), jwSignRecord.getScheduleItemId(), sportId, judgeId, score, null);
                if (oldJwScore != null) {
                    jwHaiScore.setId(oldJwScore.getId());
                    updateJwHaiScore(jwHaiScore);
                } else {
                    insertJwHaiScore(jwHaiScore);
                }
            }
        }
        return 1;
    }

    // 计算组别成绩
    @Transactional
    public int jiSuanGameItem(Long gameItemId, String type) {
        // type  计算分数方式   直接平均分   去最高最低分   修正计算法

        JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(gameItemId);

        if ("zhijiepingjun".equals(type)) {
            jwSignRecordService.clearScoreByGameItem(gameItemId);

            // 每个组别的全部选手
            List<JwSignRecord> jwSignRecordList = jwSignRecordService.selectJwSignRecordListByGameItem(jwGameItem.getId());

            jwSignRecordList.forEach(jwSignRecord -> {
                // 选手的全部得分
                List<JwHaiScore> jwScoreListPlayer = selectJwHaiScoreListBySport(jwSignRecord.getId());

                BigDecimal playerScoreAll = new BigDecimal("0");
                int playerCount = 0;

                jwScoreListPlayer.sort((u1, u2) -> new BigDecimal(u2.getScore()).compareTo(new BigDecimal(u1.getScore())));

                for (JwHaiScore jwScore : jwScoreListPlayer) {
                    if (StringUtils.isNotEmpty(jwScore.getScore()) && new BigDecimal(jwScore.getScore()).compareTo(new BigDecimal("0")) > 0) {
                        playerScoreAll = playerScoreAll.add(new BigDecimal(jwScore.getScore()));
                        playerCount++;
                    }
                }

                if (playerCount > 0) {
                    BigDecimal playerAvg = playerScoreAll.divide(new BigDecimal(String.valueOf(playerCount)), 3, BigDecimal.ROUND_HALF_DOWN);
                    JwSignRecord jwSignRecordUpdate = new JwSignRecord();
                    jwSignRecordUpdate.setId(jwSignRecord.getId());
                    jwSignRecordUpdate.setAllScore(playerScoreAll.toString());
                    jwSignRecordUpdate.setAvgScore(playerAvg.toPlainString());
                    jwSignRecordService.updateJwSignRecord(jwSignRecordUpdate);
                }
            });
        } else if ("zuigaozuidi".equals(type)) {

            jwSignRecordService.clearScoreByGameItem(gameItemId);

            // 每个组别的全部选手
            List<JwSignRecord> jwSignRecordList = jwSignRecordService.selectJwSignRecordListByGameItem(jwGameItem.getId());

            jwSignRecordList.forEach(jwSignRecord -> {
                // 选手的全部得分
                List<JwHaiScore> jwScoreListPlayer = selectJwHaiScoreListBySport(jwSignRecord.getId());

                BigDecimal playerScoreAll = new BigDecimal("0");
                int playerCount = 0;

                jwScoreListPlayer.sort((u1, u2) -> new BigDecimal(u2.getScore()).compareTo(new BigDecimal(u1.getScore())));
                for (int i = 1; i < jwScoreListPlayer.size() - 1; i++) {
                    JwHaiScore jwScore = jwScoreListPlayer.get(i);
                    if (StringUtils.isNotEmpty(jwScore.getScore()) && new BigDecimal(jwScore.getScore()).compareTo(new BigDecimal("0")) > 0) {
                        playerScoreAll = playerScoreAll.add(new BigDecimal(jwScore.getScore()));
                        playerCount++;
                    }
                }

                if (playerCount > 0) {
                    BigDecimal playerAvg = playerScoreAll.divide(new BigDecimal(String.valueOf(playerCount)), 3, BigDecimal.ROUND_HALF_DOWN);
                    JwSignRecord jwSignRecordUpdate = new JwSignRecord();
                    jwSignRecordUpdate.setId(jwSignRecord.getId());
                    jwSignRecordUpdate.setAllScore(playerScoreAll.toString());
                    jwSignRecordUpdate.setAvgScore(playerAvg.toPlainString());
                    jwSignRecordService.updateJwSignRecord(jwSignRecordUpdate);
                }
            });

        } else if ("xiuzheng".equals(type)) {

            jwSignRecordService.clearScoreByGameItem(gameItemId);

            // 整个组别 全部打分
            List<JwHaiScore> jwScoreListAll = selectJwHaiScoreListByGameItem(gameItemId);

            BigDecimal allSum = new BigDecimal("0"); // 全部总分
            int allCount = 0;

            for (JwHaiScore jwScore : jwScoreListAll) {
                if (StringUtils.isNotEmpty(jwScore.getScore()) && new BigDecimal(jwScore.getScore()).compareTo(new BigDecimal("0")) > 0) {
                    allSum = allSum.add(new BigDecimal(jwScore.getScore()));
                    allCount++;
                }
            }
            if (allCount > 0) {
                // 全部平均分
                BigDecimal allSumAvg = allSum.divide(new BigDecimal(String.valueOf(allCount)), 3, RoundingMode.HALF_UP);

                BigDecimal finalAllSumAvg = allSumAvg;

                // 获取组别的每个小组
                List<JwScheduleItem> jwScheduleItemList = jwScheduleItemService.selectJwScheduleItemByGameItemId(gameItemId);
                if (jwScheduleItemList != null && jwScheduleItemList.size() > 0) {
                    jwScheduleItemList.forEach(jwScheduleItem -> {
                        // 每个小组的打分
                        List<JwHaiScore> jwScoreListIndex = selectJwHaiScoreListByScheduleItem(jwScheduleItem.getId());

                        // 计算小组平均分
                        BigDecimal indexSum = new BigDecimal("0"); // 小组 总分
                        int indexCount = 0;

                        for (JwHaiScore jwScore : jwScoreListIndex) {
                            if (StringUtils.isNotEmpty(jwScore.getScore()) && new BigDecimal(jwScore.getScore()).compareTo(new BigDecimal("0")) > 0) {
                                indexSum = indexSum.add(new BigDecimal(jwScore.getScore()));
                                indexCount++;
                            }
                        }
                        if (indexCount > 0) {
                            // // 小组 平均分
                            BigDecimal indexSumAvg = indexSum.divide(new BigDecimal(String.valueOf(indexCount)), 3, BigDecimal.ROUND_HALF_DOWN);
                            // 每个组别的全部选手
                            List<JwSignRecord> jwSportList = jwSignRecordService.selectJwSignRecordListByScheduleItem(jwScheduleItem.getId());

                            BigDecimal finalIndexSumAvg = indexSumAvg;
                            jwSportList.forEach(jwSignRecord -> {
                                // 选手的全部得分
                                List<JwHaiScore> jwScoreListPlayer = selectJwHaiScoreListBySport(jwSignRecord.getId());

                                BigDecimal playerScoreAll = new BigDecimal("0");
                                int playerCount = 0;
                                BigDecimal playerAvg = new BigDecimal("0");

                                BigDecimal avgscore = new BigDecimal("0"); // 选手最终得分
                                for (JwHaiScore jwScore : jwScoreListPlayer) {
                                    if (StringUtils.isNotEmpty(jwScore.getScore()) && new BigDecimal(jwScore.getScore()).compareTo(new BigDecimal("0")) > 0) {
                                        playerScoreAll = playerScoreAll.add(new BigDecimal(jwScore.getScore()));
                                        playerCount++;
                                    }
                                }
                                if (playerCount > 0) {
                                    playerAvg = playerScoreAll.divide(new BigDecimal(String.valueOf(playerCount)), 3, BigDecimal.ROUND_HALF_DOWN);

                                    avgscore = playerAvg.multiply(finalAllSumAvg).divide(finalIndexSumAvg, 3, BigDecimal.ROUND_HALF_DOWN);

                                    JwSignRecord jwSportUpdate = new JwSignRecord();
                                    jwSportUpdate.setId(jwSignRecord.getId());
                                    jwSportUpdate.setAllScore(playerScoreAll.toString());
                                    jwSportUpdate.setAvgScore(avgscore.toPlainString());
                                    jwSignRecordService.updateJwSignRecord(jwSportUpdate);
                                }
                            });
                        }
                    });
                }
            }

        }

        // 排名
        List<JwSignRecord> jwSignRecordList = jwSignRecordService.selectJwSignRecordListByGameItem(jwGameItem.getId());

        jwSignRecordList = jwSignRecordList.stream().filter(jwSport1 -> StringUtils.isNotEmpty(jwSport1.getAvgScore())).collect(Collectors.toList());

        jwSignRecordList.sort((u1, u2) -> new BigDecimal(u2.getAvgScore()).compareTo(new BigDecimal(u1.getAvgScore())));
        String preavgscore = "";
        Long prei = 0l;
        for (int i = 0; i < jwSignRecordList.size(); i++) {
            JwSignRecord jwSport2 = new JwSignRecord();
            jwSport2.setId(jwSignRecordList.get(i).getId());
            if (preavgscore.equals(jwSignRecordList.get(i).getAvgScore())) {
                jwSport2.setRankOrder(prei + 1);
            } else {
                jwSport2.setRankOrder((long) (i + 1));
                prei = (long) i;
            }
            jwSignRecordService.updateJwSignRecord(jwSport2);
            preavgscore = jwSignRecordList.get(i).getAvgScore();
        }

        // 把成绩存成字符串准备上传到服务器
        JwSignRecord jwSignRecord = new JwSignRecord();
        jwSignRecord.setGameItemId(gameItemId);
        List<JwSignRecord> jwSignRecordList1 = listGameItemGradeDes(jwSignRecord);
        jwSignRecordList1.forEach(jwSignRecord1 -> {
            JwSignRecord updateSign = new JwSignRecord();
            updateSign.setId(jwSignRecord1.getId());
            if(StringUtils.isNotEmpty(jwSignRecord1.getGradeStr())){
                updateSign.setGradeStr(jwSignRecord1.getGradeStr());
                jwSignRecordMapper.saveAward(updateSign);
            }

        });
        return 1;
    }

    // 海选打分完成
    @Transactional
    public AjaxResult haiXuanComplete(Long gameItemId) {
        JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(gameItemId);
        Long promotionNum = jwGameItem.getPromotionNum();
        List<JwSignRecord> jwSignRecordList = jwSignRecordService.selectHaiXuanJinJiSportList(gameItemId);
        if (jwSignRecordList != null && jwSignRecordList.size() > promotionNum) {
            return AjaxResult.error("海选" + jwSignRecordList + "强大于晋级人数个人");
        }

        if (jwSignRecordList != null && jwSignRecordList.size() == promotionNum) {

            String orders = jwSignRecordList.stream().map(signRecord -> signRecord.getRankOrder().toString()).collect(Collectors.joining(""));
            String formatText = "";
            for (int i = 1; i <= promotionNum; i++) {
                formatText += String.valueOf(i);
            }
            if (!formatText.equals(orders)) {
                return AjaxResult.error("名次有并列排名，需手动处理");
            }

            // 编排 开始
            jwEightService.deleteAllJwEight(gameItemId);

            // 全部晋级人数
            jwSignRecordList.forEach(jwSport -> {
                JwEight jwEight = new JwEight();
                jwEight.setPlayerId(jwSport.getId());
                jwEight.setPlayerIndex(jwSport.getRankOrder()); // 名次 1 2 3 4
                jwEight.setPlayerPosition(String.valueOf(promotionNum));// 多少强 32 16 8
                jwEight.setGameItemId(gameItemId);
                jwEight.setGroupIndex(promotionNum + "强");
                jwEight.setMatchId(jwGameItem.getMatchId());
                jwEightService.insertJwEight(jwEight);
            });

            // 选32强，先插入16强空位置
            if (promotionNum > 16l) {
                for (Long ii = 1l; ii <= 16l; ii++) {
                    JwEight jwEight = new JwEight();
                    jwEight.setPlayerIndex(ii);
                    jwEight.setPlayerPosition("16");// 多少强 32 16 8
                    jwEight.setGameItemId(gameItemId);
                    jwEight.setGroupIndex("16强");
                    jwEight.setMatchId(jwGameItem.getMatchId());
                    jwEightService.insertJwEight(jwEight);
                }
            }
            // 选16强，先插入8强空位置
            if (promotionNum > 8l) {
                for (Long ii = 1l; ii <= 8; ii++) {
                    JwEight jwEight = new JwEight();
                    jwEight.setPlayerIndex(ii);
                    jwEight.setPlayerPosition("8");// 多少强 32 16 8
                    jwEight.setGameItemId(gameItemId);
                    jwEight.setGroupIndex("8强");
                    jwEight.setMatchId(jwGameItem.getMatchId());
                    jwEightService.insertJwEight(jwEight);
                }
            }
            if (promotionNum > 4) {
                // 插入半决赛空位置
                for (Long ii = 1l; ii <= 4; ii++) {
                    JwEight jwEight = new JwEight();
                    jwEight.setPlayerIndex(ii);
                    jwEight.setPlayerPosition("4");// 多少强 32 16 8
                    jwEight.setGameItemId(gameItemId);
                    jwEight.setGroupIndex("半决赛");
                    jwEight.setMatchId(jwGameItem.getMatchId());
                    jwEightService.insertJwEight(jwEight);
                }
            }

            JwEight jwEight8 = new JwEight();
            jwEight8.setGameItemId(gameItemId);
            jwEight8.setGroupIndex("季军争夺");
            jwEight8.setPlayerPosition("3");
            jwEight8.setPlayerIndex(1l);
            jwEight8.setMatchId(jwGameItem.getMatchId());
            jwEightService.insertJwEight(jwEight8);

            JwEight jwEight9 = new JwEight();
            jwEight9.setGameItemId(gameItemId);
            jwEight9.setGroupIndex("季军争夺");
            jwEight9.setPlayerPosition("3");
            jwEight9.setPlayerIndex(2l);
            jwEight9.setMatchId(jwGameItem.getMatchId());
            jwEightService.insertJwEight(jwEight9);

            JwEight jwEight10 = new JwEight();
            jwEight10.setGameItemId(gameItemId);
            jwEight10.setGroupIndex("季军");
            jwEight10.setPlayerPosition("3");
            jwEight10.setPlayerIndex(0l);
//            jwEight10.setEightOrder("3.3");
            jwEight10.setMatchId(jwGameItem.getMatchId());
            jwEightService.insertJwEight(jwEight10);

            JwEight jwEight5 = new JwEight();
            jwEight5.setGameItemId(gameItemId);
            jwEight5.setGroupIndex("冠军争夺");
            jwEight5.setPlayerPosition("2");
            jwEight5.setPlayerIndex(1l);
            jwEight5.setMatchId(jwGameItem.getMatchId());
            jwEightService.insertJwEight(jwEight5);

            JwEight jwEight6 = new JwEight();
            jwEight6.setGameItemId(gameItemId);
            jwEight6.setGroupIndex("冠军争夺");
            jwEight6.setPlayerPosition("2");
            jwEight6.setPlayerIndex(2l);
            jwEight6.setMatchId(jwGameItem.getMatchId());
            jwEightService.insertJwEight(jwEight6);

            JwEight jwEight7 = new JwEight();
            jwEight7.setGameItemId(gameItemId);
            jwEight7.setGroupIndex("冠军");
            jwEight7.setPlayerIndex(1l);
            jwEight7.setPlayerPosition("1");
            jwEight7.setMatchId(jwGameItem.getMatchId());
//            jwEight7.setEightOrder("1");
            jwEightService.insertJwEight(jwEight7);

        } else {
            return AjaxResult.error("人数不够");
        }

        return AjaxResult.success();
    }

    // 获取比赛 项目成绩
    public List<JwSignRecord> listGameItemGradeDes(JwSignRecord jwSignRecord) {
        List<JwSignRecord> list = new ArrayList<>();
        if (StringUtils.isLongNotNull(jwSignRecord.getGameItemId())) {

            JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(jwSignRecord.getGameItemId());

            // 获取海选成绩
            list = jwSignRecordService.selectJwSignRecordHaiScore(jwSignRecord);

            // 如果有决赛， 获取决赛成绩
            if ("2".equals(jwGameItem.getMatchType())) {

                List<JwEight> jwEightList = getEightOrder(jwSignRecord.getGameItemId());

                if (jwEightList != null && jwEightList.size() > 0) {
                    for (int i = 0; i < jwEightList.size(); i++) {
                        JwEight jwEight = jwEightList.get(i);
                        list.forEach(item -> {
                            if (item.getId().equals(jwEight.getPlayerId())) {
                                item.setRankOrder(jwEight.getEightOrder());
                                item.setDescription(jwEight.getDescription());
                            }
                        });
                    }
                }
                list = list.stream().filter(jwSignRecord1 -> StringUtils.isLongNotNull(jwSignRecord1.getRankOrder())).collect(Collectors.toList());
                list.sort(Comparator.comparing(JwSignRecord::getRankOrder));
            }


            if (list != null && list.size() != 0) {
                list = list.stream().filter(jwSignRecord1 -> StringUtils.isLongNotNull(jwSignRecord1.getRankOrder())).collect(Collectors.toList());
                list.sort(Comparator.comparing(JwSignRecord::getRankOrder));
                int allSize = list.size();
                list.forEach(jwSignRecord1 -> {
                    if (StringUtils.isNotEmpty(jwGameItem.getResultDesId())) {
                        List<JwAwardsItem> jwAwardsItemList = jwAwardsItemService.selectJwAwardsItemListByIds(jwGameItem.getResultDesId().split(","));
                        JwAwardsItem awardsItem = getOrderDes(jwAwardsItemList, Long.valueOf(allSize), jwSignRecord1);
                        jwSignRecord1.setItemName(jwGameItem.getName());
                        if (awardsItem != null) {
                            jwSignRecord1.setGradeStr(JSONObject.toJSONString(awardsItem));
                            jwSignRecord1.setRankOrderDes(awardsItem.getRankText());
                        }
                    }
                });
            }
        }
//        list = list.stream().filter(jwSignRecord1 -> jwSignRecord1.getJwTeam().getTeamName().contains("充轻舞飞扬艺术")).collect(Collectors.toList());
        return list;
    }

    // 获取比赛全部成绩
    // 获取比赛 项目成绩
    public List<JwSignRecord> listAllGameItemGradeDes(Long matchId, Long gameItemId) {
        List<JwSignRecord> list = new ArrayList<>();
        if (StringUtils.isLongNotNull(matchId)) {
            List<JwGameItem> gameItemList = jwGameItemService.selectJwGameItemListByMatchId(matchId, gameItemId);
            if (gameItemList != null && gameItemList.size() > 0) {
                gameItemList.sort(Comparator.comparing(JwGameItem::getCode));
                gameItemList.forEach(jwGameItem -> {
                    // 获取海选成绩
                    JwSignRecord query = new JwSignRecord();
                    query.setGameItemId(jwGameItem.getId());
                    query.setMatchId(matchId);
                    List<JwSignRecord> jwSignRecordList = jwSignRecordService.selectJwSignRecordHaiScore(query);
                    if (jwSignRecordList != null && jwSignRecordList.size() > 0) {
                        // 如果有决赛， 获取决赛成绩
                        if ("2".equals(jwGameItem.getMatchType())) {

                            List<JwEight> jwEightList = getEightOrder(jwGameItem.getId());

                            if (jwEightList != null && jwEightList.size() > 0) {
                                for (int i = 0; i < jwEightList.size(); i++) {
                                    JwEight jwEight = jwEightList.get(i);
                                    jwSignRecordList.forEach(item -> {
                                        if (item.getId().equals(jwEight.getPlayerId())) {
                                            item.setRankOrder(jwEight.getEightOrder());
                                            item.setDescription(jwEight.getDescription());
                                        }
                                    });
                                }
                            }
                            jwSignRecordList = jwSignRecordList.stream().filter(jwSignRecord1 -> StringUtils.isLongNotNull(jwSignRecord1.getRankOrder())).collect(Collectors.toList());
                            jwSignRecordList.sort(Comparator.comparing(JwSignRecord::getRankOrder));
                        }

                        if (jwSignRecordList != null && jwSignRecordList.size() != 0) {
                            jwSignRecordList = jwSignRecordList.stream().filter(jwSignRecord1 -> StringUtils.isLongNotNull(jwSignRecord1.getRankOrder())).collect(Collectors.toList());
                            jwSignRecordList.sort(Comparator.comparing(JwSignRecord::getRankOrder));
                            int allSize = jwSignRecordList.size();
                            if (StringUtils.isNotEmpty(jwGameItem.getResultDesId())) {
                                List<JwAwardsItem> jwAwardsItemList = jwAwardsItemService.selectJwAwardsItemListByIds(jwGameItem.getResultDesId().split(","));
                                jwSignRecordList.forEach(jwSignRecord1 -> {
                                    JwAwardsItem awardsItem = getOrderDes(jwAwardsItemList, Long.valueOf(allSize), jwSignRecord1);
                                    jwSignRecord1.setJwGameItem(jwGameItem);
                                    jwSignRecord1.setItemName(jwGameItem.getName());
                                    if (awardsItem != null) jwSignRecord1.setRankOrderDes(awardsItem.getRankText());
                                });
                            }

                            jwSignRecordList.forEach(jwSignRecord1 -> {
                                jwSignRecord1.setJwGameItem(jwGameItem);
                            });
                        }
                        list.addAll(jwSignRecordList);
                    }
                });
            }
        }

        list.sort(Comparator.comparing(jwSignRecord -> jwMatchTeamService.getJwMatchTeam(matchId, jwSignRecord.getTeamId()).getIndexOrder()));
        return list;
    }


    // 获取代表队成绩数据统计
    public List<JwMatchTeamGrade> listTeamGradeDes(JwSignRecord jwSignRecord) {
        List<JwMatchTeamGrade> jwMatchTeamGradeList = new ArrayList<>();
        JwTeam queryT = new JwTeam();
        queryT.setMatchId(jwSignRecord.getMatchId());
        // 先生成基本数据
        List<JwTeam> jwTeamList = jwTeamMapper.selectJwTeamListWithMatch(queryT);
        if (jwTeamList != null && jwTeamList.size() > 0) {
            jwTeamList.forEach(jwTeam -> {
                if (jwSignRecord.getTeamId() == null || jwSignRecord.getTeamId() == 0l || jwSignRecord.getTeamId().equals(jwTeam.getId()))
                    jwMatchTeamGradeList.add(new JwMatchTeamGrade(jwTeam.getId(), jwTeam.getMatchId(), jwTeam.getIndexOrder(), new ArrayList<JwSignRecord>(), new ArrayList<JwAwardsItem>()));
            });
        }

        // 获取比赛的全部项目
        List<JwGameItem> gameItemList = jwGameItemService.selectJwGameItemListByMatchId(jwSignRecord.getMatchId(), null);
        if (gameItemList != null && gameItemList.size() > 0) {
            gameItemList.sort(Comparator.comparing(JwGameItem::getCode));

            // 获取项目的全部选手
            gameItemList.forEach(jwGameItem -> {
                if (StringUtils.isNotEmpty(jwGameItem.getResultDesId())) {
                    JwSignRecord queryJ = new JwSignRecord();
                    queryJ.setMatchId(jwGameItem.getMatchId());
                    queryJ.setGameItemId(jwGameItem.getId());

                    List<JwSignRecord> list = jwSignRecordService.selectJwSignRecordHaiScore(queryJ);

                    // 如果有决赛， 获取决赛成绩
                    if ("2".equals(jwGameItem.getMatchType())) {

                        List<JwEight> jwEightList = getEightOrder(jwGameItem.getId());

                        if (jwEightList != null && jwEightList.size() > 0) {
                            for (int i = 0; i < jwEightList.size(); i++) {
                                JwEight jwEight = jwEightList.get(i);
                                list.forEach(item -> {
                                    if (item.getId().equals(jwEight.getPlayerId())) {
                                        item.setRankOrder(jwEight.getEightOrder());
                                        item.setDescription(jwEight.getDescription());
                                    }
                                });
                            }
                        }
                        list = list.stream().filter(jwSignRecord1 -> StringUtils.isLongNotNull(jwSignRecord1.getRankOrder())).collect(Collectors.toList());
                        list.sort(Comparator.comparing(JwSignRecord::getRankOrder));
                    }

                    List<JwAwardsItem> jwAwardsItemList = jwAwardsItemService.selectJwAwardsItemListByIds(jwGameItem.getResultDesId().split(","));

                    if (list != null && list.size() != 0) {
                        list = list.stream().filter(jwSignRecord1 -> StringUtils.isLongNotNull(jwSignRecord1.getRankOrder())).collect(Collectors.toList());
                        list.sort(Comparator.comparing(JwSignRecord::getRankOrder));
                        int allSize = list.size();
                        list.forEach(jwSignRecord1 -> {

                            jwMatchTeamGradeList.forEach(jwMatchTeamGrade -> {
                                if (jwMatchTeamGrade.getTeamId().equals(jwSignRecord1.getTeamId())) {
                                    jwSignRecord1.setJwGameItem(jwGameItem);
                                    JwAwardsItem awardsItem = getOrderDes(jwAwardsItemList, Long.valueOf(allSize), jwSignRecord1);
                                    if (awardsItem != null) jwSignRecord1.setRankOrderDes(awardsItem.getRankText());

                                    // 获奖记录加进去
                                    List<JwSignRecord> jwSignRecordList = jwMatchTeamGrade.getJwSignRecordList();
                                    jwSignRecordList.add(jwSignRecord1);
                                    jwMatchTeamGrade.setJwSignRecordList(jwSignRecordList);

                                    if (awardsItem != null) {

                                        JwAwardsItem jwAwardsItem = jwAwardsItemService.selectJwAwardsItemById(awardsItem.getId());

                                        if (jwAwardsItem.getJiangBei().equals(99l)) jwAwardsItem.setJiangBei((long) jwSignRecord1.getJwSignRecordSportList().size());
                                        if (jwAwardsItem.getJiangPai().equals(99l)) jwAwardsItem.setJiangPai((long) jwSignRecord1.getJwSignRecordSportList().size());
                                        if (jwAwardsItem.getZhengShu().equals(99l)) jwAwardsItem.setZhengShu((long) jwSignRecord1.getJwSignRecordSportList().size());

                                        // 获奖统计加进去
                                        List<JwAwardsItem> awardsItemList = jwMatchTeamGrade.getAwardsItemList();
                                        Boolean hasA = false;
                                        for (JwAwardsItem awardsItemOld : awardsItemList) {
                                            if (awardsItemOld.getId().equals(jwAwardsItem.getId())) {
                                                hasA = true;
                                                awardsItemOld.setJiangBei(awardsItemOld.getJiangBei() + jwAwardsItem.getJiangBei());
                                                awardsItemOld.setJiangPai(awardsItemOld.getJiangPai() + jwAwardsItem.getJiangPai());
                                                awardsItemOld.setZhengShu(awardsItemOld.getZhengShu() + jwAwardsItem.getZhengShu());
                                                awardsItemOld.setCountNum(awardsItemOld.getCountNum() + 1);
                                            }
                                        }
                                        if (!hasA) {
                                            jwAwardsItem.setCountNum(1l);
                                            awardsItemList.add(jwAwardsItem);
                                        }
                                        jwMatchTeamGrade.setAwardsItemList(awardsItemList);
                                    }
                                }
                            });
//                            JwMatchTeamGrade jwMatchTeamGrade = jwMatchTeamGradeList.stream().filter(u -> u.getTeamId().equals(jwSignRecord1.getTeamId())).findFirst().orElse(null);
                        });
                    }
                }
            });
        }
        return jwMatchTeamGradeList;
    }

    private JwAwardsItem getOrderDes(List<JwAwardsItem> jwAwardsItemList, Long allSize, JwSignRecord jwSignRecord) {

        JwAwardsItem res = null;

        for (JwAwardsItem jwAwardsItem : jwAwardsItemList) {
            Long rankOrder = jwSignRecord.getRankOrder();
            // 处理固定名次
            if (jwAwardsItem.getRankStart().equals(jwAwardsItem.getRankEnd()) && rankOrder.equals(jwAwardsItem.getRankStart())) {
                res = jwAwardsItem;
            } else {
                // 首先判断名次区域在那个奖项项目
                if (rankOrder >= jwAwardsItem.getRankStart() && rankOrder <= jwAwardsItem.getRankEnd()) {

                    // 判断百分比要把开始名次之前的减掉算百分比
                    Long newAllSize = allSize - jwAwardsItem.getRankStart() + 1l;

                    Long rankEnd = jwAwardsItem.getRankEnd() - jwAwardsItem.getRankStart() + 1l;
                    if (newAllSize > rankEnd) {
                        newAllSize = rankEnd;
                    }

                    rankOrder = rankOrder - jwAwardsItem.getRankStart() + 1l;

                    // 然后判断名次区域百分比
                    Long proportionStart = new BigDecimal(newAllSize).multiply(jwAwardsItem.getProportionStart()).divide(new BigDecimal(100), 0, BigDecimal.ROUND_HALF_UP).longValue();
                    if (proportionStart <= 0) proportionStart = 1l;

                    if (new BigDecimal("0.00").compareTo(jwAwardsItem.getProportionStart()) == 0) {
                        proportionStart = 0l;
                    }
                    Long proportionEnd = new BigDecimal(newAllSize).multiply(jwAwardsItem.getProportionEnd()).divide(new BigDecimal(100), 0, BigDecimal.ROUND_HALF_UP).longValue();
                    if (newAllSize <= 3) {
                        proportionEnd = new BigDecimal(newAllSize).multiply(jwAwardsItem.getProportionEnd()).divide(new BigDecimal(100), 0, BigDecimal.ROUND_UP).longValue();
                    }

                    if (proportionEnd <= 0) proportionEnd = 1l;

                    if (new BigDecimal("100").compareTo(jwAwardsItem.getProportionEnd()) == 0) {
                        proportionEnd = newAllSize;
                    }

                    if (proportionStart.equals(proportionEnd) && rankOrder.equals(proportionStart)) {
                        res = jwAwardsItem;
                    } else if (rankOrder > proportionStart && rankOrder <= proportionEnd) {
                        res = jwAwardsItem;
                    }
                }
                if (res != null) {
                    break;
                }
            }
        }
        return res;
    }

    // 获取对阵排名
    public List<JwEight> getEightOrder(Long gameItemId) {

        List<JwEight> jwEightList = jwEightMapper.getEightOrder(gameItemId);

        ArrayList<JwEight> result = new ArrayList();
        if (jwEightList != null && jwEightList.size() > 3) {
            JwEight jwEight = jwEightList.get(3);
            jwEight.setEightOrder(1l);
            result.add(jwEight); // 冠军
        }
        if (jwEightList != null && jwEightList.size() > 2) {
            JwEight jwEight = jwEightList.get(2);
            jwEight.setEightOrder(2l);
            jwEight.setDescription("亚军");
            result.add(jwEight);
        }
        if (jwEightList != null && jwEightList.size() > 1) {
            JwEight jwEight = jwEightList.get(1);
            jwEight.setEightOrder(3l);
            jwEight.setDescription("季军");
            result.add(jwEight);
        }
        if (jwEightList != null && jwEightList.size() > 1) {
            JwEight jwEight = jwEightList.get(0);
            jwEight.setEightOrder(4l);
            jwEight.setDescription("四强");
            result.add(jwEight);
        }

        for (int i = 4; i < jwEightList.size(); i++) {
            JwEight jwEight = jwEightList.get(i);
            jwEight.setEightOrder(Long.valueOf(i + 1));
            result.add(jwEight);
        }

        return result;
    }
}