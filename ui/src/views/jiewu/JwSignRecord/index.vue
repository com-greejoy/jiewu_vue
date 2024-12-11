<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="比赛" prop="matchId">
        <ELSelectMatch :matchId.sync="queryParams.matchId"/>
      </el-form-item>
      <el-form-item label="代表队" prop="teamId">
        <el-select filterable v-model="queryParams.teamId" placeholder="代表队" clearable @change="getList">
          <el-option
            v-for="team in JwTeamList"
            :key="team.id"
            :label="team.indexOrder + ' : ' + team.teamName"
            :value="team.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="组别" prop="gameItemId">
        <el-select filterable v-model="queryParams.gameItemId" placeholder="组别" clearable @change="getList">
          <el-option
            v-for="gameItem in JwGameItemList"
            :key="gameItem.id"
            :label="gameItem.code + ' : ' + gameItem.name"
            :value="gameItem.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="背号" prop="backNumber">
        <el-input
          v-model="queryParams.backNumber"
          placeholder="请输入背号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="success" icon="el-icon-upload2" size="mini" @click="handleImport">导入数据</el-button>
        <el-button type="warning" icon="el-icon-plus" size="mini" @click="handleAddA">添加</el-button>
        <el-button icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="JwSignRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <!--<el-table-column label="ID" align="center" prop="id" />-->
      <!--<el-table-column label="代表队" align="left" prop="teamId" >-->
      <!--<template slot-scope="scope">-->
      <!--<span>{{ getTeamName(scope.row) }}</span>-->
      <!--</template>-->
      <!--</el-table-column>-->
      <el-table-column label="组别" width="300" show-overflow-tooltip align="left" prop="gameItemId">
        <template slot-scope="scope">
          <span>{{ scope.row.code+":"+scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="人数限制" width="80" align="center" prop="sportLimit">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.jw_sport_limit" :value="scope.row.sportLimit"/>
        </template>
      </el-table-column>
      <!--<el-table-column label="背号" width="100" align="center" prop="backNumber" />-->
      <el-table-column label="选手" align="center">
        <template slot-scope="scope">
          <span v-for="item in scope.row.sportList"
                class="quick-edit"
                :class="item.sex === 'f'? 'female' : (item.sex === 'm'? 'male' : '')"
          >
           (
            <span class="back-num" v-if="item.backNumber">{{item.backNumber}}:</span>
            <span v-for="itemm in item.jwSignRecordSportList"
                  class="sport-name"
                  :class="itemm.sex === 'f'? 'female' : (itemm.sex === 'm'? 'male' : '')"
          >
            <el-popover
              placement="top"
              width="320"
              v-model="itemm.visible">
              <div style="text-align: center; margin: 0">
                <el-link :underline="false"  style="margin-right: 12px" type="primary" @click="playMusic(item)">音乐</el-link>
                <el-link :underline="false"  style="margin-right: 12px" type="danger" @click="delSport(itemm)">删除选手</el-link>
                <el-link :underline="false"  style="margin-right: 12px" type="primary" @click="addSport(item, scope.row)">加人</el-link>
                <el-link :underline="false" type="warning" style="margin-right: 12px" @click="delRecord(item)">删除记录</el-link>
                <el-link :underline="false" type="success" @click="handleChange(item)">改组</el-link>
              </div>
              <span slot="reference">{{itemm.playerName}}<span v-if="scope.row.sportLimit != 1" style="margin-right: 8px"></span></span>
            </el-popover>

          </span>)
          </span>
          <span class="add-sign" @click="handleAdd(scope.row)">
            <i class="el-icon-circle-plus"></i>
          </span>
        </template>
      </el-table-column>
      <!--<el-table-column label="比赛ID" align="center" prop="matchId" />-->


      <!--<el-table-column label="作品名称" align="center" prop="worksName" />-->
      <!--<el-table-column label="作品音乐" align="center" prop="worksMusic"  >-->
      <!--<template slot-scope="scope">-->
      <!--<i v-if="scope.row.worksMusic" class="el-icon-video-play music-icon"></i>-->
      <!--</template>-->
      <!--</el-table-column>-->

      <!--<el-table-column label="作品视频" align="center" prop="worksVideo" />-->
      <!--<el-table-column label="操作" width="80" align="center" class-name="small-padding fixed-width">-->
      <!--<template slot-scope="scope">-->
      <!--<el-button-->
      <!--size="mini"-->
      <!--type="text"-->
      <!--icon="el-icon-edit"-->
      <!--@click="handleChange(scope.row)"-->
      <!--v-hasPermi="['jiewu:JwSignRecord:edit']"-->
      <!--&gt;改组</el-button>-->
      <!--<el-button-->
      <!--size="mini"-->
      <!--type="text"-->
      <!--icon="el-icon-delete"-->
      <!--@click="handleDelete(scope.row)"-->
      <!--v-hasPermi="['jiewu:JwSignRecord:remove']"-->
      <!--&gt;删除</el-button>-->
      <!--</template>-->
      <!--</el-table-column>-->
    </el-table>

    <!--<pagination-->
    <!--v-show="total>0"-->
    <!--:total="total"-->
    <!--:page.sync="queryParams.pageNum"-->
    <!--:limit.sync="queryParams.pageSize"-->
    <!--@pagination="getList"-->
    <!--/>-->

    <!-- 改组 -->
    <el-dialog title="改组" :visible.sync="changeGameItem" width="500px" :close-on-click-modal="false" append-to-body>
      <el-form ref="form" :model="changeGameItemForm" :rules="rules" label-width="80px">
        <el-form-item label="背号" prop="backNumber">
          <el-input v-model="changeGameItemForm.backNumber" placeholder=""/>
        </el-form-item>
        <el-form-item label="组别" prop="gameItemId">
          <el-select style="width: 380px" v-model="changeGameItemForm.gameItemId" placeholder="组别" disabled>
            <el-option
              v-for="gameItem in JwGameItemList"
              :key="gameItem.id"
              :label="gameItem.code + ' : ' + gameItem.name"
              :value="gameItem.id"

            />
          </el-select>
        </el-form-item>
        <el-form-item label="新组别" prop="gameItemId">
          <el-select filterable style="width: 380px" v-model="changeGameItemId" placeholder="组别" clearable>
            <el-option
              v-for="gameItem in JwGameItemList"
              :key="gameItem.id"
              :label="gameItem.code + ' : ' + gameItem.name"
              :value="gameItem.id"
              v-if="gameItem.sportLimit == changeGameItemForm.sportLimit && gameItem.id != changeGameItemForm.gameItemId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveChangeGameItem">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="比赛">
          <el-input v-model="form.gameItem.matchName" placeholder="请输入比赛ID" readonly/>
        </el-form-item>
        <el-form-item label="代表队" prop="teamId">
          <el-select style="width: 380px" filterable v-model="form.teamId" placeholder="代表队" @change="handleAddGameItemChange">
            <el-option
              v-for="team in JwTeamList"
              :key="team.id"
              :label="team.indexOrder + ' : ' + team.teamName"
              :value="team.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="组别" prop="teamId">
          <el-select style="width: 380px" filterable v-model="form.gameItemId" placeholder="组别" clearable @change="handleAddGameItemChange">
            <el-option
              v-for="gameItem in JwGameItemList"
              :key="gameItem.id"
              :label="gameItem.code + ' : ' + gameItem.name"
              :value="gameItem.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="选手" prop="gameItemId">
          <el-select multiple style="width: 380px" v-model="form.sportIds" filterable placeholder="选手" :popper-append-to-body="false">
            <el-option
              v-for="item in JwSportList"
              :key="item.id"
              :label="item.playerName"
              :value="item.id">
              <div class="option-sport">
                <span style="float: left; flex: 1" :class="item.sex === 'f'? 'female' : (item.sex === 'm'? 'male' : '')">{{ item.playerName }}</span>
                <span style="float: left; margin-right: 8px">{{ item.age }}岁</span>
                <dict-tag :options="dict.type.jw_sex" :value="item.sex"/>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 加人对话框 -->
    <el-dialog title="增加选手" :visible.sync="addSportOpen" width="500px" :close-on-click-modal="false">
      <el-form ref="form" :model="addSportForm" :rules="rules" label-width="80px">
        <el-form-item label="比赛">
          <el-input v-model="addSportForm.gameItem.matchName" readonly/>
        </el-form-item>
        <el-form-item label="代表队" prop="teamId">
          <el-select style="width: 380px" filterable v-model="addSportForm.teamId" placeholder="代表队" disabled>
            <el-option
              v-for="team in JwTeamList"
              :key="team.id"
              :label="team.indexOrder + ' : ' + team.teamName"
              :value="team.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="组别" prop="teamId">
          <el-select style="width: 380px" filterable v-model="addSportForm.gameItemId" placeholder="组别" clearable disabled>
            <el-option
              v-for="gameItem in JwGameItemList"
              :key="gameItem.id"
              :label="gameItem.code + ' : ' + gameItem.name"
              :value="gameItem.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="已有选手">
          <div class="sport-names">
            <span v-for="itemm in addSportForm.jwSignRecordSportList" class="sport-name" style="margin-right: 8px;" :class="itemm.sex === 'f'? 'female' : (itemm.sex === 'm'? 'male' : '')">{{itemm.playerName}}</span>
          </div>
        </el-form-item>

        <el-form-item label="新增选手" prop="gameItemId">
          <el-select multiple style="width: 380px" v-model="addSportForm.sportIds" filterable placeholder="选手" :popper-append-to-body="false">
            <el-option
              v-for="item in JwSportList"
              :key="item.id"
              :label="item.playerName"
              :value="item.id">
              <div class="option-sport">
                <span style="float: left; flex: 1" :class="item.sex === 'f'? 'female' : (item.sex === 'm'? 'male' : '')">{{ item.playerName }}</span>
                <span style="float: left; margin-right: 8px">{{ item.age }}岁</span>
                <dict-tag :options="dict.type.jw_sex" :value="item.sex"/>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAddSportForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="导入报名" :visible.sync="upload.open" width="400px" append-to-body>

      <el-form ref="form" :model="upload" label-width="80px">
        <el-form-item label="比赛" prop="matchId">
          <ELSelectMatch :matchId.sync="upload.matchId"/>
        </el-form-item>
      </el-form>

      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?matchId=' + upload.matchId"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {listJwSignRecord, getJwSignRecord, delJwSignRecord, addJwSignRecord, updateJwSignRecord, changeGameItem} from "@/api/jiewu/JwSignRecord";
  import {listJwTeam} from "@/api/jiewu/JwTeam";
  import {listJwGameItem} from "@/api/jiewu/JwGameItem";
  import {delJwSignRecordSport, addJwSignRecordSport} from "@/api/jiewu/JwSignRecordSport";
  import {listJwSport} from "@/api/jiewu/JwSport";
  import { getToken } from "@/utils/auth";

  export default {
    name: "JwSignRecord",
    dicts: ['jw_sport_limit', 'jw_sex'],
    data() {
      return {

        // 用户导入参数
        upload: {
          // 是否显示弹出层（用户导入）
          open: false,
          // 弹出层标题（用户导入）
          title: "",
          // 是否禁用上传
          isUploading: false,
          // 是否更新已经存在的用户数据
          matchId: 0,
          // 设置上传的请求头部
          headers: { Authorization: "Bearer " + getToken() },
          // 上传的地址
          url: process.env.VUE_APP_BASE_URL + "/jiewu/JwSignRecord/importData"
        },
        addSportForm: {gameItem: {}},
        addSportOpen: false,
        // 遮罩层
        loading: false,
        changeGameItemId: null,
        changeGameItem: false,
        changeGameItemForm: {},
        JwSportList: [],
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
        // 报名记录表格数据
        JwSignRecordList: [],
        JwGameItemList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        JwTeamList: [],
        importForm: {matchId: null},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          backNumber: null,
          teamId: null,
          matchId: (this.Cookies.get("matchId") * 1) || null,
          gameItemId: null,
          sportLimit: null,
          worksName: null,
          worksMusic: null,
          worksVideo: null,
        },
        // 表单参数
        form: {gameItem: {}},
        editSportForm: {},
        // 表单校验
        rules: {}
      };
    },
    created() {
      // this.getList();
      this.getTeamList()
      this.getGameItemList()
    },
    watch: {
      "queryParams.matchId": function (val) {
        this.getTeamList();
        this.getGameItemList();
      },
    },
    methods: {
      playMusic(item) {

      },
      submitAddSportForm() {
        addJwSignRecordSport({
          matchId: this.addSportForm.matchId,
          sportIds: this.addSportForm.sportIds,
          signRecordId: this.addSportForm.id,
          teamId: this.addSportForm.teamId,
          gameItemId: this.addSportForm.gameItemId
        }).then(res => {
          this.$modal.msgSuccess("新增成功");
          this.addSportOpen = false;
          this.getList();
        })

      },
      addSport(item, gameItem) {
        this.addSportForm = {};
        this.addSportForm = item;
        this.addSportForm.gameItem = gameItem;

        if(!this.JwTeamList.find((item) => item.id == this.queryParams.teamId)){
          this.$modal.msgError("先选择代表队");
          return;
        }
        let createUserId = this.JwTeamList.find((item) => item.id == this.queryParams.teamId).createUserId;
        listJwSport({gameItemId: gameItem.id, createUserId: createUserId, pageNum: 1, pageSize: 5000}).then(response => {
          this.JwSportList = response.rows || [];
          this.addSportOpen = true;
        });
      },
      delSport(item) {
        this.$modal.confirm('是否确认删除 " ' + item.playerName + ' " ？').then(function () {
          return delJwSignRecordSport(item.id);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      delRecord(item) {
        this.$modal.confirm('是否确认删除报名记录').then(function () {
          return delJwSignRecord(item.id);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      handleDeleteSport(sport) {

      },
      // 改组
      saveChangeGameItem() {
        if (this.changeGameItemId) {
          changeGameItem({id: this.changeGameItemForm.id, changeGameItemId: this.changeGameItemId}).then(res => {
            this.getList();
            this.changeGameItem = false;
          })
        }
      },
      handleChange(row) {
        this.changeGameItemForm = row;
        this.changeGameItemId = null;
        this.changeGameItem = true;
      },
      getGameItemName(row) {
        let item = this.JwGameItemList.find((item) => item.id == row.gameItemId);
        return item.code + ":" + item.name;
      },
      getGameItemList() {
        listJwGameItem({matchId: this.queryParams.matchId, pageNum: 1, pageSize: 5000}).then(response => {
          this.JwGameItemList = response.rows;
        });
      },
      getTeamName(row) {
        if (this.JwTeamList && this.JwTeamList.find((item) => item.id == row.teamId)) {
          return this.JwTeamList.find((item) => item.id == row.teamId).teamName;
        }
      },
      getTeamList() {
        listJwTeam({matchId: this.queryParams.matchId, pageNum: 1, pageSize: 5000}).then(response => {
          this.JwTeamList = response.rows || [];
          this.JwTeamList.sort((a,b)=>a.indexOrder - b.indexOrder)
        });
      },
      getList() {
        this.loading = true;
        this.JwSignRecordList = [];
        listJwSignRecord(this.queryParams).then(response => {
          this.JwSignRecordList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.addSportOpen = false;
        this.changeGameItem = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: null,
          backNumber: null,
          matchId: null,
          gameItemId: null,
          sportLimit: null,
          worksName: null,
          worksMusic: null,
          worksVideo: null,
          createTime: null,
          updateTime: null,
          gameItem: {},
          sportIds: []
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
        this.ids = selection.map(item => item.id);
        this.single = selection.length !== 1;
        this.multiple = !selection.length
      },
      handleAddA(){
        this.reset();

        this.form.gameItem = this.JwGameItemList[0];
        this.form.gameItemId = this.JwGameItemList[0].id;
        this.form.teamId = this.queryParams.teamId;
        this.form.matchId = this.queryParams.matchId;
        listJwSport({gameItemId: this.form.gameItemId, createUserId: this.JwTeamList.find((item) => item.id == this.form.teamId).createUserId, pageNum: 1, pageSize: 5000}).then(response => {
          this.JwSportList = response.rows;
          this.open = true;
          this.title = "添加报名";
        });

      },
      handleAdd(item) {
        this.reset();

        if(!this.JwTeamList.find((item) => item.id == this.queryParams.teamId)){
          this.$modal.msgError("先选择代表队");
          return;
        }

        this.form.gameItem = item;
        this.form.gameItemId = item.id;
        this.form.teamId = this.queryParams.teamId;
        this.form.matchId = item.matchId;
        listJwSport({gameItemId: item.id, createUserId: this.JwTeamList.find((item) => item.id == this.form.teamId).createUserId, pageNum: 1, pageSize: 5000}).then(response => {
          this.JwSportList = response.rows;
          this.open = true;
          this.title = "添加报名";
        });
      },
      handleAddGameItemChange() {
        listJwSport({gameItemId: this.form.gameItemId, createUserId: this.JwTeamList.find((item) => item.id == this.form.teamId).createUserId, pageNum: 1, pageSize: 5000}).then(response => {
          this.form.sportIds = [];
          this.JwSportList = response.rows;
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids;
        getJwSignRecord(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改报名记录";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          console.log(this.form)
          addJwSignRecord({teamId: this.form.teamId, sportIds: this.form.sportIds, gameItemId: this.form.gameItemId}).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.open = false;
            this.getList();
          });
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$modal.confirm('是否确认删除报名记录编号为"' + ids + '"的数据项？').then(function () {
          return delJwSignRecord(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('jiewu/JwSignRecord/export', {
          ...this.queryParams
        }, `报名数据_${new Date().getTime()}.xlsx`)
      },
      handleImport(){
        if(!this.queryParams.matchId){
          this.$modal.msgError("先选择比赛");
          return;
        }
        this.importForm.matchId = this.queryParams.matchId;
        this.upload.title = "导入";
        this.upload.open = true;
      },
      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
      },
      // 文件上传中处理
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true;
      },
      // 文件上传成功处理
      handleFileSuccess(response, file, fileList) {
        this.upload.open = false;
        this.upload.isUploading = false;
        this.$refs.upload.clearFiles();
        this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
        this.getList();
      },
    }
  };
</script>

<style lang="scss" scoped>
  .text-info {
    font-size: 12px;
    color: #888888;
  }
  .back-num{
    color: #07c160;
    font-weight: 600;
  }
  .quick-edit{
    white-space: nowrap;
    margin-right: 12px;
  }

  .male {
    color: #1890FF;
  }

  .female {
    color: #FF3399;
  }
  ::v-deep .el-table .cell{
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
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

  .music-icon {
    font-size: 20px;
  }

  .option-sport {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  .app-container {
    ::v-deep .el-select-dropdown__item {
      padding: 0 12px;
      /*display: flex !important;*/
      /*flex-direction: row !important;*/
      /*justify-content: space-between !important;*/
      .option-sport {
        display: flex;
        flex-direction: row;
      }
    }

    ::v-deep .el-select-dropdown.is-multiple .el-select-dropdown__item.selected {
      background: #E6A23C;
    }

    ::v-deep .el-input.is-disabled .el-input__inner {
      background: #fff;
      color: #606266;
    }
  }


</style>
