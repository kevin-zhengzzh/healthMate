<template>
  <div style="width: 60%; margin: 10px auto">
    <div class="card" style="padding: 20px; margin-bottom: 20px">
      <div style="text-align: center; font-size: 25px; font-weight: bold; margin-bottom: 20px">{{ data.forum.title }}</div>
      <div style="display: flex; align-items: center; justify-content: center; color: #666; font-size: 13px; margin-bottom: 20px">
        <el-icon style="position: relative; top: 2px"><User /></el-icon>
        <span style="margin-left: 5px; margin-right: 20px">{{ data.forum.userName }}</span>
        <el-icon style="position: relative; top: 2px"><View /></el-icon>
        <span style="margin-left: 5px; margin-right: 20px">{{ data.forum.readCount }}</span>
        <el-icon style="position: relative; top: 2px"><Clock /></el-icon>
        <span style="margin-left: 5px">{{ data.forum.time }}</span>
      </div>
      <div style="padding: 20px; font-size: 16px; color: #444; line-height: 24px" v-html="data.forum.content"></div>
    </div>

    <div class="card" style="padding: 20px">
      <div style="margin-bottom: 20px">
        <div style="font-size: 20px; font-weight: bold; margin-bottom: 20px">Comment ({{ data.commentCount }})</div>
        <div v-if="data.user.role === 'USER'">
          <el-input type="textarea" :rows="3" v-model="data.form.content" placeholder="Please insert comment"></el-input>
          <div style="text-align: right; margin-top: 5px"><el-button type="primary" @click="add(null)">Comment</el-button></div>
        </div>
      </div>

      <div>
        <div v-for="item in data.commentList" :key="item.id" style="display: flex; grid-gap: 20px; margin-bottom: 20px">
          <img style="width: 50px; height: 50px; border-radius: 50%" :src="item.userAvatar">
          <div style="flex: 1">
            <div style="color: #666; margin-bottom: 5px">{{ item.userName }}</div>
            <div style="color: #333; margin-bottom: 5px">{{ item.content }}</div>
            <div style="color: #666; font-size: 13px; display: flex;align-items: center; margin-bottom: 10px">
              <div style="width: 150px">{{ item.time }}</div>
              <div @click="handleReply(item)" style="cursor: pointer; margin-right: 20px"><el-icon size="14" color="#666" style=" position: relative; top: 2px"><Comment /></el-icon>Reply</div>
              <div @click="deleteComment(item.id)" v-if="item.userId === data.user.id" style="cursor: pointer;"><el-icon size="14" color="#666" style="position: relative; top: 2px"><Delete /></el-icon>Delete</div>
            </div>
            <div v-if="item.showReply" style="margin-bottom: 10px">
              <el-input type="textarea" v-model="item.replyContent" placeholder="Please insert reply content"></el-input>
              <div style="text-align: right; margin-top: 5px"><el-button type="primary" @click="add(item)">Reply</el-button></div>
            </div>

            <div v-for="sub in item.children" :key="item.id" style="display: flex; grid-gap: 20px; margin-bottom: 20px">
              <img style="width: 50px; height: 50px; border-radius: 50%" :src="sub.userAvatar">
              <div style="flex: 1">
                <div style="color: #666; margin-bottom: 5px">{{ sub.userName }}</div>
                <div style="color: #333; margin-bottom: 5px">{{ sub.content }}</div>
                <div style="color: #666; font-size: 13px; display: flex;align-items: center; margin-bottom: 10px">
                  <div style="width: 150px">{{ sub.time }}</div>
                  <div @click="handleReply(sub)" style="cursor: pointer; margin-right: 20px"><el-icon size="14" color="#666" style=" position: relative; top: 2px"><Comment /></el-icon>Reply</div>
                  <div @click="deleteComment(sub.id)" v-if="sub.userId === data.user.id" style="cursor: pointer;"><el-icon size="14" color="#666" style="position: relative; top: 2px"><Delete /></el-icon>Delete</div>
                </div>
                <div v-if="sub.showReply">
                  <el-input type="textarea" v-model="sub.replyContent" placeholder="Please insert reply content"></el-input>
                  <div style="text-align: right; margin-top: 5px"><el-button type="primary" @click="add(sub)">Reply</el-button></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="data.total" style="margin-top: 30px">
        <el-pagination @current-change="loadComment" layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
      </div>

    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  id:router.currentRoute.value.query.id,
  forum: {},
  form: {},
  total: 0,
  pageNum: 1,
  pageSize: 5,
  commentList: [],
  showReply: false,
  commentCount: 0,
})

request.get('/forum/selectById/' + data.id).then(res => {
  data.forum = res.data
})

const deleteComment = (id) => {
  ElMessageBox.confirm('Are you sure to delete?','confirm', {type:"warning"}).then(res =>{
    request.delete('/comment/deepDelete/'+ id).then(res => {
      if (res.code === '200') {
        ElMessage.success("delete successfully")
        loadComment()
      } else {
        ElMessage.error(res.msg)}
    })
  }).catch(err =>{
    console.error(err)
  })
}

const handleReply = (comment) =>{
  comment.showReply = !comment.showReply
  comment.replyContent = null
}

const add = (comment) => {
  if (comment) { //回复
    if(!comment.replyContent){
      ElMessage.warning('Please insert comment')
      return
    }
    data.form.content = comment.replyContent
    data.form.pid = comment.id //父级id
  } else { //直接评论
    if(!data.form.content){
      ElMessage.warning('Please insert comment')
      return
    }
  }
  data.form.userId = data.user.id
  data.form.fid = data.id
  request.post('/comment/add', data.form).then(res =>{
    if (res.code ==='200'){
      ElMessage.success('Comment successfully')
      data.form = {}
      loadComment()
    }else{
      ElMessage.error(res.msg)
    }
  })
}

const loadComment = ()=> {
  request.get('/comment/selectTotal/' + data.id).then(res => {
    data.commentCount = res.data
  })

  request.get('/comment/selectTree', {
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      fid: data.id
    }
  }).then(res => {
    if (res.code === '200'){
      data.commentList = res.data?.list || []
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadComment()
</script>