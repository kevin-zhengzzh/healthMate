<template>
  <div style="width: 50%" class="card">

    <el-form ref="formRef" :rules="data.rules" :model="data.user" label-width="140px" style="padding:20px">
      <el-form-item label="Old password" prop="password">
        <el-input v-model="data.user.password" placeholder="Please input old password" show-password></el-input>
      </el-form-item>
      <el-form-item label="New password" prop="newPassword">
        <el-input v-model="data.user.newPassword" placeholder="Please input new password" show-password></el-input>
      </el-form-item>
      <el-form-item label="Confirm password" prop="confirmPassword">
        <el-input v-model="data.user.confirmPassword" placeholder="Please confirm new password" show-password></el-input>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" @click="updatePassword">Save</el-button>
      </div>
    </el-form>

  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const formRef = ref()
const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('Please confirm password'))
  } else {
    if (value !== data.user.newPassword) {
      callback(new Error("Two inputs don't match!"))
    }
    callback()
  }
}

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  rules: {
    password: [{
      required: true, message: "Please insert old password", trigger: 'blur'},
    ],
    newPassword: [{
      required: true, message: "Please insert new password", trigger: 'blur'},
    ],
    confirmPassword: [
      { validator: validatePass, trigger: 'blur' }
    ]
  }
})

const updatePassword = () => {
  formRef.value.validate(valid => {
    if(valid){
      request.put('/updatePassword', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success("Update successfully")
          logout()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const logout = () => {
  //remove storage
  localStorage.removeItem("xm-user")
  router.push('/login')
}

</script>

<style scoped>

</style>
