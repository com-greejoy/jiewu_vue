<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="比赛" prop="matchId">
        <ELSelectMatch :matchId.sync="queryParams.matchId"/>
      </el-form-item>
      <el-form-item label="编号" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入项目名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="比赛模式" prop="matchType">
        <el-select v-model="queryParams.matchType" placeholder="请选择比赛模式" clearable>
          <el-option
            v-for="dict in dict.type.jw_match_type"
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
          v-hasPermi="['jiewu:JwGameItem:add']"
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
          v-hasPermi="['jiewu:JwGameItem:edit']"
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
          v-hasPermi="['jiewu:JwGameItem:remove']"
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
          v-hasPermi="['jiewu:JwGameItem:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="JwGameItemList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <!--<el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="比赛" show-overflow-tooltip align="left" prop="matchName"/>
      <el-table-column label="编号" align="left" width="55" prop="code"/>
      <el-table-column label="项目名" width="260" align="left" prop="name"/>
      <el-table-column label="分组数" align="center" prop="groupLimit"/>
      <el-table-column label="分组模式" width="120" align="center" prop="groupMode">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.jw_group_mode" :value="scope.row.groupMode"/>
        </template>
      </el-table-column>
      <el-table-column label="报名数" align="center" prop="signCount">
        <template slot-scope="scope">
          <el-link :underline="false" @click="viewSignRecord(scope.row)" type="primary">{{scope.row.signCount}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="时长(秒)" align="center" prop="singleDuration"/>
      <el-table-column label="比赛模式" align="center" prop="matchType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.jw_match_type" :value="scope.row.matchType"/>
        </template>
      </el-table-column>
      <el-table-column label="项目类型" align="center" prop="sportLimit">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.jw_sport_limit" :value="scope.row.sportLimit"/>
        </template>
      </el-table-column>
      <el-table-column label="人数限制" align="center" prop="maxSport">
        <template slot-scope="scope">
          {{scope.row.minSport}}-{{scope.row.maxSport}}
        </template>
      </el-table-column>
      <el-table-column label="晋级人数" align="center" prop="promotionNum"/>
      <el-table-column label="最小年龄" align="center" prop="minYear"/>
      <el-table-column label="最大年龄" align="center" prop="maxYear"/>
      <el-table-column label="性别" align="center" prop="matchType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.jw_sex" :value="scope.row.sexCon"/>
        </template>
      </el-table-column>
      <el-table-column label="报名费" align="center" prop="fee"/>

      <el-table-column label="成绩奖项" align="center" prop="resultDesId">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.resultDesId"/>
        </template>
      </el-table-column>
      <el-table-column label="投屏背景图" align="center" prop="screenImg" width="100">
        <template slot-scope="scope">
          <image-preview v-if="scope.row.screenImg" :src="scope.row.screenImg" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="操作" width="180" fixed="right" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            style="padding: 6px;"
            size="mini"
            type="warning"
            @click="handleReGroup(scope.row)"
            v-hasPermi="['jiewu:JwGameItem:edit']"
          >重新分组
          </el-button>
          <el-button
            style="padding: 6px;"
            size="mini"
            type="success"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['jiewu:JwGameItem:edit']"
          >修改
          </el-button>
          <el-button
            style="padding: 6px;"
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['jiewu:JwGameItem:remove']"
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

    <!-- 添加或修改比赛项目对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="75vw" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="比赛" prop="matchId">
              <ELSelectMatch :matchId.sync="form.matchId"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="编号" prop="code">
              <el-input v-model="form.code" placeholder="请输入编号"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目名" prop="name">
              <el-input v-model="form.name" placeholder="请输入项目名"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="比赛模式" prop="matchType">
              <el-radio-group v-model="form.matchType">
                <el-radio
                  v-for="dict in dict.type.jw_match_type"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="晋级人数" prop="promotionNum">
              <el-input v-model="form.promotionNum" placeholder="请输入晋级人数"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="报名费" prop="fee">
              <el-input style="width: 140px" v-model="form.fee" placeholder="请输入报名费"/>
              最多人数
              <el-input-number size="small" v-model="form.feeMaxSport" controls-position="right" :min="1"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="项目类型" prop="sportLimit">
              <el-radio-group v-model="form.sportLimit">
                <el-radio
                  v-for="dict in dict.type.jw_sport_limit"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="分组数" prop="groupLimit">
              <el-input-number size="small" v-model="form.groupLimit" controls-position="right" :min="1"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="选手时长(秒)" prop="singleDuration">
              <el-input-number size="small" v-model="form.singleDuration" controls-position="right" :min="1"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>

          <el-col :span="8">
            <el-form-item label="人数限制" prop="maxSport">
              <el-input-number size="small" v-model="form.minSport" controls-position="right" :min="1"/>
              -
              <el-input-number size="small" v-model="form.maxSport" controls-position="right" :min="1"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年龄限制" prop="minYear">
              <el-input-number size="small" v-model="form.minYear" controls-position="right" :min="1"/>
              -
              <el-input-number size="small" v-model="form.maxYear" controls-position="right" :min="1"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="性别限制" prop="sexCon">
              <el-radio-group v-model="form.sexCon">
                <el-radio
                  v-for="dict in dict.type.jw_sex"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <!--<el-form-item label="年龄限制" prop="minYear">-->
            <!--<el-input-number size="small" v-model="form.minYear" controls-position="right" :min="1" /> - -->
            <!--<el-input-number size="small" v-model="form.maxYear" controls-position="right" :min="1" />-->
            <!--</el-form-item>-->
          </el-col>
          <el-col :span="8">

          </el-col>
          <el-col :span="8">

          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="成绩奖项" prop="resultDesId">
              <el-radio-group v-model="form.resultDesId">
                <el-radio
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="parseInt(dict.value)"
                >{{dict.label}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分组模式" prop="groupMode">
              <el-radio-group v-model="form.groupMode">
                <el-radio
                  v-for="dict in dict.type.jw_group_mode"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="投屏背景图" prop="screenImg">
              <image-upload v-model="form.screenImg"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="报名记录" :visible.sync="viewSignOpen" width="85vw" append-to-body v-loading="cardLoadIng">

      <el-tabs v-model="currentTab" type="border-card" >
        <el-tab-pane :label="key" :name="key" v-for="(value, key) in signRecordList" :key="key">
          <el-table :data="value" height="65vh">
            <el-table-column label="ID" align="left" prop="id" width="50"/>
            <el-table-column label="赛程小项" align="left" prop="jwScheduleItem.itemName"/>
            <el-table-column label="代表队" align="left" prop="jwTeam.teamName"/>
            <el-table-column label="选手" align="center" prop="backNumber">
              <template slot-scope="scope">
            <span v-for="itemm in scope.row.jwSignRecordSportList"
                  class="sport-name"
                  :class="itemm.sex === 'f'? 'female' : (itemm.sex === 'm'? 'male' : '')"
            >
           <span>{{itemm.playerName}}<span v-if="scope.row.sportLimit == 3" style="margin-right: 8px"></span></span>
          </span>
              </template>
            </el-table-column>
            <el-table-column label="背号" align="center" width="80" prop="backNumber"/>
            <el-table-column label="出场顺序" align="center" width="80" prop="indexOrder"/>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
  import 'core-js/actual/array/group';
  import {listJwGameItem, getJwGameItem, delJwGameItem, addJwGameItem, updateJwGameItem, reGroupJwGameItem} from "@/api/jiewu/JwGameItem";
  import {listJwSignRecordByGameItem} from "@/api/jiewu/JwSignRecord";

  export default {
    name: "JwGameItem",
    dicts: ['sys_yes_no', 'jw_match_type', 'jw_sex', 'jw_sport_limit', 'jw_group_mode'],
    data() {
      return {
        currentTab: "",
        cardLoadIng: false,
        viewSignOpen: false,
        signRecordList: {},

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
        // 比赛项目表格数据
        JwGameItemList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          matchId: (this.Cookies.get("matchId") * 1) || null,
          code: null,
          name: null,
          groupLimit: null,
          singleDuration: null,
          matchType: null,
          promotionNum: null,
          minYear: null,
          maxYear: null,
          fee: null,
          resultDesId: null,
          sexCon: null,

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
      // 查看比赛项目的报名数据
      viewSignRecord(gameItem) {
        let that = this;
        that.signRecordList = [];
        that.cardLoadIng = true;
        that.viewSignOpen = true
        listJwSignRecordByGameItem({gameItemId: gameItem.id}).then(res => {
          let signRecordList = res.data || [];
          signRecordList.forEach(item=>{
            if(!item.jwScheduleItem || !item.jwScheduleItem.itemName){
              item.jwScheduleItem = {itemName: "未分组"}
            }
          })
          signRecordList.sort((a, b) => {
            if (a.jwScheduleItem.itemName == b.jwScheduleItem.itemName) {
              return a.indexOrder - b.indexOrder;
            } else {
              return a.jwScheduleItem.itemName > b.jwScheduleItem.itemName ? 1 : -1;
            }
          });

          that.currentTab = signRecordList[0].jwScheduleItem.itemName;
          that.signRecordList = signRecordList.group((b) => b.jwScheduleItem.itemName);
          that.cardLoadIng = false;


        })
      },
      /** 查询比赛项目列表 */
      getList() {
        this.loading = true;
        listJwGameItem(this.queryParams).then(response => {
          this.JwGameItemList = response.rows;
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
          code: null,
          name: null,
          groupLimit: null,
          singleDuration: null,
          matchType: null,
          promotionNum: null,
          minYear: null,
          maxYear: null,
          fee: null,
          resultDesId: null,
          updateTime: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          screenImg: null,
          remark: null,
          sexCon: null,
          minSport: 1,
          maxSport: 1,
          feeMaxSport: 1
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
        this.form.matchId = this.queryParams.matchId;
        this.title = "添加比赛项目";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getJwGameItem(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改比赛项目";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateJwGameItem(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addJwGameItem(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      // 重新分组
      handleReGroup(row) {
        const id = row.id;
        if (id) {
          this.$modal.confirm('是否确认 "' + row.name + '" 项目 重新分组？').then(function () {
            return reGroupJwGameItem({id});
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess("分组成功");
          }).catch(() => {
          });
        }
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$modal.confirm('是否确认删除比赛项目编号为"' + ids + '"的数据项？').then(function () {
          return delJwGameItem(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('jiewu/JwGameItem/export', {
          ...this.queryParams
        }, `JwGameItem_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>

<style lang="scss" scoped>

  .text-info {
    font-size: 12px;
    color: #888888;
  }

  .male {
    color: #1890FF;
  }

  .female {
    color: #FF3399;
  }

  .add-sign {
    cursor: pointer;
    word-break: keep-all;
    color: #1890FF;
    font-size: 16px;
    margin-left: 10px;
  }

  .sport-names {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
  }

  .sport-name {
    cursor: pointer;
    word-break: keep-all;
  }
</style>
