<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="比赛" prop="matchId">
        <ELSelectMatch :matchId.sync="queryParams.matchId"/>
      </el-form-item>
      <el-form-item label="组别" prop="gameItemId">
        <el-select style="width: 360px" filterable v-model="queryParams.gameItemId" placeholder="组别" clearable>
          <el-option
            v-for="gameItem in JwGameItemList"
            :key="gameItem.id"
            :label="gameItem.code + ' : ' + gameItem.name"
            :value="gameItem.id"
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
          v-hasPermi="['jiewu:JwHaiScore:add']"
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
          v-hasPermi="['jiewu:JwHaiScore:edit']"
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
          v-hasPermi="['jiewu:JwHaiScore:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['jiewu:JwHaiScore:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--<el-table v-loading="loading" :data="JwHaiScoreList" @selection-change="handleSelectionChange">-->
      <!--<el-table-column type="selection" width="55" align="center" />-->
      <!--<el-table-column label="${comment}" align="center" prop="id" />-->
      <!--<el-table-column label="比赛ID" align="center" prop="matchId" />-->
      <!--<el-table-column label="组别" align="center" prop="gameItemId" />-->
      <!--<el-table-column label="赛程小项" align="center" prop="scheduleItemId" />-->
      <!--<el-table-column label="${comment}" align="center" prop="sportId" />-->
      <!--<el-table-column label="${comment}" align="center" prop="judgeId" />-->
      <!--<el-table-column label="${comment}" align="center" prop="score" />-->
      <!--<el-table-column label="${comment}" align="center" prop="scoreType" />-->
      <!--<el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
        <!--<template slot-scope="scope">-->
          <!--<el-button-->
            <!--size="mini"-->
            <!--type="text"-->
            <!--icon="el-icon-edit"-->
            <!--@click="handleUpdate(scope.row)"-->
            <!--v-hasPermi="['jiewu:JwHaiScore:edit']"-->
          <!--&gt;修改</el-button>-->
          <!--<el-button-->
            <!--size="mini"-->
            <!--type="text"-->
            <!--icon="el-icon-delete"-->
            <!--@click="handleDelete(scope.row)"-->
            <!--v-hasPermi="['jiewu:JwHaiScore:remove']"-->
          <!--&gt;删除</el-button>-->
        <!--</template>-->
      <!--</el-table-column>-->
    <!--</el-table>-->
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改海选打分对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="${comment}" prop="sportId">
          <el-input v-model="form.sportId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="judgeId">
          <el-input v-model="form.judgeId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="score">
          <el-input v-model="form.score" placeholder="请输入${comment}" />
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
import { listJwHaiScore, getJwHaiScore, delJwHaiScore, addJwHaiScore, updateJwHaiScore } from "@/api/jiewu/JwHaiScore";
import {listJwGameItem} from "@/api/jiewu/JwGameItem";
export default {
  name: "JwHaiScore",
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
      // 海选打分表格数据
      JwHaiScoreList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      JwGameItemList: [],
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
      rules: {
      }
    };
  },
  created() {
    this.getGameItemList()
    this.getList();

  },
  watch: {
    "queryParams.matchId": function (val) {
      this.getGameItemList()
    },
  },
  methods: {
    getGameItemName(row) {
      let item = this.JwGameItemList.find((item) => item.id == row.gameItemId);
      return item ? item.code + ":" + item.name : "";
    },
    getGameItemList() {
      listJwGameItem({matchId: this.queryParams.matchId, pageNum: 1, pageSize: 5000}).then(response => {
        this.JwGameItemList = response.rows;
      });
    },
    /** 查询海选打分列表 */
    getList() {
      this.loading = true;
      listJwHaiScore(this.queryParams).then(response => {
        this.JwHaiScoreList = response.rows;
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
      this.single = selection.length!==1
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
      this.$modal.confirm('是否确认删除海选打分编号为"' + ids + '"的数据项？').then(function() {
        return delJwHaiScore(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
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
