<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="比赛" prop="matchId">
        <ELSelectMatch :matchId.sync="queryParams.matchId"/>
      </el-form-item>
      <el-form-item label="序号" prop="indexOrder">
        <el-input
          v-model="queryParams.indexOrder"
          placeholder="请输入序号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名字" prop="teamName">
        <el-input
          v-model="queryParams.teamName"
          placeholder="代表队名字"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--<el-col :span="1.5">-->
        <!--<el-button-->
          <!--type="primary"-->
          <!--plain-->
          <!--icon="el-icon-plus"-->
          <!--size="mini"-->
          <!--@click="handleAdd"-->
          <!--v-hasPermi="['jiewu:JwMatchTeam:add']"-->
        <!--&gt;新增-->
        <!--</el-button>-->
      <!--</el-col>-->
      <!--<el-col :span="1.5">-->
        <!--<el-button-->
          <!--type="success"-->
          <!--plain-->
          <!--icon="el-icon-edit"-->
          <!--size="mini"-->
          <!--:disabled="single"-->
          <!--@click="handleUpdate"-->
          <!--v-hasPermi="['jiewu:JwMatchTeam:edit']"-->
        <!--&gt;修改-->
        <!--</el-button>-->
      <!--</el-col>-->
      <!--<el-col :span="1.5">-->
        <!--<el-button-->
          <!--type="danger"-->
          <!--plain-->
          <!--icon="el-icon-delete"-->
          <!--size="mini"-->
          <!--:disabled="multiple"-->
          <!--@click="handleDelete"-->
          <!--v-hasPermi="['jiewu:JwMatchTeam:remove']"-->
        <!--&gt;删除-->
        <!--</el-button>-->
      <!--</el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['jiewu:JwMatchTeam:export']"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleBeiHaoExport"
          v-hasPermi="['jiewu:JwMatchTeam:export']"
        >全部背号
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleDownLoadAllScheduleInfo"
          v-hasPermi="['jiewu:JwMatchTeam:export']"
        >全部赛程
        </el-button>
      </el-col>


      <right-toolbar :showSearch.sync="showSearch" @queryTable="getTeamList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="JwTeamList" :summary-method="getSummaries" show-summary>
      <!--<el-table-column type="selection" width="55" align="center"/>-->
      <el-table-column label="ID" width="64" align="center" prop="id"/>
      <el-table-column label="序号" width="64" align="center" prop="indexOrder"/>
      <el-table-column label="代表队" align="left" prop="teamName"/>

      <el-table-column label="单人" width="120" align="center" prop="singleCount">
        <template slot-scope="scope">
          <el-link :underline="false" type="success" style="font-weight: 600">{{scope.row.singleCount||0}} 人</el-link>
        </template>
      </el-table-column>
      <el-table-column label="齐舞" width="120" align="center" prop="qiCount">
        <template slot-scope="scope">
          <el-link :underline="false" type="warning" style="font-weight: 600">{{scope.row.qiCount||0}} 队 / {{scope.row.qiSportCount||0}} 人</el-link>
        </template>
      </el-table-column>
      <el-table-column label="总费用" width="120" align="center" prop="allFee">
        <template slot-scope="scope">
          <el-link :underline="false" type="danger" style="font-weight: 600">{{scope.row.allFee||0}} 元</el-link>
        </template>
      </el-table-column>
      <el-table-column label="报名" width="88" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-link :underline="false" type="primary" style="font-weight: 600" @click="viewTeamSignRecords(scope.row)">详情</el-link>
        </template>
      </el-table-column>
      <el-table-column label="收费表" width="88" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-link :underline="false" type="primary" style="font-weight: 600" @click="handleDownLoadFee(scope.row)">打印</el-link>
        </template>
      </el-table-column>
      <el-table-column label="赛程表" width="88" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-link :underline="false" type="primary" style="font-weight: 600" @click="handleDownLoadScheduleInfo(scope.row)">打印</el-link>
        </template>
      </el-table-column>
      <el-table-column label="成绩" width="88" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-link :underline="false" type="primary" style="font-weight: 600" @click="handleDownLoadGrade(scope.row)">打印</el-link>
        </template>
      </el-table-column>
      <el-table-column label="联系人" align="left" prop="userName"/>
      <el-table-column label="联系电话" align="left" prop="userPhone"/>
      <el-table-column label="邮寄地址" align="left" prop="addr"/>

    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getTeamList"
    />

    <!-- 添加或修改比赛参赛的队伍对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="比赛" prop="matchId">
          <el-input v-model="form.matchId" placeholder="请输入比赛"/>
        </el-form-item>
        <el-form-item label="序号" prop="indexOrder">
          <el-input v-model="form.indexOrder" placeholder="请输入序号"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="报名详情" :visible.sync="viewSignOpen" width="75vw" center :append-to-body="false">
      <!--<div class="btn-row">-->
      <!--&lt;!&ndash;<el-button type="success" plain size="mini" @click="handleAdd">一览表</el-button>&ndash;&gt;-->
      <!--<el-button type="primary" plain size="mini" @click="handleDownLoadFee">收费表</el-button>-->
      <!--<el-button type="warning" plain size="mini" @click="handleDownLoadScheduleInfo">赛程表</el-button>-->
      <!--</div>-->
      <el-tabs type="border-card">
        <el-tab-pane label="报名记录" key="1">
          <el-table class="sign" height="65vh" :data="teamSignRecordList">
            <!--<el-table-column type="selection" width="55" align="center"/>-->
            <!--<el-table-column type="selection" width="55" align="center">-->
            <!--<template slot-scope="scope">-->
            <!--<span>{{ scope.$index+1 }}</span>-->
            <!--</template>-->
            <!--</el-table-column>-->
            <el-table-column label="组别" width="300" show-overflow-tooltip align="left" prop="gameItemId">
              <template slot-scope="scope">
                <span>{{ scope.row.jwGameItem.code+":"+scope.row.jwGameItem.name }}</span>
              </template>
            </el-table-column>
            <el-table-column label="人数限制" width="80" align="left" prop="sportLimit">
              <template slot-scope="scope">
                <dict-tag :options="dict.type.jw_sport_limit" :value="scope.row.sportLimit"/>
              </template>
            </el-table-column>
            <el-table-column label="背号" width="100" align="left" prop="backNumber"/>
            <el-table-column label="选手" align="left">
              <template slot-scope="scope">
              <span v-for="itemm in scope.row.jwSignRecordSportList"
                    class="sport-name"
                    :class="itemm.sex === 'f'? 'female' : (itemm.sex === 'm'? 'male' : '')"
              >
                <span>{{itemm.playerName}}<span v-if="scope.row.sportLimit != 1" style="margin-right: 8px"></span>
                </span>
              </span>
              </template>
            </el-table-column>
            <el-table-column label="费用" width="100" align="left" prop="fee"/>
            <el-table-column label="人均费用" width="100" align="left" prop="avgFee"/>
            <!--<el-table-column label="作品名称" align="center" prop="worksName"/>-->
            <!--<el-table-column label="作品音乐" align="center" prop="worksMusic">-->
            <!--<template slot-scope="scope">-->
            <!--<i v-if="scope.row.worksMusic" class="el-icon-video-play music-icon"></i>-->
            <!--</template>-->
            <!--</el-table-column>-->
            <!--<el-table-column label="作品视频" align="center" prop="worksVideo"/>-->
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="按项目查看" key="2">
          <el-table class="sign" height="65vh" :data="jwGameItemList">
            <!--<el-table-column type="selection" width="55" align="center"/>-->

            <el-table-column label="组别" width="300" show-overflow-tooltip align="left" prop="gameItemId">
              <template slot-scope="scope">
                <span>{{ scope.row.code+":"+scope.row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column label="人数限制" width="80" align="center" prop="sportLimit">
              <template slot-scope="scope">
                <dict-tag :options="dict.type.jw_sport_limit" :value="scope.row.sportLimit"/>
              </template>
            </el-table-column>
            <el-table-column label="选手" align="center">
              <template slot-scope="scope">
          <span v-for="item in scope.row.sportList" class="quick-edit">
           (
            <span class="back-num" v-if="item.backNumber">{{item.backNumber}}:</span>
            <span v-for="itemm in item.jwSignRecordSportList" class="sport-name" :class="itemm.sex === 'f'? 'female' : (itemm.sex === 'm'? 'male' : '')">
              <span>{{itemm.playerName}}<span v-if="scope.row.sportLimit != 1" style="margin-right: 8px"></span>
              </span>
          </span>)
          </span>
              </template>
            </el-table-column>
          </el-table>

        </el-tab-pane>

        <el-tab-pane label="按选手查看" key="3">
          <el-table class="sign" height="65vh" :data="jwSportList">
            <!--<el-table-column type="selection" width="55" align="center"/>-->
            <el-table-column label="选手" width="300" show-overflow-tooltip align="left" prop="gameItemId">
              <template slot-scope="scope">
                <span class="sport-name" :class="scope.row.sex === 'f'? 'female' : (scope.row.sex === 'm'? 'male' : '')">{{scope.row.playerName}}</span>
              </template>
            </el-table-column>
            <el-table-column label="项目" align="left">
              <template slot-scope="scope">
                <span v-for="itemm in scope.row.jwGameItemList" class="sport-name">
                  <span>{{itemm.code}}：{{itemm.name}}<span style="margin-right: 16px"></span>
                  </span>
                </span>
              </template>
            </el-table-column>
            <el-table-column label="费用" width="90" align="center" prop="allFee"/>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewSignOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog title="打印收费单" :visible.sync="downLoadFeeOpen" center width="750px" :append-to-body="false">

      <div class="btn-row">
        <el-button type="primary" plain size="mini" @click="handlePrint('printFeeCon')">打印</el-button>
      </div>
      <div class="fee-items" id="printFeeCon" v-if="downLoadFeeOpen">
        <div class="title-name">报名费收费通知单</div>
        <div class="team-name">代表队：{{currentTeam.indexOrder}} {{currentTeam.teamName}}</div>
        <div class="fee-item">
          <div class="index-v h index">序号</div>
          <div class="index-v h back">背号</div>
          <div class="index-v h sport">选手</div>
          <div class="index-v h game-item">项目组别</div>
          <div class="index-v h fee">报名费</div>
          <div class="index-v h remark">备注</div>
        </div>
        <div class="fee-item" v-for="(item, index) in teamFeeList">
          <div class="index-v  index">{{index + 1}}</div>
          <div class="index-v back">{{item.backNumber }}</div>
          <div class="index-v sport">
            <span v-for="itemm in item.jwSignRecordSportList">
              <span v-if="item.sportLimit != 1" class="m-sport">{{itemm.playerName}}</span>
              <span v-else>{{itemm.playerName}}</span>
            </span>
          </div>
          <div class="index-v game-item">{{ item.jwGameItem.code+":"+item.jwGameItem.name }}</div>
          <div class="index-v fee">{{item.fee}}
            <div style="margin-left: 4px" v-if="item.sportLimit != 1"> ({{item.avgFee}}/人)</div>
          </div>
          <div class="index-v remark"></div>
        </div>
        <div class="fee-item">
          <div class="index-v sport all-fee" style="font-size: 16px;font-weight: 600;">
            总计：{{teamSignRecordList.reduce((pre, curr) => pre + curr.fee, 0)}} 元
          </div>
        </div>
      </div>

    </el-dialog>

    <el-dialog title="打印赛程表" :visible.sync="downLoadScheduleInfo" width="750px" center :append-to-body="false">
      <div class="btn-row">
        <el-button type="primary" plain size="mini" @click="handlePrint('printFeeConT')">打印</el-button>
      </div>
      <div class="fee-items" id="printFeeConT">
        <div class="match-name">2024乐山市中小学生运动会体育舞蹈比赛 街舞项目</div>
        <div class="title-name">代表队赛程表</div>
        <div class="team-name">代表队：{{currentTeam.indexOrder}} {{currentTeam.teamName}}</div>
        <div class="fee-item">
          <div class="index-v h time">比赛时间</div>
          <div class="index-v h game-item s">比赛组别</div>
          <div class="index-v h area">场地</div>
          <div class="sport-rows">
            <div class="sport-row">
              <div class="index-v h index">上场序号</div>
              <div class="index-v h back">背号</div>
              <div class="index-v h sport s">选手</div>
            </div>
          </div>

        </div>
        <div class="fee-item" v-for="(item, key) in scheduleInfoList">
          <div class="index-v time">
            <!--<br> {{item[0].scheduleName}}-->
            {{item[0].placeTime.split(" ")[1]}} 第{{item[0].placeOrder}}场
          </div>
          <div class="index-v game-item s">{{key}}</div>
          <div class="index-v area">{{getAreaLabel(item[0])}}</div>

          <div class="sport-rows">
            <div class="sport-row" v-for="itemm in item">
              <div class="index-v index">{{itemm.indexOrder}}</div>
              <div class="index-v back">{{itemm.backNumber }}</div>
              <div class="index-v sport s">
                <span v-for="sport in itemm.jwSignRecordSportList">
                  <span v-if="itemm.sportLimit != 1" class="m-sport">{{sport.playerName}}</span>
                  <span v-else>{{sport.playerName}}</span>
                </span>
              </div>
            </div>
          </div>
        </div>
        <!--<div class="fee-item">-->
        <!--<div class="index-v sport" style="font-size: 16px;font-weight: 600;">-->
        <!--</div>-->
        <!--</div>-->
      </div>


    </el-dialog>


    <el-dialog title="打印全部赛程表" :visible.sync="downLoadAllScheduleInfo" width="750px" center :append-to-body="false">
      <div class="btn-row">
        <el-button type="primary" plain size="mini" @click="handlePrint('printFeeConTA')">打印</el-button>
      </div>
      <div id="printFeeConTA">
        <div class="ssh" v-for="(itemmm, index) in allscheduleInfoList">
          <div class="fee-items">
            <div class="match-name">2024乐山市中小学生运动会体育舞蹈比赛 街舞项目</div>
            <div class="title-name">代表队赛程表</div>
            <div class="team-name">代表队：{{getTeamIndexName(itemmm)}}</div>
            <div class="fee-item">
              <div class="index-v h time">比赛时间</div>
              <div class="index-v h game-item s">比赛组别</div>
              <div class="index-v h area">场地</div>
              <div class="sport-rows">
                <div class="sport-row">
                  <div class="index-v h index">上场序号</div>
                  <div class="index-v h back">背号</div>
                  <div class="index-v h sport s">选手</div>
                </div>
              </div>
            </div>
            <div class="fee-item" v-for="(item, key) in itemmm">
              <div class="index-v time">
                <!--<br> {{item[0].scheduleName}}-->
                {{item[0].placeTime.split(" ")[1]}} 第{{item[0].placeOrder}}场
              </div>
              <div class="index-v game-item s">{{key}}</div>
              <div class="index-v area">{{getAreaLabel(item[0])}}</div>

              <div class="sport-rows">
                <div class="sport-row" v-for="itemm in item">
                  <div class="index-v index">{{itemm.indexOrder}}</div>
                  <div class="index-v back">{{itemm.backNumber }}</div>
                  <div class="index-v sport s">
                <span v-for="sport in itemm.jwSignRecordSportList">
                  <span v-if="itemm.sportLimit != 1" class="m-sport">{{sport.playerName}}</span>
                  <span v-else>{{sport.playerName}}</span>
                </span>
                  </div>
                </div>
              </div>
            </div>
            <!--<div class="fee-item">-->
            <!--<div class="index-v sport" style="font-size: 16px;font-weight: 600;">-->
            <!--</div>-->
            <!--</div>-->
          </div>
        </div>
      </div>
    </el-dialog>


    <el-dialog title="代表队成绩" :visible.sync="showDownloadGrade" width="740px" center :append-to-body="false">
      <div class="btn-row">
        <el-button type="primary" plain size="mini" @click="handlePrint('printTeamGrade')">打印</el-button>
      </div>
      <div class="grade-items" v-loading="loadGradeing" id="printTeamGrade" v-if="showDownloadGrade">
        <div class="team-name">{{currentTeam.indexOrder}}. {{currentTeam.teamName}}</div>
        <div class="sub-grade">成绩单</div>
        <div class="grade-item">
          <div class="index-v h back-num">背号</div>
          <div class="index-v h sport-name">选手</div>
          <div class="index-v h grade">成绩</div>
          <div class="index-v h game-item">项目</div>
        </div>
        <div class="grade-item" v-for="(item, key) in gradeSignRecordList">
          <div class="index-v  back-num">{{item.backNumber}}</div>
          <div class="index-v  sport-name">{{item.jwSignRecordSportList.map(itemm => itemm.playerName).join(" ")}}</div>
          <div class="index-v  grade">{{item.rankOrderDes}}</div>
          <div class="index-v  game-item">{{ item.jwGameItem.code+":"+item.jwGameItem.name }}</div>
        </div>
        <div class="sub-grade t">成绩统计</div>
        <div class="grade-item">
          <div class="index-v h grade-text">奖项</div>
          <!--<div class="index-v h grade-text">奖项</div>-->
          <div class="index-v h num">数量</div>
          <div class="index-v h num">证书</div>
          <div class="index-v h num">奖杯</div>
          <div class="index-v h num">奖牌</div>
        </div>
        <div class="grade-item" v-for="(item, key) in gradeAwardsItemList">
          <div class="index-v grade-text">{{item.awardName}}</div>
          <!--<div class="index-v grade-text">{{item.rankText}}</div>-->
          <div class="index-v num">{{item.countNum}}</div>
          <div class="index-v num">{{item.zhengShu}}</div>
          <div class="index-v num">{{item.jiangBei}}</div>
          <div class="index-v num">{{item.jiangPai}}</div>
        </div>
      </div>

    </el-dialog>

  </div>
</template>

<script>
  import {listJwMatchTeam, getJwMatchTeam, delJwMatchTeam, addJwMatchTeam, updateJwMatchTeam, getTeamFee, getTeamScheduleInfoList} from "@/api/jiewu/JwMatchTeam";
  import {listJwTeam} from "@/api/jiewu/JwTeam";

  import {listTeamGradeDes} from "@/api/jiewu/JwHaiScore";

  import 'core-js/actual/array/group';
  import {listMatchJwSport} from "@/api/jiewu/JwSport";

  export default {
    name: "JwMatchTeam",
    dicts: ['sys_yes_no', 'jw_match_type', 'jw_sex', 'jw_sport_limit', 'jw_group_mode', 'jw_area'],
    data() {
      return {
        loadGradeing: false,
        showDownloadGrade: false,
        gradeAwardsItemList: [],
        gradeSignRecordList: [],
        scheduleInfoList: [],
        scheduleLoading: false,
        downLoadScheduleInfo: false,
        downLoadAllScheduleInfo: false,
        teamFeeList: [],
        currentTeam: {},
        viewSignOpen: false,
        teamSignRecordList: [],
        jwGameItemList: [],
        jwSportList: [],
        downLoadFeeOpen: false,
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
        // 总条数
        total: 0,
        // 比赛参赛的队伍表格数据
        JwTeamList: [],
        allscheduleInfoList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          teamName: "",
          matchId: (this.Cookies.get("matchId") * 1) || null,
          indexOrder: null
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {}
      };
    },
    watch: {
      "queryParams.matchId": function (val) {
        this.getTeamList();

      },
    },
    created() {
      this.getTeamList();
    },
    methods: {
      getTeamIndexName(itemss) {
        for (let key in itemss) {
          if(itemss[key] && itemss[key][0]){
            if (this.JwTeamList && this.JwTeamList.find((item) => item.teamName == itemss[key][0].teamName)) {
              let team = this.JwTeamList.find((item) => item.teamName == itemss[key][0].teamName);
              return team.indexOrder +"."+ team.teamName;
            }
          }
        }
      },
      // 打印代表队成绩
      handleDownLoadGrade(row) {
        this.currentTeam = row;
        this.showDownloadGrade = true;
        this.loadGradeing = true;
        this.gradeAwardsItemList = [];
        this.gradeSignRecordList = [];
        listTeamGradeDes({matchId: this.queryParams.matchId, teamId: this.currentTeam.id}).then(res => {
          this.gradeAwardsItemList = res.data[0].awardsItemList || [];
          this.gradeSignRecordList = res.data[0].jwSignRecordList || [];
          this.loadGradeing = false;
        })
      },
      getSummaries(param) {
        const {columns, data} = param;
        const sums = [];
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '总计';
            return;
          }
          if (index == 3 || index == 4 || index == 5) {
            const values = data.map(item => Number(item[column.property]));
            if (!values.every(value => isNaN(value))) {
              sums[index] = values.reduce((prev, curr) => {
                const value = Number(curr);
                if (!isNaN(value)) {
                  return prev + curr;
                } else {
                  return prev;
                }
              }, 0);
              if (index == 3) {
                sums[index] += ' 人';
              }
              if (index == 4) {
                sums[index] += ' 队 / ';

                const values2 = data.map(item => Number(item["qiSportCount"]));

                let ss = values2.reduce((prev, curr) => {
                  const value = Number(curr);
                  if (!isNaN(value)) {
                    return prev + curr;
                  } else {
                    return prev;
                  }
                }, 0);

                sums[index] += ss + ' 人';
              }
              if (index == 5) {
                sums[index] += ' 元';
              }
            } else {
              sums[index] = '';
            }
          } else {
            sums[index] = '';
          }

        });

        return sums;
      },
      getAreaLabel(item) {
        return this.selectDictLabel(this.dict.type.jw_area, item.area);
      },
      // 打印 代表队 赛程
      handleDownLoadScheduleInfo(row) {
        this.currentTeam = row;
        this.scheduleInfoList = {};
        this.downLoadScheduleInfo = true;
        this.scheduleLoading = true;

        getTeamScheduleInfoList({matchId: this.queryParams.matchId, teamId: this.currentTeam.id}).then(res => {
          let scheduleInfoList = res.data || [];
          this.scheduleInfoList = scheduleInfoList.group((b) => b.itemName);
          this.scheduleLoading = false;
        })
      },
      // 打印 全部 代表队 赛程
      handleDownLoadAllScheduleInfo() {
        let allscheduleInfoList = [];
        this.JwTeamList.forEach(item => {
          getTeamScheduleInfoList({matchId: this.queryParams.matchId, teamId: item.id}).then(res => {
            let scheduleInfoList = res.data || [];
            allscheduleInfoList.push(scheduleInfoList.group((b) => b.itemName));
          })
          this.scheduleLoading = false;


        })
        this.downLoadAllScheduleInfo = true;
        this.allscheduleInfoList = allscheduleInfoList;

      },
      handleDownLoadFee(row) {
        this.currentTeam = row;
        getTeamFee({matchId: this.queryParams.matchId, teamId: row.id}).then(res => {
          this.teamSignRecordList = res.data.jwSignRecordList || [];
          let teamFeeList = [].concat(res.data.jwSignRecordList || []);
          teamFeeList.sort((a, b) => {
            if (a.sportLimit != b.sportLimit) {
              return a.sportLimit - b.sportLimit;
            } else {
              return a.backNumber - b.backNumber;
            }
          });
          this.teamFeeList = teamFeeList;
          this.downLoadFeeOpen = true;
        })
      },
      // 查看报名详情
      viewTeamSignRecords(row) {
        this.currentTeam = row;
        getTeamFee({matchId: this.queryParams.matchId, teamId: row.id}).then(res => {
          this.teamSignRecordList = res.data.jwSignRecordList || [];
          this.teamSignRecordList.sort((a, b) => {
            if (!a.backNumber || !b.backNumber) {
              return a.jwGameItem.code - b.jwGameItem.code;
            } else {
              return a.backNumber - b.backNumber;
            }
          });
          this.jwGameItemList = res.data.jwGameItemList || [];

          this.jwSportList = res.data.jwSportList || [];
          this.jwSportList.forEach(item => {
            item.allFee = item.jwGameItemList.reduce((pre, curr) => pre + curr.fee, 0);
            item.jwGameItemList.sort((a, b) => a.code - b.code);
          });
          this.viewSignOpen = true;
        })
      },
      /** 查询比赛参赛的队伍列表 */
      getTeamList() {
        if (this.queryParams.matchId) {
          this.loading = true;
          listJwTeam(this.queryParams).then(response => {
            this.JwTeamList = response.rows;
            this.total = response.total;
            this.loading = false;
          });
        } else {
          this.loading = false;
          this.JwTeamList = [];
          this.total = 0;
        }

      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          teamId: null,
          matchId: null,
          indexOrder: null
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getTeamList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.teamId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加比赛参赛的队伍";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const teamId = row.teamId || this.ids
        getJwMatchTeam(teamId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改比赛参赛的队伍";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.teamId != null) {
              updateJwMatchTeam(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getTeamList();
              });
            } else {
              addJwMatchTeam(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getTeamList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const teamIds = row.teamId || this.ids;
        this.$modal.confirm('是否确认删除比赛参赛的队伍编号为"' + teamIds + '"的数据项？').then(function () {
          return delJwMatchTeam(teamIds);
        }).then(() => {
          this.getTeamList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      handleBeiHaoExport() {
        this.download('jiewu/JwMatchTeam/exportTeamBackNum', {
          ...this.queryParams
        }, `代表队背号.xlsx`)
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('jiewu/JwMatchTeam/export', {
          ...this.queryParams
        }, `代表队名单.xlsx`)
      }
    }
  };
</script>

<style lang="scss" scoped>

  .match-name {
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    margin-bottom: 8px;
  }

  .title-name, .team-name {
    text-align: center;
    margin-bottom: 8px;
    font-weight: 600;
    font-size: 16px;
  }

  .grade-items {
    display: flex;
    flex-direction: column;
    color: #000;

    .sub-grade {
      text-align: center;
      font-size: 18px;
      padding-bottom: 8px;

      &.t {
        border-top: 1px solid #000;
        padding-top: 32px;
      }
    }

    .grade-item {
      display: flex;
      flex-direction: row;
      border: 1px solid #000;
      border-bottom: none;
      page-break-inside: avoid;

      &:last-child {
        border-bottom: 1px solid #000;
      }

      .index-v {
        /*text-align: center;*/
        border-right: 1px solid #000;
        padding: 2px 0;
        font-size: 12px;
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        justify-content: center;
        color: #000;

        &.h {
          font-weight: 600;
          font-size: 14px;
        }

        &.back-num {
          width: 56px;
          color: #000;
        }

        &.sport-name {
          width: 0;
          flex: 1;
          padding-left: 4px;
          padding-right: 4px;
          line-height: 12px;
        }

        &.grade {
          width: 64px;
        }

        &.game-item {
          width: 260px;
          border-right: none;
        }

        &.grade-text {
          width: 120px;
        }

        &.num {
          flex: 1;

          &:last-child {
            border-right: none;
          }
        }
      }
    }
  }

  .ssh {
    page-break-inside: avoid;
    page-break-before: always;
    page-break-after: always;
  }

  .fee-items {
    display: flex;
    flex-direction: column;
    margin-right: 12px;
    color: #000;
    width: 520pt;
    page-break-inside: avoid;

    .fee-item {
      display: flex;
      flex-direction: row;
      border: 1px solid #000;
      border-bottom: none;
      page-break-inside: avoid;

      &:last-child {
        border-bottom: 1px solid #000;
      }

      .index-v {
        /*text-align: center;*/
        border-right: 1px solid #000;
        padding: 2px 0;
        font-size: 12px;
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        justify-content: center;

        &.h {
          font-weight: 600;
          font-size: 14px;
        }

        &:last-child {
          border-right: none;
        }

        &.index {
          width: 36px;
        }

        &.back {
          width: 48px;
        }

        &.sport {
          width: 0;
          flex: 1;
          padding-left: 4px;
          padding-right: 4px;
          line-height: 12px;

          &.s {
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
          }

          .m-sport {
            margin-right: 8px;
            white-space: nowrap;
            font-size: 10px;
          }

          &.all-fee {
            height: 40px;
          }
        }

        &.game-item {
          width: 20px;
          flex: 1;
          padding-left: 4px;
          padding-right: 4px;

          &.s {
            width: 160px;
            flex: none;
          }
        }

        &.fee {
          width: 100px;
          line-height: 12px;
        }

        &.remark {
          width: 80px;
        }

        &.time {
          width: 100px;
          justify-content: flex-start;
          padding-left: 4px;
        }

        &.area {
          width: 36px;
        }
      }

      .sport-rows {
        flex: 1;
        display: flex;
        flex-direction: column;
        page-break-inside: avoid;

        .sport-row {
          flex: 1;
          display: flex;
          flex-direction: row;
          border-bottom: 1px solid #000;
          page-break-inside: avoid;

          &:last-child {
            border-bottom: none;
          }
        }
      }
    }
  }

  .text-info {
    font-size: 12px;
    color: #888888;
  }

  .btn-row {
    display: flex;
    flex-direction: row;
    margin-bottom: 12px;
    justify-content: end;
  }

  .back-num {
    color: #07c160;
    font-weight: 600;
  }

  .quick-edit {
    /*white-space: nowrap;*/
    margin-right: 12px;
  }

  .male {
    color: #1890FF;
  }

  .female {
    color: #FF3399;
  }

  ::v-deep .el-dialog__body {
    padding: 0 16px 16px 16px;
  }

  ::v-deep .el-table.sign .cell {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
  }

  .add-sign {
    cursor: pointer;
    word-break: keep-all;
    color: #1890FF;
    font-size: 16px;
    margin-left: 10px;
  }

  .sport-names {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
  }

  .sport-name {
    cursor: pointer;
    word-break: keep-all;
  }

  .music-icon {
    font-size: 20px;
  }
</style>

