<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          placeholder="请输入手机号"
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
          v-hasPermi="['jiewu:JwWxUser:add']"
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
          v-hasPermi="['jiewu:JwWxUser:edit']"
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
          v-hasPermi="['jiewu:JwWxUser:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['jiewu:JwWxUser:export']"
        >导出</el-button>

        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAddTeam"
            v-hasPermi="['jiewu:JwWxUser:add']"
          >同步创建队伍</el-button>
        </el-col>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="JwWxUserList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" width="60" align="center" prop="id" />
      <el-table-column label="姓名" show-overflow-tooltip align="center" prop="name" />
      <el-table-column label="手机号" align="center" prop="mobile" />
      <!--<el-table-column label="性别" align="center" prop="sex">-->
        <!--<template slot-scope="scope">-->
          <!--<dict-tag :options="dict.type.jw_sex" :value="scope.row.sex"/>-->
        <!--</template>-->
      <!--</el-table-column>-->
      <el-table-column label="头像" align="center" prop="avatar" width="100">
        <template slot-scope="scope">
          <image-preview v-if="scope.row.avatar" :src="scope.row.avatar" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="OpenID" show-overflow-tooltip align="center" prop="openId" />
      <el-table-column label="创建时间" show-overflow-tooltip align="center" prop="createTime" />
      <!--<el-table-column label="备注" align="center" prop="remark" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['jiewu:JwWxUser:edit']"
          >修改</el-button>
          <!--<el-button-->
            <!--size="mini"-->
            <!--type="text"-->
            <!--icon="el-icon-delete"-->
            <!--@click="handleDelete(scope.row)"-->
            <!--v-hasPermi="['jiewu:JwWxUser:remove']"-->
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

    <!-- 添加或修改微信用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号" />
        </el-form-item>
        <!--<el-form-item label="性别" prop="sex">-->
          <!--<el-radio-group v-model="form.sex">-->
            <!--<el-radio-->
              <!--v-for="dict in dict.type.jw_sex"-->
              <!--:key="dict.value"-->
              <!--:label="dict.value"-->
            <!--&gt;{{dict.label}}</el-radio>-->
          <!--</el-radio-group>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="头像" prop="avatar">-->
          <!--<image-upload v-model="form.avatar"/>-->
        <!--</el-form-item>-->
        <el-form-item label="OpenID" prop="openId">
          <el-input v-model="form.openId" readonly placeholder="请输入微信OpenID" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listJwWxUser, getJwWxUser, delJwWxUser, addJwWxUser, updateJwWxUser } from "@/api/jiewu/JwWxUser";
import { addJwTeam} from "@/api/jiewu/JwTeam";

export default {
  name: "JwWxUser",
  dicts: ['jw_sex'],
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
      // 微信用户表格数据
      JwWxUserList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        mobile: null,
        sex: null,
        avatar: null,
        openId: null,
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
    /** 查询微信用户列表 */
    getList() {
      this.loading = true;
      listJwWxUser(this.queryParams).then(response => {
        this.JwWxUserList = response.rows;
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
        name: null,
        mobile: null,
        sex: null,
        avatar: null,
        openId: null,
        createTime: null,
        updateTime: null,
        remark: null
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
      this.single = selection.length !== 1;
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.form.openId = this.generateRandomString(20)+new Date().getTime();
      this.open = true;
      this.title = "添加微信用户";


      // const units = [
      //   "1502","foxdance","JR舞蹈","JS舞蹈西航港校区","北星街舞","布吉街舞","极舞","嘉州舞馆","李婷婷","天府新区实验小学",
      //   "米奇奥街舞西村校区","米奇奥龙泉校区","魔力舞士","欧耶舞蹈","树高街舞","吾空舞蹈"
      //
      // ];
      const units = [
        "chenlong","SD仪街舞","爱舞艺术","步客街舞","超元素街舞","成都市东光实验小学",
        "街本街街舞","南充am街舞","南充道鑫双语学校","南充轻舞飞扬艺术学校",
        "南充一中附属小学","舞星街舞","西充舞飞扬舞蹈艺术学校","星动力街舞", "炫酷街舞", "营山嘉昱艺术学校", "营山有你街舞"
      ];


      // units.forEach(item=>{
      //   let formm = {openId: this.generateRandomString(20)+new Date().getTime(), name: item};
      //
      //   addJwWxUser(formm).then(response => {
      //
      //     this.getList();
      //   });
      // })


    },
    generateRandomString(length) {
      const characters = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
      let result = '';
      for (let i = 0; i < length; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length);
        result += characters[randomIndex];
      }
      return result;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getJwWxUser(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改微信用户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateJwWxUser(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addJwWxUser(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleAddTeam(){
      const ids = this.ids || [];
      this.JwWxUserList.forEach(user=>{
        if(ids.indexOf(user.id) >= 0){
          addJwTeam({createUserId: user.id, teamName: user.name})
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除微信用户编号为"' + ids + '"的数据项？').then(function() {
        return delJwWxUser(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('jiewu/JwWxUser/export', {
        ...this.queryParams
      }, `JwWxUser_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
