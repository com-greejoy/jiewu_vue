<template>
  <div class="zw-app-score-page">
    <div class="opt-header">
      <ELSelectMatch style="width: 200pt" v-if="showMatchSelect || !matchId" :matchId.sync="matchId"/>
    </div>
    <div class="game-info" v-html="match.matchName" @dblclick="setGameNull"></div>
    <div v-if="matchJudgeList && matchJudgeList.length > 0" class="judge-items">
      <div class="judge-item" v-for="judge in matchJudgeList"  @click="selectJudge(judge)">
        <el-image v-if="judge.img" :src="judge.img" fit="cover"/>
        <img v-if="!judge.img" src="@/assets/images/nouser.png" fit="cover" :style="`width:50px;height:50px;border-radius: 25px`"/>
        <el-link class="title" :underline="false" type="primary">{{ judge.judgeName }}</el-link>
      </div>
    </div>

    <!--<div @click="goPhone">→</div>-->

    <transition name="el-zoom-in-center">
      <div class="show-queren" v-show="currentSelectJudge.id" @click.stop="hideJudge">
        <div class="judge-con" @click.stop="aaa">
          <div class="judge-title">登录确认</div>
          <div class="judge-item">
            <el-image v-if="currentSelectJudge.img" :src="currentSelectJudge.img" fit="cover"/>
            <img v-if="!currentSelectJudge.img" src="@/assets/images/nouser.png" fit="cover" :style="`width:50px;height:50px;border-radius: 25px`"/>
            <div>{{ currentSelectJudge.judgeName }}</div>
          </div>
          <div class="judge-btn">
            <el-link :underline="false" type="warning" @click="hideJudge">取 消</el-link>
            <el-link :underline="false" type="success" @click="judgeLogin">登 录</el-link>
          </div>
        </div>
      </div>
    </transition>
    <div class="show-score" :class="{show: showScore}">
      <appHaiScore :match="match" :currentJudge="currentJudge" v-if="showScore" :visible.sync="showScore"></appHaiScore>
    </div>
  </div>
</template>

<script>
  import {getMatchInfo} from "@/api/jiewu/JwAppScore";
  import appHaiScore from '@/views/jiewu/appScore/appHaiScore';
  export default {
    name: "appScore",
    components: {appHaiScore},
    watch: {
      "matchId": function (val) {
        if (this.Cookies.get("matchId") * 1) {
          this.showMatchSelect = false;
          this.getMatchInfoHandel();
        } else {
          this.showMatchSelect = true;
        }
      },
    },
    data() {
      return {
        currentSelectJudge: {},
        showMatchSelect: false,
        match: {},
        currentJudge: {},
        matchJudgeList: [],
        showScore: false,
        matchId:  14,
      }
    },
    created() {
      this.matchId = this.$route.query.matchId;
      this.matchId = 14;
      if (!this.matchId) {
        this.showMatchSelect = true;
      } else {
        this.getMatchInfoHandel()
      }
    },
    methods: {
      goPhone(){
        this.$router.push("/battlePhoneNew");
      },
      aaa() {

      },
      setGameNull(){
        this.showMatchSelect = true;
        // this.matchId = null;
      },
      judgeLogin() {
        this.currentJudge = this.currentSelectJudge;
        this.currentSelectJudge = {};
        this.showScore = true;
      },
      hideJudge() {
        this.currentSelectJudge = {}
      },
      selectJudge(judge) {
        this.currentSelectJudge = judge
      },
      refresh() {
        this.$router.replace({path: `/appScore?r=${Math.random()}`});
      },
      getMatchInfoHandel() {
        getMatchInfo({matchId: this.matchId}).then(response => {
          this.match = response.data.match || {};
          if (this.match.mainImg) this.match.mainImg = process.env.VUE_APP_BASE_URL + this.match.mainImg;
          if (this.match.battleImg) this.match.battleImg = process.env.VUE_APP_BASE_URL + this.match.battleImg;

          this.matchJudgeList = response.data.judgeList || [];
          this.matchJudgeList.forEach(item => {
            if(item.img){
              item.img = process.env.VUE_APP_BASE_URL + item.img;
            }
          })
          // this.currentJudge = this.matchJudgeList[0]
          // this.showScore = true;
        });
      },
    }
  }
</script>

<style scoped lang="scss">
  .zw-app-score-page {
    height: 100%;
    display: flex;
    flex-direction: column;
    color: #2c3e50;

    .opt-header {
      padding: 8pt 56pt;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .game-info {
      text-align: center;
      padding: 12pt;
      font-weight: 800;
      font-size: 18pt;
    }

    .show-score {
      position: fixed;
      left: 100vw;
      right: 0;
      bottom: 0;
      top: 0;
      transition: left 0.2s;
      background: #fff;

      &.show {
        left: 0;
      }
    }

    .show-queren {
      position: fixed;
      left: 0;
      right: 0;
      bottom: 0;
      top: 0;
      display: flex;
      align-items: center;
      justify-content: center;

      .judge-con {
        width: 200px;
        border-radius: 12px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
        padding: 24px;
        background: #fff;
      }

      .judge-title {
        text-align: center;
        font-weight: 400;
        font-size: 18px;
      }

      .judge-item {
        margin-right: 0;
        margin-top: 18pt;
      }

      .judge-btn {
        text-align: center;

        .el-link {
          margin-right: 32pt;
          margin-top: 20pt;
          font-size: 18px;

          &:nth-child(2) {
            margin-right: 0;
          }
        }
      }
    }

    .judge-items {

      padding: 12pt 12pt;
      display: inline-grid;
      grid-template-columns: repeat(auto-fill, 60pt);
      grid-gap: 10px;
      justify-content: center;

      /*max-height: calc(100vh - 268px);*/
      overflow: auto;
    }

    .judge-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      /*margin-right: 24pt;*/
      margin-bottom: 4pt;
      background: #f9fafb;
      padding: 8pt;
      border-radius: 8pt;

      .el-image {
        width: 32pt;
        height: 32pt;
        border-radius: 16pt;
      }
      div{
        margin-top: 8pt;
      }

      i {
        font-weight: 600;
        color: #E6A23C;
        font-size: 20px;
        align-self: flex-end;
        cursor: pointer;
      }

      .title {
        margin-top: 8px;
      }
    }
  }
</style>
