<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="比赛名称" prop="matchName">
        <el-input
          v-model="queryParams.matchName"
          placeholder="请输入比赛名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="state">
        <el-select v-model="queryParams.state" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.jw_match_state"
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
          v-hasPermi="['jiewu:jwMatch:add']"
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
          v-hasPermi="['jiewu:jwMatch:edit']"
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
          v-hasPermi="['jiewu:jwMatch:remove']"
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
          v-hasPermi="['jiewu:jwMatch:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="jwMatchList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" align="center" prop="id"/>
      <el-table-column label="比赛名称" align="center" prop="matchName"/>
      <el-table-column label="开始时间" align="center" prop="beginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="报名开始时间" align="center" prop="signBeginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.signBeginTime, '{y}-{m}-{d}  {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="报名结束时间" align="center" prop="signEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.signEndTime, '{y}-{m}-{d}  {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="地址" align="center" prop="addr"/>
      <el-table-column label="盖章单位" show-overflow-tooltip align="center" prop="sealUnit" />
      <el-table-column label="海报" align="center" prop="posterImg" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.posterImg" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="state">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.jw_match_state" :value="scope.row.state"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['jiewu:jwMatch:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['jiewu:jwMatch:remove']"
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

    <!-- 添加或修改赛事管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1100px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="比赛名称" prop="matchName">
              <el-input v-model="form.matchName" placeholder="请输入比赛名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地址" prop="addr">
              <el-input v-model="form.addr" placeholder="请输入地址"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="beginTime">
              <el-date-picker clearable
                              v-model="form.beginTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择开始时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker clearable
                              v-model="form.endTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择结束时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="报名开始时间" prop="signBeginTime">
              <el-date-picker clearable
                              v-model="form.signBeginTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择报名开始时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报名结束时间" prop="signEndTime">
              <el-date-picker clearable
                              v-model="form.signEndTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择报名结束时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="盖章单位" prop="sealUnit">
              <el-input v-model="form.sealUnit" placeholder="请输入盖章单位" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="state">
              <el-radio-group v-model="form.state">
                <el-radio
                  v-for="dict in dict.type.jw_match_state"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row>
          <el-col :span="8">
            <el-form-item label="海报" prop="posterImg">
              <image-upload v-model="form.posterImg"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="赛事详情" prop="matchDetails">
              <image-upload :limit="10" v-model="form.matchDetails"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="竞赛规程" prop="matchRegulations">
              <image-upload :limit="10" v-model="form.matchRegulations"/>

            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {listJwMatch, getJwMatch, delJwMatch, addJwMatch, updateJwMatch} from "@/api/jiewu/jwMatch";

  export default {
    name: "JwMatch",
    dicts: ['jw_match_state'],
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
        // 赛事管理表格数据
        jwMatchList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          matchName: null,
          beginTime: null,
          endTime: null,
          signBeginTime: null,
          signEndTime: null,
          addr: null,
          posterImg: null,
          state: null
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {}
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询赛事管理列表 */
      getList() {
        this.loading = true;
        listJwMatch(this.queryParams).then(response => {
          this.jwMatchList = response.rows;
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
          matchName: null,
          beginTime: null,
          endTime: null,
          signBeginTime: null,
          signEndTime: null,
          addr: null,
          posterImg: null,
          state: null,
          matchDetails: null,
          matchRegulations: null
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
        this.title = "添加赛事管理";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getJwMatch(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改赛事管理";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateJwMatch(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addJwMatch(this.form).then(response => {
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
        this.$modal.confirm('是否确认删除赛事管理编号为"' + ids + '"的数据项？').then(function () {
          return delJwMatch(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('jiewu/jwMatch/export', {
          ...this.queryParams
        }, `jwMatch_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
