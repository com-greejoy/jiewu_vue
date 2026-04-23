<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="比赛" prop="matchId">
        <ELSelectMatch :matchId.sync="queryParams.matchId"/>
      </el-form-item>
      <el-form-item label="代表队" prop="teamId">
        <el-select filterable v-model="queryParams.teamId" placeholder="代表队" clearable>
          <el-option
            v-for="team in JwTeamList"
            :key="team.id"
            :label="team.indexOrder + ' : ' + team.teamName"
            :value="team.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="组别" prop="gameItemId">
        <el-select filterable v-model="queryParams.gameItemId" placeholder="组别" clearable>

          <el-option
            v-if="gameItem.isMusic == 'Y'"
            v-for="gameItem in JwGameItemList"
            :key="gameItem.id"
            :label="gameItem.code + ' : ' + gameItem.name"
            :value="gameItem.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="背号" prop="backNumber" style="width:180px;">
        <el-input
          style="width:100px;"
          v-model="queryParams.backNumber"
          placeholder="请输入背号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-radio-group v-model="showType" size="mini" @change="toggleShowData">
          <el-radio-button label="1">全部</el-radio-button>
          <el-radio-button label="2">已传</el-radio-button>
          <el-radio-button label="3">未传</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item>

        <el-button type="danger" size="mini" v-if="JwSignRecordList" >{{JwSignRecordList.filter(item=>item.worksMusicName).length}} / {{JwSignRecordList.length}}</el-button>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button type="warning" size="mini" @click="downloadAllMusic">下载音乐</el-button>
        <el-button type="danger" size="mini" @click="handleStopMusic()">暂停</el-button>

      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="showJwSignRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" width="55" align="center" prop="id"/>
      <el-table-column label="代表队" width="220" align="left" prop="teamId">
        <template slot-scope="scope">
          <span>{{ getTeamName(scope.row) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="组别" width="220" align="left" prop="gameItemId">
        <template slot-scope="scope">
          <span>{{ getGameItemName(scope.row) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="背号" width="88" align="center" prop="backNumber">
        <template slot-scope="scope">
          <span style="color: #07c160; font-weight: 600;">{{ scope.row.backNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column label="选手" align="left" prop="backNumber">
        <template slot-scope="scope">
          <span style="color: #1890ff; font-weight: 600;margin-right: 8pt;" v-for="sport in scope.row.jwSignRecordSportList">{{ sport.playerName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="出场顺序" width="88" align="center" prop="indexOrder">
        <template slot-scope="scope">
          <span style="color: #E6A23C; font-weight: 600;">{{ scope.row.indexOrder }}</span>
        </template>
      </el-table-column>
      <el-table-column label="作品名称" align="left" width="220" prop="worksName">
        <template slot-scope="scope">
          <el-popover
            @show="reWorksName = scope.row.worksName"
            placement="top"
            :width="200"
            v-model="scope.row.visible">
            <div style="text-align: center; margin: 0">
              <el-input size="small" v-model="reWorksName" controls-position="right" :min="1"/>
              <div class="area-btns" style="margin-top: 24px">
                <el-link :underline="false" type="danger" @click="handleChangeWorksName(scope.row)">确认</el-link>
              </div>
            </div>
            <div slot="reference" class="area-btn">
              <el-link :underline="false" type="danger">
                <div style="cursor: pointer">{{scope.row.worksName || '-'}}</div>
              </el-link>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column label="音乐/视频" align="left" prop="worksMusicName"/>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button @click="handleUploadMusic(scope.row)" type="warning" size="mini" icon="el-icon-video-play" >上传</el-button>
          <el-button @click="handleDoloadMusic(scope.row)" type="warning" size="mini" icon="el-icon-video-play" >下载</el-button>
          <el-button v-if="scope.row.worksMusic" @click="handleOpenMusic(scope.row)" type="success" size="mini" icon="el-icon-video-play" ></el-button>
          <el-button @click="handlePlayMusic(scope.row)" type="success" size="mini" icon="el-icon-video-play" >远程</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="音乐视频" :visible.sync="musicOpen" center :append-to-body="false">
      <video controls autoplay ref="videoElement" style="width: 100%" v-if="musicOpen">
        <source :src="musicUrl" type="video/mp4">
        Your browser does not support the video tag.
      </video>
    </el-dialog>

    <el-dialog title="导入音乐" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".MP3, .mp4"
        :headers="upload.headers"
        :action="upload.url + '?jwSignRecordId=' + upload.jwSignRecordId"
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
  import {listQiWuMusic, updateJwSignRecord} from "@/api/jiewu/JwSignRecord";
  import {listJwTeam} from "@/api/jiewu/JwTeam";
  import {listJwGameItem} from "@/api/jiewu/JwGameItem";
  import {sendMusic} from "@/api/jiewu/ScreenSend";
  import {getToken} from "@/utils/auth";

  export default {
    name: "JwQiWuMusic",
    dicts: ['jw_sport_limit', 'jw_sex'],
    data() {
      return {
        reWorksName: "",
        loading: true,
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
        showJwSignRecordList: [],
        JwGameItemList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        musicOpen: false,
        musicUrl: "",
        JwTeamList: [],
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
          orderByColumn: "indexOrder",
          isAsc: "ascending"
        },
        form: {gameItem: {}},
        rules: {},
        ws: null,
        showType: "1",
        upload: {
          // 是否显示弹出层（用户导入）
          open: false,
          // 弹出层标题（用户导入）
          title: "",
          // 是否禁用上传
          isUploading: false,
          // 是否更新已经存在的用户数据
          jwSignRecordId: 0,
          // 设置上传的请求头部
          headers: {Authorization: "Bearer " + getToken()},
          // 上传的地址
          url: process.env.VUE_APP_BASE_URL + "/jiewu/JwSignRecord/upLoadMusic"
        },
      };
    },
    created() {
      this.getTeamList();
      this.getGameItemList();
      // this.getList();
    },
    watch: {
      "queryParams.matchId": function (val) {
        this.getTeamList();
        this.getGameItemList();
      },
    },
    methods: {
      toggleShowData(){
        let showType = this.showType;
        let showJwSignRecordList = this.JwSignRecordList || [];

        // this.showType = showType;

        if(showType == "2"){
          showJwSignRecordList = showJwSignRecordList.filter(item=>item.worksMusicName)
        }else if(showType == "3"){
          showJwSignRecordList = showJwSignRecordList.filter(item=>!item.worksMusicName)
        }
        console.log(showJwSignRecordList, showType)
        this.showJwSignRecordList = showJwSignRecordList;
      },
      handleChangeWorksName(row) {
        if (this.reWorksName) {
          updateJwSignRecord({id: row.id, worksName: this.reWorksName}).then(res => {
            this.reWorksName = "";
            this.getList()
          })
        }
      },

      handleUploadMusic(row) {
        this.upload.jwSignRecordId = row.id;
        this.upload.title = "导入音乐";
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
        this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {dangerouslyUseHTMLString: true});
        this.getList();
      },
      handlePlayMusic(item) {
        sendMusic({worksMusic: item.worksMusic, matchId: this.queryParams.matchId}).then(res => {
          this.$modal.msgSuccess("发送成功");
        })
      },
      handleStopMusic() {
        sendMusic({worksMusic: "", matchId: this.queryParams.matchId}).then(res => {
          this.$modal.msgSuccess("发送成功");
        })
      },
      handleOpenMusic(item) {
        if (item.worksMusic) {
          this.musicUrl = process.env.VUE_APP_BASE_URL + item.worksMusic;
          this.musicOpen = true;
        } else {
          this.$modal.msgError("没有音乐");
        }
      },
      async handleDoloadMusic(item) {
        if (item.worksMusic) {
          const flieArr = item.worksMusic.split('.');
          let suffix = flieArr[flieArr.length - 1];

          const response = await fetch(process.env.VUE_APP_BASE_URL + item.worksMusic); // 替换为你的API地址
          const blob = await response.blob();

          const filename = `${this.getGameItemName(item)}-${item.indexOrder}-${item.backNumber}.${suffix}`; // 自定义文件名
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement("a");
          a.href = url;
          a.download = filename;
          document.body.appendChild(a);
          a.click();
          window.URL.revokeObjectURL(url);
          document.body.removeChild(a);
        }
      },
      async downloadAllMusic() {

        for (let i = 0; i< this.JwSignRecordList.length; i++) {

          let item =  this.JwSignRecordList[i];

        // for (const item of this.JwSignRecordList) {
          if (item.worksMusic) {
            const response = await fetch(process.env.VUE_APP_BASE_URL + item.worksMusic); // 替换为你的API地址
            const blob = await response.blob();
            const flieArr = item.worksMusic.split('.');
            let suffix = flieArr[flieArr.length - 1];

            const filename = `${this.getGameItemName(item)}-${item.indexOrder}-${item.backNumber}.${suffix}`; // 自定义文件名
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = filename;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);

          }else{
            console.log(i);
            console.log(item);
          }
        }
        // if (item.worksMusic) {
        //   this.musicUrl = process.env.VUE_APP_BASE_URL + item.worksMusic;
        //   this.musicOpen = true;
        // } else {
        //   this.$modal.msgError("没有音乐");
        // }
      },
      playMusic(item) {

      },

      getGameItemName(row) {
        let item = this.JwGameItemList.find((item) => item.id == row.gameItemId);
        if (item) {
          return item.code + ":" + item.name;
        } else {
          return row.gameItemId;
        }

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
          this.JwTeamList.sort((a, b) => a.indexOrder - b.indexOrder)
        });
      },
      getList() {
        this.loading = true;
        // this.queryParams.sportLimit = 3;
        this.queryParams.isMusic = 'Y';
        listQiWuMusic(this.queryParams).then(response => {
          this.JwSignRecordList = response.rows || [];
          this.showJwSignRecordList = this.JwSignRecordList;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.musicOpen = false;
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


      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids;

      },
    }
  };
</script>

<style lang="scss" scoped>


</style>
