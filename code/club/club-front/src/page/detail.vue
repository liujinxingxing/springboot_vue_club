<!--
订单详情 * author:Daye Shing
-->
<template>
  <div class="detail-main">
    <el-descriptions class="margin-top" :column="details.length" border direction="vertical" v-if="data.length > 0">
      <template v-for="(val,key) in data">
        <template v-for="(item,index) in details">
          <el-descriptions-item
            :key="key + ':' + index"
            :labelClassName="key != 0?'label--hidden':''"
          >
            <template slot="label" v-if="key == 0">{{item.label}}</template>
            {{val[item.prop]}}
          </el-descriptions-item>
        </template>
      </template>
    </el-descriptions>
  </div>
</template>

<script>
import { inter } from "api/interface";
export default {
  data() {
    return {
      data: [],
      details:[],
    };
  },
  computed: {
    info() {
      return this.$store.state.render.detail;
    }
  },
  mounted() {
    this.$bus.$on("setData", this.setData);
  },
  methods: {
    setData(path, his, data) {
      if (path == "detail") {
        this.details.push.apply(this.details,this.info[0]);
        this.details.push.apply(this.details,this.info[his?2:1]);
        this.details.push.apply(this.details,this.info[3]);
        if(his){
          this.details.push.apply(this.details,this.info[4]);
        }
        this.data = data;
      }
    }
  }
};
</script>

<style lang="less">
.detail-main {
  padding: 30px;
  .label--hidden {
    display: none;
  }
   .el-descriptions-item__cell {
     text-align: center !important;
   }
}
</style>
