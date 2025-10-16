<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.title" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="Insert post title to query"></el-input>
      <el-select placeholder="Please select status" style="width: 240px; margin-right: 10px" v-model="data.status">
        <el-option label="Under review" value="Under review"></el-option>
        <el-option label="Approved" value="Approved"></el-option>
        <el-option label="Rejected" value="Rejected"></el-option>
      </el-select>
      <el-button type="info" plain @click="load">query</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">reset</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" plain @click="handleAdd" v-if="data.user.role === 'USER'">Add</el-button>
      <el-button type="danger" plain @click="delBatch">Batch delete</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="img" label="Cover">
          <template #default="scope">
            <el-image style="width: 80px; height: 50px; display: block; border-radius: 5px" :src="scope.row.img" :preview-src-list="[scope.row.img]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="Title" show-overflow-tooltip></el-table-column>
        <el-table-column prop="description" label="Description" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="Content">
          <template #default="scope">
            <el-button size="small" type="primary" @click="view(scope.row.content)">Check content</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Review Status">
          <template v-slot="scope">
            <el-tag type="warning" v-if="scope.row.status === 'Under review'">Under review</el-tag>
            <el-tag type="success" v-if="scope.row.status === 'Approved'">Approved</el-tag>
            <el-tag type="danger" v-if="scope.row.status === 'Rejected'">Rejected</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="Poster"></el-table-column>
        <el-table-column prop="time" label="Time"></el-table-column>
        <el-table-column prop="readCount" label="ReadCount"></el-table-column>
        <el-table-column label="Review" width="180" fixed="right" v-if="data.user.role === 'ADMIN'">
          <template v-slot="scope">
            <el-button type="primary" plain @click="changeStatus(scope.row, 'Approved')" v-if="scope.row.status === 'Under review' || scope.row.status === 'Rejected'">Approve</el-button>
            <el-button type="danger" plain @click="changeStatus(scope.row, 'Rejected')" v-if="scope.row.status === 'Under review' || scope.row.status === 'Approved'">Reject</el-button>
          </template>
        </el-table-column>
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

  <el-dialog title="Post information" v-model="data.formVisible" width="50%" destroy-on-close>
    <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="70px" style="padding:20px">
      <el-form-item prop="img" label="Cover">
        <el-upload
            :action="baseUrl + '/files/upload'"
            :headers="{ 'token': data.user.token }"
            :on-success="handleFileUpload"
            list-type="picture"
        >
          <el-button type="primary">Upload</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item prop="title" label="Title">
        <el-input v-model="data.form.title" placeholder="Please insert title"></el-input>
      </el-form-item>
      <el-form-item prop="description" label="Description">
        <el-input type="textarea" :rows="5" v-model="data.form.description" placeholder="Please insert description"></el-input>
      </el-form-item>

      <el-form-item prop="content" label="Content">
        <div style="border: 1px solid #ccc; width: 100%">
          <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :mode="mode"
          />
          <Editor
              style="height: 500px; overflow-y: hidden;"
              v-model="data.form.content"
              :mode="mode"
              :defaultConfig="editorConfig"
              @onCreated="handleCreated"
          />
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <span slot="footer" class="dialog-footer">
      <el-button @click="data.formVisible = false">cancel</el-button>
      <el-button type="primary" @click="save">confirm</el-button>
    </span>
    </template>
  </el-dialog>

    <el-dialog title="Content" v-model="data.viewVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div class="editor-content-view" style="padding: 20px" v-html="data.content"></div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.viewVisible = false">Close</el-button>
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
import '@wangeditor/editor/dist/css/style.css' // import css
import {onBeforeUnmount, ref, shallowRef} from "vue";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
import { i18nChangeLanguage } from '@wangeditor/editor'


const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  title: null,
  status: null,
  ids: [],
  rules: {
    title: [
      { required: true, message: "Please insert title", trigger: 'blur' }
    ],
    description: [
      { required: true, message: "Please insert description", trigger: 'blur' }
    ]
  },
  content: null
})

const changeStatus = (row, status) => {
  ElMessageBox.confirm('Are you sure to make it ' + status + ' ?','Confirm', {type:"warning"}).then(res =>{
    data.form = JSON.parse(JSON.stringify(row))
    data.form.status = status
    update()
  }).catch(err => {})

}

const formRef = ref()

const baseUrl = import.meta.env.VITE_BASE_URL
const handleFileUpload = (res) => {
  data.form.img = res.data
}

i18nChangeLanguage('en')
/* wangEditor5 init start */
const editorRef = shallowRef()  // Editor instance, must use shallowRef 编辑器实例，必须用 shallowRef
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
// Image upload configuration 图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user.token,
  },
  server: baseUrl + '/files/wang/upload',  // Server-side image upload interface 服务端图片上传接口
  fieldName: 'file'  // Server-side image upload interface parameters 服务端图片上传接口参数
}
/**
 When the component is destroyed, the editor is also destroyed in time, otherwise it may cause memory leaks
 组件销毁时，也及时销毁编辑器，否则可能会造成内存泄漏
*/
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
const handleCreated = (editor) => {
  editorRef.value = editor
}
/* wangEditor5 innit end */

const view = (content) => {
  data.content = content
  data.viewVisible = true
}

const load = ()=> {
  request.get('/forum/selectPage', {
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
      status: data.status,
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
  data.form.userId = data.user.id
  request.post('/forum/add', data.form).then(res =>{
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
  request.put('/forum/update', data.form).then(res => {
    if (res.code === '200'){
      ElMessage.success("Success")
      data.formVisible=false


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
      if (!data.form.img || !data.form.content){
        ElMessage.warning('Please complete all the information')
        return
      }

      data.form.id ? update() : add()
    }
  })
}
const del = (id) =>{
  ElMessageBox.confirm('Are you sure to delete?','Confirm', {type:"warning"}).then(res =>{
    request.delete('/forum/delete/'+ id).then(res => {
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
    request.delete("/forum/delete/batch", {data: data.ids}).then(res =>{
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
  data.status = null
  load()
}
load()
</script>