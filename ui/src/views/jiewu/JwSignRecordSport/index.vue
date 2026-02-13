<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="比赛ID" prop="matchId">
        <ELSelectMatch :matchId.sync="queryParams.matchId"/>
      </el-form-item>
      <el-form-item label="代表队" prop="teamId">
        <el-select filterable v-model="queryParams.teamId" placeholder="代表队" clearable>
          <el-option
            v-for="team in JwTeamList"
            :key="team.id"
            :label="team.indexOrder + ' : ' + team.teamName + ' - '+ team.userName+' - '+ team.createUserId"
            :value="team.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="姓名" prop="playerName">
        <el-input
          v-model="queryParams.playerName"
          placeholder="请输入运动员姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="背号" prop="backNumber">
        <el-input
          v-model="queryParams.backNumber"
          placeholder="请输入背号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="身份证" prop="idCard">
        <el-input
          v-model="queryParams.idCard"
          placeholder="请输入身份证"
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
          v-hasPermi="['jiewu:JwSignRecordSport:add']"
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
          v-hasPermi="['jiewu:JwSignRecordSport:edit']"
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
          v-hasPermi="['jiewu:JwSignRecordSport:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-refresh"
          size="mini"
          :disabled="multiple"
          @click="handleChangeTeam"
          v-hasPermi="['jiewu:JwSignRecordSport:remove']"
        >改队伍
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['jiewu:JwSignRecordSport:export']"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="teamSportHandel"
          v-hasPermi="['jiewu:JwSignRecordSport:export']"
        >代表队名单
        </el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="JwSportList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" width="55" align="center" prop="id"/>
      <el-table-column label="代表队" align="left" prop="teamName"/>
      <el-table-column label="背号" width="60" align="center" prop="backNumber"/>
      <el-table-column label="姓名" align="center" prop="playerName"/>
      <el-table-column label="身份证" align="center" prop="idCard"/>
      <el-table-column label="性别" align="center" prop="sex">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.jw_sex" :value="scope.row.sex"/>
        </template>
      </el-table-column>
      <el-table-column label="年龄" align="center" prop="age"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['jiewu:JwSignRecordSport:edit']"
          >修改
          </el-button>
          <!--<el-button-->
          <!--size="mini"-->
          <!--type="text"-->
          <!--icon="el-icon-delete"-->
          <!--@click="handleDelete(scope.row)"-->
          <!--v-hasPermi="['jiewu:JwSignRecordSport:remove']"-->
          <!--&gt;删除</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />


    <el-dialog title="换队伍" :visible.sync="changeOpen" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="选手" prop="playerName">
          <el-input v-model="changeTeamForm.sportIds" :readOnly="true"/>
        </el-form-item>
        <el-form-item label="新队伍" prop="newTeamId">
          <el-select filterable v-model="changeTeamForm.newTeamId" placeholder="代表队" clearable>
            <el-option
              v-for="team in JwTeamList"
              :key="team.id"
              :label="team.indexOrder + ' : ' + team.teamName"
              :value="team.id"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitChangeTeam">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改报名记录选手对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="playerName">
          <el-input v-model="form.playerName" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="身份证" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="代表队名单" :visible.sync="showTeamSport" width="750px" center :append-to-body="false">
      <div class="btn-row">
        <el-button type="primary" plain size="mini" @click="handlePrint('printFeeConT')">打印</el-button>
      </div>
      <div class="fee-items" id="printFeeConT" v-if="showTeamSport">
        <div class="match-name">{{matchName}}</div>
        <div class="title-name">代表队名单</div>
        <div class="team-item" v-for="(item, key) in teamSports">
          <div class="team-name">{{key}}</div>

          <div class="la">{{getTeamUserName(key)}}</div>
          <div class="la">{{getTeamUserNameT(key)}}</div>
          <div class="la">{{getTeamUserNameTH(key)}}</div>
          <div class="la">运动员:</div>
          <div class="sport-rows">
            <div class="sport-row" v-for="itemm in item">
              <div class="sport-name">{{itemm.playerName}}</div>
            </div>
          </div>
        </div>
      </div>

    </el-dialog>

  </div>
</template>

<script>
  import {getJwMatch} from "@/api/jiewu/jwMatch";

  import {listMatchJwSport, getJwSport, delJwSport, addJwSport, updateJwSport, changeTeam} from "@/api/jiewu/JwSport";
  import {listJwTeam} from "@/api/jiewu/JwTeam";
  import 'core-js/actual/array/group';

  export default {
    name: "JwSignRecordSport",
    dicts: ['jw_sport_limit', 'jw_sex'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        JwTeamList: [],
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 报名记录选手表格数据
        JwSportList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        changeOpen: false,
        changeTeamForm: {
          newTeamId: null,
          sportIds: []
        },
        matchName: "",
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          matchId: (this.Cookies.get("matchId") * 1) || null,
          playerName: null,
          teamId: null,
          backNumber: null,
          idCard: null
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        teamSports: [],
        showTeamSport: false
      };
    },
    watch: {
      "queryParams.matchId": function (val) {
        this.getTeamList()
        this.getMatchInfo()
      },
    },
    created() {
      this.getList();
      this.getTeamList();
      this.getMatchInfo()
    },
    methods: {
      getTeamUserNameTH(teamName) {
        if (this.JwTeamList && this.JwTeamList.find((item) => item.teamName == teamName)) {
          let team = this.JwTeamList.find((item) => item.teamName == teamName);
          let res = "";
          (team.jwTeamLeaderList || []).forEach(jtl => {
            if (jtl.userType == "3") {
              res += "管理： " + jtl.leaderName;
            }
          })
          return res;
        }
      },
      getTeamUserNameT(teamName) {
        if (this.JwTeamList && this.JwTeamList.find((item) => item.teamName == teamName)) {
          let team = this.JwTeamList.find((item) => item.teamName == teamName);
          let res = "";
          (team.jwTeamLeaderList || []).forEach(jtl => {
            if (jtl.userType == "2") {
              res = "教练： " + jtl.leaderName;
            }
          })
          return res;
        }
      },
      getTeamUserName(teamName) {
        if (this.JwTeamList && this.JwTeamList.find((item) => item.teamName == teamName)) {
          let team = this.JwTeamList.find((item) => item.teamName == teamName);
          let res = "";
          if (team.userName) {
            res = "领队： " + team.userName;
          }
          (team.jwTeamLeaderList || []).forEach(jtl => {
            if (jtl.userType == "1") {
              res = "领队： " + jtl.leaderName;
            }
          });
          return res;
        }
      },
      getMatchInfo() {
        getJwMatch(this.queryParams.matchId).then(response => {
          this.matchName = response.data.matchName;
        });
      },
      getTeamList() {
        listJwTeam({matchId: this.queryParams.matchId, pageNum: 1, pageSize: 5000}).then(response => {
          this.JwTeamList = response.rows;
        });
      },
      teamSportHandel() {
        listMatchJwSport({
          pageNum: 1,
          pageSize: 100000,
          matchId: this.queryParams.matchId
        }).then(response => {
          this.teamSports = response.rows.group((b) => b.teamName);
          console.log(this.teamSports);
          this.showTeamSport = true;
        });
      },
      getList() {
        this.loading = true;
        listMatchJwSport(this.queryParams).then(response => {
          this.JwSportList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      cancel() {
        this.open = false;
        this.changeOpen = false;
        this.reset();
      },
      reset() {
        this.form = {
          id: null,
          playerName: null,
          idCard: null,
        };
        this.changeTeamForm = {
          newTeamId: null,
          sportIds: []
        }
        this.resetForm("form");
      },
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },

      handleAdd() {
        this.reset();
        if (!this.queryParams.teamId) {
          this.$modal.msgError("请选择代表队");
          return;
        }
        let createUserId = this.JwTeamList.find((item) => item.id == this.queryParams.teamId).createUserId
        if (!createUserId) {
          this.$modal.msgError("请选择代表队");
          return;
        }
        this.form.createUserId = createUserId;
        this.open = true;
        this.title = "添加选手";
      },
      submitChangeTeam() {

        console.log(this.changeTeamForm);
        changeTeam({sportIds: this.changeTeamForm.sportIds.join(","), newTeamId: this.changeTeamForm.newTeamId}).then(response => {
          this.$modal.msgSuccess("修改成功");
          this.changeOpen = false;
          this.getList();
        });

      },
      handleChangeTeam() {

        this.changeTeamForm = {
          newTeamId: null,
          sportIds: this.ids || []
        };
        this.changeOpen = true;
      },
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getJwSport(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改选手";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateJwSport(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addJwSport(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      handleDelete(row) {
        const ids = row.id || this.ids;

      },
      handleExport() {
        if (!this.queryParams.matchId) {
          this.$modal.msgError("比赛");
          return;
        }
        this.download('jiewu/JwSport/export', {
          ...this.queryParams
        }, `参赛选手名单.xlsx`)
      }
    }
  };
</script>
<style lang="scss" scoped>

  .match-name {
    text-align: center;
    font-size: 24px;
    /*font-weight: 600;*/
    margin-bottom: 16px;
    font-family: "华文中宋";
  }

  .title-name {
    text-align: center;
    margin-bottom: 8px;
    /*font-weight: 600;*/
    font-size: 18px;
    font-family: "华文中宋";
  }

  .fee-items {
    display: flex;
    flex-direction: column;
    margin-right: 12px;
    color: #000;
    width: 520pt;
    page-break-inside: avoid;
    font-family: "华文中宋";

    .team-item {
      margin-top: 32px;
      page-break-inside: avoid;

      .team-name {
        text-align: center;
        margin-bottom: 8px;
        /*font-weight: 600;*/
        font-size: 18px;
      }

      .la {
        /*font-weight: 600;*/
        font-size: 16px;
        margin-bottom: 8px;
        padding-left: 16px;

      }

      .sport-rows {
        padding-left: 24px;
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        /*justify-content: space-between;*/

        .sport-row {
          margin-right: 24px;
          margin-bottom: 8px;

          .sport-name {
            white-space: nowrap;
            width: 56px;
          }
        }
      }
    }

  }
</style>
