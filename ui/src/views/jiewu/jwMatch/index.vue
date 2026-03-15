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
      <el-table-column label="比赛名称" width="200" align="left" prop="matchName"/>
      <el-table-column label="管理员" width="64" align="center">
        <template slot-scope="scope">
          <el-link :underline="false" type="primary" style="font-weight: 600" @click="setManager(scope.row)">设置</el-link>
        </template>
      </el-table-column>
      <el-table-column label="管理密码" align="center" prop="manageCode"/>
      <el-table-column label="邀请码" align="center" prop="invitationCode"/>
      <el-table-column label="列表" show-overflow-tooltip align="center" prop="invitationList"/>
      <el-table-column label="开始时间" align="center" prop="beginTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="报名开始时间" align="center" prop="signBeginTime" width="150">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.signBeginTime, '{y}-{m}-{d}  {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="报名结束时间" align="center" prop="signEndTime" width="150">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.signEndTime, '{y}-{m}-{d}  {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="地址" show-overflow-tooltip align="left" prop="addr"/>
      <el-table-column label="盖章单位" show-overflow-tooltip align="center" prop="sealUnit"/>
      <el-table-column label="起始背号" show-overflow-tooltip align="center" prop="startBackNum"/>
      <el-table-column label="状态" align="center" prop="state">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.jw_match_state" :value="scope.row.state"/>
        </template>
      </el-table-column>
      <!--      <el-table-column label="海报" align="center" prop="posterImg" width="100">-->
      <!--        <template slot-scope="scope">-->
      <!--          <image-preview :src="scope.row.posterImg" :width="50" :height="50"/>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="主屏" align="center" prop="mainImg" width="100">-->
      <!--        <template slot-scope="scope">-->
      <!--          <image-preview :src="scope.row.mainImg" :width="50" :height="50"/>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="对阵屏" align="center" prop="battleImg" width="100">-->
      <!--        <template slot-scope="scope">-->
      <!--          <image-preview :src="scope.row.battleImg" :width="50" :height="50"/>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column label="是否显示" align="center" prop="isShow">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.isShow"/>
        </template>
      </el-table-column>
      <el-table-column label="显示成绩" align="center" prop="isShowGrade">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.isShowGrade"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" align="center" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            style="padding: 6px;"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['jiewu:jwMatch:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="danger"
            style="padding: 6px;"
            @click="handleDelete(scope.row)"
            v-hasPermi="['jiewu:jwMatch:remove']"
          >删除
          </el-button>
          <el-button
            size="mini"
            type="primary"
            style="padding: 6px;"
            @click="genPdf(scope.row)"
          >生成背号PDF
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
    <el-dialog :title="title" :visible.sync="open" width="1100px" append-to-body :close-on-click-modal=false>
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
              <el-input type="textarea" :rows="2" v-model="form.sealUnit" placeholder="请输入盖章单位"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="state">
              <el-radio-group v-model="form.state">
                <el-radio
                  v-for="dict in dict.type.jw_match_state"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="是否显示" prop="isShow">
              <el-radio-group v-model="form.isShow">
                <el-radio
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="起始背号" prop="startBackNum">
              <el-input-number size="mini" v-model="form.startBackNum" controls-position="right" :min="1"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="邀请码" prop="invitationCode">
              <el-input v-model="form.invitationCode" placeholder="请输入邀请码"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="管理密码" prop="manageCode">
              <el-input v-model="form.manageCode" placeholder="请输入管理密码"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider>配置</el-divider>

        <el-form-item label="投屏海选成绩" prop="matchConfig.grade" v-if="form.matchConfig && form.matchConfig.grade">
          <el-checkbox v-model="form.matchConfig.grade.rankOrder">排名</el-checkbox>
          <el-checkbox v-model="form.matchConfig.grade.backNum">背号</el-checkbox>
          <el-checkbox v-model="form.matchConfig.grade.sport">选手</el-checkbox>
          <el-checkbox v-model="form.matchConfig.grade.worksName">作品名称</el-checkbox>
          <el-checkbox v-model="form.matchConfig.grade.teamName">代表队</el-checkbox>
          <el-checkbox v-model="form.matchConfig.grade.avgScore">成绩(平均分)</el-checkbox>
          <el-checkbox v-model="form.matchConfig.grade.allScore">成绩(总分)</el-checkbox>
          <el-checkbox v-model="form.matchConfig.grade.rankOrderDes">成绩(奖项)</el-checkbox>
        </el-form-item>

        <el-form-item label="投屏晋级名单" prop="matchConfig.jinji" v-if="form.matchConfig && form.matchConfig.jinji">
          <el-checkbox v-model="form.matchConfig.jinji.rankOrder">排名</el-checkbox>
          <el-checkbox v-model="form.matchConfig.jinji.backNum">背号</el-checkbox>
          <el-checkbox v-model="form.matchConfig.jinji.sport">选手</el-checkbox>
          <el-checkbox v-model="form.matchConfig.jinji.teamName">代表队</el-checkbox>
        </el-form-item>

        <el-form-item label="海选打分方式" prop="matchConfig.hScoreMode" v-if="form.matchConfig && form.matchConfig.hScoreMode">
          <el-radio-group v-model="form.matchConfig.hScoreMode">
            <el-radio key="1" label="1">键盘输入</el-radio>
            <el-radio key="2" label="2">拖动</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-row>
          <el-col :span="8">
            <el-form-item label="海选一轮上几人" prop="matchConfig.hMatchCount" v-if="form.matchConfig && form.matchConfig.hMatchCount">
              <el-radio-group v-model="form.matchConfig.hMatchCount">
                <el-radio key="1" label="1">1人</el-radio>
                <el-radio key="2" label="2">2人</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="每场间隔时间" prop="matchConfig.scheduleTime" v-if="form.matchConfig && form.matchConfig.scheduleTime">
              <el-input-number size="mini" v-model="form.matchConfig.scheduleTime" controls-position="right" :min="1"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="对阵几个场地" prop="matchConfig.pkPlaceCount" v-if="form.matchConfig && form.matchConfig.pkPlaceCount">
              <el-radio-group v-model="form.matchConfig.pkPlaceCount">
                <el-radio :key="1" :label="1">1个</el-radio>
                <el-radio :key="2" :label="2">2个</el-radio>
                <el-radio :key="4" :label="4">2个</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider>图片</el-divider>
        <el-row>
          <el-col :span="6">
            <el-form-item label="海报" prop="posterImg">
              <image-upload :limit="200" v-model="form.posterImg"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="赛事详情" prop="matchDetails">
              <image-upload :limit="200" v-model="form.matchDetails"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="竞赛规程" prop="matchRegulations">
              <image-upload :limit="200" v-model="form.matchRegulations"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="群二维码" prop="qunCode">
              <image-upload :limit="1" v-model="form.qunCode"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="主屏" prop="mainImg">
              <image-upload :limit="1" v-model="form.mainImg"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="对阵屏" prop="battleImg">
              <image-upload :limit="1" v-model="form.battleImg"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="显示成绩" prop="isShowGrade">
              <el-radio-group v-model="form.isShowGrade">
                <el-radio
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 修改管理员 -->
    <el-dialog title="修改管理员" :visible.sync="matchOpen" width="500px" append-to-body>
      <el-form ref="form" :model="userform" :rules="rules" label-width="80px">
        <el-form-item label="名字" prop="matchName">
          <el-input v-model="userform.matchName" readonly/>
        </el-form-item>

        <el-form-item label="微信用户" prop="userIds">
          <el-select style="width: 380px" v-model="userform.userIds"
                     filterable
                     clearable
                     remote
                     multiple
                     reserve-keyword
                     placeholder="请输入微信用户"
                     :remote-method="remoteMethodUser"
                     :loading="loadingUser">
            <el-option v-for="item in wxUserList" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUserForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {listJwMatch, getJwMatch, delJwMatch, addJwMatch, updateJwMatch, genBackNumPDF} from "@/api/jiewu/jwMatch";
  import {listJwMatchUser, addJwMatchUser} from "@/api/jiewu/JwMatchUser";
  import {listJwWxUser} from "@/api/jiewu/JwWxUser";

  export default {
    name: "JwMatch",
    dicts: ['jw_match_state', 'sys_yes_no'],
    data() {
      return {
        userform: {},
        matchOpen: false,
        wxUserList: [],
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
        loadingUser: false,
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
          state: null,
          orderByColumn: "id", isAsc: "descending"
        },
        // 表单参数
        form: {matchConfig: {grade: {}, jinji: {}}},
        // 表单校验
        rules: {}
      };
    },
    created() {
      this.getList();
    },
    methods: {
      getInitConfing() {
        return {
          grade: {
            rankOrder: true,
            backNum: true,
            sport: true,
            teamName: true,
            avgScore: true,
            worksName: false,
            allScore: false,
            rankOrderDes: false,
          }, jinji: {
            rankOrder: true,
            backNum: true,
            sport: true,
            teamName: true,
          },
          hScoreMode: "1",
          hMatchCount: "1",
          scheduleTime: 2,
          pkPlaceCount: 1
        }
      },
      genPdf(row) {
        genBackNumPDF({id: row.id}).then(res => {

        })
      },
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
      submitUserForm() {
        let that = this;
        addJwMatchUser(this.userform).then(res => {
          that.getList();
          that.matchOpen = false;
        })
      },
      // 设置管理员
      setManager(row) {
        let that = this;
        this.wxUserList = [];
        listJwMatchUser({matchId: row.id}).then(res => {
          let userIds = (res.rows || []).map(item => Number(item.userId));
          that.userform = {matchId: row.id, matchName: row.matchName, userIds: userIds};
          that.matchOpen = true;
        });
      },
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
        this.matchOpen = false;
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
          matchRegulations: null,
          invitationCode: null,
          invitationList: null,
          isShowGrade: null,
          manageCode: null,
          qunCode: null,
          matchConfig: this.getInitConfing()
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
          if (!this.form.matchConfig) {
            this.form.matchConfig = this.getInitConfing();
          } else {
            this.form.matchConfig = JSON.parse(this.form.matchConfig || "{}");
          }
          this.open = true;
          this.title = "修改赛事管理";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.matchConfig = JSON.stringify(this.form.matchConfig);
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
