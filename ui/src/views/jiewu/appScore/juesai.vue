<template>
  <div class="juesai-con" v-loading="loadingSave">
    <div class="select-chang">
      <el-radio size="mini" v-model="currentArea" label="A" border>A场地</el-radio>
      <!--<el-radio size="mini" v-model="currentArea" label="B" border>B场地</el-radio>-->

      <el-radio size="mini" v-model="lun" label="1" border>第一轮</el-radio>
      <el-radio size="mini" v-model="lun" label="2" v-if="currentPk == '1.1' || currentPk == '3.1'" border>第二轮</el-radio>
      <el-radio size="mini" v-model="lun" label="3" v-if="currentPk == '1.1' || currentPk == '3.1'" border>第三轮</el-radio>
    </div>
    <!--{{aaa}}-->
    <div class="sport-con">
      <div class="sport-left">
        <div class="sport-info">
          <div class="sport-name">{{p1.playerName}}</div>
          <div class="sport-back">{{p1.backNumber}}</div>
          <div class="sport-score"><span>得分:</span>{{p1.score || '-'}}</div>
        </div>
      </div>
      <div class="pk-box">
        <div class="PK">VS</div>
        <div class="lun">第 {{lun}} 轮</div>
      </div>
      <div class="sport-right">
        <div class="sport-info">
          <div class="sport-name">{{p2.playerName}}</div>
          <div class="sport-back">{{p2.backNumber}}</div>
          <div class="sport-score"><span>得分:</span>{{p2.score || '-'}}</div>
        </div>
      </div>
    </div>
    <div class="score-con">
      <div class="score-row">
        <div class="row-title">
          <div class="title-center">音乐性（20%）</div>
        </div>
        <div class="row-sport">
          <div class="row-left" :class="{blue: score1l < 50, red: score1l> 50}">
            <div class="zw-tip">{{(getGenScore(score1l)).toFixed(1)}}%</div>
            <el-slider v-model="score1l" :step="0.1" @input="scoreChange" input-size="large" :show-tooltip="false"></el-slider>
          </div>
        </div>
      </div>
      <div class="score-row">
        <div class="row-title">
          <div class="title-center">技巧性（20%）</div>
        </div>
        <div class="row-sport">
          <div class="row-left" :class="{blue: score2l < 50, red: score2l> 50}">
            <div class="zw-tip">{{(getGenScore(score2l)).toFixed(1)}}%</div>
            <el-slider v-model="score2l" :step="0.1" @input="scoreChange" input-size="large" :show-tooltip="false"></el-slider>
          </div>
        </div>
      </div>
      <div class="score-row">
        <div class="row-title">
          <div class="title-center">创意性（20%）</div>
        </div>
        <div class="row-sport">
          <div class="row-left" :class="{blue: score3l < 50, red: score3l> 50}">
            <div class="zw-tip">{{(getGenScore(score3l)).toFixed(1)}}%</div>
            <el-slider v-model="score3l" :step="0.1" @input="scoreChange" input-size="large" :show-tooltip="false"></el-slider>
          </div>
        </div>
      </div>
      <div class="score-row">
        <div class="row-title">
          <div class="title-center">多样性（20%）</div>
        </div>
        <div class="row-sport">
          <div class="row-left" :class="{blue: score4l < 50, red: score4l> 50}">
            <div class="zw-tip">{{(getGenScore(score4l)).toFixed(1)}}%</div>
            <el-slider v-model="score4l" :step="0.1" @input="scoreChange" input-size="large" :show-tooltip="false"></el-slider>
          </div>
        </div>
      </div>
      <div class="score-row">
        <div class="row-title">
          <div class="title-center">完整性（20%）</div>
        </div>
        <div class="row-sport">
          <div class="row-left" :class="{blue: score5l < 50, red: score5l> 50}">
            <div class="zw-tip">{{(getGenScore(score5l)).toFixed(1)}}%</div>
            <el-slider v-model="score5l" :step="0.1" @input="scoreChange" input-size="large" :show-tooltip="false"></el-slider>
          </div>
        </div>
      </div>
    </div>
    <div class="btn-con">
      <div class="save-btn" @click="saveScore">提交打分</div>
    </div>
  </div>
</template>

<script>
  import {saveEightScore, getCurrentPk} from "@/api/jiewu/JwAppScore";

  export default {
    name: "juesai",
    props: {
      currentJudge: {
        required: true,
        type: Object,
        default: {}
      },
      currentGameItem: {
        required: true,
        type: Object,
        default: {}
      }
    },
    data() {
      return {
        aaa: '',
        p1: {},
        p2: {},
        currentPk: "",
        score1l: 100,
        score1r: 0,
        score2l: 100,
        score2r: 0,
        score3l: 100,
        score3r: 0,
        score4l: 100,
        score4r: 0,
        score5l: 100,
        score5r: 0,
        loadingSave: false,
        lun: "1",
        ws: null,
        timeout: 2000,
        interval: 10000,
        reconnect: true,
        area: ["A", "B"],
        currentArea: "A",
        eightList: [],
        inte: null

      }
    },
    destroyed() {
      if (this.inte) {
        clearInterval(this.inte);
      }
    },
    created() {
      // this.initSocket();
      // this.startHeartbeat();

      this.inte = setInterval(this.getCurrentPkHandel, 1000)
    },
    methods: {
      getGenScore(score) {
        if (score < 50) {
          return 50 - score;
        } else if (score > 50) {
          return score - 50;
        } else {
          return 0;
        }
      },
      onmessage(currentPkGroup, data) {
        this.eightList = data.list || [];
        // if ((this.lun != currentPkGroup.lun || this.currentPk != currentPkGroup.currentPk) && (currentPkGroup.area == "全" || currentPkGroup.area == this.currentArea)) {
        //   this.lun = currentPkGroup.lun;

        if ((this.currentPk != currentPkGroup.currentPk) && (currentPkGroup.area == "全" || currentPkGroup.area == this.currentArea)) {

          this.currentPk = currentPkGroup.currentPk;
          let p1 = this.eightList.find(item => item.playerPosition + "." + item.playerIndex == currentPkGroup.pk1);
          let p2 = this.eightList.find(item => item.playerPosition + "." + item.playerIndex == currentPkGroup.pk2);
          if (p1 && p2) {
            p1.playerName = (p1 && p1.jwSignRecordSportList) ? (p1.jwSignRecordSportList.map(item => item.playerName).join(" ")) : " ";
            p2.playerName = (p2 && p2.jwSignRecordSportList) ? (p2.jwSignRecordSportList.map(item => item.playerName).join(" ")) : " ";
          }

          this.p1 = p1;
          this.p2 = p2;

          this.score1l = 50;
          this.score1r = 0;
          this.score2l = 50;
          this.score2r = 0;
          this.score3l = 50;
          this.score3r = 0;
          this.score4l = 50;
          this.score4r = 0;
          this.score5l = 50;
          this.score5r = 0;
        }

      },
      getCurrentPkHandel() {
        getCurrentPk({area: this.currentArea || "全"}).then(res => {
          let data = res.data;
          let currentPkGroup = JSON.parse(data.currentPkGroup);
          this.onmessage(currentPkGroup, data)
        })
      },
      saveScore() {
        if (this.p1.score || this.p2.score) {
          if (this.p1.score == this.p2.score) {
            this.$notify.error({
              title: '错误',
              message: '分数相同，不能 ONE MORE',
              offset: 300
            });
            return;
          }
          if (this.p1.id && this.p2.id) {
            this.loadingSave = true;
            saveEightScore({
              judgeId: this.currentJudge.id,
              currentPkGroup: this.currentPk,
              jinJiId: ((this.p1.score || 0) * 1) > ((this.p2.score || 0) * 1) ? this.p1.playerId : this.p2.playerId,
              lun: this.lun
            }).then(res => {
              this.loadingSave = false;
              this.$notify({
                title: '成功',
                message: '提交成功',
                type: 'success',
                offset: 300
              });
              this.currentPk = "";

              this.p1 = {};
              this.p2 = {};

              this.score1l = 100
              this.score1r = 0
              this.score2l = 100
              this.score2r = 0
              this.score3l = 100
              this.score3r = 0
              this.score4l = 100
              this.score4r = 0
              this.score5l = 100
              this.score5r = 0
            }, () => {
              this.loadingSave = false;
            })
          }
        } else {
          this.$notify.error({
            title: '错误',
            message: '请给选手打分',
            offset: 300
          });
        }

      },
      scoreChange() {
        let p1Score = 0;
        let p2Score = 0;

        [this.score1l, this.score2l, this.score3l, this.score4l, this.score5l].forEach(item => {
          if (item < 50) {
            p1Score += (50 - item);
          } else if (item > 50) {
            p2Score += (item - 50);
          }
        });
        this.p1.score = (p1Score / 5).toFixed(2);
        this.p2.score = (p2Score / 5).toFixed(2);

        // this.p1.score = ((500 - this.score1l - this.score2l - this.score3l - this.score4l - this.score5l) / 5).toFixed(2);
        // this.p2.score = ((this.score1r + this.score2r + this.score3r + this.score4r + this.score5r) / 5).toFixed(2);
      },
      startHeartbeat() {
        let that = this;
        // 心跳检测
        setTimeout(() => {
          if (that.ws && that.ws.readyState === 1) {
            // 发送心跳
            that.ws.send('heartbeat');
          } else {
            that.reconnect = true;
            that.initSocket();
          }
          that.startHeartbeat();
        }, that.interval);
      },
      initSocket() {
        let that = this;
        if (!this.currentJudge.id) {
          return;
        }
        let url = `${process.env.VUE_APP_BASE_URL}/websocket/socket/juesai-${this.currentJudge.id}`;
        url = url.replace("http", "ws");
        // this.aaa = url;
        let ws = new WebSocket(url);
        // let ws = new WebSocket(`ws://192.168.1.252/pokejiewuadmin/websocket/socket/juesai-${this.currentJudge.id}`);

        // let ws = new WebSocket(`wss://www.greejoy.com/pokejiewu/websocket/socket/juesai-${this.currentJudge.id}`);


        this.ws = ws;
        // 连接成功后的回调函数
        ws.onopen = (params) => {
          console.log('客户端连接成功')
        };
        // 从服务器接受到信息时的回调函数
        ws.onmessage = (e) => {

          console.log('收到服务器响应', e.data)

          if (e.data != "heart") {
            let data = JSON.parse(e.data);
            if (data.type == "startPk") {
              // 发送开始打分了
              let currentPkGroup = JSON.parse(data.currentPkGroup);

              this.onmessage(currentPkGroup, data)
            }
          }
        };

        // 连接关闭后的回调函数
        ws.onclose = (evt) => {

          console.log("关闭客户端连接");
        };

        // 连接失败后的回调函数
        ws.onerror = (evt) => {

          console.log(evt, "连接失败了");
        };
      }
    }
  }
</script>

<style scoped lang="scss">
  .is-bordered {
    margin-right: 0 !important;
  }

  .juesai-con {
    user-select: none;
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    padding-bottom: 12pt;
    background: #F2F4F7;
  }

  .select-chang {
    display: flex;
    padding-top: 6pt;
    flex-direction: row;
    flex-wrap: wrap;
    align-items: center;
    justify-content: center;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    background: #fff;
    border-radius: 12px;
    margin: 6pt 12pt;
    margin-bottom: 0;
    padding-bottom: 0;

    .el-radio {
      margin-bottom: 6pt;
    }
  }

  .sport-con {
    flex: 1;

    display: flex;
    flex-direction: row;
    align-items: center;
    padding: 8pt;
    margin: 6pt 8pt;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    background: #fff;
    border-radius: 12px;

    .pk-box {
      .lun {
        text-align: center;
        font-weight: 900;
        font-size: 18pt;
      }
    }

    .PK {
      margin: 0 12pt;
      font-size: 40pt;
      color: #df5000;
      font-style: italic;

      font-family: Verdana, Geneva, STCaiyun, sans-serif;
      background-image: -webkit-linear-gradient(#ff8a00 0%, #ffffff 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;

      -webkit-filter: drop-shadow(2px 2px 15px #b052b0);

    }

    .sport-left {
      width: 0;
      flex: 1;
      text-align: center;
      background: #409EFF;
      border-radius: 16px;

      .sport-back, .sport-name {
        /*color: #409EFF;*/
      }
    }

    .sport-right {
      width: 0;
      flex: 1;
      text-align: center;
      background: #F56C6C;
      border-radius: 16px;

      .sport-back, .sport-name {
        /*color: #F56C6C;*/
      }
    }

    .sport-info {
      /*width: 120px;*/
      display: flex;
      flex-direction: column;
      padding: 8pt;
      border-radius: 16px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
      color: #fff;

      .sport-score {
        color: #fff;
        font-weight: 600;
        white-space: nowrap;
        font-size: 22px;
        margin-top: 8pt;
        border-radius: 12px;
        /*border: 2px solid #fff;*/

        span {
          font-size: 13px;
        }
      }

      .sport-name {
        margin-bottom: 8pt;
        font-size: 18pt;
      }

      .sport-back {
        font-weight: 600;
        font-size: 20pt;
      }
    }
  }

  .score-con {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    background: #fff;
    border-radius: 12px;
    margin: 8pt;
    padding-top: 8pt;
    margin-top: 0;
    padding-bottom: 12pt;
  }

  .score-row {
    position: relative;
    padding: 0 24pt;
    margin-bottom: 0pt;

    .row-title {
      display: flex;
      flex-direction: row;
      text-align: center;
      align-items: center;
      justify-content: center;

      .title-left {

      }

      .title-center {
        font-size: 10pt;
        font-weight: 600;
        text-align: center;
      }

      .title-right {

      }
    }

    .row-sport {
      display: flex;
      flex-direction: row;
      align-items: center;

      .row-left, .row-right {
        width: 0;
        flex: 1;
        padding-top: 12pt;
        position: relative;

        &.blue {
          ::v-deep .el-slider__button {
            border-color: #409EFF;
          }

          .zw-tip {
            background: #409EFF;

          }
        }

        &.red {
          ::v-deep .el-slider__button {
            border-color: #F56C6C;
          }

          .zw-tip {
            background: #F56C6C;

          }
        }
      }

      .row-left {

        .zw-tip {
          background: #409EFF;
          left: calc(50% - 24px);
        }

        ::v-deep .el-slider__bar {
          height: 12px;
          background: none;

          /*border-radius: 6px;*/
        }

        ::v-deep .el-slider__button {
          width: 32px;
          height: 32px;
          /*border-color: #409EFF;*/
        }

        ::v-deep .el-slider__runway {
          /*background-color: #409EFF;*/

          background: linear-gradient(to right, #409EFF 50%, #F56C6C 50%);
        }
      }

      .row-right {
        .zw-tip {
          background: #F56C6C;
          left: calc(50% - 16px);
        }

        ::v-deep .el-slider__bar {
          height: 12px;
          background: #F56C6C;
          border-radius: 6px;
        }

        ::v-deep .el-slider__runway {
          background-color: #F56C6C;
        }

        ::v-deep .el-slider__button {
          width: 32px;
          height: 32px;
          border-color: #F56C6C;
        }
      }


    }

    .demonstration {
      font-size: 12px;
    }

    .zw-tip {
      width: 48px;
      height: 16px;
      line-height: 16px;
      border-radius: 8px;
      text-align: center;
      background: #67C23A;
      color: #fff;
      font-size: 9pt;
      font-weight: 600;
      position: absolute;
      right: 0;
      top: 3pt;
    }
  }


  ::v-deep .el-slider__runway {
    height: 12px;
    border-radius: 6px;
    margin: 12px 0;
    margin-top: 6px;
  }


  ::v-deep .el-slider__button-wrapper {
    top: -12px;
  }

  .btn-con {
    display: flex;
    flex-direction: row;
    justify-content: center;
    text-align: center;

    .save-btn {
      width: 160px;
      text-align: center;
      /*height: 48px;*/
      background: #E6A23C;
      padding: 8pt 16pt;
      color: #fff;
      border-radius: 24px;
    }
  }

</style>
