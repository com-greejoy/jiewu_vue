<template>
  <div class="hai-con" :style="{ backgroundImage: 'url(' + background + ')' }">
    <!--<div class="match-name" v-html="match.matchName"></div>-->
    <div class="game-item-name">{{currentGameItem.name}}</div>
    <div class="title-desc">成绩公告</div>
    <div class="rank-con">
      <div class="rank-item">
        <div class="si rank">排名</div>
        <div class="si back-num">背号</div>
        <div class="si sport" :class="{longName: currentGameItem.sportLimit != 1}">选手</div>
        <div class="si team-name">代表队</div>
        <!--<div class="si avg-score">成绩</div>-->
      </div>
      <div class="rank-item row-value" v-for="item in showSportRankList">
        <div class="si rank">{{item.rankOrder}}</div>
        <div class="si back-num">{{item.backNumber}}</div>
        <div class="si sport" :class="{longName: currentGameItem.sportLimit != 1}">{{item.jwSignRecordSportList.map(item => item.playerName).join(" ")}}</div>
        <div class="si team-name">{{item.jwTeam.teamName}}</div>
        <!--<div class="si avg-score">{{item.avgScore || '-'}}</div>-->
        <!--<div class="si avg-score">{{getdesc(item.rankOrder)}}</div>-->
      </div>
    </div>
  </div>
</template>

<script>
  import {listJwHaiScore} from "@/api/jiewu/JwHaiScore";
  import {getJwMatch} from "@/api/jiewu/jwMatch";

  export default {
    name: 'haiXuanGrade',
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
        pageSize: 10,
        background: "",
        sportRankList: [],
        showSportRankList: [],
        inter: null,
        lockk: "N"
      };
    },
    computed: {},
    destroyed() {
      if (this.inter) {
        clearInterval(this.inter);
      }
    },
    created() {
      console.log("created");
      let that = this;
      that.getList();
      this.inter = setInterval(() => {
        that.getshowSportRankList()
      }, 8000);

      that.getMatchInfo()
    },
    methods: {
      getshowSportRankList(){
        let that = this;
        that.showSportRankList = that.sportRankList.slice((that.page - 1) * that.pageSize, that.page * that.pageSize);
        if (that.page * that.pageSize > that.sportRankList.length) {
          this.page = 1;
          that.getList();
        } else {
          this.page++;
        }
      },
      getdesc(indexOrder) {
        indexOrder = indexOrder * 1;
        if (indexOrder >= 1 && indexOrder <= 5) {
          return "最佳潜力奖"
        } else if (indexOrder >= 6 && indexOrder <= 10) {
          return "最佳表现奖"
        } else if (indexOrder >= 11 && indexOrder <= 17) {
          return "最佳人气奖"
        }
      },
      getList() {
        let that = this;
        listJwHaiScore({gameItemId: this.gameItemId, matchId: this.matchId}).then(res => {
          let lockk = "Y";
          (res.data || []).forEach(item => {
            item.sortOr = item.rankOrder || 999;
            if (item.jwScheduleItem.lockScore == 'N') {
              lockk = "N";
            }
          });

          this.lockk = lockk;
          this.sportRankList = (res.data || []).sort((a, b) => a.sortOr - b.sortOr);

          that.getshowSportRankList()
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
    color: #fff;
    font-weight: 600;
    background-size: 100% 100%;
    background-repeat: no-repeat;

    .match-name {
      font-size: 30px;
      text-align: center;
      /*margin-bottom: 24px;*/
    }

    .game-item-name {
      font-size: 64px;
      text-align: center;
      margin-top: 466px;
      letter-spacing: 4px;
    }

    .title-desc {
      font-size: 48px;
      text-align: center;
      margin-top: -12px;
      letter-spacing: 4px;
    }

    .rank-con {
      height: 100%;
      width: 100%;
      display: flex;
      flex-direction: column;
      padding: 8px 16px;
      overflow: auto;
      align-items: center;
      margin-top: 24px;


      .rank-item {
        display: flex;
        flex-direction: row;
        align-items: center;
        font-size: 48px;
        transform: skew(-30deg);
        margin-bottom: 26px;
        padding: 8px 48px;

        &.row-value {
          color: #fff;
          background: linear-gradient(to right bottom, #f50d0d, #111d42);
          background: linear-gradient(to right bottom, #f6c328, #f83b01);
          background: linear-gradient(to right , #d5282a, #444446);

        }

        &:first-child {
          background: none;
          color: #fff;
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
          width: 320px;
          min-width: 220px;
          margin-right: 24px;

          &.longName {
            width: 800px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
          }
        }

        .back-num {
          text-align: center;
          width: 260px;
          min-width: 160px;
        }

        .team-name {
          width: 780px;
          min-width: 780px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          margin-right: 24px;
        }

        .avg-score {
          text-align: center;
          width: 340px;
          min-width: 140px;
        }
      }
    }
  }
</style>
