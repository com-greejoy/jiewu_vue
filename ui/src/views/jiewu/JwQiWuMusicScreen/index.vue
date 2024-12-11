<template>
  <div class="app-container">
    <ELSelectMatch v-if="showMatchSelect" :matchId.sync="matchId"/>
    <video :class="isMp4 ? 'top' : ''" autoplay ref="videoElement" v-if="musicOpen">
      <source :src="musicUrl" type="video/mp4">
    </video>
    <el-image class="img" :src="match.mainImg" fit="fill" v-if="match.mainImg"></el-image>

    <Battle v-if="showBattle" :gameItemId.sync="battle.gameItemId" :currentGameItem.sync="battle.currentGameItem" :matchId.sync="matchId"></Battle>

    <HaiXuanGrade v-if="showHaiXuanGrade" :gameItemId.sync="battle.gameItemId" :currentGameItem.sync="battle.currentGameItem" :matchId.sync="matchId"></HaiXuanGrade>
    <JinJiSport v-if="showjinJiSport" :gameItemId.sync="battle.gameItemId" :currentGameItem.sync="battle.currentGameItem" :matchId.sync="matchId"></JinJiSport>

  </div>
</template>

<script>
  import {getJwMatch} from "@/api/jiewu/jwMatch";
  import {getJwGameItem} from "@/api/jiewu/JwGameItem";
  import Battle from '@/views/jiewu/JwQiWuMusicScreen/battle';
  import HaiXuanGrade from '@/views/jiewu/JwQiWuMusicScreen/haiXuanGrade';
  import JinJiSport from '@/views/jiewu/JwQiWuMusicScreen/jinjiSport';

  export default {
    name: "JwQiWuMusicScreen",
    components: {Battle, HaiXuanGrade, JinJiSport},
    data() {
      return {
        battle: {},
        showBattle: false,
        showHaiXuanGrade: false,
        showjinJiSport: false,
        ws: null,
        timeout: 2000,
        interval: 10000,
        reconnect: true,
        musicUrl: null,
        isMp4: false,
        musicOpen: false,
        matchId: (this.Cookies.get("matchId") * 1) || null,
        showMatchSelect: false,
        match: {},
      };
    },
    destroyed() {

    },
    beforeDestroy() {

    },
    watch: {
      "matchId": function (val) {
        if (this.Cookies.get("matchId") * 1) {
          this.showMatchSelect = false;
          this.getMatchInfo();
          this.initSocket();
        } else {
          this.showMatchSelect = true;
        }
      },
    },
    created() {
      if (!this.matchId) {
        this.showMatchSelect = true;
      } else {
        this.getMatchInfo()
      }
      this.initSocket();
      this.startHeartbeat();
    },
    methods: {
      getMatchInfo() {
        getJwMatch(this.matchId).then(response => {
          this.match = response.data || {};
          if (this.match.mainImg) this.match.mainImg = process.env.VUE_APP_BASE_URL + this.match.mainImg;
          if (this.match.battleImg) this.match.battleImg = process.env.VUE_APP_BASE_URL + this.match.battleImg;
        });
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
        if (!this.matchId) {
          return;
        }
        let ws = new WebSocket(`${process.env.VUE_APP_BASE_URL}/websocket/socket/playMusicEr-${this.matchId}`);
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

            that.musicOpen = false;
            that.showBattle = false;
            that.showHaiXuanGrade = false;
            that.showjinJiSport = false;

            if (data.type == "sendMusic" && data.worksMusic) {
              // 播放音乐视频
              setTimeout(() => {
                that.musicUrl = process.env.VUE_APP_BASE_URL + data.worksMusic;

                if (that.musicUrl && that.musicUrl.indexOf(".mp4") >= 0) {
                  that.isMp4 = true;
                } else {
                  that.isMp4 = false;
                }
                that.musicOpen = true;
                setTimeout(() => {
                  that.$refs.videoElement.play();
                }, 100)
              }, 100)
            } else if (data.type == "sendBattle" && data.gameItemId) {
              // 投屏对阵图
              setTimeout(() => {
                getJwGameItem(data.gameItemId).then(res => {
                  that.battle = {gameItemId: data.gameItemId, currentGameItem: res.data || data};
                  that.showBattle = true;
                })
              }, 100)
            } else if (data.type == "sendHaiXuanGrade" && data.gameItemId) {
              // 投屏海选成绩
              setTimeout(() => {
                getJwGameItem(data.gameItemId).then(res => {
                  that.battle = {gameItemId: data.gameItemId, currentGameItem: res.data || data};
                  that.showHaiXuanGrade = true;
                })
              }, 100)
            } else if (data.type == "sendJinJiSport" && data.gameItemId) {
              // 投屏晋级名单
              setTimeout(() => {
                getJwGameItem(data.gameItemId).then(res => {
                  that.battle = {gameItemId: data.gameItemId, currentGameItem: res.data || data};
                  that.showjinJiSport = true;
                })
              }, 100)
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
    },
  };
</script>

<style scoped lang="scss">
  html {
    overflow: hidden;
  }

  .app-container {
    padding: 0;
    width: 100%;
    height: 99.6%;

    video {
      width: 100%;
      height: 100%;
      object-fit: fill;
      z-index: 1;
      position: fixed;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;

      &.top {
        z-index: 3;
      }
    }

    .img {
      width: 100%;
      height: 100%;
      z-index: 2;
      position: fixed;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
    }
  }

  .battle-con, .hai-con {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
  }

</style>
