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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['jiewu:JwMatchTeam:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['jiewu:JwMatchTeam:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['jiewu:JwMatchTeam:remove']"
        >删除
        </el-button>
      </el-col>
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getTeamList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="JwTeamList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" width="64" align="center" prop="id"/>
      <el-table-column label="序号" width="64" align="center" prop="indexOrder"/>
      <el-table-column label="代表队名字" align="left" prop="teamName"/>
      <el-table-column label="报名" width="180" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-link :underline="false" type="primary" @click="viewTeamSignRecords(scope.row)">报名详情</el-link>
        </template>
      </el-table-column>
      <el-table-column label="联系人" align="left" prop="userName"/>
      <el-table-column label="联系电话" align="left" prop="userPhone"/>
      <el-table-column label="邮寄地址" align="left" prop="addr"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['jiewu:JwTeam:edit']"
          >修改
          </el-button>
          <!--<el-button-->
            <!--size="mini"-->
            <!--type="text"-->
            <!--icon="el-icon-delete"-->
            <!--@click="handleDelete(scope.row)"-->
            <!--v-hasPermi="['jiewu:JwTeam:remove']"-->
          <!--&gt;删除-->
          <!--</el-button>-->
        </template>
      </el-table-column>
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
      <div class="btn-row">
        <!--<el-button type="success" plain size="mini" @click="handleAdd">一览表</el-button>-->
        <el-button type="primary" plain size="mini" @click="handleDownLoadFee">收费表</el-button>
        <el-button type="warning" plain size="mini" @click="handleDownLoadScheduleInfo">赛程表</el-button>
      </div>
      <el-tabs type="border-card">
        <el-tab-pane label="报名记录" key="1">
          <el-table class="sign" height="65vh" :data="teamSignRecordList">
            <el-table-column type="selection" width="55" align="center"/>
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
                <span>{{itemm.playerName}}<span v-if="scope.row.sportLimit == 3" style="margin-right: 8px"></span>
                </span>
              </span>
              </template>
            </el-table-column>
            <el-table-column label="费用" width="100" align="left" prop="fee"/>
            <el-table-column label="人均费用" width="100" align="left" prop="avgFee"/>
            <el-table-column label="作品名称" align="center" prop="worksName"/>
            <el-table-column label="作品音乐" align="center" prop="worksMusic">
              <template slot-scope="scope">
                <i v-if="scope.row.worksMusic" class="el-icon-video-play music-icon"></i>
              </template>
            </el-table-column>
            <el-table-column label="作品视频" align="center" prop="worksVideo"/>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="按项目查看" key="2">
          <el-table class="sign" height="65vh" :data="jwGameItemList">
            <el-table-column type="selection" width="55" align="center"/>

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
              <span>{{itemm.playerName}}<span v-if="scope.row.sportLimit == 3" style="margin-right: 8px"></span>
              </span>
          </span>)
          </span>
              </template>
            </el-table-column>
          </el-table>

        </el-tab-pane>

        <el-tab-pane label="按选手查看" key="3">
          <el-table class="sign" height="65vh" :data="jwSportList">
            <el-table-column type="selection" width="55" align="center"/>
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

    <el-dialog title="打印收费单" :visible.sync="downLoadFeeOpen" center :append-to-body="false">

      <div class="btn-row">
        <el-button type="primary" plain size="mini" @click="handlePrintFee('printFeeCon')">打印</el-button>
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
              <span v-if="item.sportLimit == 3" class="m-sport">{{itemm.playerName}}</span>
              <span v-if="item.sportLimit != 3">{{itemm.playerName}}</span>
            </span>
          </div>
          <div class="index-v game-item">{{ item.jwGameItem.code+":"+item.jwGameItem.name }}</div>
          <div class="index-v fee">{{item.fee}}
            <div style="margin-left: 4px" v-if="item.sportLimit == 3"> ({{item.avgFee}}/人)</div>
          </div>
          <div class="index-v remark"></div>
        </div>
        <div class="fee-item">
          <div class="index-v sport" style="font-size: 16px;font-weight: 600;">
            总计：{{teamSignRecordList.reduce((pre, curr) => pre + curr.fee, 0)}} 元
          </div>

        </div>
      </div>

    </el-dialog>

    <el-dialog title="打印赛程表" :visible.sync="downLoadScheduleInfo" center :append-to-body="false">

      <div class="btn-row">
        <el-button type="primary" plain size="mini" @click="handlePrintFee('printFeeConT')">打印</el-button>
      </div>
      <div class="fee-items" id="printFeeConT" v-if="downLoadScheduleInfo" v-loading="scheduleLoading">
        <div class="title-name">代表队赛程表</div>
        <div class="team-name">代表队：{{currentTeam.indexOrder}} {{currentTeam.teamName}}</div>
        <div class="fee-item">
          <div class="index-v h time">比赛时间</div>
          <div class="index-v h game-item s">比赛组别</div>
          <div class="index-v h area">场地</div>
          <div class="sport-rows">
            <div class="sport-row" >
              <div class="index-v h index">上场序号</div>
              <div class="index-v h back">背号</div>
              <div class="index-v h sport s">选手</div>
            </div>
          </div>

        </div>
        <div class="fee-item" v-for="(item, key) in scheduleInfoList">

          <div class="index-v time">
            {{item[0].placeTime}} <br> {{item[0].scheduleName}} 第{{item[0].placeOrder}}场
          </div>
          <div class="index-v game-item s">{{key}}</div>
          <div class="index-v area">{{getAreaLabel(item[0])}}</div>

          <div class="sport-rows">
            <div class="sport-row" v-for="itemm in item">
              <div class="index-v index">{{itemm.indexOrder}}</div>
              <div class="index-v back">{{itemm.backNumber }}</div>
              <div class="index-v sport s">
                <span v-for="sport in itemm.jwSignRecordSportList">
                  <span v-if="itemm.sportLimit == 3" class="m-sport">{{sport.playerName}}</span>
                  <span v-if="itemm.sportLimit != 3">{{sport.playerName}}</span>
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

  </div>
</template>

<script>
  import {listJwMatchTeam, getJwMatchTeam, delJwMatchTeam, addJwMatchTeam, updateJwMatchTeam, getTeamFee, getTeamScheduleInfoList} from "@/api/jiewu/JwMatchTeam";
  import {listJwTeam} from "@/api/jiewu/JwTeam";
  import printJS from 'print-js';
  import 'core-js/actual/array/group';

  export default {
    name: "JwMatchTeam",
    dicts: ['sys_yes_no', 'jw_match_type', 'jw_sex', 'jw_sport_limit', 'jw_group_mode', 'jw_area'],
    data() {
      return {
        scheduleInfoList: [],
        scheduleLoading: false,
        downLoadScheduleInfo: false,
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
    created() {
      this.getTeamList();
    },
    methods: {
      getAreaLabel(item) {
        return this.selectDictLabel(this.dict.type.jw_area, item.area);
      },
      // 打印 代表队 赛程
      handleDownLoadScheduleInfo() {
        this.scheduleInfoList = {};
        this.downLoadScheduleInfo = true;
        this.scheduleLoading = true;

        getTeamScheduleInfoList({matchId: this.queryParams.matchId, teamId: this.currentTeam.id}).then(res => {
          let scheduleInfoList = res.data || [];

          this.scheduleInfoList = scheduleInfoList.group((b) => b.itemName);
          this.scheduleLoading = false;
        })
      },
      handlePrintFee(id) {
        printJS({
          printable: id,
          type: 'html',
          //为了样式生效需要添加 targetStyles:['*'] 和 font_size:''
          targetStyles: ['*'],
          font_size: '',
          //margin0 默认打印页边距为0
          style: `
          @page {
          size:auto;
            margin: 16;
          }
        `
        });
      },
      handleDownLoadFee() {
        let teamFeeList = [].concat(this.teamSignRecordList);
        teamFeeList.sort((a, b) => {
          if (a.sportLimit != b.sportLimit) {
            return a.sportLimit - b.sportLimit;
          } else {
            return a.backNumber - b.backNumber;
          }
        });
        this.teamFeeList = teamFeeList;
        this.downLoadFeeOpen = true;
      },
      // 查看报名详情
      viewTeamSignRecords(row) {
        this.currentTeam = row;
        getTeamFee({matchId: this.queryParams.matchId, teamId: row.id}).then(res => {
          this.teamSignRecordList = res.data.jwSignRecordList || [];
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
        this.loading = true;
        listJwTeam(this.queryParams).then(response => {
          this.JwTeamList = response.rows;
          this.total = response.total;
          this.loading = false;
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
      /** 导出按钮操作 */
      handleExport() {
        this.download('jiewu/JwMatchTeam/export', {
          ...this.queryParams
        }, `JwMatchTeam_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>


<style lang="scss" scoped>
  .title-name, .team-name {
    text-align: center;
    margin-bottom: 8px;
    font-weight: 600;
    font-size: 16px;
  }

  .fee-items {
    display: flex;
    flex-direction: column;
    margin-right: 12px;
    color: #000;
    width: 520pt;

    .fee-item {
      display: flex;
      flex-direction: row;
      border: 1px solid #000;
      border-bottom: none;

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
      .sport-rows{
        flex: 1;
        display: flex;
        flex-direction: column;
        .sport-row{
          flex: 1;
          display: flex;
          flex-direction: row;
          border-bottom: 1px solid #000;
          &:last-child{
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

