<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名字" prop="teamName">
        <el-input
          v-model="queryParams.teamName"
          placeholder="代表队名字"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系人" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入联系人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="userPhone">
        <el-input
          v-model="queryParams.userPhone"
          placeholder="请输入联系电话"
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
          v-hasPermi="['jiewu:JwTeam:add']"
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
          v-hasPermi="['jiewu:JwTeam:edit']"
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
          v-hasPermi="['jiewu:JwTeam:remove']"
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
          v-hasPermi="['jiewu:JwTeam:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="JwTeamList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" width="55" align="center" prop="id"/>
      <el-table-column label="名字" align="left" prop="teamName"/>
      <el-table-column label="微信用户" width="72" align="center" prop="createUserId"/>
      <el-table-column label="联系人" align="left" prop="userName"/>
      <el-table-column label="联系电话" align="left" prop="userPhone"/>
      <el-table-column label="邮寄地址" align="left" prop="addr"/>
      <el-table-column label="备注" show-overflow-tooltip align="center" prop="remark"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAddMatch(scope.row)"
            v-hasPermi="['jiewu:JwTeam:edit']"
          >加入比赛
          </el-button>
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
      @pagination="getList"
    />

    <!-- 添加或修改代表队对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名字" prop="teamName">
          <el-input v-model="form.teamName" placeholder="请输入名字"/>
        </el-form-item>
        <el-form-item label="联系人" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入联系人"/>
        </el-form-item>
        <el-form-item label="联系电话" prop="userPhone">
          <el-input v-model="form.userPhone" placeholder="请输入联系电话"/>
        </el-form-item>
        <el-form-item label="邮寄地址" prop="addr">
          <el-input v-model="form.addr" placeholder="请输入邮寄地址"/>
        </el-form-item>
        <el-form-item label="微信用户" prop="createUserId">
          <el-select style="width: 380px" v-model="form.createUserId"
                     filterable
                     clearable
                     remote
                     reserve-keyword
                     placeholder="请输入微信用户"
                     :remote-method="remoteMethodUser"
                     :loading="loadingUser">
            <el-option v-for="item in wxUserList" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="加入比赛" :visible.sync="matchOpen" width="500px" append-to-body>
      <el-form ref="form" :model="matchForm" label-width="80px">
        <el-form-item label="比赛" prop="matchId">
          <ELSelectMatch :matchId.sync="matchForm.matchId"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMatchForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listJwTeam, getJwTeam, delJwTeam, addJwTeam, updateJwTeam} from "@/api/jiewu/JwTeam";
  import { listJwWxUser} from "@/api/jiewu/JwWxUser";
  import { addJwMatchTeam} from "@/api/jiewu/JwMatchTeam";

  export default {
    name: "JwTeam",
    data() {
      return {
        matchOpen: false,
        matchForm: {},
        wxUserList: [],
        loadingUser: false,
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
        // 代表队表格数据
        JwTeamList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          teamName: null,
          userName: null,
          userPhone: null,
          addr: null,
          createUserId: null,
          matchId: null
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          teamName: [
            { required: true, message: "代表队名字不能为空", trigger: "blur" }
          ],
          createUserId: [
            { required: true, message: "微信用户不能为空", trigger: "change" }
          ],
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      remoteMethodUser(query) {
        if (query !== '') {
          this.loadingUser = true;
          listJwWxUser({
            name: query,
            pageNum: 1,
            pageSize: 50,
          }).then(response => {
            this.wxUserList = response.rows;
            this.loadingUser = false;
          });
        } else {
          this.wxUserList = [];
        }
      },
      /** 查询代表队列表 */
      getList() {
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
        this.matchOpen = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: null,
          teamName: null,
          userName: null,
          userPhone: null,
          addr: null,
          createTime: null,
          updateTime: null,
          remark: null,
          createUserId: null,
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
        this.title = "添加代表队";
      },
      submitMatchForm(){
        if(!this.matchForm.matchId){
          this.$modal.msgError("请选择比赛");
          return;
        }
        addJwMatchTeam(this.matchForm).then(res=>{
          this.$modal.msgSuccess("加入成功");
          this.matchOpen = false;
        })
      },
      handleAddMatch(row){
        this.matchForm = {teamId: row.id, matchId: null}
        this.matchOpen = true;
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getJwTeam(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改代表队";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateJwTeam(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addJwTeam(this.form).then(response => {
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
        this.$modal.confirm('是否确认删除代表队编号为"' + ids + '"的数据项？').then(function () {
          return delJwTeam(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('jiewu/JwTeam/export', {
          ...this.queryParams
        }, `JwTeam_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
