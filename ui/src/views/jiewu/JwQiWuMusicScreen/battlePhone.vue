<template>
  <div class="battle-con-h">
    <el-form ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item label="比赛" prop="matchId">
        <ELSelectMatch :matchId.sync="matchId"/>
      </el-form-item>
      <el-form-item style="margin-left: 88px" label="组别" prop="gameItemId">
        <el-select style="width: 360px" v-model="gameItemId" placeholder="组别" clearable @change="gameItemChange">
          <el-option
            v-for="gameItem in JwGameItemList"
            :key="gameItem.id"
            :label="gameItem.code + ' : ' + gameItem.name + (gameItem.matchType == '2' ?  ' (晋级' + gameItem.promotionNum + ')' : '')"
            :value="gameItem.id"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <Battle :class="{four: currentGameItem.promotionNum == 4, eight: currentGameItem.promotionNum == 8, sixteen: currentGameItem.promotionNum == 16, thirtyTwo: currentGameItem.promotionNum == 32}"
            v-if="matchId && gameItemId"
            :gameItemId.sync="gameItemId"
            :currentGameItem.sync="currentGameItem"
            :matchId.sync="matchId"
            :isPhone="true"
    ></Battle>
  </div>
</template>

<script>
  import {listJwEight, saveEightPro} from "@/api/jiewu/jwEight";
  import {getJwMatch} from "@/api/jiewu/jwMatch";
  import Battle from '@/views/jiewu/JwQiWuMusicScreen/battle';
  import {listJwGameItem} from "@/api/jiewu/JwGameItem";

  export default {
    name: 'battlePhone',
    components: {Battle},
    watch: {
      "matchId": function (val) {
        this.getGameItemList();
      },
      "gameItemId": function (val) {
        // this.getList();
      },
    },
    data() {
      return {
        matchId: null,
        gameItemId: null,
        currentGameItem: {},
        JwGameItemList: []
      };
    },
    computed: {},
    created() {
      this.getGameItemList()
    },
    methods: {
      gameItemChange() {
        this.currentGameItem = this.JwGameItemList.find(item => item.id == this.gameItemId);
      },
      getGameItemList() {
        this.JwGameItemList = [];
        if (this.matchId) {
          listJwGameItem({matchId: this.matchId, pageNum: 1, pageSize: 5000, matchType: 2}).then(response => {
            this.JwGameItemList = response.rows;
          });
        }

      },

    }
  }
</script>

<style scoped lang="scss">

  .el-form {
    padding: 16px;
    padding-bottom: 0;
    /*text-align: center;*/
  }

  .battle-con-h {
    display: flex;
    flex-direction: column;
    background: #409EFF;

    width: 2000px;
    height: 1250px;
  }

  ::v-deep.battle-con {
    flex: 1;
  .contextmenu{
    font-size: 20px;
    color: #111d42;
  }
    .eight-box {


      .game-item-name, .title-desc {
        display: none !important;
      }

      .eight-con {
        margin-top: 0;
        .line-row .line-item{
          width: 16px !important;
          margin-right: 20px;
        }
        .line{
          width: 16px !important;
          margin-right: -36px;
        }


        &.four {
          transform: scale(1) !important;
        }

        &.eight {
          transform: scale(1) !important;
        }

        &.sixteen {
          transform: scale(1) !important;
        }

        &.thirtyTwo {
          transform: scale(1) !important;
        }
      }
    }
  }
</style>
