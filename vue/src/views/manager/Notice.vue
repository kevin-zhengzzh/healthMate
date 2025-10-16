<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.title" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="Insert title to query"></el-input>
      <el-button type="info" plain @click="load">query</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">reset</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" plain @click="handleAdd">Add</el-button>
      <el-button type="danger" plain @click="delBatch">Batch delete</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="title" label="Announcement title" />
        <el-table-column prop="content" label="Content" />
        <el-table-column prop="time" label="Publish time" />
        <el-table-column label="operation" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

  <el-dialog title="Announcement information" v-model="data.formVisible" width="40%" destroy-on-close>
    <el-form ref="form" :model="data.form" label-width="70px" style="padding:20px">
      <el-form-item prop="title" label="Title">
        <el-input v-model="data.form.title" placeholder="please insert your title"></el-input>
      </el-form-item>
      <el-form-item prop="content" label="Content">
        <el-input type="textarea" :rows="4" v-model="data.form.content" placeholder="please insert your content"></el-input>
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
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  title: null,
  ids: []
})
const load = ()=> {
  request.get('/notice/selectPage', {
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
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
  request.post('/notice/add', data.form).then(res =>{
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
  request.put('/notice/update', data.form).then(res => {
    if (res.code === '200'){
      ElMessage.success("success")
      data.formVisible=false

      // 如果更新的是当前登录用户，把新头像写回本地并通知头部刷新
      try {
        const me = JSON.parse(localStorage.getItem('xm-user') || '{}');
        if (me?.id && data.form?.id && me.id === data.form.id && data.form.avatar) {
          const updated = { ...me, avatar: data.form.avatar };
          localStorage.setItem('xm-user', JSON.stringify(updated));
          window.dispatchEvent(new Event('avatar-updated'));   // 通知 Manager.vue
        }
      } catch {}
      
      load()
    }
  })
}
const save = () => {
  data.form.id ? update() : add()
}
const del = (id) =>{
  ElMessageBox.confirm('Are you sure to delete?','confirm', {type:"warning"}).then(res =>{
    request.delete('/notice/delete/'+ id).then(res => {
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
    request.delete("/notice/delete/batch", {data: data.ids}).then(res =>{
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
  data.title = null
  load()
}
load()
</script>