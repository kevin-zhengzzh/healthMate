<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.content" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="Insert comment content to query"></el-input>
      <el-button type="info" plain @click="load">query</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">reset</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="danger" plain @click="delBatch">Batch delete</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="userAvatar" label="UserAvatar">
          <template #default="scope">
            <el-image style="width: 50px; height: 50px; display: block; border-radius: 5px" :src="scope.row.userAvatar" :preview-src-list="[scope.row.userAvatar]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="UserName"></el-table-column>
        <el-table-column prop="forumTitle" label="ForumTitle">
          <template v-slot="scope">
            <a style="color: #0066ff" :href="'/manager/forumDetail?id=' + scope.row.fid">{{ scope.row.forumTitle }}</a>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="Content"></el-table-column>
        <el-table-column prop="time" label="Time"></el-table-column>
        <el-table-column label="operation" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)" v-if="data.user.role === 'USER'"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

  <el-dialog title="Comment information" v-model="data.formVisible" width="40%" destroy-on-close>
    <el-form ref="form" :model="data.form" label-width="70px" style="padding:20px">
      <el-form-item prop="title" label="Comment content">
        <el-input v-model="data.form.content" placeholder="please insert your comment"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span slot="footer" class="dialog-footer">
      <el-button @click="data.formVisible = false">cancel</el-button>
      <el-button type="primary" @click="save">confirm</el-button>
    </span>
    </template>
  </el-dialog>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import request from '@/utils/request.js'
import {Delete, Edit} from "@element-plus/icons-vue";


const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  content: null,
  ids: []
})
const load = ()=> {
  request.get('/comment/selectPage', {
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      content: data.content,
      userId: data.user.role === 'USER' ? data.user.id : null
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
const handleAdd = () => {
  data.form={}
  data.formVisible=true
}
const handleEdit = (row) =>{
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible=true
}
const add = () => {
  request.post('/comment/add', data.form).then(res =>{
    if (res.code ==='200'){
      ElMessage.success('success')
      data.formVisible=false
      load()
    }else{
      ElMessage.error(res.msg)
    }
  })
}
const update = () =>{
  request.put('/comment/update', data.form).then(res => {
    if (res.code === '200'){
      ElMessage.success("success")
      data.formVisible=false

      try {
        const me = JSON.parse(localStorage.getItem('xm-user') || '{}');
        if (me?.id && data.form?.id && me.id === data.form.id && data.form.avatar) {
          const updated = { ...me, avatar: data.form.avatar };
          localStorage.setItem('xm-user', JSON.stringify(updated));
          window.dispatchEvent(new Event('avatar-updated'));   // notice Manager.vue
        }
      } catch {}
      
      load()
    }
  })
}
const save = () => {
  data.form.id ? update() : add()
}
const del = (id) => {
  ElMessageBox.confirm('Are you sure to delete?','confirm', {type:"warning"}).then(res =>{
    request.delete('/comment/delete/'+ id).then(res => {
      if (res.code === '200') {
        ElMessage.success("delete successfully")
        load()
      } else {
        ElMessage.error(res.msg)}
    })
  }).catch(err =>{
    console.error(err)
  })
}
//batch delete
const delBatch = () =>{
  if(!data.ids.length){
    ElMessage.warning("Please choose data")
    return
  }
  ElMessageBox.confirm('Are you sure to delete?','confirm', {type:"warning"}).then(res =>{
    request.delete("/comment/delete/batch", {data: data.ids}).then(res =>{
      if (res.code === '200') {
        ElMessage.success("delete successfully")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err =>{
    console.error(err)
  })
}

// check box binding id
const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
  console.log(data.ids)
}
const reset = () => {
  data.content = null
  load()
}
load()
</script>