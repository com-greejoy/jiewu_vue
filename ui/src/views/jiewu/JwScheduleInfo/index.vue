<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item>
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['jiewu:JwScheduleInfo:add']"
        >新增
        </el-button>
      </el-form-item>
      <el-form-item label="比赛" prop="matchId">
        <ELSelectMatch :matchId.sync="queryParams.matchId"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
      <el-button style="float: right;" type="warning" size="mini" @click="handleCalculateTime">计算时间</el-button>

    </el-form>

    <div class="info-items" :key="componentKey">
      <el-card class="box-card" v-for="jwScheduleInfo in JwScheduleInfoList" :key="jwScheduleInfo.id">
        <div slot="header" class="clearfix">
          <span>{{jwScheduleInfo.scheduleName}} {{parseTime(jwScheduleInfo.beginTime, '{h}:{i}')}}</span>
          <el-button style="float: right; padding: 3px 8px" type="primary" @click="handleAddSchedulePlace(jwScheduleInfo)" size="mini">加场</el-button>
          <el-button style="float: right; padding: 3px 8px; margin-right: 8px" size="mini" @click="handleUpdate(jwScheduleInfo)" type="success">修改</el-button>
          <el-button style="float: right; padding: 3px 8px; " type="danger" @click="handleDelScheduleInfo(jwScheduleInfo)" size="mini">删除</el-button>
        </div>
        <el-card class="schedule-place-card" v-for="schedulePlace in jwScheduleInfo.schedulePlaceList" :key="schedulePlace.id">
          <div slot="header" class="clearfix">
            <span>第 {{schedulePlace.placeOrder}} 场：{{parseTime(schedulePlace.placeTime, '{h}:{i}')}}</span>
            <el-button style="float: right; padding: 3px 8px" type="primary" @click="handleAddScheduleItem(schedulePlace, jwScheduleInfo)" size="mini">加组</el-button>
            <el-button style="float: right; padding: 3px 8px; margin-right: 8px" size="mini" @click="handleUpdateSchedulePlace(schedulePlace)" type="success">修改</el-button>
            <el-button style="float: right; padding: 3px 8px; " type="danger" @click="handleDelSchedulePlace(schedulePlace)" size="mini">删除</el-button>
          </div>
          <el-card class="schedule-item-card" v-for="scheduleItem in schedulePlace.jwScheduleItemList" :key="scheduleItem.id">

            <div class="schedule-item">{{scheduleItem.itemName}}({{scheduleItem.sportCount}})</div>
            <el-popover
              @show="changeAreaShow(scheduleItem.area)"
              placement="top"
              :width="630"
              v-model="scheduleItem.visible">
              <div style="text-align: center; margin: 0">
                <el-radio-group v-model="changeArea">
                  <el-radio
                    v-for="dict in dict.type.jw_area"
                    :key="dict.value"
                    :label="dict.value"
                  >{{dict.label}}场地
                  </el-radio>
                </el-radio-group>
                <div class="area-btns" style="margin-top: 24px">
                  <el-link :underline="false" type="danger" @click="handleChangeArea(scheduleItem, jwScheduleInfo)">确认</el-link>
                </div>
              </div>
              <div slot="reference" class="area-btn" >
                <dict-tag size="mini" :options="dict.type.jw_area" :value="scheduleItem.area"/>
              </div>
            </el-popover>
            <div style="flex: 1;"></div>
            <el-button type="danger" size="mini" @click="handleDelScheduleItem(scheduleItem, jwScheduleInfo)" icon="el-icon-delete" circle></el-button>
          </el-card>
        </el-card>
      </el-card>
    </div>

    <!-- 添加或修改阶段对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="比赛" prop="matchId">
          <ELSelectMatch style="width: 360px" :matchId.sync="form.matchId"/>
        </el-form-item>
        <el-form-item label="阶段" prop="scheduleName">
          <el-input style="width: 360px" v-model="form.scheduleName" placeholder="请输入阶段"/>
        </el-form-item>
        <el-form-item label="开始时间" prop="beginTime">
          <el-date-picker clearable
                          v-model="form.beginTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
                          v-model="form.endTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="排序" prop="">
          <el-input-number style="width: 220px" v-model="form.indexOrder" controls-position="right" :min="1"/>

        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="增加小项" :visible.sync="addItemOpen" width="85vw" append-to-body>
      <el-form ref="form" :model="addItemForm" label-width="80px">

        <el-form-item label="阶段" prop="scheduleInfoId">
          <el-input v-model="addItemForm.jwScheduleInfo.scheduleName" placeholder="请输入阶段" readonly/>
        </el-form-item>
        <el-form-item label="场次" prop="placeOrder">
          <el-input v-model="addItemForm.schedulePlaceName" placeholder="请输入场次" readonly/>
        </el-form-item>
        <el-form-item label="小项">
          <el-checkbox-group v-model="addItemForm.scheduleItemIds" style="height: 50vh;overflow: auto;display: flex;flex-direction: column;flex-wrap: wrap;">
            <el-checkbox v-for="item in addItemForm.scheduleItemList" :label="item.id" class="checkbox-game-item" :key="item.itemName">
              <div class="item-name">{{item.itemName}}({{item.sportCount}})</div>
              <dict-tag size="mini" :options="dict.type.jw_area" :value="item.area"/>
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAddItemForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改场地对话框 -->
    <el-dialog title="修改场地" :visible.sync="updatePlaceOpen" width="500px" append-to-body>
      <el-form ref="form" :model="updatePlaceForm" label-width="80px">
        <el-form-item label="场次" prop="placeOrder">
          <el-input-number size="small" v-model="updatePlaceForm.placeOrder" controls-position="right" :min="1"/>

        </el-form-item>
        <el-form-item label="开始时间" prop="placeTime">
          <el-date-picker clearable
                          v-model="updatePlaceForm.placeTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitPlaceForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {listJwScheduleInfo, getJwScheduleInfo, delJwScheduleInfo, addJwScheduleInfo, updateJwScheduleInfo} from "@/api/jiewu/JwScheduleInfo";
  import {listJwSchedulePlace, listJwSchedulePlaceWithScheduleItem, getJwSchedulePlace, delJwSchedulePlace, addJwSchedulePlace, updateJwSchedulePlace} from "@/api/jiewu/JwSchedulePlace";
  import {listNoPlaceJwScheduleItem, updateJwScheduleItem, updateJwScheduleItemPlace, clearSchedulePlaceById, calculateTime} from "@/api/jiewu/JwScheduleItem";

  export default {
    name: "JwScheduleInfo",
    dicts: ['jw_area'],
    data() {
      return {
        changeArea: null,
        updatePlaceForm: {},
        updatePlaceOpen: false,
        addPlaceOpen: false,
        addPlaceForm: {},
        componentKey: 0,
        addItemOpen: false,
        addItemForm: {jwScheduleInfo: {}, schedulePlace: {}},
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
        // 阶段表格数据
        JwScheduleInfoList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          matchId: (this.Cookies.get("matchId") * 1) || null,
          scheduleName: null,
          beginTime: null,
          endTime: null,
          indexOrder: null
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          matchId: [
            {required: true, message: "比赛不能为空", trigger: "blur"}
          ],
          scheduleName: [
            {required: true, message: "阶段不能为空", trigger: "blur"}
          ],
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      // 计算时间
      handleCalculateTime() {
        let that = this;
        this.$modal.confirm('是否确认全部计算赛程时间？').then(function () {
          return calculateTime({matchId: that.queryParams.matchId});
        }).then(() => {
          that.getList();
          that.$modal.msgSuccess("计算完成");
        })
      },
      changeAreaShow(area) {
        this.changeArea = area;
      },
      // 从场地中删除小项
      handleDelScheduleItem(scheduleItem, jwScheduleInfo) {
        let that = this;
        this.$modal.confirm('是否确认删除该小项 ？').then(function () {
          return clearSchedulePlaceById({id: scheduleItem.id});
        }).then(() => {
          this.getJwScheduleInfoSchedulePlace(jwScheduleInfo.matchId, jwScheduleInfo.id);
          this.$modal.msgSuccess("删除成功");
        })
      },
      // 修改场地
      handleChangeArea(scheduleItem, jwScheduleInfo) {
        if (this.changeArea) {
          updateJwScheduleItem({id: scheduleItem.id, area: this.changeArea}).then(res => {
            this.getJwScheduleInfoSchedulePlace(jwScheduleInfo.matchId, jwScheduleInfo.id)
          })
        }
      },
      // 保存场次
      submitPlaceForm() {
        updateJwSchedulePlace(this.updatePlaceForm).then(response => {
          this.$modal.msgSuccess("修改成功");
          this.updatePlaceOpen = false;
          this.getList();
        });
      },
      // 修改场次
      handleUpdateSchedulePlace(schedulePlace) {
        this.updatePlaceForm = schedulePlace;
        this.updatePlaceOpen = true;
      },
      // 删除比赛单元
      handleDelScheduleInfo(jwScheduleInfo) {
        let that = this;
        this.$modal.confirm('是否确认删除 ' + jwScheduleInfo.scheduleName + ' ？').then(function () {
          return delJwScheduleInfo(jwScheduleInfo.id);
        }).then(() => {
          that.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      // 删除场次
      handleDelSchedulePlace(schedulePlace) {
        let that = this;
        this.$modal.confirm('是否确认删除 第 ' + schedulePlace.placeOrder + ' 场？').then(function () {
          return delJwSchedulePlace(schedulePlace.id);
        }).then(() => {
          that.getJwScheduleInfoSchedulePlace(schedulePlace.matchId, schedulePlace.scheduleInfoId);
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      // 保存场次选择的小项
      submitAddItemForm() {
        let that = this;
        updateJwScheduleItemPlace({
          jwScheduleItemIds: this.addItemForm.scheduleItemIds.join(","),
          scheduleInfoId: this.addItemForm.jwScheduleInfo.id,
          schedulePlaceId: this.addItemForm.schedulePlace.id
        }).then(res => {
          that.getJwScheduleInfoSchedulePlace(this.addItemForm.jwScheduleInfo.matchId, this.addItemForm.jwScheduleInfo.id);
          that.addItemOpen = false;
        })
      },
      // 给场次增加组别
      handleAddScheduleItem(schedulePlace, jwScheduleInfo) {
        let that = this;
        listNoPlaceJwScheduleItem({matchId: schedulePlace.matchId}).then(res => {
          that.addItemForm = {schedulePlace, jwScheduleInfo, schedulePlaceName: "第 " + schedulePlace.placeOrder + " 场", scheduleItemIds: []};
          that.addItemForm.scheduleItemList = res.data || [];
          that.addItemForm.scheduleItemList.sort((a, b) => a.itemName.split(":")[0] *  1 - b.itemName.split(":")[0] *  1)
          that.addItemOpen = true;
        })
      },
      // 给单元增加场次
      handleAddSchedulePlace(jwScheduleInfo) {
        let that = this;
        addJwSchedulePlace({
          matchId: jwScheduleInfo.matchId,
          scheduleInfoId: jwScheduleInfo.id,
          placeOrder: null,
          placeTime: null
        }).then(res => {
          that.getJwScheduleInfoSchedulePlace(jwScheduleInfo.matchId, jwScheduleInfo.id);
        })
      },
      // 获取每个单元的场次和小项
      getJwScheduleInfoSchedulePlace(matchId, scheduleInfoId) {
        let that = this;
        listJwSchedulePlaceWithScheduleItem({matchId: matchId, scheduleInfoId: scheduleInfoId}).then(res => {
          let index = that.JwScheduleInfoList.findIndex(item => item.id === scheduleInfoId);
          that.JwScheduleInfoList[index].schedulePlaceList = res.data || [];
          that.componentKey = new Date()
        })
      },
      getList() {
        let that = this;
        this.loading = true;
        listJwScheduleInfo(this.queryParams).then(response => {
          this.JwScheduleInfoList = response.rows;
          this.total = response.total;
          this.loading = false;
          this.JwScheduleInfoList.forEach(item => {
            that.getJwScheduleInfoSchedulePlace(item.matchId, item.id);
          })
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.updatePlaceOpen = false;
        this.addItemOpen = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: null,
          matchId: null,
          scheduleName: null,
          beginTime: null,
          endTime: null,
          indexOrder: null
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
        this.form.matchId = this.queryParams.matchId;
        this.open = true;
        this.title = "添加阶段";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getJwScheduleInfo(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改阶段";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateJwScheduleInfo(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addJwScheduleInfo(this.form).then(response => {
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
        this.$modal.confirm('是否确认删除阶段编号为"' + ids + '"的数据项？').then(function () {
          return delJwScheduleInfo(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('jiewu/JwScheduleInfo/export', {
          ...this.queryParams
        }, `JwScheduleInfo_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>

<style lang="scss" scoped>
  .checkbox-game-item {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-bottom: 8px;
    ::v-deep.el-checkbox__label {
      display: flex;
      flex-direction: row;
      align-items: center;
      .item-name{
        margin-right: 12px;
        width: 400px;
        white-space: normal;
      }
    }
  }

  .info-items {
    display: flex;
    flex-direction: row;
    overflow: auto;
    padding-bottom: 16px;
    max-height: calc(100vh - 200px);

    &::-webkit-scrollbar {
      height: 18px;
    }

    .box-card {
      width: 30%;
      min-width: 450px;
      margin-right: 24px;

      ::v-deep.el-card__body {
        overflow: auto;
        max-height: calc(100% - 64px);
      }
    }

    ::v-deep.el-card__body {
      padding: 12px;
    }

    .schedule-place-card {
      margin-bottom: 24px;
    }

    .schedule-item-card {
      margin-bottom: 4px;

      ::v-deep.el-card__body {
        display: flex;
        flex-direction: row;
        align-items: center;
        padding: 8px;

        .schedule-item {
          margin-right: 8px;
        }

        .area-btn {
          cursor: pointer;

        }

      }
    }

  }
</style>

