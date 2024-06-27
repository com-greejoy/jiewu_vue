<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="judgeName">
        <el-input
          v-model="queryParams.judgeName"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row>
      <el-col :span="15">
        <el-card class="box-card" style="height: calc(100vh - 180px);">
          <div slot="header" class="clearfix">
            <span>系统裁判</span>
            <el-button type="primary" style="float: right; " size="mini" icon="el-icon-plus" @click="handleAdd" circle></el-button>
          </div>
          <div v-if="showJwJudgeList && showJwJudgeList.length > 0" class="judge-items">
              <div class="judge-item"   v-for="judge in showJwJudgeList" :key="judge.id">
                <i class="el-icon-right" @click="toMatch(judge)"></i>
                <image-preview v-if="judge.img" :src="judge.img" :width="50" :height="50" :radius="25"/>
                <img v-if="!judge.img" src="@/assets/images/nouser.png" fit="cover" :style="`width:50px;height:50px;border-radius: 25px`" />
                <el-link class="title" :underline="false" type="primary" @click="handleUpdate(judge)">{{ judge.judgeName }}</el-link>
              </div>
          </div>
          <el-empty v-if="!showJwJudgeList || showJwJudgeList.length == 0" description="暂无裁判"></el-empty>
        </el-card>
      </el-col>
      <el-col :span="1" style="height: 2px">

      </el-col>
      <el-col :span="8">
        <el-card class="box-card" style="height: calc(100vh - 180px);">
          <div slot="header" class="clearfix">
            <span>比赛裁判</span>
            <ELSelectMatch style="margin-left: 12px" :matchId.sync="matchId"/>
          </div>
          <div v-if="matchJudgeList && matchJudgeList.length > 0" class="judge-items">
            <div class="judge-item" v-for="judge in matchJudgeList" :key="judge.id">
              <i class="el-icon-back" style="color:#F56C6C;" @click="leaveMatch(judge)"></i>
              <image-preview v-if="judge.img" :src="judge.img" :width="50" :height="50" :radius="25"/>
              <img v-if="!judge.img" src="@/assets/images/nouser.png" fit="cover" :style="`width:50px;height:50px;border-radius: 25px`" />
              <el-link class="title" :underline="false" type="primary" @click="handleUpdate(judge)">{{ judge.judgeName }}</el-link>
            </div>
          </div>
          <el-empty v-if="!matchJudgeList || matchJudgeList.length == 0" description="暂无裁判"></el-empty>
        </el-card>
      </el-col>

    </el-row>

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="judgeName">
          <el-input v-model="form.judgeName" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="头像" prop="img">
          <image-upload v-model="form.img"/>
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

  import { listJwJudge, getJwJudge, delJwJudge, addJwJudge, updateJwJudge} from "@/api/jiewu/JwJudge";
  import { listJwJudgeMatch, delJwJudgeMatch, addJwJudgeMatch} from "@/api/jiewu/JwJudgeMatch";

  export default {
    name: "JwJudge",
    data() {
      return {
        matchId: (this.Cookies.get("matchId") * 1) || null,
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
        // 打分裁判表格数据
        JwJudgeList: [],
        matchJudgeList: [],

        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 1000,
          judgeName: null,
          judgeType: null,
          img: null
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {}
      };
    },
    watch: {
      "matchId": function (val) {
        this.getMatchJudgeList()
      },
    },
    created() {
      this.getList();
      this.getMatchJudgeList();

    },
    computed: {
      showJwJudgeList() {
        let showJwJudgeList = [];
        this.JwJudgeList.forEach(item=>{
          let m = this.matchJudgeList.find((aa) => aa.id == item.id);
          if(!m || !m.id)showJwJudgeList.push(item)
        });
        return showJwJudgeList;
      }
    },
    methods: {
      leaveMatch(judge){
        delJwJudgeMatch({matchId: this.matchId, judgeId: judge.id}).then(res=>{
          this.getMatchJudgeList();
        })
      },
      toMatch(judge){
        addJwJudgeMatch({matchId: this.matchId, judgeId: judge.id}).then(res=>{
          this.getMatchJudgeList();
        })
      },
      getMatchJudgeList(){
        this.loading = true;
        listJwJudgeMatch({matchId: this.matchId}).then(response=>{
          this.matchJudgeList = response.rows || [];
          this.matchJudgeList.sort((a,b)=> a.judgeId-b.judgeId)
          this.loading = false;
        })
      },
      getList() {
        this.loading = true;
        listJwJudge(this.queryParams).then(response => {
          this.JwJudgeList = response.rows || [];
          this.JwJudgeList.sort((a,b)=> a.judgeId-b.judgeId)
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
          judgeName: null,
          judgeType: null,
          img: null
        };
        this.resetForm("form");
      },
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加打分裁判";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getJwJudge(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改打分裁判";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateJwJudge(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
                this.getMatchJudgeList();
              });
            } else {
              addJwJudge(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
                this.getMatchJudgeList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$modal.confirm('是否确认删除打分裁判编号为"' + ids + '"的数据项？').then(function () {
          return delJwJudge(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('jiewu/JwJudge/export', {
          ...this.queryParams
        }, `JwJudge_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
<style lang="scss" scoped>
  .judge-items {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    max-height: calc(100vh - 268px);
    overflow: auto;

    .judge-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      margin-right: 24px;
      margin-bottom: 24px;
      i{
        font-weight: 600;
        color: #E6A23C;
        font-size: 20px;
        align-self: flex-end;
        cursor: pointer;
      }
      .title {
        margin-top: 8px;
      }
    }
  }
</style>

