<template>
  <div class="battle-con-h">
    <el-form ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item style="margin-right: 88px" label="比赛" prop="matchId" v-if="!matchId">
        <ELSelectMatch :matchId.sync="matchId"/>
      </el-form-item>
      <el-form-item label="组别" prop="gameItemId" style="margin-bottom: 0;margin-right: 32px;">
        <el-select style="width: 360px" v-model="gameItemId" placeholder="组别" clearable @change="gameItemChange">
          <el-option
            v-for="gameItem in JwGameItemList"
            :key="gameItem.id"
            :label="gameItem.code + ' : ' + gameItem.name + (gameItem.matchType == '2' ?  ' (晋级' + gameItem.promotionNum + ')' : '')"
            :value="gameItem.id"
          />
        </el-select>
      </el-form-item>

      <el-button type="primary" plain size="mini" @click.stop="handleSendBattle()">投屏对阵</el-button>

      <el-button type="primary" plain size="mini" @click.stop="handleClearScreen()">显示主屏</el-button>

    </el-form>
    <div class="battle-con">
      <div class="ballte-header">
        <div class="fen">
          <el-switch
            v-model="isfen"
            active-text="2个场地"
            inactive-text="不分场地">
          </el-switch>
          <div style="width: 40px"></div>
          <el-switch
            v-model="isfenF"
            active-text="4个场地"
            inactive-text="不分场地">
          </el-switch>
          <div style="width: 40px"></div>
          <el-switch
            v-model="isTwoLun"
            active-text="打两轮"
            inactive-text="打一轮">
          </el-switch>
          <div class="qing" @click.stop="xianshidafen">显示打分</div>
          <div class="qing" @click.stop="xianshi3dafen">显示3打分</div>
          <div class="qing" @click.stop="qingPing">清屏</div>
        </div>
        <el-radio-group v-model="showNum">
          <el-radio label="32" size="mini" border v-if="currentGameItem.promotionNum >= 32">32 进 16</el-radio>
          <el-radio label="16" size="mini" border v-if="currentGameItem.promotionNum >= 16">16 进 8</el-radio>
          <el-radio label="8" size="mini" border v-if="currentGameItem.promotionNum >= 8">8 进 4</el-radio>
          <el-radio label="4" size="mini" border>半决赛</el-radio>
          <el-radio label="2" size="mini" border>决赛</el-radio>
        </el-radio-group>

      </div>
      <div class="battle-sports">

        <div class="battle-left battle-c" data-area="A" v-if="showNum == 32">
          <div class="battle-item" data-area-f="A" data-pk1="32.1" data-pk2="32.32" data-pk="16.1" :class="{select: currentPk == '16.1'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 1)}}</div>
              <div class="battle-sport">{{getSportName(32, 32)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 1, '16.1')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 32, '16.1')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="A" data-pk1="32.16" data-pk2="32.17" data-pk="16.16" :class="{select: currentPk == '16.16'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 16)}}</div>
              <div class="battle-sport">{{getSportName(32, 17)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 16, '16.16')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 17, '16.16')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="A" data-pk1="32.8" data-pk2="32.25" data-pk="16.8" :class="{select: currentPk == '16.8'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 8)}}</div>
              <div class="battle-sport">{{getSportName(32, 25)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 8, '16.8')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 25, '16.8')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="A" data-pk1="32.9" data-pk2="32.24" data-pk="16.9" :class="{select: currentPk == '16.9'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 9)}}</div>
              <div class="battle-sport">{{getSportName(32, 24)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 9, '16.9')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 24, '16.9')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="C" data-pk1="32.21" data-pk2="32.12" data-pk="16.12" :class="{select: currentPk == '16.12'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 21)}}</div>
              <div class="battle-sport">{{getSportName(32, 12)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 21, '16.12')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 12, '16.12')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="C" data-pk1="32.28" data-pk2="32.5" data-pk="16.5" :class="{select: currentPk == '16.5'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 28)}}</div>
              <div class="battle-sport">{{getSportName(32, 5)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 28, '16.5')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 5, '16.5')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="C" data-pk1="32.20" data-pk2="32.13" data-pk="16.13" :class="{select: currentPk == '16.13'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 20)}}</div>
              <div class="battle-sport">{{getSportName(32, 13)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 20, '16.13')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 13, '16.13')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="C" data-pk1="32.29" data-pk2="32.4" data-pk="16.4" :class="{select: currentPk == '16.4'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 29)}}</div>
              <div class="battle-sport">{{getSportName(32, 4)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 29, '16.4')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 4, '16.4')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
        </div>
        <div class="battle-right battle-c" data-area="B" v-if="showNum == 32">
          <div class="battle-item" data-area-f="B" data-pk1="32.3" data-pk2="32.30" data-pk="16.3" :class="{select: currentPk == '16.3'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 3)}}</div>
              <div class="battle-sport">{{getSportName(32, 30)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 3 , '16.3')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 30, '16.3')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="B" data-pk1="32.14" data-pk2="32.19" data-pk="16.14" :class="{select: currentPk == '16.14'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 14)}}</div>
              <div class="battle-sport">{{getSportName(32, 19)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 14, '16.14')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 19, '16.14')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="B" data-pk1="32.6" data-pk2="32.27" data-pk="16.6" :class="{select: currentPk == '16.6'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 6)}}</div>
              <div class="battle-sport">{{getSportName(32, 27)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 6, '16.6')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 27, '16.6')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="B" data-pk1="32.11" data-pk2="32.22" data-pk="16.11" :class="{select: currentPk == '16.11'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 11)}}</div>
              <div class="battle-sport">{{getSportName(32, 22)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 11, '16.11')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 22, '16.11')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="D" data-pk1="32.23" data-pk2="32.10" data-pk="16.10" :class="{select: currentPk == '16.10'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 23)}}</div>
              <div class="battle-sport">{{getSportName(32, 10)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 23, '16.10')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 10, '16.10')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="D" data-pk1="32.26" data-pk2="32.7" data-pk="16.7" :class="{select: currentPk == '16.7'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 26)}}</div>
              <div class="battle-sport">{{getSportName(32, 7)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 26, '16.7')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 7, '16.7')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="D" data-pk1="32.18" data-pk2="32.15" data-pk="16.15" :class="{select: currentPk == '16.15'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 18)}}</div>
              <div class="battle-sport">{{getSportName(32, 15)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 18, '16.15')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 15, '16.15')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="D" data-pk1="32.31" data-pk2="32.2" data-pk="16.2" :class="{select: currentPk == '16.2'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(32, 31)}}</div>
              <div class="battle-sport">{{getSportName(32, 2)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 31, '16.2')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(32, 2, '16.2')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
        </div>


        <div class="battle-left battle-c" data-area="A" v-if="showNum == 16">
          <div class="battle-item" data-area-f="A" data-pk1="16.1" data-pk2="16.16" data-pk="8.1" :class="{select: currentPk == '8.1'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(16, 1)}}</div>
              <div class="battle-sport">{{getSportName(16, 16)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 1, '8.1')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 16, '8.1')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="A" data-pk1="16.8" data-pk2="16.9" data-pk="8.8" :class="{select: currentPk == '8.8'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(16, 8)}}</div>
              <div class="battle-sport">{{getSportName(16, 9)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 8, '8.8')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 9, '8.8')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="C" data-pk1="16.12" data-pk2="16.5" data-pk="8.5" :class="{select: currentPk == '8.5'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(16, 12)}}</div>
              <div class="battle-sport">{{getSportName(16, 5)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 12, '8.5')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 5, '8.5')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="C" data-pk1="16.13" data-pk2="16.4" data-pk="8.4" :class="{select: currentPk == '8.4'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(16, 13)}}</div>
              <div class="battle-sport">{{getSportName(16, 4)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 13, '8.4')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 4, '8.4')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
        </div>
        <div class="battle-right battle-c" data-area="B" v-if="showNum == 16">

          <div class="battle-item" data-area-f="B" data-pk1="16.3" data-pk2="16.14" data-pk="8.3" :class="{select: currentPk == '8.3'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(16, 3)}}</div>
              <div class="battle-sport">{{getSportName(16, 14)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16,3 , '8.3')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 14, '8.3')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="B" data-pk1="16.6" data-pk2="16.11" data-pk="8.6" :class="{select: currentPk == '8.6'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(16, 6)}}</div>
              <div class="battle-sport">{{getSportName(16, 11)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 6, '8.6')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 11, '8.6')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="D" data-pk1="16.10" data-pk2="16.7" data-pk="8.7" :class="{select: currentPk == '8.7'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(16, 10)}}</div>
              <div class="battle-sport">{{getSportName(16, 7)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 10, '8.7')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 7, '8.7')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="D" data-pk1="16.15" data-pk2="16.2" data-pk="8.2" :class="{select: currentPk == '8.2'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(16, 15)}}</div>
              <div class="battle-sport">{{getSportName(16, 2)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 15, '8.2')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(16, 2, '8.2')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
        </div>

        <div class="battle-left battle-c" data-area="A" v-if="showNum == 8">
          <div class="battle-item" data-area-f="A" data-pk1="8.1" data-pk2="8.8" data-pk="4.1" :class="{select: currentPk == '4.1'}" @click="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(8, 1)}}</div>
              <div class="battle-sport">{{getSportName(8, 8)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(8, 1, '4.1')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(8, 8, '4.1')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="C" data-pk1="8.5" data-pk2="8.4" data-pk="4.4" :class="{select: currentPk == '4.4'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(8, 5)}}</div>
              <div class="battle-sport">{{getSportName(8, 4)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(8, 5, '4.4')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(8, 4, '4.4')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
        </div>
        <div class="battle-right battle-c" data-area="B" v-if="showNum == 8">
          <div class="battle-item" data-area-f="B" data-pk1="8.3" data-pk2="8.6" data-pk="4.3" :class="{select: currentPk == '4.3'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(8, 3)}}</div>
              <div class="battle-sport">{{getSportName(8, 6)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(8, 3, '4.3')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(8, 6, '4.3')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
          <div class="battle-item" data-area-f="D" data-pk1="8.7" data-pk2="8.2" data-pk="4.2" :class="{select: currentPk == '4.2'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(8, 7)}}</div>
              <div class="battle-sport">{{getSportName(8, 2)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(8, 7, '4.2')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(8, 2, '4.2')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
        </div>

        <div class="battle-left battle-c" data-area="A" v-if="showNum == 4">
          <div class="battle-item" data-pk1="4.1" data-pk2="4.4" data-pk="2.1" :class="{select: currentPk == '2.1'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(4, 1)}}</div>
              <div class="battle-sport">{{getSportName(4, 4)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(4, 1, '2.1')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(4, 4, '2.1')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
        </div>
        <div class="battle-right battle-c" data-area="B" v-if="showNum == 4">
          <div class="battle-item" data-pk1="4.3" data-pk2="4.2" data-pk="2.2" :class="{select: currentPk == '2.2'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(4, 3)}}</div>
              <div class="battle-sport">{{getSportName(4, 2)}}</div>
            </div>
            <div class="item-score">
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(4, 3, '2.2')"></div>
              </div>
              <div class="battle-score">
                <div class="score-o" v-for="item in getSportScore(4, 2, '2.2')"></div>
              </div>
            </div>
            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
        </div>
        <div class="battle-left battle-c all" data-area="全" v-if="showNum == 2">
          <div class="tipp">冠军战</div>
          <div class="battle-item" data-pk1="2.1" data-pk2="2.2" data-pk="1.1" :class="{select: currentPk == '1.1'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(2, 1)}}</div>
              <div class="battle-sport">{{getSportName(2, 2)}}</div>
            </div>
            <div class="lun-item-box">
              <div class="lun-item" :class="{select: '1' == lun && currentPk == '1.1'}" data-lun="1" @click.stop="battleLunItemSelect">
                <div class="lun-label">第一轮</div>
                <div class="item-score">
                  <div class="battle-score">
                    <div class="score-o" v-for="item in getSportScoreT(2, 1, '1.1', 1)"></div>
                  </div>
                  <div class="battle-score">
                    <div class="score-o" v-for="item in getSportScoreT(2, 2, '1.1', 1)"></div>
                  </div>
                </div>
              </div>
              <div class="lun-item" :class="{select: '2' == lun && currentPk == '1.1'}" data-lun="2" @click.stop="battleLunItemSelect">
                <div class="lun-label">第二轮</div>
                <div class="item-score">
                  <div class="battle-score">
                    <div class="score-o" v-for="item in getSportScoreT(2, 1, '1.1', 2)"></div>
                  </div>
                  <div class="battle-score">
                    <div class="score-o" v-for="item in getSportScoreT(2, 2, '1.1', 2)"></div>
                  </div>
                </div>
              </div>
              <div class="lun-item" :class="{select: '3' == lun && currentPk == '1.1'}" data-lun="3" @click.stop="battleLunItemSelect">
                <div class="lun-label">第三轮</div>
                <div class="item-score">
                  <div class="battle-score">
                    <div class="score-o" v-for="item in getSportScoreT(2, 1, '1.1', 3)"></div>
                  </div>
                  <div class="battle-score">
                    <div class="score-o" v-for="item in getSportScoreT(2, 2, '1.1', 3)"></div>
                  </div>
                </div>
              </div>
            </div>
            <div class="item-btn">
              <!--<div class="save-btn q" @click.stop="cancelScoreHandel">清除分数</div>-->
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
        </div>
        <div class="battle-right battle-c all" data-area="全" v-if="showNum == 2">
          <div class="tipp">季军战</div>
          <div class="battle-item" data-pk1="3.1" data-pk2="3.2" data-pk="3.0" :class="{select: currentPk == '3.0'}" @click.stop="battleItemSelect">
            <div class="item-sport">
              <div class="battle-sport">{{getSportName(3, 1)}}</div>
              <div class="battle-sport">{{getSportName(3, 2)}}</div>
            </div>
            <div class="lun-item-box">
              <div class="lun-item" :class="{select: '1' == lun && currentPk == '3.0'}" data-lun="1" @click.stop="battleLunItemSelect">
                <div class="lun-label">第一轮</div>
                <div class="item-score">
                  <div class="battle-score">
                    <div class="score-o" v-for="item in getSportScoreT(3, 1, '3.0',1)"></div>
                  </div>
                  <div class="battle-score">
                    <div class="score-o" v-for="item in getSportScoreT(3, 2, '3.0',1)"></div>
                  </div>
                </div>
              </div>
              <div class="lun-item" :class="{select: '2' == lun && currentPk == '3.0'}" data-lun="2" @click.stop="battleLunItemSelect">
                <div class="lun-label">第二轮</div>
                <div class="item-score">
                  <div class="battle-score">
                    <div class="score-o" v-for="item in getSportScoreT(3, 1, '3.0',2)"></div>
                  </div>
                  <div class="battle-score">
                    <div class="score-o" v-for="item in getSportScoreT(3, 2, '3.0',2)"></div>
                  </div>
                </div>
              </div>
              <div class="lun-item" :class="{select: '3' == lun && currentPk == '3.0'}" data-lun="3" @click.stop="battleLunItemSelect">
                <div class="lun-label">第三轮</div>
                <div class="item-score">
                  <div class="battle-score">
                    <div class="score-o" v-for="item in getSportScoreT(3, 1, '3.0',3)"></div>
                  </div>
                  <div class="battle-score">
                    <div class="score-o" v-for="item in getSportScoreT(3, 2, '3.0',3)"></div>
                  </div>
                </div>
              </div>
            </div>

            <div class="item-btn">
              <div class="save-btn" @click.stop="saveScoreHandel">确认分数</div>
            </div>
          </div>
        </div>
      </div>
      <div class="battle-bottom">
      </div>
    </div>
  </div>
</template>

<script>
  import {listJwEight, saveEightPro, clearEightPro} from "@/api/jiewu/jwEight";
  import {getJwMatch} from "@/api/jiewu/jwMatch";
  import {listJwGameItem} from "@/api/jiewu/JwGameItem";
  import {startPk, getPkScores, xianshidafen, xianshi3dafen} from "@/api/jiewu/JwAppScore";
  import {sendBattle, sendMusic} from "@/api/jiewu/ScreenSend";

  export default {
    name: 'battlePhoneNew321',
    components: {},
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
        lun: 1,
        isfen: false,
        isfenF: false,
        isTwoLun: false,
        currentPk: "",
        showNum: "32",
        matchId: null,
        gameItemId: null,
        currentGameItem: {},
        JwGameItemList: [],
        jwEightList: [],
        pkScoreList: [],
        inte: null,
        sportConfig: {
          "16.1": 1, "16.2": 9, "16.3": 5, "16.4": 13, "16.5": 3, "16.6": 11, "16.7": 7, "16.8": 15,
          "16.9": 2, "16.10": 10, "16.11": 6, "16.12": 14, "16.13": 4, "16.14": 12, "16.15": 8, "16.16": 16,
          "8.1": 1, "8.2": 5, "8.3": 3, "8.4": 7, "8.5": 2, "8.6": 6, "8.7": 4, "8.8": 8,
          "4.1": 1, "4.2": 3, "4.3": 2, "4.4": 4,
          "2.1": 1, "2.2": 2,
          "3.1": 1, "3.2": 2,
        }
      };
    },
    computed: {},
    destroyed() {
      if (this.inte) {
        clearInterval(this.inte);
      }
    },
    created() {
      this.getGameItemList();
      this.getList();
      this.getPkScores();
      this.inte = setInterval(this.getList, 2000)
    },
    methods: {
      handleClearScreen(){
        sendMusic({worksMusic: "", matchId: this.matchId}).then(res => {
          this.$modal.msgSuccess("发送成功");
        })
      },
      handleSendBattle() {
        sendBattle({gameItemId: this.gameItemId, matchId: this.matchId}).then(res => {
          this.$modal.msgSuccess("发送成功");
        })
      },
      cancelScoreHandel(e) {
        let that = this;
        e.stopPropagation();
        let pk = e.target.closest(".battle-item").getAttribute("data-pk");
        clearEightPro({gameItemId: this.gameItemId, playerPosition: pk,}).then(res => {

        })
      },
      saveScoreHandel(e) {
        let that = this;
        e.stopPropagation();
        let pk = e.target.closest(".battle-item").getAttribute("data-pk");
        let pks = pk.split(".");
        let p1 = e.target.closest(".battle-item").getAttribute("data-pk1").split(".");
        let sport1 = this.jwEightList.find(item => item.playerPosition == p1[0] && item.playerIndex == p1[1]);
        let score1 = this.pkScoreList.filter(item => (item.playerPkGroup == pk && item.playerId == sport1.playerId)).length || 0;
        let p2 = e.target.closest(".battle-item").getAttribute("data-pk2").split(".");
        let sport2 = this.jwEightList.find(item => item.playerPosition == p2[0] && item.playerIndex == p2[1]);
        let score2 = this.pkScoreList.filter(item => (item.playerPkGroup == pk && item.playerId == sport2.playerId)).length || 0;
        if (score1 + score2 <= 0) {
          return;
        }

        let p1Score = 0;
        let p2Score = 0;

        for (let lun = 1; lun <= 3; lun++) {
          if (((this.pkScoreList || []).filter(item => (item.lun == lun)).length) > 0) {
            let pk1ScoreList1 = (this.pkScoreList || []).filter(item => (item.lun == lun && item.playerPkGroup == pk && item.playerId == sport1.playerId));
            let pk1ScoreList2 = (this.pkScoreList || []).filter(item => (item.lun == lun && item.playerPkGroup == pk && item.playerId == sport2.playerId));
            if (pk1ScoreList1.length > pk1ScoreList2.length) {
              p1Score++;
            } else if (pk1ScoreList1.length < pk1ScoreList2.length) {
              p2Score++;
            } else {
              p1Score++;
              p2Score++;
            }
          }
        }
        if (p1Score == p2Score) {
          this.$notify.error({
            title: '错误',
            message: '分数相同，需要 ONE MORE'
          });
          return;
        }

        let proSport = p1Score > p2Score ? sport1 : sport2;
        let nerPro = {
          gameItemId: this.gameItemId,
          playerId: proSport.playerId,
          playerIndex: pks[1],
          playerPosition: pks[0]
        };

        that.$modal.confirm(`确认  ${(proSport && proSport.jwSignRecordSportList && proSport.jwSignRecordSportList.length > 0) ? (proSport.jwSignRecordSportList.map(item => item.playerName).join(" ")) : ""}  晋级`).then(function () {
          return saveEightPro(nerPro);

        }).then(() => {
          if ((pks[0] * 1) == 2) {
            let nerProT = {gameItemId: this.gameItemId, playerId: p1Score > p2Score ? sport2.playerId : sport1.playerId, playerIndex: pks[1], playerPosition: "3"};
            saveEightPro(nerProT).then(res => {

            })
          }

          that.$notify({
            title: '成功',
            message: '确认成功',
            type: 'success'
          });
        }).catch(() => {
        });
      },
      battleItemSelect(e) {
        let area = e.target.closest(".battle-c").getAttribute("data-area");
        if (!this.isfen) area = '全';

        if (this.isfenF) {
          // 分四个场地
          area = e.target.closest(".battle-item").getAttribute("data-area-f");
        }

        let isTwoLun = this.isTwoLun ? "2" : "1";

        let pk = e.target.closest(".battle-item").getAttribute("data-pk");
        this.currentPk = pk;
        let p1 = e.target.closest(".battle-item").getAttribute("data-pk1");
        let p2 = e.target.closest(".battle-item").getAttribute("data-pk2");
        this.lun = "1";
        startPk({gameItemId: this.gameItemId, currentPosition: p1.split(".")[0], currentPkGroup: {lun: this.lun, isTwoLun: isTwoLun, itemName: this.currentGameItem.name, currentPk: this.currentPk, pk1: p1, pk2: p2, area: area}}).then(res => {

        })
      },
      battleLunItemSelect(e) {
        e.stopPropagation();
        let lun = e.target.closest(".lun-item").getAttribute("data-lun");
        this.lun = lun;

        let isTwoLun = this.isTwoLun ? "2" : "1";

        let area = e.target.closest(".battle-c").getAttribute("data-area");
        if (!this.isfen) area = '全';
        let pk = e.target.closest(".battle-item").getAttribute("data-pk");
        this.currentPk = pk;
        let p1 = e.target.closest(".battle-item").getAttribute("data-pk1");
        let p2 = e.target.closest(".battle-item").getAttribute("data-pk2");

        startPk({gameItemId: this.gameItemId, currentPosition: p1.split(".")[0], currentPkGroup: {lun: this.lun, isTwoLun: isTwoLun, itemName: this.currentGameItem.name, currentPk: this.currentPk, pk1: p1, pk2: p2, area: area}}).then(res => {

        })

      },
      qingPing() {
        startPk({currentPkGroup: {currentPk: "null"}}).then(res => {

        })
      },
      xianshidafen() {
        xianshidafen({}).then(res => {

        })
      },
      xianshi3dafen() {
        xianshi3dafen({}).then(res => {

        })
      },
      getPkScores() {
        // if (this.currentPk) {
        getPkScores({gameItemId: this.gameItemId, currentPkGroup: ""}).then(res => {
          this.pkScoreList = res.data || []
        })
        // }
      },
      getSportScore(positoin, index, pk) {
        let sport = this.jwEightList.find(item => item.playerPosition == positoin && item.playerIndex == index);
        if (sport) {
          sport.score = this.pkScoreList.filter(item => (item.playerPkGroup == pk && item.playerId == sport.playerId)).length;
          return sport.score;
        }
        return 0;
      },
      getSportScoreT(positoin, index, pk, lun) {
        let sport = this.jwEightList.find(item => item.playerPosition == positoin && item.playerIndex == index);
        if (sport) {
          sport.score = this.pkScoreList.filter(item => (item.playerPkGroup == pk && item.playerId == sport.playerId && item.lun == lun)).length;
          return sport.score;
        }
        return 0;
      },
      getList() {
        if (this.gameItemId) {
          listJwEight({gameItemId: this.gameItemId}).then(response => {
            this.jwEightList = response.rows;
            this.getPkScores()
          });
        } else {
          this.pkScoreList = [];
          this.jwEightList = [];
        }
      },
      getSportName(positoin, index) {
        // return index;
        // index = this.sportConfig[positoin + "." + index];
        let sport = this.jwEightList.find(item => item.playerPosition == positoin && item.playerIndex == index);

        return (sport && sport.jwSignRecordSportList && sport.jwSignRecordSportList.length > 0) ? (sport.jwSignRecordSportList.map(item => item.playerName).join(" ")) : " - ";
      },
      gameItemChange() {
        this.currentGameItem = this.JwGameItemList.find(item => item.id == this.gameItemId);
        this.showNum = this.currentGameItem.promotionNum + "";

        this.getList()
      },
      getGameItemList() {
        this.JwGameItemList = [];
        if (this.matchId) {
          listJwGameItem({matchId: this.matchId, pageNum: 1, pageSize: 5000, matchType: 2}).then(response => {
            this.JwGameItemList = (response.rows || []).filter(ite=>ite.signCount > 0);
          });
        }
      },
    }
  }
</script>

<style scoped lang="scss">
  .battle-con-h {
    user-select: none;
  }

  .el-form {
    padding: 16px;
    padding-bottom: 0;
    text-align: center;
  }

  .battle-con-h {
    display: flex;
    flex-direction: column;
    background: #F2F6FC;
    height: 100%;

  }

  .battle-con {
    display: flex;
    flex-direction: column;

    .ballte-header {
      display: flex;
      flex-direction: column;
      align-items: center;
      text-align: center;
      margin-bottom: 12px;
      margin-top: 12px;

      .fen {
        display: flex;
        flex-direction: row;
        align-items: center;
        /*margin-right: 32pt;*/
        /*margin-left: 32pt;*/
        margin-bottom: 12pt;
      }

      .qing {
        font-size: 16pt;
        font-weight: 900;
        margin-left: 56pt;
        /*margin-left: 48px;*/
        color: #df5000;
      }
    }

    .battle-sports {
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;

      .battle-left, .battle-right {
        flex: 1;
        padding: 8pt;

        .tipp {
          background: #409EFF;
          width: 88px;
          text-align: center;
          border-radius: 8px;
          color: #fff;

          margin-left: calc(50% - 44px);
        }

        &.all {
          flex: auto;
          width: 100%;
          padding: 0 8pt;
        }
      }

      .battle-item {
        display: flex;
        flex-direction: row;
        align-items: center;
        background: #fff;
        padding: 4pt 8pt;
        margin-bottom: 16px;
        border-radius: 4pt;
        border: 2px solid #fff;

        &.select {
          border: 2px solid #0000ff;
        }

        .lun-item-box {
          flex: 1;
          display: flex;
          flex-direction: column;
          padding-left: 12px;
          padding-right: 12px;
          padding-top: 12px;

          .lun-item {
            display: flex;
            flex-direction: row;
            align-items: center;
            background: #efefef;
            color: #333;
            margin-bottom: 12px;
            border-radius: 8px;
            padding-left: 8px;

            &.select {
              background: #e8c805;
            }
          }
        }

        .item-sport {
          .battle-sport {
            width: 100px;
            text-align: center;
            color: #1572E5;
            background: #EFF8FF;
            border: 1px solid #1572E5;
            border-radius: 4px;
            height: 24px;
            line-height: 24px;

            white-space: nowrap;
            overflow: hidden;

            &:last-child {
              margin-top: 4px;
            }

            &:nth-child(2) {
              color: #F56C6C;
              background: #f9f9f9;
              border: 1px solid #F56C6C;
            }
          }
        }

        .item-score {
          flex: 1;
          padding: 0 8pt;

          .battle-score {
            height: 20px;
            display: flex;
            flex-direction: row;
            align-items: center;

            .score-o {
              margin-left: 12px;
              width: 20px;
              height: 20px;
              border-radius: 12px;
              background: #1572E5;
            }

            &:last-child {
              margin-top: 4px;

              .score-o {

                background: #F56C6C;
              }
            }
          }
        }

        .item-btn {
          .save-info {
            text-align: center;
            font-weight: 600;
            color: #E6A23C;
          }

          .save-btn {
            margin-top: 4px;
            text-align: center;
            background: #E6A23C;
            padding: 4pt 8pt;
            color: #fff;
            font-size: 12px;
            border-radius: 24px;
          }
        }
      }
    }
  }
</style>
