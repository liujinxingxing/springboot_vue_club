<!--
ifamer 组件 * author:阳春白雪
-->
<template>
  <div class="ifamer-main">
    <iframe :src="path" scrolling="auto"></iframe>
  </div>
</template>

<script>
export default {
  name: "ifamer",
  components: {},
  data() {
    return {};
  },
  props: {
    path: {
      type: String,
      default() {
        return "";
      }
    }
  }
};
</script>

<style lang="less">
.ifamer-main {
  height: 100%;
  iframe {
    width: 100%;
    height: 100%;
    border: 0;
  }
}
</style>