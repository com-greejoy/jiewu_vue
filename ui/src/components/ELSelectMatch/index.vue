<template>
  <el-select style="width: 200pt" v-model="currentMatchId" placeholder="请选择比赛" @change="ch">
    <el-option
      style="width: 200pt"
      v-for="match in jwMatchList"
      :key="match.id"
      :label="match.matchName"
      :value="match.id"
    />
  </el-select>
</template>

<script>
  import Cookies from 'js-cookie'

  import {listJwMatch} from "@/api/jiewu/jwMatch";

  export default {
    name: "ELSelectMatch",
    props: {
      matchId: {
        type: Number,
        default: 14,
      },
      width:{
        type: Number,
        default: 220,
      }
    },
    data() {
      return {
        jwMatchList: []
      }
    },
    created() {
      listJwMatch(   {
        pageNum: 1,
          pageSize: 100,
          orderByColumn: "id", isAsc: "descending"
      },).then(res => {
        this.jwMatchList = res.rows;
        this.currentMatchId = (Cookies.get("matchId")  * 1) || null;
      })
    },
    methods:{
      ch(v){
        if(v){
          Cookies.set("matchId", v)
        }
      }
    },
    computed: {
      currentMatchId: {
        get() {
          return this.matchId || (Cookies.get("matchId")  * 1) || null
        },
        set(val) {
          this.$emit('update:matchId', val)
        }
      },
    },
    filters: {}
  };
</script>
<style scoped>

</style>
