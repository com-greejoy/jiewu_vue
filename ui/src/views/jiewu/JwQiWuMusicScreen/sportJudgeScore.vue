<template>
  <div class="hai-con" :style="{ backgroundImage: 'url(' + background + ')' }">
    <div class="game-item-name background-linear-gradient">
      <div>{{match.matchName}}</div>
    </div>
    <div v-if="jwSignRecord" class="sport-info background-linear-gradient">
      <div class="sport-index">出场序号：{{jwSignRecord.indexOrder}}</div>
      <div class="sport-work" v-if="jwSignRecord.worksName">作品名称：{{jwSignRecord.worksName}}</div>
      <div v-if="jwSignRecord && jwSignRecord.jwSignRecordSportList" class="sport-name">选手：{{jwSignRecord.jwSignRecordSportList.map(item => item.playerName).join(" ")}}</div>
      <div v-if="jwSignRecord && jwSignRecord.teamName" class="sport-team" >代表队：{{jwSignRecord.teamName}}</div>
    </div>
    <div class="title-desc background-linear-gradient">
      <div>裁判打分</div>
    </div>
    <div v-if="jwSignRecord" class="score-con">
      <div class="judges-container">
        <div
          v-for="(judge, index) in jwHaiScoreList "
          :key="index"
          class="judge-card"
          :class="{
            'middle': judge.judgeId != minScore && judge.judgeId != maxScore,
            'extreme': judge.judgeId === maxScore || judge.judgeId === minScore
          }"
        >
          <img class="judge-avatar" :src="getJudgeImg(judge)" fit="cover"/>

          <div class="judge-score">{{ judge.score }}</div>
          <div class="judge-name" v-html="judge.judgeName"></div>
          <div v-if="judge.judgeId === maxScore" class="tag highest">最高分</div>
          <div v-else-if="judge.judgeId === minScore" class="tag lowest">最低分</div>
          <div v-else class="tag "></div>
        </div>
      </div>
      <div class="final-score-box">
        <div class="final-score-label">最终得分（去除最高 & 最低后平均）</div>
        <div class="final-score-value">{{ jwSignRecord.avgScore }}</div>
      </div>
    </div>
  </div>
</template>

<script>
  import {listJwHaiScoreBySport} from "@/api/jiewu/JwHaiScore";
  import {getJwMatch} from "@/api/jiewu/jwMatch";

  export default {
    name: 'sportJudgeScore',
    props: {
      matchId: {
        required: true,
        type: Number,
        default: 0
      },
      jwSignRecordId: {
        required: true,
        type: Number
      }
    },
    watch: {
      "matchId": function (val) {
        this.getMatchInfo()
      },
    },
    data() {
      return {
        match: {},
        background: "",
        jwSignRecord: {},
        scores: [],
        jwHaiScoreList: []
      };
    },
    computed: {
      maxScore() {
        return ([...this.jwHaiScoreList].filter(aa => aa.score) || []).sort((b, a) => a.score * 1 - b.score * 1)[0].judgeId;
      },
      minScore() {
        return ([...this.jwHaiScoreList].filter(aa => aa.score) || []).sort((a, b) => a.score * 1 - b.score * 1)[0].judgeId;
      }
    },
    destroyed() {

    },
    created() {
      let that = this;
      that.getMatchInfo()
      that.getJudgeScoreList()
    },
    methods: {
      getJudgeImg(judge) {
        return process.env.VUE_APP_BASE_URL + judge.judgeImg;
      },
      getJudgeScoreList() {
        listJwHaiScoreBySport({id: this.jwSignRecordId}).then(res => {
          this.jwSignRecord = res.data[0] || {};
          this.jwHaiScoreList = (this.jwSignRecord.jwHaiScoreList || []).sort((a, b) => a.judgeId - b.judgeId)
        })
      },
      getMatchInfo() {
        getJwMatch(this.matchId).then(response => {
          this.match = response.data || {};
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

    align-items: center;
    justify-content: center;
    padding-top: 166px;

    .match-name {
      font-size: 56px;
      text-align: center;
      margin-bottom: 56px;
      margin-top: 56px;
    }

    .game-item-name {
      font-size: 64px;
      text-align: center;
      margin-top: 100px;
      letter-spacing: 4px;
      display: flex;
      align-items: center;
      justify-content: center;

      div {
        padding: 8px 64px;
      }
    }

    .sport-info {
      margin: 40px;
      display: flex;
      flex-direction: row;
      align-items: center;
      font-size: 48px;
      padding: 16px 64px;
      white-space: nowrap;

      .sport-index {
        /*margin-right: 120px;*/
      }
      .sport-name{
        white-space: normal;
      }

      .sport-team, .sport-name, .sport-work{
        margin-left: 120px;
      }
    }

    .title-desc {
      font-size: 48px;
      text-align: center;
      letter-spacing: 4px;
      display: flex;
      align-items: center;
      justify-content: center;

      div {
        padding: 8px 64px;
      }
    }

    .score-con {
      width: 90vw;
      padding: 100px 60px;
      margin-top: -100px;
      background: rgba(151, 59, 42, .6);
      border-radius: 24pt;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);

      transform: scale(0.65);
    }

    .judges-container {
      font-size: 40px;
      display: flex;
      justify-content: space-around;
      flex-wrap: wrap;
      /*gap: 240px;*/
      margin-bottom: 48px;
    }

    .judge-card {
      /*width: 100px;*/
      display: flex;
      flex-direction: column;
      align-items: center;
      transition: all 0.2s ease;
      /*margin-left: 100px;*/
      /*margin-right: 100px;*/

      &:first-child {
        margin-left: 0;
      }
    }

    .judge-avatar {
      width: 160px;
      height: 160px;
      border-radius: 32px;
      object-fit: cover;
      /*background-color: #f1f5f9;*/
      margin-bottom: 40px;
    }

    /* 中间分数（突出） */
    .judge-card.middle {
      /*transform: scale(1.1);*/
    }

    .judge-card.middle .judge-avatar {
      border: 3px solid #4f46e5;
    }

    .judge-card.middle .judge-score {
      background: #4f46e5;
      color: white;
      font-weight: bold;
    }

    /* 最高/最低分（弱化） */
    .judge-card.extreme {
      opacity: 0.85;
    }

    .judge-card.extreme .judge-avatar {
      border: 2px solid #cbd5e1;
    }

    .judge-card.extreme .judge-score {
      background: #e2e8f0;
      color: #64748b;
    }

    .judge-score {
      /*width: 70px;*/
      height: 80px;
      display: flex;
      padding: 0 24px;
      justify-content: center;
      align-items: center;
      border-radius: 12px;
      font-size: 80px;
      letter-spacing: 4px;
      font-weight: 600;
      margin-bottom: 32px;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
    }

    .judge-name {
      letter-spacing: 4px;
      font-size: 48px;
      color: #fff;
      font-weight: 600;
      /*max-width: 100px;*/
      /*overflow: hidden;*/
      /*text-overflow: ellipsis;*/
      /*white-space: nowrap;*/
    }

    .tag {
      height: 52px;
      line-height: 52px;
      font-size: 28px;
      padding: 0 12px;
      border-radius: 10px;
      margin-top: 12px;
      /*font-weight: bold;*/
    }

    .tag.highest {
      background: #fee2e2;
      color: #dc2626;
    }

    .tag.lowest {
      background: #dbeafe;
      color: #1d4ed8;
    }

    .final-score-box {
      text-align: center;
      background: #f0f9ff;
      padding: 40px;
      border-radius: 16px;
      margin-top: 20px;
      border: 2px dashed #bae6fd;
    }

    .final-score-label {
      font-size: 40px;
      color: #0c4a6e;
      margin-bottom: 40px;
    }

    .final-score-value {
      font-size: 120px;
      font-weight: bold;
      color: #0284c7;
    }

  }
</style>
