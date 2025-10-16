<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input v-model="data.title" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="Insert post title to query"></el-input>
      <el-button type="info" plain @click="load">query</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">reset</el-button>
    </div>

    <el-row :gutter="10">
      <el-col :span="12" v-for="item in data.tableData" :key="item.id">
        <div class="card" style="display: flex; grid-gap: 20px; margin-bottom: 10px; cursor: pointer" @click="router.push('/manager/forumDetail?id=' + item.id)">
          <img style="width: 100px; height: 100px; border-radius: 5px" :src="item.img" alt="">
          <div style="flex: 1">
            <div style="font-weight: bold; font-size: 16px; margin-bottom: 10px">{{ item.title }}</div>
            <div style="color: #888; margin-bottom: 10px; height: 40px; line-height: 20px; font-size: 13px" class="line2">{{ item.description }}</div>
            <div style="display: flex; align-items: center; color: #666; font-size: 13px">
              <img style="width: 20px; height: 20px; border-radius: 30%" :src="item.userAvatar">
              <span style="margin-left: 5px; margin-right: 30px">{{ item.userName }}</span>
              <el-icon style="position: relative; top: 2px"><View /></el-icon>
              <span style="margin-left: 5px; margin-right: 30px">{{ item.readCount }}</span>
              <el-icon style="position: relative; top: 2px"><Clock /></el-icon>
              <span style="margin-left: 5px">{{ item.time }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";


const data = reactive({
  title: null,
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
})

const load = ()=> {
  request.get('/forum/selectPage', {
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
      status: 'Approved'
    }
  }).then(res => {
    if (res.code === '200'){
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

load()
</script>