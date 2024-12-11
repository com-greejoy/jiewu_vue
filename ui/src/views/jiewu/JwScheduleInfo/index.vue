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
        <el-button type="el-icon-refresh" icon="el-icon-search" size="mini" @click="handleQuery">刷新</el-button>
        <el-button icon="el-icon-c-scale-to-original" type="success"  size="mini" @click="handelALLSchedcule('schedule')">竞赛日程表</el-button>
        <el-button icon="el-icon-date" type="warning"  size="mini" @click="handelALLSchedcule('scheduleDetail')">详细赛程表</el-button>
        <el-button icon="el-icon-data-analysis" type="danger"  size="mini" @click="handelALLSchedcule('jianluPai')">检录举牌</el-button>
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

    <el-dialog title="赛程表明细" :visible.sync="downLoadScheduleInfo" width="950px" center :append-to-body="false">
      <div class="btn-row">
        <el-button type="primary" plain size="mini" @click="handlePrint('printScheduleDetail')">打印</el-button>

        <el-button type="success" plain size="mini" @click="saveAsImage()">图片</el-button>
      </div>
      <div class="fee-items" id="printScheduleDetail" ref="capture" v-if="downLoadScheduleInfo" v-loading="scheduleLoading">
        <div class="match-name" v-html="JwScheduleInfoList[0].matchName"></div>
        <div class="title-name">赛程表明细</div>
        <div class="fee-item">
          <div class="index-v h time">比赛时间</div>
          <div class="index-v h game-item s">比赛组别</div>
          <div class="index-v h area">场地</div>
          <div class="sport-rows">
            <div class="sport-row">
              <div class="index-v h index">上场序号</div>
              <div class="index-v h back">背号</div>
              <div class="index-v h sport s">选手</div>
              <div class="index-v h team-name">代表队</div>
            </div>
          </div>
        </div>
        <div class="fee-item" v-for="(item, key) in scheduleInfoList">
          <div class="index-v time">
            <!--<br> {{item[0].scheduleName}}-->
            {{item[0].placeTime.split(" ")[1]}} 第{{item[0].placeOrder}}场
          </div>
          <div class="index-v game-item s">{{key}}</div>
          <div class="index-v area">{{getAreaLabel(item[0])}}</div>

          <div class="sport-rows">
            <div class="sport-row" v-for="itemm in item">
              <div class="index-v index">{{itemm.indexOrder}}</div>
              <div class="index-v back">{{itemm.backNumber }}</div>
              <div class="index-v sport s">
                <span v-for="sport in itemm.jwSignRecordSportList">
                  <span v-if="itemm.sportLimit != 1" class="m-sport">{{sport.playerName}}</span>
                  <span v-else>{{sport.playerName}}</span>
                </span>
              </div>
              <div class="index-v team-name">{{itemm.jwTeam.teamName}}</div>
            </div>
          </div>
          <!--<div style='page-break-after:always;'></div>-->
        </div>
      </div>
    </el-dialog>

    <el-dialog title="竞赛日程表" :visible.sync="downLoadSchedule" width="950px" center :append-to-body="false">
      <div class="btn-row">
        <el-button type="primary" plain size="mini" @click="handlePrint('printSchedule')">打印</el-button>
      </div>
      <div class="fee-items schedule"  id="printSchedule" v-if="downLoadSchedule" v-loading="scheduleLoading">
        <div class="match-name" v-html="JwScheduleInfoList[0].matchName"></div>
        <div class="title-name" >竞赛日程表</div>
        <div class="fee-item">
          <div class="index-v h time">比赛时间</div>
          <div class="index-v h game-item s">比赛组别</div>
          <div class="index-v h area">场地</div>
        </div>
        <div v-for="schedulePlace in JwScheduleInfoList">
          <div class="fee-item" v-for="item in schedulePlace.schedulePlaceList">
            <div class="index-v time">
              {{parseTime(item.placeTime, '{h}:{i}')}} 第{{item.placeOrder}}场
            </div>
            <div class="index-v game-item sd">
              <div class="gama-item-s" v-for="itemm in item.jwScheduleItemList">
                <div class="index-v name">{{itemm.itemName}} ({{itemm.sportCount}} 人)</div>
                <div class="index-v area">{{getAreaLabel(itemm)}}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>

    <el-dialog title="检录举牌" :visible.sync="showJuPai" width="1400px" center :append-to-body="false">
      <div class="btn-row">
        <el-button type="primary" plain size="mini" @click="handlePrintC('printJuPai')">打印</el-button>
      </div>
      <div class="jupai-items" id="printJuPai" v-if="showJuPai" v-loading="scheduleLoading">
        <div class="jupai-item" v-for="(jupai, key) in juPaiList">
          <div class="item-game" v-for="(item, key2) in jupai">
            <div class="item-time">第{{key}}场</div>
            <div class="item-name">
              <div class="name-t">{{item[0].itemName}}</div>
              <div class="item-area">{{getAreaLabel2(key2)}}</div>
            </div>
            <div class="item-sports">
              <div class="item-sport" v-for="itemm in item">
                <div class="sport-index">{{itemm.indexOrder}}</div>
                <div class="sport-num">{{itemm.backNumber}}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {listJwScheduleInfo, getJwScheduleInfo, delJwScheduleInfo, addJwScheduleInfo, updateJwScheduleInfo} from "@/api/jiewu/JwScheduleInfo";
  import {listJwSchedulePlace, listJwSchedulePlaceWithScheduleItem, getJwSchedulePlace, delJwSchedulePlace, addJwSchedulePlace, updateJwSchedulePlace} from "@/api/jiewu/JwSchedulePlace";
  import {listNoPlaceJwScheduleItem, updateJwScheduleItem, updateJwScheduleItemPlace, clearSchedulePlaceById, calculateTime} from "@/api/jiewu/JwScheduleItem";
  import { getTeamScheduleInfoList} from "@/api/jiewu/JwMatchTeam";
  import 'core-js/actual/array/group';
  import printJS from 'print-js';
  import html2canvas from 'html2canvas';

  export default {
    name: "JwScheduleInfo",
    dicts: ['jw_area'],
    data() {
      return {
        showJuPai: false,
        downLoadSchedule: false,
        showType: "",
        scheduleLoading: false,
        scheduleInfoList: [],
        downLoadScheduleInfo: false,
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
        juPaiList: {},
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
      async saveAsImage() {
        try {
          const canvas = await html2canvas(this.$refs.capture, {scale: 4});
          const img = canvas.toDataURL('image/png');
          const link = document.createElement('a');
          link.href = img;
          link.download = 'capture.png';
          link.click();
        } catch (error) {
          console.error('Error capturing the image:', error);
        }
      },
      handlePrintC(id) {
        printJS({
          printable: id,
          maxWidth: "1200",
          type: 'html',
          //为了样式生效需要添加 targetStyles:['*'] 和 font_size:''
          targetStyles: ['*'],
          font_size: '',
          //margin0 默认打印页边距为0
          style: `
          @page {
            size:auto;
            margin: 16;
          }
        `
        });
      },
      //导出赛程表
      handelALLSchedcule(type){
        let that = this;
        if("scheduleDetail" == type){
          // 详细赛程表
          this.scheduleLoading = true;
          getTeamScheduleInfoList({matchId: this.queryParams.matchId, teamId: null}).then(res => {
            this.showType = type;
            let scheduleInfoList = res.data || [];
            this.scheduleInfoList = scheduleInfoList.group((b) => b.itemName);
            this.scheduleLoading = false;
            this.downLoadScheduleInfo = true;
          })
        }else if("schedule" == type){
          // 竞赛日程表
          this.downLoadSchedule = true;

        }else if("jianluPai" == type){
          getTeamScheduleInfoList({matchId: this.queryParams.matchId, teamId: null}).then(res => {
            this.showType = type;
            let scheduleInfoList = res.data || [];
            this.juPaiList = scheduleInfoList.group((b) => b.placeOrder);
            for(let key in this.juPaiList){
              this.juPaiList[key] = this.juPaiList[key].group((b) => b.area);
            }
            console.log(this.juPaiList)
            this.scheduleLoading = false;
            this.showJuPai = true;
          })
        }

      },
      getAreaLabel(item) {
        return this.selectDictLabel(this.dict.type.jw_area, item.area);
      },
      getAreaLabel2(item) {
        return this.selectDictLabel(this.dict.type.jw_area, item);
      },
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
        this.updatePlaceForm = {}
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


  .match-name {
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    margin-bottom: 8px;
  }
  .jupai-item{
    display: flex;
    color: #000000;
    flex-direction: column;
    page-break-before: always;
    page-break-after:always;
    .item-time{
      text-align: center;
      font-size: 36px;
      font-weight: 600;
    }
    .item-game{
      page-break-before: always;
      page-break-after:always;
      display: flex;
      flex-direction: column;
      height: 85vh;

      .item-name{
        font-size: 36px;
        display: flex;
        flex-direction: row;
        margin-bottom: 24px;
        margin-top: 12px;
        align-items: center;
        .name-t{

        }
        .item-area{
          margin-top: 8px;
          margin-left: 32px;
        }
      }
      .item-sports{
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;

        .item-sport{
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          font-size: 48px;
          margin-right: 8px;
          margin-bottom: 8px;
          padding: 8px 16px;
          border: 1px solid #000000;
          border-radius: 4px;
          width: 120px;
          .sport-index{
            text-align: center;
            border-bottom: 3px solid #888;
            border-radius: 50%;
            width: 56px;
            height: 56px;
          }
        }
      }
    }

  }
  .title-name {
    text-align: center;
    margin-bottom: 32px;
    font-weight: 600;
    font-size: 18px;
  }
  .btn-row {
    display: flex;
    flex-direction: row;
    margin-bottom: 12px;
    justify-content: end;
  }
  .fee-items {
    display: flex;
    flex-direction: column;
    margin-right: 12px;
    color: #000;
    width: 520pt;

    width: 620pt;
    padding: 24pt;
    page-break-inside: avoid;
    &.schedule{
      .fee-item{
        .index-v{
          padding: 6px 0;
          &.time {
            width: 120px;

            padding-left: 8px;
          }
          &.game-item.s{
            flex: 1;
          }
        }
      }
    }
    .gama-item-s{
      width: 100%;
      display: flex;
      flex-direction: row;
      border-bottom: 1px solid #000;

      &:last-child {
        border-bottom: none;
      }
      .name{
        flex: 1;
      }
    }

    .fee-item {
      display: flex;
      flex-direction: row;
      border: 1px solid #000;
      /*border-bottom: none;*/
      page-break-inside: avoid;

      &:last-child {
        border-bottom: 1px solid #000;
      }

      .index-v {
        /*text-align: center;*/
        border-right: 1px solid #000;
        padding: 2px 0;
        font-size: 14px;
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        justify-content: center;

        &.h {
          font-weight: 600;
          font-size: 16px;
        }

        &:last-child {
          border-right: none;
        }

        &.index {
          width: 36px;
        }

        &.back {
          width: 48px;
        }

        &.sport {
          width: 0;
          flex: 1;
          padding-left: 4px;
          padding-right: 4px;
          line-height: 12px;
          white-space: nowrap;

          &.s {
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
          }

          .m-sport {
            margin-right: 8px;
            white-space: nowrap;
            font-size: 12px;
          }

          &.all-fee {
            height: 40px;
          }
        }

        &.game-item {
          width: 20px;
          flex: 1;
          padding-left: 4px;
          padding-right: 4px;

          &.s {
            width: 160px;
            flex: none;
          }
          &.sd{
            display: flex;
            flex-direction: column;
            padding: 0;
          }
        }

        &.fee {
          width: 100px;
          line-height: 12px;
        }

        &.remark {
          width: 80px;
        }

        &.time {
          width: 100px;
          justify-content: flex-start;
          padding-left: 4px;
        }

        &.area {
          width: 40px;
        }
        &.team-name{
          width: 176px;
        }
      }

      .sport-rows {
        flex: 1;
        display: flex;
        flex-direction: column;
        page-break-inside: avoid;

        .sport-row {
          flex: 1;
          display: flex;
          flex-direction: row;
          border-bottom: 1px solid #000;
          page-break-inside: avoid;

          &:last-child {
            border-bottom: none;
          }
        }
      }

    }
  }


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
      width: 100%;
      min-width: 450px;
      margin-right: 24px;

      ::v-deep.el-card__body {
        overflow: auto;
        max-height: calc(100% - 64px);
        display: flex;
        flex-wrap: wrap;
      }
    }

    ::v-deep.el-card__body {
      padding: 12px;
    }

    .schedule-place-card {
      margin-bottom: 8px;
      width: 30%;
      margin-right: 8px;
      ::v-deep.el-card__body {
        overflow: visible;
      }
    }

    .schedule-item-card {
      margin-bottom: 4px;
      width: 100%;
      &:last-child{
        margin-bottom: 0;
      }

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

