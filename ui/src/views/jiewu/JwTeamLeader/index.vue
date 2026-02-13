<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="比赛" prop="matchId">
        <ELSelectMatch :matchId.sync="queryParams.matchId"/>
      </el-form-item>
      <el-form-item label="代表队" prop="teamId">
        <el-select filterable v-model="queryParams.createUserId" placeholder="代表队" clearable>
          <el-option
            v-for="team in JwTeamList"
            :key="team.createUserId"
            :label="team.teamName"
            :value="team.createUserId"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="姓名" prop="leaderName">
        <el-input
          v-model="queryParams.leaderName"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="人员类型" prop="userType">
        <el-select v-model="queryParams.userType" placeholder="请选择人员类型" clearable>
          <el-option
            v-for="dict in dict.type.jw_team_leader_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['jiewu:JwTeamLeader:add']"
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
          v-hasPermi="['jiewu:JwTeamLeader:edit']"
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
          v-hasPermi="['jiewu:JwTeamLeader:remove']"
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
          v-hasPermi="['jiewu:JwTeamLeader:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="JwTeamLeaderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" width="80" align="center" prop="id"/>
      <el-table-column label="姓名" align="center" prop="leaderName"/>
      <el-table-column label="身份证" align="center" prop="idCard"/>
      <el-table-column label="创建者" align="center" prop="createUserId"/>
      <el-table-column label="电话" align="center" prop="leaderPhone"/>
      <el-table-column label="人员类型" align="center" prop="userType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.jw_team_leader_type" :value="scope.row.userType"/>
        </template>
      </el-table-column>
      <el-table-column label="比赛" align="center" prop="matchId"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['jiewu:JwTeamLeader:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['jiewu:JwTeamLeader:remove']"
          >删除
          </el-button>
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

    <!-- 添加或修改队伍人员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

        <el-form-item label="比赛" prop="matchId">
          <ELSelectMatch :matchId.sync="form.matchId"/>
        </el-form-item>
        <el-form-item label="代表队" prop="teamId">
          <el-select filterable v-model="form.createUserId" placeholder="代表队" clearable>
            <el-option
              v-for="team in JwTeamList"
              :key="team.createUserId"
              :label="team.teamName"
              :value="team.createUserId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="姓名" prop="leaderName">
          <el-input v-model="form.leaderName" placeholder="请输入姓名"/>
        </el-form-item>

        <el-form-item label="电话" prop="leaderPhone">
          <el-input v-model="form.leaderPhone" placeholder="请输入电话"/>
        </el-form-item>

        <el-form-item label="人员类型" prop="userType">
          <el-radio-group v-model="form.userType">
            <el-radio
              v-for="dict in dict.type.jw_team_leader_type"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}
            </el-radio>
          </el-radio-group>
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
  import {listJwTeamLeader, getJwTeamLeader, delJwTeamLeader, addJwTeamLeader, updateJwTeamLeader} from "@/api/jiewu/JwTeamLeader";
  import {listJwTeam} from "@/api/jiewu/JwTeam";

  export default {
    name: "JwTeamLeader",
    dicts: ['jw_team_leader_type'],
    data() {
      return {
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
        // 队伍人员表格数据
        JwTeamLeaderList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          leaderName: null,
          createUserId: null,
          leaderPhone: null,
          createAddId: null,
          userType: null,
          matchId: null
        },
        JwTeamList: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          leaderName: [
            {required: true, message: "姓名不能为空", trigger: "blur"}
          ],
        }
      };
    },
    created() {
      this.getList();
      this.getTeamList()
    },
    methods: {
      getTeamList() {
        listJwTeam({pageNum: 1, pageSize: 5000}).then(response => {
          this.JwTeamList = response.rows;
        });
      },
      /** 查询队伍人员列表 */
      getList() {
        this.loading = true;
        listJwTeamLeader(this.queryParams).then(response => {
          this.JwTeamLeaderList = response.rows;
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
          id: null,
          leaderName: null,
          idCard: null,
          createUserId: null,
          leaderPhone: null,
          createAddId: null,
          userType: null,
          matchId: null
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
        this.title = "添加队伍人员";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getJwTeamLeader(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改队伍人员";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateJwTeamLeader(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addJwTeamLeader(this.form).then(response => {
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
        this.$modal.confirm('是否确认删除队伍人员编号为"' + ids + '"的数据项？').then(function () {
          return delJwTeamLeader(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('jiewu/JwTeamLeader/export', {
          ...this.queryParams
        }, `JwTeamLeader_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
