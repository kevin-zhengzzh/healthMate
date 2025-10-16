<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-date-picker style="width: 240px; margin-right: 10px" v-model="data.date" type="date" placeholder="Select date to query" format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
      <el-button type="info" plain @click="load">Query</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">Reset</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" plain @click="handleAdd" v-if="data.user.role === 'USER'">Add</el-button>
      <el-button type="danger" plain @click="delBatch">Batch delete</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="userName" label="UserName"></el-table-column>
        <el-table-column prop="foodsName" label="FoodsName"></el-table-column>
        <el-table-column prop="intake" label="Intake">
          <template v-slot="scope">{{ scope.row.intake }} g</template>
        </el-table-column>
        <el-table-column prop="calorie" label="Calorie">
          <template v-slot="scope">{{ scope.row.calorie }} kcal</template>
        </el-table-column>
        <el-table-column prop="date" label="Date"></el-table-column>

        <el-table-column label="Operation" width="100" fixed="right">
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

  <el-dialog title="Eating records information" v-model="data.formVisible" width="40%" destroy-on-close>
    <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="170px" style="padding:20px">
      <el-form-item prop="foodsName" label="FoodsName">
        <el-input style="width: 300px" v-model="data.form.foodsName" placeholder="Please insert foodsName"></el-input>
      </el-form-item>
      <el-form-item prop="intake" label="Intake (g)">
        <el-input-number style="width: 300px" v-model="data.form.intake" placeholder="Please insert intake"></el-input-number>
      </el-form-item>
      <el-form-item prop="calorie" label="Calorie (kcal)">
        <el-input-number style="width: 300px" v-model="data.form.calorie" placeholder="Please insert calorie"></el-input-number>
      </el-form-item>
      <el-form-item prop="date" label="Date">
        <el-date-picker style="width: 300px" v-model="data.form.date" type="date" placeholder="Date" format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
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
import {reactive,ref} from "vue";
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
  date: null,
  ids: [],
  rules: {
    foodsName: [
      { required: true, message: 'Please insert foodsName', trigger: 'blur' }
    ],
    intake: [
      { required: true, message: 'Please insert intake', trigger: 'blur' }
    ],
    calorie: [
      { required: true, message: 'Please insert calorie', trigger: 'blur' }
    ],
  }
})

const formRef = ref()

const load = ()=> {
  request.get('/eatingRecords/selectPage', {
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      date: data.date,
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
  data.formVisible = true
}
const add = () => {
  data.form.userId = data.user.id
  request.post('/eatingRecords/add', data.form).then(res =>{
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
  request.put('/eatingRecords/update', data.form).then(res => {
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
    if(valid) {
      data.form.id ? update() : add()
    }
  })
}
const del = (id) =>{
  ElMessageBox.confirm('Are you sure to delete?','confirm', {type:"warning"}).then(res =>{
    request.delete('/eatingRecords/delete/'+ id).then(res => {
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
    request.delete("/eatingRecords/delete/batch", {data: data.ids}).then(res =>{
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
  data.date = null
  load()
}
load()
</script>