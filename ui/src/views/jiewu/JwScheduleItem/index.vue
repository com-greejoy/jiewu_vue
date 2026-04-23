<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="比赛" prop="matchId">
        <ELSelectMatch :matchId.sync="queryParams.matchId"/>
      </el-form-item>
      <el-form-item label="组别" prop="gameItemId">
        <el-select filterable v-model="queryParams.gameItemId" placeholder="组别" clearable>
          <el-option
            v-for="gameItem in JwGameItemList"
            :key="gameItem.id"
            :label="gameItem.code + ' : ' + gameItem.name"
            :value="gameItem.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="阶段" prop="scheduleInfoId">
        <el-select filterable v-model="queryParams.scheduleInfoId" placeholder="阶段" clearable @change="getSchedulePlaceList">
          <el-option
            v-for="scheduleInfo in JwScheduleInfoList"
            :key="scheduleInfo.id"
            :label="scheduleInfo.scheduleName"
            :value="scheduleInfo.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="场次" prop="schedulePlaceId">
        <el-select filterable v-model="queryParams.schedulePlaceId" placeholder="阶段" clearable>
          <el-option
            v-for="schedulePlace in JwSchedulePlaceList"
            :key="schedulePlace.id"
            :label="'第 '+schedulePlace.placeOrder + ' 场'"
            :value="schedulePlace.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="小项" prop="itemName">
        <el-input
          v-model="queryParams.itemName"
          placeholder="请输入小项"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="场地" prop="area">
        <el-select v-model="queryParams.area" placeholder="请选择场地" clearable>
          <el-option
            v-for="dict in dict.type.jw_area"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="打分锁定" prop="lockScore">
        <el-select v-model="queryParams.lockScore" placeholder="请选择打分锁定" clearable>
          <el-option
            v-for="dict in dict.type.sys_yes_no"
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
          v-hasPermi="['jiewu:JwScheduleItem:add']"
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
          v-hasPermi="['jiewu:JwScheduleItem:edit']"
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
          v-hasPermi="['jiewu:JwScheduleItem:remove']"
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
          v-hasPermi="['jiewu:JwScheduleItem:export']"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-warning"
          size="mini"
          @click="handleInit"
          v-hasPermi="['jiewu:JwScheduleItem:add']"
        >初始化小项
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-warning"
          size="mini"
          @click="handleInitBackNum"
          v-hasPermi="['jiewu:JwScheduleItem:add']"
        >初始化背号
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="JwScheduleItemList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <!--<el-table-column label="ID" align="center" prop="id"/>-->
      <!--<el-table-column label="比赛" align="center" prop="matchId"/>-->
      <el-table-column label="阶段" align="center" prop="scheduleInfoId">
        <template slot-scope="scope">
          <span>{{ getScheduleInfoName(scope.row) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="场次" align="center" prop="schedulePlaceId">
        <template slot-scope="scope">
          <span>{{ getSchedulePlaceName(scope.row) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="组别" width="260" align="left" prop="gameItemId">
        <template slot-scope="scope">
          <span>{{ getGameItemName(scope.row) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="场地" align="center" prop="area">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.jw_area" :value="scope.row.area"/>
        </template>
      </el-table-column>
      <el-table-column label="小项" width="300" align="left" prop="itemName"/>
      <el-table-column label="进程" align="center" prop="itemProcess">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.jw_item_process" :value="scope.row.itemProcess"/>
        </template>
      </el-table-column>
      <el-table-column label="选手" align="center" prop="sportCount">
        <template slot-scope="scope">
          <el-link :underline="false" type="primary" @click="viewSignRecord(scope.row)">{{scope.row.sportCount}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="打分锁定" align="center" prop="lockScore">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.lockScore"
            active-value="Y"
            inactive-value="N"
            @change="handleLockChange(scope.row)"
          ></el-switch>

        </template>
      </el-table-column>
      <el-table-column label="开始时间" align="center" prop="shceduleTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.shceduleTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['jiewu:JwScheduleItem:remove']"
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

    <!-- 添加或修改赛程小项对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="比赛" prop="matchId">
          <ELSelectMatch :matchId.sync="form.matchId"/>
        </el-form-item>
        <el-form-item label="项目" prop="gameItemId">
          <el-select filterable v-model="form.gameItemId" placeholder="组别" clearable>
            <el-option
              v-for="gameItem in JwGameItemList"
              :key="gameItem.id"
              :label="gameItem.code + ' : ' + gameItem.name"
              :value="gameItem.id"
            />
          </el-select>
        </el-form-item>

<!--        <el-form-item label="小项" prop="itemName">-->
<!--          <el-input v-model="form.itemName" placeholder="请输入小项"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="开始时间" prop="shceduleTime">-->
<!--          <el-date-picker clearable-->
<!--                          v-model="form.shceduleTime"-->
<!--                          type="date"-->
<!--                          value-format="yyyy-MM-dd"-->
<!--                          placeholder="请选择开始时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->
        <el-form-item label="场地" prop="area">
          <el-radio-group v-model="form.area">
            <el-radio
              v-for="dict in dict.type.jw_area"
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

    <el-dialog title="选手" center :visible.sync="viewSignOpen" width="65vw" append-to-body>
      <div class="btn-row">
        <el-button type="primary" plain size="mini" @click="handleArrangeOrder()">整理排序</el-button>
        <el-button type="success" plain size="mini" @click="handleOrderSignRecord()">重新排序</el-button>
      </div>
      <el-table :data="signRecordList" height="75vh">
        <el-table-column label="ID" align="left" prop="id" width="75"/>
        <el-table-column label="代表队" align="left" prop="jwTeam.teamName"/>
        <el-table-column label="选手" align="center" prop="backNumber">
          <template slot-scope="scope">
            <span v-for="itemm in scope.row.jwSignRecordSportList"
                  class="sport-name"
                  :class="itemm.sex === 'f'? 'female' : (itemm.sex === 'm'? 'male' : '')"
            >
           <span>{{itemm.playerName}}<span v-if="scope.row.sportLimit != 1" style="margin-right: 8px"></span></span>
          </span>
          </template>
        </el-table-column>
        <el-table-column label="背号" align="center" width="80" prop="backNumber"/>
        <el-table-column label="出场顺序" align="center" width="80" prop="indexOrder">
          <template slot-scope="scope">
            <el-popover
              @show="reOrder = scope.row.indexOrder"
              placement="top"
              :width="200"
              v-model="scope.row.visible">
              <div style="text-align: center; margin: 0">
                <el-input-number size="small" v-model="reOrder" controls-position="right" :min="1"/>
                <div class="area-btns" style="margin-top: 24px">
                  <el-link :underline="false" type="danger" @click="handleChangeOrder(scope.row)">确认</el-link>
                </div>
              </div>
              <div slot="reference" class="area-btn" >
                <el-link :underline="false" type="danger"><div style="width: 60px; cursor: pointer">{{scope.row.indexOrder}}</div></el-link>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center"  class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <!--<el-button-->
            <!--size="mini"-->
            <!--type="warning"-->
            <!--style="padding: 6px;"-->
            <!--@click="handleUpdate(scope.row)"-->
            <!--v-hasPermi="['jiewu:JwScheduleItem:edit']"-->
            <!--&gt;改项目-->
            <!--</el-button>-->
            <el-button
              size="mini"
              type="success"
              style="padding: 6px;"
              @click="handleChangeScheduleItem(scope.row)"
              v-hasPermi="['jiewu:JwScheduleItem:edit']"
            >改小项
            </el-button>
            <!--<el-button-->
              <!--size="mini"-->
              <!--type="text"-->
              <!--style="padding: 6px;"-->
              <!--@click="handleDelete(scope.row)"-->
              <!--v-hasPermi="['jiewu:JwScheduleItem:remove']"-->
            <!--&gt;删除-->
            <!--</el-button>-->
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 改组 -->
    <el-dialog title="改小项" center :visible.sync="changeScheduleItem" width="500px" :close-on-click-modal="false" append-to-body>
      <el-form ref="form" :model="changeScheduleItemForm" label-width="80px">
        <el-form-item label="背号" prop="backNumber">
          <el-input v-model="changeScheduleItemForm.backNumber" placeholder=""/>
        </el-form-item>
        <el-form-item label="小项" prop="gameItemId">
          <el-select style="width: 380px" v-model="changeScheduleItemForm.scheduleItemId" placeholder="小项" disabled>
            <el-option
              v-for="gameItem in JwScheduleItemListChange"
              :key="gameItem.id"
              :label="gameItem.itemName"
              :value="gameItem.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="新小项" prop="gameItemId">
          <el-select filterable style="width: 380px" v-model="changeScheduleItemId" placeholder="新小项" clearable>
            <el-option
              v-for="gameItem in JwScheduleItemListChange"
              :key="gameItem.id"
              :label="gameItem.itemName"
              :value="gameItem.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveChangeScheduleItem">确 定</el-button>
        <el-button @click="changeScheduleItem = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {listJwScheduleItem, getJwScheduleItem, delJwScheduleItem, addJwScheduleItem, updateJwScheduleItem, initScheduleItem, initBackNum, arrangeOrder, orderSignRecord} from "@/api/jiewu/JwScheduleItem";
  import {listJwGameItem} from "@/api/jiewu/JwGameItem";
  import {listJwSignRecordByGameItem, changeJwScheduleItem, updateJwSignRecord} from "@/api/jiewu/JwSignRecord";
  import {listJwScheduleInfo} from "@/api/jiewu/JwScheduleInfo";
  import {listJwSchedulePlace} from "@/api/jiewu/JwSchedulePlace";

  export default {
    name: "JwScheduleItem",
    dicts: ['jw_area', 'jw_item_process', "sys_yes_no"],
    data() {
      return {
        scheduleItem: {},
        reOrder: 0,
        JwScheduleItemListChange: [],
        changeScheduleItemId: null,
        changeScheduleItemForm: {},
        changeScheduleItem: false,
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
        // 赛程小项表格数据
        JwScheduleItemList: [],
        JwGameItemList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        JwScheduleInfoList: [],
        JwSchedulePlaceList: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          matchId: (this.Cookies.get("matchId") * 1) || null,
          gameItemId: null,
          scheduleInfoId: null,
          schedulePlaceId: null,
          itemName: null,
          shceduleTime: null,
          area: null,
          lockScore: null,
          // orderByColumn: "jsi.placeOrder",
          // isAsc: "ascending"
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          matchId: [
            {required: true, message: "比赛不能为空", trigger: "blur"}
          ],
        },
        signRecordList: [],
        viewSignOpen: false,
      };
    },
    watch: {
      "queryParams.matchId": function (val) {
        this.getGameItemList()
      },
    },
    created() {
      this.getList();
      this.getGameItemList();
      this.getScheduleInfoList();
      this.getSchedulePlaceList();
    },
    methods: {
      handleLockChange(v){
       updateJwScheduleItem({id: v.id, lockScore: v.lockScore}).then(res=>{
         this.getList();
       });
      },
      handleChangeOrder(row){
        if(this.reOrder){
          updateJwSignRecord({id: row.id, indexOrder: this.reOrder}).then(res=>{
            this.viewSignRecord(this.scheduleItem)
          })
        }
      },
      saveChangeScheduleItem() {
        changeJwScheduleItem({id: this.changeScheduleItemForm.id, changeScheduleItemId: this.changeScheduleItemId}).then(res=>{
          this.changeScheduleItem = false;
          this.viewSignOpen = false;
          this.getList();
          this.viewSignRecord({gameItemId: this.changeScheduleItemForm.gameItemId, id: this.changeScheduleItemForm.scheduleItemId})
        })
      },
      // 改小项
      handleChangeScheduleItem(item) {
        listJwScheduleItem({ pageNum: 1, pageSize: 100, matchId: (this.Cookies.get("matchId") * 1) || null, gameItemId: item.gameItemId}).then(res=>{
          this.changeScheduleItemId = null;
          this.changeScheduleItemForm = item;
          this.JwScheduleItemListChange = res.rows || [];
          this.changeScheduleItem = true
        })

      },
      getScheduleInfoName(row) {
        let item = this.JwScheduleInfoList.find((item) => item.id == row.scheduleInfoId);
        return item ? item.scheduleName : "";
      },
      getSchedulePlaceName(row) {
        let item = this.JwSchedulePlaceList.find((item) => item.id == row.schedulePlaceId);
        return item ? "第 " + item.placeOrder + " 场" : "";
      },
      getScheduleInfoList() {
        listJwScheduleInfo({matchId: this.queryParams.matchId, pageNum: 1, pageSize: 10000}).then(response => {
          this.JwScheduleInfoList = response.rows;
        });
      },
      getSchedulePlaceList() {
        listJwSchedulePlace({matchId: this.queryParams.matchId, scheduleInfoId: this.queryParams.scheduleInfoId, pageNum: 1, pageSize: 10000}).then(response => {
          this.JwSchedulePlaceList = response.rows;
        });
      },
      // 查看比赛项目的报名数据
      viewSignRecord(scheduleItem) {
        let that = this;
        that.signRecordList = [];
        that.scheduleItem = scheduleItem;
        listJwSignRecordByGameItem({gameItemId: scheduleItem.gameItemId, scheduleItemId: scheduleItem.id}).then(res => {
          let signRecordList = res.data || [];
          signRecordList.sort((a, b) => {
            if (a.jwScheduleItem.itemName == b.jwScheduleItem.itemName) {
              return a.indexOrder - b.indexOrder;
            } else {
              return a.jwScheduleItem.itemName > b.jwScheduleItem.itemName ? 1 : -1;
            }
          });
          that.signRecordList = signRecordList;
          that.viewSignOpen = true
        })
      },
      getGameItemName(row) {
        let item = this.JwGameItemList.find((item) => item.id == row.gameItemId);
        return item ? item.code + ":" + item.name : "";
      },
      getGameItemList() {
        listJwGameItem({matchId: this.queryParams.matchId, pageNum: 1, pageSize: 5000}).then(response => {
          this.JwGameItemList = response.rows;
        });
      },
      /** 查询赛程小项列表 */
      getList() {
        this.loading = true;
        listJwScheduleItem(this.queryParams).then(response => {
          this.JwScheduleItemList = response.rows;
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
          scheduleInfoId: null,
          schedulePlaceId: null,
          itemName: null,
          shceduleTime: null,
          area: null
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
        this.title = "添加赛程小项";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getJwScheduleItem(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改赛程小项";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateJwScheduleItem(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addJwScheduleItem(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      handleInitBackNum() {
        let that = this;
        if (this.queryParams.matchId) {
          this.$confirm('确认初始化所有背号？初始化会清除当前比赛的所有背号，然后根据代表队重新生成哦').then(_ => {
            initBackNum({matchId: this.queryParams.matchId}).then(res => {
              that.getList()
            })
          }).catch(_ => {
          });
        } else {
          this.$modal.msgError("请选择比赛");
        }

      },
      handleInit() {
        let that = this;
        if (this.queryParams.matchId) {
          this.$confirm('确认初始化赛程小项？初始化会清除当前比赛的所有小项，然后根据比赛项目重新生成小项哦').then(_ => {
            initScheduleItem({matchId: this.queryParams.matchId}).then(res => {
              that.getList()
            })
          }).catch(_ => {
          });
        } else {
          this.$modal.msgError("请选择比赛");
        }
      },
      // 重新排序
      handleOrderSignRecord(){
        orderSignRecord({scheduleItemId: this.scheduleItem.id}).then(res=>{
          this.$modal.msgSuccess("排序完成");
          this.viewSignRecord(this.scheduleItem)
        })
      },
      // 整理排序
      handleArrangeOrder(){
        arrangeOrder({gameItemId: this.scheduleItem.gameItemId}).then(res=>{
          this.$modal.msgSuccess("整理完成");
          this.viewSignRecord(this.scheduleItem)
        })
      },
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$modal.confirm('是否确认删除赛程小项编号为"' + ids + '"的数据项？').then(function () {
          return delJwScheduleItem(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('jiewu/JwScheduleItem/export', {
          ...this.queryParams
        }, `JwScheduleItem_${new Date().getTime()}.xlsx`)
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
    /*word-break: keep-all;*/
  }
  .btn-row {
    display: flex;
    flex-direction: row;
    margin-bottom: 12px;
    justify-content: end;
  }
</style>
