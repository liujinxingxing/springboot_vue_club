<!--
选择用户下拉table选择 * author:阳春白雪
-->
<template>
  <SelectTable :selectTable="selectTable" @change="change" ref="rolesTable" v-model="roles">
    <div slot="toolbar">
      <SearchInput
        v-model="keyword"
        ref="searchInput"
        @search="searchTable"
        @clear="searchTable"
      ></SearchInput>
    </div>
  </SelectTable>
</template>

<style lang="less"></style>

<script>
import SelectTable from "components/select-table.vue";
import SearchInput from "components/search-input.vue";
import { inter } from "api/interface.js";

import Qs from "qs";
export default {
  data() {
    return {
      roles: [],
      keyword: "",
    };
  },
  computed: {
    selectTable(){
      var data = this.$store.state.render.common.selectRoles;
      data.url = inter.user.list;
      data.columns[2].formatter = this.formatter;
      return data;
    }
  },
  components: {
    SelectTable,
    SearchInput,
  },
  methods: {
    formatter(row) {
      switch (row.leveles) {
        case 3:
        case 2:
        case 1:
          return this.$store.state.render.users.info.enum[0][row.leveles];
        default:
          return this.$store.state.render.users.info.enum[0][0];
      }
    },
    change(val){
      this.$emit("change", val);
    },
    searchTable: function() {
      this.$refs.rolesTable.reload({ keyword: this.keyword });
    },
    getSelect: function() {
      return this.roles;
    },
    setValue: function(value) {
      this.$refs.rolesTable.setValue(value);
    },
    clearSelect: function() {
      this.$refs.searchInput.clear();
      this.$refs.rolesTable.setValue([]);
    },
  },
};
</script>
