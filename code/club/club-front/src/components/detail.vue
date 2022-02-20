<!--
订单详情，结算对话框，个人中心用到 * author:阳春白雪
-->
<template>
  <el-descriptions class="margin-top" :column="3" border>
    <template v-for="(item,key) in info">
      <el-descriptions-item :key="key" v-if="item.start || val.finishTime">
        <template slot="label">
          <i :class="item.icon"></i>
          {{item.label||(val.entry?item.labels[0]:item.labels[1])}}
        </template>
        <template v-if="item.arr">
          <el-tag size="small" v-for="name in val[item.prop]" :key="name">{{name}}</el-tag>
        </template>
        <template
          v-else 
        >{{item.value?(val[item.prop]?item.value[0]:item.value[1]):val[item.prop]}}</template>
      </el-descriptions-item>
    </template>
  </el-descriptions>
</template>

<script>
export default {
  data() {
    return {};
  },
  components: {},
  computed: {
    info() {
      return this.$store.state.render.common.detail;
    }
  },
  props: {
    val: Object
  },
  methods: {}
};
</script>

<style lang="less">
</style>
