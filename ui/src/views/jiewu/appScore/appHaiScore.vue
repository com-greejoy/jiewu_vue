<template>
  <div class="hai-score" v-loading="saveAllIng" @click.stop="aaac">
    <div class="hai-score-header">
      <div class="judge-name" @dblclick="setTypeHandel">{{currentJudge.judgeName}}</div>
      <div class="game-item" @click="selectGameItemHandel">
        <div class="game-item-name">{{currentGameItem.itemName || '请选择打分组别'}}</div>
        <div class="qiehuan">切换组别</div>
      </div>
      <div class="lou-out" @click="logout">退出<i class="el-icon-right"></i></div>
    </div>

    <div class="hai-score-body" v-if="!isJueSai && (!currentGameItem.scoreType || currentGameItem.scoreType == '1')" v-loading="loadSport">
      <div class="save-btn-h" v-if="currentGameItem && currentGameItem.id">
        <el-button @click="saveAllScore" type="primary" size="mini">提交分数</el-button>
      </div>
      <div class="sport-item-con-h" :class="{inputMode: match.matchConfig.hScoreMode == '1'}">

        <div class="sport-item-con">
          <div class="no-user" @click="selectGameItemHandel" v-if="!currentGameItem || !currentGameItem.id">选择组别</div>
          <div class="sport-item-box" v-for="sport in sportList">
            <div class="sport-item" :class="{select: currentSport.id == sport.id}" @click="selectSport(sport)">
              <div class="sport-index">{{sport.indexOrder}}</div>
<!--              <div class="sport-index">{{sport.backNumber}}</div>-->
<!--              <div class="sport-name w"  v-if="sport.worksName"> {{sport.worksName}}</div>-->
              <div class="sport-name">({{sport.backNumber}}) {{sport.playerName}}</div>
              <div class="sport-score" v-if="match.matchConfig.hScoreMode == '1'">
                <input type="number" v-model="sport.judgeScore" @keyup="saveScoreHandel">
              </div>
              <div class="sport-score" v-else>{{sport.judgeScore || ""}}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="score-con" v-if="match.matchConfig.hScoreMode == '2'">
        <div class="no-user" v-if="!currentSport || !currentSport.id">先选择选手</div>
        <div class="score-row zong-fen">
          <span class="demonstration">总分（100分制）</span>
          <div class="zw-tip">{{scoreAll}}</div>
          <el-slider v-model="scoreAll" :marks="marks" :min="80" :max="100" input-size="large" :step="0.01" :show-tooltip="false" @input="scoreChangeALL" @change="saveScoreHandel"></el-slider>
        </div>
        <div class="xiao-fen">
          <div class="score-left">
            <div class="score-row">
              <span class="demonstration">音乐性（20%）</span>
              <div class="zw-tip">{{score1}}%</div>
              <el-slider v-model="score1" :min="80" :max="100" input-size="large" :step="0.1" :show-tooltip="false" @input="scoreChange" @change="saveScoreHandel"></el-slider>
            </div>
            <div class="score-row">
              <span class="demonstration">技巧性（20%）</span>
              <div class="zw-tip">{{score2}}%</div>
              <el-slider v-model="score2" :min="80" :max="100" input-size="large" :step="0.1" :show-tooltip="false" @input="scoreChange" @change="saveScoreHandel"></el-slider>
            </div>
            <div class="score-row">
              <span class="demonstration">创意性（20%）</span>
              <div class="zw-tip">{{score3}}%</div>
              <el-slider v-model="score3" :min="80" :max="100" input-size="large" :step="0.1" :show-tooltip="false" @input="scoreChange" @change="saveScoreHandel"></el-slider>
            </div>
            <div class="score-row">
              <span class="demonstration">多样性（20%）</span>
              <div class="zw-tip">{{score4}}%</div>
              <el-slider v-model="score4" :min="80" :max="100" input-size="large" :step="0.1" :show-tooltip="false" @input="scoreChange" @change="saveScoreHandel"></el-slider>
            </div>
            <div class="score-row">
              <span class="demonstration">完整性（20%）</span>
              <div class="zw-tip">{{score5}}%</div>
              <el-slider v-model="score5" :min="80" :max="100" input-size="large" :show-tooltip="false" @input="scoreChange" @change="saveScoreHandel"></el-slider>
            </div>
          </div>
          <div class="score-right">
            <div class="opt-row">
              <div @click="addScore(-0.1)">
                <i class="el-icon-minus"></i>
              </div>
              <div @click="addScore(0.1)">
                <i class="el-icon-plus"></i>
              </div>
            </div>
            <div class="all-score">{{scoreAll || ""}}</div>
            <div class="save-btn danger" @click="clearScoreHandel">清除分数</div>
            <div class="save-btn" @click="saveScoreHandel">提交分数</div>
          </div>
        </div>
      </div>
    </div>

    <div class="hai-score-body score-level" v-if="!isJueSai && currentGameItem.scoreType == '2'" v-loading="loadSport">
      <div class="save-btn-h" v-if="currentGameItem && currentGameItem.id">
        <el-button @click="saveAllAward" type="primary" size="mini">提交分数</el-button>
      </div>
      <div class="sport-item-con-h" :class="{inputMode: match.matchConfig.hScoreMode == '1'}">
        <div class="sport-item-con">
          <div class="no-user" @click="selectGameItemHandel" v-if="!currentGameItem || !currentGameItem.id">选择组别</div>
          <div class="sport-item-box" v-for="sport in sportList">
            <div class="sport-item" :class="{select: currentSport.id == sport.id}" @click="selectSport(sport)">
              <div class="sport-index">{{sport.indexOrder}}</div>
              <!--              <div class="sport-index">{{sport.backNumber}}</div>-->
              <!--              <div class="sport-name w"  v-if="sport.worksName"> {{sport.worksName}}</div>-->
              <div class="sport-name">({{sport.backNumber}}) {{sport.playerName}}</div>
              <div class="sport-score ll" v-if="match.matchConfig.hScoreMode == '1'">
                <el-radio-group v-model="sport.awardId" size="mini" :key="`award-group-${sport.id}`" @change="sportAwardChange(sport)">
                  <el-radio-button v-for="award in awardList" :key="award.id" :label="award.id">{{award.rankText.replace("奖", "")}}</el-radio-button>
                </el-radio-group>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="hai-score-body" v-if="isJueSai" v-loading="loadSport">
      <juesai :currentJudge="currentJudge" :currentGameItem="currentGameItem"></juesai>
    </div>

    <transition name="el-zoom-in-center">
      <div class="show-select-game" v-show="showSelectGameItem" @click.stop="aaac">
        <div class="game-item-con" @click.stop="aaac">
          <div class="g-title">选择组别</div>
          <div class="select-types">
            <div><el-radio-group size="mini" v-model="type1">
              <el-radio-button label="">全部</el-radio-button>
              <el-radio-button label="朝阳路"></el-radio-button>
              <el-radio-button label="紫竹路"></el-radio-button>
            </el-radio-group></div>

            <div>
              <el-radio-group size="mini" v-model="type3">
                <el-radio-button label="">全部</el-radio-button>
                <el-radio-button v-if="type1 == '朝阳路'" label="一楼学术厅"></el-radio-button>
                <el-radio-button v-if="type1 == '朝阳路'" label="二楼艺术中心"></el-radio-button>
                <el-radio-button v-if="type1 == '紫竹路'" label="一楼多功能厅"></el-radio-button>
                <el-radio-button v-if="type1 == '紫竹路'" label="二楼紫竹剧场"></el-radio-button>
                <el-radio-button v-if="type1 == '紫竹路'" label="一楼民族文化厅"></el-radio-button>
              </el-radio-group>
            </div>

            <div>
              <el-radio-group size="mini" v-model="type2">
                <el-radio-button label="">全部</el-radio-button>
                <el-radio-button label="语言"></el-radio-button>
                <el-radio-button label="舞蹈"></el-radio-button>
                <el-radio-button label="音乐"></el-radio-button>
                <el-radio-button label="棋类"></el-radio-button>
                <el-radio-button label="书法"></el-radio-button>
                <el-radio-button label="美术"></el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div class="g-body" v-loading="loadingItem">
            <div v-for="itemmm in gameItemList.filter(itee=>itee.placeName.includes(type1) && itee.placeName.includes(type2) && itee.placeName.includes(type3) )">
              <div class="place-name">{{itemmm.placeName}}</div>
              <div class="game-items" v-for="item in itemmm.schedulePlaceList">
                <div class="item-chang">第 {{item.placeOrder}} 场 </div>
                <div class="item-chang-items">
                  <div class="game-item" @click="selectGameItem(scheduleItem)" :class="{select: currentSelectGameItem.id == scheduleItem.id}" v-for="scheduleItem in item.scheduleItemList">
                    <div class="item-name">第 {{scheduleItem.scheduleIndex}} 组 {{scheduleItem.area}}:{{scheduleItem.itemName}}</div>
                    <div class="item-check">
                      <i class="el-icon-check"></i>
                    </div>
                  </div>
                </div>
              </div>

            </div>
          </div>
          <div class="g-btn">
            <el-link :underline="false" type="warning" @click="hideSelect">取 消</el-link>
            <el-link :underline="false" type="primary" @click="queRenGameItem">确 认</el-link>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
  import {getScheduleItems, getSports, saveScore, saveScoreList, saveAwardList, saveAward, getGameItemAwards} from "@/api/jiewu/JwAppScore";

  import juesai from '@/views/jiewu/appScore/juesai';

  export default {
    name: "appHaiScore2",
    components: {juesai},
    props: {
      match: {
        required: true,
        type: Object,
        default: {}
      },
      currentJudge: {
        required: true,
        type: Object,
        default: {}
      },
      visible: {
        type: Boolean,
        default: true,
      },
    },
    data() {
      return {
        saveAllIng: false,
        type1:"",
        type3:"",
        type2: "",
        height: "20",
        score1: 0,
        score2: 0,
        score3: 0,
        score4: 0,
        score5: 0,
        scoreAll: 0,
        loadSport: false,
        loadingItem: false,
        currentGameItem: {},
        currentSport: {},
        sportList: [],
        currentSelectGameItem: {},
        gameItemList: [],
        showSelectGameItem: false,

        isJueSai: false,
        eightList: [],
        requestChain: Promise.resolve(),
        awardList:[],
        marks: {
          80: '80',
          // 65: '65',
          // 70: '70',
          // 75: '75',
          // 80: '80',
          85: '85',
          90: '90',
          95: '95',
          100: '100',
        }
      }
    },
    created() {

    },
    methods: {
      setTypeHandel(){
        this.currentSelectGameItem.scoreType = this.currentSelectGameItem.scoreType =="1" ? "2" : "1";
        this.queRenGameItem()
      },
      saveAllAward(){
        let that = this;
        this.saveAllIng = true;
        let allScore = [];
        this.sportList.forEach(item=>{
          allScore.push({"awardId": item.awardId * 1, "sportId": item.id * 1});
        });
        saveAwardList(allScore).then(res=>{
          that.saveAllIng = false;
          that.$notify({
            title: '成功',
            message: '分数提交成功',
            type: 'success',
            offset: 300
          });
        })
      },
      saveAllScore(){
        let that = this;
        this.saveAllIng = true;
        let allScore = [];
        this.sportList.forEach(item=>{
          allScore.push({sportId: item.id * 1 , score: item.judgeScore, judgeId : that.currentJudge.id * 1})
        });
        saveScoreList(allScore).then(res=>{
          that.saveAllIng = false;
          that.$notify({
            title: '成功',
            message: '分数提交成功',
            type: 'success',
            offset: 300
          });
        })

      },
      addScore(score) {
        this.scoreAll = ((this.scoreAll * 1) + (score * 1)).toFixed(2) * 1;
        if (this.scoreAll <= 0) {
          this.scoreAll = 0.00;
        }
        if (this.scoreAll >= 100) {
          this.scoreAll = 100
        }
        this.currentSport.judgeScore = this.scoreAll;
        this.saveScoreHandel()
      },
      selectSport(sport) {
        this.currentSport = {};
        if(this.match.matchConfig.hScoreMode == '2'){
          sport.judgeScore = (sport.judgeScore || 0) * 1;
          let avg = (sport.judgeScore / 5).toFixed(2);
          this.score1 = (avg * 5).toFixed(2) * 1;
          this.score2 = (avg * 5).toFixed(2) * 1;
          this.score3 = (avg * 5).toFixed(2) * 1;
          this.score4 = (avg * 5).toFixed(2) * 1;
          this.score5 = ((sport.judgeScore - (avg * 4)) * 5).toFixed(2) * 1;
        }

        // this.score1 = 0;
        // this.score2 = 0;
        // this.score3 = 0;
        // this.score4 = 0;
        // this.score5 = 0;
        this.currentSport = sport;
        this.scoreAll = sport.judgeScore;
      },
      async clearScoreHandel(){
        if (this.currentSport.id) {
          await saveScore({"judgeId": this.currentJudge.id, "score": null, "sportId": this.currentSport.id}).then(res => {
            this.score1 = 0;
            this.score2 = 0;
            this.score3 = 0;
            this.score4 = 0;
            this.score5 = 0;
            this.scoreAll = 0;
            this.currentSport.judgeScore = null;
            this.$notify({
              title: '成功',
              message: '清除成功',
              type: 'success',
              offset: 300
            });
          })
        }
      },

      sportAwardChange(sport){
        if (sport.id) {
          let judgeId = this.currentJudge.id * 1;
          let sportId = sport.id * 1;
          this.requestChain = this.requestChain.then(() => {
            return this.doSaveAward(judgeId, sport.awardId, sportId)
          }).catch(err => {
            console.warn('请求链中发生错误，但继续执行:', err)
          })
        }
      },
      doSaveAward(judgeId, awardId, sportId){
        return saveAward({"judgeId": judgeId, "awardId": awardId, "sportId": sportId})
      },
      saveScoreHandel() {
        if (this.currentSport.id) {
          // 将当前保存任务追加到串行队列末尾
          let judgeId = this.currentJudge.id * 1;
          let scoreAll = this.currentSport.judgeScore * 1;
          let sportId = this.currentSport.id * 1;
          this.requestChain = this.requestChain.then(() => {
            return this.doSaveSocre(judgeId, scoreAll, sportId)
          }).catch(err => {
            console.warn('请求链中发生错误，但继续执行:', err)
          })
        }
      },

      doSaveSocre(judgeId, scoreAll, sportId){
        return saveScore({"judgeId": judgeId, "score": scoreAll || null, "sportId": sportId})
      },
      // async saveScoreHandel() {
      //   if (this.currentSport.id) {
      //     await saveScore({"judgeId": this.currentJudge.id, "score": this.currentSport.judgeScore || null, "sportId": this.currentSport.id}).then(res => {
      //
      //     })
      //   }
      // },
      scoreChangeALL(v) {
        if(v === 80){
          return;
        }
        this.currentSport.judgeScore = this.scoreAll;
        // let avg = (this.scoreAll / 5).toFixed(2) * 1;
        // this.score1 = avg;
        // this.score2 = avg;
        // this.score3 = avg;
        // this.score4 = avg;
        // this.score5 = this.scoreAll - (avg * 4);
        // this.saveScoreHandel()
      },
      scoreChange(v, e) {
        if(v === 80){
          return;
        }
        console.log(e)
        this.scoreAll = (this.score1 * 0.2 + this.score2 * 0.2 + this.score3 * 0.2 + this.score4 * 0.2 + this.score5 * 0.2).toFixed(2) * 1;
        if (!this.scoreAll || this.scoreAll == 0) this.scoreAll = 0;
        this.currentSport.judgeScore = this.scoreAll;

        this.$forceUpdate()
        // this.saveScoreHandel()
      },
      selectGameItem(scheduleItem) {
        this.currentSelectGameItem = scheduleItem;

        this.currentSport = {};
        this.score1 = 0;
        this.score2 = 0;
        this.score3 = 0;
        this.score4 = 0;
        this.score5 = 0;
        this.scoreAll = 0;
      },
      queRenGameItem() {
        let that = this;
        if (this.currentSelectGameItem && this.currentSelectGameItem.id) {
          this.showSelectGameItem = false;
          this.currentGameItem = this.currentSelectGameItem;
          this.loadSport = true;

          getSports({judgeId: this.currentJudge.id, scheduleItemId: this.currentGameItem.id}).then(res => {
            let sportList = [];

            if (res.data.isJueSai) {
              // 决赛打分
              this.isJueSai = true;
              this.eightList = res.data.list || [];
            } else {
              this.isJueSai = false;
              (res.data || []).forEach(item => {
                let playerName = [];
                item["jwSignRecordSportList"].forEach((sport) => {
                  playerName.push(sport["playerName"]);
                });
                item.playerName = playerName.join(",");
                item.awardId = 0;
                sportList.push(item)
              })
              sportList.sort((a, b) => a.indexOrder - b.indexOrder)
              this.sportList = sportList;
            }

            if(that.currentGameItem.scoreType == '2'){
              getGameItemAwards({gameItemId: that.currentGameItem.gameItemId}).then(res=>{
                that.awardList = Array.from(
                  new Map((res.data || []).map(item => [item.rankText, item])).values()
                );
                let sportList = [].concat(that.sportList);
                sportList.forEach(sport=>{
                  if(sport.gradeStr){
                    sport.awardId = that.awardList.find(item=>item.id == JSON.parse(sport.gradeStr).id * 1).id + "";
                  }
                })
                that.sportList = sportList;
              })
            }
            this.loadSport = false;
          })
        } else {
          this.$notify.error({
            title: '提示',
            type: 'primary',
            message: '先选择打分组别'
          });
        }
      },
      selectGameItemHandel() {
        let that = this;
        that.showSelectGameItem = true;
        that.loadingItem = true;
        this.gameItemList = [];
        getScheduleItems({matchId: this.match.id}).then(res => {
          if (this.showSelectGameItem) {
            let gameItemList = [];
            res.data.forEach(item=>{
              // gameItemList = gameItemList.concat(item.jwSchedulePlaceList || []);

              // let gameItemList = res.data[0].jwSchedulePlaceList || [];
              let schedulePlaceList = [];
              item.jwSchedulePlaceList.forEach(place => {
                let scheduleItemList = [];
                place["jwScheduleItemList"].forEach((item) => {
                  scheduleItemList.push({
                    area: ["A", "B", "C", "D", "E", "F"][item["area"] * 1 - 1],
                    gameItemId: item["gameItemId"],
                    scheduleInfoId: item["scheduleInfoId"],
                    schedulePlaceId: item["schedulePlaceId"],
                    itemName: item["itemName"],
                    id: item["id"],
                    scoreType: item["scoreType"],
                    scheduleIndex: item["scheduleIndex"]
                  });
                });

                // scheduleItemList = scheduleItemList.sort((a, b) => a.area > b.area ? 1 : -1);

                scheduleItemList = scheduleItemList.sort((a, b) => a.scheduleIndex * 1 - b.scheduleIndex * 1);


                schedulePlaceList.push({placeOrder: place["placeOrder"], scheduleItemList: scheduleItemList});
              });
              if(schedulePlaceList){
                schedulePlaceList.sort((a, b) => a.placeOrder - b.placeOrder)
              }
              gameItemList.push({placeName: item.scheduleName, schedulePlaceList: schedulePlaceList});
            })

            this.gameItemList = gameItemList;
            // console.log(this.gameItemList)
          }
          this.currentSelectGameItem = this.currentGameItem || {};
          that.loadingItem = false
        })
      },

      aaac() {

      },
      hideSelect() {
        this.showSelectGameItem = false
      },
      logout() {
        this.$emit('update:visible', false)
      },
      refresh() {
        this.$router.replace({path: `/appScore?r=${Math.random()}`});
      },

    }
  }
</script>

<style scoped lang="scss">

  @media screen and (max-width: 500px) {
    .hai-score .hai-score-body .sport-item-con-h .sport-item-con .sport-item-box .sport-item .sport-name{
      width: 160pt !important;
      margin-left: 2pt !important;
      margin: 0 2pt !important;
    }
    .hai-score {
      .sport-item-con {
        justify-content: center;
      }

      .sport-item-box {
        width: 95% !important;

        .sport-score:not(.ll) {
          width: 56pt !important;
        }



        .sport-item{
          padding: 0 2pt !important;
        }
        .sport-score{


          ::v-deep .el-radio-button__inner{

            padding: 0px 6pt !important;
          }
        }
      }
    }
  }

  @media screen and (max-width: 768px) {
    .hai-score {
      .sport-item-con {
        justify-content: center;
      }

      .sport-item-box {
        width: 95% !important;

        .sport-score:not(.ll) {
          width: 56pt !important;
        }

        .sport-name {
          width: 160pt !important;
          margin-left: 12pt !important;
        }
      }
    }
  }

  .hai-score {

    user-select: none;
    display: flex;
    flex-direction: column;
    height: 100%;
    color: #2c3e50;
    background: #F2F4F7;

    ::v-deep .el-slider__runway {
      height: 12px;
      border-radius: 6px;
      margin: 12px 0;
      margin-top: 6px;
    }

    ::v-deep .el-slider__stop {
      display: none;
    }

    ::v-deep .el-slider__bar {
      height: 12px;
      background: #1572E5;
      border-radius: 6px;
    }

    ::v-deep .el-slider__button {
      width: 24px;
      height: 24px;
      border-color: #1572E5;
    }

    ::v-deep .el-slider__button-wrapper {
      top: -12px;
    }

    .hai-score-body {
      height: 0;
      flex: 1;
      display: flex;
      flex-direction: column;
      .save-btn-h{
        text-align: right;
        margin-right: 12pt;
        margin-top: 6pt;
        .el-button{
          font-size: 8pt;
          padding: 6px;
        }
      }
      &.score-level{
        .sport-item-con-h{
          margin-bottom: 12pt !important;
          .sport-item-con{
            .sport-item-box{
              width: 80%;
              margin-bottom: 12pt;
              .sport-item{
                padding-right: 0;
                border-radius: 4px;

                &.select{
                  border-right: none;
                }
                .sport-score{
                  width: auto;
                  .el-radio-group{
                    vertical-align: bottom;
                  }
                  ::v-deep .el-radio-button__inner{
                    height: 32px;
                    line-height: 32px;
                    padding: 0px 10pt;
                  }
                }
              }
            }
          }
        }
      }

      .sport-item-con-h {
        flex: 1;
        height: 0;
        overflow: auto;

        position: relative;
        background: #fff;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
        margin: 12pt 6pt;
        border-radius: 12px;
        &.inputMode{
          margin-top: 4pt;
          /*margin-bottom: 280pt;*/
        }
        .sport-item-con {
          flex: 1;
          display: flex;
          flex-direction: row;
          flex-wrap: wrap;
          flex-grow: 0;
          padding: 12pt 0;

          .sport-item-box {
            width: 50%;
            display: flex;
            flex-direction: row;
            align-items: center;
            justify-content: center;
            margin-bottom: 6pt;

            .sport-item {
              width: calc(100% - 0pt);

              display: flex;
              flex-direction: row;
              align-items: center;
              height: 32px;
              padding: 0 6pt;

              background: #F9FAFB;
              border: 1px solid #F9FAFB;
              border-radius: 8px;

              &.select {
                color: #1572E5;
                background: #EFF8FF;
                border: 1px solid #1572E5;

                i {
                  visibility: visible;
                }

                .sport-score {
                  border-bottom: none;
                }
              }

              .sport-index {
                width: 24pt;
                height: 20pt;
                min-width: 20pt;
                min-height: 20pt;
                line-height: 22pt;
                border-radius: 10pt;
                text-align: center;
                background: #E6A23C;
                font-weight: 600;
                color: #fff;
              }

              .sport-name {
                flex: 1;
                width: 160px;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
                margin: 0 6pt;
                .w{
                  width: 200px;
                }
              }

              .sport-score {
                height: 32px;
                line-height: 32px;
                font-weight: 600;
                width: 40pt;
                text-align: center;
                /*border-bottom: 1px solid #606266;*/
                input{
                  border: none;
                  width: 100%;
                  height: 100%;
                  background: transparent;
                  border-bottom: 1px solid #606266;
                  outline: none;
                  text-align: center;
                  font-size: 16px;
                  font-weight: 900;
                  &:focus{
                    outline: none;
                  }
                }
              }
            }
          }
        }
      }

      .no-user {
        position: absolute;
        left: 0;
        right: 0;
        bottom: 0;
        top: 0;
        background: rgba(0, 0, 0, .35);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 8888;
        font-weight: 900;
        color: #1572E5;
        border-radius: 12px;
      }

      .score-con {
        margin: 12pt;
        border-radius: 12px;
        padding-bottom: 12pt;
        /*height: 300pt;*/
        position: relative;

        background: #fff;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);

        .zong-fen {
          display: flex;
          flex-direction: column;
          padding: 8pt 12pt;
          position: relative;

          .zw-tip {
            top: 8px !important;
          }
        }

        .xiao-fen {
          display: flex;
          flex-direction: row;
          position: relative;
        }

        .score-row {
          position: relative;

          .demonstration {
            font-size: 12px;
          }

          .zw-tip {
            width: 40px;
            height: 12pt;
            line-height: 12pt;
            border-radius: 8pt;
            text-align: center;
            background: #1572E5;
            color: #fff;
            font-size: 8pt;
            font-weight: 600;
            position: absolute;
            right: 12pt;
            top: 2pt;
          }
        }

        .score-left {
          flex: 1;
          display: flex;
          flex-direction: column;
          padding: 0 12pt;


        }

        .score-right {
          width: 80pt;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: space-between;
          font-weight: 800;
          padding-bottom: 12px;

          .opt-row {
            display: flex;
            flex-direction: row;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 24px;

            div {
              width: 32pt;
              height: 32pt;
              border-radius: 16pt;
              display: flex;
              align-items: center;
              justify-content: center;
              color: #fff;

              i:before {
                font-weight: 900;
              }

              &:first-child {
                background: #E6A23C;
                margin-right: 16px;
              }

              &:nth-child(2) {
                background: #1572E5;
              }
            }
          }

          .all-score {
            margin-bottom: 24px;
            font-size: 26pt;
          }

          .save-btn {
            height: 32pt;
            text-align: center;
            line-height: 36pt;
            padding: 0 12pt;
            background: #1572E5;
            /*padding: 8pt 16pt;*/
            color: #fff;
            border-radius: 16pt;
            &.danger{
              background: #ff4949;
              height: 24pt;
              line-height: 28pt;
              font-size: 10pt;
              text-align: center;

              margin-bottom: 8pt;
            }
          }
        }
      }
    }

    .hai-score-header {
      display: flex;
      flex-direction: row;
      align-items: center;
      padding: 8pt 0;
      height: 40pt;
      background: #fff;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
      border-top: 2px solid #DA4F33;
      /*margin-top: 8pt;*/
      padding-top: 16pt;
      /*background: black;*/
      .judge-name {
        /*width: 100pt;*/
        font-weight: 800;
        color: #E6A23C;
        padding: 0 8pt;

      }

      .game-item {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
        width: 0;
        flex: 1;
        font-weight: 800;
        padding-left: 12pt;
        margin-right: 12pt;
        /*color: #409EFF;*/

        .game-item-name {
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }

        .qiehuan {
            white-space: nowrap;
        }
      }

      .lou-out {
        /*width: 48pt;*/
        font-weight: 600;
        color: #E6A23C;
        padding: 0 8pt;
        text-align: right;

      }

    }

    .show-select-game {

      position: fixed;
      left: 0;
      right: 0;
      bottom: 0;
      top: 0;
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 9999;
      background: rgba(0, 0, 0, .2);

      .game-item-con {
        display: flex;
        flex-direction: column;
        /*align-items: center;*/
        position: absolute;
        top: 24pt;
        left: 12pt;
        right: 12pt;
        bottom: 12pt;

        border-radius: 12px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
        padding: 0;
        background: #fff;
      }

      .g-title {
        text-align: center;
        font-weight: 600;
        font-size: 16px;
        padding: 8pt 24pt;
        border-radius: 12px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
      }
      .select-types{
        margin-top: 4pt;
        text-align: center;
        .el-radio-group:first-child{
          margin-bottom: 4pt;
        }
      }

      .g-body {
        flex: 1;
        margin-right: 0;
        margin-top: 2pt;
        padding: 4pt 8pt;
        overflow: auto;
        .place-name{
          text-align: center;
          font-weight: 900;
          font-size: 13pt;
          margin-bottom: 8pt;
        }

        .game-items {
          padding: 4pt 8pt;
          margin-bottom: 6pt;
          border-radius: 6px;
          box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);

          display: flex;
          flex-direction: row;
          align-items: center;

          .item-chang {
            font-size: 12pt;
            font-weight: 600;
            text-align: center;
            margin-right: 16pt;
          }

          .item-chang-items {
            flex: 1;
            display: flex;
            flex-direction: column;
            /*flex-wrap: wrap;*/
            /*justify-content: space-between;*/

            .game-item {
              padding: 2pt 8pt;
              border-radius: 4pt;
              display: flex;
              flex-direction: row;
              align-items: center;
              /*width: 45%;*/
              background: #F9FAFB;
              border: 1px solid #F9FAFB;
              margin-bottom: 6pt;

              &:last-child {
                margin-bottom: 0;
              }

              &.select {
                color: #1572E5;
                background: #EFF8FF;
                border: 1px solid #1572E5;

                i {
                  visibility: visible;
                }
              }

              i {
                visibility: hidden;
              }

              .item-name {
                width: 0;
                flex: 1;
                margin-right: 6pt;
              }
            }
          }

        }
      }

      .g-btn {
        display: flex;
        flex-direction: row;
        align-items: center;
        border-radius: 12px;
        text-align: center;
        padding: 0pt 24pt;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);

        .el-link {
          flex: 1;
          /*margin-right: 88pt;*/
          font-size: 14pt;
          font-weight: 900;
          /*width: 120px;*/
          height: 64px;

          &:nth-child(2) {
            margin-right: 0;
          }
        }
      }
    }


  }
</style>
