<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="比赛" prop="matchId">
        <ELSelectMatch :matchId.sync="queryParams.matchId"/>
      </el-form-item>
      <el-form-item label="组别" prop="gameItemId">
        <el-select style="width: 360px" filterable v-model="queryParams.gameItemId" placeholder="组别" clearable @change="gameItemChange">
          <el-option
            v-for="gameItem in JwGameItemList.filter(it=>it.signCount > 0)"
            :key="gameItem.id"
            :label="gameItem.code + ' : ' + gameItem.name + (gameItem.matchType == '2' ?  ' (晋级' + (gameItem.promotionNum || '') + ')' : '')"
            :value="gameItem.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-refresh" size="mini" @click="handleQuery">刷新</el-button>
      </el-form-item>
    </el-form>

    <el-row class="mb8" style="display: flex;flex-direction: row;flex-wrap: wrap;align-items: center;">
      <el-button type="warning" plain size="mini" @click="handleLockScore">锁定打分</el-button>
      <el-button type="primary" plain size="mini" @click="handleJiSuanGameItem('zhijiepingjun')">直接平均分</el-button>
      <el-button type="primary" plain size="mini" @click="handleJiSuanGameItem('zuigaozuidi')">去最高最低</el-button>
      <el-button type="primary" plain size="mini" @click="handleJiSuanGameItem('xiuzheng')">修正系数</el-button>
      <el-button type="success" plain size="mini" @click="printHaiScore">海选成绩</el-button>
      <el-divider direction="vertical" style="margin: 0 32px"></el-divider>
      <el-button type="warning" plain size="mini" v-if="currentGameItem.matchType == 2" @click="haiXuanComplete">海选完成</el-button>
      <el-divider direction="vertical" style="margin: 0 32px"></el-divider>
      <el-button type="success" plain size="mini" v-if="currentGameItem.matchType == 2" @click="printJinJiSport">晋级名单</el-button>
      <el-button type="success" plain size="mini" v-if="currentGameItem.matchType == 2" @click="printJinJiSportGrade">晋级成绩</el-button>
      <el-divider direction="vertical" style="margin: 0 32px"></el-divider>
      <div class="order-row">
        <el-input size="mini" v-model="minOrder"></el-input>
        -
        <el-input size="mini" v-model="maxOrder"></el-input>
      </div>
      <el-button type="primary" plain size="mini" @click="handleSendHaiXuanGrade(true)">指定名次</el-button>
      <el-button type="primary" plain size="mini" @click="handleSendHaiXuanGrade(false)">投屏成绩</el-button>
      <el-button type="success" plain size="mini" v-if="currentGameItem.matchType == 2" @click="handleSendJinJiSport">投屏晋级名单</el-button>
      <el-button type="warning" plain size="mini" v-if="currentGameItem.matchType == 2" @click="handleSendBattle">投屏对阵</el-button>
      <el-divider direction="vertical" style="margin: 0 32px"></el-divider>
      <el-button type="primary" plain size="mini" @click="handlePrintZhengshu">证书打印</el-button>

      <el-divider direction="vertical" style="margin: 0 32px"></el-divider>
      <el-button type="primary" plain size="mini" @click="handlePrintAllZhengshu">全部证书</el-button>
      <el-button type="primary" plain size="mini" @click="handlePrintAllGrage">全部成绩</el-button>
      <el-button type="primary" plain size="mini" @click="handleDownloadAllGrage">导出成绩</el-button>
      <el-button type="warning" plain size="mini" @click="handleUpLoadGrade">上传成绩</el-button>
      <el-divider direction="vertical" style="margin: 0 32px"></el-divider>
      <el-button type="warning" plain size="mini" @click="handleRendReload">刷新屏幕</el-button>
      <el-button type="danger" plain size="mini" @click="handleClearScreen">清屏</el-button>
    </el-row>
    <div class="box-card aa">
      <div class="score-con">
        <div class="box-card" v-for="item in JwHaiScoreList" style="margin-right: 12px;margin-bottom: 12px;">
          <div class="box-header" style="white-space: nowrap;">
            <span style="margin-right: 8px;">{{item.name}}</span>
            【审核：
            <el-switch style="margin-top: -4px;"
                       v-model="item.lockScore"
                       active-value="Y"
                       inactive-value="N"
                       @change="handleLockChange(item)"
            ></el-switch>
            】

          </div>
          <div class="score-items">
            <div class="score-item">
              <div class="si index-order">出场</div>
              <div class="si back-num">背号</div>
              <div class="si judge-name" v-for="judge in item.judgeList">{{judge.judgeName}}</div>
              <div class="si all-score">总分</div>
              <div class="si avg-score">得分</div>
              <div class="si rank">排名</div>
              <div class="si rank-str">奖项</div>
              <div class="si opt">操作</div>
            </div>
            <div class="score-item" v-for="sport in item.items">
              <div class="si index-order">{{sport.indexOrder}}</div>
              <div class="si back-num" :title="sport.teamName">{{sport.backNumber}}</div>
              <div class="si judge-name" v-for="judge in sport.jwHaiScoreList">
                <input :class="'score-input' + getScoreClass(judge.score, sport.jwHaiScoreList)" type="number" v-model="judge.score" @change="scoreChange(sport, judge)">
              </div>
              <div class="si all-score">{{sport.allScore || '-'}}</div>
              <div class="si avg-score">{{sport.avgScore || '-'}}</div>
              <div class="si rank">{{sport.rankOrder || '-'}}</div>
              <div class="si rank-str">{{sport.gradeStr ? JSON.parse(sport.gradeStr).awardName : "-"}}</div>
              <div class="si opt">
                <el-switch
                           v-model="sport.lockJudgeScore"
                           active-value="Y"
                           inactive-value="N"
                           :width="32"
                           @change="handleScoreLockChange(sport)"
                ></el-switch>
                <i class="el-icon-s-platform" @click="handleSendScoreScreen(sport)"></i>
              </div>
            </div>
          </div>

          <el-empty v-if="!item || !item.items || item.items.length == 0" description="暂无打分"></el-empty>
        </div>

      </div>
      <div class="rank-con">
        <div class="rank-item">
          <div class="si rank">排名</div>
          <div class="si back-num">背号</div>
          <div class="si avg-score">得分</div>
        </div>
        <div class="rank-item" v-for="item in sportRankList">
          <div class="si rank"><input class="score-input" type="number" v-model="item.rankOrder" @change="rankChange(item)"></div>
          <div class="si back-num">{{item.backNumber}}</div>
          <div class="si avg-score">{{item.avgScore || '-'}}</div>
        </div>
      </div>
    </div>

    <el-dialog title="晋级名单" :visible.sync="showJinJiSport" width="750px" center :append-to-body="false">
      <div class="btn-row">
        <el-button type="warning" size="mini" @click="handlePrint('printJinJiSport')">打印</el-button>
      </div>
      <div class="print-sport-items" id="printJinJiSport" v-if="showJinJiSport">
        <div class="match-name" v-html="currentGameItem.matchName"></div>
        <div class="game-item-name">{{currentGameItem.code}} {{currentGameItem.name}} 晋级名单</div>
        <div class="sport-item" v-if="matchConfig.grade">
          <div class="index-v h order" v-if="matchConfig.grade.rankOrder" >名次</div>
          <div class="index-v h grade" >成绩</div>
          <div class="index-v h back-num" v-if="matchConfig.grade.backNum">背号</div>
          <div class="index-v h sport" v-if="matchConfig.grade.sport">选手</div>
          <div class="index-v h team" v-if="matchConfig.grade.teamName">代表队</div>
        </div>
        <div class="sport-item" v-if="matchConfig.grade" v-for="item in signRecordList">
          <div class="index-v order" v-if="matchConfig.grade.rankOrder">{{item.rankOrder}}</div>
          <div class="index-v grade">{{item.avgScore}}</div>
          <div class="index-v back-num" v-if="matchConfig.grade.backNum">{{item.backNumber}}</div>
          <div class="index-v sport" v-if="matchConfig.grade.sport">{{item.jwSignRecordSportList.map(item => item.playerName).join(" ")}}</div>
          <div class="index-v team" v-if="matchConfig.grade.teamName">{{item.jwTeam.teamName}}</div>
        </div>
      </div>
    </el-dialog>

    <el-dialog title="晋级成绩" :visible.sync="showJinJiSportGrade" width="750px" center :append-to-body="false">
      <div class="btn-row">
        <el-button type="warning" size="mini" @click="handlePrint('jinJiSportGrade')">打印</el-button>
      </div>
      <div class="print-sport-items" id="jinJiSportGrade" v-if="signRecordJinJiGradeList">
        <div class="match-name" v-html="currentGameItem.matchName"></div>
        <div class="game-item-name">{{currentGameItem.code}} {{currentGameItem.name}} 晋级成绩</div>
        <div class="sport-item" v-if="matchConfig.grade">
          <div class="index-v h order">海选名次</div>
          <div class="index-v h grade">海选成绩</div>
          <div class="index-v h back-num">背号</div>
          <div class="index-v h sport">选手</div>
          <div class="index-v h team" v-if="matchConfig.grade.teamName">代表队</div>
          <div class="index-v h jinji">晋级</div>
        </div>
        <div class="sport-item" v-if="matchConfig.grade" v-for="item in signRecordJinJiGradeList">
          <div class="index-v order">{{item.rankOrder}}</div>
          <div class="index-v grade">{{item.avgScore}}</div>
          <div class="index-v back-num">{{item.backNumber}}</div>
          <div class="index-v sport">{{item.jwSignRecordSportList.map(item => item.playerName).join(" ")}}</div>
          <div class="index-v team" v-if="matchConfig.grade.teamName">{{item.jwTeam.teamName}}</div>
          <div class="index-v jinji">{{item.jinJiStr}}</div>
        </div>
      </div>
    </el-dialog>

    <el-dialog title="全部成绩" :visible.sync="showAllGrade" width="750px" center :append-to-body="false">
      <div class="btn-row">
        <el-button type="warning" size="mini" @click="handlePrint('haiScoreAll')">打印</el-button>
        <el-button type="warning" size="mini" @click="downloadAllScore()">下载</el-button>
      </div>
      <div class="print-sport-items" id="haiScoreAll" v-if="showAllGrade">
        <div class="match-name" v-if="allGrade && allGrade[0]" v-html="allGrade[0].gameItem.matchName"></div>
        <div style="margin-bottom: 56px" v-for="(itemmm, index) in allGrade">

          <div class="game-item-name">{{itemmm.gameItem.code}}:{{itemmm.gameItem.name}} 成绩</div>
          <div class="sport-item">
            <div class="index-v h order">名次</div>
            <div class="index-v h order-des">奖项</div>
            <!--<div class="index-v h grade" v-if="itemmm.gameItem.matchType == 1">成绩</div>-->
            <!--<div class="index-v h grade" v-if="itemmm.gameItem.matchType == 2">决赛成绩</div>-->
            <!--<div class="index-v h grade" v-if="itemmm.gameItem.matchType == 2">海选成绩</div>-->
            <div class="index-v h back-num">背号</div>
            <div class="index-v h sport" :class="{qiwu: itemmm.gameItem.sportLimit != 1}">选手</div>
            <div class="index-v h team">代表队</div>
          </div>
          <div class="sport-item" v-for="item in itemmm.grades">
            <div class="index-v order">{{item.rankOrder}}</div>
            <div class="index-v order-des">
              <!--<span v-if="item.rankOrder == 1">[冠军]</span>-->
              <!--<span v-if="item.rankOrder == 2">[亚军]</span>-->
              <!--<span v-if="item.rankOrder == 3">[季军]</span>-->
              {{item.rankOrderDes}}
            </div>
            <!--<div class="index-v h grade" v-if="itemmm.gameItem.matchType == 2">{{item.description}}</div>-->
            <!--<div class="index-v grade">{{item.avgScore}}</div>-->
            <div class="index-v back-num">{{item.backNumber}}</div>
            <div class="index-v sport" :class="{qiwu: itemmm.gameItem.sportLimit != 1}">{{item.jwSignRecordSportList.map(item => item.playerName).join(" ")}}</div>
            <div class="index-v team">{{item.jwTeam.teamName}}</div>
          </div>
        </div>

      </div>
    </el-dialog>

    <el-dialog title="海选成绩" :visible.sync="showHaiGrade" width="750px" center :append-to-body="false">
      <div class="btn-row">
        <el-button type="primary" size="mini" @click="downloadGameScore()">下载</el-button>
        <el-button type="warning" size="mini" @click="handlePrint('haiScore')">打印</el-button>
      </div>
      <div class="print-sport-items" id="haiScore" v-if="showHaiGrade">
        <div class="match-name" v-html="currentGameItem.matchName"></div>
        <div class="game-item-name">{{currentGameItem.code}} {{currentGameItem.name}} 成绩</div>
        <div class="sport-item" v-if="matchConfig.grade">
          <div class="index-v h order" v-if="matchConfig.grade.rankOrder" >名次</div>
<!--          <div class="index-v h order-des">奖项</div>-->

<!--          <div class="index-v h grade" v-if="matchConfig.grade.rankOrderDes">奖项</div>-->

          <div class="index-v h grade" >成绩</div>

<!--          <div class="index-v h grade" v-if="currentGameItem.matchType == 2">决赛成绩</div>-->
<!--          <div class="index-v h grade" v-if="currentGameItem.matchType == 2">海选成绩</div>-->

          <div class="index-v h back-num" v-if="matchConfig.grade.backNum">背号</div>
          <div class="index-v h sport" v-if="matchConfig.grade.sport" :class="{qiwu: currentGameItem.sportLimit != 1}">选手</div>
          <div class="index-v h team" v-if="matchConfig.grade.teamName">代表队</div>
        </div>
        <div class="sport-item" v-if="matchConfig.grade" v-for="item in signRecordJinJiGradeList">
          <div class="index-v order" v-if="matchConfig.grade.rankOrder">{{item.rankOrder}}</div>
<!--      <div class="index-v order-des">{{item.rankOrderDes}}</div>-->
          <!--<div class="index-v order-des"></div>-->

          <div class="index-v h grade" >{{item.rankOrderDes || '-'}}</div>

<!--          <div class="index-v h grade" v-if="currentGameItem.matchType == 2">{{item.description}}</div>-->
<!--          <div class="index-v grade">{{item.avgScore}}</div>-->

          <div class="index-v back-num" v-if="matchConfig.grade.backNum">{{item.backNumber}}</div>
          <div class="index-v sport" v-if="matchConfig.grade.sport" :class="{qiwu: currentGameItem.sportLimit != 1}">{{item.jwSignRecordSportList.map(item => item.playerName).join(" ")}}</div>
          <div class="index-v team" v-if="matchConfig.grade.teamName">{{item.jwTeam.teamName}}</div>
        </div>
      </div>
    </el-dialog>

    <el-dialog title="证书打印" :visible.sync="showZhengShu" width="750px" center :append-to-body="false">
      <div class="btn-row">
        <el-button type="warning" size="mini" @click="handlePrint('zhengshu')">打印</el-button>
      </div>
      <div class="print-con-zhengshu" id="zhengshu" v-if="showZhengShu">
        <div class="page-con-box" :style="{height: item.jwSignRecordSportList.length * 1005 + 'px'} " v-for="item in signRecordJinJiGradeList">
          <div class="page-con" v-for="itemm in item.jwSignRecordSportList">
            <div class="row-item name">
              <div class="item-label">姓名</div>
              <div class="colon">:</div>
              <div class="item-value">{{itemm.playerName}}</div>
            </div>
            <div class="row-item gameItemName">
              <div class="item-label">组别</div>
              <div class="colon">:</div>
<!--              <div class="item-value" >{{item.itemName.split(":")[1]}}</div>-->
              <div class="item-value" :class="{small: item.jwGameItem.name.length > 13 , smalll: item.jwGameItem.name.length > 16}">{{item.jwGameItem.name}}</div>
            </div>
            <div class="row-item order">
              <div class="item-label">名次</div>
              <div class="colon">:</div>
              <div class="item-value">{{item.rankOrderDes}}</div>
            </div>
            <div class="row-item team">
              <div class="item-label">代表队</div>
              <div class="colon">:</div>
              <div class="item-value" :class="{small: item.jwTeam.teamName.length > 13, smalll: item.jwTeam.teamName.length >= 105 } ">{{item.jwTeam.teamName}}</div>
            </div>

<!--            <div class="row-item team">-->
<!--              <div class="item-label">时间</div>-->
<!--              <div class="colon">:</div>-->
<!--              <div class="item-value">二〇二六年五月一日</div>-->
<!--            </div>-->
<!--            <div class="row-item team">-->
<!--              <div class="item-label">地点</div>-->
<!--              <div class="colon">:</div>-->
<!--              <div class="item-value">四川 · 遂宁</div>-->
<!--            </div>-->
          </div>
        </div>
      </div>
    </el-dialog>

    <el-dialog title="成绩上传" :visible.sync="showUpLoad" center :append-to-body="false">
      <div class="btn-row">
        <el-button type="warning" size="mini" @click="handleUpLoadGradeOpt()">上传</el-button>
      </div>
      <div class="upload-box">
        <el-table
          center
          :data="mergedComparisonList"
          style="width: 100%; "
          border
          size="mini"
          :row-class-name="getRowClass"
        >
          <!-- 背号 (作为主键) -->
          <el-table-column align="center" prop="backNumber" label="背号" width="64"/>
          <el-table-column align="center" label="ID" width="64">
            <template slot-scope="scope">
              <div class="comparison-cell">
                <div :class="['side', scope.row.offline.id !== scope.row.online.id ? 'diff' : '']">
                  {{ scope.row.offline.id }}
                </div>
                <div :class="['side', scope.row.offline.id !== scope.row.online.id ? 'diff' : '']">
                  {{ scope.row.online.id }}
                </div>
              </div>
            </template>
          </el-table-column>
          <!-- 队伍名称对比 -->
          <el-table-column align="center" label="队伍名称">
            <template slot-scope="scope">
              <div class="comparison-cell">
                <!-- 线下数据 -->
                <div :class="['side', scope.row.offline.teamName !== scope.row.online.teamName ? 'diff' : '']">
                  {{ scope.row.offline.teamName }}
                </div>

                <!-- 线上数据 -->
                <div :class="['side', scope.row.offline.teamName !== scope.row.online.teamName ? 'diff' : '']">
                  {{ scope.row.online.teamName }}
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column prop="offline.allScore" width="88" align="center" label="总分"/>
          <el-table-column prop="offline.avgScore" width="88" align="center" label="平均分"/>
          <el-table-column prop="offline.rankOrder" width="88" align="center" label="排名"/>
          <el-table-column prop="offline.indexOrder" width="88" align="center" label="序号"/>

          <el-table-column label="状态" align="center" width="100">
            <template slot-scope="scope">
              <el-tag
                size="mini"
                :type="scope.row.status === 'added' ? 'success' :
                   scope.row.status === 'removed' ? 'danger' : 'warning'"
              >
                {{ scope.row.statusText }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {updateJwSignRecord} from "@/api/jiewu/JwSignRecord";
  import {getJwMatch} from "@/api/jiewu/jwMatch";
  import {updateJwScheduleItem} from "@/api/jiewu/JwScheduleItem";
  import 'core-js/actual/array/group';
  import {lockJwGameItem, listJwGameItem, uploadGameItemGrade, getGameItemGradeComPar} from "@/api/jiewu/JwGameItem";
  import {listJwHaiScore, getJwHaiScore, delJwHaiScore, addJwHaiScore, updateJwHaiScore, jiSuanGameItem, saveCustomOrder, haiXuanComplete, listGameItemGradeDes, listAllGameItemGradeDes} from "@/api/jiewu/JwHaiScore";
  import {sendBattle, sendHaiXuanGrade, sendJinJiSport, sendScoreScreen} from "@/api/jiewu/ScreenSend";
  import {sendMusic, sendScreenOpt} from "@/api/jiewu/ScreenSend";
  import {listJwSchedulePlaceWithScheduleItem} from "@/api/jiewu/JwSchedulePlace";
  import {listJwScheduleInfo} from "@/api/jiewu/JwScheduleInfo";

  export default {
    name: "JwHaiScore",
    data() {
      return {
        match:{},
        matchConfig:{},
        minOrder: 1,
        maxOrder: 999,
        allGrade: [],
        showAllGrade: false,
        showZhengShu: false,
        showJinJiSport: false,
        showJinJiSportGrade: false,
        showHaiGrade: false,
        currentTab: "",
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        showUpLoad: false,
        upJwSignRecordList: [],
        upJwSignRecordListOnline: [],
        // 总条数
        total: 0,
        // 海选打分表格数据
        JwHaiScoreList: [],
        sportRankList: [],
        signRecordList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        JwGameItemList: [],
        signRecordJinJiGradeList: [],
        currentGameItem: {},
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          matchId: (this.Cookies.get("matchId") * 1) || null,
          gameItemId: null,
          scheduleItemId: null,
          sportId: null,
          judgeId: null,
          score: null,
          scoreType: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {}
      };
    },
    created() {
      this.getGameItemList()
      this.getList();
      this.getMatchInfo()
    },
    computed: {
      mergedComparisonList() {
        // 1. 创建 Map 便于查找
        const offlineMap = new Map(this.upJwSignRecordList.map(item => [item.backNumber, item]));
        const onlineMap = new Map(this.upJwSignRecordListOnline.map(item => [item.backNumber, item]));

        // 2. 合并所有出现过的 backNumber
        const allBackNumbers = new Set([...offlineMap.keys(), ...onlineMap.keys()]);
        const mergedList = [];

        for (const backNumber of allBackNumbers) {
          const offlineItem = offlineMap.get(backNumber) || {};
          const onlineItem = onlineMap.get(backNumber) || {};

          let status = 'normal';
          if (!offlineMap.has(backNumber)) status = 'added';
          else if (!onlineMap.has(backNumber)) status = 'removed';
          else if (offlineItem.id !== onlineItem.id || offlineItem.teamName !== onlineItem.teamName) {
            status = 'modified';
          }

          mergedList.push({
            backNumber,
            rankOrder: onlineItem.rankOrder || offlineItem.rankOrder || '-', // 优先使用线上排名，若无则用线下
            offline: {...offlineItem},
            online: {...onlineItem},
            status,
            statusText: status === 'added' ? '新增' : status === 'removed' ? '已删除' : status === 'modified' ? '已修改' : '正常'
          });
        }

        // 3. 按 backNumber 排序
        return mergedList.sort((a, b) => {
          // 将 rankOrder 转为数字进行比较，处理可能的字符串或缺失值
          const rankA = parseInt(a.rankOrder, 10) || 9999; // 若无效，排到最后
          const rankB = parseInt(b.rankOrder, 10) || 9999;
          return rankA - rankB;
        });
        // return mergedList.sort((a, b) => a.backNumber.localeCompare(b.backNumber, 'zh'));
      }
    },
    watch: {
      "queryParams.matchId": function (val) {
        this.getMatchInfo();
        this.getGameItemList();
      },
    },
    methods: {
      // 下载全部成绩表
      downloadAllScore(){
        this.download('jiewu/JwHaiScore/downloadAllScore', {matchId: this.queryParams.matchId}, `全部成绩表.docx`)
      },
      // 下载项目成绩
      downloadGameScore(){
        this.download('jiewu/JwHaiScore/downloadGameItemScore',
          {matchId: this.queryParams.matchId, gameItemId: this.currentGameItem.id},
          `${this.currentGameItem.code} ${this.currentGameItem.name} 成绩.docx`);
      },
      getScoreClass(score, jwHaiScoreList) {
        if (score && jwHaiScoreList && jwHaiScoreList.length > 1) {
          const minScore = [...jwHaiScoreList].filter(aa => aa.score).sort((a, b) => a.score * 1 - b.score * 1)[0];
          const maxScore = [...jwHaiScoreList].filter(aa => aa.score).sort((b, a) => a.score * 1 - b.score * 1)[0];

          if (score - minScore.score === 0) {
            return ' min';
          }
          if (score - maxScore.score === 0) {
            return ' max';
          }
        }

        return '';
      },
      handleClearScreen(item) {
        sendMusic({worksMusic: "", matchId: this.queryParams.matchId}).then(res => {
          this.$modal.msgSuccess("发送成功");
        })
      },
      handleRendReload() {
        sendScreenOpt({opt: "rendReload", matchId: this.queryParams.matchId}).then(res => {
          this.$modal.msgSuccess("发送成功");
        })
      },
      // 打印全部成绩
      handlePrintAllGrage() {
        let allGrade = [];
        // this.JwGameItemList.forEach(item => {
        //   listGameItemGradeDes({
        //     matchId: (this.Cookies.get("matchId") * 1) || null,
        //     gameItemId: item.id,
        //   },).then(res => {
        //     allGrade.push({gameItem: item, grades: ([].concat(res.data || []).filter(item => item.rankOrder).sort((a, b) => a.rankOrder - b.rankOrder))})
        //   })
        // })
        this.allGrade = allGrade;
        this.showAllGrade = true;

      },
      // 打印全部证书
      handlePrintAllZhengshu() {
        listAllGameItemGradeDes({matchId: this.queryParams.matchId}).then(res => {
          this.signRecordJinJiGradeList = [].concat(res.data || []).filter(item => (item.rankOrder && item.rankOrderDes));
          this.showZhengShu = true;
        })
      },
      // 导出全部成绩
      handleDownloadAllGrage() {
        this.download('jiewu/JwHaiScore/exportAllGrade', {
          ...this.queryParams
        }, `全部成绩表.xlsx`)
      },
      handleUpLoadGradeOpt() {
        let that = this;
        uploadGameItemGrade({gameItemId: this.queryParams.gameItemId}).then(res => {
          that.showUpLoad = false;
          that.$modal.msgSuccess("上传成功");
        }, err => {
          this.$modal.msgError(err);
        });
      },
      // 上传组别的成绩
      handleUpLoadGrade() {
        let that = this;
        if (this.queryParams.gameItemId) {
          getGameItemGradeComPar({gameItemId: this.queryParams.gameItemId}).then(res => {
            if (res.data) {
              that.showUpLoad = true;
              that.upJwSignRecordList = res.data.jwSignRecordList || [];
              that.upJwSignRecordListOnline = res.data.jwSignRecordListOnline || [];
            }
          })
        } else {
          this.$modal.msgWarning("先选择组别");
        }
      },
      getRowClass({row, column}, field, source) {
        if (row.status === 'added') return 'row-added';
        if (row.status === 'removed') return 'row-removed';
        if (row.status === 'modified') return 'row-modified';
        return '';
      },
      // 打印证书
      handlePrintZhengshu() {
        listGameItemGradeDes(this.queryParams).then(res => {
          this.signRecordJinJiGradeList = [].concat(res.data || []).filter(item => item.rankOrder).sort((a, b) => a.rankOrder - b.rankOrder);
          this.showZhengShu = true;
        })
      },
      // 投屏晋级名单
      handleSendJinJiSport() {
        sendJinJiSport({gameItemId: this.queryParams.gameItemId, matchId: this.queryParams.matchId}).then(res => {
          this.$modal.msgSuccess("发送成功");
        })
      },
      // 投屏一个选手的打分详情
      handleSendScoreScreen(sport){
        sendScoreScreen({jwSignRecordId: sport.id, matchId: this.queryParams.matchId}).then(res => {
          this.$modal.msgSuccess("发送成功");
        })
      },
      // 投屏成绩
      handleSendHaiXuanGrade(zhiDing) {
        let minOrder = 1, maxOrder = 999;
        if (zhiDing) {
          minOrder = this.minOrder;
          maxOrder = this.maxOrder;
        }
        sendHaiXuanGrade({minOrder: minOrder, maxOrder: maxOrder, gameItemId: this.queryParams.gameItemId, matchId: this.queryParams.matchId}).then(res => {
          this.$modal.msgSuccess("发送成功");
        })
      },
      // 投屏对阵
      handleSendBattle() {
        sendBattle({gameItemId: this.queryParams.gameItemId, matchId: this.queryParams.matchId}).then(res => {
          this.$modal.msgSuccess("发送成功");
        })
      },
      handleScoreLockChange(sport){
        updateJwSignRecord({id: sport.id, lockJudgeScore: sport.lockJudgeScore}).then(res=>{
          this.getList();
        });
      },
      handleLockChange(item) {
        updateJwScheduleItem({id: item.scheduleItemId, lockScore: item.lockScore}).then(res => {
          this.getList();
        });
      },
      // 海选完成
      haiXuanComplete() {
        let that = this;
        if (this.queryParams.gameItemId) {
          this.$confirm('确认海选完成并重新生成对阵表？？？？').then(_ => {
            haiXuanComplete({id: this.queryParams.gameItemId}).then(res => {
              that.$modal.msgSuccess("对阵生成完成");
            })
          }).catch(_ => {
          });
        }
      },
      // 打印海选成绩
      printHaiScore() {
        let that = this;
        listGameItemGradeDes(this.queryParams).then(res => {
          this.signRecordJinJiGradeList = [].concat(res.data || []).filter(item => item.rankOrder).sort((a, b) => a.rankOrder - b.rankOrder);
          that.showHaiGrade = true;
        })
      },
      // 打印晋级成绩
      printJinJiSportGrade() {
        let that = this;
        this.getList().then(res => {
          that.showJinJiSportGrade = true;
        })
      },
      // 打印晋级名单
      printJinJiSport() {
        let that = this;
        this.getList().then(res => {
          that.showJinJiSport = true;
        })
      },
      gameItemChange() {
        let currentGameItem = this.JwGameItemList.find((item) => item.id == this.queryParams.gameItemId);
        this.currentGameItem = currentGameItem;
        this.getList();
      },
      // 修改排名
      rankChange(item) {
        saveCustomOrder({id: item.id, rankOrder: item.rankOrder}).then(res => {
          this.getList();
        })

      },
      // 锁定打分
      handleLockScore() {
        if (this.queryParams.gameItemId) {
          lockJwGameItem({id: this.queryParams.gameItemId}).then(res => {
            this.getList();
          });
        }
      },
      // 计算成绩
      handleJiSuanGameItem(type) {
        let that = this;
        if (that.queryParams.gameItemId) {
          that.$modal.confirm('是否确认重新计算成绩').then(function () {
            return jiSuanGameItem({gameItemId: that.queryParams.gameItemId, type: type});
          }).then(() => {
            that.getList();
            that.$modal.msgSuccess("计算完成");
          }).catch(() => {
          });
        }
      },
      scoreChange(sport, judge) {

        addJwHaiScore({sportId: sport.id, judgeId: judge.judgeId, score: judge.score}).then(res => {

        })
        console.log(sport, judge)
      },
      getGameItemName(row) {
        let item = this.JwGameItemList.find((item) => item.id == row.gameItemId);
        return item ? item.code + ":" + item.name : "";
      },
      getGameItemList() {
        // let that = this;
        // listJwScheduleInfo(this.queryParams).then(response => {
        //
        //   (response.rows || []).forEach(item => {
        //
        //     listJwSchedulePlaceWithScheduleItem({matchId: this.queryParams.matchId, scheduleInfoId: item.id}).then(res => {
        //
        //       console.log(res)
        //
        //       that.$forceUpdate()
        //     })
        //   })
        // });


        listJwGameItem({matchId: this.queryParams.matchId, pageNum: 1, pageSize: 5000}).then(response => {
          this.JwGameItemList = response.rows;
        });
      },
      /** 查询海选打分列表 */
      getList() {
        let that = this;
        this.loading = true;
        this.JwHaiScoreList = [];
        return new Promise((resolve, reject) => {
          listJwHaiScore(this.queryParams).then(response => {
            let JwHaiScoreList = response.data || [];

            // 排名数据
            this.sportRankList = [].concat(JwHaiScoreList).sort((a, b) => a.rankOrder - b.rankOrder).filter(item => item.rankOrder);

            // 晋级名单数据
            this.signRecordList = [].concat(JwHaiScoreList).filter(item => (item.rankOrder && item.rankOrder <= that.currentGameItem.promotionNum)).sort((a, b) => a.rankOrder - b.rankOrder);

            // 晋级成绩
            this.signRecordJinJiGradeList = [].concat(JwHaiScoreList).filter(item => (item.rankOrder)).sort((a, b) => a.rankOrder - b.rankOrder);
            this.signRecordJinJiGradeList.forEach(item => {
              if (item.rankOrder <= that.currentGameItem.promotionNum) {
                item.jinJiStr = `晋级${that.currentGameItem.promotionNum}强`;
              } else {
                item.jinJiStr = "未晋级";
              }
              item.rankOrderDes = this.getOrderDesc(that.signRecordJinJiGradeList.length, that.currentGameItem.name, item.rankOrder)
            });

            if (JwHaiScoreList && JwHaiScoreList.length > 0) {
              JwHaiScoreList = JwHaiScoreList.group((b) => b.itemName);
            }

            Object.keys(JwHaiScoreList).forEach(key => {
              let sportItems = JwHaiScoreList[key] || [];

              // 一个项目的全部打分裁判
              let judgeList = [];
              sportItems.forEach(ss => {
                if (ss && ss.jwHaiScoreList && ss.jwHaiScoreList.length > 0) {
                  ss.jwHaiScoreList.forEach(s => {
                    if (judgeList.findIndex(ju => ju.judgeId == s.judgeId) < 0) {
                      judgeList.push({judgeId: s.judgeId, judgeName: s.judgeName});
                    }
                  });
                }
              });
              judgeList.sort((a, b) => a.judgeId - b.judgeId);

              sportItems.forEach(item => {
                let itemJudges = [];
                judgeList.forEach(judge => {
                  let score = item.jwHaiScoreList[item.jwHaiScoreList.findIndex(ju => ju.judgeId == judge.judgeId)];
                  if (score) {
                    itemJudges.push({id: score.id, judgeId: score.judgeId, score: score.score})
                  } else {
                    itemJudges.push({id: null, judgeId: judge.judgeId, score: ""})
                  }
                });
                item.jwHaiScoreList = itemJudges;
              });

              let scheduleItem = {};
              scheduleItem.name = key;
              scheduleItem.items = sportItems;
              scheduleItem.judgeList = judgeList;
              scheduleItem.lockScore = sportItems[0].jwScheduleItem.lockScore;
              scheduleItem.scheduleItemId = sportItems[0].jwScheduleItem.id;
              that.JwHaiScoreList.push(scheduleItem);
            });
            that.JwHaiScoreList.sort((a, b) => a.name.localeCompare(b.name));

            this.loading = false;
            resolve()
          });
        });
      },
      getMatchInfo() {
        getJwMatch(this.queryParams.matchId).then(response => {
          this.match = response.data || {};
          this.matchConfig = JSON.parse(this.match.matchConfig || "{}");
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: null,
          matchId: null,
          gameItemId: null,
          scheduleItemId: null,
          sportId: null,
          judgeId: null,
          score: null,
          scoreType: null,
          createTime: null
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加海选打分";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getJwHaiScore(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改海选打分";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateJwHaiScore(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addJwHaiScore(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$modal.confirm('是否确认删除海选打分编号为"' + ids + '"的数据项？').then(function () {
          return delJwHaiScore(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('jiewu/JwHaiScore/export', {
          ...this.queryParams
        }, `JwHaiScore_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
<style lang="scss" scoped>
  .order-row {
    /*width: 120px;*/
    display: flex;
    flex-direction: row;
    align-items: center;

    .el-input {
      width: 24pt;

      ::v-deep .el-input__inner {
        padding: 0 4px;
      }
    }
  }

  .print-con-zhengshu .item-label, .colon {
    /*visibility: hidden;*/
    font-weight: 900;
    line-height: 100px;
  }

  .item-value {
    /*border-bottom: none !important;*/
  }

  .print-con-zhengshu {
    font-size: 26px;
    padding-left: 175px;
    padding-right: 35px;
    color: #000;

    .page-con-box {
      page-break-before: always;
      /*page-break-after:always;*/
    }

    .page-con {
      padding-top: 240px;
      font-family: '华文中宋';
      height: 900px;
      page-break-before: always;
      /*page-break-after:always;*/
      .row-item {
        height: 76px;
        line-height: 120px;
        display: flex;
        flex-direction: row;
        white-space: nowrap;

        &.name .item-value {
          padding-left: 30px;
          letter-spacing: 30px;
        }

        &.order .item-value {
          padding-left: 30px;
          letter-spacing: 30px;
        }
      }

      .item-label {
        text-justify: distribute-all-lines;
        float: left;
        text-align: justify;
        text-align-last: justify;
        width: 102px;
        margin-right: 6px;
        white-space: nowrap;
        font-weight: 900;
        line-height: 100px;
        /*visibility: hidden;*/
      }

      .item-value {
        flex: 1;
        text-align: center;
        margin-left: 24px;
        border-bottom: 2px solid #000;
        padding-bottom: 6px;
        white-space: nowrap;
        color: #000;
        font-weight: 900;
        line-height: 100px;

        &.small {
          font-size: 22px;
        }

        &.smalll {
          font-size: 20px;
          white-space: normal;
          line-height: 22px;
          display: flex;
          align-items: end;
        }
      }
    }
  }

  .btn-row {
    text-align: right;
  }

  .print-sport-items {
    font-family: "华文中宋";
    color: #333;

    .match-name {
      text-align: center;
      font-size: 24px;
      font-weight: 600;
    }

    .game-item-name {
      text-align: center;
      font-size: 20px;
      font-weight: 600;
      margin-bottom: 12px;
      margin-top: 4px;
    }

    .sport-item {
      min-height: 28px;
      font-size: 16px;
      display: flex;
      flex-direction: row;
      border-top: 1px solid #333;
      page-break-inside: avoid;

      &:last-child {
        border-bottom: 1px solid #333;
      }

      .index-v {
        display: flex;
        align-items: center;
        justify-content: center;
        border-left: 1px solid #333;

        &:last-child {
          border-right: 1px solid #333;
        }

        &.h {

        }

        &.order {
          width: 68px;
        }

        &.order-des {
          width: 92px;
        }

        &.grade {
          width: 88px;
        }

        &.back-num {
          width: 64px;
        }

        &.sport {
          flex: 1;
          word-break: keep-all;
          width: 96px;
          text-align: center;

          &.qiwu {
            width: 220px;
            padding-left: 4px;
            padding-right: 4px;
          }
        }

        &.team {
          width: 120px;
          flex: 1;
          padding: 0 8px;
        }

        &.jinji {
          width: 100px;
        }
      }
    }
  }

  .el-divider {
    margin: 0 16px;
  }

  .box-card {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    padding: 16px;
    border-radius: 4px;

    .box-header {
      padding: 0px 12px 12px 12px;
      border-bottom: 1px solid #ebeef5;
    }

    &.aa {
      display: flex;
      flex-direction: row;
      height: calc(100vh - 224px);
      overflow: auto;
      padding: 0;

    }
  }

  .rank-con {
    width: 240px;
    display: flex;
    flex-direction: column;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    padding: 8px 16px;
    overflow: auto;

    .rank-item {
      display: flex;
      flex-direction: row;

      &:first-child .si {
        border-top: 1px solid #ddd;
      }

      .rank {
        width: 48px;
        min-width: 48px;

        input {
          width: calc(100% - 2px);
          border: none;
          height: calc(100% - 2px);
          text-align: center;
          outline: none;
          font-size: 14px;

          &:focus {
            border: 2px solid #67C23A;
          }

          &::-webkit-inner-spin-button,
          &::-webkit-outer-spin-button {
            -webkit-appearance: none;
            margin: 0;
          }
        }
      }

      .back-num {
        width: 56px;
        min-width: 56px;
      }

      .avg-score {
        width: 80px;
        min-width: 80px;
      }

    }
  }

  .score-con {
    width: 0;
    flex: 1;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    overflow: auto;
    padding: 8px;
  }

  .si {
    border-left: 1px solid #ddd;
    border-bottom: 1px solid #ddd;
    text-align: center;
    height: 32px;
    line-height: 32px;

    &:last-child {
      border-right: 1px solid #ddd;
    }
  }

  .score-items {
    display: flex;
    flex-direction: column;
    font-size: 14px;

    .score-item {
      display: flex;
      flex-direction: row;

      &:first-child .si {
        border-top: 1px solid #ddd;
      }

      .index-order {
        width: 48px;
        min-width: 48px;
      }

      .back-num {
        width: 56px;
        min-width: 56px;
      }

      .judge-name {
        width: 64px;
        min-width: 64px;

        input {
          width: calc(100% - 2px);
          border: none;
          height: calc(100% - 2px);
          text-align: center;
          outline: none;
          font-size: 14px;

          &:focus {
            border: 2px solid #67C23A;
          }

          &::-webkit-inner-spin-button,
          &::-webkit-outer-spin-button {
            -webkit-appearance: none;
            margin: 0;
          }

          &.min {
            color: #ff4949;
          }

          &.max {
            color: #1890ff;
          }
        }
      }

      .all-score {
        width: 80px;
        min-width: 80px;
      }

      .avg-score {
        width: 80px;
        min-width: 80px;
      }

      .rank {
        width: 56px;
        min-width: 56px;
      }
      .rank-str{
        width: 80px;
        min-width: 80px;
      }
      .opt{
        width: 80px;
        min-width: 80px;
        display: flex;
        align-items: center;
        justify-content: space-around;
        i{
          cursor: pointer;
          font-size: 26px;
          color: #409EFF;
        }
      }

    }
  }

  .upload-box {
    ::v-deep .el-table--mini .el-table__cell {
      padding: 0;
    }

    .comparison-cell {
      display: flex;
      flex-direction: column;
    }

    .comparison-cell .side {
      flex: 1;
      line-height: 20px;
      text-align: center;
      border-radius: 3px;
    }

    .comparison-cell .diff {
      background-color: #ffeb3b !important;
      font-weight: bold;
    }

    .comparison-cell .separator {
      color: #999;
      font-weight: bold;
    }

    /* 行样式 */
    .row-added {
      background-color: #e8f5e8 !important;
    }

    .row-removed {
      background-color: #ffebee !important;
      text-decoration: line-through;
    }

    .row-modified {
      background-color: #fff3e0 !important;
    }
  }
</style>
