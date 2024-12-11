<template>
  <div class="battle-con" :style="{ backgroundImage: isPhone ? '' : 'url(' + background + ')' }">
    <div class="eight-box" style="display: none;" @click="cancelShow">
      <div class="game-item-name">{{currentGameItem.name}}</div>
      <div class="title-desc">决赛对阵</div>
      <div class="eight-con" :class="{four: currentGameItem.promotionNum == 4, eight: currentGameItem.promotionNum == 8, sixteen: currentGameItem.promotionNum == 16, thirtyTwo: currentGameItem.promotionNum == 32}">
        <div class="order-row" data-p="32" v-if="currentGameItem.promotionNum == 32">
          <div class="order-item player-name" data-position="32" data-pk="16.1" data-t="1" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 1)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.1" data-t="32" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 32)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.9" data-t="9" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 9)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.9" data-t="28" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 28)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.5" data-t="5" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 5)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.5" data-t="24" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 24)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.12" data-t="12" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 12)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.12" data-t="20" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 20)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.13" data-t="13" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 13)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.13" data-t="17" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 17)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.8" data-t="8" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 8)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.8" data-t="21" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 21)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.16" data-t="16" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 16)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.16" data-t="25" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 25)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.3" data-t="3" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 3)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.3" data-t="30" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 30)}}</div>
        </div>
        <div class="line-row" data-p="32" v-if="currentGameItem.promotionNum == 32" style="height: 800px;">
          <div class="line-item" data-p="4.1">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.4">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.1">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.4">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.1">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.1">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.4">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.1">
            <div class="line"></div>
          </div>
        </div>
        <div class="order-row" data-p='16' v-if="currentGameItem.promotionNum >= 16">
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.1" data-t="1" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 1)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item vs">vs</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.1" data-t="9" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 9)}}</div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->

          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.5" data-t="5" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 5)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item vs">vs</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.5" data-t="13" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 13)}}</div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->

          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.3" data-t="3" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 3)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item vs">vs</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.3" data-t="11" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 11)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item"></div>

          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.7" data-t="7" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 7)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item vs">vs</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.7" data-t="15" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 15)}}</div>
          <!--<div class="order-item"></div>-->
        </div>
        <div class="line-row" data-p="16" v-if="currentGameItem.promotionNum >= 16">
          <div class="line-item" data-p="4.1">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.3">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.2">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.4">
            <div class="line"></div>
          </div>
        </div>
        <div class="order-row" data-p='8' v-if="currentGameItem.promotionNum >= 8">
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="8" data-pk="4.1" data-t="1" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(8, 1)}}</div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item"></div>
          <div class="order-item vs">vs</div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="8" data-pk="4.1" data-t="5" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(8, 5)}}</div>
          <div class="order-item"></div>

          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="8" data-pk="4.3" data-t="3" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(8, 3)}}</div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item vs">vs</div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="8" data-pk="4.3" data-t="7" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(8, 7)}}</div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->

        </div>
        <div class="line-row" data-p='8' v-if="currentGameItem.promotionNum >= 8" style="height: 650px;">
          <div class="line-item" style="height: 220px;" data-p="4.1">
            <div class="line"></div>
          </div>
          <div class="line-item" style="height: 220px;" data-p="4.3">
            <div class="line"></div>
          </div>
        </div>
        <div class="order-row">
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="4" data-d="3" data-pk="2.1" data-t="1" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(4, 1)}}</div>
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item vs">vs</div>
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="4" data-d="1" data-pk="2.1" data-t="3" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(4, 3)}}</div>
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item"></div>
        </div>
        <div class="line-row" data-p="2.1">
          <div class="line-item" data-p="2.1">
            <div class="line"></div>
          </div>
        </div>
        <div class="order-row">
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="2" data-pk="1.1" data-t="1" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(2, 1)}}</div>
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="3" data-pk="3.0" data-t="1" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(3, 1)}}</div>
          <div class="order-item"></div>
        </div>
        <div class="order-row">
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item guanjun-item win" data-position>
            冠军
            <div class="player-name" data-position="1" data-t="1" @contextmenu.prevent="onRightClick">{{getSportName(1, 1)}}</div>
          </div>
          <div class="order-item guanjun" data-position>
            <div class="guanjun-box">
              <div class="guanjun-line" style="transform: skew(30deg);margin-left: -25px;"></div>
            </div>
          </div>
          <div class="order-item"></div>
          <div class="order-item guanjun" data-position>
            <div class="guanjun-box bottom">
              <div class="guanjun-line" style="height: 32px; margin-left: 16px; margin-bottom: 30px;transform: skew(-30deg);"></div>
            </div>
          </div>
          <div class="order-item player-name" data-position="3" data-t="0" @contextmenu.prevent="onRightClick">{{getSportName(3, 0)}}</div>
        </div>
        <div class="order-row">
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="2" data-pk="1.1" data-t="2" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(2, 2)}}</div>
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="3" data-pk="3.0" data-t="2" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(3, 2)}}</div>
          <div class="order-item"></div>
        </div>
        <div class="line-row right-line" data-p="2.1">
          <div class="line-item" data-p="2.1">
            <div class="line"></div>
          </div>
        </div>
        <div class="order-row">
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="4" data-d="4" data-pk="2.2" data-t="2" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(4, 2)}}</div>
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item vs">vs</div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item"></div>

          <div class="order-item player-name" data-position="4" data-d="2" data-pk="2.2" data-t="4" @click.stop="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(4, 4)}}</div>
          <div class="order-item"></div>
          <div class="order-item"></div>
          <div class="order-item"></div>
        </div>

        <div class="line-row right-line" data-p='8' style="height: 650px;" v-if="currentGameItem.promotionNum >= 8">
          <div class="line-item" style="height: 220px;" data-p="4.1">
            <div class="line"></div>
          </div>
          <div class="line-item" style="height: 220px;" data-p="4.3">
            <div class="line"></div>
          </div>
        </div>
        <div class="order-row" data-p='8' v-if="currentGameItem.promotionNum >= 8">
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="8" data-pk="4.2" data-t="2" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(8, 2)}}</div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item vs">vs</div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="8" data-pk="4.2" data-t="6" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(8, 6)}}</div>

          <div class="order-item"></div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="8" data-pk="4.4" data-t="4" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(8, 4)}}</div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item vs">vs</div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
          <div class="order-item"></div>
          <div class="order-item player-name" data-position="8" data-pk="4.4" data-t="8" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(8, 8)}}</div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->
          <!--<div class="order-item"></div>-->
        </div>

        <div class="line-row right-line" data-p="16" v-if="currentGameItem.promotionNum >= 16">
          <div class="line-item" data-p="4.1">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.3">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.2">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.4">
            <div class="line"></div>
          </div>
        </div>
        <div class="order-row" data-p='16' v-if="currentGameItem.promotionNum >= 16">
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.2" data-t="2" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 2)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item vs">vs</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.2" data-t="10" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 10)}}</div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->

          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.6" data-t="6" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 6)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item vs">vs</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.6" data-t="14" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 14)}}</div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->

          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.4" data-t="4" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 4)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item vs">vs</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.4" data-t="12" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 12)}}</div>
          <div class="order-item"></div>
          <!--<div class="order-item"></div>-->

          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.8" data-t="8" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 8)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item vs">vs</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="16" data-pk="8.8" data-t="16" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(16, 16)}}</div>
          <!--<div class="order-item"></div>-->
        </div>

        <div class="line-row right-line" data-p="32" style="height: 800px;" v-if="currentGameItem.promotionNum >= 32">
          <div class="line-item" data-p="4.1">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.4">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.1">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.4">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.1">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.1">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.4">
            <div class="line"></div>
          </div>
          <div class="line-item" data-p="4.1">
            <div class="line"></div>
          </div>
        </div>
        <div class="order-row" data-p="32" v-if="currentGameItem.promotionNum >= 32">
          <div class="order-item player-name" data-position="32" data-pk="16.2" data-t="2" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 2)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.2" data-t="31" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 31)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.10" data-t="10" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 10)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.10" data-t="27" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 27)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.6" data-t="6" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 6)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.6" data-t="23" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 23)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.11" data-t="11" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 11)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.11" data-t="19" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 19)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.14" data-t="14" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 14)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.14" data-t="18" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 18)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.7" data-t="7" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 7)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.7" data-t="22" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 22)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.15" data-t="15" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 15)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.15" data-t="26" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 26)}}</div>
          <!--<div class="order-item"></div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.4" data-t="4" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 4)}}</div>
          <!--<div class="order-item vs">vs</div>-->
          <div class="order-item player-name" data-position="32" data-pk="16.4" data-t="29" @click="sportClick" @contextmenu.prevent="onRightClick">{{getSportName(32, 29)}}</div>
        </div>
      </div>
    </div>
    <div class="battle-p" :class="{two: p3.playerName && p4.playerName}" v-if="!isPhone && ((p1.playerName && p2.playerName) || (p3.playerName && p4.playerName))">
      <div class="current-battle" v-if="!isPhone && p1.playerName && p2.playerName">
        <div class="changdi-label">
          <div class="lab-text">A 场地</div>
        </div>
        <div class="sport-con">
          <div class="sport-left">
            <div class="sport-info">
              <div class="sport-name">{{p1.playerName}}</div>
              <div class="sport-back">{{p1.backNumber}}</div>
              <div class="sport-score">{{p1Score}}</div>
            </div>
          </div>
          <div class="PK">VS</div>
          <div class="sport-right">
            <div class="sport-info">
              <div class="sport-name">{{p2.playerName}}</div>
              <div class="sport-back">{{p2.backNumber}}</div>
              <div class="sport-score le">{{p2Score}}</div>
            </div>
          </div>
        </div>
        <div class="score-con">
          <div class="score-row header-row">
            <div class="score-label"></div>
            <div class="score-judges">
              <div class="score-judge" v-for="item in judgeNum1">
                {{item}}
              </div>
            </div>
          </div>
          <div class="score-row">
            <div class="score-label">第一轮</div>
            <div class="score-judges">
              <div class="score-judge" v-if="scoreLun1 && scoreLun1.length > 0" v-for="item in scoreLun1" :class="{blue: item.playerId == p1.playerId, red: item.playerId == p2.playerId}"></div>
            </div>
          </div>
          <!--<div class="score-row" v-if="p1.currentPk == '1.1'">-->
          <div class="score-row" v-if="scoreLun2 && scoreLun2.length > 0">
            <div class="score-label">第二轮</div>
            <div class="score-judges">
              <div class="score-judge" v-for="item in scoreLun2" :class="{blue: item.playerId == p1.playerId, red: item.playerId == p2.playerId}"></div>
            </div>
          </div>
          <div class="score-row" v-if="scoreLun3 && scoreLun3.length > 0">
            <div class="score-label">第三轮</div>
            <div class="score-judges">
              <div class="score-judge"  v-for="item in scoreLun3" :class="{blue: item.playerId == p1.playerId, red: item.playerId == p2.playerId}"></div>
            </div>
          </div>
          <!--<div class="score-row" v-if="((pkScoreList1 && pkScoreList1.length > 0) && (p1.currentPk == '1.1') && ((p1Score == p2Score) || (p1Score + p2Score > 2)))">-->
            <!--<div class="score-label">第三轮</div>-->
            <!--<div class="score-judges">-->
              <!--<div class="score-judge" v-if="scoreLun3 && scoreLun3.length > 0" v-for="item in scoreLun3" :class="{blue: item.playerId == p1.playerId, red: item.playerId == p2.playerId}"></div>-->
            <!--</div>-->
          <!--</div>-->
        </div>
      </div>

      <div class="current-battle" v-if="!isPhone && p3.playerName && p4.playerName">
        <div class="changdi-label">
          <div class="lab-text">B 场地</div>
        </div>
        <div class="sport-con">
          <div class="sport-left">
            <div class="sport-info">
              <div class="sport-name">{{p3.playerName}}</div>
              <div class="sport-back">{{p3.backNumber}}</div>
              <div class="sport-score">{{p3Score}}</div>
            </div>
          </div>
          <div class="PK">VS</div>
          <div class="sport-right">
            <div class="sport-info">
              <div class="sport-name">{{p4.playerName}}</div>
              <div class="sport-back">{{p4.backNumber}}</div>
              <div class="sport-score le">{{p4Score}}</div>
            </div>
          </div>
        </div>
        <div class="score-con">
          <div class="score-row header-row">
            <div class="score-label"></div>
            <div class="score-judges">
              <div class="score-judge" v-for="item in judgeNum2">
                JUDGE {{item}}
              </div>
            </div>
          </div>
          <div class="score-row">
            <div class="score-label">第一轮</div>
            <div class="score-judges">
              <div class="score-judge" v-if="item.lun == 1" v-for="item in pkScoreList2" :class="{blue: item.playerId == p3.playerId, red: item.playerId == p4.playerId}">{{item.judgeName}}</div>
            </div>
          </div>
          <!--<div class="score-row" >-->
          <!--<div class="score-label">第二轮</div>-->
          <!--<div class="score-judges">-->
          <!--<div class="score-judge" v-if="item.lun == 2" v-for="item in pkScoreList2" :class="{blue: item.playerId == p3.playerId, red: item.playerId == p4.playerId}"></div>-->
          <!--</div>-->
          <!--</div>-->
          <!--<div class="score-row" v-if="p3Score == 1 && p4Score == 1">-->
          <!--<div class="score-label">第三轮</div>-->
          <!--<div class="score-judges">-->
          <!--<div class="score-judge" v-if="item.lun == 3" v-for="item in pkScoreList2" :class="{blue: item.playerId == p3.playerId, red: item.playerId == p4.playerId}"></div>-->
          <!--</div>-->
          <!--</div>-->
        </div>
      </div>
    </div>
    <div class="contextmenu" :style="showMPos" v-if="showM" @click="savePro">晋级: ({{proPlayer.sport.playerName}})</div>
    <div class="contextmenu" :style="showMPos" v-if="showMC" @click="cancelPro">清除</div>
  </div>
</template>

<script>
  import {listJwEight, saveEightPro} from "@/api/jiewu/jwEight";
  import {getJwMatch} from "@/api/jiewu/jwMatch";
  import {getPkScores} from "@/api/jiewu/JwAppScore";

  export default {
    name: 'battle',
    props: {
      isPhone: {
        type: Boolean,
        default: false
      },
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
        this.getMatchInfo()
      },
      "gameItemId": function (val) {
        this.getList();
      },
    },
    data() {
      return {
        currentPk: "",
        p1Score: 0,
        p2Score: 0,
        p3Score: 0,
        p4Score: 0,
        p1: {},
        p2: {},
        p3: {},
        p4: {},
        ws: null,
        timeout: 2000,
        interval: 10000,
        reconnect: true,
        showMC: false,
        showMPos: {},
        showM: false,
        match: {},
        background: "",
        proPlayer: {},
        jwEightList: [],
        judgeNum1: 3,
        judgeNum2: 3,
        pkScoreList1: [],
        pkScoreList2: [],
        scoreLun1: [],
        scoreLun2: [],
        scoreLun3: [],
      };
    },
    computed: {},
    created() {
      this.getList();
      setInterval(this.getList, 2000);
      this.getMatchInfo()
      if (!this.isPhone) {
        this.initSocket();
        this.startHeartbeat();
      }
    },
    methods: {
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
        let ws = new WebSocket(`${process.env.VUE_APP_BASE_URL}/websocket/socket/juesai-99999`);
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

              if (currentPkGroup.currentPk == "null") {
                that.p1 = {};
                that.p2 = {};
                that.p3 = {};
                that.p4 = {};
              } else {
                let area = currentPkGroup.area;
                let eightList = data.list || [];
                // if (this.currentPk != currentPkGroup.currentPk) {
                that.currentPk = currentPkGroup.currentPk;
                if (area == "全") {
                  // 不分场地， 只有一个
                  let p1 = eightList.find(item => item.playerPosition + "." + item.playerIndex == currentPkGroup.pk1);
                  let p2 = eightList.find(item => item.playerPosition + "." + item.playerIndex == currentPkGroup.pk2);
                  p1.playerName = (p1 && p1.jwSignRecordSportList) ? (p1.jwSignRecordSportList.map(item => item.playerName).join(" ")) : " ";
                  p2.playerName = (p2 && p2.jwSignRecordSportList) ? (p2.jwSignRecordSportList.map(item => item.playerName).join(" ")) : " ";

                  p1.currentPk = this.currentPk;
                  p2.currentPk = this.currentPk;
                  that.p1 = p1;
                  that.p2 = p2;
                  that.p3 = {};
                  that.p4 = {};
                } else {
                  if (area == "A") {
                    let p1 = eightList.find(item => item.playerPosition + "." + item.playerIndex == currentPkGroup.pk1);
                    let p2 = eightList.find(item => item.playerPosition + "." + item.playerIndex == currentPkGroup.pk2);
                    p1.playerName = (p1 && p1.jwSignRecordSportList) ? (p1.jwSignRecordSportList.map(item => item.playerName).join(" ")) : " ";
                    p2.playerName = (p2 && p2.jwSignRecordSportList) ? (p2.jwSignRecordSportList.map(item => item.playerName).join(" ")) : " ";
                    p1.currentPk = this.currentPk;
                    p2.currentPk = this.currentPk;
                    that.p1 = p1;
                    that.p2 = p2;
                  } else {
                    let p3 = eightList.find(item => item.playerPosition + "." + item.playerIndex == currentPkGroup.pk1);
                    let p4 = eightList.find(item => item.playerPosition + "." + item.playerIndex == currentPkGroup.pk2);
                    p3.playerName = (p3 && p3.jwSignRecordSportList) ? (p3.jwSignRecordSportList.map(item => item.playerName).join(" ")) : " ";
                    p4.playerName = (p4 && p4.jwSignRecordSportList) ? (p4.jwSignRecordSportList.map(item => item.playerName).join(" ")) : " ";
                    p3.currentPk = this.currentPk;
                    p4.currentPk = this.currentPk;
                    that.p3 = p3;
                    that.p4 = p4;
                  }
                }
                // }
              }
              that.getList();
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
      },
      getPkScores() {
        getPkScores({gameItemId: this.gameItemId, currentPkGroup: ""}).then(res => {

          let pkScoreList1 = (res.data || []).filter(item => ((item.playerPkGroup == this.p1.currentPk || item.playerPkGroup == this.p2.currentPk) && (item.playerId == this.p1.playerId || item.playerId == this.p2.playerId)));
          pkScoreList1.sort((a, b) => a.judgeId - b.judgeId);
          this.pkScoreList1 = pkScoreList1 || [];
          let p1Score = 0;
          let p2Score = 0;
          let p3Score = 0;
          let p4Score = 0;
          let judgeNum1 = 3;
          let judgeNum2 = 3;
          let scoreLun1 = [];
          let scoreLun2 = [];
          let scoreLun3 = [];

          // 所有裁判名字
          let judeNames = [...new Set((pkScoreList1 || []).map(({judgeName}) => judgeName))];

          // 计算轮次分数
          // A场地
          for (let lun = 1; lun <= 3; lun++) {
            if (((pkScoreList1 || []).filter(item => (item.lun == lun)).length) > 0) {
              let pk1ScoreList1 = (pkScoreList1 || []).filter(item => (item.lun == lun && item.playerPkGroup == this.p1.currentPk && item.playerId == this.p1.playerId));
              let pk1ScoreList2 = (pkScoreList1 || []).filter(item => (item.lun == lun && item.playerPkGroup == this.p2.currentPk && item.playerId == this.p2.playerId));
              if (pk1ScoreList1.length > pk1ScoreList2.length) {
                p1Score++;
              } else if (pk1ScoreList1.length < pk1ScoreList2.length) {
                p2Score++;
              } else {
                // p1Score++;
                // p2Score++;
              }
              let aa = pk1ScoreList1.concat(pk1ScoreList2);

              for (let nam = 0; nam < judeNames.length; nam++  ) {

                let score = aa[aa.findIndex(ju => ju.judgeName == judeNames[nam])];

                if (lun == 1) {
                  if (score) {
                    scoreLun1.push(score)
                  } else {
                    scoreLun1.push({})
                  }
                }
                if (lun == 2) {
                  if (score) {
                    scoreLun2.push(score)
                  } else {
                    scoreLun2.push({})
                  }
                }
                if (lun == 3) {
                  if (score) {
                    scoreLun3.push(score)
                  } else {
                    scoreLun3.push({})
                  }
                }
              }

              // judgeNum1 = judgeNum1 < pk1ScoreList1.length + pk1ScoreList2.length ? pk1ScoreList1.length + pk1ScoreList2.length : judgeNum1;
            }
          }
          this.judgeNum1 = judeNames;


          this.scoreLun1 = scoreLun1 || [];
          this.scoreLun2 = scoreLun2 || [];
          this.scoreLun3 = scoreLun3 || [];

          let pkScoreList2 = (res.data || []).filter(item => ((item.playerPkGroup == this.p3.currentPk || item.playerPkGroup == this.p4.currentPk) && (item.playerId == this.p3.playerId || item.playerId == this.p4.playerId)));
          this.judgeNum2 = pkScoreList2.length >= 3 ? pkScoreList2.length : 3;
          this.pkScoreList2 = pkScoreList2 || [];
          // B场地
          for (let lun = 1; lun <= 3; lun++) {
            if (((pkScoreList2 || []).filter(item => (item.lun == lun)).length) > 0) {
              console.log(pkScoreList2)
              let pk1ScoreList1 = (pkScoreList2 || []).filter(item => (item.lun == lun && item.playerPkGroup == this.p3.currentPk && item.playerId == this.p3.playerId));
              let pk1ScoreList2 = (pkScoreList2 || []).filter(item => (item.lun == lun && item.playerPkGroup == this.p4.currentPk && item.playerId == this.p4.playerId));
              if (pk1ScoreList1.length > pk1ScoreList2.length) {
                p3Score++;
              } else if (pk1ScoreList1.length < pk1ScoreList2.length) {
                p4Score++;
              } else {
                // p3Score++;
                // p4Score++;
              }
              judgeNum2 = judgeNum2 < pk1ScoreList1.length + pk1ScoreList2.length ? pk1ScoreList1.length + pk1ScoreList2.length : judgeNum2;
            }
          }
          this.judgeNum2 = judgeNum2;
          this.p1Score = p1Score;
          this.p2Score = p2Score;
          this.p3Score = p3Score;
          this.p4Score = p4Score;
        })
      },
      getMatchInfo() {
        getJwMatch(this.matchId).then(response => {
          // console.log(response)
          this.match = response.data || {};
          this.match.mainImg = process.env.VUE_APP_BASE_URL + this.match.mainImg;
          this.background = process.env.VUE_APP_BASE_URL + this.match.battleImg;
        });
      },
      cancelPro(e) {
        let that = this;
        let nerPro = {gameItemId: this.gameItemId, playerId: null, playerIndex: this.proPlayer.playerIndex, playerPosition: this.proPlayer.playerPosition};
        saveEightPro(nerPro).then(res => {
          that.getList();
          that.showMC = false;
          that.proPlayer = {};
        })
      },
      onRightClick(e) {
        let p = e.target.getAttribute("data-position");
        if (p * 1 < this.currentGameItem.promotionNum) {
          let t = e.target.getAttribute("data-t");
          let sport = this.jwEightList.find(item => item.playerPosition == p && item.playerIndex == t);
          sport.playerName = sport.jwSignRecordSportList.map(item => item.playerName).join(" ");
          this.proPlayer = {playerPosition: p, playerIndex: t}

          let mouseX = e.pageX;
          let mouseY = e.pageY;
          let menuLeft = mouseX + -80 + "px";
          let menuTop = mouseY - 60 + "px";
          this.showM = false;
          this.showMC = true;
          this.showMPos = {
            "left": menuLeft,
            "top": menuTop
          };
        } else {
          this.showM = false;
          this.showMC = false;
        }
      },
      cancelShow() {
        this.showM = false;
        this.showMC = false;
      },
      savePro() {
        let that = this;
        let nerPro = {gameItemId: this.gameItemId, playerId: this.proPlayer.playerId, playerIndex: this.proPlayer.playerIndex, playerPosition: this.proPlayer.playerPosition};
        if (this.proPlayer && (this.proPlayer.playerPosition * 1) == 2) {

          let sport = this.jwEightList.find(item => item.playerPosition == 4 && item.playerIndex == this.proPlayer.d * 1);
          let nerProT = {gameItemId: this.gameItemId, playerId: sport.playerId, playerIndex: this.proPlayer.playerIndex, playerPosition: "3"};

          saveEightPro(nerProT).then(res => {
            that.getList();
            that.showM = false;
            that.proPlayer = {};
          })
        }
        saveEightPro(nerPro).then(res => {
          that.getList();
          that.showM = false;
          that.proPlayer = {};
        })
      },
      sportClick(e) {
        e.stopPropagation();
        let dp = e.target.getAttribute("data-pk");
        let d = e.target.getAttribute("data-d");
        let p = e.target.getAttribute("data-position");
        let t = e.target.getAttribute("data-t");
        let sport = this.jwEightList.find(item => item.playerPosition == p && item.playerIndex == t);
        sport.playerName = sport.jwSignRecordSportList.map(item => item.playerName).join(" ");
        this.proPlayer = {p: p, t: t, d: d, sport: sport, playerGroup: this.gameItemId, playerId: sport.playerId, playerPosition: dp.split(".")[0], playerIndex: dp.split(".")[1]}

        let mouseX = e.pageX;
        let mouseY = e.pageY;
        let menuLeft = mouseX + -80 + "px";
        let menuTop = mouseY - 60 + "px";
        this.showMC = false;
        this.showM = true;
        this.showMPos = {
          "left": menuLeft,
          "top": menuTop
        };
      },
      getList() {
        if (this.gameItemId) {
          listJwEight({gameItemId: this.gameItemId}).then(response => {
            this.jwEightList = response.rows;
            this.getPkScores()
          });
        } else {
          this.jwEightList = [];
        }
      },
      getSportName(positoin, index) {
        // return index;
        let sport = this.jwEightList.find(item => item.playerPosition == positoin && item.playerIndex == index);
        return (sport && sport.jwSignRecordSportList) ? (sport.jwSignRecordSportList.map(item => item.playerName).join(" ")) : " ";
      }
    }
  }
</script>

<style scoped lang="scss">
  .battle-p {
    display: flex;
    flex-direction: row;
    align-items: center;
    position: fixed;
    left: 80pt;
    top: 80pt;
    bottom: 80pt;
    right: 80pt;
    background: rgba(151, 59, 42, .6);
    border-radius: 24pt;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);

    &.two {
      .current-battle {

        .changdi-label {
          display: flex;
          flex-direction: row;
          justify-content: center;
          align-items: center;
        }

        .score-con .score-row {
          .score-label {
            width: 120pt;
            min-width: 120pt;
            font-size: 24pt;
          }

          .score-judges {
            .score-judge {
              white-space: nowrap;
              font-size: 18pt;
              width: 66pt;
            }
          }
        }
      }

    }

    .current-battle {
      width: 0;
      flex: 1;
      color: #1d2939;

      .changdi-label {
        display: none;
        text-align: center;
        font-size: 64pt;
        color: #fff;
      }

      .sport-con {
        flex: 1;
        display: flex;
        flex-direction: row;
        align-items: center;
        padding: 56pt 56pt;
        margin: 56pt;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
        background: #fff;
        border-radius: 12px;

        .PK {
          margin: 0 64pt;
          font-size: 120px;
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
          position: relative;
          /*width: 120px;*/
          display: flex;
          flex-direction: column;
          padding: 24px 8px;
          border-radius: 16px;
          box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
          color: #fff;
          font-weight: 600;

          .sport-name {
            margin-bottom: 8px;
            font-size: 48pt;
          }

          .sport-back {
            font-weight: 600;
            font-size: 56pt;
          }

          .sport-score {
            position: absolute;
            right: -36pt;
            top: -36pt;
            background: #ffad00;
            color: #fff;
            width: 72pt;
            height: 72pt;
            border-radius: 36pt;
            font-size: 48pt;
            line-height: 72pt;
            text-align: center;

            &.le {
              left: -36pt;
            }
          }
        }
      }

      .score-con {
        display: flex;
        flex-direction: column;
        padding: 56pt 56pt;
        margin: 56pt 56pt;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
        background: #fff;
        border-radius: 12px;

        .score-row {
          display: flex;
          flex-direction: row;
          align-items: center;

          &:not(.header-row) {
            margin-bottom: 32pt;
          }

          .score-label {
            line-height: 56pt;
            width: 180pt;
            min-width: 180pt;
            font-weight: 600;
            font-size: 40pt;
            height: 56pt;
            margin-right: 32pt;
          }

          .score-judges {
            display: flex;
            flex-direction: row;

            .score-judge {
              text-align: center;
              width: 112pt;
              height: 72px;
              font-size: 36pt;
              border-radius: 32pt;
              margin-right: 44pt;
              white-space: nowrap;

              &.blue {
                background: #409EFF;
                color: #fff;
                line-height: 72px;
              }

              &.red {
                background: #F56C6C;
                color: #fff;
                line-height: 72px;
              }
            }
          }
        }


      }
    }

  }

  .battle-con {
    z-index: 9;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    display: flex;
    flex-direction: column;
    font-weight: 600;
  }

  .eight-box {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
  }

  .game-item-name {
    font-size: 64px;
    text-align: center;
    margin-top: 320px;
    letter-spacing: 4px;
    color: #fff;
  }

  .title-desc {
    font-size: 56px;
    text-align: center;
    margin-top: -16px;
    letter-spacing: 4px;
    color: #fff;
  }

  .eight-con {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    /*margin-top: 176px;*/
    /*transform: scale(0.7);*/
    width: 100%;
    margin-top: 40px;
    /*height: 100%;*/
    flex: 1;

    &.four {
      margin-top: -96px;
      transform: scale(2.2);
    }

    &.eight {
      margin-top: -96px;
      transform: scale(1.6);
    }

    &.sixteen {
      margin-top: -126px;
      transform: scale(1.4);
    }

    &.thirtyTwo {
      margin-top: -96px;
      transform: scale(1.3);
    }

    .order-row {
      display: flex;
      flex-direction: column;
      /*margin-left: 12px;*/
      /*margin-right: 12px;*/

      .order-item {
        width: 140px;
        height: 32px;
        line-height: 32px;
        display: flex;
        visibility: hidden;
        align-items: center;
        text-align: center;
        justify-content: center;
        border: 2px solid #fff;
        /*margin-right: 16px;*/
        margin-bottom: 22px;
        font-size: 28px;
        letter-spacing: 2px;
        color: #fff;
        border-radius: 4px;

        &:last-child {
          margin-bottom: 0;
        }

        i {
          width: 0;
          top: -41px;
          left: 26px;
          position: relative;

          &:before {
            font-size: 28px;
          }

        }

        &[data-position] {
          background: #333333;
          white-space: nowrap;
          border: none;
          border-radius: 0;
          transform: skew(-30deg);
          background: linear-gradient(to right bottom, #f50d0d, #111d42);

          span {
            transform: skew(30deg);
            font-size: 30px;
          }
        }
      }

      .order-item[data-position], .vs {
        visibility: visible;
        display: flex;
      }

      .vs {
        border: none;
        font-size: 28px;
        font-style: italic;
      }

      .win {
        color: #f8ac59;
        border: 2px solid #f8ac59;
      }
    }

    &[data-p="16"] {
      .vs {
        /*height: 40px;*/
        margin-bottom: 8px;
      }

      .order-item {
        margin-bottom: 8px;
        margin-top: 8px;

        &:empty {
          height: 32px;
        }
      }
    }

    &[data-p="32"] .order-item {
      width: 120px;
    }


    .line-row {
      display: flex;
      flex-direction: column;
      height: 684px;
      justify-content: space-between;

      &[data-p="16"] {
        height: 760px;

        .line-item {
          height: 112px;
        }
      }

      &[data-p="2.1"] {
        justify-content: center;
        height: 436px;
        /*margin-bottom: 16px;*/
      }

      .line-item {
        width: 50px;
        height: 50px;
        border-bottom: 4px solid #fff;
        border-top: 4px solid #fff;
        border-right: 4px solid #fff;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 49px;
        border-top-right-radius: 8px;
        border-bottom-right-radius: 8px;

        &[data-p="2.1"] {
          height: 100%;
        }
      }
    }

    &.t {
      .line-item {
        width: 20px;
        margin-right: 28px;
      }

      .line {
        margin-right: -50px;
        width: 30px;
      }
    }

    .line {
      margin-right: -100px;
      width: 50px;
      border-bottom: 4px solid #fff;
    }

    .right-line {

      transform: rotateY(180deg);
    }

    .bottom {
      transform: rotateX(180deg);
    }

    .guanjun-item {
      border: none;
      flex-direction: column;
      color: #FFF;
      background: none !important;

      div {
        width: 140px;
        height: 56px;
        min-height: 56px;
        font-size: 26px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        border: 2px solid #f50d0d;
        margin-bottom: 76px;
        /*border-radius: 8px;*/
        background: #f50d0d;

        &[data-position="2.2"] {
          margin-bottom: 26px;
        }
      }
    }

    .guanjun {
      border: none;
      display: flex;
      justify-content: center;
      align-items: center;
      background: none !important;
    }

    .guanjun-box {
      width: 100%;
      height: 2px;
      display: flex;
      justify-content: center;
      align-items: center;
      border-bottom: 4px solid #fff;

    }

    .guanjun-line {
      border: none;
      width: 2px;
      height: 40px;
      margin-bottom: 39px;
      border-left: 4px solid #fff;
    }

    [data-position="2.0"] {
      margin-top: 12px;
      margin-bottom: 0 !important;
    }
  }

  .contextmenu {

    position: fixed;
    width: 140px;
    height: 40px;
    line-height: 40px;
    /*padding: 0 24px;*/
    background: #FFFFFF;
    border-radius: 5px;
    font-size: 16px;
    overflow: hidden;
    z-index: 99;
    text-align: center;
    color: #000000;
    transition: ease 0.01s;
  }

  .contextmenu:hover {
    background: #f7a54a;
    color: #fff;
  }

</style>
