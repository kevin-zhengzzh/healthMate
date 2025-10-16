<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="insert name to query"></el-input>
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
        <el-table-column prop="username" label="account" />
        <el-table-column prop="avatar" label="avatar">
          <template v-slot="scope">
            <el-image style="width:40px; height: 40px; border-radius: 50%; display: block"
                      :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="name" />
        <el-table-column prop="role" label="role" />
        <el-table-column prop="phone" label="phone" />
        <el-table-column prop="email" label="email" />
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

  <el-dialog title="Ordinary user information" v-model="data.formVisible" width="40%" destroy-on-close>
    <el-form ref="formRef" :model="data.form" label-width="70px" :rules="data.rules" style="padding:20px">
      <el-form-item prop="username" label="username">
        <el-input v-model="data.form.username" placeholder="please insert your username"></el-input>
      </el-form-item>
      <el-form-item prop="avatar" label="avatar">
        <el-upload
        :action="baseUrl + '/files/upload'"
        :on-success="handleFileUpload"
        list-type="picture"
        >
          <el-button type="primary">click to upload</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item prop="name" label="name">
        <el-input v-model="data.form.name" placeholder="please insert your name"></el-input>
      </el-form-item>
      <el-form-item prop="phone" label="phone">
        <el-input v-model="data.form.phone" placeholder="please insert your phone"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="email">
        <el-input v-model="data.form.email" placeholder="please insert your email"></el-input>
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
import {reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import request from '@/utils/request.js'
import {Delete, Edit} from "@element-plus/icons-vue";

const baseUrl = import.meta.env.VITE_BASE_URL
const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  ids: [],
  rules: {
    username: [
      { required: true, message: 'Please insert account number', trigger: 'blur' }
    ],
    name: [
      { required: true, message: 'Please insert name', trigger: 'blur' }
    ],
  }
})

const formRef = ref()
const load = ()=> {
  request.get('/user/selectPage', {
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    if (res.code === '200'){
      data.tableData = res.data?.list || []
      data.total = res.data?.total
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
  request.post('/user/add', data.form).then(res =>{
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
  request.put('/user/update', data.form).then(res => {
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
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}
const del = (id) =>{
  ElMessageBox.confirm('Are you sure to delete?','confirm', {type:"warning"}).then(res =>{
    request.delete('/user/delete/'+ id).then(res => {
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
    request.delete("/user/delete/batch", {data: data.ids}).then(res =>{
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
const handleFileUpload = (res) =>{
  data.form.avatar = res.data
}
const reset = () => {
  data.name = null
  load()
}
load()
</script>