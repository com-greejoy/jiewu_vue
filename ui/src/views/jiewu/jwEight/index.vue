<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="比赛" prop="matchId">
        <ELSelectMatch :matchId.sync="queryParams.matchId"/>
      </el-form-item>
      <el-form-item label="组别" prop="gameItemId">
        <el-select style="width: 360px" filterable v-model="queryParams.gameItemId" placeholder="组别" clearable @change="gameItemChange">
          <el-option
            v-for="gameItem in JwGameItemList"
            :key="gameItem.id"
            :label="gameItem.code + ' : ' + gameItem.name + (gameItem.matchType == '2' ?  ' (晋级' + gameItem.promotionNum + ')' : '')"
            :value="gameItem.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">刷新</el-button>

      </el-form-item>
    </el-form>

    <el-row class="mb8">
      <el-button type="warning" plain size="mini"  @click="haiXuanComplete">16强名单</el-button>
      <el-button type="success" plain size="mini"  @click="printJinJiSport">8强名单</el-button>
      <el-button type="success" plain size="mini"  @click="printJinJiSportGrade">4强名单</el-button>
      <el-divider direction="vertical" style="margin: 0 32px"></el-divider>
      <el-button type="warning" plain size="mini"  @click="handleSendBattle">投屏对阵</el-button>
      <el-divider direction="vertical" style="margin: 0 32px"></el-divider>
      <el-button type="primary" plain size="mini" @click="handlePrintZhengShu">证书打印</el-button>
    </el-row>

    <battle :class="{four: currentGameItem.promotionNum == 4, eight: currentGameItem.promotionNum == 8, sixteen: currentGameItem.promotionNum == 16, thirtyTwo: currentGameItem.promotionNum == 32}" :gameItemId.sync="queryParams.gameItemId" :currentGameItem.sync="currentGameItem" :isPhone="true" :matchId.sync="queryParams.matchId"></battle>

  </div>
</template>

<script>
  import {listJwEight, getJwEight, delJwEight, addJwEight, updateJwEight, saveEightPro} from "@/api/jiewu/jwEight";
  import {listJwGameItem} from "@/api/jiewu/JwGameItem";
  import {getJwMatch} from "@/api/jiewu/jwMatch";
  import battle from '@/views/jiewu/JwQiWuMusicScreen/battle'   //普通用的
  // import battle from '@/views/jiewu/JwQiWuMusicScreen/battleNewLeShan'; // 乐山用的
  import {sendBattle} from "@/api/jiewu/ScreenSend";

  export default {
    name: "JwEight",
    components:{battle},
    data() {
      return {
        showMC: false,
        showMPos: {},
        showM: false,
        // 遮罩层
        loading: true,
        match: {},
        background: "",
        showSearch: true,
        // 对阵名单表格数据
        jwEightList: [],
        JwGameItemList: [],
        proPlayer: {},
        currentGameItem:{},
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          playerId: null,
          playerIndex: null,
          matchId: (this.Cookies.get("matchId") * 1) || null,
          gameItemId: 0,
          playerPosition: null,
          playerPkGroup: null,
          groupIndex: null
        }
      };
    },
    created() {
      this.getGameItemList();
      // this.getList();
      // this.getMatchInfo();
    },
    watch: {
      "queryParams.matchId": function (val) {
        this.getGameItemList();
        if(this.Cookies.get("matchId") * 1){
          // this.getMatchInfo()
        }else{
        }
      },
    },
    methods: {
      // 打印证书
      handlePrintZhengShu(){

      },
      // 投屏对阵
      handleSendBattle(){
        sendBattle({ gameItemId: this.queryParams.gameItemId, matchId: this.queryParams.matchId}).then(res=>{
          this.$modal.msgSuccess("发送成功");
        })
      },
      gameItemChange() {
        this.currentGameItem = this.JwGameItemList.find(item => item.id == this.queryParams.gameItemId);
        // this.getList()
      },
      getGameItemList() {
        this.JwGameItemList = [];
        listJwGameItem({matchId: this.queryParams.matchId, pageNum: 1, pageSize: 5000, matchType: 2}).then(response => {
          this.JwGameItemList = (response.rows || []).filter(ite=>ite.signCount > 0);
        });
      },


      handleQuery() {
        this.queryParams.pageNum = 1;
        // this.getList();
      },

    }
  };
</script>

<style >
  .battle-con{
    /*transform: scale(0.5);*/
    .eight-box{
      background: #a39898;
      .game-item-name, .title-desc{
        display: none;
      }
      .eight-con{
        margin-top: 0;
        &.four{
          transform: scale(0.8);
        }
        &.eight{
          transform: scale(0.8);
        }
        &.sixteen{
          transform: scale(0.7);
        }
        &.thirtyTwo{
          transform: scale(0.6);
        }
      }
    }
  }

</style>
