<template>
  <div class="hai-con" :style="{ backgroundImage: 'url(' + background + ')' }">
    <!--<div class="match-name" v-html="match.matchName"></div>-->
    <div class="game-item-name">{{currentGameItem.name}}</div>
    <div class="title-desc">晋级名单</div>
    <div class="rank-con">
      <div class="rank-item" :class="{leftLast: index == sportRankList.length / 2 - 1 , mangSport: currentGameItem.promotionNum == 32}" v-for="(item, index) in sportRankList">
        <div class="si rank">{{item.rankOrder}}</div>
        <div class="si back-num">{{item.backNumber}}</div>
        <div class="si sport">{{item.jwSignRecordSportList.map(item => item.playerName).join(" ")}}</div>
        <div class="si team-name">{{item.jwTeam.teamName}}</div>
        <div class="si avg-score">晋级{{currentGameItem.promotionNum || '-'}}强</div>
      </div>
    </div>
  </div>
</template>

<script>
  import {listJwHaiScore} from "@/api/jiewu/JwHaiScore";
  import {getJwMatch} from "@/api/jiewu/jwMatch";

  export default {
    name: 'jinjiSport',
    props: {
      matchId: {
        required: true,
        type: Number,
        default: 0
      },
      gameItemId: {
        required: true,
        type: Number
      },
      currentGameItem: {
        type: Object,
        default: {}
      }
    },
    watch: {
      "matchId": function (val) {
        this.page = 1;
        this.getMatchInfo()
      },
      "gameItemId": function (val) {
        this.page = 1;
        this.getList();
      },
    },
    data() {
      return {
        match: {},
        page: 1,
        pageSize: 5,
        background: "",
        sportRankList: [],
      };
    },
    computed: {},
    destroyed(){

    },
    created() {
      console.log("created");
      let that = this;
      that.getMatchInfo()
      that.getList();

    },
    methods: {
      getList() {
        listJwHaiScore({gameItemId: this.gameItemId, matchId: this.matchId}).then(res => {
          this.sportRankList = (res.data || []).filter(item=>(item.rankOrder && item.rankOrder <= this.currentGameItem.promotionNum)).sort((a, b) => a.rankOrder - b.rankOrder);
        })
      },
      getMatchInfo() {
        getJwMatch(this.matchId).then(response => {
          this.match = response.data || {};
          this.match.mainImg = process.env.VUE_APP_BASE_URL + this.match.mainImg;
          this.background = process.env.VUE_APP_BASE_URL + this.match.battleImg;
        });
      }
    }
  }
</script>

<style scoped lang="scss">
  .hai-con {
    z-index: 9;
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    color: #fff;
    font-weight: 600;
    background-size: 100% 100%;
    background-repeat: no-repeat;


    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding-top: 200px;
    .match-name{
      font-size: 56px;
      text-align: center;
      margin-bottom: 56px;
      margin-top: 56px;
    }
    .game-item-name{
      font-size: 64px;
      text-align: center;
      /*margin-top: 294px;*/
      letter-spacing: 4px;
    }
    .title-desc{
      font-size: 48px;
      text-align: center;
      margin-top: -12px;
      letter-spacing: 4px;
    }

    .rank-con {
      width: 100%;
      display: flex;
      flex-wrap: wrap;
      flex-direction: row;
      padding: 8px 16px;
      align-items: center;
      justify-content: center;
      padding-left: 120px;
      margin-top: 16px;

      .rank-item {
        display: flex;
        flex-direction: row;
        align-items: center;
        font-size: 48px;
        transform: skew(-30deg);
        margin-bottom: 48px;
        padding: 8px 48px;
        margin-right: 120px;
        color: #fff;
        background: linear-gradient(to right bottom, #f50d0d, #111d42);

        &.mangSport{
          margin-bottom: 4px;
          padding: 4px 48px;
        }
        .si {
          transform: skew(30deg);
        }

        .rank {
          text-align: center;
          width: 96px;
          min-width: 96px;
        }

        .sport {
          text-align: center;
          width: 200px;
          min-width: 200px;
        }

        .back-num {
          text-align: center;
          width: 120px;
          min-width: 120px;
        }

        .team-name {
          width: 480px;
          min-width: 480px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          font-size: 40px;
        }

        .avg-score {
          text-align: center;
          width: 222px;
          min-width: 222px;
        }
      }
    }
  }
</style>
