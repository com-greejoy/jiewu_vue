<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="比赛" prop="matchId">
        <ELSelectMatch :matchId.sync="queryParams.matchId"/>
      </el-form-item>

      <el-form-item label="组别" prop="gameItemId">
        <el-select filterable v-model="queryParams.gameItemId" placeholder="组别" clearable @change="getList">

          <el-option
            v-if="gameItem.isMusic == 'Y'"
            v-for="gameItem in JwGameItemList"
            :key="gameItem.id"
            :label="gameItem.code + ' : ' + gameItem.name"
            :value="gameItem.id"
          />
        </el-select>
      </el-form-item>

    </el-form>
    <div class="record-items">
      <div class="record-item" v-for="item in showJwSignRecordList" :class="{current: item.id==currentItem.id}">
        <div class="item-header">
          <div class="item-order">{{item.indexOrder}}</div>
          <div class="item-backNum">{{item.backNumber}}</div>
          <div style="flex:1;"></div>
          <div @click="handleStopMusic(item)" class="stop-play" v-if="item.id==currentItem.id">暂停</div>
          <div @click="handlePlayMusic(item)" class="item-play">播放</div>
        </div>
        <div class="item-body">
          <div class="team-name">{{getTeamName(item)}}</div>
          <div class="player-name">
            <span  v-for="sport in item.jwSignRecordSportList">{{ sport.playerName }}</span>
          </div>
          <div class="music-name">{{item.worksName}} - {{item.worksMusicName}}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {listQiWuMusic} from "@/api/jiewu/JwSignRecord";
  import {listJwTeam} from "@/api/jiewu/JwTeam";
  import {listJwGameItem} from "@/api/jiewu/JwGameItem";
  import {sendMusic} from "@/api/jiewu/ScreenSend";

  export default {
    name: "zhuChiMusic",
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
        currentItem:{}
      };
    },
    created() {
      this.getTeamList()
      this.getGameItemList()
      this.getList();
    },
    watch: {
      "queryParams.matchId": function (val) {
        this.getTeamList();
        this.getGameItemList();
      },
    },
    methods: {

      handlePlayMusic(item) {
        this.currentItem = item;
        sendMusic({worksMusic: item.worksMusic, matchId: this.queryParams.matchId}).then(res => {
          this.$modal.msgSuccess("发送成功");
        })
      },
      handleStopMusic() {
        this.currentItem = {};
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

      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
    }
  };
</script>

<style lang="scss" scoped>
  .app-container{
    padding: 16pt 0;
    overflow: hidden;
    height: 100%;
    display: flex;
    flex-direction: column;
  }
  .record-items {
    overflow: auto;
    display: flex;
    flex-direction: column;
    padding: 4pt 0;

    .record-item {
      margin: 4pt 8pt;

      border-radius: 8pt;
      padding: 8pt;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
      &.current{
        box-shadow: 0 2px 12px 0 #409EFF;
      }
      .item-header {
        display: flex;
        flex-direction: row;
        align-items: center;
        margin-bottom: 4pt;

        .item-order {
          display: flex;
          justify-content: center;
          align-items: center;
          background: #E6A23C;
          color: #fff;
          font-weight: 600;
          border-radius: 50%;
          padding: 8pt 8pt;
          min-width: 28pt;
        }

        .item-backNum {
          display: flex;
          justify-content: center;
          align-items: center;
          background: #67C23A;
          color: #fff;
          font-weight: 600;
          border-radius: 8pt;
          padding: 8pt 8pt;
          margin-left: 16pt;
        }
        .stop-play{
          display: flex;
          justify-content: center;
          align-items: center;
          background: #F56C6C;
          color: #fff;
          font-weight: 600;
          border-radius: 8pt;
          padding: 8pt 16pt;
          margin-left: 16pt;
        }
        .item-play{
          display: flex;
          justify-content: center;
          align-items: center;
          background: #409EFF;
          color: #fff;
          font-weight: 600;
          border-radius: 8pt;
          padding: 8pt 16pt;
          margin-left: 16pt;
        }
      }
    }
  }

</style>
