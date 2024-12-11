<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="描述" prop="awardName">
        <el-input
          v-model="queryParams.awardName"
          placeholder="请输入描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="奖项" prop="rankText">
        <el-input
          v-model="queryParams.rankText"
          placeholder="请输入奖项"
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
          v-hasPermi="['jiewu:JwAwardsItem:add']"
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
          v-hasPermi="['jiewu:JwAwardsItem:edit']"
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
          v-hasPermi="['jiewu:JwAwardsItem:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['jiewu:JwAwardsItem:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="JwAwardsItemList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="描述" align="center" prop="awardName" />
      <el-table-column label="开始名次" align="center" prop="rankStart" />
      <el-table-column label="结束名次" align="center" prop="rankEnd" />
      <el-table-column label="百分比开始" align="center" prop="proportionStart" />
      <el-table-column label="百分比结束" align="center" prop="proportionEnd" />
      <el-table-column label="奖项" align="center" prop="rankText" />
      <el-table-column label="奖杯" align="center" prop="jiangBei" />
      <el-table-column label="奖牌" align="center" prop="jiangPai" />
      <el-table-column label="证书" align="center" prop="zhengShu" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['jiewu:JwAwardsItem:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['jiewu:JwAwardsItem:remove']"
          >删除</el-button>
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

    <!-- 添加或修改奖项设置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="描述" prop="awardName">
          <el-input v-model="form.awardName" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="名次" prop="rankStart">
          <el-input-number size="mini" v-model="form.rankStart" controls-position="right" :min="0"/>
          -
          <el-input-number size="mini" v-model="form.rankEnd" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item label="百分比" prop="proportionStart">
          <el-input-number size="mini" v-model="form.proportionStart" controls-position="right" :min="0"/>
          -
          <el-input-number size="mini" v-model="form.proportionEnd" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item label="奖项" prop="rankText">
          <el-input v-model="form.rankText" placeholder="请输入奖项" />
        </el-form-item>
        <el-form-item label="奖杯" prop="jiangBei">
          <el-input-number size="mini" v-model="form.jiangBei" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item label="奖牌" prop="jiangPai">
          <el-input v-model="form.jiangPai" placeholder="请输入奖牌" />
        </el-form-item>
        <el-form-item label="证书" prop="zhengShu">
          <el-input v-model="form.zhengShu" placeholder="请输入证书" />
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
import { listJwAwardsItem, getJwAwardsItem, delJwAwardsItem, addJwAwardsItem, updateJwAwardsItem } from "@/api/jiewu/JwAwardsItem";

export default {
  name: "JwAwardsItem",
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
      // 奖项设置表格数据
      JwAwardsItemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        awardName: null,
        rankStart: null,
        rankEnd: null,
        proportionStart: null,
        proportionEnd: null,
        rankText: null,
        jiangBei: null,
        jiangPai: null,
        zhengShu: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询奖项设置列表 */
    getList() {
      this.loading = true;
      listJwAwardsItem(this.queryParams).then(response => {
        this.JwAwardsItemList = response.rows;
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
        awardName: null,
        rankStart: null,
        rankEnd: null,
        proportionStart: null,
        proportionEnd: null,
        rankText: null,
        jiangBei: 0,
        jiangPai: 0,
        zhengShu: 0
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
      this.title = "添加奖项设置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getJwAwardsItem(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改奖项设置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateJwAwardsItem(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addJwAwardsItem(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除奖项设置编号为"' + ids + '"的数据项？').then(function() {
        return delJwAwardsItem(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('jiewu/JwAwardsItem/export', {
        ...this.queryParams
      }, `JwAwardsItem_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
