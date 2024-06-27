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
            :label="team.indexOrder + ' : ' + team.teamName"
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
        >新增</el-button>
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
        >修改</el-button>
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
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['jiewu:JwSignRecordSport:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="JwSportList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" width="55" align="center" prop="id" />
      <el-table-column label="代表队" align="left" prop="teamName"/>
      <el-table-column label="背号" width="60" align="center" prop="backNumber" />
      <el-table-column label="姓名" align="center" prop="playerName" />
      <el-table-column label="身份证" align="center" prop="idCard" />
      <el-table-column label="性别" align="center" prop="sex">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.jw_sex" :value="scope.row.sex"/>
        </template>
      </el-table-column>
      <el-table-column label="年龄" align="center" prop="age" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['jiewu:JwSignRecordSport:edit']"
          >修改</el-button>
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

    <!-- 添加或修改报名记录选手对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="playerName">
          <el-input v-model="form.playerName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="身份证" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listMatchJwSport, getJwSport, delJwSport, addJwSport, updateJwSport } from "@/api/jiewu/JwSport";
  import {listJwTeam} from "@/api/jiewu/JwTeam";

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
      rules: {
      }
    };
  },
  watch: {
    "queryParams.matchId": function (val) {
      this.getTeamList()
    },
  },
  created() {
    this.getList();
    this.getTeamList()
  },
  methods: {
    getTeamList() {
      listJwTeam({matchId: this.queryParams.matchId, pageNum: 1, pageSize: 5000}).then(response => {
        this.JwTeamList = response.rows;
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
      this.reset();
    },
    reset() {
      this.form = {
        id: null,
        playerName: null,
        idCard: null,
      };
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset();
      if(!this.queryParams.teamId){
        this.$modal.msgError("请选择代表队");
        return;
      }
      let createUserId = this.JwTeamList.find((item) => item.id == this.queryParams.teamId).createUserId
      if(!createUserId){
        this.$modal.msgError("请选择代表队");
        return;
      }
      this.form.createUserId = createUserId;
      this.open = true;
      this.title = "添加选手";
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
      this.download('jiewu/JwSignRecordSport/export', {
        ...this.queryParams
      }, `JwSignRecordSport_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
